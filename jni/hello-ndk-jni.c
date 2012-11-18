#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <time.h>
#include <android/bitmap.h>

#define LOG_TAG "hello-ndk-jni" //自定义的变量，相当于logcat函数中的tag
#undef LOG
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__))	//宏定义
#define LOGE(...) ((void) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))

int currentTimeMillis() {
	time_t timer_null = time(NULL); //得到从（一般是19700101午夜）到当前时间的秒数
	return timer_null;
}

//返回一个字符串
jstring Java_wo_wocom_xwell_XAplasma_stringFromNDKJNI(JNIEnv* env, jobject thiz) {
	return (*env)->NewStringUTF(env, "well,hello-ndk-jni!!!");
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

/*http://www.ibm.com/developerworks/cn/opensource/tutorials/os-androidndk/index.html
 * convertToGray 参数。第一个是颜色 Bitmap， 第二个是使用第一个参数的灰度版本填充的 Bitmap。
 changeBrightness 第一个是表示 up 或 down 的整数。第二个是逐个像素进行修改的 Bitmap。
 findEdges 两个参数。第一个是灰度 Bitmap，第二个是接收图像“仅显示边缘”版本的 Bitmap。
 */


typedef struct {
	uint8_t alpha;
	uint8_t red;
	uint8_t green;
	uint8_t blue;
} argb;

/*
 convertToGray
 Pixel operation
 此函数有两个调用 Java 代码的参数：
 ARGB 格式中的彩色 Bitmap 和接收彩色图像的灰度版本的 8 位灰度 Bitmap。

 指针 fun

 以 C 语言编写的图像处理应用程序通常涉及指针的使用。指针是“指向”存储地址的变量。
 变量的数据类型指定您使用的存储类型和大小。
 例如，char 表示带符号的 8 位值，因此 char 指针 (char *) 支持您引用 8 位值，
 并通过该指针执行操作。
 图像数据表示为 uint8_t，这表示未带符号的 8 位值，其中每个字节的值范围为 0 至 255。
 3 个未带符号的 8 位值集合表示 24 位图像的图像数据的一个像素。

 完成图像设计处理各行数据和在列之前移动。Bitmap 结构包含名为“跨距”的成员。
 跨距表示单个图像数据行的宽度（以字节为单位）。
 例如，带有 alpha 通道的 24 位彩色图像每个像素为 32 位或 4 字节。
 因此，宽度为 320 像素的图像，其跨距为 320*4 或 1,280 字节。
 8 位灰度图像的每个像素为 8 位或 1 字节。
 宽度为 320 像素的灰度图像，其跨距为 320*1 或 320 字节。
 */

//convertToGray(bitmapOrig, bitmapWip);bitmapOrig 保存原始彩色图像;bitmapGray 保存图像的灰度副本
JNIEXPORT void JNICALL Java_wo_wocom_xwell_XAplasma_convertToGray(JNIEnv* env, jobject  obj, jobject bitmapcolor,jobject bitmapgray){

	AndroidBitmapInfo  infocolor;//在 bitmap.h 中定义，有助于了解 Bitmap 对象。
	void*  pixelscolor;	//pixelscolor 的指针引用图像数据的基准地址
	AndroidBitmapInfo  infogray;
	void* pixelsgray;	//pixelsgray 的指针引用图像数据的基准地址。
	int  ret;
	int y;
	int x;

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


	    LOGI("color image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infocolor.width, infocolor.height, infocolor.stride, infocolor.format, infocolor.flags);
	    if (infocolor.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
	        LOGE("Bitmap format is not RGBA_8888 !");
	        return;
	    }

	    LOGI("gray image :: width is %d; height is %d; stride is %d; format is %d;flags is %d", infogray.width, infogray.height, infogray.stride, infogray.format, infogray.flags);
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

	    // 处理算法processing algorithm

	    //使用 infocolor.height 值获得行的计数。
	    //每迭代一行，指针就设置对应于行的图像数据第一“列”的存储位置。
	    //当您迭代某一行的列时，就将彩色数据的每个像素转换为表示灰度值的单个值。
	    //当转换了完成行时，您需要将指针指向下一行。这通过跳过跨距值来完成。
	    for (y=0;y<infocolor.height;y++) {
	    	argb * line = (argb *) pixelscolor;
	    	uint8_t * grayline = (uint8_t *) pixelsgray;
	    	for (x=0;x<infocolor.width;x++) {
	    		grayline[x] =0.3*line[x].red +0.59*line[x].green+0.11*line[x].blue;
	    	}
	    	pixelscolor = (char *)pixelscolor + infocolor.stride;
	    	pixelsgray = (char *) pixelsgray + infogray.stride;//AndroidBitmapInfo  infogray
	    }

	    LOGI("unlocking pixels");
	    AndroidBitmap_unlockPixels(env, bitmapcolor);//解锁，解锁之前锁定的像素数据
	    AndroidBitmap_unlockPixels(env, bitmapgray);

}

/*
 changeBrightness
 Pixel Operation

 此函数仅需要一个灰度位图。已修改了传入的图像。
 此函数每次对每个像素增加或减少 5。此常量可以更改。我使用 5 是因为每次进行时会明显更改图像，
 无需总是按加号或减号按钮。
 像素值被限制为 0 至 255。在直接使用未带符号的变量进行这些操作时要小心，因为很容易会“越界”。
 例如，当我使用 changeBrightness 函数为一个值（比如 252）增加了 5，
 而最终得到的实际有效值却是 2。
 得到的效果很有意思，但并不是我想要的。这就是我使用名为 v 的整数，
 并将像素数据映射到带符号整数，然后再将该值与 0 和 255 比较的原因。

 还有一个图像处理算法需要检查：findEdges 函数，它的工作方式与之前的两个像素导向的函数有些不同。
 清单 7 展示了 findEdges 函数。
 */
JNIEXPORT void JNICALL Java_wo_wocom_xwell_XAplasma_changeBrightness(JNIEnv
	* env, jobject obj, int direction,jobject bitmap)
{
	AndroidBitmapInfo  infogray;
	void* pixelsgray;
	int  ret;
	int y;
	int x;
	uint8_t save;

	if ((ret = AndroidBitmap_getInfo(env, bitmap, &infogray)) < 0) {
	        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
	        return;
	    }

	LOGI("gray image :: width is %d; height is %d; stride is %d; format is %d;flags is %d", infogray.width, infogray.height, infogray.stride, infogray.format, infogray.flags);
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

	    		if (direction == 1)
	    			v -=5;
	    		else
	    			v += 5;
	    		if (v >= 255) {
	    			grayline[x] = 255;
	    		} else if (v <= 0) {
	    			grayline[x] = 0;
	    		} else {
	    			grayline[x] = (uint8_t) v;
	    		}
	    	}

	    	pixelsgray = (char *) pixelsgray + infogray.stride;
	    }

	    AndroidBitmap_unlockPixels(env, bitmap);

}

/*
 findEdges
 Matrix operation

 findEdges 例程与之前的两个函数有很多相同之处：

 与 convertToGray 函数一样，此函数有两个位图参数，
 但是在本例中，两个位图参数都是灰度图像。
 询问了位图以保证它们为所需的格式。
 会相应地锁定和解锁位图像素。
 该算法迭代源图像的行和列。

 与之前的两个函数不同，此函数将每个像素与其周围的像素进行比较，
 而不是简单地对像素值本身进行数学操作。
 此函数中实现的算法是 Sobel Edge Detection 算法的变体。
 在本实现中，我将每个像素与其周围的一个像素进行了比较。
 此算法和其他算法的变体可使用更大的“边界”获得不同的结果。
 将每个像素与其周围的像素进行比较突出了像素之间的对比度，
 而且这样做强调了“边缘”。

 */
JNIEXPORT void JNICALL Java_wo_wocom_xwell_XAplasma_findEdges(JNIEnv * env, jobject obj, jobject bitmapgray,jobject bitmapedges)
{
	AndroidBitmapInfo infogray;
	void* pixelsgray;
	AndroidBitmapInfo infoedges;
	void* pixelsedge;
	int ret;
	int y;
	int x;
	int sumX,sumY,sum;
	int i,j;
	int Gx[3][3];
	int Gy[3][3];
	uint8_t *graydata;
	uint8_t *edgedata;

	LOGI("findEdges running");

	Gx[0][0] = -1;Gx[0][1] = 0;Gx[0][2] = 1;
	Gx[1][0] = -2;Gx[1][1] = 0;Gx[1][2] = 2;
	Gx[2][0] = -1;Gx[2][1] = 0;Gx[2][2] = 1;

	Gy[0][0] = 1;Gy[0][1] = 2;Gy[0][2] = 1;
	Gy[1][0] = 0;Gy[1][1] = 0;Gy[1][2] = 0;
	Gy[2][0] = -1;Gy[2][1] = -2;Gy[2][2] = -1;

	if ((ret = AndroidBitmap_getInfo(env, bitmapgray, &infogray)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
		return;
	}

	if ((ret = AndroidBitmap_getInfo(env, bitmapedges, &infoedges)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
		return;
	}

	LOGI("gray image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infogray.width,infogray.height,infogray.stride,infogray.format,infogray.flags);
	if (infogray.format != ANDROID_BITMAP_FORMAT_A_8) {
		LOGE("Bitmap format is not A_8 !");
		return;
	}

	LOGI("color image :: width is %d; height is %d; stride is %d; format is %d;flags is %d",infoedges.width,infoedges.height,infoedges.stride,infoedges.format,infoedges.flags);
		if (infoedges.format != ANDROID_BITMAP_FORMAT_A_8) {
			LOGE("Bitmap format is not A_8 !");
			return;
		}

		if ((ret = AndroidBitmap_lockPixels(env, bitmapgray, &pixelsgray)) < 0) {
			LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
		}

		if ((ret = AndroidBitmap_lockPixels(env, bitmapedges, &pixelsedge)) < 0) {
			LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
		}

		// modify pixels with image processing algorithm

		LOGI("time to modify pixels....");

		graydata = (uint8_t *) pixelsgray;
		edgedata = (uint8_t *) pixelsedge;

		for (y=0;y<=infogray.height - 1;y++) {
			for (x=0;x<infogray.width -1;x++) {
				sumX = 0;
				sumY = 0;
				// check boundaries
				if (y==0 || y == infogray.height-1) {
					sum = 0;
				} else if (x == 0 || x == infogray.width -1) {
					sum = 0;
				} else {
					// calc X gradient
					for (i=-1;i<=1;i++) {
						for (j=-1;j<=1;j++) {
							sumX += (int) ( (*(graydata + x + i + (y + j) * infogray.stride)) * Gx[i+1][j+1]);
						}
					}

			// calc Y gradient
			for (i=-1;i<=1;i++) {
				for (j=-1;j<=1;j++) {
					sumY += (int) ( (*(graydata + x + i + (y + j) * infogray.stride)) * Gy[i+1][j+1]);
				}
			}

			sum = abs(sumX) + abs(sumY);

		}

		if (sum>255) sum = 255;
		if (sum<0) sum = 0;

		*(edgedata + x + y*infogray.width) = 255 - (uint8_t) sum;

	}
}

		AndroidBitmap_unlockPixels(env, bitmapgray);
		AndroidBitmap_unlockPixels(env, bitmapedges);

}

