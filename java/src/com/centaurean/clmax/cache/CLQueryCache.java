package com.centaurean.clmax.cache;

import com.centaurean.clmax.schema.values.CLValue;
import com.centaurean.commons.structures.Pair;

import java.util.Hashtable;
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
 * 25/03/13 21:31
 * @author gpnuma
 */
public class CLQueryCache {
    static {
        cacheStorage = new Hashtable<Pair<Long, CLQueryCacheKey>, CLValue>();
    }

    private static final ReentrantLock cacheLock = new ReentrantLock(true);
    private static Hashtable<Pair<Long, CLQueryCacheKey>, CLValue> cacheStorage;
    private static long hits = 0;
    private static long queries = 0;

    public static CLValue add(Long pointer, CLQueryCacheKey key, CLValue value) {
        cacheLock.lock();
        try {
            return cacheStorage.put(Pair.of(pointer, key), value);
        } finally {
            cacheLock.unlock();
        }
    }

    public static boolean contains(Long pointer, CLQueryCacheKey key) {
        cacheLock.lock();
        try {
            return cacheStorage.containsKey(Pair.of(pointer, key));
        } finally {
            cacheLock.unlock();
        }
    }

    public static CLValue get(Long pointer, CLQueryCacheKey key) {
        cacheLock.lock();
        try {
            queries ++;
            CLValue value = cacheStorage.get(Pair.of(pointer, key));
            if(value != null)
                hits ++;
            return value;
        } finally {
            cacheLock.unlock();
        }
    }

    public static long getHits() {
        return hits;
    }

    public static long getQueries() {
        return queries;
    }

    public static String status() {
        cacheLock.lock();
        try {
            return "Query cache : " + getHits() + " hits / " + getQueries() +  " queries ( " + Math.round((100.0 * getHits()) / getQueries()) + " % )";
        } finally {
            cacheLock.unlock();
        }
    }
}
