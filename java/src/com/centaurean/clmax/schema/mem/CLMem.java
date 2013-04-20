package com.centaurean.clmax.schema.mem;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.CLCachedObject;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.platforms.CLPlatform;
import com.centaurean.clmax.schema.values.CLValue;
import com.centaurean.clmax.schema.versions.exceptions.CLVersionException;

import java.util.LinkedList;

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
 * 15/04/13 02:25
 * @author gpnuma
 */
public class CLMem extends CLCachedObject<CLMemInfo> {
    private CLPlatform platform;
    private CLContext context;

    public CLMem(long pointer, CLPlatform platform, CLContext context) {
        super(pointer);
        this.platform = platform;
        this.context = context;
    }

    @Override
    public CLValue get(CLMemInfo memInfo) {
        if (platform.getVersion().compareTo(memInfo.getMinimumCLVersion()) < 0)
            throw new CLVersionException(memInfo.name() + " (" + memInfo.getMinimumCLVersion().majorMinor() + " function) not supported by this " + platform.getVersion().majorMinor() + " platform.");
        CLValue valueInCache = CLQueryCache.get(getPointer(), memInfo);
        if (valueInCache == null) {
            switch (memInfo.getReturnType()) {
                case INT:
                    valueInCache = new CLValue(CL.getMemInfoIntNative(getPointer(), memInfo.getKey()));
                    break;
                case LONG:
                case SIZE_T:
                case BIT_FIELD:
                    valueInCache = new CLValue(CL.getMemInfoLongNative(getPointer(), memInfo.getKey()));
                    break;
            }
            CLQueryCache.add(getPointer(), memInfo, valueInCache);
        }
        return valueInCache;
    }

    @Override
    public String toString() {
        LinkedList<CLMemInfo> displayList = new LinkedList<CLMemInfo>();
        for (CLMemInfo memInfo : CLMemInfo.values())
            if (platform.getVersion().compareTo(memInfo.getMinimumCLVersion()) > 0)
                displayList.add(memInfo);
        return toString(displayList);
    }
}
