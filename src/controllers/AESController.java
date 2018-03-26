/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.AES;

/**
 *
 * @author SBE
 */
public class AESController extends AES_DESController {

    @Override
    public void sendEncyptedData(String send) {
        client.send(AES.encrypt(send, key));
    }

    @Override
    public void receiveData(String message) {
        txtDecryptMessage.setText(txtDecryptMessage.getText() + AES.decrypt(message, txtKey.getText().trim()) + "\n");
    }

}
