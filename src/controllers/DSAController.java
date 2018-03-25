/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.DSA;
import models.KeyToString;
import models.UserWithPublicKey;

/**
 *
 * @author SBE
 */
public class DSAController extends RSA_DSAController {

    public void generateKey() {
        try {
            KeyPair keyPair = DSA.buildKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            txtPrivateKey.setText(KeyToString.privateKeyToString(privateKey));
            txtPublicKey.setText(KeyToString.publicKeyToString(publicKey));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSAandDSAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendData() {
        String send = txtSendMessage.getText().trim();
        txtSendMessage.setText("");
        try {
            String signed = DSA.sign(txtName.getText(), privateKey);//Ký
            client.send(send + ":" + signed.trim());
        } catch (Exception ex) {
            System.out.println("error in line 44 file RSAandDSAController");
            Logger.getLogger(RSAandDSAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void receiveData(String message) {
        txtOriginalMessage.setText(txtOriginalMessage.getText() + message + "\n");
        boolean check = false;
        String[] result = message.split(":");
        for (int i = 0; i < cbModel.getSize(); i++) {
            try {
                UserWithPublicKey user = (UserWithPublicKey) cbModel.getElementAt(i);
                PublicKey key = KeyToString.strToPublicKeyDSA(user.getPublicKey());
                boolean verify = DSA.verify(user.getName(), result[1], key);
                if (verify) {
                    txtDecryptMessage.setText(txtDecryptMessage.getText() + result[0] + "\nSignature:" + user.getName() + "\n");
                    check = true;
                    break;
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                System.out.println("Chuyển đổi khóa lỗi");
            } catch (Exception ex) {
            }
        }
        if (!check) {
            txtDecryptMessage.setText(txtDecryptMessage.getText() + "Bạn ko đọc được tin nhắn này" + "\n");
        }
    }
}
