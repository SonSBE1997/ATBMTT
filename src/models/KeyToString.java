/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author SBE
 */
public class KeyToString {
///Public key

    public static PublicKey strToPublicKeyRSA(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes); //X509EncodedKeySpec
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return (PublicKey) fact.generatePublic(keySpec);
    }

    public static PublicKey strToPublicKeyDSA(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes); //X509EncodedKeySpec
        KeyFactory fact = KeyFactory.getInstance("DSA");
        return (PublicKey) fact.generatePublic(keySpec);
    }

    public static String publicKeyToString(PublicKey publicKey) {
        byte[] encodedPublicKey = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedPublicKey);
    }

//// Private key
    public static PrivateKey strToPrivateKeyRSA(String privatekeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privatekeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);//PKCS8EncodedKeySpec
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return (PrivateKey) fact.generatePrivate(keySpec);
    }

    public static PrivateKey strToPrivateKeyDSA(String privatekeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privatekeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);//PKCS8EncodedKeySpec
        KeyFactory fact = KeyFactory.getInstance("DSA");
        return (PrivateKey) fact.generatePrivate(keySpec);
    }

    public static String privateKeyToString(PrivateKey privateKey) {
        byte[] encodedPrivateKey = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedPrivateKey);
    }
}
