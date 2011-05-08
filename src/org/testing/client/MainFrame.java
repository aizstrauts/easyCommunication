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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on Apr 4, 2011, 11:06:36 PM
 */
package org.testing.client;

/**
 *
 * @author artis
 */

import org.socsimnet.client.AvailableDataDatabase;
import org.socsimnet.client.Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFrame extends javax.swing.JFrame {

    // Variables declaration
    private javax.swing.JButton jButton_connect;
    private javax.swing.JButton jButton_register;
    private javax.swing.JButton jButton_subscribe;
    private javax.swing.JButton jButton_unsubscribe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel_info_lastvalue;
    private javax.swing.JLabel jLabel_info_name;
    private javax.swing.JList jList_alldata;
    private javax.swing.JList jList_mydata;
    private javax.swing.JList jList_subscribed;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea_info_description;
    private javax.swing.JTextArea jTextArea_registration_description;
    private javax.swing.JTextField jTextField_port;
    private javax.swing.JTextField jTextField_registration_name;
    private javax.swing.JTextField jTextField_server;

    private Client client;
    private String serverAddress;
    private int serverPort = -1;

    private UpdateDataList uDataList;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField_server = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField_port = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_alldata = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_subscribed = new javax.swing.JList();
        jButton_unsubscribe = new javax.swing.JButton();
        jButton_subscribe = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel_info_name = new javax.swing.JLabel();
        jLabel_info_lastvalue = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextArea_info_description = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList_mydata = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jTextField_registration_name = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton_register = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea_registration_description = new javax.swing.JTextArea();
        jButton_connect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Communication adapter");

        jLabel1.setText("Server:");

        jTextField_server.setText("localhost");

        jLabel2.setText("Port:");

        jTextField_port.setText("1983");

        /* jList_alldata.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {"ziema", "pavasaris", "vasara", "rudens", "ziema"};

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        */

        jList_alldata.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList_alldata);

        /*jList_subscribed.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {"ziema"};

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        */
        jList_subscribed.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList_subscribed.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                jList_subscribedActionPerformed(listSelectionEvent);
            }
        });
        jScrollPane2.setViewportView(jList_subscribed);

        jButton_unsubscribe.setText("< unsubscribe");
        jButton_unsubscribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_unsubscribeActionPerformed(evt);
            }
        });


        jButton_subscribe.setText("subscribe >");
        jButton_subscribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_subscribeActionPerformed(evt);
            }
        });

        jLabel_info_name.setText("Name: none");

        jLabel_info_lastvalue.setText("Last value: none");

        jLabel6.setText("Descr:");

        jTextArea_info_description.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea_info_description.setColumns(20);
        jTextArea_info_description.setRows(5);
        jTextArea_info_description.setText("none\n");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(jPanel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                                        .add(jPanel2Layout.createSequentialGroup()
                                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(jPanel2Layout.createSequentialGroup()
                                                                .add(21, 21, 21)
                                                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                                                                .add(20, 20, 20))
                                                        .add(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .add(jButton_subscribe, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                                                .add(18, 18, 18)))
                                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jButton_unsubscribe, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                                .add(21, 21, 21)
                                                .add(jLabel6)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jTextArea_info_description, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel_info_name, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jLabel_info_lastvalue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jButton_unsubscribe, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jButton_subscribe, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(6, 6, 6)
                                .add(jLabel_info_name)
                                .add(2, 2, 2)
                                .add(jLabel_info_lastvalue)
                                .add(1, 1, 1)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel6)
                                        .add(jTextArea_info_description, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Available data", jPanel2);

        jScrollPane3.setViewportView(jList_mydata);

        jLabel3.setText("Name:");

        jLabel4.setText("Description:");

        jButton_register.setText("Register");
        jButton_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_registerActionPerformed(evt);
            }
        });


        jTextArea_registration_description.setColumns(20);
        jTextArea_registration_description.setRows(5);
        jScrollPane4.setViewportView(jTextArea_registration_description);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(8, 8, 8)
                                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(jLabel4)
                                                        .add(jLabel3)))
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane4)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_registration_name)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 334, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                        .add(jPanel1Layout.createSequentialGroup()
                                                .add(124, 124, 124)
                                                .add(jButton_register, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField_registration_name)
                                .add(5, 5, 5)
                                .add(jLabel4)
                                .add(7, 7, 7)
                                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton_register, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("My data", jPanel1);

        jButton_connect.setText("Connect");
        jButton_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_connectActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(jTabbedPane1)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                                .add(jLabel1)
                                                .add(2, 2, 2)
                                                .add(jTextField_server, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel2)
                                                .add(2, 2, 2)
                                                .add(jTextField_port, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 28, Short.MAX_VALUE)
                                                .add(jButton_connect)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap(34, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jTextField_server, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jTextField_port, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jButton_connect, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 393, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_connectActionPerformed

        this.serverAddress = this.jTextField_server.getText();
        if (isNumeric(this.jTextField_port.getText())) {
            this.serverPort = Integer.parseInt(this.jTextField_port.getText());
        }

        boolean error = false;

        if (this.serverAddress == null || this.serverAddress.equals("")) {
            JOptionPane.showMessageDialog(this, "Incorrect server address!", "Cannot connect", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
        if (this.serverPort == -1) {
            JOptionPane.showMessageDialog(null, "Incorrect server port!", "Cannot connect", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
        if (!error) {
            client = new Client();
            client.startServerHandler();
            while (!client.isReady()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            jList_alldata.setEnabled(false);
            DefaultListModel model2 = new DefaultListModel();
            AvailableDataDatabase adDB = client.getDataList();
            Enumeration vEnum = adDB.elements();
            while (vEnum.hasMoreElements()) {
                String elem = (String) vEnum.nextElement();
                model2.addElement(elem);
            }
            jList_alldata.setModel(model2);
            jList_alldata.setEnabled(true);

            this.uDataList = new UpdateDataList();
            this.uDataList.start();
            this.uDataList.run = true;

        }
    }

    /**
     * Subscribe button action
     *
     * @param evt java.awt.event.ActionEvent
     */
    private void jButton_subscribeActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Subscribe button pressed");
        String selValue = (String) this.jList_alldata.getSelectedValue();

        ArrayList<String> data = new ArrayList<String>();
        jList_subscribed.setEnabled(false);
        jList_alldata.setEnabled(false);
        ListModel existingModel = jList_subscribed.getModel();
        DefaultListModel newModel = new DefaultListModel();
        int size = existingModel.getSize();
        for (int i = 0; i < size; i++) {
            data.add((String) existingModel.getElementAt(i));
        }
        //check is it already in subscribed list
        if (data != null && !data.contains(selValue)) {
            data.add(selValue);
            Collections.sort(data);
            Iterator itr = data.iterator();
            while (itr.hasNext()) {
                newModel.addElement(itr.next());
            }
            jList_subscribed.setModel(newModel);

            // subscribe for this data
            this.client.subscribeData(selValue);
        }
        jList_subscribed.setEnabled(true);
        jList_alldata.setEnabled(true);
    }

    /**
     * unSubscribe button action
     *
     * @param evt java.awt.event.ActionEvent
     */
    private void jButton_unsubscribeActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("unSubscribe button pressed");
        String selValue = (String) this.jList_subscribed.getSelectedValue();

        ArrayList<String> data = new ArrayList<String>();
        jList_subscribed.setEnabled(false);
        ListModel existingModel = jList_subscribed.getModel();
        DefaultListModel newModel = new DefaultListModel();
        int size = existingModel.getSize();
        for (int i = 0; i < size; i++) {
            data.add((String) existingModel.getElementAt(i));
        }
        //check is it already in subscribed list
        if (data.contains(selValue)) {
            data.remove(selValue);
            Collections.sort(data);

            Iterator itr = data.iterator();
            while (itr.hasNext()) {
                newModel.addElement(itr.next());
            }
            jList_subscribed.setModel(newModel);

            // unsubscribe for this data
            this.client.unsubscribeData(selValue);
        }

        jList_subscribed.setEnabled(true);
    }

    /**
     * subscribed item selectionaction
     *
     * @param listSelectionEvent ListSelectionEvent
     */
    private void jList_subscribedActionPerformed(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            String selValue = (String) this.jList_subscribed.getSelectedValue();
            String value = client.getData(selValue);
            this.setInfoLabelDataName(selValue);
            this.setInfoLabelDataLastValue(value);
        }
    }

    /**
     * register data button action
     *
     * @param evt java.awt.event.ActionEvent
     */
    private void jButton_registerActionPerformed(java.awt.event.ActionEvent evt) {
        String name = this.jTextField_registration_name.getText();
        if (name == null || name.length() < 1) {
            JOptionPane.showMessageDialog(this, "Incorrect data name!", "Cannot register data", JOptionPane.ERROR_MESSAGE);
        } else if (this.client == null || !this.client.isReady()) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server!", "Cannot register data", JOptionPane.ERROR_MESSAGE);
        } else {
            this.jTextField_registration_name.setText("");
            this.client.registerData(name);

            ArrayList<String> data = new ArrayList<String>();
            ArrayList<String> allData = new ArrayList<String>();
            this.jList_mydata.setEnabled(false);
            ListModel existingModel = jList_mydata.getModel();
            ListModel allDataModel = jList_alldata.getModel();
            DefaultListModel newModel = new DefaultListModel();
            int size = existingModel.getSize();
            for (int i = 0; i < size; i++) {
                data.add((String) existingModel.getElementAt(i));
            }

            size = allDataModel.getSize();
            for (int i = 0; i < size; i++) {
                allData.add((String) allDataModel.getElementAt(i));
            }

            //check is it already in subscribed list
            if (!data.contains(name) && !allData.contains(name)) {
                data.add(name);
                Collections.sort(data);

                Iterator itr = data.iterator();
                while (itr.hasNext()) {
                    newModel.addElement(itr.next());
                }
                jList_mydata.setModel(newModel);
            } else {
                JOptionPane.showMessageDialog(this, "This name is already registered!", "Cannot register data", JOptionPane.ERROR_MESSAGE);
            }
            jList_mydata.setEnabled(true);
        }
    }


    /**
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    public static boolean isNumeric(String aStringValue) {
        Pattern pattern = Pattern.compile("\\d+");

        Matcher matcher = pattern.matcher(aStringValue);
        return matcher.matches();
    }

    public void setInfoLabelDataName(String value) {
        this.jLabel_info_name.setText("Name: " + value);
    }

    public void setInfoLabelDataLastValue(String value) {
        this.jLabel_info_lastvalue.setText("Last value: " + value);
    }

    private class UpdateDataList extends Thread {
        private boolean run = true;

        @Override
        public void run() {
            while (this.run) {
                try {

                    sleep(10000);

                    jList_alldata.setEnabled(false);
                    DefaultListModel model2 = new DefaultListModel();
                    AvailableDataDatabase adDB = client.getDataList();
                    Enumeration vEnum = adDB.elements();
                    while (vEnum.hasMoreElements()) {
                        String elem = (String) vEnum.nextElement();
                        model2.addElement(elem);
                    }
                    jList_alldata.setModel(model2);
                    jList_alldata.setEnabled(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        }

        public void stopUpdate() {
            this.run = false;
        }
    }

}
