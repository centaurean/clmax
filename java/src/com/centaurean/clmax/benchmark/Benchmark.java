package com.centaurean.clmax.benchmark;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.devices.CLDevice;
import com.centaurean.clmax.schema.devices.CLDeviceType;
import com.centaurean.clmax.schema.devices.CLDevices;
import com.centaurean.clmax.schema.exceptions.CLNativeException;
import com.centaurean.clmax.schema.kernels.CLKernel;
import com.centaurean.clmax.schema.mem.CLMapType;
import com.centaurean.clmax.schema.mem.CLMemInfo;
import com.centaurean.clmax.schema.mem.buffers.CLBuffer;
import com.centaurean.clmax.schema.mem.buffers.CLBufferType;
import com.centaurean.clmax.schema.platforms.CLPlatform;
import com.centaurean.clmax.schema.platforms.CLPlatforms;
import com.centaurean.clmax.schema.programs.CLProgram;
import com.centaurean.clmax.schema.queues.CLCommandQueue;
import com.centaurean.commons.logs.Log;
import com.centaurean.commons.logs.LogLevel;
import com.centaurean.commons.logs.LogStatus;
import com.centaurean.commons.utilities.Transform;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
 * 23/03/13 19:55
 * @author gpnuma
 */
public class Benchmark {
    private static final int BUFFER_SIZE = 1048576;

    private static void getCLBufferContentFloatSample(CLBuffer buffer, int elements) {
        StringBuilder content = new StringBuilder();
        buffer.getHostBuffer().rewind();
        for(int i = 0; i < elements; i++)
            content.append(buffer.getHostBuffer().getFloat()).append(", ");
        Log.message(content.append("... (").append(buffer.get(CLMemInfo.CL_MEM_SIZE)).append(" elements)").toString());
    }

    public Benchmark(File clFile, String kernelName) throws IOException, InterruptedException {
         try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            byte[] shaded = sha512.digest("This is a test !".getBytes());
            Log.message(Arrays.toString(shaded));
            String hex = Transform.toHexString(shaded);
            Log.message(hex);
            Log.message(Arrays.toString(Transform.toByteArray(hex)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.startMessage("Getting platforms");
        CLPlatforms platforms = CLPlatforms.getPlatforms();
        Log.endMessage(LogStatus.OK);
        Log.message("Found " + platforms.size() + " platform(s)");
        for (CLPlatform platform : platforms.values())
            Log.message(platform);
        for (CLPlatform platform : platforms.values()) {
            Log.message();
            Log.startMessage("Getting devices for platform " + platform.getPointer());
            CLDevices devices = platform.getDevices(CLDeviceType.CL_DEVICE_TYPE_ALL);
            Log.endMessage(LogStatus.OK);
            Log.message("Found " + devices.size() + " device(s)");
            for (CLDevice device : devices.values())
                Log.message(device);
        }
        for (CLPlatform platform : platforms.values()) {
            Log.message();
            CLDevice first = platform.attachedDevices().values().iterator().next();
            Log.startMessage("Ignoring and reinstating device");
            platform.attachedDevices().ignore(first);
            platform.attachedDevices().reinstate(first);
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Creating context on platform " + platform.getPointer());
            CLContext context = platform.createContext();
            Log.endMessage(LogStatus.OK);
            Log.message(context);
            Log.startMessage("Creating command queue");
            CLCommandQueue queue = first.createCommandQueue(context);
            Log.endMessage(LogStatus.OK);
            Log.message(queue);
            Log.startMessage("Creating program");
            CLProgram program = context.createProgram(clFile);
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Building program");
            try {
                program.build(platform.attachedDevices());
            } catch (CLNativeException exception) {
                Log.endMessage(LogStatus.ERROR);
                Thread.sleep(100);
                for (CLDevice device : program.getBuildDevices())
                    Log.message(System.err, LogLevel.ERROR, program.buildInfos(device));
                throw exception;
            }
            Log.endMessage(LogStatus.OK);
            Log.message(program);
            for (CLDevice device : program.getBuildDevices())
                Log.message(program.buildInfos(device));
            /*CLProgramBinaries binaries = program.get(CLProgramInfo.CL_PROGRAM_BINARIES).getBinaries();
            for (int i = 0; i < binaries.size(); i++) {
                FileOutputStream out = new FileOutputStream("out.bn" + i);
                binaries.toStream(i, out);
                out.close();
            }*/
            Log.startMessage("Creating kernel");
            CLKernel kernel = program.createKernel(kernelName);
            Log.endMessage(LogStatus.OK);
            Log.message(kernel);
            for (CLDevice device : program.getBuildDevices())
                Log.message(kernel.workGroupInfos(device));
            Log.startMessage("Creating buffers");
            ByteBuffer a = ByteBuffer.allocateDirect(BUFFER_SIZE);
            a.order(ByteOrder.nativeOrder());
            for(int i = 0; i < BUFFER_SIZE / 4; i ++)
                a.putFloat(i);
            ByteBuffer b = ByteBuffer.allocateDirect(BUFFER_SIZE);
            b.order(ByteOrder.nativeOrder());
            for(int i = 0; i < BUFFER_SIZE / 4; i ++)
                b.putFloat(i);
            ByteBuffer c = ByteBuffer.allocateDirect(BUFFER_SIZE);
            c.order(ByteOrder.nativeOrder());
            for(int i = 0; i < BUFFER_SIZE / 4; i ++)
                c.putFloat(0.0f);
            CLBuffer clA = context.createBuffer(a, CLBufferType.READ_ONLY);
            CLBuffer clB = context.createBuffer(b, CLBufferType.READ_ONLY);
            CLBuffer clC = context.createBuffer(c, CLBufferType.WRITE_ONLY);
            Log.endMessage(LogStatus.OK);
            Log.message(clA);
            getCLBufferContentFloatSample(clA, 25);
            Log.message(clB);
            getCLBufferContentFloatSample(clB, 25);
            Log.message(clC);
            getCLBufferContentFloatSample(clC, 25);
            Log.startMessage("Setting kernel args");
            kernel.setArgs(clA, clB, clC);
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Running kernel");
            kernel.runIn(queue, new int[] {32, 32}, new int[] {32, 32});
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Mapping buffer");
            clB.map(queue, CLMapType.READ);
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Unmapping buffer");
            clB.unmap(queue);
            Log.endMessage(LogStatus.OK);
            Log.message(clC);
            getCLBufferContentFloatSample(clC, 25);
            Log.startMessage("Releasing buffers");
            clA.release();
            clB.release();
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Releasing kernel");
            kernel.release();
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Releasing program");
            program.release();
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Releasing command queue");
            queue.release();
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Releasing context");
            context.release();
            Log.endMessage(LogStatus.OK);
            Log.message(CLQueryCache.status());
        }
    }

    public static void main(String... args) throws IOException, InterruptedException {
        new Benchmark(new File(args[0]), args[1]);
    }
}
