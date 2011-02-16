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

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.socsimnet.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/13/11
 * Time: 8:08 PM
 */
public class ServerTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetPort() throws Exception {
        Server instance = new Server();
        int expResult = 1983;
        int result = instance.getPort();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetPort() throws Exception {
        Server instance = new Server(11);
        int expResult = 11;
        int result = instance.getPort();
        assertEquals(expResult, result);
    }

    @Test
    public void testClientAccept() throws Exception {
        Server server = new Server();
        assertTrue(server != null);
        server.start();
        Socket sock = new Socket("localhost", server.getPort());
        assertTrue(sock != null);
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        assertTrue(in != null);
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        assertTrue(out != null);
        assertTrue(sock.isConnected());
        out.println(new JSONObject().put("action", "test").toString());
        out.flush();
        String data = in.readLine();
        JSONObject jsonObject = new JSONObject(data);
        assertEquals("test", jsonObject.get("msg"));

        Socket sock2 = new Socket("localhost", server.getPort());
        assertTrue(sock2 != null);
        BufferedReader in2 = new BufferedReader(new InputStreamReader(sock2.getInputStream()));
        assertTrue(in2 != null);
        PrintWriter out2 = new PrintWriter(sock2.getOutputStream(), true);
        assertTrue(out2 != null);
        assertTrue(sock2.isConnected());
        out2.println(new JSONObject().put("action", "test2").toString());
        out2.flush();
        String data2 = in2.readLine();
        JSONObject jsonObject2 = new JSONObject(data2);
        assertEquals("test2", jsonObject2.get("msg"));

        out.println(new JSONObject().put("action", "bye").toString());
        out.flush();
        out2.println(new JSONObject().put("action", "bye").toString());
        out2.flush();
        server.stopServer();


    }
}
