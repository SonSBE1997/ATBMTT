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
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import models.KeyToString;
import models.RSA;
import socket.Client;
import socket.IPAddress;

/**
 *
 * @author SBE
 */
public class RSAandDSAController {

    Client client;
    boolean isConnected;
    String algorithm;
    DefaultComboBoxModel<String> cbModel;
    String selectedPublicKey;

    JTextPane txtOriginalMessage;
    JTextPane txtDecryptMessage;
    JTextField txtName, txtPrivateKey, txtPublicKey;
    JTextArea txtSendMessage;
    JComboBox cbPublicKey;
    PrivateKey privateKey;
    PublicKey publicKey;
    List<PublicKey> lsPublicKeys;
//Constuctor

    public RSAandDSAController() {
        client = new Client();
        cbModel = new DefaultComboBoxModel<>();
        selectedPublicKey = "";
    }
//Getter and setter 

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setTxtPublicKey(JTextField txtPublicKey) {
        this.txtPublicKey = txtPublicKey;
    }

    public void setTxtPrivateKey(JTextField txtPrivateKey) {
        this.txtPrivateKey = txtPrivateKey;
    }

    public void setCbPublicKey(JComboBox cbPublicKey) {
        this.cbPublicKey = cbPublicKey;
        cbPublicKey.setModel(cbModel);
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

        txtOriginalMessage.setText(txtOriginalMessage.getText() + "u: " + send + "\n");
        txtDecryptMessage.setText(txtDecryptMessage.getText() + "u: " + send + "\n");
        if (algorithm.equals("RSA")) {
            try {
                if (selectedPublicKey.equals("")) {
                    return;
                }
//                String encryptedMessage = RSA.sign(privateKey, getSendMessageWithSenderName(send));
                String encryptedMessage = RSA.encrypt(KeyToString.strToPublicKeyRSA(selectedPublicKey), getSendMessageWithSenderName(send));
                client.send(KeyToString.publicKeyToString(publicKey) + ":" + encryptedMessage);
            } catch (Exception ex) {
                System.out.println("error in line 130 file RSAandDSAController");
                Logger.getLogger(RSAandDSAController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            return;
        }

        if (algorithm.equals("DSA")) {

        }
    }

    public void SendPublicKey() {
        client.send("User public key:" + txtPublicKey.getText().trim());
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

                    if (message.contains("User public key:")) {
                        String tempKey = message.substring(16);
                        boolean chk = false;
                        for (int i = 0; i < cbModel.getSize(); i++) {
                            if (tempKey.equals(cbModel.getElementAt(i))) {
                                chk = true;
                                break;
                            }
                        }
                        if (!chk) {
                            cbModel.addElement(tempKey);
                        }
                        cbPublicKey.addNotify();
                    } else {
                        txtOriginalMessage.setText(txtOriginalMessage.getText() + message + "\n");
                        String[] result = message.split(":");
                        boolean chk = false;
                        for (int i = 0; i < cbModel.getSize(); i++) {
                            if (result[0].equals(cbModel.getElementAt(i))) {
                                chk = true;
                                break;
                            }
                        }
                        if (!chk) {
                            cbModel.addElement(result[0]);
                        }
                        cbPublicKey.addNotify();

                        if (algorithm.equals("RSA")) {
                            try {
//                                PublicKey key = RSA.strToPublicKeyRSA(result[0]);
//                                String temp = RSA.verifySignature(key, result[1]);
                                String temp = RSA.decrypt(privateKey, result[1]);
                                txtDecryptMessage.setText(txtDecryptMessage.getText() + temp + "\n");
                            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                                System.out.println("Chuyển đổi khóa lỗi");
                            } catch (Exception ex) {
                                txtDecryptMessage.setText(txtDecryptMessage.getText() + "Bạn ko đọc được tin nhắn này" + "\n");
//                                System.out.println("Bạn ko đọc được tin nhắn này");
                                Logger.getLogger(RSAandDSAController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (algorithm.equals("DSA")) {
//                            txtDecryptMessage.setText(txtDecryptMessage.getText() + DES.decrypt(message, txtKey.getText().trim()) + "\n");
                        }
                    }
                }
            } catch (IOException ex) {
                client.closeConnection();
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getSendMessageWithSenderName(String message) {
        return String.format("%s: %s", txtName.getText().trim(), message);
    }

    public void generateKey() {
        try {
            KeyPair keyPair = null;
            if (algorithm.equals("RSA")) {
                keyPair = RSA.buildKeyPair();
            }
            if (keyPair == null) {
                return;
            }
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            txtPrivateKey.setText(KeyToString.privateKeyToString(privateKey));
            txtPublicKey.setText(KeyToString.publicKeyToString(publicKey));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSAandDSAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectedComboBoxChangeEvent() {
        if (cbModel.getSize() == 0) {
            return;
        }
        selectedPublicKey = (String) cbPublicKey.getSelectedItem();
    }
}
