#include <jni.h>
#include <string>
#include "testjnilib.h"

//java中stringFromJNI
extern "C" JNIEXPORT jstring JNICALL
//以注意到jni的取名规则，一般都是包名 + 类名，jni方法只是在前面加上了Java_，并把包名和类名之间的.换成了_
Java_com_yc_testjnilib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    //思考一下，为什么直接返回字符串会出现错误提示？
    //return "hello";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_yc_testjnilib_NativeLib_getMd5(JNIEnv *env, jobject thiz, jstring str) {
    return env->NewStringUTF("哈哈哈，逗比");
}

/**
 * 初始化操作
 */
__attribute__((section(JNI_SECTION))) JNICALL void initLib(JNIEnv *env, jobject obj, jstring version) {
    printf("初始化: 初始化操作1", version);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_yc_testjnilib_NativeLib_initLib(JNIEnv *env, jobject thiz, jstring version) {
    printf("初始化: 初始化操作2", version);
}









//JNIEnv是什么？
//JNIEnv代表Java调用native层的环境，一个封装了几乎所有的JNI方法的指针。其只在创建它的线程有效，不能跨线程传递，不同的线程的JNIEnv彼此独立。
//native 环境中创建的线程，如果需要访问JNI，必须调用AttachCurrentThread 进行关联，然后使用DetachCurrentThread 解除关联。


//JNI动态注册案例学习
//动态注册其实就是使用到了前面分析的so加载原理：在最后一步的JNI_OnLoad中注册对应的jni方法。这样在类加载的过程中就可以自动注册native函数。
//java路径
#define JNI_CLASS_NAME "com/yc/testjnilib/NativeLib"
static JNINativeMethod gMethods[] = {
        {"stringFromJNI","()Ljava/lang/String;",
         (void *)Java_com_yc_testjnilib_NativeLib_stringFromJNI
         },
};

int register_dynamic_Methods(JNIEnv *env){
    std::string s = JNI_CLASS_NAME;
    const char* className = s.c_str();
    jclass clazz = env->FindClass(className);
    if(clazz == NULL){
        return JNI_FALSE;
    }
    //注册JNI方法
    //核心方法：RegisterNatives，jni注册native方法。
    if(env->RegisterNatives(clazz,gMethods,sizeof(gMethods)/sizeof(gMethods[0]))<0){
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

//类加载时会调用到这里
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if(vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK){
        return JNI_ERR;
    }
    assert(env != NULL);
    if(!register_dynamic_Methods(env)){
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}

//native层异常
int test_error1(JNIEnv *env){
    /*检测是否有异常*/
    jboolean hasException = env->ExceptionCheck();
    if(hasException == JNI_TRUE){
        //处理方式1：native层自行处理
        //打印异常，同Java中的printExceptionStack;
        env->ExceptionDescribe();
        //清除当前异常
        env->ExceptionClear();

        /*方式2：抛出异常给上面，让Java层去捕获*/
        jclass noFieldClass = env->FindClass("java/lang/Exception");
        std::string msg("fieldName");
        std::string header = "找不到该字段";
        env->ThrowNew(noFieldClass,header.append(msg).c_str());
        //env->ReleaseStringUTFChars(fieldName,_fieldName);
        return 0;

    }
    return 0;
}
