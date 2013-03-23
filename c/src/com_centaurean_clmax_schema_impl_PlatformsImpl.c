#include "com_centaurean_clmax_schema_impl_PlatformsImpl.h"
#include <OpenCL/opencl.h>
#include <stdlib.h>

#define MAX_CL_PLATFORMS            256
#define MAX_CL_PLATFORM_INFO_SIZE   512

void checkResult(cl_int result) {
    if(result != CL_SUCCESS)
        printf("Error code %d", result);
}

JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_impl_PlatformsImpl_nativePlatforms(JNIEnv *env, jobject this, jobject platformList) {
    jclass platformListClass = (*env)->GetObjectClass(env, platformList);
    if(platformListClass == NULL)
        return;
    jmethodID methodAdd = (*env)->GetMethodID(env, platformListClass, "add", "(Lcom/centaurean/clmax/schema/Platform;)V");
    if(methodAdd == NULL)
        return;

    cl_platform_id *platforms = (cl_platform_id*)malloc(MAX_CL_PLATFORMS * sizeof(cl_platform_id));
    cl_uint num_platforms;
    checkResult(clGetPlatformIDs(MAX_CL_PLATFORMS, platforms, &num_platforms));
    char* info = (char*)malloc(MAX_CL_PLATFORM_INFO_SIZE * sizeof(char));
    size_t retsize;

    jclass platformClass = (*env)->FindClass(env, "com/centaurean/clmax/schema/Platform");
    if(platformClass == NULL)
        return;
    jmethodID platformClassConstructor = (*env)->GetMethodID(env, platformClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    if(platformClassConstructor == NULL)
        return;

    for (int i = 0; i < num_platforms; i++) {
        checkResult(clGetPlatformInfo(platforms[i], CL_PLATFORM_PROFILE, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring profile = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(platforms[i], CL_PLATFORM_VERSION, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring version = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(platforms[i], CL_PLATFORM_NAME, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring name = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(platforms[i], CL_PLATFORM_VENDOR, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring vendor = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(platforms[i], CL_PLATFORM_EXTENSIONS, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring extensions = (*env)->NewStringUTF(env, info);
        jobject platform = (*env)->NewObject(env, platformClass, platformClassConstructor, profile, version, name, vendor, extensions);
        jboolean isAdded = (*env)->CallBooleanMethod(env, platformList, methodAdd, platform);
    }

    free(info);
    free(platforms);
}