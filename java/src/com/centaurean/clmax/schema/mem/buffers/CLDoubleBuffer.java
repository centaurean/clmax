package com.centaurean.clmax.schema.mem.buffers;

import com.centaurean.clmax.schema.CL;
import com.centaurean.clmax.schema.contexts.CLContext;

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
 * 24/04/13 17:48
 * @author gpnuma
 */
public class CLDoubleBuffer extends CLBuffer {
    public static CLDoubleBuffer create(CLContext context, CLBufferType type, int size) {
        ByteBuffer hostBuffer = createDirectByteBuffer(size << 3);
        return new CLDoubleBuffer(CL.createBufferNative(context.getPointer(), hostBuffer, type.getValue()), hostBuffer, context, size);
    }

    private CLDoubleBuffer(long pointerBuffer, ByteBuffer hostBuffer, CLContext context, int size) {
        super(pointerBuffer, hostBuffer, context, size);
    }

    public void putDouble(double d) {
        getHostBuffer().putDouble(d);
    }

    public double getDouble() {
        return getHostBuffer().getDouble();
    }
}
