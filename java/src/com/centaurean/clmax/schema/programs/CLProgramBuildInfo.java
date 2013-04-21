package com.centaurean.clmax.schema.programs;

import com.centaurean.clmax.cache.CLQueryCacheKey;
import com.centaurean.clmax.schema.values.CLValueType;
import com.centaurean.clmax.schema.versions.CLVersion;
import com.centaurean.clmax.schema.versions.CLVersionMatcher;

import static com.centaurean.clmax.schema.values.CLValueType.CHAR_ARRAY;
import static com.centaurean.clmax.schema.values.CLValueType.INT;
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
 * 21/04/13 20:20
 * @author gpnuma
 */
public enum CLProgramBuildInfo implements CLQueryCacheKey, CLVersionMatcher {
    // OpenCL 1.0
    CL_PROGRAM_BUILD_STATUS(0x1181, INT),
    CL_PROGRAM_BUILD_OPTIONS(0x1182, CHAR_ARRAY),
    CL_PROGRAM_BUILD_LOG(0x1183, CHAR_ARRAY),

    // OpenCL 1.2
    CL_PROGRAM_BINARY_TYPE(0x1184, INT, OPENCL_1_2);

    private int key;
    private CLValueType returnType;
    private CLVersion minimumVersion;

    private CLProgramBuildInfo(int key, CLValueType returnType, CLVersion minimumVersion) {
        this.key = key;
        this.returnType = returnType;
        this.minimumVersion = minimumVersion;
    }

    private CLProgramBuildInfo(int key, CLValueType returnType) {
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