package com.centaurean.clmax.schema;

import com.centaurean.clmax.cache.CLQueryCacheKey;
import com.centaurean.clmax.schema.exceptions.CLException;
import com.centaurean.clmax.schema.exceptions.CLNativeException;
import com.centaurean.clmax.schema.values.CLValue;
import com.centaurean.commons.logs.Log;

import java.util.Iterator;
import java.util.List;

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
 * 20/04/13 17:38
 * @author gpnuma
 */
public abstract class CLCachedObject<T extends CLQueryCacheKey> extends CLObject {
    public CLCachedObject(long pointer) {
        super(pointer);
    }

    protected abstract CLValue get(T key);

    private void appendTo(StringBuilder stringBuilder, T key) {
        try {
            stringBuilder.append(key.name()).append("='").append(get(key));
        } catch (CLNativeException exception) {
            Log.message(new CLException("[" + super.toString() + "] Querying " + key.name() + " returned error " + exception.getMessage()));
        } finally {
            stringBuilder.append("'");
        }
    }

    protected String toString(List<T> keys) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString()).append(" {");
        Iterator<T> iterator = keys.iterator();
        if (iterator.hasNext()) {
            appendTo(stringBuilder, iterator.next());
            while (iterator.hasNext()) {
                stringBuilder.append(", ");
                appendTo(stringBuilder, iterator.next());
            }
        }
        return stringBuilder.append("}").toString();
    }
}
