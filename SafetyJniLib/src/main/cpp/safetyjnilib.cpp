#include <jni.h>
#include <string>

//
// Created by 杨充 on 2023/6/7.
//

extern "C"
JNIEXPORT jstring JNICALL
Java_com_yc_safetyjni_SafetyJniLib_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++ , yc";
    return env->NewStringUTF(hello.c_str());
}