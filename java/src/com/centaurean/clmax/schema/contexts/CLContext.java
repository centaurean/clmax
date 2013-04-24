package com.centaurean.clmax.schema.contexts;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.CLCachedObject;
import com.centaurean.clmax.schema.devices.CLDevice;
import com.centaurean.clmax.schema.devices.CLDevices;
import com.centaurean.clmax.schema.exceptions.CLException;
import com.centaurean.clmax.schema.platforms.CLPlatform;
import com.centaurean.clmax.schema.programs.CLProgram;
import com.centaurean.clmax.schema.queues.CLCommandQueue;
import com.centaurean.clmax.schema.values.CLValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
 * 24/03/13 17:37
 * @author gpnuma
 */
public class CLContext extends CLCachedObject<CLContextInfo> {
    private static final int READ_SIZE = 1024;

    private CLPlatform platform;
    private CLDevices devices;

    public CLContext(long pointer, CLDevices devices, CLPlatform platform) {
        super(pointer);
        this.platform = platform;
        this.devices = devices;
    }

    public CLValue get(CLContextInfo contextInfo) {
        CLValue valueInCache = CLQueryCache.get(getPointer(), contextInfo);
        if (valueInCache == null) {
            switch (contextInfo.getReturnType()) {
                case INT:
                    valueInCache = new CLValue(CL.getContextInfoIntNative(getPointer(), contextInfo.getKey()));
                    break;
                case LONG_ARRAY:
                    valueInCache = new CLValue(CL.getContextInfoLongArrayNative(getPointer(), contextInfo.getKey()));
                    break;
            }
            CLQueryCache.add(getPointer(), contextInfo, valueInCache);
        }
        return valueInCache;
    }

    public void release() {
        CL.releaseContextNative(getPointer());
    }

    public CLPlatform getPlatform() {
        return platform;
    }

    public CLDevices getDevices() {
        return devices;
    }

    public CLProgram createProgram(String source) {
        return new CLProgram(CL.createProgramWithSourceNative(getPointer(), source), platform, this);
    }

    public CLProgram createProgram(File file) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int read;
        byte[] bytesRead = new byte[READ_SIZE];
        FileInputStream fileInputStream = new FileInputStream(file);
        while ((read = fileInputStream.read(bytesRead)) != -1)
            output.write(bytesRead, 0, read);
        fileInputStream.close();
        return createProgram(output.toString());
    }

    public CLCommandQueue createCommandQueue(CLDevice device) {
        if (!devices.containsKey(device.getPointer()))
            throw new CLException("Context must contain device !");
        return new CLCommandQueue(CL.createCommandQueueNative(getPointer(), device.getPointer()), this, device);
    }

    @Override
    public String toString() {
        return toString(Arrays.asList(CLContextInfo.values()));
    }
}
