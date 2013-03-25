package com.centaurean.clmax.schema;

import com.centaurean.clmax.schema.exceptions.CLException;
import com.centaurean.commons.utilities.Transform;

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
 * 24/03/13 17:37
 * @author gpnuma
 */
public class CLContext {
    public static final int CL_CONTEXT_REFERENCE_COUNT = 0x1080;
    public static final int CL_CONTEXT_DEVICES         = 0x1081;
    public static final int CL_CONTEXT_PROPERTIES      = 0x1082;
    public static final int CL_CONTEXT_NUM_DEVICES     = 0x1083;

    private long pointer;
    private int referenceCount = Integer.MAX_VALUE;
    private int numDevices = Integer.MAX_VALUE;
    private long[] devices;
    private long[] properties;

    CLContext(long pointer) {
        if(pointer == 0)
            throw new CLException("Context creation failed : null pointer returned");
        this.pointer = pointer;
    }

    public long getPointer() {
        return pointer;
    }

    public int getReferenceCount() {
        if(referenceCount == Integer.MAX_VALUE)
            referenceCount = (int)CL.getContextInfoLongNative(getPointer(), CL_CONTEXT_REFERENCE_COUNT);
        return referenceCount;
    }

    public int getNumDevices() {
        if(numDevices == Integer.MAX_VALUE)
            numDevices = (int)CL.getContextInfoLongNative(getPointer(), CL_CONTEXT_NUM_DEVICES);
        return numDevices;
    }

    public long[] getDevices() {
        if(devices == null)
            devices = CL.getContextInfoLongArrayNative(getPointer(), CL_CONTEXT_DEVICES);
        return devices;
    }

    public long[] getProperties() {
        if(properties == null)
            properties = CL.getContextInfoLongArrayNative(getPointer(), CL_CONTEXT_PROPERTIES);
        return properties;
    }

    public void release() {
        CL.releaseContextNative(getPointer());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{pointer='").append(Long.toHexString(getPointer())).append("', referenceCount='").append(getReferenceCount()).append("', numDevices='").append(getNumDevices())
                .append("', devices='").append(Transform.toHexArray(getDevices())).append("', properties='").append(Transform.toHexArray(getProperties()))
                .append("'}");
        return stringBuilder.toString();
    }
}
