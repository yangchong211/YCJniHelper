#include <android/log.h>
#include <dlfcn.h>
#include <fcntl.h>
#include <inttypes.h>
#include <jni.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <unistd.h>
#include <setjmp.h>
#include "bytehook.h"
#include "jni_init.h"
#include "mooner_exception.h"



#define HACKER_JNI_HANDLER "onHandleSignal"
#define SIGNAL_CRASH_STACK_SIZE (1024 * 128)
#define TAG "mooner"


static sigjmp_buf sig_env;
static volatile int handleFlag = 0;
static JNIEnv *currentEnv;
static struct sigaction old;

// 原本线程参数
struct ThreadHookeeArgus {
    void *(*current_func)(void *);

    void *current_arg;
};

static void *pthread(void *arg) {
    struct ThreadHookeeArgus *temp = (struct ThreadHookeeArgus *) arg;
    if (sigsetjmp(sig_env, 1)) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "%s", "crash 了，但被我抓住了");
        JavaVMAttachArgs vmAttachArgs;
        vmAttachArgs.version = HACKER_JNI_VERSION;
        vmAttachArgs.name = NULL;
        vmAttachArgs.group = NULL;
        jint attachRet = (*currentVm)->AttachCurrentThread(currentVm, (JNIEnv **) &currentEnv, &vmAttachArgs);
        // 现在处于native子线程，默认是booster加载器
        jmethodID id = (*currentEnv)->GetStaticMethodID(currentEnv, callClass,HACKER_JNI_HANDLER, "()V");
        (*currentEnv)->CallStaticVoidMethod(currentEnv, callClass, id);
    } else {
        temp->current_func(temp->current_arg);
    }
    handleFlag = 0;
}

// pthread_create 的定义
typedef int (*pthread_create_define)(const pthread_t *, const pthread_attr_t *,
                                     void *(void *), void *);

static int pthread_create_auto(pthread_t *thread, pthread_attr_t *attr,
                               void *(*start_routine)(void *), void *arg) {
    struct ThreadHookeeArgus *params;
    params = (struct ThreadHookeeArgus *) malloc(sizeof(struct ThreadHookeeArgus));
    params->current_func = start_routine;
    params->current_arg = arg;
    handleFlag = 1;
    __android_log_print(ANDROID_LOG_INFO, TAG, "%s", "call pthread_create");
    int fd = BYTEHOOK_CALL_PREV(pthread_create_auto, pthread_create_define, thread, attr, pthread,
                                (void *) params);
    BYTEHOOK_POP_STACK();
    return fd;
}

static void sig_handler(int sig, struct siginfo *info, void *ptr) {
    if (handleFlag == 1) {
        siglongjmp(sig_env, 1);
    } else {
        // 交给原来的信号处理器处理
        sigaction(sig, &old, NULL);
    }
}




JNIEXPORT void JNICALL
Java_com_pika_mooner_1core_Mooner_preventPthreadCrash(JNIEnv *env, jobject thiz, jstring so_name,
                                               jint signal) {
    jclass clazz = (*env)->FindClass(env,HACKER_JNI_CLASS_NAME);
    callClass= (*env)->NewGlobalRef(env,clazz);


    void *open_proxy;
    open_proxy = (void *) pthread_create_auto;
    const char * hook_so_name = (*env)->GetStringUTFChars(env,so_name,0);
    bytehook_hook_single(hook_so_name, NULL, "pthread_create", open_proxy, NULL,
                         NULL);
    do {
        stack_t ss;
        if (NULL == (ss.ss_sp = calloc(1, SIGNAL_CRASH_STACK_SIZE))) {
            handle_exception(env);
            break;
        }
        ss.ss_size = SIGNAL_CRASH_STACK_SIZE;
        ss.ss_flags = 0;
        if (0 != sigaltstack(&ss, NULL)) {
            handle_exception(env);
            break;
        }
        struct sigaction sigc;
        sigc.sa_sigaction = sig_handler;
        sigemptyset(&sigc.sa_mask);
        // 推荐采用SA_RESTART 虽然不是所有系统调用都支持，被中断后重新启动，但是能覆盖大部分
        sigc.sa_flags = SA_SIGINFO | SA_ONSTACK | SA_RESTART;
        int flag = sigaction(signal, &sigc, &old);
        if (flag == -1) {
            handle_exception(env);
            break;
        }
    } while (0);

    (*env)->ReleaseStringUTFChars(env,so_name,hook_so_name);

}

