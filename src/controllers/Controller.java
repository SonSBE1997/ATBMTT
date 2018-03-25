/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.Socket;
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
    String algorithm;
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

    public void receive(Socket server) {
    }

    public String getSendMessageWithSenderName(String message) {
        return String.format("%s: %s", txtName.getText().trim(), message);
    }
}
