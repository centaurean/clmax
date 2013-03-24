package com.centaurean.clmax.schema;

/*
 * Copyright (c) 2013, Centaurean software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Centaurean software nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Centaurean software BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * jetFlow
 *
 * 23/03/13 22:57
 * @author gpnuma
 */
public class CLDevice {
    public static final int CL_DEVICE_TYPE = 0x1000;
    public static final int CL_DEVICE_VENDOR_ID = 0x1001;
    public static final int CL_DEVICE_MAX_COMPUTE_UNITS = 0x1002;
    public static final int CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = 0x1003;
    public static final int CL_DEVICE_MAX_WORK_GROUP_SIZE = 0x1004;
    public static final int CL_DEVICE_MAX_WORK_ITEM_SIZES = 0x1005;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_CHAR = 0x1006;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_SHORT = 0x1007;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_INT = 0x1008;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_LONG = 0x1009;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_FLOAT = 0x100A;
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_DOUBLE = 0x100B;
    public static final int CL_DEVICE_MAX_CLOCK_FREQUENCY = 0x100C;
    public static final int CL_DEVICE_ADDRESS_BITS = 0x100D;
    public static final int CL_DEVICE_MAX_READ_IMAGE_ARGS = 0x100E;
    public static final int CL_DEVICE_MAX_WRITE_IMAGE_ARGS = 0x100F;
    public static final int CL_DEVICE_MAX_MEM_ALLOC_SIZE = 0x1010;
    public static final int CL_DEVICE_IMAGE2D_MAX_WIDTH = 0x1011;
    public static final int CL_DEVICE_IMAGE2D_MAX_HEIGHT = 0x1012;
    public static final int CL_DEVICE_IMAGE3D_MAX_WIDTH = 0x1013;
    public static final int CL_DEVICE_IMAGE3D_MAX_HEIGHT = 0x1014;
    public static final int CL_DEVICE_IMAGE3D_MAX_DEPTH = 0x1015;
    public static final int CL_DEVICE_IMAGE_SUPPORT = 0x1016;
    public static final int CL_DEVICE_MAX_PARAMETER_SIZE = 0x1017;
    public static final int CL_DEVICE_MAX_SAMPLERS = 0x1018;
    public static final int CL_DEVICE_MEM_BASE_ADDR_ALIGN = 0x1019;
    public static final int CL_DEVICE_MIN_DATA_TYPE_ALIGN_SIZE = 0x101A;
    public static final int CL_DEVICE_SINGLE_FP_CONFIG = 0x101B;
    public static final int CL_DEVICE_GLOBAL_MEM_CACHE_TYPE = 0x101C;
    public static final int CL_DEVICE_GLOBAL_MEM_CACHELINE_SIZE = 0x101D;
    public static final int CL_DEVICE_GLOBAL_MEM_CACHE_SIZE = 0x101E;
    public static final int CL_DEVICE_GLOBAL_MEM_SIZE = 0x101F;
    public static final int CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE = 0x1020;
    public static final int CL_DEVICE_MAX_CONSTANT_ARGS = 0x1021;
    public static final int CL_DEVICE_LOCAL_MEM_TYPE = 0x1022;
    public static final int CL_DEVICE_LOCAL_MEM_SIZE = 0x1023;
    public static final int CL_DEVICE_ERROR_CORRECTION_SUPPORT = 0x1024;
    public static final int CL_DEVICE_PROFILING_TIMER_RESOLUTION = 0x1025;
    public static final int CL_DEVICE_ENDIAN_LITTLE = 0x1026;
    public static final int CL_DEVICE_AVAILABLE = 0x1027;
    public static final int CL_DEVICE_COMPILER_AVAILABLE = 0x1028;
    public static final int CL_DEVICE_EXECUTION_CAPABILITIES = 0x1029;
    public static final int CL_DEVICE_QUEUE_PROPERTIES = 0x102A;
    public static final int CL_DEVICE_NAME = 0x102B;
    public static final int CL_DEVICE_VENDOR = 0x102C;
    public static final int CL_DRIVER_VERSION = 0x102D;
    public static final int CL_DEVICE_PROFILE = 0x102E;
    public static final int CL_DEVICE_VERSION = 0x102F;
    public static final int CL_DEVICE_EXTENSIONS = 0x1030;
    public static final int CL_DEVICE_PLATFORM = 0x1031;

    // OpenCL 1.1
    public static final int CL_DEVICE_PREFERRED_VECTOR_WIDTH_HALF       = 0x1034;
    public static final int CL_DEVICE_HOST_UNIFIED_MEMORY               = 0x1035;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_CHAR          = 0x1036;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_SHORT         = 0x1037;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_INT           = 0x1038;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_LONG          = 0x1039;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_FLOAT         = 0x103A;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_DOUBLE        = 0x103B;
    public static final int CL_DEVICE_NATIVE_VECTOR_WIDTH_HALF          = 0x103C;
    public static final int CL_DEVICE_OPENCL_C_VERSION                  = 0x103D;

    // OpenCL 1.2
    public static final int CL_DEVICE_LINKER_AVAILABLE                  = 0x103E;
    public static final int CL_DEVICE_BUILT_IN_KERNELS                  = 0x103F;
    public static final int CL_DEVICE_IMAGE_MAX_BUFFER_SIZE             = 0x1040;
    public static final int CL_DEVICE_IMAGE_MAX_ARRAY_SIZE              = 0x1041;
    public static final int CL_DEVICE_PARENT_DEVICE                     = 0x1042;
    public static final int CL_DEVICE_PARTITION_MAX_SUB_DEVICES         = 0x1043;
    public static final int CL_DEVICE_PARTITION_PROPERTIES              = 0x1044;
    public static final int CL_DEVICE_PARTITION_AFFINITY_DOMAIN         = 0x1045;
    public static final int CL_DEVICE_PARTITION_TYPE                    = 0x1046;
    public static final int CL_DEVICE_REFERENCE_COUNT                   = 0x1047;
    public static final int CL_DEVICE_PREFERRED_INTEROP_USER_SYNC       = 0x1048;
    public static final int CL_DEVICE_PRINTF_BUFFER_SIZE                = 0x1049;

    private long pointer;
    private long type = Long.MAX_VALUE;
    private int vendorId = Integer.MAX_VALUE;
    private int maxComputeUnits = Integer.MAX_VALUE;

    private String name;
    private String vendor;
    private String version;
    /*private int addressBits;
    private boolean available;
    private boolean compilerAvailable;
    private bitField doubleFpConfig;
    private boolean endianLittle;
    private boolean errorCorrectionSupport;
    private bitField executionCapabilities;
    private String extensions;
    private long globalMemCacheSize;
    private bitField globalMemCacheType;
    private int globalMemCachelineSize;
    private long globalMemSize;
    private bitField halfFpConfig;
    private boolean imageSupport;
    private size_t image2dMaxHeight;
    private size_t image2dMaxWidth;
    private size_t image3dMaxDepth;
    private size_t image3dMaxHeight;
    private size_t image3dMaxWidth;
    private long localMemSize;
    private bitField localMemType;
    private int maxClockFrequency;
    private int maxConstantArgs;
    private long maxConstantBufferSize;
    private long maxMemAllocSize;
    private size_t maxParameterSize;
    private int maxReadImageArgs;
    private int maxSamplers;
    private size_t maxWorkGroupSize;
    private int maxWorkItemDimensions;
    private size_t[] maxWorkItemSizes;
    private int maxWriteImageArgs;
    private int memBaseAddrAlign;
    private int minDataTypeAlignSize;
    private long platformPointer;
    private int preferredVectorWidthChar;
    private int preferredVectorWidthShort;
    private int preferredVectorWidthInt;
    private int preferredVectorWidthLong;
    private int preferredVectorWidthFloat;
    private int preferredVectorWidthDouble;
    private String profile;
    private size_t profilingTimerResolution;
    private bitField queueProperties;
    private bitField singleFpConfig;
    private String vendor;
    private String version; */
    private short majorVersion;
    private short minorVersion;

    CLDevice(long pointer) {
        this.pointer = pointer;
    }

    public long getPointer() {
        return pointer;
    }

    public long getType() {
        if(type == Long.MAX_VALUE)
            type = CL.getDeviceInfoLongNative(getPointer(), CL_DEVICE_TYPE);
        return type;
    }

    public int getVendorId() {
        if(vendorId == Integer.MAX_VALUE)
            vendorId = (int)CL.getDeviceInfoLongNative(getPointer(), CL_DEVICE_VENDOR_ID);
        return vendorId;
    }

    public int getMaxComputeUnits() {
        if(maxComputeUnits == Integer.MAX_VALUE)
            maxComputeUnits = (int)CL.getDeviceInfoLongNative(getPointer(), CL_DEVICE_MAX_COMPUTE_UNITS);
        return maxComputeUnits;
    }

    public String getName() {
        if(name == null)
            name = CL.getDeviceInfoStringNative(getPointer(), CL_DEVICE_NAME);
        return name;
    }

    public String getVersion() {
        if(version == null)
            version = CL.getDeviceInfoStringNative(getPointer(), CL_DEVICE_VERSION);
        int indexMajor = version.indexOf(' ') + 1;
        this.majorVersion = Short.decode(version.substring(indexMajor, indexMajor + 1));
        int indexMinor = version.indexOf('.') + 1;
        this.minorVersion = Short.decode(version.substring(indexMinor, indexMinor + 1));
        return version;
    }

    public short getMajorVersion() {
        if(version == null)
            getVersion();
        return majorVersion;
    }

    public short getMinorVersion() {
        if(version == null)
            getVersion();
        return minorVersion;
    }

    public String getVendor() {
        if(vendor == null)
            vendor = CL.getDeviceInfoStringNative(getPointer(), CL_DEVICE_VENDOR);
        return vendor;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{pointer='").append(getPointer()).append("', type='").append(getType()).append("', vendorId='").append(getVendorId()).append("', maxComputeUnits='").append(getMaxComputeUnits())
                .append("', name='").append(getName()).append("', version='").append(getVersion()).append("', vendor='").append(getVendor()).append("'}");
        return stringBuilder.toString();
    }
}
