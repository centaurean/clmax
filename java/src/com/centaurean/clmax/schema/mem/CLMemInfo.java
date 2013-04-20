package com.centaurean.clmax.schema.mem;

import com.centaurean.clmax.cache.CLQueryCacheKey;
import com.centaurean.clmax.schema.values.CLValueType;
import com.centaurean.clmax.schema.versions.CLVersion;
import com.centaurean.clmax.schema.versions.CLVersionMatcher;

import static com.centaurean.clmax.schema.values.CLValueType.*;
import static com.centaurean.clmax.schema.versions.CLVersion.OPENCL_1_0;
import static com.centaurean.clmax.schema.versions.CLVersion.OPENCL_1_1;

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
 * 20/04/13 18:16
 * @author gpnuma
 */
public enum CLMemInfo implements CLQueryCacheKey, CLVersionMatcher {
    // Open CL 1.0
    CL_MEM_TYPE(0x1100, INT),
    CL_MEM_FLAGS(0x1101, BIT_FIELD),
    CL_MEM_SIZE(0x1102, SIZE_T),
    CL_MEM_HOST_PTR(0x1103, LONG),
    CL_MEM_MAP_COUNT(0x1104, INT),
    CL_MEM_REFERENCE_COUNT(0x1105, INT),
    CL_MEM_CONTEXT(0x1106, LONG),

    // OpenCL 1.1
    CL_MEM_ASSOCIATED_MEMOBJECT(0x1107, LONG, OPENCL_1_1),
    CL_MEM_OFFSET(0x1108, SIZE_T, OPENCL_1_1);

    private int key;
    private CLValueType returnType;
    private CLVersion minimumVersion;

    private CLMemInfo(int key, CLValueType returnType, CLVersion minimumVersion) {
        this.key = key;
        this.returnType = returnType;
        this.minimumVersion = minimumVersion;
    }

    private CLMemInfo(int key, CLValueType returnType) {
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