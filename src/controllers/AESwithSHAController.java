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

    @Override
    public void receiveData(String message) {
        String[] result = message.split(":");
        String decypt = AES.decrypt(result[0], txtKey.getText().trim());
        String hashText = SHA.encrytSHA1(decypt);
        if (hashText.equals(result[1])) {
            txtDecryptMessage.setText(txtDecryptMessage.getText() + decypt + "\nMesage wasn't edited\n");
        } else {
            txtDecryptMessage.setText(txtDecryptMessage.getText() + decypt + "\nMesage was edited\n");
        }
        txtOriginalMessage.setText(txtOriginalMessage.getText() + "\n");
    }

    @Override
    public void sendEncyptedData(String send) {

        String hashText = SHA.encrytSHA1(send);
        client.send(AES.encrypt(send, key) + ":" + hashText);
    }

}
