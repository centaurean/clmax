#include "clmax.h"

#define MAX_CL_PLATFORMS                32
#define MAX_CL_DEVICES_PER_PLATFORM     256
#define MAX_CL_PLATFORM_INFO_SIZE       512
#define MAX_CL_DEVICE_INFO_SIZE         1024

void checkResult(cl_int result, JNIEnv *env) {
    if(result != CL_SUCCESS) {
        char* message = (char*)malloc(256 * sizeof(char));
        sprintf(message, "CL error code %d", result);
        throwCLException(env, message);
    }
}

jint throwCLException(JNIEnv *env, char* message) {
    jclass exClass;
    char *className = "com/centaurean/clmax/schema/exceptions/CLException" ;
    exClass = (*env)->FindClass(env, className);
    return (*env)->ThrowNew(env, exClass, message);
}

// Platform list
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformsNative(JNIEnv *env, jclass this) {
    cl_uint num_platforms;
    cl_platform_id* platforms = (cl_platform_id*)malloc(MAX_CL_PLATFORMS * sizeof(cl_platform_id));

    checkResult(clGetPlatformIDs(MAX_CL_PLATFORMS, platforms, &num_platforms), env);

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

    checkResult(clGetPlatformInfo((cl_platform_id)pointer, parameter, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize), env);

    jstring result = (*env)->NewStringUTF(env, info);

    free(info);

    return result;
}

// Device list
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDevicesNative(JNIEnv *env, jclass this, jlong pointerPlatform, jlong devicesType) {
    cl_uint num_devices;
    cl_device_id* devices = (cl_device_id*)malloc(MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id));

    checkResult(clGetDeviceIDs((cl_platform_id)pointerPlatform, devicesType, MAX_CL_DEVICES_PER_PLATFORM, devices, &num_devices), env);

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
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongNative(JNIEnv *env, jclass this, jlong pointerDevice, jint parameter) {
    long result;
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, MAX_CL_DEVICE_INFO_SIZE, &result, &retsize), env);

    return (long long)result;
}

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongArrayNative(JNIEnv *env, jclass this, jlong pointerDevice, jint parameter) {
    size_t retsize;
    size_t* values = (size_t*)malloc(MAX_CL_DEVICE_INFO_SIZE * sizeof(size_t));

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, MAX_CL_DEVICE_INFO_SIZE, values, &retsize), env);

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

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoStringNative(JNIEnv *env, jclass this, jlong pointerDevice, jint parameter) {
    char* info = (char*)malloc(MAX_CL_DEVICE_INFO_SIZE * sizeof(char));
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, MAX_CL_DEVICE_INFO_SIZE, info, &retsize), env);

    jstring result = (*env)->NewStringUTF(env, info);

    free(info);

    return result;
}

// Context creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createContextNative(JNIEnv *env, jclass this, jlong pointerPlatform, jlongArray pointersDevices) {
    cl_uint num_devices = (*env)->GetArrayLength(env, pointersDevices);
    cl_device_id* devices = (cl_device_id*)malloc(num_devices * sizeof(cl_device_id));    
    jlong *body = (*env)->GetLongArrayElements(env, pointersDevices, 0);
    for (jint i = 0; i < num_devices; i++)
        devices[i] = (cl_device_id)body[i];
	(*env)->ReleaseLongArrayElements(env, pointersDevices, body, 0);
    
    //cl_device_id* devices = (cl_device_id*)malloc(MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id));
    //checkResult(clGetDeviceIDs((cl_platform_id)pointerPlatform, devicesType, MAX_CL_DEVICES_PER_PLATFORM, devices, &num_devices));
    cl_int errcode_ret;
    cl_context_properties properties[] = {CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointerPlatform, 0};
    cl_context context = clCreateContext(properties, num_devices, devices, NULL, NULL, &errcode_ret);
    checkResult(errcode_ret, env);

    // Check for const in method clCreateContext
    free(devices);

    return (long long)context;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createCLGLContextNative(JNIEnv *env, jclass this, jlong pointerPlatform, jlongArray pointersDevices) {
    cl_uint num_devices = (*env)->GetArrayLength(env, pointersDevices);
    cl_device_id* devices = (cl_device_id*)malloc(num_devices * sizeof(cl_device_id));
    jlong *body = (*env)->GetLongArrayElements(env, pointersDevices, 0);
    for (jint i = 0; i < num_devices; i++)
        devices[i] = (cl_device_id)body[i];
	(*env)->ReleaseLongArrayElements(env, pointersDevices, body, 0);

    cl_context context = NULL;
    cl_int errcode_ret;
#ifdef __APPLE__
    /*CGLContextObj glContext;
    CGLPixelFormatAttribute attributes[4] = {
      kCGLPFAAccelerated,   // no software rendering
      kCGLPFAOpenGLProfile, // core profile with the version stated below
      (CGLPixelFormatAttribute) kCGLOGLPVersion_3_2_Core,
      (CGLPixelFormatAttribute) 0
    };
    CGLPixelFormatObj pix;
    CGLError errorCode;
    GLint num;
    errorCode = CGLChoosePixelFormat(attributes, &pix, &num);
    errorCode = CGLCreateContext(pix, NULL, &glContext);
    CGLDestroyPixelFormat( pix );
    errorCode = CGLSetCurrentContext(glContext);*/

    // Get current CGL Context and CGL Share group
    CGLContextObj kCGLContext = CGLGetCurrentContext();
    if(kCGLContext == NULL)
        return 0;
    CGLShareGroupObj kCGLShareGroup = CGLGetShareGroup(kCGLContext);

    // Create CL context properties, add handle & share-group enum
    cl_context_properties properties[] = {
        CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE,
        (cl_context_properties)kCGLShareGroup, 0
    };

    // Optional: Get the CPU device (we can request this in addition to GPUs in Share Group)
    //cl_device_id cpu_devices[32]; cl_uint count;
    //checkResult(clGetDeviceIDs((cl_platform_id)pointer, CL_DEVICE_TYPE_CPU, 32 * sizeof(cl_device_id), cpu_devices, &count), env);

    // Create a context from a CGL share group (note: only use CPU if software renderer is enabled!)
    context = clCreateContext(properties, /*0, 0*/num_devices, devices, NULL, 0, &errcode_ret);
#elif _WIN32
    // Create CL context properties, add WGL context & handle to DC
    cl_context_properties properties[] = {
        CL_GL_CONTEXT_KHR, (cl_context_properties)wglGetCurrentContext(),       // WGL Context
        CL_WGL_HDC_KHR, (cl_context_properties)wglGetCurrentDC(),               // WGL HDC
        CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointer,    // OpenCL platform
        0
    };

    // Find CL capable devices in the current GL context
    cl_device_id devices[32];
    size_t size;
    clGetGLContextInfoKHR(properties, CL_DEVICES_FOR_GL_CONTEXT_KHR, 32 * sizeof(cl_device_id), devices, &size);

    // Create a context using the supported devices
    cl_int count = size / sizeof(cl_device_id);
    context = clCreateContext(properties, devices, num_devices, NULL, 0, &errcode_ret);
#elif __linux__
    // Create CL context properties, add GLX context & handle to DC
    cl_context_properties properties[] = {
        CL_GL_CONTEXT_KHR, (cl_context_properties)glXGetCurrentContext(),       // GLX Context
        CL_GLX_DISPLAY_KHR, (cl_context_properties)glXGetCurrentDisplay(),      // GLX Display
        CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointer,    // OpenCL platform
        0
    };

    // Find CL capable devices in the current GL context
    cl_device_id devices[32]; size_t size;
    clGetGLContextInfoKHR(properties, CL_DEVICES_FOR_GL_CONTEXT_KHR, 32 * sizeof(cl_device_id), devices, &size);

    // Create a context using the supported devices
    cl_int count = size / sizeof(cl_device_id);
    context = clCreateContext(properties, devices, num_devices, NULL, 0, &errcode_ret);
#endif
    
    free(devices);

    checkResult(errcode_ret, env);

    return (long long)context;
}

// Context release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseContextNative(JNIEnv *env, jclass this, jlong pointer) {
    checkResult(clReleaseContext((cl_context)pointer), env);
}