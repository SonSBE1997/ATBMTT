/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author SBE
 */
public class AES {

    public static void main(String[] args) {
        String secret = "samy";

        String plainText = "Samy, i love u";
        String encryptedText = encrypt(plainText, secret);
        System.out.println("Key:" + new String(key));
        System.out.println("Encrypt:" + encryptedText);
        System.out.println("Decrypt:" + decrypt(encryptedText, secret));

    }

    public static SecretKeySpec secretKey;
    public static byte[] key;
    ///Key Size  128, 192, 256

    public static void setKey(String myKey) {
        try {
            key = myKey.getBytes("UTF-8");
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            key = md5.digest(key);
            key = Arrays.copyOf(key, 16);

            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println("Error while encrypting" + e.toString());
            return null;
        }
    }

    public static String decrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToEncrypt)));

        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println("Error while encrypting" + e.toString());
            return null;
        }
    }
}
