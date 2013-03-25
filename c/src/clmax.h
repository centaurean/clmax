#include <OpenCL/opencl.h>
#include <OpenGL/OpenGL.h>
#include <stdlib.h>
#include <stdio.h>

#include "com_centaurean_clmax_schema_CL.h"

void checkResult(cl_int, JNIEnv*);
jint throwCLException(JNIEnv*, char*);
