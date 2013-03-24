#include "clmax.h"

#define MAX_CL_PLATFORMS                32
#define MAX_CL_DEVICES_PER_PLATFORM     256
#define MAX_CL_PLATFORM_INFO_SIZE       512
#define MAX_CL_DEVICE_INFO_SIZE         1024

void checkResult(cl_int result) {
    if(result != CL_SUCCESS)
        printf("Error code %d\n", result);
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

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongArrayNative(JNIEnv *env, jclass this, jlong pointer, jint parameter) {
    size_t retsize;
    size_t* values = (size_t*)malloc(MAX_CL_DEVICE_INFO_SIZE * sizeof(size_t));

    checkResult(clGetDeviceInfo((cl_device_id)pointer, parameter, MAX_CL_DEVICE_INFO_SIZE, values, &retsize));

    int arraySize = retsize / sizeof(size_t);
    jlongArray result;
    result = (*env)->NewLongArray(env, arraySize);
    if(result == NULL)
        return NULL;
    jlong construct[arraySize];
    for (int i = 0; i < arraySize; i++)
        construct[i] = (long long)values[i];
    (*env)->SetLongArrayRegion(env, result, 0, arraySize, construct);

    free(values);

    return result;
}

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoStringNative(JNIEnv *env, jclass this, jlong pointer, jint parameter) {
    char* info = (char*)malloc(MAX_CL_DEVICE_INFO_SIZE * sizeof(char));
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointer, parameter, MAX_CL_DEVICE_INFO_SIZE, info, &retsize));

    jstring result = (*env)->NewStringUTF(env, info);

    free(info);

    return result;
}

// Context creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createContextNative(JNIEnv *env, jclass this, jlong pointer) {
    cl_uint num_devices;
    cl_device_id* devices = (cl_device_id*)malloc(MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id));
    checkResult(clGetDeviceIDs((cl_platform_id)pointer, CL_DEVICE_TYPE_ALL, MAX_CL_DEVICES_PER_PLATFORM, devices, &num_devices));
    cl_int errcode_ret;
    cl_context_properties properties[] = {CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointer, 0};
    cl_context context = clCreateContext(properties, num_devices, devices, NULL, NULL, &errcode_ret);
    checkResult(errcode_ret);

    free(devices);

    return (long long)context;
}

// Context release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseContextNative(JNIEnv *env, jclass this, jlong pointer) {
    checkResult(clReleaseContext((cl_context)pointer));
}