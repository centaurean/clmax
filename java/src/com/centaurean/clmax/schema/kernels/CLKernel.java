package com.centaurean.clmax.schema.kernels;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.CLObject;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.exceptions.CLException;
import com.centaurean.clmax.schema.exceptions.CLNativeException;
import com.centaurean.clmax.schema.mem.buffers.CLBuffer;
import com.centaurean.clmax.schema.platforms.CLPlatform;
import com.centaurean.clmax.schema.queues.CLCommandQueue;
import com.centaurean.clmax.schema.values.CLValue;
import com.centaurean.clmax.schema.versions.exceptions.CLVersionException;
import com.centaurean.commons.logs.Log;

import java.util.concurrent.locks.ReentrantLock;

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
 * 31/03/13 20:59
 * @author gpnuma
 */
public class CLKernel extends CLObject {
    private static final ReentrantLock lock = new ReentrantLock(true);

    private CLPlatform platform;
    private CLContext context;
    private int argIndex;

    public CLKernel(long pointer, CLPlatform platform, CLContext context) {
        super(pointer);
        this.platform = platform;
        this.context = context;
        argIndex = 0;
    }

    private CLValue get(CLKernelInfo kernelInfo) {
        if (platform.getVersion().compareTo(kernelInfo.getMinimumCLVersion()) < 0)
            throw new CLVersionException(kernelInfo.name() + " (" + kernelInfo.getMinimumCLVersion().majorMinor() + " function) not supported by this " + platform.getVersion().majorMinor() + " platform.");
        CLValue valueInCache = CLQueryCache.get(getPointer(), kernelInfo);
        if (valueInCache == null) {
            switch (kernelInfo.getReturnType()) {
                case INT:
                    valueInCache = new CLValue(CL.getKernelInfoIntNative(getPointer(), kernelInfo.getKey()));
                    break;
                case LONG:
                    valueInCache = new CLValue(CL.getKernelInfoLongNative(getPointer(), kernelInfo.getKey()));
                    break;
                case CHAR_ARRAY:
                    valueInCache = new CLValue(CL.getKernelInfoStringNative(getPointer(), kernelInfo.getKey()));
                    break;
            }
            CLQueryCache.add(getPointer(), kernelInfo, valueInCache);
        }
        return valueInCache;
    }

    public void release() {
        CL.releaseKernelNative(getPointer());
    }

    public CLKernel setArg(int index, CLBuffer buffer) {
        lock.lock();
        CL.setKernelArgBufferNative(getPointer(), index, buffer.getPointer());
        argIndex = index + 1;
        lock.unlock();
        return this;
    }

    public CLKernel setArgs(CLBuffer... buffers) {
        for (int i = 0; i < buffers.length; i++)
            setArg(argIndex, buffers[i]);
        return this;
    }

    public CLKernel setArg(int index, int value) {
        lock.lock();
        CL.setKernelArgIntNative(getPointer(), index, value);
        argIndex = index + 1;
        lock.unlock();
        return this;
    }

    public CLKernel setArg(int value) {
        return setArg(argIndex, value);
    }

    public void runIn(CLCommandQueue commandQueue) {
        if(!commandQueue.getContext().equals(getContext()))
            throw new CLException("The OpenCL context associated with kernel and command-queue must be the same !");
        CL.runKernelNative(getPointer(), commandQueue.getPointer());
    }

    public CLPlatform getPlatform() {
        return platform;
    }

    public CLContext getContext() {
        return context;
    }

    public int getArgIndex() {
        return argIndex;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append(super.toString());
        for (CLKernelInfo kernelInfo : CLKernelInfo.values()) {
            if (platform.getVersion().compareTo(kernelInfo.getMinimumCLVersion()) > 0)
                try {
                    stringBuilder.append(", ").append(kernelInfo.name()).append("='").append(get(kernelInfo));
                } catch (CLNativeException exception) {
                    Log.message(new CLException("[Kernel " + super.toString() + "] Querying kernel info " + kernelInfo.name() + " returned error " + exception.getMessage()));
                } finally {
                    stringBuilder.append("'");
                }
        }
        return stringBuilder.append("}").toString();
    }
}
