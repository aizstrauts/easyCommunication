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

package org.socsimnet.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/19/11
 * Time: 11:32 PM
 */
public class DataDatabase {
    private HashMap<String, String> dataMap;

    public DataDatabase() {
        this.dataMap = new HashMap<String, String>();
    }

    public void put(String key, String data) {
        this.dataMap.put(key, data);
    }

    public String get(String key) {
        if (this.dataMap.containsKey(key)) {
            return this.dataMap.get(key);
        } else {
            return "";
        }
    }

    public boolean hasKey(String key) {
        return this.dataMap.containsKey(key);
    }

    public Set<String> getKeySet() {
        return this.dataMap.keySet();
    }

    @Override
    public String toString() {
        String returnData = "";
        Set set = this.dataMap.keySet();
        Iterator i = set.iterator();
        String key;
        while (i.hasNext()) {
            key = (String) i.next();
            returnData += ",{\"" + key + "\":\"" + this.dataMap.get(key) + "\"}";
        }
        if (returnData.length() > 1) returnData = returnData.substring(1); // removes starting comma
        return returnData;
    }

}
