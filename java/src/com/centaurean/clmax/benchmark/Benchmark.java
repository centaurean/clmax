package com.centaurean.clmax.benchmark;

import com.centaurean.clmax.schema.*;
import com.centaurean.commons.logs.Log;
import com.centaurean.commons.logs.LogStatus;

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

    public Benchmark() {
        /*Log.startMessage("Creating GL context");
        GLProfile.initSingleton();
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(null);
        GLCanvas glCanvas = new GLCanvas(caps);
        Frame frame = new Frame("Window Test");
        frame.setSize(300, 300);
        frame.add(glCanvas);
        frame.setVisible(true);
        Log.endMessage(LogStatus.OK);*/

        Log.startMessage("Getting platforms");
        CLPlatforms platforms = CLPlatforms.getPlatforms();
        Log.endMessage(LogStatus.OK);
        Log.message("Found " + platforms.size() + " platform(s)");
        for (CLPlatform platform : platforms.values())
            Log.message(platform.toString());
        for (CLPlatform platform : platforms.values()) {
            Log.startMessage("Getting devices for platform " + platform.getPointer());
            CLDevices devices = platform.getDevices(CLDevicesType.CL_DEVICE_TYPE_ALL);
            Log.endMessage(LogStatus.OK);
            Log.message("Found " + devices.size() + " device(s)");
            for (CLDevice device : devices.values())
                Log.message(device.toString());
        }
        for (CLPlatform platform : platforms.values()) {
            CLDevice first = platform.attachedDevices().values().iterator().next();
            Log.startMessage("Ignoring and reinstating context");
            platform.attachedDevices().ignore(first);
            platform.attachedDevices().reinstate(first);
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Creating context on platform " + platform.getPointer());
            CLContext context = platform.createContext();
            Log.endMessage(LogStatus.OK);
            Log.message(context.toString());
            Log.startMessage("Releasing context");
            context.release();
            Log.endMessage(LogStatus.OK);
            /*Log.startMessage("Creating CL GL context on platform " + platform.getPointer());
            context = platform.createCLGLContext();
            Log.endMessage(LogStatus.OK);
            Log.startMessage("Releasing context");
            context.release();
            Log.endMessage(LogStatus.OK);*/
        }
    }

    public static void main(String... args) {
        new Benchmark();
    }
}
