
#include <jni.h>

#define HACKER_JNI_CLASS_NAME "com/pika/mooner_core/Mooner"
#define HACKER_JNI_ERROR_HANDLER "onError"
#define HACKER_JNI_VERSION JNI_VERSION_1_6

extern JavaVM  *currentVm;
extern jclass callClass ;