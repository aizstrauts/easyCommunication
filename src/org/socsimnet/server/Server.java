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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Set;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/13/11
 * Time: 7:29 PM
 */
public class Server extends Thread {
    private final int DEFAULT_PORT = 1983;
    private int port;
    private ServerSocket serverSocket;
    private boolean stop = false;

    protected final String ERROR_MESSAGE_UNKNOWN_ACTION = "Error, unknown action!";
    protected final String ERROR_MESSAGE_REGISTER_DATA_EMPTY_NAME = "Error, cannot register data, 'name' is empty!";
    protected final String ERROR_MESSAGE_REGISTER_DATA_DUPLICATE_NAME = "Error, cannot register data! Duplicate name.";
    protected final String ERROR_MESSAGE_REGISTER_DATA_SYSTEM_ERROR = "Error, cannot register data! Server failure.";
    protected final String ERROR_MESSAGE_GET_DATA_SYSTEM_ERROR = "Error, cannot get data! Server failure.";
    protected final String ERROR_MESSAGE_GET_DATA_EMPTY_NAME = "Error, cannot get data, 'name' is empty";
    protected final String ERROR_MESSAGE_SUBDCRIBE_DATA_EMPTY_NAME = "Error, cannot subscribe, 'name' is empty";
    protected final String ERROR_MESSAGE_SUBDCRIBE_DATA_SYSTEM_ERROR = "Error, cannot subscribe, system error";
    protected final String ERROR_MESSAGE_SUBDCRIBE_DATA_UNKNOWN_NAME = "Error, cannot subscribe, unregistred data name";
    protected final String ERROR_MESSAGE_UNSUBDCRIBE_DATA_EMPTY_NAME = "Error, cannot unsubscribe, 'name' is empty";
    protected final String ERROR_MESSAGE_UNSUBDCRIBE_DATA_SYSTEM_ERROR = "Error, cannot unsubscribe, system error";
    protected final String ERROR_MESSAGE_UNSUBDCRIBE_DATA_UNKNOWN_NAME = "Error, cannot unsubscribe, unregistered data name";
    protected final String ERROR_MESSAGE_SEND_DATA_DATA_EMPTY_NAME = "Error, cannot send data, empty name";
    protected final String ERROR_MESSAGE_SEND_DATA_DATA_EMPTY_VALUE = "Error, cannot send data, empty value";
    protected final String ERROR_MESSAGE_SEND_DATA_SYSTEM_ERROR = "Error, cannot send data! Server failure.";
    protected final String ERROR_MESSAGE_SEND_DATA_UNKNOWN_NAME = "Error, cannot send data! Unregistered name.";


    private final ConnectionDatabase cDB;
    private final DataDatabase dDB;
    private final SubscribeDatabase sDB;

    public Server() {
        this.port = DEFAULT_PORT;
        this.cDB = new ConnectionDatabase();
        this.dDB = new DataDatabase();
        this.sDB = new SubscribeDatabase();
    }

    public Server(int port) {
        this.port = port;
        this.cDB = new ConnectionDatabase();
        this.dDB = new DataDatabase();
        this.sDB = new SubscribeDatabase();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(this.port);
            Socket client;
            System.out.println("Server is listening port " + this.port + "...");
            while (!this.stop) {
                try {
                    client = serverSocket.accept();
                } catch (SocketException e) {
                    break;
                }
                System.out.println("Client accepted " + client.toString() + " hash:" + client.hashCode());
                this.cDB.put(client.hashCode(), client);
                new ClientHandler(this, client).start();
            }
            System.out.println("Server shutting down...");
            printCloseMessage();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printCloseMessage() {
        OutputStreamWriter networkPout;
        Socket clientSocket;
        String message = null;
        try {
            message = new JSONObject().put("action", "shutdown").toString() + "\r\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Set set = this.cDB.getKeySet();
        for (Object aSet : set) {
            int clientHash = (Integer) aSet;
            clientSocket = (Socket) this.cDB.get(clientHash);
            try {
                networkPout = new OutputStreamWriter(clientSocket.getOutputStream());
                networkPout.write(message);
                networkPout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stop = true; //TODO if true, ClientHandler cannot accept any actions
    }

    private class ClientHandler extends Thread {
        private final Socket client;
        private final int clientHash;
        private final Server server;

        private ClientHandler(Server server, Socket client) {
            this.server = server;
            this.client = client;
            this.clientHash = this.client.hashCode();
        }

        @Override
        public void run() {
            try {
                BufferedReader networkBin = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStreamWriter networkPout = new OutputStreamWriter(client.getOutputStream());
                JSONObject jsonObject;
                String action;
                while (true) {
                    String line = networkBin.readLine();
                    System.out.println("DEBUG incoming json:" + line);
                    jsonObject = new JSONObject(line);
                    action = jsonObject.get("action").toString();
                    if ((action == null) || action.equals("bye")) {
                        break;
                    } else if (action.equals("register_data")) {
                        if (jsonObject.has("name")) {
                            String returnData = registerData(jsonObject.get("name").toString());
                            networkPout.write(returnData + "\r\n");
                            networkPout.flush();
                        } else {
                            JSONObject returnJSON = new JSONObject();
                            returnJSON.put("status", "error");
                            returnJSON.put("msg", server.ERROR_MESSAGE_REGISTER_DATA_EMPTY_NAME);
                            returnJSON.put("action", action);
                            networkPout.write(returnJSON.toString() + "\r\n");
                            networkPout.flush();
                        }
                    } else if (action.equals("subscribe")) {
                        if (jsonObject.has("name")) {
                            String returnData = subscribeData(jsonObject.get("name").toString(), this.clientHash);
                            networkPout.write(returnData + "\r\n");
                            networkPout.flush();
                        } else {
                            JSONObject returnJSON = new JSONObject();
                            returnJSON.put("status", "error");
                            returnJSON.put("msg", server.ERROR_MESSAGE_SUBDCRIBE_DATA_EMPTY_NAME);
                            returnJSON.put("action", action);
                            networkPout.write(returnJSON.toString() + "\r\n");
                            networkPout.flush();
                        }
                    } else if (action.equals("unsubscribe")) {
                        if (jsonObject.has("name")) {
                            String returnData = unsubscribeData(jsonObject.get("name").toString(), this.clientHash);
                            networkPout.write(returnData + "\r\n");
                            networkPout.flush();
                        } else {
                            JSONObject returnJSON = new JSONObject();
                            returnJSON.put("status", "error");
                            returnJSON.put("msg", server.ERROR_MESSAGE_UNSUBDCRIBE_DATA_EMPTY_NAME);
                            returnJSON.put("action", action);
                            networkPout.write(returnJSON.toString() + "\r\n");
                            networkPout.flush();
                        }

                    } else if (action.equals("send_data")) {
                        if (!jsonObject.has("name")) {
                            JSONObject returnJSON = new JSONObject();
                            returnJSON.put("status", "error");
                            returnJSON.put("msg", server.ERROR_MESSAGE_SEND_DATA_DATA_EMPTY_NAME);
                            returnJSON.put("action", action);
                            networkPout.write(returnJSON.toString() + "\r\n");
                            networkPout.flush();
                        }
                        if (!jsonObject.has("value")) {
                            JSONObject returnJSON = new JSONObject();
                            returnJSON.put("status", "error");
                            returnJSON.put("msg", server.ERROR_MESSAGE_SEND_DATA_DATA_EMPTY_VALUE);
                            returnJSON.put("action", action);
                            networkPout.write(returnJSON.toString() + "\r\n");
                            networkPout.flush();
                        }
                        String returnData = getSendData(jsonObject.get("name").toString(), jsonObject.get("value").toString());
                        networkPout.write(returnData + "\r\n");
                        networkPout.flush();
                    } else if (action.equals("get_data")) {
                        if (jsonObject.has("name")) {
                            String returnData = getRegistredData(jsonObject.get("name").toString());
                            networkPout.write(returnData + "\r\n");
                            networkPout.flush();
                        } else {
                            JSONObject returnJSON = new JSONObject();
                            returnJSON.put("status", "error");
                            returnJSON.put("msg", server.ERROR_MESSAGE_GET_DATA_EMPTY_NAME);
                            returnJSON.put("action", action);
                            networkPout.write(returnJSON.toString() + "\r\n");
                            networkPout.flush();
                        }
                    } else if (action.equals("get_data_list")) {
                        String returnData = getRegistredDataList();
                        networkPout.write(returnData + "\r\n");
                        networkPout.flush();
                    } else if (action.equals("test")) {
                        //TODO remove this testing case!!!
                        networkPout.write(new JSONObject().put("msg", jsonObject.get("action")).toString() + "\r\n");
                        networkPout.flush();
                    } else {
                        JSONObject returnJSON = new JSONObject();
                        returnJSON.put("status", "error");
                        returnJSON.put("msg", server.ERROR_MESSAGE_UNKNOWN_ACTION);
                        returnJSON.put("action", action);
                        networkPout.write(returnJSON.toString() + "\r\n");
                        networkPout.flush();
                    }
                }
                networkBin.close();
                networkPout.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String registerData(String name) {
            JSONObject returnJSON = new JSONObject();
            try {
                if (server.dDB.hasKey(name)) {
                    returnJSON.put("status", "error");
                    returnJSON.put("action", "register_data");
                    returnJSON.put("msg", server.ERROR_MESSAGE_REGISTER_DATA_DUPLICATE_NAME);
                    return returnJSON.toString();
                }
                server.dDB.put(name, "");
                returnJSON.put("status", "ok");
                returnJSON.put("action", "register_data");

                announceNewData(name);

                return returnJSON.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"action\":\"register_data\",\"msg\":\"" + server.ERROR_MESSAGE_REGISTER_DATA_SYSTEM_ERROR + "\"}";
            }
        }

        private String getRegistredDataList() {
            JSONObject returnJSON = new JSONObject();
            Set set = server.dDB.getKeySet();
            for (Object aSet : set) {
                String name = (String) aSet;
                try {
                    returnJSON.append("data_list", (new JSONObject()).put("name", name));
                    returnJSON.put("status", "ok");
                    returnJSON.put("action", "get_data_list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return returnJSON.toString();
        }

        private String subscribeData(String key, int clientHash) {
            JSONObject returnJSON = new JSONObject();
            try {
                if (!server.dDB.hasKey(key)) {
                    returnJSON.put("status", "error");
                    returnJSON.put("action", "subscribe");
                    returnJSON.put("msg", server.ERROR_MESSAGE_SUBDCRIBE_DATA_UNKNOWN_NAME);
                    return returnJSON.toString();
                }
                server.sDB.put(key, clientHash);

                returnJSON.put("status", "ok");
                returnJSON.put("action", "subscribe");
                returnJSON.put("lastvalue", server.dDB.get(key));
                return returnJSON.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"action\":\"subscribe\",\"msg\":\"" + server.ERROR_MESSAGE_SUBDCRIBE_DATA_SYSTEM_ERROR + "\"}";
            }
        }

        private String unsubscribeData(String key, int clientHash) {
            JSONObject returnJSON = new JSONObject();
            try {
                if (!server.dDB.hasKey(key)) {
                    returnJSON.put("status", "error");
                    returnJSON.put("action", "unsubscribe");
                    returnJSON.put("msg", server.ERROR_MESSAGE_UNSUBDCRIBE_DATA_UNKNOWN_NAME);
                    return returnJSON.toString();
                }
                server.sDB.remove(key, clientHash);
                returnJSON.put("status", "ok");
                returnJSON.put("action", "unsubscribe");
                return returnJSON.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"action\":\"unsubscribe\",\"msg\":\"" + server.ERROR_MESSAGE_UNSUBDCRIBE_DATA_SYSTEM_ERROR + "\"}";
            }
        }

        private String getSendData(String key, String value) {
            try {
                //TODO can everyone send data or just the "owner"?

                if (server.dDB.hasKey(key)) {
                    server.dDB.put(key, value);
                    sendDataToAllSubscribers(key);
                    JSONObject returnJSON = new JSONObject();
                    returnJSON.put("status", "ok");
                    returnJSON.put("action", "send_data");
                    return returnJSON.toString();
                } else {
                    return "{\"status\":\"error\",\"action\":\"send_data\",\"msg\":\"" + server.ERROR_MESSAGE_SEND_DATA_UNKNOWN_NAME + "\"}";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"action\":\"send_data\",\"msg\":\"" + server.ERROR_MESSAGE_SEND_DATA_SYSTEM_ERROR + "\"}";
            }
        }

        private String getRegistredData(String key) {
            //TODO vai ši funkcija atbilst merķim??
            JSONObject returnJSON = new JSONObject();
            try {
                returnJSON.put("status", "ok");
                returnJSON.put("action", "get_data");
                returnJSON.put("name", key);
                returnJSON.put("value", server.dDB.get(key));
                return returnJSON.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return "{\"status\":\"error\",\"action\":\"get_data\",\"msg\":\"" + server.ERROR_MESSAGE_GET_DATA_SYSTEM_ERROR + "\"}";
            }
        }


        private void announceNewData(String dataName) {
            Set set = server.cDB.getKeySet();
            Iterator i = set.iterator();
            int key;
            OutputStreamWriter networkPout;
            Socket subscriber;
            JSONObject returnJSON = new JSONObject();
            while (i.hasNext()) {
                key = (Integer) i.next();
                subscriber = (Socket) server.cDB.get(key);
                try {
                    networkPout = new OutputStreamWriter(subscriber.getOutputStream());
                    returnJSON.put("status", "ok");
                    returnJSON.put("action", "new_data");
                    returnJSON.put("name", dataName);
                    networkPout.write(returnJSON.toString() + "\r\n");
                    networkPout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


        private void sendDataToAllSubscribers(String dataName) {
            Set set = server.cDB.getKeySet();
            Iterator i = set.iterator();
            int key;
            OutputStreamWriter networkPout;
            Socket subscriber;
            JSONObject returnJSON = new JSONObject();
            while (i.hasNext()) {
                key = (Integer) i.next();
                subscriber = (Socket) server.cDB.get(key);
                try {
                    networkPout = new OutputStreamWriter(subscriber.getOutputStream());
                    returnJSON.put("status", "ok");
                    returnJSON.put("action", "get_data");
                    returnJSON.put("name", dataName);
                    returnJSON.put("value", server.dDB.get(dataName));
                    networkPout.write(returnJSON.toString() + "\r\n");
                    networkPout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
