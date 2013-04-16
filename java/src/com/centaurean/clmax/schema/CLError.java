package com.centaurean.clmax.schema;

import java.util.Hashtable;
import java.util.Map;

import static java.lang.Integer.decode;

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
 * 27/03/13 15:13
 * @author gpnuma
 */
public enum CLError {
    CL_SUCCESS(0),
    CL_DEVICE_NOT_FOUND(-1),
    CL_DEVICE_NOT_AVAILABLE(-2),
    CL_COMPILER_NOT_AVAILABLE(-3),
    CL_MEM_OBJECT_ALLOCATION_FAILURE(-4),
    CL_OUT_OF_RESOURCES(-5),
    CL_OUT_OF_HOST_MEMORY(-6),
    CL_PROFILING_INFO_NOT_AVAILABLE(-7),
    CL_MEM_COPY_OVERLAP(-8),
    CL_IMAGE_FORMAT_MISMATCH(-9),
    CL_IMAGE_FORMAT_NOT_SUPPORTED(-10),
    CL_BUILD_PROGRAM_FAILURE(-11),
    CL_MAP_FAILURE(-12),
    CL_MISALIGNED_SUB_BUFFER_OFFSET(-13),
    CL_EXEC_STATUS_ERROR_FOR_EVENTS_IN_WAIT_LIST(-14),
    CL_COMPILE_PROGRAM_FAILURE(-15),
    CL_LINKER_NOT_AVAILABLE(-16),
    CL_LINK_PROGRAM_FAILURE(-17),
    CL_DEVICE_PARTITION_FAILED(-18),
    CL_KERNEL_ARG_INFO_NOT_AVAILABLE(-19),

    CL_INVALID_VALUE(-30),
    CL_INVALID_DEVICE_TYPE(-31),
    CL_INVALID_PLATFORM(-32),
    CL_INVALID_DEVICE(-33),
    CL_INVALID_CONTEXT(-34),
    CL_INVALID_QUEUE_PROPERTIES(-35),
    CL_INVALID_COMMAND_QUEUE(-36),
    CL_INVALID_HOST_PTR(-37),
    CL_INVALID_MEM_OBJECT(-38),
    CL_INVALID_IMAGE_FORMAT_DESCRIPTOR(-39),
    CL_INVALID_IMAGE_SIZE(-40),
    CL_INVALID_SAMPLER(-41),
    CL_INVALID_BINARY(-42),
    CL_INVALID_BUILD_OPTIONS(-43),
    CL_INVALID_PROGRAM(-44),
    CL_INVALID_PROGRAM_EXECUTABLE(-45),
    CL_INVALID_KERNEL_NAME(-46),
    CL_INVALID_KERNEL_DEFINITION(-47),
    CL_INVALID_KERNEL(-48),
    CL_INVALID_ARG_INDEX(-49),
    CL_INVALID_ARG_VALUE(-50),
    CL_INVALID_ARG_SIZE(-51),
    CL_INVALID_KERNEL_ARGS(-52),
    CL_INVALID_WORK_DIMENSION(-53),
    CL_INVALID_WORK_GROUP_SIZE(-54),
    CL_INVALID_WORK_ITEM_SIZE(-55),
    CL_INVALID_GLOBAL_OFFSET(-56),
    CL_INVALID_EVENT_WAIT_LIST(-57),
    CL_INVALID_EVENT(-58),
    CL_INVALID_OPERATION(-59),
    CL_INVALID_GL_OBJECT(-60),
    CL_INVALID_BUFFER_SIZE(-61),
    CL_INVALID_MIP_LEVEL(-62),
    CL_INVALID_GLOBAL_WORK_SIZE(-63),
    CL_INVALID_PROPERTY(-64),
    CL_INVALID_IMAGE_DESCRIPTOR(-65),
    CL_INVALID_COMPILER_OPTIONS(-66),
    CL_INVALID_LINKER_OPTIONS(-67),
    CL_INVALID_DEVICE_PARTITION_COUNT(-68);

    private static final Map<Integer, CLError> lookupTable;

    static {
        lookupTable = new Hashtable<Integer, CLError>();
        for (CLError error : CLError.values())
            lookupTable.put(error.getCode(), error);
    }

    private int code;

    private CLError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CLError getError(int code) {
        return lookupTable.get(code);
    }

    public static String toString(String code) {
        int codeInt = decode(code);
        if (lookupTable.containsKey(codeInt))
            return getError(codeInt).name();
        else
            return "Unknown CL Error";
    }
}
