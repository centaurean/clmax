package com.centaurean.clmax.benchmark;

import com.centaurean.clmax.cache.CLQueryCache;
import com.centaurean.clmax.schema.contexts.CLContext;
import com.centaurean.clmax.schema.devices.CLDevice;
import com.centaurean.clmax.schema.devices.CLDeviceInfo;
import com.centaurean.clmax.schema.devices.CLDeviceType;
import com.centaurean.clmax.schema.devices.CLDevices;
import com.centaurean.clmax.schema.exceptions.CLNativeException;
import com.centaurean.clmax.schema.kernels.CLKernel;
import com.centaurean.clmax.schema.mem.CLMapType;
import com.centaurean.clmax.schema.mem.buffers.CLDoubleBuffer;
import com.centaurean.clmax.schema.mem.buffers.CLFloatBuffer;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import static com.centaurean.clmax.schema.mem.buffers.CLBufferType.READ_ONLY;
import static com.centaurean.clmax.schema.mem.buffers.CLBufferType.WRITE_ONLY;

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
    private static final int BUFFER_SIZE = 2048 * 2048;

    private static void getCLBufferContentFloatSample(CLFloatBuffer buffer, int elements) {
        StringBuilder content = new StringBuilder();
        buffer.rewind();
        for (int i = 0; i < elements; i++)
            content.append(buffer.getFloat()).append(", ");
        Log.message(content.append("... (").append(buffer.getElementSize()).append(" elements)").toString());
    }

    private static void getCLBufferContentDoubleSample(CLDoubleBuffer buffer, int elements) {
        StringBuilder content = new StringBuilder();
        buffer.rewind();
        for (int i = 0; i < elements; i++)
            content.append(buffer.getDouble()).append(", ");
        Log.message(content.append("... (").append(buffer.getElementSize()).append(" elements)").toString());
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
            for (CLDevice currentDevice : platform.attachedDevices().values()) {
                CLFloatBuffer clA = null;
                CLFloatBuffer clB = null;
                CLFloatBuffer clC = null;
                CLKernel kernel = null;
                CLProgram program = null;
                CLCommandQueue queue = null;
                CLContext context = null;
                try {
                    Log.message();
                    Log.message(currentDevice.get(CLDeviceInfo.CL_DEVICE_NAME) + " (" + currentDevice.getPointer() + ")");
                    Log.message();
                    /*CLDevice first = platform.attachedDevices().values().iterator().next();
                    Log.startMessage("Ignoring and reinstating device");
                    platform.attachedDevices().ignore(currentDevice);
                    platform.attachedDevices().reinstate(currentDevice);
                    Log.endMessage(LogStatus.OK);*/
                    Log.startMessage("Creating context on platform " + platform.getPointer());
                    context = platform.createContext();
                    Log.endMessage(LogStatus.OK);
                    Log.message(context);
                    Log.startMessage("Creating command queue");
                    queue = currentDevice.createCommandQueue(context);
                    Log.endMessage(LogStatus.OK);
                    Log.message(queue);
                    Log.startMessage("Creating program");
                    program = context.createProgram(clFile);
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
                    for (CLDevice device : program.getBuildDevices())
                        Log.message(program.buildInfos(device));
                    Log.message(program);
                /*CLProgramBinaries binaries = program.get(CLProgramInfo.CL_PROGRAM_BINARIES).getBinaries();
                for (int i = 0; i < binaries.size(); i++) {
                    FileOutputStream out = new FileOutputStream("out.bn" + i);
                    binaries.toStream(i, out);
                    out.close();
                }*/
                    Log.startMessage("Creating buffers");
                    clA = CLFloatBuffer.create(context, READ_ONLY, BUFFER_SIZE);
                    clB = CLFloatBuffer.create(context, READ_ONLY, BUFFER_SIZE);
                    clC = CLFloatBuffer.create(context, WRITE_ONLY, BUFFER_SIZE);
                    Random random = new Random(7L);
                    for (int i = 0; i < BUFFER_SIZE; i++)
                        clA.putFloat(random.nextFloat());
                    for (int i = 0; i < BUFFER_SIZE; i++)
                        clB.putFloat(random.nextFloat());
                    for (int i = 0; i < BUFFER_SIZE; i++)
                        clC.putFloat(random.nextFloat());
                    Log.endMessage(LogStatus.OK);
                    Log.message(clA);
                    getCLBufferContentFloatSample(clA, 25);
                    Log.message(clB);
                    getCLBufferContentFloatSample(clB, 25);
                    Log.message(clC);
                    getCLBufferContentFloatSample(clC, 25);
                    Log.startMessage("Creating kernel");
                    kernel = program.createKernel(kernelName);
                    Log.endMessage(LogStatus.OK);
                    Log.message(kernel);
                    for (CLDevice device : program.getBuildDevices())
                        Log.message(kernel.workGroupInfos(device));
                    Log.startMessage("Setting kernel args");
                    kernel.setArgs(clA, clB, clC);
                    Log.endMessage(LogStatus.OK);
                    for (int i = 0; i < 5; i++) {
                        Log.startMessage("Running kernel");
                        kernel.runIn(queue, new int[]{2048, 2048}/*, new int[] {1024}*/, new int[]{32, 32});
                        Log.endMessage(LogStatus.OK);
                        Log.message("GFLOPS = " + /*((float)BUFFER_SIZE) / (Log.chronometer().elapsed())*/ 2 * Math.pow(2048, 3) / (Log.chronometer().elapsed()));
                    }
                    Log.startMessage("Mapping buffer");
                    clC.map(queue, CLMapType.READ);
                    Log.endMessage(LogStatus.OK);
                    Log.startMessage("Unmapping buffer");
                    clC.unmap(queue);
                    Log.endMessage(LogStatus.OK);
                    getCLBufferContentFloatSample(clC, 25);
                } finally {
                    if (Log.chronometer().started() && !Log.chronometer().stopped())
                        Log.endMessage(LogStatus.ERROR);
                    Log.startMessage("Releasing buffers");
                    if (clA != null)
                        clA.release();
                    if (clB != null)
                        clB.release();
                    if (clC != null)
                        clC.release();
                    Log.endMessage(LogStatus.OK);
                    Log.startMessage("Releasing kernel");
                    if (kernel != null)
                        kernel.release();
                    Log.endMessage(LogStatus.OK);
                    Log.startMessage("Releasing program");
                    if (program != null)
                        program.release();
                    Log.endMessage(LogStatus.OK);
                    Log.startMessage("Releasing command queue");
                    if (queue != null)
                        queue.release();
                    Log.endMessage(LogStatus.OK);
                    Log.startMessage("Releasing context");
                    if (context != null)
                        context.release();
                    Log.endMessage(LogStatus.OK);
                    Log.message(CLQueryCache.status());
                }
            }
        }
    }

    public static void main(String... args) throws IOException, InterruptedException {
        new Benchmark(new File(args[0]), args[1]);
    }
}
