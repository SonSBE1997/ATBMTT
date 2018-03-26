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
import socket.Client;
import socket.IPAddress;

/**
 *
 * @author SBE
 */
public class Controller {
    //Attribute

    Client client;
    boolean isConnected;
    JTextPane txtOriginalMessage;
    JTextPane txtDecryptMessage;
    JTextField txtName;
    JTextArea txtSendMessage;

// Constuctor
    public Controller() {
        client = new Client();
    }

//Getter and setter 
    public boolean getIsConnected() {
        return isConnected;
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
                    receiveMessage(bytes);
                }
            } catch (IOException ex) {
                client.closeConnection();
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void receiveMessage(byte[] bytes) {
    }
    
    public String getSendMessageWithSenderName(String message) {
        return String.format("%s: %s", txtName.getText().trim(), message);
    }
}
