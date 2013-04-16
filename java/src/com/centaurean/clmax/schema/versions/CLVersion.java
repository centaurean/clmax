package com.centaurean.clmax.schema.versions;

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
 * 25/03/13 19:41
 * @author gpnuma
 */
public class CLVersion implements Comparable<CLVersion> {
    public static final CLVersion OPENCL_1_0 = new CLVersion(1, 0);
    public static final CLVersion OPENCL_1_1 = new CLVersion(1, 1);
    public static final CLVersion OPENCL_1_2 = new CLVersion(1, 2);

    private int major;
    private int minor;
    private String infos;

    public static CLVersion parse(String parseString) {
        int indexMajor = parseString.indexOf(' ') + 1;
        int indexMinor = parseString.indexOf('.', indexMajor) + 1;
        int indexInfos = parseString.indexOf(' ', indexMinor);
        return new CLVersion(Integer.decode(parseString.substring(indexMajor, indexMajor + 1)), Integer.decode(parseString.substring(indexMinor, indexMinor + 1)), parseString.substring(indexInfos));
    }

    public CLVersion(int major, int minor, String infos) {
        this.major = major;
        this.minor = minor;
        this.infos = infos;
    }

    public CLVersion(int major, int minor) {
        this(major, minor, "");
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getInfos() {
        return infos;
    }

    @Override
    public int compareTo(CLVersion version) {
        if(getMajor() != version.getMajor())
            return getMajor() - version.getMajor();
        return getMinor() - version.getMinor();
    }

    @Override
    public int hashCode() {
        return (getMajor() << 8) + getMinor();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (object == this)
            return true;
        if (!(object instanceof CLVersion))
            return false;
        CLVersion version = (CLVersion) object;
        return (getMajor() == version.getMajor() && getMinor() == version.getMinor());
    }

    public String majorMinor() {
        return "OpenCL " + getMajor() + "." + getMinor();
    }

    @Override
    public String toString() {
        return majorMinor() + getInfos();
    }
}
