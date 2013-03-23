#include "clmax.h"

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

    memory_context.platforms = (cl_platform_id*)malloc(MAX_CL_PLATFORMS * sizeof(cl_platform_id));
    checkResult(clGetPlatformIDs(MAX_CL_PLATFORMS, memory_context.platforms, &memory_context.num_platforms));
    char* info = (char*)malloc(MAX_CL_PLATFORM_INFO_SIZE * sizeof(char));
    size_t retsize;

    jclass platformClass = (*env)->FindClass(env, "com/centaurean/clmax/schema/Platform");
    if(platformClass == NULL)
        return;
    jmethodID platformClassConstructor = (*env)->GetMethodID(env, platformClass, "<init>", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    if(platformClassConstructor == NULL)
        return;

    for (int i = 0; i < memory_context.num_platforms; i++) {
        jlong id = (long long)&memory_context.platforms[i];
        checkResult(clGetPlatformInfo(memory_context.platforms[i], CL_PLATFORM_PROFILE, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring profile = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(memory_context.platforms[i], CL_PLATFORM_VERSION, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring version = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(memory_context.platforms[i], CL_PLATFORM_NAME, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring name = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(memory_context.platforms[i], CL_PLATFORM_VENDOR, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring vendor = (*env)->NewStringUTF(env, info);
        checkResult(clGetPlatformInfo(memory_context.platforms[i], CL_PLATFORM_EXTENSIONS, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));
        jstring extensions = (*env)->NewStringUTF(env, info);
        jobject platform = (*env)->NewObject(env, platformClass, platformClassConstructor, id, profile, version, name, vendor, extensions);
        jboolean isAdded = (*env)->CallBooleanMethod(env, platformList, methodAdd, platform);
    }

    free(info);
    //free(cl.platforms); free at end of usage
}