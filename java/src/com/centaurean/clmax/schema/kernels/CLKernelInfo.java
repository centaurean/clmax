package com.centaurean.clmax.schema.kernels;

import com.centaurean.clmax.cache.CLQueryCacheKey;
import com.centaurean.clmax.schema.values.CLValueType;
import com.centaurean.clmax.schema.versions.CLVersion;
import com.centaurean.clmax.schema.versions.CLVersionMatcher;

import static com.centaurean.clmax.schema.values.CLValueType.*;
import static com.centaurean.clmax.schema.versions.CLVersion.OPENCL_1_0;
import static com.centaurean.clmax.schema.versions.CLVersion.OPENCL_1_2;

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
 * CLmax
 *
 * 01/04/13 20:54
 * @author gpnuma
 */
public enum CLKernelInfo implements CLQueryCacheKey, CLVersionMatcher {
    // OpenCL 1.0
    CL_KERNEL_FUNCTION_NAME(0x1190, CHAR_ARRAY),
    CL_KERNEL_NUM_ARGS(0x1191, INT),
    CL_KERNEL_REFERENCE_COUNT(0x1192, INT),
    CL_KERNEL_CONTEXT(0x1193, LONG),
    CL_KERNEL_PROGRAM(0x1194, LONG),

    // OpenCL 1.2
    CL_KERNEL_ATTRIBUTES(0x1195, CHAR_ARRAY, OPENCL_1_2);

    private int key;
    private CLValueType returnType;
    private CLVersion minimumVersion;

    private CLKernelInfo(int key, CLValueType returnType, CLVersion minimumVersion) {
        this.key = key;
        this.returnType = returnType;
        this.minimumVersion = minimumVersion;
    }

    private CLKernelInfo(int key, CLValueType returnType) {
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