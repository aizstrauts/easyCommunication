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

import org.json.JSONArray;
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
    private boolean ready;

    private final DataDatabase dDB;
    private final AvailableDataDatabase adDB;

    private ServerHandler serverHandler;
    private boolean serverHanderIsRunning = false;

    public Client() {
        this.serverHost = DEFAULT_SERVER_HOST;
        this.serverPort = DEFAULT_SERVER_PORT;
        this.dDB = new DataDatabase();
        this.adDB = new AvailableDataDatabase();
        this.ready = false;
        serverHandler = new ServerHandler(this);
    }

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.dDB = new DataDatabase();
        this.adDB = new AvailableDataDatabase();
        this.ready = false;
        serverHandler = new ServerHandler(this);
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void startServerHandler() {
        serverHandler.start();
        serverHandler.getDataList();
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
            serverHandler.unsubscribeData(name);
        }
    }

    public String getData(String name) {
        return this.dDB.get(name);
    }

    public void refreshData(String name) {
        if (this.serverHanderIsRunning) {
            serverHandler.getData(name);
        }
    }

    public AvailableDataDatabase getDataList() {
        return this.adDB;
    }

    public void refreshDataList() {
        if (this.serverHanderIsRunning) {
            serverHandler.getDataList();
        }
    }

    public void sendData(String name, String value) {
        if (this.serverHanderIsRunning) {
            serverHandler.sendData(name, value);
        }
    }

    public void stopServerHandler() {
        this.serverHandler.requestStop();
        this.serverHandler = null;
    }

    public boolean isReady() {
        return ready;
    }

    private class ServerHandler extends Thread {
        Client client;
        Socket sock;
        BufferedReader in;
        PrintWriter out;
        private volatile boolean stop = false;

        public ServerHandler(Client client) {
            this.client = client;
            this.init();
        }

        private void init() {
            try {
                System.out.println("Connecting to server (" + client.serverHost + ":" + client.serverPort + ")");
                sock = new Socket(client.serverHost, client.serverPort);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new PrintWriter(sock.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void requestStop() {
            try {
                this.stop = true;
                this.sock.shutdownInput();
                this.sock.shutdownOutput();
                this.sock.close();
                this.in.close();
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

        protected void unsubscribeData(String name) {
            try {
                if (name != null && name.length() > 0) {
                    JSONObject json = new JSONObject();
                    json.put("action", "unsubscribe");
                    json.put("name", name);
                    out.println(json.toString());
                    out.flush();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void getDataList() {
            try {
                JSONObject json = new JSONObject();
                json.put("action", "get_data_list");
                out.println(json.toString());
                out.flush();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void getData(String name) {
            try {
                if (name != null && name.length() > 0) {
                    JSONObject json = new JSONObject();
                    json.put("action", "get_data");
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
                while (!this.stop) {
                    String line = in.readLine();
                    jsonObject = new JSONObject(line);
                    System.out.println("DEBUG:: incoming json: " + jsonObject.toString());
                    action = jsonObject.get("action").toString();
                    status = jsonObject.get("status").toString();

                    System.out.println("action:" + action);
                    if ("register_data".equals(action)) {

                    } else if ("subscribe".equals(action)) {
                        try {
                            String name = jsonObject.get("name").toString();
                            String value = jsonObject.get("lastvalue").toString();
                            this.client.dDB.put(name, value);
                        } catch (JSONException jsonex) {
                            jsonex.printStackTrace();
                        }

                    } else if ("unsubscribe".equals(action)) {

                    } else if ("send_data".equals(action)) {

                    } else if ("get_data".equals(action)) {
                        if ("ok".equals(status)) {
                            String name = jsonObject.get("name").toString();
                            String value = jsonObject.get("value").toString();
                            this.client.dDB.put(name, value);
                        } else {
                            System.out.println("DEBUG:: incoming get_data error:" + jsonObject.get("msg").toString());
                        }
                    } else if ("get_data_list".equals(action)) {
                        try {
                            if (jsonObject.has("data_list")) {
                                JSONArray dataList = jsonObject.getJSONArray("data_list");

                                int size = dataList.length();
                                this.client.adDB.clear();
                                for (int i = 0; i < size; i++) {
                                    JSONObject d = (JSONObject) dataList.get(i);
                                    this.client.adDB.add((String) d.get("name"));
                                }
                            }
                            this.client.ready = true;
                        } catch (JSONException jsonex) {
                            jsonex.printStackTrace();
                        }
                    } else if ("new_data".equals(action)) {
                        String name = jsonObject.get("name").toString();
                        if (!this.client.adDB.contains(name)) {
                            this.client.adDB.add(name);
                        }
                    } else {
                        System.out.println("DEBUG:: incoming error, unknown action!");
                    }
                }
                System.out.println("Exit Client ServerHandler");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
