LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := hello-ndk-jni 	#编译的目标对象
LOCAL_LDLIBS    :=-L$(SYSROOT)/system/lib -llog -ljnigraphics		#编译指定额外库，"-lxxx"格式,ljnigraphics提供对Java中的 bitmap 对象的操作
LOCAL_SRC_FILES := hello-ndk-jni.c	#编译的源文件

include $(BUILD_SHARED_LIBRARY)
#$(call import-module,android/native_app_glue)