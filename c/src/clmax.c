#include "clmax.h"

#define MAX_CL_PLATFORMS                32
#define MAX_CL_DEVICES_PER_PLATFORM     256
#define MAX_CL_PLATFORM_INFO_SIZE       512
#define MAX_CL_DEVICE_INFO_SIZE         1024

void checkResult(cl_int result) {
    if(result != CL_SUCCESS)
        printf("Error code %d", result);
}

// Platform list
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformsNative(JNIEnv *env, jclass this) {
    cl_uint num_platforms;
    cl_platform_id* platforms = (cl_platform_id*)malloc(MAX_CL_PLATFORMS * sizeof(cl_platform_id));

    checkResult(clGetPlatformIDs(MAX_CL_PLATFORMS, platforms, &num_platforms));

    jlongArray result;
    result = (*env)->NewLongArray(env, num_platforms);
    if(result == NULL)
        return NULL;
    jlong pointers[num_platforms];
    for (int i = 0; i < num_platforms; i++)
        pointers[i] = (long long)platforms[i];
    (*env)->SetLongArrayRegion(env, result, 0, num_platforms, pointers);

    free(platforms);

    return result;
}

// Platform info
JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformInfoNative(JNIEnv *env, jclass CLPlatform, jlong pointer, jint parameter) {
    char* info = (char*)malloc(MAX_CL_PLATFORM_INFO_SIZE * sizeof(char));
    size_t retsize;

    checkResult(clGetPlatformInfo((cl_platform_id)pointer, parameter, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize));

    jstring result = (*env)->NewStringUTF(env, info);

    free(info);

    return result;
}

// Device list
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDevicesNative(JNIEnv *env, jclass this, jlong pointer) {
    cl_uint num_devices;
    cl_device_id* devices = (cl_device_id*)malloc(MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id));

    checkResult(clGetDeviceIDs((cl_platform_id)pointer, CL_DEVICE_TYPE_ALL, MAX_CL_DEVICES_PER_PLATFORM, devices, &num_devices));

    jlongArray result;
    result = (*env)->NewLongArray(env, num_devices);
    if(result == NULL)
        return NULL;
    jlong pointers[num_devices];
    for (int i = 0; i < num_devices; i++)
        pointers[i] = (long long)devices[i];
    (*env)->SetLongArrayRegion(env, result, 0, num_devices, pointers);

    free(devices);

    return result;
}

// Device info
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongNative(JNIEnv *env, jclass this, jlong pointer, jint parameter) {
    long result;
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointer, parameter, MAX_CL_DEVICE_INFO_SIZE, &result, &retsize));

    return (long long)result;
}

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoStringNative(JNIEnv *env, jclass this, jlong pointer, jint parameter) {
    char* info = (char*)malloc(MAX_CL_DEVICE_INFO_SIZE * sizeof(char));
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointer, parameter, MAX_CL_DEVICE_INFO_SIZE, info, &retsize));

    jstring result = (*env)->NewStringUTF(env, info);

    free(info);

    return result;
}

/*JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_impl_PlatformsImpl_nativePlatforms(JNIEnv *env, jobject this, jobject platformList) {
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
}*/