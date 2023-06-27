
#include <jni.h>
#include "mooner_exception.h"
#include "jni_init.h"

void handle_exception(JNIEnv *env) {
    jclass main = (*env)->FindClass(env, HACKER_JNI_CLASS_NAME);
    jmethodID id = (*env)->GetStaticMethodID(env, main, HACKER_JNI_ERROR_HANDLER, "()V");
    (*env)->CallStaticVoidMethod(env, main, id);
}