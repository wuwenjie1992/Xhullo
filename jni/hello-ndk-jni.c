#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <time.h>
#include <android/bitmap.h>
#include <math.h>

#define LOG_TAG "hello-ndk-jni" //自定义的变量，相当于logcat函数中的tag
#undef LOG
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__))	//宏定义
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))

int currentTimeMillis() {
	time_t timer_null = time(NULL); //得到从（一般是19700101午夜）到当前时间的秒数
	return timer_null;
}

//返回一个字符串
jstring Java_wo_wocom_xwell_XAplasma_stringFromNDKJNI(JNIEnv* env, jobject thiz) {
	return (*env)->NewStringUTF(env, "well,hello-ndk-jni!!! 李泰珉");
	//Constructs a new java.lang.String object from an array of UTF-8 characters.
}

//logcat
void Java_wo_wocom_xwell_XAplasma_printLOGI(JNIEnv * env, jobject jobj) {
	LOGI("Java_wo_wocom_xwell_XAplasma_printLOGI\n");
}

//返回秒数
jint Java_wo_wocom_xwell_XAplasma_currentTimeMillis(JNIEnv* env, jobject thiz) {
	return currentTimeMillis();
}

/* http://www.ibm.com/developerworks/cn/opensource/tutorials/os-androidndk/index.html
 * convertToGray 		参数：颜色Bitmap，二是灰度版本填充的Bitmap。
 * changeBrightness 	一是up或down的整数。二是逐个像素进行修改的Bitmap
 * findEdges 			一是灰度Bitmap，二是接收图像“仅显示边缘”的Bitmap。
 */

/* 图像数据表示为uint8_t，表示未带符号的8位值，每字节值的范围0至255 */
typedef struct {
	uint8_t alpha;
	uint8_t red;
	uint8_t green;
	uint8_t blue;
} argb; // 3个未带符号的8位值集合表示24位图像的图像数据的一个像素

/*
 convertToGray
 Pixel operation
 此函数有两个调用 Java 代码的参数：
 ARGB 格式中的彩色 Bitmap 和接收彩色图像的灰度版本的 8 位灰度 Bitmap

 char带符号的8位值，char指针(char *)支持您引用8位值，并通过该指针执行操作

 完成图像设计处理各行数据和在列之前移动。
 Bitmap 结构包含名为“跨距”的成员。
 跨距表示单个图像数据行的宽度
 （以字节为单位）。
 例如，带有 alpha 通道的 24 位彩色图像
 每个像素为 32 位或 4 字节。
 因此，宽度为 320 像素的图像，其跨距为 320*4 或 1,280 字节。
 8 位灰度图像的每个像素为 8 位或 1 字节。
 宽度为 320 像素的灰度图像，其跨距为 320*1 或 320 字节。
 */

//convertToGray(bitmapOrig, bitmapWip);bitmapOrig 保存原始彩色图像;bitmapGray 保存图像的灰度副本
JNIEXPORT void JNICALL Java_wo_wocom_xwell_XAplasma_convertToGray(JNIEnv* env, jobject  obj, jobject bitmapcolor,jobject bitmapgray){

	AndroidBitmapInfo  infocolor;//bitmap.h中定义，有助了解Bitmap对象
	void*  pixelscolor;	//pixelscolor 的指针引用图像数据的基准地址
	AndroidBitmapInfo  infogray;
	void* pixelsgray;	//pixelsgray 的指针引用图像数据的基准地址
	int  ret,y,x;

	/*typedef struct {
    	uint32_t    width;
    	uint32_t    height;
    	uint32_t    stride;	//Stride:步幅  每行字节数
    	int32_t     format;
    	uint32_t    flags;      // 0 for now
		} AndroidBitmapInfo;
	 */

	    LOGI("convertToGray");
	    //AndroidBitmap_getInfo 函数，在 jnigraphics 库中，获取有关具体 Bitmap 对象的信息
	    if ((ret = AndroidBitmap_getInfo(env, bitmapcolor, &infocolor)) < 0) {
	        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
	        return;
	    }

	    if ((ret = AndroidBitmap_getInfo(env, bitmapgray, &infogray)) < 0) {
	        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
	        return;
	    }


	    LOGI("color img:width %d;height %d;stride %d;format %d;flags %d",infocolor.width,infocolor.height,infocolor.stride,infocolor.format,infocolor.flags);
	    if (infocolor.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
	        LOGE("Bitmap format is not RGBA_8888 !");
	        return;
	    }

	    /*enum AndroidBitmapFormat {
    			ANDROID_BITMAP_FORMAT_NONE      = 0,
    			ANDROID_BITMAP_FORMAT_RGBA_8888 = 1,
    			ANDROID_BITMAP_FORMAT_RGB_565   = 4,
    			ANDROID_BITMAP_FORMAT_RGBA_4444 = 7,
    			ANDROID_BITMAP_FORMAT_A_8       = 8,
			};
	     */

	    LOGI("gray img:width %d;height %d;stride %d;format %d;flags %d",infogray.width,infogray.height,infogray.stride,infogray.format,infogray.flags);
	    if (infogray.format != ANDROID_BITMAP_FORMAT_A_8) {
	        LOGE("Bitmap format is not A_8 !");
	        return;
	    }

	    //对像素缓存上锁，即获得该缓存的指针，可以直接在数据上执行操作
	    if ((ret = AndroidBitmap_lockPixels(env, bitmapcolor, &pixelscolor)) < 0) {
	        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	    }
	    //对像素缓存上锁，即获得该缓存的指针
	    if ((ret = AndroidBitmap_lockPixels(env, bitmapgray, &pixelsgray)) < 0) {
	        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	    }

	    //处理算法processing algorithm

	    //使用infocolor.height值获得行的计数
	    //每迭代一行，指针就设置对应于行的图像数据第一“列”的存储位置。
	    //当您迭代某一行的列时，就将彩色数据的每个像素转换为表示灰度值的单个值
	    //当转换了完成行时，需要将指针指向下一行,通过跳过跨距值来完成
	    for(y=0;y<infocolor.height;y++) {
	    		argb * line =(argb *) pixelscolor;//定义argb的结构体指针
	    		uint8_t * grayline = (uint8_t *) pixelsgray;
	    		for(x=0;x<infocolor.width;x++) {
	    			grayline[x]=0.3*line[x].red+0.59*line[x].green+0.11*line[x].blue;
	    		}
	    	pixelscolor=(char *)pixelscolor+infocolor.stride;//void* pixelscolor指针;stride步幅  每行字节数//指针指向下一行
	    	pixelsgray =(char *)pixelsgray +infogray.stride;//AndroidBitmapInfo  infogray
	    }

	    LOGI("unlocking pixels");
	    AndroidBitmap_unlockPixels(env, bitmapcolor);//解锁，解锁之前锁定的像素数据
	    AndroidBitmap_unlockPixels(env, bitmapgray);
}

/*
 changeBrightness
 Pixel Operation

 函数需灰度位图。已修改了传入的图像
 函数每次对每个像素增加或减少5
 像素值被限制为 0 至 255
 例如，当我使用 changeBrightness函数为一个值
 （比如 252）增加了 5，
 而最终得到的实际有效值却是 2
 得到的效果很有意思，但并不是我想要的
 这就是我使用名为 v 的整数
 并将像素数据映射到带符号整数
 然后再将该值与 0 和 255 比较的原因
 */
JNIEXPORT void JNICALL Java_wo_wocom_xwell_XAplasma_changeBrightness(JNIEnv
	* env, jobject obj, int direction,jobject bitmap)
{
	AndroidBitmapInfo  infogray;
	void* pixelsgray;
	int ret,y,x;
	uint8_t save;

	if ((ret = AndroidBitmap_getInfo(env, bitmap, &infogray)) < 0) {
	        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
	        return;
	    }

	LOGI("gray img:width %d; height %d; stride %d; format %d;flags %d",infogray.width,infogray.height,infogray.stride,infogray.format,infogray.flags);
	if (infogray.format != ANDROID_BITMAP_FORMAT_A_8) {
	        LOGE("Bitmap format is not A_8 !");
	        return;
	    }

	if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixelsgray)) < 0) {
	        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
		}

	    // modify pixels with image processing algorithm
	    LOGI("time to modify pixels....");
	    for (y=0;y<infogray.height;y++) {
	    	uint8_t * grayline = (uint8_t *) pixelsgray;
	    	int v;
	    	for (x=0;x<infogray.width;x++) {

	    			v = (int) grayline[x];
	    			if (direction == 1) v-=5;
	    			else v+= 5;
	    		//
	    			if (v >= 255) {grayline[x] = 255;}
	    			else if (v <= 0) {grayline[x] = 0;}
	    			else {grayline[x] = (uint8_t) v;}
	    		}

	    	pixelsgray = (char *)pixelsgray+infogray.stride;//指针指向下一行
	    }
	    AndroidBitmap_unlockPixels(env, bitmap);//解锁
}

/*
 findEdges
 Sobel边缘检测算法
 Sobel算子 3*3邻域
	灰度矩阵 * Sobel算子

 bitmapgray 灰度bitmap  输入
 bitmapedges 边界bitmap	输出

 参考：http://baike.baidu.com/view/676368.htm
 	 	 http://zh.wikipedia.org/wiki/Sobel%E7%AE%97%E5%AD%90
 	 	 http://blog.csdn.net/tianhai110/article/details/5663756
 	 	 http://zh.wikipedia.org/wiki/%E8%BE%B9%E7%BC%98%E6%A3%80%E6%B5%8B
 */
JNIEXPORT void JNICALL Java_wo_wocom_xwell_XAplasma_findEdges(JNIEnv * env, jobject obj, jobject bitmapgray,jobject bitmapedges)
{
	AndroidBitmapInfo infogray;
	void* pixelsgray;
	AndroidBitmapInfo infoedges;
	void* pixelsedge;
	int ret,y,x;
	int sumX,sumY,sum;
	int i,j;
	int Gx[3][3];//纵向算子
	int Gy[3][3];//横向算子
	uint8_t *graydata;//指针
	uint8_t *edgedata;//指针

	/*typedef struct {
	    	uint32_t    width;
	    	uint32_t    height;
	    	uint32_t    stride;	//Stride:步幅  每行字节数
	    	int32_t     format;
	    	uint32_t    flags;      // 0 for now
			} AndroidBitmapInfo;
		 */

	LOGI("findEdges running");

	Gx[0][0] = -1;Gx[0][1] = 0;Gx[0][2] = 1;//Sobel卷积因子 纵向
	Gx[1][0] = -3;Gx[1][1] = 0;Gx[1][2] = 3;	//	-1 0 1
	Gx[2][0] = -1;Gx[2][1] = 0;Gx[2][2] = 1;	//	-2 0 2
													//	-1  0 1
	Gy[0][0] = 1;Gy[0][1] = 3;Gy[0][2] = 1;//横向
	Gy[1][0] = 0;Gy[1][1] = 0;Gy[1][2] = 0;
	Gy[2][0] =-1;Gy[2][1] =-3;Gy[2][2] =-1;

	if ((ret = AndroidBitmap_getInfo(env, bitmapgray, &infogray)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed error=%d", ret);
		return;
	}

	if ((ret = AndroidBitmap_getInfo(env, bitmapedges, &infoedges)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed error=%d", ret);
		return;
	}

	LOGI("gray img: width %d;height %d;stride %d;format %d;flags %d",infogray.width,infogray.height,infogray.stride,infogray.format,infogray.flags);
	if (infogray.format != ANDROID_BITMAP_FORMAT_A_8) {
		LOGE("Bitmap format is not A_8 !");
		return;
	}

	LOGI("color img:width %d;height %d;stride %d;format %d;flags %d",infoedges.width,infoedges.height,infoedges.stride,infoedges.format,infoedges.flags);
		if (infoedges.format != ANDROID_BITMAP_FORMAT_A_8) {
			LOGE("Bitmap format is not A_8 !");
			return;
		}

		if ((ret = AndroidBitmap_lockPixels(env, bitmapgray,&pixelsgray)) < 0) {
			LOGE("AndroidBitmap_lockPixels() failed error=%d",ret);
		}

		if ((ret = AndroidBitmap_lockPixels(env, bitmapedges,&pixelsedge)) < 0) {
			LOGE("AndroidBitmap_lockPixels() failed error=%d",ret);
		}

		// modify pixels with image processing algorithm

		LOGI("time to modify pixels....");

		graydata = (uint8_t *) pixelsgray;//灰度bitmap的指针；void* pixelsgray;
		edgedata = (uint8_t *) pixelsedge;//uint8_t *graydata;AndroidBitmapInfo infogray;

		for (y=0;y<=infogray.height - 1;y++) {
			for (x=0;x<infogray.width -1;x++) {
				sumX = 0;
				sumY = 0;
				// 边界检测 check boundaries
				if (y== 0|| y== infogray.height-1) {sum = 0;}
				else if(x == 0||x== infogray.width-1){sum = 0;}
				else {
						// calc X gradient
						for (i=-1;i<=1;i++) {
							for (j=-1;j<=1;j++) {
								sumX += (int)( (*(graydata+x+i+(y+j)*infogray.stride))*Gx[i+1][j+1] );
								//sumX+= 图像某点灰度值*算子
								//Stride:步幅，每行字节数 (y+j)*infogray.stride 高度*每行字节数=已经计算的行数的字节数
								//x 已计算此行字节数；graydata：灰度bitmap的指针（地址）
								//(graydata+x+i+(y+j)*infogray.stride) //当前应计算的【灰度bitmap的指针】；*：取值
							}
						}

						// calc Y gradient
						for (i=-1;i<=1;i++) {
							for (j=-1;j<=1;j++) {
								sumY += (int)((*(graydata+x+i+(y+j)*infogray.stride))*Gy[i+1][j+1]);
								}
							}

						sum = sqrt(sumX*sumX+sumY*sumY);//sum = abs(sumX) + abs(sumY);
						//图像的每一个像素的横向及纵向梯度近似值
					}

		if (sum>255) sum = 255;//不能超出边界
		if (sum<0) sum = 0;

		*(edgedata+x+y*infogray.width) = 255-(uint8_t)sum;//对边界bitmap赋值；历遍每个像素
	}//横向的某一行完成
}//infogray.height行完成

		AndroidBitmap_unlockPixels(env, bitmapgray);//解锁
		AndroidBitmap_unlockPixels(env, bitmapedges);

}
