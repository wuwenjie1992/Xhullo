#include <stdio.h>
#include <string.h>
#include <jni.h>

#define LOG_TAG "hello-ndk-jni" //自定义的变量，相当于logcat函数中的tag
#undef LOG
#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__))

jstring Java_wo_wocom_xwell_XAplasma_stringFromNDKJNI( JNIEnv* env,jobject thiz ){

   return (*env)->NewStringUTF(env, "well,i from hello-ndk-jni!!!");

}

void  Java_wo_wocom_xwell_XAplasma_printLOGI(JNIEnv * env, jobject jobj){

	LOGI("LOGI\n");
}

