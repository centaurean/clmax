package com.centaurean.clmax.schema.programs;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.CLCachedObject;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.devices.CLDevice;
import com.centaurean.clmax.schema.devices.CLDevices;
import com.centaurean.clmax.schema.kernels.CLKernel;
import com.centaurean.clmax.schema.platforms.CLPlatform;
import com.centaurean.clmax.schema.values.CLValue;
import com.centaurean.clmax.schema.versions.exceptions.CLVersionException;

import java.io.IOException;
import java.util.Collection;
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
 * 28/03/13 11:16
 * @author gpnuma
 */
public class CLProgram extends CLCachedObject<CLProgramInfo> {
    private CLPlatform platform;
    private CLContext context;
    private Collection<CLDevice> buildDevices = new LinkedList<CLDevice>();

    public CLProgram(long pointer, CLPlatform platform, CLContext context) {
        super(pointer);
        this.platform = platform;
        this.context = context;
    }

    public CLValue get(CLProgramInfo programInfo) {
        if (platform.getVersion().compareTo(programInfo.getMinimumCLVersion()) < 0)
            throw new CLVersionException(programInfo.name() + " (" + programInfo.getMinimumCLVersion().majorMinor() + " function) not supported by this " + platform.getVersion().majorMinor() + " platform.");
        CLValue valueInCache = CLQueryCache.get(getPointer(), programInfo);
        if (valueInCache == null) {
            switch (programInfo) {
                case CL_PROGRAM_BINARIES:
                    valueInCache = new CLValue(CL.getProgramInfoBinariesNative(getPointer(), get(CLProgramInfo.CL_PROGRAM_BINARY_SIZES).getLongArray()));
                    break;
                default:
                    switch (programInfo.getReturnType()) {
                        case BOOLEAN:
                        case INT:
                            valueInCache = new CLValue(CL.getProgramInfoIntNative(getPointer(), programInfo.getKey()));
                            break;
                        case LONG:
                        case SIZE_T:
                        case BIT_FIELD:
                            valueInCache = new CLValue(CL.getProgramInfoLongNative(getPointer(), programInfo.getKey()));
                            break;
                        case CHAR_ARRAY:
                            valueInCache = new CLValue(CL.getProgramInfoStringNative(getPointer(), programInfo.getKey()));
                            break;
                        case LONG_ARRAY:
                        case SIZE_T_ARRAY:
                            valueInCache = new CLValue(CL.getProgramInfoLongArrayNative(getPointer(), programInfo.getKey()));
                            break;
                    }
                    break;
            }
            CLQueryCache.add(getPointer(), programInfo, valueInCache);
        }
        return valueInCache;
    }

    public void build(CLDevices devices, String options) {
        this.buildDevices = devices.values();
        CL.buildProgramNative(getPointer(), devices.getPointers(), options);
    }

    public void build(CLDevices devices) {
        build(devices, "");
    }

    public CLValue getBuildInfo(CLDevice device, CLProgramBuildInfo programBuildInfo) {
        if (platform.getVersion().compareTo(programBuildInfo.getMinimumCLVersion()) < 0)
            throw new CLVersionException(programBuildInfo.name() + " (" + programBuildInfo.getMinimumCLVersion().majorMinor() + " function) not supported by this " + platform.getVersion().majorMinor() + " platform.");
        CLValue valueInCache = CLQueryCache.get(getPointer(), programBuildInfo);
        if (valueInCache == null) {
            switch (programBuildInfo.getReturnType()) {
                case INT:
                    valueInCache = new CLValue(CL.getProgramBuildInfoIntNative(getPointer(), device.getPointer(), programBuildInfo.getKey()));
                    break;
                case CHAR_ARRAY:
                    valueInCache = new CLValue(CL.getProgramBuildInfoStringNative(getPointer(), device.getPointer(), programBuildInfo.getKey()));
                    break;
            }
            CLQueryCache.add(getPointer(), programBuildInfo, valueInCache);
        }
        return valueInCache;
    }

    public CLKernel createKernel(String kernelName) {
        return new CLKernel(CL.createKernelNative(getPointer(), kernelName), platform, context);
    }

    public void release() {
        CL.releaseProgramNative(getPointer());
    }

    public CLPlatform getPlatform() {
        return platform;
    }

    public CLContext getContext() {
        return context;
    }

    public Collection<CLDevice> getBuildDevices() {
        return buildDevices;
    }

    public String buildInfos(CLDevice device) throws IOException {
        String separator = "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Build infos for program ").append(getPointer()).append(" and device ").append(device.getPointer()).append(" {");
        for (CLProgramBuildInfo programBuildInfo : CLProgramBuildInfo.values())
            if (platform.getVersion().compareTo(programBuildInfo.getMinimumCLVersion()) > 0) {
                stringBuilder.append(separator).append(programBuildInfo.name()).append(" = '").append(getBuildInfo(device, programBuildInfo).toString()).append("'");
                separator = ", ";
            }
        return stringBuilder.append("}").toString();
    }

    @Override
    public String toString() {
        LinkedList<CLProgramInfo> displayList = new LinkedList<CLProgramInfo>();
        for (CLProgramInfo programInfo : CLProgramInfo.values())
            if (platform.getVersion().compareTo(programInfo.getMinimumCLVersion()) > 0)
                displayList.add(programInfo);
        return toString(displayList);
    }
}
