#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_mymusicapp_presentation_activity_MainActivity_stringFromJNI(JNIEnv *env,
                                                                             jobject thiz) {
    return env->NewStringUTF("test");
}