/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.DES;

/**
 *
 * @author SBE
 */
public class DESController extends AES_DESController {

    @Override
    public void receiveData(String message) {
        txtOriginalMessage.setText(txtOriginalMessage.getText() + message + "\n");
        txtDecryptMessage.setText(txtDecryptMessage.getText() + DES.decrypt(message, txtKey.getText().trim()) + "\n");
    }

    @Override
    public void sendEncyptedData(String send) {
        client.send(DES.encrypt(send, key));
    }

}
