package com.centaurean.clmax.schema.programs;

import com.centaurean.clmax.cache.CLQueryCacheKey;
import com.centaurean.clmax.schema.values.CLValueType;
import com.centaurean.clmax.schema.versions.CLVersion;
import com.centaurean.clmax.schema.versions.CLVersionMatcher;

import static com.centaurean.clmax.schema.values.CLValueType.*;
import static com.centaurean.clmax.schema.versions.CLVersion.OPENCL_1_0;
import static com.centaurean.clmax.schema.versions.CLVersion.OPENCL_1_2;

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
 * 28/03/13 18:04
 * @author gpnuma
 */
public enum CLProgramInfo implements CLQueryCacheKey, CLVersionMatcher {
    // OpenCL 1.0
    CL_PROGRAM_REFERENCE_COUNT(0x1160, INT),
    CL_PROGRAM_CONTEXT(0x1161, LONG),
    CL_PROGRAM_NUM_DEVICES(0x1162, INT),
    CL_PROGRAM_DEVICES(0x1163, LONG_ARRAY),
    CL_PROGRAM_SOURCE(0x1164, CHAR_ARRAY),
    CL_PROGRAM_BINARY_SIZES(0x1165, LONG_ARRAY),
    CL_PROGRAM_BINARIES(0x1166, BYTE_ARRAY_ARRAY),

    // OpenCL 1.2
    CL_PROGRAM_NUM_KERNELS(0x1167, LONG, OPENCL_1_2),
    CL_PROGRAM_KERNEL_NAMES(0x1168, CHAR_ARRAY, OPENCL_1_2);

    private int key;
    private CLValueType returnType;
    private CLVersion minimumVersion;

    private CLProgramInfo(int key, CLValueType returnType, CLVersion minimumVersion) {
        this.key = key;
        this.returnType = returnType;
        this.minimumVersion = minimumVersion;
    }

    private CLProgramInfo(int key, CLValueType returnType) {
        this(key, returnType, OPENCL_1_0);
    }

    @Override
    public int getKey() {
        return key;
    }

    public CLValueType getReturnType() {
        return returnType;
    }

    @Override
    public CLVersion getMinimumCLVersion() {
        return minimumVersion;
    }
}