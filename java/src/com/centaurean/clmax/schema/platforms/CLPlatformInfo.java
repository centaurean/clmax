package com.centaurean.clmax.schema.platforms;

import com.centaurean.clmax.cache.CLQueryCacheKey;
import com.centaurean.clmax.schema.values.CLValueType;

import static com.centaurean.clmax.schema.values.CLValueType.CHAR_ARRAY;

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
 *                                            @
 * CLmax
 *
 * 27/03/13 01:53
 * @author gpnuma
 */
public enum CLPlatformInfo implements CLQueryCacheKey {
    CL_PLATFORM_PROFILE(0x0900, CHAR_ARRAY),
    CL_PLATFORM_VERSION(0x0901, CHAR_ARRAY),
    CL_PLATFORM_NAME(0x0902, CHAR_ARRAY),
    CL_PLATFORM_VENDOR(0x0903, CHAR_ARRAY),
    CL_PLATFORM_EXTENSIONS(0x0904, CHAR_ARRAY);

    private int key;
    private CLValueType returnType;

    private CLPlatformInfo(int key, CLValueType returnType) {
        this.key = key;
        this.returnType = returnType;
    }

    @Override
    public int getKey() {
        return key;
    }

    public CLValueType getReturnType() {
        return returnType;
    }
}
