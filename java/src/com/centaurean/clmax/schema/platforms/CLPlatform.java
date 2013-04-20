package com.centaurean.clmax.schema.platforms;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.CLCachedObject;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.devices.CLDevice;
import com.centaurean.clmax.schema.devices.CLDeviceType;
import com.centaurean.clmax.schema.devices.CLDevices;
import com.centaurean.clmax.schema.values.CLValue;
import com.centaurean.clmax.schema.versions.CLVersion;

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
 * 23/03/13 21:44
 * @author gpnuma
 */
public class CLPlatform extends CLCachedObject<CLPlatformInfo> {
    private CLDevices devices = null;
    private CLVersion version;

    CLPlatform(long pointer) {
        super(pointer);
    }

    public CLDevices getDevices(CLDeviceType type) {
        if (devices == null || type != devices.getType()) {
            long[] pointers = CL.getDevicesNative(getPointer(), type.getType());
            devices = new CLDevices(type);
            for (long pointer : pointers)
                devices.add(new CLDevice(pointer));
        }
        return devices;
    }

    public CLDevices attachedDevices() {
        return devices;
    }

    public CLContext createContext() {
        return new CLContext(CL.createContextNative(getPointer(), devices.getPointers()), devices, this);
    }

    public CLContext createCLGLContext() {
        return new CLContext(CL.createCLGLContextNative(getPointer()), devices, this);
    }

    public CLVersion getVersion() {
        if (version == null)
            version = CLVersion.parse(get(CLPlatformInfo.CL_PLATFORM_VERSION).getString());
        return version;
    }

    protected CLValue get(CLPlatformInfo platformInfo) {
        CLValue valueInCache = CLQueryCache.get(getPointer(), platformInfo);
        if (valueInCache == null) {
            switch (platformInfo.getReturnType()) {
                case CHAR_ARRAY:
                    valueInCache = new CLValue(CL.getPlatformInfoNative(getPointer(), platformInfo.getKey()));
                    break;
            }
            CLQueryCache.add(getPointer(), platformInfo, valueInCache);
        }
        return valueInCache;
    }

    @Override
    public String toString() {
        return toString(Arrays.asList(CLPlatformInfo.values()));
    }
}
