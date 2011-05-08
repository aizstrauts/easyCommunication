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

package org.testing;

/**
 * Created by IntelliJ IDEA.
 * User: artis
 * Date: 3/22/11
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */

import org.socsimnet.client.AvailableDataDatabase;
import org.socsimnet.client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

public class ClientTest extends Thread {

    Client client;

    public ClientTest() {
        client = new Client();
    }

    public static void main(String[] args) {
        ClientTest ct = new ClientTest();
        ct.start();
    }

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        client.startServerHandler();
        String msg;

        System.out.print("Enter command:");
        try {
            while ((msg = in.readLine()) != null) {
                String[] params = msg.split(" ");
                if ("register".equals(params[0]) && params.length > 1 && !"".equals(params[1])) {
                    client.registerData(params[1]);
                } else if ("subscribe".equals(params[0]) && params.length > 1 && !"".equals(params[1])) {
                    client.subscribeData(params[1]);
                } else if ("unsubscribe".equals(params[0]) && params.length > 1 && !"".equals(params[1])) {
                    client.unsubscribeData(params[1]);
                } else if ("get".equals(params[0]) && params.length > 1 && !"".equals(params[1])) {
                    System.out.println("Data - " + params[1] + " : " + client.getData(params[1]));
                } else if ("send".equals(params[0]) && params.length > 2 && !"".equals(params[1]) && !"".equals(params[2])) {
                    client.sendData(params[1], params[2]);

                } else if ("getdatalist".equals(params[0])) {
                    AvailableDataDatabase adDB = client.getDataList();
                    Enumeration vEnum = adDB.elements();
                    System.out.println("Available data:");
                    while (vEnum.hasMoreElements()) {
                        System.out.print(vEnum.nextElement() + " ");
                    }
                    System.out.println();
                } else if ("exit".equals(params[0])) {
                    client.stopServerHandler();
                    client = null;
                    break;
                } else {
                    System.out.println("=== HELP ===");
                    System.out.println("Available commands:");
                    System.out.println("\tregister {name}");
                    System.out.println("\tsubscribe {name}");
                    System.out.println("\tunsubscribe {name}");
                    System.out.println("\tget {name}");
                    System.out.println("\tsend {name} {value}");
                    System.out.println("\tretgetdatalist");
                    System.out.println("=========");

                }
                System.out.print("Enter command:");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
