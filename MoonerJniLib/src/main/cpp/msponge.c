#include <jni.h>
#include <android/log.h>
#include <shadowhook.h>
#include "msponge.h"
#include "threads.h"


void *los_alloc_orig = NULL;
void *alloc_internal_with_gc_orig = NULL;
void *throw_out_of_memory_error_orig = NULL;
void *stub = NULL;
int64_t lastAllocLOS = 0;
void *grow_for_utilization_orig = NULL;

void* los = NULL;


bool tryAgainAllocateInternalWithGc = false;
bool findThrowOutOfMemoryError = false;

uint64_t get_num_bytes_allocated(void * los){
    void *handle = shadowhook_dlopen("libart.so");
    void *func = shadowhook_dlsym(handle,
                                  "_ZN3art2gc5space16LargeObjectSpace17GetBytesAllocatedEv");
    uint64_t num_bytes_allocated = ((int (*)(void *)) func)(los);
    __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s,%lu", "num_bytes_allocated ->",
                        num_bytes_allocated);
    return num_bytes_allocated;
}


void *los_alloc_proxy(void *thiz, void *self, size_t num_bytes, size_t *bytes_allocated,
                      size_t *usable_size,
                      size_t *bytes_tl_bulk_allocated) {

    void *largeObjectMap = ((los_alloc) los_alloc_orig)(thiz, self, num_bytes, bytes_allocated,
                                                        usable_size,
                                                        bytes_tl_bulk_allocated);
    los = thiz;
    return largeObjectMap;
}

void call_record_free(void * heap,int64_t freeSize){
    //拦截并跳过本次OutOfMemory，并置标记位
    void *handle = shadowhook_dlopen("libart.so");
    void *func = shadowhook_dlsym(handle, "_ZN3art2gc4Heap10RecordFreeEml");
    ((void (*)(void *, uint64_t, int64_t)) func)(heap, -1, freeSize);
}



void *allocate_internal_with_gc_proxy(void *heap, void *self,
                                      enum AllocatorType allocator,
                                      bool instrumented,
                                      size_t alloc_size,
                                      size_t *bytes_allocated,
                                      size_t *usable_size,
                                      size_t *bytes_tl_bulk_allocated,
                                      void *klass) {
    __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s", "gc 后分配");
    tryAgainAllocateInternalWithGc = false;
    void *object = ((alloc_internal_with_gc_type) alloc_internal_with_gc_orig)(heap, self,
                                                                               allocator,
                                                                               instrumented,
                                                                               alloc_size,
                                                                               bytes_allocated,
                                                                               usable_size,
                                                                               bytes_tl_bulk_allocated,
                                                                               klass);

    // 分配内存为null，且发生了oom
    if (object == NULL && findThrowOutOfMemoryError) {
        // 证明oom后系统进行gc依旧没能找到合适的内存，所以要尝试进行堆清除
        __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s", "分配内存不足，采取堆清除策略");
        tryAgainAllocateInternalWithGc = true;
        object = ((alloc_internal_with_gc_type) alloc_internal_with_gc_orig)(heap, self, allocator,
                                                                             instrumented,
                                                                             alloc_size,
                                                                             bytes_allocated,
                                                                             usable_size,
                                                                             bytes_tl_bulk_allocated,
                                                                             klass);
        // 再次重试后，依旧找不到合适的内存，就直接return，走原来的oom逻辑
        if (object == NULL){
            __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s", "再次分配依旧找不到合适内存 return ");
            return object;
        }
        // 如果当前heap 通过gc后释放了属于largeobjectspace 的空间，此时要进行heap补偿
        if (los != NULL){
            uint64_t currentAllocLOS = get_num_bytes_allocated(los);
            __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s %lu : %lu", "当前数值",currentAllocLOS, lastAllocLOS);
            if (currentAllocLOS < lastAllocLOS){
                call_record_free(heap,currentAllocLOS - lastAllocLOS);
                __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s %lu", "los进行补偿",currentAllocLOS - lastAllocLOS);
            }
        }

        tryAgainAllocateInternalWithGc = false;
    }
    return object;
}



void throw_out_of_memory_error_proxy(void *heap, void *self, size_t byte_count,
                                     enum AllocatorType allocator_type) {
    __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s,%d,%d", "发生了oom ", pthread_gettid_np(pthread_self()), tryAgainAllocateInternalWithGc);
    // 发生了oom，把oom的标志位设置为true
    findThrowOutOfMemoryError = true;
    // 如果当前不是清除堆空间后再引发的oom，则进行堆清除，否则直接oom
    if (!tryAgainAllocateInternalWithGc) {
        __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s", "发生了oom，进行gc拦截");

        if (los != NULL){
            uint64_t currentAlloc = get_num_bytes_allocated(los);
            // 需要考虑多次触发oom的情况，如果当前进行了堆删减，才会进行补偿，否则要把lastAllocLOS设置为0避免产生大额补偿
            if (currentAlloc > lastAllocLOS){
                call_record_free(heap,currentAlloc - lastAllocLOS);
                __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s,%d", "本次增量:",currentAlloc - lastAllocLOS);
                lastAllocLOS = currentAlloc;
                return;
            } else{
                lastAllocLOS = 0;
            }
        }

    }
    //如果不允许拦截，则直接调用原函数，抛出OOM异常
    __android_log_print(ANDROID_LOG_ERROR, MSPONGE_TAG, "%s", "oom拦截失效");
    // 这里要设置成fasle，因为throw_out_of_memory_error方法会多次再尝试gc，避免多次走到重新尝试分配流程
    findThrowOutOfMemoryError = false;
    ((out_of_memory) throw_out_of_memory_error_orig)(heap, self, byte_count, allocator_type);
}


void
grow_for_utilization_proxy(void *heap, void *collector_ran, uint64_t bytes_allocated_before_gc) {
    ((grow_for_utilization) grow_for_utilization_orig)(heap, collector_ran, 0);
}


JNIEXPORT void JNICALL
Java_com_pika_mooner_1core_Mooner_memorySponge(JNIEnv *env, jobject thiz) {

    shadowhook_hook_sym_name(
            "libart.so",
            "_ZN3art2gc5space13FreeListSpace5AllocEPNS_6ThreadEmPmS5_S5_",
            (void *) los_alloc_proxy,
            (void **) &los_alloc_orig);


    //AllocateInternalWithGcE
    shadowhook_hook_sym_name(
            "libart.so",
            "_ZN3art2gc4Heap22AllocateInternalWithGcEPNS_6ThreadENS0_13AllocatorTypeEbmPmS5_S5_PNS_6ObjPtrINS_6mirror5ClassEEE",
            (void *) allocate_internal_with_gc_proxy,
            (void **) &alloc_internal_with_gc_orig);

    shadowhook_hook_sym_name(
            "libart.so",
            "_ZN3art2gc4Heap21ThrowOutOfMemoryErrorEPNS_6ThreadEmNS0_13AllocatorTypeE",
            (void *) throw_out_of_memory_error_proxy,
            (void **) &throw_out_of_memory_error_orig);

    shadowhook_hook_sym_name(
            "libart.so",
            "_ZN3art2gc4Heap18GrowForUtilizationEPNS0_9collector16GarbageCollectorEm",
            (void *) grow_for_utilization_proxy,
            (void **) &grow_for_utilization_orig);


    int error_num = shadowhook_get_errno();
    const char *error_msg1 = shadowhook_to_errmsg(error_num);
    __android_log_print(ANDROID_LOG_WARN, MSPONGE_TAG, "hook return: %d - %s", error_num,
                        error_msg1);


}