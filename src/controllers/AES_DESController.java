/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.swing.JTextField;

/**
 *
 * @author SBE
 */
public class AES_DESController extends Controller {
//Attribute

    String key;
    JTextField txtKey;

//Constructor
    public AES_DESController() {
        key = "";
    }

//Getter & setter
    public void setTxtKey(JTextField txtKey) {
        this.txtKey = txtKey;
    }

// Method
    public void sendMessage() {
        String send = txtSendMessage.getText().trim();
        txtSendMessage.setText("");
        if (send.equals("")) {
            return;
        }
        if (key.equals("")) {
            client.send("Key is: " + send);
            txtKey.setText(send);
            key = send;
            return;
        }
        txtOriginalMessage.setText(txtOriginalMessage.getText() + "u: " + send + "\n");
        txtDecryptMessage.setText(txtDecryptMessage.getText() + "u: " + send + "\n");

        sendEncyptedData(getSendMessageWithSenderName(send));
    }

    public void sendEncyptedData(String send) {
    }

    @Override
    public void receiveMessage(byte[] bytes) {
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
            receiveData(message);
        }
    }

    public void receiveData(String message) {
    }

}
