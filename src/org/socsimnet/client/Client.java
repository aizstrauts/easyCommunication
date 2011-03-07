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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 3/6/11
 * Time: 10:14 PM
 */
public class Client {
    public static final int DEFAULT_SERVER_PORT = 1983;
    public static final String DEFAULT_SERVER_HOST = "localhost";

    private String serverHost;
    private int serverPort;

    private ServerHandler serverHandler;
    private boolean serverHanderIsRunning = false;

    public Client() {
        this.serverHost = DEFAULT_SERVER_HOST;
        this.serverPort = DEFAULT_SERVER_PORT;
        serverHandler = new ServerHandler();
    }

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        serverHandler = new ServerHandler();
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void startServerHandler() {
        serverHandler.start();
        serverHanderIsRunning = true;
    }

    public void registerData(String name) {
        if (this.serverHanderIsRunning) {
            serverHandler.registerData(name);
        }
    }

    public void subscribeData(String name) {
        if (this.serverHanderIsRunning) {
            serverHandler.subscribeData(name);
        }
    }

    public void unsubscribeData(String name) {
        if (this.serverHanderIsRunning) {
            //TODO unsubscribe
        }
    }

    public void getData(String name) {
        if (this.serverHanderIsRunning) {
            //TOOD get data
        }
    }

    public void getDataList(String name) {
        if (this.serverHanderIsRunning) {
            //TODO get data list
        }
    }

    public void sendData(String name, String value) {
        if (this.serverHanderIsRunning) {
            serverHandler.sendData(name, value);
        }
    }

    private class ServerHandler extends Thread {
        Socket sock;
        BufferedReader in;
        PrintWriter out;

        private ServerHandler() {
            this.init();
        }

        private void init() {
            try {
                sock = new Socket(Client.DEFAULT_SERVER_HOST, Client.DEFAULT_SERVER_PORT);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new PrintWriter(sock.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void registerData(String name) {
            try {
                if (name != null && name.length() > 0) {
                    JSONObject json = new JSONObject();
                    json.put("action", "register_data");
                    json.put("name", name);
                    out.println(json.toString());
                    out.flush();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void subscribeData(String name) {
            try {
                if (name != null && name.length() > 0) {
                    JSONObject json = new JSONObject();
                    json.put("action", "subscribe");
                    json.put("name", name);
                    out.println(json.toString());
                    out.flush();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void sendData(String name, String value) {
            try {
                if (name != null && name.length() > 0 && value != null && value.length() > 0) {
                    JSONObject json = new JSONObject();
                    json.put("action", "send_data");
                    json.put("name", name);
                    json.put("value", value);
                    out.println(json.toString());
                    out.flush();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                JSONObject jsonObject;
                String action;
                String status;
                while (true) {
                    String line = in.readLine();
                    System.out.println("LINE:" + line);
                    jsonObject = new JSONObject(line);
                    action = jsonObject.get("action").toString();
                    status = jsonObject.get("status").toString();
                    if ("register_data".equals(action)) {

                    } else {
                        //TODO unknown action

                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
