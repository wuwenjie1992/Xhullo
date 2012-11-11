#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <time.h>

#define LOG_TAG "hello-ndk-jni" //自定义的变量，相当于logcat函数中的tag
#undef LOG
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__))	//宏定义

int currentTimeMillis(){
	time_t  timer_null = time(NULL);//得到从（一般是19700101午夜）到当前时间的秒数
	return timer_null;
}

//返回一个字符串
jstring Java_wo_wocom_xwell_XAplasma_stringFromNDKJNI(JNIEnv* env,jobject thiz ){
   return (*env)->NewStringUTF(env, "well,hello-ndk-jni!!!");
   //Constructs a new java.lang.String object from an array of UTF-8 characters.
}

//logcat
void  Java_wo_wocom_xwell_XAplasma_printLOGI(JNIEnv * env, jobject jobj){
	LOGI("Java_wo_wocom_xwell_XAplasma_printLOGI\n");
}

//返回秒数
jint Java_wo_wocom_xwell_XAplasma_currentTimeMillis(JNIEnv* env,jobject thiz){
	return currentTimeMillis();
}


