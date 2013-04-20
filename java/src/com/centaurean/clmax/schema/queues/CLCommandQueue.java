package com.centaurean.clmax.schema.queues;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.CLCachedObject;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.devices.CLDevice;
import com.centaurean.clmax.schema.values.CLValue;

import java.util.Arrays;

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
 * 16/04/13 22:32
 * @author gpnuma
 */
public class CLCommandQueue extends CLCachedObject<CLCommandQueueInfo> {
    private CLContext context;
    private CLDevice device;

    public CLCommandQueue(long pointer, CLContext context, CLDevice device) {
        super(pointer);
        this.context = context;
        this.device = device;
    }

    @Override
    public CLValue get(CLCommandQueueInfo commandQueueInfo) {
        CLValue valueInCache = CLQueryCache.get(getPointer(), commandQueueInfo);
        if (valueInCache == null) {
            switch (commandQueueInfo.getReturnType()) {
                case INT:
                    valueInCache = new CLValue(CL.getCommandQueueInfoIntNative(getPointer(), commandQueueInfo.getKey()));
                    break;
                case LONG:
                case BIT_FIELD:
                    valueInCache = new CLValue(CL.getCommandQueueInfoLongNative(getPointer(), commandQueueInfo.getKey()));
                    break;
            }
            CLQueryCache.add(getPointer(), commandQueueInfo, valueInCache);
        }
        return valueInCache;
    }

    public void release() {
        CL.releaseCommandQueueNative(getPointer());
    }

    public CLContext getContext() {
        return context;
    }

    public CLDevice getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return super.toString(Arrays.asList(CLCommandQueueInfo.values()));
    }
}
