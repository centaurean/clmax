/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_centaurean_clmax_schema_CL */

#ifndef _Included_com_centaurean_clmax_schema_CL
#define _Included_com_centaurean_clmax_schema_CL
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    getPlatformsNative
 * Signature: ()[J
 */
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformsNative
  (JNIEnv *, jclass);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    getPlatformInfoNative
 * Signature: (JI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformInfoNative
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    getDevicesNative
 * Signature: (J)[J
 */
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDevicesNative
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    getDeviceInfoLongNative
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongNative
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    getDeviceInfoLongArrayNative
 * Signature: (JI)[J
 */
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongArrayNative
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    getDeviceInfoStringNative
 * Signature: (JI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoStringNative
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    createContextNative
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createContextNative
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    createContextCLGLNative
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createContextCLGLNative
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_centaurean_clmax_schema_CL
 * Method:    releaseContextNative
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseContextNative
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
