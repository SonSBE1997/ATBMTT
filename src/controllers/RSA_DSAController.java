/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.security.PrivateKey;
import java.security.PublicKey;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import models.UserWithPublicKey;

/**
 *
 * @author SBE
 */
public class RSA_DSAController extends Controller {
//Attribute

    DefaultComboBoxModel<UserWithPublicKey> cbModel;
    String selectedPublicKey;
    JTextField txtPrivateKey, txtPublicKey;
    JComboBox cbPublicKey;
    PrivateKey privateKey;
    PublicKey publicKey;

//Constructor
    public RSA_DSAController() {
        cbModel = new DefaultComboBoxModel<>();
        selectedPublicKey = "";
    }

//Getter and setter
    public void setTxtPrivateKey(JTextField txtPrivateKey) {
        this.txtPrivateKey = txtPrivateKey;
    }

    public void setTxtPublicKey(JTextField txtPublicKey) {
        this.txtPublicKey = txtPublicKey;
    }

    public void setCbPublicKey(JComboBox cbPublicKey) {
        this.cbPublicKey = cbPublicKey;
        cbPublicKey.setModel(cbModel);
    }

//Method
    public void SendPublicKey() {
        client.send("User public key:" + txtName.getText() + ":" + txtPublicKey.getText().trim());
    }

    @Override
    public void receiveMessage(byte[] bytes) {
        String message = new String(bytes).trim();
        if (message.compareTo("") == 0) {
            return;
        }

        if (message.contains("User public key:")) {
            String[] result = message.split(":");
            boolean check = false;
            for (int i = 0; i < cbModel.getSize(); i++) {
                if (result[2].equals(((UserWithPublicKey) cbModel.getElementAt(i)).getPublicKey())) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                cbModel.addElement(new UserWithPublicKey(result[1], result[2]));
            }
            cbPublicKey.addNotify();
        } else {
            receiveData(message);
        }
    }

    public void receiveData(String message) {
    }

    public void sendMessage() {
        String send = txtSendMessage.getText().trim();
        if (send.equals("")) {
            return;
        }
        txtOriginalMessage.setText(txtOriginalMessage.getText() + "u: " + send + "\n");
        txtDecryptMessage.setText(txtDecryptMessage.getText() + "u: " + send + "\n");
        sendData();
    }

    public void sendData() {
    }

    public void selectedComboBoxChangeEvent() {
        if (cbModel.getSize() == 0) {
            return;
        }
        selectedPublicKey = ((UserWithPublicKey) cbPublicKey.getSelectedItem()).getPublicKey();
    }
}
