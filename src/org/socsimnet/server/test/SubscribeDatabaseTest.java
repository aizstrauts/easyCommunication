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

package org.socsimnet.server.test;

import org.junit.Test;
import org.socsimnet.server.SubscribeDatabase;

import static org.junit.Assert.assertEquals;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/28/11
 * Time: 11:46 PM
 */
public class SubscribeDatabaseTest {
    @Test
    public void testPut() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testHasKey() throws Exception {

    }

    @Test
    public void testGetKeySet() throws Exception {

    }

    @Test
    public void testToString() throws Exception {
        SubscribeDatabase subscribers = new SubscribeDatabase();
        subscribers.put("data1", 123);
        subscribers.put("data2", 567);
        String expected = "{\"data1\":[\"123\"]},{\"data2\":[\"567\"]}";
        String actual = subscribers.toString();
        assertEquals(expected, actual);
    }
}