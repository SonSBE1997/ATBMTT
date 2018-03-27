/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

/**
 *
 * @author SBE
 */
public class DSA {

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String signed = sign("hihi", privateKey);
        System.out.println("" + signed);
        System.out.println("" + verify("hihi", signed, KeyToString.strToPublicKeyDSA(KeyToString.publicKeyToString(pubKey))));
    }

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 512;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA1withDSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes("UTF-8"));
        byte[] signatureByte = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signatureByte);
    }

    public static boolean verify(String plaintext, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA1withDSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plaintext.getBytes("UTF-8"));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }
}
