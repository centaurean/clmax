package com.centaurean.clmax.schema.mem;

import com.centaurean.clmax.schema.versions.CLVersion;
import com.centaurean.clmax.schema.versions.CLVersionMatcher;

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
 * 17/04/13 03:23
 * @author gpnuma
 */
public enum CLMapType implements CLVersionMatcher {
    READ(1),
    WRITE(1 << 1),
    WRITE_INVALIDATE_REGION(1 << 2, OPENCL_1_2);

    private int value;
    private CLVersion minimumVersion;

    private CLMapType(int value, CLVersion minimumVersion) {
        this.value = value;
        this.minimumVersion = minimumVersion;
    }

    private CLMapType(int value) {
        this(value, OPENCL_1_0);
    }

    public int getValue() {
        return value;
    }

    @Override
    public CLVersion getMinimumCLVersion() {
        return minimumVersion;
    }
}
