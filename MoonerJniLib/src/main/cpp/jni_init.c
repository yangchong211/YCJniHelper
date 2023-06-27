
#include <android/log.h>
#include <unistd.h>
#include <sys/eventfd.h>
#include <pthread.h>
#include <jni.h>
#include "errno.h"
#include "jni_init.h"

JavaVM *currentVm = NULL;
jclass callClass = NULL;



JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    currentVm = vm;
    jclass cls;
    if (NULL == vm) return -1;
    if (JNI_OK != (*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_6)) return -1;
    if (NULL == (cls = (*env)->FindClass(env, HACKER_JNI_CLASS_NAME))) return -1;

    // 此时的cls仅仅是一个局部变量，如果错误引用会出现错误
    callClass = (*env)->NewGlobalRef(env, cls);
    return JNI_VERSION_1_6;
}



JNIEXPORT void JNICALL JNI_OnUnLoad(JavaVM* jvm, void* reserved)
{
    JNIEnv* env;
    if ((*jvm)->GetEnv(jvm,(void**) &env, HACKER_JNI_VERSION) != JNI_OK) {
        return;
    }

    if (callClass != NULL) {
        (*env)->DeleteWeakGlobalRef(env,callClass);
        callClass = NULL;
    }
}

