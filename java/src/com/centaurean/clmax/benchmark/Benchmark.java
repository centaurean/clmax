package com.centaurean.clmax.benchmark;

import com.centaurean.clmax.schema.Platform;
import com.centaurean.clmax.schema.impl.PlatformsImpl;
import com.centaurean.commons.chronometers.Chronometer;
import com.centaurean.commons.logs.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

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
 * 23/03/13 19:55
 * @author gpnuma
 */
public class Benchmark {
    static {
        System.loadLibrary("clmax");
    }

    public static final int SIZE = 65536 * 16384;

    public Benchmark() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(SIZE);
        buffer.order(ByteOrder.nativeOrder());
        if (!buffer.isDirect())
            throw new RuntimeException("Not direct");

        IntBuffer viewAsInt = buffer.asIntBuffer();
        for (int i = 0; i < SIZE / 4; i++)
            viewAsInt.put(i);

        display(viewAsInt);

        Chronometer chronometer = new Chronometer();
        chronometer.start();
        process(viewAsInt);
        chronometer.stop();
        Log.message("JNI : " + chronometer.toString());

        chronometer.reset();
        chronometer.start();
        for (int i = 0; i < SIZE / 4; i++)
            viewAsInt.put(i, viewAsInt.get(i) + 1);
        chronometer.stop();
        Log.message("JVM : " + chronometer.toString());

        display(viewAsInt);

        System.out.println();
        PlatformsImpl platforms = new PlatformsImpl();
        platforms.populate();
        for (Platform platform : platforms.values())
            Log.message(platform.toString());
    }

    public static void main(String... args) {
        new Benchmark();
    }

    private void display(IntBuffer buffer) {
        buffer.rewind();
        for (int i = 0; i < 10; i++)
            System.out.print(buffer.get() + " ");
        System.out.println();
    }

    private native void process(IntBuffer buffer);
}
