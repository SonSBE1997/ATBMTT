/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 *
 * @author SBE
 */
public class RSA {

    /**
     * Sinh cặp khóa riêng và khóa công khai
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * Ký bằng khóa riêng
     *
     * @param privateKey khóa riêng
     * @param message đoạn tin nhắn cần ký
     * @return
     * @throws Exception
     */
    public static String sign(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
    }

    /**
     * Xác nhận chữ ký bằng khóa công khai
     *
     * @param publicKey
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String verifySignature(PublicKey publicKey, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }

    /**
     * Mã hóa bằng khóa công khai
     *
     * @param publicKey
     * @param message
     * @return
     * @throws Exception
     */
    public static String encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes("UTF-8")));
    }

    /**
     * Giải mã bằng khóa riêng
     *
     * @param privateKey
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decrypt(PrivateKey privateKey, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
    }

    public static void main(String[] args) throws Exception {
        // generate public and private keys
        KeyPair keyPair = RSA.buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String strPrivate = KeyToString.privateKeyToString(privateKey);
        System.out.println("Private Key:" + privateKey);
        System.out.println("String private:" + strPrivate);
        System.out.println("Private Key:" + KeyToString.strToPrivateKeyRSA(strPrivate));
//        // encrypt the message
//        String encrypted = RSA.sign(privateKey, "This is a secret message");
//        System.out.println("\nSign:" + encrypted);  // <<encrypted message>>
//        String strPub = publicKeyToString(publicKey);
//        // decrypt the message
//        String secret = RSA.verifySignature(strToPublicKeyRSA(strPub), encrypted);
//        System.out.println("Verify:" + secret);     // This is a secret message
    }
}
