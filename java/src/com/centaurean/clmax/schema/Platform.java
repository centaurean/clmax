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
 * 23/03/13 19:46
 * @author gpnuma
 */
public class Platform {
    private static int idGenerator = 0;

    private long pointer;
    private String profile;
    private String version;
    private String name;
    private String vendor;
    private String extensions;
    private short majorVersion;
    private short minorVersion;

    private static synchronized int newId() {
        return ++idGenerator;
    }

    private Platform(long pointer, String profile, String version, String name, String vendor, String extensions) {
        this.pointer = pointer;
        this.profile = profile;
        this.version = version;
        this.name = name;
        this.vendor = vendor;
        this.extensions = extensions;
        int indexMajor = version.indexOf(' ') + 1;
        this.majorVersion = Short.decode(version.substring(indexMajor, indexMajor + 1));
        int indexMinor = version.indexOf('.') + 1;
        this.minorVersion = Short.decode(version.substring(indexMinor, indexMinor + 1));
    }

    public long getPointer() {
        return pointer;
    }

    public String getExtensions() {
        return extensions;
    }

    public String getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    /**
     * @return OpenCL<space><major_version.minor_ version><space><platform-specific information>
     */
    public String getVersion() {
        return version;
    }

    public short getMajorVersion() {
        return majorVersion;
    }

    public short getMinorVersion() {
        return minorVersion;
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{pointer='").append(getPointer()).append("', profile='").append(getProfile()).append("', version='").append(getVersion()).append("', name='").append(getName()).append("', vendor='").append(getVendor()).append("', extensions='").append(getExtensions()).append("'}");
        return stringBuilder.toString();
    }
}
