#include <jni.h>
#include <string>
#include "testjnilib.h"

//java中stringFromJNI
extern "C" JNIEXPORT jstring JNICALL
//以注意到jni的取名规则，一般都是包名 + 类名，jni方法只是在前面加上了Java_，并把包名和类名之间的.换成了_
Java_com_yc_testjnilib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
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