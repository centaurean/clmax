package com.centaurean.clmax.schema.devices;

import com.centaurean.clmax.schema.versions.CLVersion;
import com.centaurean.clmax.schema.versions.CLVersionMatcher;

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
 * 25/03/13 14:53
 * @author gpnuma
 */
public enum CLDeviceType implements CLVersionMatcher {
    CL_DEVICE_TYPE_DEFAULT(1),
    CL_DEVICE_TYPE_CPU(1 << 1),
    CL_DEVICE_TYPE_GPU(1 << 2),
    CL_DEVICE_TYPE_ACCELERATOR(1 << 3),
    CL_DEVICE_TYPE_ALL(0xFFFFFFFF),
    CL_DEVICE_TYPE_CUSTOM(1 << 4, OPENCL_1_2);

    private long type;
    private CLVersion minimumVersion;

    private CLDeviceType(long type) {
        this(type, new CLVersion(1, 0));
    }

    private CLDeviceType(long type, CLVersion minimumVersion) {
        this.type = type;
        this.minimumVersion = minimumVersion;
    }

    public long getType() {
        return type;
    }

    @Override
    public CLVersion getMinimumCLVersion() {
        return minimumVersion;
    }
}
