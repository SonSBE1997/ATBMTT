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
        final int keySize = 2048; //đặt kích thước khóa là 2048 bíts// kích thước khóa của RSA có thể:từ 1024 bits trở lên
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");//bộ sinh khóa có sẵn
        keyPairGenerator.initialize(keySize); //sinh cặp khóa
        return keyPairGenerator.genKeyPair(); //trả về cặp khóa
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

// Demo
    public static void main(String[] args) throws Exception {
        // generate public and private keys
        KeyPair keyPair = RSA.buildKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String originalMess = "từng bước chân cuốn đi mùa thu xa lắm";
        //////////Ký
        System.out.println("Ký");
        // sign the message
        String sign = RSA.sign(privateKey, originalMess);
        System.out.println("Original message:" + originalMess);
        System.out.println("Sign:" + sign);
        // verify the message
        String verifySignature = RSA.verifySignature(publicKey, sign);
        System.out.println("Verify:" + verifySignature);
        
        
        //////// Mã hóa
        System.out.println("Mã hóa");
        //encrypt the message
        String encrypted = RSA.encrypt(publicKey, originalMess);
        System.out.println("Original message:" + originalMess);
        System.out.println("Encrypt:" + sign);
        // decrypt the message
        String decypted = RSA.decrypt(privateKey, encrypted);
        System.out.println("Decrypt:" + decypted);
    }
}
