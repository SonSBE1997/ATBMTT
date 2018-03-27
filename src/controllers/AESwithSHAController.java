/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.AES;
import models.SHA;

/**
 *
 * @author SBE
 */
public class AESwithSHAController extends AES_DESController {

    int receiveNumber;
    String hash;

    public AESwithSHAController() {
        receiveNumber = 0;
        hash = "";
    }

    @Override
    public void receiveData(String message) {
        receiveNumber++;
        if (receiveNumber == 1) {
            hash = AES.decrypt(message, txtKey.getText().trim());
        } else {
            String decypt = AES.decrypt(message, txtKey.getText().trim());
            if (hash.equals(SHA.encrytSHA1(decypt))) {
                txtDecryptMessage.setText(txtDecryptMessage.getText() + decypt + "\n=>Mesage wasn't edited\n");
            } else {
                txtDecryptMessage.setText(txtDecryptMessage.getText() + decypt + "\n=>Mesage was edited\n");
            }
            txtOriginalMessage.setText(txtOriginalMessage.getText() + message + "\n\n");
            receiveNumber = 0;
            hash = "";
        }

    }

    @Override
    public void sendEncyptedData(String send) {

        String hashText = SHA.encrytSHA1(send);
        client.send(AES.encrypt(hashText, key));
        client.send(AES.encrypt(send, key));
    }

}
