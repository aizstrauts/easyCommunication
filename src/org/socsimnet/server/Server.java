package org.socsimnet.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/13/11
 * Time: 7:29 PM
 */
public class Server extends Thread {
    public int DEFAULT_PORT = 1983;
    private int port;
    private ServerSocket serverSocket;
    private boolean stop = false;

    private ConnectionDatabase cDB;

    public Server() {
        this.port = DEFAULT_PORT;
        this.cDB = new ConnectionDatabase();
    }

    public Server(int port) {
        this.port = port;
        this.cDB = new ConnectionDatabase();
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
            System.out.println("Server is listening post " + this.port + "...");
            while (!this.stop) {
                Socket client = serverSocket.accept();
                System.out.println("Client accepted " + client.toString() + " " + client.hashCode());
                this.cDB.put(client.hashCode(), client);
                new ClientHandler(client).start();
            }
            serverSocket.close();
            System.out.println("Server shutting down...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        this.stop = true;
    }

    private class ClientHandler extends Thread {
        private Socket client;
        private int clientHash;

        private ClientHandler(Socket client) {
            this.client = client;
            this.clientHash = this.client.hashCode();
        }

        @Override
        public void run() {
            try {
                BufferedReader networkBin = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStreamWriter networkPout = new OutputStreamWriter(client.getOutputStream());

                while (true) {
                    String line = null;
                    line = networkBin.readLine();
                    JSONObject jsonObject = new JSONObject(line);
                    String action = jsonObject.get("action").toString();
                    if ((action == null) || action.equals("bye")) {
                        break;
                    } else if (action.equals("register_data")) {

                    } else if (action.equals("subscribe")) {

                    } else if (action.equals("unsubscribe")) {

                    } else if (action.equals("send_data")) {

                    } else if (action.equals("get_data")) {

                    } else if (action.equals("get_data_list")) {

                    }

                    networkPout.write(new JSONObject().put("msg", jsonObject.get("action")).toString() + "\r\n");
                    networkPout.flush();
                }
                networkBin.close();
                networkPout.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
