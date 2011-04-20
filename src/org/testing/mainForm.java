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

import org.socsimnet.client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 3/30/11
 * Time: 10:05 PM
 */
public class mainForm {
    private JPanel panel1;
    private JTextField textFieldServer;
    private JButton connectButton;
    private JList list1;
    private JList list2;
    Client client;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Communication adapter - http://www.socsim.net");
        frame.setContentPane(new mainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private JList list3;
    private JButton addButton;
    private JButton removeButton;
    private JLabel infoLabelName;
    private JLabel infoLabelOwner;
    private JLabel infoLabelLastValue;
    private JTextField textFieldPort;

    public mainForm() {
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // check for doubleclick
                if (mouseEvent.getClickCount() == 2) {
                    String selValue = (String) list1.getModel().getElementAt(list1.getSelectedIndex());
                    DefaultListModel data = (DefaultListModel) list2.getModel();
                    if (!data.contains(selValue)) {
                        data.addElement(selValue);
                        int numItems = data.getSize();
                        String[] a = new String[numItems];
                        for (int i = 0; i < numItems; i++) {
                            a[i] = (String) data.getElementAt(i);
                        }
                        Arrays.sort(a);
                        int newIdx = 0;
                        for (int i = 0; i < numItems; i++) {
                            if (selValue.equals(a[i])) newIdx = i;
                            data.setElementAt(a[i], i);
                        }
                        list2.ensureIndexIsVisible(newIdx);
                        list2.clearSelection();
                        clearInfoLabels();
                    }

                }
            }
        });

        list2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                if (list2.getSelectedIndex() > -1) {
                    String selValue = (String) list2.getModel().getElementAt(list2.getSelectedIndex());
                    infoLabelName.setText("Name:" + selValue);
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String server = textFieldServer.getText();
                String port = textFieldPort.getText();
                boolean error = false;

                if (server == null || server.equals("")) {
                    JOptionPane.showMessageDialog(null, "Incorrect server address!", "Cannot connect", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }
                if (server == null || server.equals("") || !isNumeric(port)) {
                    JOptionPane.showMessageDialog(null, "Incorrect server port!", "Cannot connect", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }


                if (!error) {
                    client = new Client(server, Integer.parseInt(port));
                    client.startServerHandler();
                }
            }
        });
    }

    private void clearInfoLabels() {
        infoLabelName.setText("Name:");
        infoLabelOwner.setText("Owner:");
        infoLabelLastValue.setText("Last value:");
    }

    public static boolean isNumeric(String aStringValue) {
        Pattern pattern = Pattern.compile("\\d+");

        Matcher matcher = pattern.matcher(aStringValue);
        return matcher.matches();
    }


}
