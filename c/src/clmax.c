/*
 * Copyright (c) 2013, Centaurean
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Centaurean nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Centaurean BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * CLmax
 *
 * 23/03/13 20:54
 * @author gpnuma
 */

#include "clmax.h"

#define MAX_CL_PLATFORMS                32
#define MAX_CL_DEVICES_PER_PLATFORM     256
#define MAX_CL_PLATFORM_INFO_SIZE       512
#define MAX_CL_DEVICE_INFO_SIZE         1024
#define MAX_CL_CONTEXT_INFO_SIZE        1024
#define MAX_CL_PROGRAM_INFO_SIZE        1024
#define MAX_CL_PROGRAM_INFO_ARRAY_SIZE  256
#define MAX_CL_KERNEL_INFO_SIZE         1024
#define MAX_ERROR_MESSAGE_SIZE          32

void checkResult(cl_int result, JNIEnv *env) {
    if(result != CL_SUCCESS) {
        char code[MAX_ERROR_MESSAGE_SIZE];
        sprintf(code, "%d", result);
        throwCLException(env, code);
    }
}

jint throwCLException(JNIEnv *env, char* code) {
    jclass exClass = (*env)->FindClass(env, "com/centaurean/clmax/schema/exceptions/CLNativeException");
    return (*env)->ThrowNew(env, exClass, code);
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
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoIntNative(JNIEnv *env, jclass this, jlong pointerDevice, jint parameter) {
    int result;
    size_t retsize;
    
    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongNative(JNIEnv *env, jclass this, jlong pointerDevice, jint parameter) {
    long result;
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, sizeof(&result), &result, &retsize), env);

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

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createCLGLContextNative(JNIEnv *env, jclass this, jlong pointerPlatform) {
    cl_context context = NULL;
    cl_int errcode_ret;

    cl_uint num_devices;
    cl_device_id* devices = (cl_device_id*)malloc(MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id));
    
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
    checkResult(clGetDeviceIDs((cl_platform_id)pointerPlatform, CL_DEVICE_TYPE_CPU, MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id), devices, &num_devices), env);
#elif _WIN32
    // Create CL context properties, add WGL context & handle to DC
    cl_context_properties properties[] = {
        CL_GL_CONTEXT_KHR, (cl_context_properties)wglGetCurrentContext(),       // WGL Context
        CL_WGL_HDC_KHR, (cl_context_properties)wglGetCurrentDC(),               // WGL HDC
        CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointerPlatform,    // OpenCL platform
        0
    };

    // Find CL capable devices in the current GL context
    size_t size;
    checkResult(clGetGLContextInfoKHR(properties, CL_DEVICES_FOR_GL_CONTEXT_KHR, MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id), devices, &size), env);
    num_devices = size / sizeof(cl_device_id);
#elif __linux__
    // Create CL context properties, add GLX context & handle to DC
    cl_context_properties properties[] = {
        CL_GL_CONTEXT_KHR, (cl_context_properties)glXGetCurrentContext(),       // GLX Context
        CL_GLX_DISPLAY_KHR, (cl_context_properties)glXGetCurrentDisplay(),      // GLX Display
        CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointerPlatform,    // OpenCL platform
        0
    };

    // Find CL capable devices in the current GL context
    size_t size;
    checkResult(clGetGLContextInfoKHR(properties, CL_DEVICES_FOR_GL_CONTEXT_KHR, MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id), devices, &size), env);
    num_devices = size / sizeof(cl_device_id);
#else
    throwCLException(env, "Unsupported platform");
#endif
    
    context = clCreateContext(properties, num_devices, devices, NULL, 0, &errcode_ret);
    
    free(devices);

    checkResult(errcode_ret, env);

    return (long long)context;
}

// Context release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseContextNative(JNIEnv *env, jclass this, jlong pointerContext) {
    checkResult(clReleaseContext((cl_context)pointerContext), env);
}

// Context infos
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getContextInfoIntNative(JNIEnv *env, jclass this, jlong pointerContext, jint parameter) {
    int result;
    size_t retsize;
    
    cl_context context = (cl_context)pointerContext;
    checkResult(clGetContextInfo((cl_context)pointerContext, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getContextInfoLongArrayNative(JNIEnv *env, jclass this, jlong pointerContext, jint parameter) {
    size_t retsize;
    size_t* values = (size_t*)malloc(MAX_CL_CONTEXT_INFO_SIZE * sizeof(size_t));
    
    checkResult(clGetContextInfo((cl_context)pointerContext, parameter, MAX_CL_CONTEXT_INFO_SIZE, values, &retsize), env);
    
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

// Program creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createProgramWithSourceNative(JNIEnv *env, jclass this, jlong pointerContext, jstring programSource) {
    cl_int errcode_ret;
    const char* nativeProgramSource = (*env)->GetStringUTFChars(env, programSource, 0);
    const char* parameters[1];
    parameters[0] = nativeProgramSource;

    cl_program program = clCreateProgramWithSource((cl_context)pointerContext, 1, parameters, NULL, &errcode_ret);

    (*env)->ReleaseStringUTFChars(env, programSource, nativeProgramSource);

    checkResult(errcode_ret, env);

    return (long long)program;
}

// Program release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseProgramNative(JNIEnv *env, jclass this, jlong pointerProgram) {
    checkResult(clReleaseProgram((cl_program)pointerProgram), env);
}

// Program build
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_buildProgramNative(JNIEnv *env, jclass this, jlong pointerProgram, jlongArray pointersDevices, jstring options) {
    cl_uint num_devices = (*env)->GetArrayLength(env, pointersDevices);
    cl_device_id* devices = (cl_device_id*)malloc(num_devices * sizeof(cl_device_id));
    jlong *body = (*env)->GetLongArrayElements(env, pointersDevices, 0);
    for (jint i = 0; i < num_devices; i++)
        devices[i] = (cl_device_id)body[i];
	(*env)->ReleaseLongArrayElements(env, pointersDevices, body, 0);
    
    const char* nativeOptions = (*env)->GetStringUTFChars(env, options, 0);
    
    checkResult(clBuildProgram((cl_program)pointerProgram, num_devices, devices, nativeOptions, NULL, NULL), env);
    
    (*env)->ReleaseStringUTFChars(env, options, nativeOptions);
}

// Program infos
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoIntNative(JNIEnv *env, jclass this, jlong pointerProgram, jint parameter) {
    int result;
    size_t retsize;
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoLongNative(JNIEnv *env, jclass this, jlong pointerProgram, jint parameter) {
    long result;
    size_t retsize;
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, sizeof(&result), &result, &retsize), env);
    
    return (long long)result;
}

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoLongArrayNative(JNIEnv *env, jclass this, jlong pointerProgram, jint parameter) {
    size_t retsize;
    size_t* values = (size_t*)malloc(MAX_CL_PROGRAM_INFO_SIZE * sizeof(size_t));
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, MAX_CL_PROGRAM_INFO_SIZE, values, &retsize), env);
    
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

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoStringNative(JNIEnv *env, jclass this, jlong pointerProgram, jint parameter) {
    char* info = (char*)malloc(MAX_CL_PROGRAM_INFO_SIZE * sizeof(char));
    size_t retsize;
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, MAX_CL_PROGRAM_INFO_SIZE, info, &retsize), env);
    
    jstring result = (*env)->NewStringUTF(env, info);
    
    free(info);
    
    return result;
}

JNIEXPORT jobjectArray JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoBinariesNative(JNIEnv *env, jclass this, jlong pointerProgram, jlongArray binarySizes) {
    size_t retsize;
    jsize length = (*env)->GetArrayLength(env, binarySizes);
    unsigned char** valuesArray = (unsigned char**)malloc(length * sizeof(unsigned char*));
    
    jlong *body = (*env)->GetLongArrayElements(env, binarySizes, 0);
    
    for (jint i = 0; i < length; i++)
        valuesArray[i] = (unsigned char*)malloc(body[i] * sizeof(unsigned char));                                                
    checkResult(clGetProgramInfo((cl_program)pointerProgram, CL_PROGRAM_BINARIES, length * sizeof(unsigned char*), valuesArray, &retsize), env);
    
    jobjectArray result = (*env)->NewObjectArray(env, length, (*env)->FindClass(env, "java/lang/Object"), (*env)->NewCharArray(env, 0));
    for (int i = 0; i < length; i++) {
        jbyteArray element = (*env)->NewByteArray(env, body[i]);
        if(element == NULL)
            return NULL;
        jbyte construct[body[i]];
        for(int j = 0; j < body[i]; j++)
            construct[i] = valuesArray[i][j];
        (*env)->SetByteArrayRegion(env, element, 0, body[i], construct);
        (*env)->SetObjectArrayElement(env, result, i, element);
    }
    for (jint i = 0; i < length; i++)
        free(valuesArray[i]);
    
    (*env)->ReleaseLongArrayElements(env, binarySizes, body, 0);
    
    free(valuesArray);
    
    return result;
}

// Kernel creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createKernelNative(JNIEnv *env, jclass this, jlong pointerProgram, jstring kernelName) {
    cl_int errcode_ret;
    const char* nativeKernelName = (*env)->GetStringUTFChars(env, kernelName, 0);
    
    cl_kernel kernel = clCreateKernel((cl_program)pointerProgram, nativeKernelName, &errcode_ret);
    
    (*env)->ReleaseStringUTFChars(env, kernelName, nativeKernelName);
    
    checkResult(errcode_ret, env);
    
    return (long long)kernel;
}

// Kernel release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseKernelNative(JNIEnv *env, jclass this, jlong pointerKernel) {
    checkResult(clReleaseKernel((cl_kernel)pointerKernel), env);
}

// Kernel infos
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getKernelInfoIntNative(JNIEnv *env, jclass this, jlong pointerKernel, jint parameter) {
    int result;
    size_t retsize;
    
    checkResult(clGetKernelInfo((cl_kernel)pointerKernel, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getKernelInfoLongNative(JNIEnv *env, jclass this, jlong pointerKernel, jint parameter) {
    long result;
    size_t retsize;
    
    checkResult(clGetKernelInfo((cl_kernel)pointerKernel, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getKernelInfoStringNative(JNIEnv *env, jclass this, jlong pointerKernel, jint parameter) {
    char* info = (char*)malloc(MAX_CL_KERNEL_INFO_SIZE * sizeof(char));
    size_t retsize;
    
    checkResult(clGetKernelInfo((cl_kernel)pointerKernel, parameter, MAX_CL_KERNEL_INFO_SIZE, info, &retsize), env);
    
    jstring result = (*env)->NewStringUTF(env, info);
    
    free(info);
    
    return result;
}

// Kernel args
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_setKernelArgBufferNative(JNIEnv *env, jclass this, jlong pointerKernel, jint argIndex, jlong pointerBuffer) {
    checkResult(clSetKernelArg((cl_kernel)pointerKernel, argIndex, sizeof(cl_mem), &pointerBuffer), env);
}

JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_setKernelArgIntNative(JNIEnv *env, jclass this, jlong pointerKernel, jint argIndex, jint value) {
    checkResult(clSetKernelArg((cl_kernel)pointerKernel, argIndex, sizeof(jint), &value), env);
}

// Kernel run
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_runKernelNative(JNIEnv *env, jclass this, jlong pointerKernel, jlong pointerCommandQueue) {
    size_t global_work_size[] = {1024};
    checkResult(clEnqueueNDRangeKernel((cl_command_queue)pointerCommandQueue, (cl_kernel)pointerKernel, 1, NULL, global_work_size, NULL, 0, NULL, NULL), env);
    checkResult(clFinish((cl_command_queue)pointerCommandQueue), env);
    fprintf(stderr, "Finished");
}

// Buffer creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createBufferNative(JNIEnv *env, jclass this, jlong pointerContext, jobject buffer, jint flags) {
    cl_int errcode_ret;
    
    cl_mem clBuffer = clCreateBuffer((cl_context)pointerContext, flags | CL_MEM_USE_HOST_PTR, (*env)->GetDirectBufferCapacity(env, buffer), (*env)->GetDirectBufferAddress(env, buffer), &errcode_ret);
    
    checkResult(errcode_ret, env);
    
    return (long long)clBuffer;
}

// Mem object release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseMemObjectNative(JNIEnv *env, jclass this, jlong pointerMemObject) {
    checkResult(clReleaseMemObject((cl_mem)pointerMemObject), env);
}

// Buffer mapping
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_mapBufferNative(JNIEnv *env, jclass this, jlong pointerCommandQueue, jlong pointerBuffer, jint mapFlags, jint bufferSize) {
    cl_int errcode_ret;
    
    void* address = clEnqueueMapBuffer((cl_command_queue)pointerCommandQueue, (cl_mem)pointerBuffer, CL_TRUE, mapFlags, 0, bufferSize, 0, NULL, NULL, &errcode_ret);
    fprintf(stderr, "%lld,", (long long)address);
    float* buf = (float*)address;
    for(int i = 0; i < 10; i ++)
        fprintf(stderr, "%g,", buf[i]);
    
    checkResult(errcode_ret, env);
}

// Mem object unmapping
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_unmapMemObjectNative(JNIEnv *env, jclass this, jlong pointerCommandQueue, jlong pointerMemObject, jobject hostBuffer) {
    fprintf(stderr, "%lld,", (long long)(*env)->GetDirectBufferAddress(env, hostBuffer));
    checkResult(clEnqueueUnmapMemObject((cl_command_queue)pointerCommandQueue, (cl_mem)pointerMemObject, (*env)->GetDirectBufferAddress(env, hostBuffer), 0, NULL, NULL), env);
}

// Command queue creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createCommandQueueNative(JNIEnv *env, jclass this, jlong pointerContext, jlong pointerDevice) {
    cl_int errcode_ret;
    
    cl_command_queue commandQueue = clCreateCommandQueue((cl_context)pointerContext, (cl_device_id)pointerDevice, 0, &errcode_ret);
    
    checkResult(errcode_ret, env);
    
    return (long long)commandQueue;
}

// Command queue release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseCommandQueueNative(JNIEnv *env, jclass this, jlong pointerCommandQueue) {
    checkResult(clReleaseCommandQueue((cl_command_queue)pointerCommandQueue), env);
}
