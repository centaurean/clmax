package com.centaurean.clmax.schema.values;

import com.centaurean.clmax.schema.programs.CLProgramBinaries;
import com.centaurean.clmax.schema.values.exceptions.CLValueFormatException;

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
 * 26/03/13 15:09
 * @author gpnuma
 */
public class CLValue {
    private Object storage;
    private CLValueType type;

    public CLValue(boolean booleanValue) {
        storage = booleanValue;
        type = CLValueType.BOOLEAN;
    }

    public CLValue(int intValue) {
        this(intValue, CLValueType.INT);
    }

    public CLValue(int intValue, CLValueType givenType) {
        storage = intValue;
        type = givenType;
    }

    public CLValue(long longValue) {
        this(longValue, CLValueType.LONG);
    }

    public CLValue(long longValue, CLValueType givenType) {
        storage = longValue;
        type = givenType;
    }

    public CLValue(String stringValue) {
        storage = stringValue;
        type = CLValueType.CHAR_ARRAY;
    }

    public CLValue(long[] longArrayValue) {
        storage = longArrayValue;
        type = CLValueType.LONG_ARRAY;
    }

    public CLValue(byte[][] byteArrayArrayValue) {
        storage = byteArrayArrayValue;
        type = CLValueType.BYTE_ARRAY_ARRAY;
    }

    public CLValueType getType() {
        return type;
    }

    public boolean getBoolean() {
        switch(type) {
            case BOOLEAN:
                return (Boolean)storage;
            default:
                throw new CLValueFormatException("Not a boolean value");
        }
    }

    public int getInt() {
        switch(type) {
            case INT:
                return (Integer)storage;
            case UNSIGNED_INT:
                Integer intValue = (Integer)storage;
                if(intValue > 0)
                    return intValue;
                else
                    return intValue + Integer.MAX_VALUE;
            default:
                throw new CLValueFormatException("Not an int value");
        }
    }

    public long getLong() {
        switch(type) {
            case LONG:
                return (Long)storage;
            case UNSIGNED_LONG:
                Long longValue = (Long)storage;
                if(longValue > 0)
                    return longValue;
                else
                    return longValue + Long.MAX_VALUE;
            default:
                throw new CLValueFormatException("Not a long value");
        }
    }

    public String getString() {
        switch(type) {
            case CHAR_ARRAY:
                return (String)storage;
            default:
                throw new CLValueFormatException("Not a String value");
        }
    }

    public long[] getLongArray() {
        switch(type) {
            case LONG_ARRAY:
                return (long[])storage;
            default:
                throw new CLValueFormatException("Not a long[] value");
        }
    }

    public CLProgramBinaries getBinaries() {
        switch(type) {
            case BYTE_ARRAY_ARRAY:
                return new CLProgramBinaries(storage);
            default:
                throw new CLValueFormatException("Not a byte[][] value");
        }
    }

    @Override
    public String toString() {
        switch(getType()) {
            case BOOLEAN:
                Boolean.toString(getBoolean());
            case INT:
            case UNSIGNED_INT:
                return Integer.toString(getInt());
            case LONG:
            case UNSIGNED_LONG:
                return Long.toString(getLong());
            case CHAR_ARRAY:
                return getString();
            case LONG_ARRAY:
                return Arrays.toString(getLongArray());
            case BYTE_ARRAY_ARRAY:
                return getBinaries().toString();
            default:
                return "";
        }
    }
}
