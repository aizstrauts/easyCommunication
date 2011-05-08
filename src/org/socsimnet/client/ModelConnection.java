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

import java.net.Socket;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 5/7/11
 * Time: 7:51 PM
 */

public class ModelCommunication extends Thread {

    //private CommunicationAdapterView adapterView;
    // private InteractionDB iDB;
    //  private ServerCommunication sc;
    private int port = 1984;

    /**
     *
     */
    public class CommunicationHanler extends Thread {

        /**
         *
         */
        protected boolean serverContinue = true;
        /**
         *
         */
        protected Socket clientSocket;
        private InteractionDB iDB;
        private ServerCommunication sc;

        private CommunicationHanler(Socket clientSoc, InteractionDB iDB, ServerCommunication sc) {
            this.iDB = iDB;
            this.sc = sc;
            clientSocket = clientSoc;
            start();
        }

        /**
         *
         */
        @Override
        public void run() {
            System.out.println("New Communication Thread Started");

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;

                while ((line = in.readLine()) != null) {
                    if ((line == null)) {
                        System.out.println("break;");
                        break;
                    } else if (line.length() > 0) {
                        // Extend padod char 0x4 un uz to XML parser nospragst
                        line = line.replaceAll("" + ((char) 4), "");

                        System.out.println("l:" + line);

                        String returnData = "";
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(line));
                        Document doc = db.parse(is);
                        NodeList nodes = doc.getElementsByTagName("senddata");

                        for (int i = 0; i < nodes.getLength(); i++) {
                            System.out.println("SUTA DATUS:" + line);
                            Element element = (Element) nodes.item(i);
                            NodeList name = element.getElementsByTagName("interaction");
                            Element row = (Element) name.item(0);
                            String interactionName = getCharacterDataFromElement(row);
                            NodeList desc = element.getElementsByTagName("data");
                            row = (Element) desc.item(0);
                            String interactionData = getCharacterDataFromElement(row);

                            //TODO pārbaudīt vai ekistē  šāda interact ??? bet varbut nevjag
                            System.out.println("iDB.insertInteraction(interactionName, interactionData);");
                            iDB.insertInteraction(interactionName, interactionData);
                            if (sc != null && sc.sock != null && sc.sock.isConnected()) {
                                sc.sendInteraction(interactionName, interactionData);
                            }
                        }

                        nodes = doc.getElementsByTagName("getdata");
                        for (int i = 0; i < nodes.getLength(); i++) {
                            System.out.println("PRASA DATUS:" + line);
                            Element row = (Element) nodes.item(0);
                            String interactionName = getCharacterDataFromElement(row);
                            String data = iDB.getLastInteraction(interactionName);
                            returnData = data;
                            //returnData = "<returndata><interaction>" + interactionName + "</interaction><data>" + data + "</data></returndata>";
                        }
                        if (returnData.length() > 0) {
                            System.out.println(returnData);
                            out.write(returnData + "\r\n");
                            out.flush();
                        }
                    }
                }
                out.close();
                in.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Problem with Communication Server");
                //System.exit(1);
            }
        }
    }

    /**
     * @param e
     * @return
     */
    public String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    /**
     * @param adapterView
     * @param iDB
     * @param sc
     * @throws IOException
     */
    public ModelCommunication(CommunicationAdapterView adapterView, InteractionDB iDB, ServerCommunication sc) throws IOException {
        this.adapterView = adapterView;
        this.iDB = iDB;
        this.sc = sc;
    }

    /**
     *
     */
    @Override
    public void run() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(1984);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    this.adapterView.setIndicatorStatus("local", 2);
                    new CommunicationHanler(serverSocket.accept(), iDB, sc);
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                this.adapterView.setIndicatorStatus("local", 0);
                //System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 1984.");
            this.adapterView.setIndicatorStatus("local", 0);
            // System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 1984.");
                System.exit(1);
            }
        }

    }
}

