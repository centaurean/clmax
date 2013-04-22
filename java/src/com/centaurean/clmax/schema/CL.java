package com.centaurean.clmax.schema;

import java.nio.ByteBuffer;

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
 * 24/03/13 16:33
 * @author gpnuma
 */
public class CL {
    static {
        System.loadLibrary("clmax");
    }

    public static native long[] getPlatformsNative();
    public static native String getPlatformInfoNative(long pointerPlatform, int parameter);

    public static native long[] getDevicesNative(long pointerPlatform, long type);
    public static native int getDeviceInfoIntNative(long pointerDevice, int parameter);
    public static native long getDeviceInfoLongNative(long pointerDevice, int parameter);
    public static native long[] getDeviceInfoLongArrayNative(long pointerDevice, int parameter);
    public static native String getDeviceInfoStringNative(long pointerDevice, int parameter);

    public static native long createContextNative(long pointerPlatform, long[] pointersDevices);
    public static native long createCLGLContextNative(long pointerPlatform);
    public static native void releaseContextNative(long pointerContext);
    public static native int getContextInfoIntNative(long pointerContext, int parameter);
    public static native long[] getContextInfoLongArrayNative(long pointerContext, int parameter);

    public static native long createProgramWithSourceNative(long pointerContext, String program);
    public static native void releaseProgramNative(long pointerProgram);
    public static native void buildProgramNative(long pointerProgram, long[] pointersDevices, String options);
    public static native int getProgramInfoIntNative(long pointerProgram, int parameter);
    public static native long getProgramInfoLongNative(long pointerProgram, int parameter);
    public static native long[] getProgramInfoLongArrayNative(long pointerProgram, int parameter);
    public static native String getProgramInfoStringNative(long pointerProgram, int parameter);
    public static native byte[][] getProgramInfoBinariesNative(long pointerProgram, long[] binarySizes);
    public static native int getProgramBuildInfoIntNative(long pointerProgram, long pointerDevice, int parameter);
    public static native String getProgramBuildInfoStringNative(long pointerProgram, long pointerDevice, int parameter);

    public static native long createKernelNative(long pointerProgram, String kernelName);
    public static native void releaseKernelNative(long pointerKernel);
    public static native int getKernelInfoIntNative(long pointerKernel, int parameter);
    public static native long getKernelInfoLongNative(long pointerKernel, int parameter);
    public static native String getKernelInfoStringNative(long pointerKernel, int parameter);
    public static native void setKernelArgBufferNative(long pointerKernel, int argIndex, long pointerBuffer);
    public static native void setKernelArgIntNative(long pointerKernel, int argIndex, int value);
    public static native void runKernelNative(long pointerKernel, long pointerCommandQueue, int[] globalWorkSizes);

    public static native long createBufferNative(long pointerContext, ByteBuffer hostBuffer, int flags);
    public static native void releaseMemObjectNative(long pointerMemObject);
    public static native void mapBufferNative(long pointerCommandQueue, long pointerBuffer, int mapFlags, int bufferSize);
    public static native void unmapMemObjectNative(long pointerCommandQueue, long pointerMemObject, ByteBuffer hostBuffer);
    public static native int getMemInfoIntNative(long pointerMemObject, int parameter);
    public static native int getMemInfoLongNative(long pointerMemObject, int parameter);

    public static native long createCommandQueueNative(long pointerContext, long pointerDevice);
    public static native void releaseCommandQueueNative(long pointerCommandQueue);
    public static native int getCommandQueueInfoIntNative(long pointerCommandQueue, int parameter);
    public static native long getCommandQueueInfoLongNative(long pointerCommandQueue, int parameter);
}
