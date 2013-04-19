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
		std::ostringstream out;
		out << result;
        throwCLException(env, out.str().c_str());
    }
}

jint throwCLException(JNIEnv *env, const char* code) {
    jclass exClass =env->FindClass("com/centaurean/clmax/schema/exceptions/CLNativeException");
    return env->ThrowNew(exClass, code);
}

// Platform list
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformsNative(JNIEnv *env, jclass callingClass) {
    cl_uint num_platforms;
    cl_platform_id* platforms = new cl_platform_id[MAX_CL_PLATFORMS * sizeof(cl_platform_id)];

    checkResult(clGetPlatformIDs(MAX_CL_PLATFORMS, platforms, &num_platforms), env);

    jlongArray result;
    result = env->NewLongArray(num_platforms);
    if(result == NULL)
        return NULL;
	jlong* pointers = new jlong[num_platforms * sizeof(jlong)];
    for (unsigned int i = 0; i < num_platforms; i++)
        pointers[i] = (long long)platforms[i];
    env->SetLongArrayRegion(result, 0, num_platforms, pointers);
	delete[] pointers;

    delete[] platforms;

    return result;
}

// Platform info
JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getPlatformInfoNative(JNIEnv *env, jclass CLPlatform, jlong pointer, jint parameter) {
    char* info = new char[MAX_CL_PLATFORM_INFO_SIZE * sizeof(char)];
    size_t retsize;

    checkResult(clGetPlatformInfo((cl_platform_id)pointer, parameter, MAX_CL_PLATFORM_INFO_SIZE, info, &retsize), env);

    jstring result = env->NewStringUTF(info);

    delete[] info;

    return result;
}

// Device list
JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDevicesNative(JNIEnv *env, jclass callingClass, jlong pointerPlatform, jlong devicesType) {
    cl_uint num_devices;
    cl_device_id* devices = new cl_device_id[MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id)];

    checkResult(clGetDeviceIDs((cl_platform_id)pointerPlatform, devicesType, MAX_CL_DEVICES_PER_PLATFORM, devices, &num_devices), env);

    jlongArray result;
    result = env->NewLongArray(num_devices);
    if(result == NULL)
        return NULL;
    jlong* pointers = new jlong[num_devices * sizeof(jlong)];
    for (unsigned int i = 0; i < num_devices; i++)
        pointers[i] = (long long)devices[i];
    env->SetLongArrayRegion(result, 0, num_devices, pointers);
	delete[] pointers;

    delete[] devices;

    return result;
}

// Device info
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoIntNative(JNIEnv *env, jclass callingClass, jlong pointerDevice, jint parameter) {
    int result;
    size_t retsize;
    
    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongNative(JNIEnv *env, jclass callingClass, jlong pointerDevice, jint parameter) {
    long result;
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, sizeof(&result), &result, &retsize), env);

    return (long long)result;
}

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoLongArrayNative(JNIEnv *env, jclass callingClass, jlong pointerDevice, jint parameter) {
    size_t retsize;
    size_t* values = new size_t[MAX_CL_DEVICE_INFO_SIZE * sizeof(size_t)];

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, MAX_CL_DEVICE_INFO_SIZE, values, &retsize), env);

    int arraySize = (int)retsize / sizeof(size_t);
    jlongArray result;
    result = env->NewLongArray(arraySize);
    if(result == NULL)
        return NULL;
    jlong* construct = new jlong[arraySize * sizeof(jlong)];
    for (int i = 0; i < arraySize; i++)
        construct[i] = (long long)values[i];
    env->SetLongArrayRegion(result, 0, arraySize, construct);
	delete[] construct;

    delete[] values;

    return result;
}

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getDeviceInfoStringNative(JNIEnv *env, jclass callingClass, jlong pointerDevice, jint parameter) {
    char* info = new char[MAX_CL_DEVICE_INFO_SIZE * sizeof(char)];
    size_t retsize;

    checkResult(clGetDeviceInfo((cl_device_id)pointerDevice, parameter, MAX_CL_DEVICE_INFO_SIZE, info, &retsize), env);

    jstring result = env->NewStringUTF(info);

    delete[] info;

    return result;
}

// Context creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createContextNative(JNIEnv *env, jclass callingClass, jlong pointerPlatform, jlongArray pointersDevices) {
    cl_uint num_devices = env->GetArrayLength(pointersDevices);
    cl_device_id* devices = new cl_device_id[num_devices * sizeof(cl_device_id)];
    jlong *body = env->GetLongArrayElements(pointersDevices, 0);
    for (unsigned int i = 0; i < num_devices; i++)
        devices[i] = (cl_device_id)body[i];
	env->ReleaseLongArrayElements(pointersDevices, body, 0);
    
    //cl_device_id* devices = (cl_device_id*)malloc(MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id));
    //checkResult(clGetDeviceIDs((cl_platform_id)pointerPlatform, devicesType, MAX_CL_DEVICES_PER_PLATFORM, devices, &num_devices));
    cl_int errcode_ret;
    cl_context_properties properties[] = {CL_CONTEXT_PLATFORM, (cl_context_properties)(cl_platform_id)pointerPlatform, 0};
    cl_context context = clCreateContext(properties, num_devices, devices, NULL, NULL, &errcode_ret);
    checkResult(errcode_ret, env);

    // Check for const in method clCreateContext
    delete[] devices;

    return (long long)context;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createCLGLContextNative(JNIEnv *env, jclass callingClass, jlong pointerPlatform) {
    cl_context context = NULL;
    cl_int errcode_ret;

    cl_uint num_devices;
    cl_device_id* devices = new cl_device_id[MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id)];
    
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
	typedef CL_API_ENTRY cl_int (CL_API_CALL *P1)(const cl_context_properties *properties, cl_gl_context_info param_name, size_t param_value_size, void *param_value, size_t *param_value_size_ret);
	CL_API_ENTRY cl_int(CL_API_CALL	*clGetGLContextInfoKHR)(const cl_context_properties *properties, cl_gl_context_info param_name, size_t param_value_size, void *param_value, size_t *param_value_size_ret) = NULL;
	clGetGLContextInfoKHR=(P1)clGetExtensionFunctionAddress("clGetGLContextInfoKHR");

    checkResult(clGetGLContextInfoKHR(properties, CL_DEVICES_FOR_GL_CONTEXT_KHR, MAX_CL_DEVICES_PER_PLATFORM * sizeof(cl_device_id), devices, &size), env);
    num_devices = (cl_uint)size / sizeof(cl_device_id);
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
    
    delete[] devices;

    checkResult(errcode_ret, env);

    return (long long)context;
}

// Context release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseContextNative(JNIEnv *env, jclass callingClass, jlong pointerContext) {
    checkResult(clReleaseContext((cl_context)pointerContext), env);
}

// Context infos
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getContextInfoIntNative(JNIEnv *env, jclass callingClass, jlong pointerContext, jint parameter) {
    int result;
    size_t retsize;
    
    cl_context context = (cl_context)pointerContext;
    checkResult(clGetContextInfo((cl_context)pointerContext, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getContextInfoLongArrayNative(JNIEnv *env, jclass callingClass, jlong pointerContext, jint parameter) {
    size_t retsize;
    size_t* values = new size_t[MAX_CL_CONTEXT_INFO_SIZE * sizeof(size_t)];
    
    checkResult(clGetContextInfo((cl_context)pointerContext, parameter, MAX_CL_CONTEXT_INFO_SIZE, values, &retsize), env);
    
    int arraySize = (int)retsize / sizeof(size_t);
    jlongArray result;
    result = env->NewLongArray(arraySize);
    if(result == NULL)
        return NULL;
    jlong* construct = new jlong[arraySize * sizeof(jlong)];
    for (int i = 0; i < arraySize; i++)
        construct[i] = (long long)values[i];
    env->SetLongArrayRegion(result, 0, arraySize, construct);
	delete[] construct;
    
    delete[] values;
    
    return result;
}

// Program creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createProgramWithSourceNative(JNIEnv *env, jclass callingClass, jlong pointerContext, jstring programSource) {
    cl_int errcode_ret;
    const char* nativeProgramSource = env->GetStringUTFChars(programSource, 0);
    const char* parameters[1];
    parameters[0] = nativeProgramSource;

    cl_program program = clCreateProgramWithSource((cl_context)pointerContext, 1, parameters, NULL, &errcode_ret);

    env->ReleaseStringUTFChars(programSource, nativeProgramSource);

    checkResult(errcode_ret, env);

    return (long long)program;
}

// Program release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseProgramNative(JNIEnv *env, jclass callingClass, jlong pointerProgram) {
    checkResult(clReleaseProgram((cl_program)pointerProgram), env);
}

// Program build
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_buildProgramNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jlongArray pointersDevices, jstring options) {
    cl_uint num_devices = env->GetArrayLength(pointersDevices);
    cl_device_id* devices = new cl_device_id[num_devices * sizeof(cl_device_id)];
    jlong *body = env->GetLongArrayElements(pointersDevices, 0);
    for (unsigned int i = 0; i < num_devices; i++)
        devices[i] = (cl_device_id)body[i];
	env->ReleaseLongArrayElements(pointersDevices, body, 0);
    
    const char* nativeOptions = env->GetStringUTFChars(options, 0);
    
    checkResult(clBuildProgram((cl_program)pointerProgram, num_devices, devices, nativeOptions, NULL, NULL), env);

	delete[] devices;
    
    env->ReleaseStringUTFChars(options, nativeOptions);
}

// Program infos
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoIntNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jint parameter) {
    int result;
    size_t retsize;
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoLongNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jint parameter) {
    long result;
    size_t retsize;
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, sizeof(&result), &result, &retsize), env);
    
    return (long long)result;
}

JNIEXPORT jlongArray JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoLongArrayNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jint parameter) {
    size_t retsize;
    size_t* values = new size_t[MAX_CL_PROGRAM_INFO_SIZE * sizeof(size_t)];
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, MAX_CL_PROGRAM_INFO_SIZE, values, &retsize), env);
    
    int arraySize = (int)retsize / sizeof(size_t);
    jlongArray result;
    result = env->NewLongArray(arraySize);
    if(result == NULL)
        return NULL;
    jlong* construct = new jlong[arraySize * sizeof(jlong)];
    for (int i = 0; i < arraySize; i++)
        construct[i] = (long long)values[i];
    env->SetLongArrayRegion(result, 0, arraySize, construct);
	delete[] construct;
    
    delete[] values;
    
    return result;    
}

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoStringNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jint parameter) {
    char* info = new char[MAX_CL_PROGRAM_INFO_SIZE * sizeof(char)];
    size_t retsize;
    
    checkResult(clGetProgramInfo((cl_program)pointerProgram, parameter, MAX_CL_PROGRAM_INFO_SIZE, info, &retsize), env);
    
    jstring result = env->NewStringUTF(info);
    
    delete[] info;
    
    return result;
}

JNIEXPORT jobjectArray JNICALL Java_com_centaurean_clmax_schema_CL_getProgramInfoBinariesNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jlongArray binarySizes) {
    size_t retsize;
    jsize length = env->GetArrayLength(binarySizes);
    unsigned char** valuesArray = new unsigned char*[length * sizeof(unsigned char*)];
    
    jlong *body = env->GetLongArrayElements(binarySizes, 0);
    
    for (jint i = 0; i < length; i++)
        valuesArray[i] = new unsigned char[(unsigned int)body[i] * sizeof(unsigned char)]; 
    checkResult(clGetProgramInfo((cl_program)pointerProgram, CL_PROGRAM_BINARIES, length * sizeof(unsigned char*), valuesArray, &retsize), env);
    
    jobjectArray result = env->NewObjectArray(length, env->FindClass("java/lang/Object"), env->NewCharArray(0));
    for (int i = 0; i < length; i++) {
        jbyteArray element = env->NewByteArray((jsize)body[i]);
        if(element == NULL)
            return NULL;
        jbyte* construct = new jbyte[(unsigned int)body[i] * sizeof(jbyte)];
        for(int j = 0; j < body[i]; j++)
            construct[i] = valuesArray[i][j];
        env->SetByteArrayRegion(element, 0, (jsize)body[i], construct);
		delete[] construct;
        env->SetObjectArrayElement(result, i, element);
    }
    for (jint i = 0; i < length; i++)
        delete[] valuesArray[i];
    
    env->ReleaseLongArrayElements(binarySizes, body, 0);
    
    delete[] valuesArray;
    
    return result;
}

// Kernel creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createKernelNative(JNIEnv *env, jclass callingClass, jlong pointerProgram, jstring kernelName) {
    cl_int errcode_ret;
    const char* nativeKernelName = env->GetStringUTFChars(kernelName, 0);
    
    cl_kernel kernel = clCreateKernel((cl_program)pointerProgram, nativeKernelName, &errcode_ret);
    
    env->ReleaseStringUTFChars(kernelName, nativeKernelName);
    
    checkResult(errcode_ret, env);
    
    return (long long)kernel;
}

// Kernel release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseKernelNative(JNIEnv *env, jclass callingClass, jlong pointerKernel) {
    checkResult(clReleaseKernel((cl_kernel)pointerKernel), env);
}

// Kernel infos
JNIEXPORT jint JNICALL Java_com_centaurean_clmax_schema_CL_getKernelInfoIntNative(JNIEnv *env, jclass callingClass, jlong pointerKernel, jint parameter) {
    int result;
    size_t retsize;
    
    checkResult(clGetKernelInfo((cl_kernel)pointerKernel, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_getKernelInfoLongNative(JNIEnv *env, jclass callingClass, jlong pointerKernel, jint parameter) {
    long result;
    size_t retsize;
    
    checkResult(clGetKernelInfo((cl_kernel)pointerKernel, parameter, sizeof(&result), &result, &retsize), env);
    
    return result;
}

JNIEXPORT jstring JNICALL Java_com_centaurean_clmax_schema_CL_getKernelInfoStringNative(JNIEnv *env, jclass callingClass, jlong pointerKernel, jint parameter) {
    char* info = new char[MAX_CL_KERNEL_INFO_SIZE * sizeof(char)];
    size_t retsize;
    
    checkResult(clGetKernelInfo((cl_kernel)pointerKernel, parameter, MAX_CL_KERNEL_INFO_SIZE, info, &retsize), env);
    
    jstring result = env->NewStringUTF(info);
    
    delete[] info;
    
    return result;
}

// Kernel args
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_setKernelArgBufferNative(JNIEnv *env, jclass callingClass, jlong pointerKernel, jint argIndex, jlong pointerBuffer) {
    checkResult(clSetKernelArg((cl_kernel)pointerKernel, argIndex, sizeof(cl_mem), &pointerBuffer), env);
}

JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_setKernelArgIntNative(JNIEnv *env, jclass callingClass, jlong pointerKernel, jint argIndex, jint value) {
    checkResult(clSetKernelArg((cl_kernel)pointerKernel, argIndex, sizeof(jint), &value), env);
}

// Kernel run
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_runKernelNative(JNIEnv *env, jclass callingClass, jlong pointerKernel, jlong pointerCommandQueue) {
    size_t global_work_size[] = {1024};
    checkResult(clEnqueueNDRangeKernel((cl_command_queue)pointerCommandQueue, (cl_kernel)pointerKernel, 1, NULL, global_work_size, NULL, 0, NULL, NULL), env);
    checkResult(clFinish((cl_command_queue)pointerCommandQueue), env);
    //fprintf(stderr, "Finished");
}

// Buffer creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createBufferNative(JNIEnv *env, jclass callingClass, jlong pointerContext, jobject buffer, jint flags) {
    cl_int errcode_ret;
    
    cl_mem clBuffer = clCreateBuffer((cl_context)pointerContext, flags | CL_MEM_USE_HOST_PTR, (size_t)env->GetDirectBufferCapacity(buffer), env->GetDirectBufferAddress(buffer), &errcode_ret);
    
    checkResult(errcode_ret, env);
    
    return (long long)clBuffer;
}

// Mem object release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseMemObjectNative(JNIEnv *env, jclass callingClass, jlong pointerMemObject) {
    checkResult(clReleaseMemObject((cl_mem)pointerMemObject), env);
}

// Buffer mapping
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_mapBufferNative(JNIEnv *env, jclass callingClass, jlong pointerCommandQueue, jlong pointerBuffer, jint mapFlags, jint bufferSize) {
    cl_int errcode_ret;
    
    void* address = clEnqueueMapBuffer((cl_command_queue)pointerCommandQueue, (cl_mem)pointerBuffer, CL_TRUE, mapFlags, 0, bufferSize, 0, NULL, NULL, &errcode_ret);
    /*fprintf(stderr, "%lld,", (long long)address);
    float* buf = (float*)address;
    for(int i = 0; i < 10; i ++)
        fprintf(stderr, "%g,", buf[i]);*/
    
    checkResult(errcode_ret, env);
}

// Mem object unmapping
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_unmapMemObjectNative(JNIEnv *env, jclass callingClass, jlong pointerCommandQueue, jlong pointerMemObject, jobject hostBuffer) {
    //fprintf(stderr, "%lld,", (long long)(*env)->GetDirectBufferAddress(env, hostBuffer));
    checkResult(clEnqueueUnmapMemObject((cl_command_queue)pointerCommandQueue, (cl_mem)pointerMemObject, env->GetDirectBufferAddress(hostBuffer), 0, NULL, NULL), env);
}

// Command queue creation
JNIEXPORT jlong JNICALL Java_com_centaurean_clmax_schema_CL_createCommandQueueNative(JNIEnv *env, jclass callingClass, jlong pointerContext, jlong pointerDevice) {
    cl_int errcode_ret;
    
    cl_command_queue commandQueue = clCreateCommandQueue((cl_context)pointerContext, (cl_device_id)pointerDevice, 0, &errcode_ret);
    
    checkResult(errcode_ret, env);
    
    return (long long)commandQueue;
}

// Command queue release
JNIEXPORT void JNICALL Java_com_centaurean_clmax_schema_CL_releaseCommandQueueNative(JNIEnv *env, jclass callingClass, jlong pointerCommandQueue) {
    checkResult(clReleaseCommandQueue((cl_command_queue)pointerCommandQueue), env);
}
