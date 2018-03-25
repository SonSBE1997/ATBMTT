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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.KeyToString;
import models.RSA;
import models.UserWithPublicKey;

/**
 *
 * @author SBE
 */
public class RSAController extends RSA_DSAController {

    int numberReceive;
    String receiveMessage;
    String decryptMessage;
    boolean[] chk;

    public RSAController() {
        numberReceive = -1;
        receiveMessage = "";
        decryptMessage = "";
        chk = new boolean[2];
        Arrays.fill(chk, false);
    }

    public void generateKey() {
        try {
            KeyPair keyPair = RSA.buildKeyPair();
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
            if (selectedPublicKey.equals("")) {
                return;
            }
            String signed = RSA.sign(privateKey, getSendMessageWithSenderName(send));//ky
            int start = signed.length() / 2;
            String encryptMess1 = RSA.encrypt(KeyToString.strToPublicKeyRSA(selectedPublicKey), signed.substring(0, start));//mahoa
            String encryptMess2 = RSA.encrypt(KeyToString.strToPublicKeyRSA(selectedPublicKey), signed.substring(start));//mahoa
            client.send(encryptMess1);
            client.send(encryptMess2);
        } catch (Exception ex) {
            System.out.println("error in line 48 file RSAandDSAController");
            Logger.getLogger(RSAandDSAController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void receiveData(String message) {
        numberReceive++;
        receiveMessage += message;
        try {
            decryptMessage += RSA.decrypt(privateKey, message);
            chk[numberReceive] = true;
        } catch (Exception ex) {
            chk[numberReceive] = false;
        }

        if (numberReceive == 1) {
            txtOriginalMessage.setText(txtOriginalMessage.getText() + receiveMessage + "\n");
            if (chk[0] && chk[1]) {
                boolean check = false;
                for (int i = 0; i < cbModel.getSize(); i++) {
                    if (check) {
                        break;
                    }
                    try {
                        PublicKey key = KeyToString.strToPublicKeyRSA(((UserWithPublicKey) cbModel.getElementAt(i)).getPublicKey());
                        String verify = RSA.verifySignature(key, decryptMessage);
                        txtDecryptMessage.setText(txtDecryptMessage.getText() + verify + "\n");
                        check = true;
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                        System.out.println("Chuyển đổi khóa lỗi");
                    } catch (Exception ex) {
                    }
                }
                if (!check) {
                    txtDecryptMessage.setText(txtDecryptMessage.getText() + "Bạn ko đọc được tin nhắn này" + "\n");
                }
            } else {
                txtDecryptMessage.setText(txtDecryptMessage.getText() + "Bạn ko đọc được tin nhắn này" + "\n");
            }
            Arrays.fill(chk, false);
            receiveMessage = "";
            decryptMessage = "";
            numberReceive = -1;
        }
    }
}
