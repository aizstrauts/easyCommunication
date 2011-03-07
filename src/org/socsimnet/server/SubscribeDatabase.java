/*
 * The MIT License
 * Copyright (c) 2011. Artis Aizstrauts, Sociotechnical Systems Engineering Institute, http://socsimnet.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.socsimnet.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/28/11
 * Time: 11:15 PM
 */
public class SubscribeDatabase {
    private HashMap<String, HashSet<Integer>> subscribers;

    public SubscribeDatabase() {
        this.subscribers = new HashMap<String, HashSet<Integer>>();
    }

    public void put(String key, int subdcriberHash) {
        if (this.subscribers.containsKey(key)) {
            if (this.subscribers.get(key) != null) {
                this.subscribers.get(key).add(subdcriberHash);
            } else {
                HashSet<Integer> users = new HashSet<Integer>();
                users.add(subdcriberHash);
                this.subscribers.put(key, users);
            }
        } else {
            HashSet<Integer> users = new HashSet<Integer>();
            users.add(subdcriberHash);
            this.subscribers.put(key, users);
        }
    }

    public void remove(String key, int subdcriberHash) {
        if (this.subscribers.containsKey(key)) {
            this.subscribers.get(key).remove(subdcriberHash);
        }
    }

    public HashSet<Integer> get(String key) {
        if (this.subscribers.containsKey(key)) {
            return this.subscribers.get(key);
        } else {
            return new HashSet<Integer>();
        }
    }

    public boolean hasKey(String key) {
        return this.subscribers.containsKey(key);
    }

    public Set<String> getKeySet() {
        return this.subscribers.keySet();
    }

    @Override
    public String toString() {
        String returnData = "";
        Set set = this.subscribers.keySet();
        Iterator i = set.iterator();
        String key;
        while (i.hasNext()) {
            key = (String) i.next();
            returnData += ",{\"" + key + "\":[";

            Iterator subscribersIterator = this.subscribers.get(key).iterator();
            String subdcriberSList = "";
            while (subscribersIterator.hasNext()) {
                subdcriberSList += "," + (Integer) subscribersIterator.next();
            }
            if (subdcriberSList.length() > 1) subdcriberSList = subdcriberSList.substring(1); // removes starting comma
            returnData += "\"" + subdcriberSList + "\"]}";
        }
        if (returnData.length() > 1) returnData = returnData.substring(1); // removes starting comma
        return returnData;
    }
}
