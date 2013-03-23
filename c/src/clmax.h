#include <OpenCL/opencl.h>
#include <stdlib.h>

#include "com_centaurean_clmax_schema_impl_PlatformsImpl.h"
#include "com_centaurean_clmax_schema_CLPlatforms.h"

struct {
    cl_uint num_platforms;
    cl_platform_id *platforms;
} memory_context;