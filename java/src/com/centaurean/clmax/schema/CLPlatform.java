package com.centaurean.clmax.schema;

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
 * 23/03/13 21:44
 * @author gpnuma
 */
public class CLPlatform {
    public static final int CL_PLATFORM_PROFILE = 0x0900;
    public static final int CL_PLATFORM_VERSION = 0x0901;
    public static final int CL_PLATFORM_NAME = 0x0902;
    public static final int CL_PLATFORM_VENDOR = 0x0903;
    public static final int CL_PLATFORM_EXTENSIONS = 0x0904;

    private long pointer;
    private String profile;
    private String version;
    private String name;
    private String vendor;
    private String extensions;
    private short majorVersion;
    private short minorVersion;

    private CLDevices devices = null;

    CLPlatform(long pointer) {
        this.pointer = pointer;
    }

    public CLDevices getDevices(CLDevicesType type) {
        if(devices == null || type != devices.getType()) {
            long[] pointers = CL.getDevicesNative(getPointer(), type.getType());
            devices = new CLDevices(type);
            for(long pointer : pointers)
                devices.add(new CLDevice(pointer));
        }
        return devices;
    }

    public CLDevices attachedDevices() {
        return devices;
    }

    public CLContext createContext() {
        long pointer = CL.createContextNative(getPointer(), devices.getPointers());
        return new CLContext(pointer);
    }

    public CLContext createCLGLContext() {
        long pointer = CL.createCLGLContextNative(getPointer());
        return new CLContext(pointer);
    }

    public long getPointer() {
        return pointer;
    }

    public String getExtensions() {
        if(extensions == null)
            extensions = CL.getPlatformInfoNative(getPointer(), CL_PLATFORM_EXTENSIONS);
        return extensions;
    }

    public String getVendor() {
        if(vendor == null)
            vendor = CL.getPlatformInfoNative(getPointer(), CL_PLATFORM_VENDOR);
        return vendor;
    }

    public String getName() {
        if(name == null)
            name = CL.getPlatformInfoNative(getPointer(), CL_PLATFORM_NAME);
        return name;
    }

    /**
     * @return OpenCL<space><major_version.minor_ version><space><platform-specific information>
     */
    public String getVersion() {
        if(version == null)
            version = CL.getPlatformInfoNative(getPointer(), CL_PLATFORM_VERSION);
        int indexMajor = version.indexOf(' ') + 1;
        this.majorVersion = Short.decode(version.substring(indexMajor, indexMajor + 1));
        int indexMinor = version.indexOf('.') + 1;
        this.minorVersion = Short.decode(version.substring(indexMinor, indexMinor + 1));
        return version;
    }

    public short getMajorVersion() {
        if(version == null)
            getVersion();
        return majorVersion;
    }

    public short getMinorVersion() {
        if(version == null)
            getVersion();
        return minorVersion;
    }

    public String getProfile() {
        if(profile == null)
            profile = CL.getPlatformInfoNative(getPointer(), CL_PLATFORM_PROFILE);
        return profile;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{pointer='0x").append(Long.toHexString(getPointer())).append("', profile='").append(getProfile()).append("', version='").append(getVersion()).append("', name='").append(getName()).append("', vendor='").append(getVendor()).append("', extensions='").append(getExtensions()).append("'}");
        return stringBuilder.toString();
    }
}
