/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import models.AES;
import models.DES;
import socket.IPAddress;
import socket.Client;

/**
 *
 * @author SBE
 */
public class AESandDESController {
//Attribute

    Client client;
    boolean isConnected;
    String algorithm;
    JTextPane txtOriginalMessage;
    JTextPane txtDecryptMessage;
    JTextField txtName, txtKey;
    JTextArea txtSendMessage;
    String key;

// Constuctor
    public AESandDESController() {
        client = new Client();
        key = "";
    }

//Getter and setter 
    public void setTxtKey(JTextField txtKey) {
        this.txtKey = txtKey;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setTxtOriginalMessage(JTextPane txtOriginalMessage) {
        this.txtOriginalMessage = txtOriginalMessage;
    }

    public void setTxtDecryptMessage(JTextPane txtDecryptMessage) {
        this.txtDecryptMessage = txtDecryptMessage;
    }

    public void setTxtName(JTextField txtName) {
        this.txtName = txtName;
    }

    public void setTxtSendMessage(JTextArea txtSendMessage) {
        this.txtSendMessage = txtSendMessage;
    }

//Method
    public void connectToServer(String ipAddress, String port) {
        if (!IPAddress.validate(ipAddress)) {
            JOptionPane.showMessageDialog(null, "IPAddress invalid!!");
            isConnected = false;
            return;
        }
        try {
            int portNumber = Integer.parseInt(port);
            isConnected = client.connectToServer(ipAddress, portNumber);
            if (!isConnected) {
                return;
            }
            JOptionPane.showMessageDialog(null, "Connect to server successfull");
            isConnected = true;
            new Thread(() -> {
                receive(client.getServer());
            }).start();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port isn't a number!!");
            isConnected = false;
        }
    }

    public void sendMessage() {
        String send = txtSendMessage.getText().trim();
        if (send.equals("")) {
            return;
        }
        txtSendMessage.setText("");
        if (key.equals("")) {
            client.send("Key is: " + send);
            txtKey.setText(send);
            key = send;
            return;
        }
        txtOriginalMessage.setText(txtOriginalMessage.getText() + "u: " + send + "\n");
        txtDecryptMessage.setText(txtDecryptMessage.getText() + "u: " + send + "\n");
        if (algorithm.equals("AES")) {
            //begin test
//            String temp = AES.encrypt(getSendMessageWithSenderName(send), key);
//            System.out.println(temp);
//            System.out.println(AES.decrypt(temp, txtKey.getText().trim()));
            //end test
            client.send(AES.encrypt(getSendMessageWithSenderName(send), key));
            return;
        }

        if (algorithm.equals("DES")) {
            client.send(DES.encrypt(getSendMessageWithSenderName(send), key));
        }
    }

    public String getSendMessageWithSenderName(String message) {
        return String.format("%s: %s", txtName.getText().trim(), message);
    }

    public void receive(Socket server) {
        while (true) {
            try {
                /* Khái báo các luồng vào dữ liệu*/
                InputStream is = server.getInputStream();
                DataInputStream din = new DataInputStream(is);
                /* -----------------------------------------------
                * Nhận mảng byte dữ liệu từ server gửi về
                * và chuyển sang chuỗi và hiển thị lên màn hình
                * -----------------------------------------------*/
                int bufferSize = server.getReceiveBufferSize();
                byte[] bytes = new byte[bufferSize];
                if (din.read(bytes) >= 0) {
                    String message = new String(bytes).trim();
                    if (message.compareTo("") == 0) {
                        return;
                    }

                    if (message.contains("Key is:")) {
                        txtKey.setText(message.substring(8));
                        key = message.substring(8);
                    } else {
                        if (key.equals("")) {
                            return;
                        }
                        txtOriginalMessage.setText(txtOriginalMessage.getText() + message + "\n");
                        if (algorithm.equals("AES")) {
                            txtDecryptMessage.setText(txtDecryptMessage.getText() + AES.decrypt(message, txtKey.getText().trim()) + "\n");
                        } else if (algorithm.equals("DES")) {
                            txtDecryptMessage.setText(txtDecryptMessage.getText() + DES.decrypt(message, txtKey.getText().trim()) + "\n");
                        }
                    }
                }
            } catch (IOException ex) {
                client.closeConnection();
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
