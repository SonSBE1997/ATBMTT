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
//Demo

    public static void main(String[] args) {
        String secret = "vì nếu em cần một bờ vai êm";
        String plainText = "nếu em cần những phút bình yên";

        String encryptedText = encrypt(plainText, secret);
        System.out.println("Encrypt:" + encryptedText);
        System.out.println("Decrypt:" + decrypt(encryptedText, secret));

    }

// Attribute
    ///kích thước khóa có thể là 128 bits, 192 bits, 256 bits
    public static SecretKeySpec secretKey;
    public static byte[] key;

    /**
     * Hàm khởi tạo khóa
     *
     * @param myKey chuỗi bí mật nhập vào
     */
    public static void setKey(String myKey) {
        try {
            key = myKey.getBytes("UTF-8"); //chuyển chuỗi bí mật thành mảng byte
            MessageDigest md5 = MessageDigest.getInstance("MD5"); //lấy thuật toán mã hóa MD5

            key = md5.digest(key); //mã hóa
            // copy 16 phần tử đầu tiên ở màng key ở trên và lưu lại vào mảng key
            key = Arrays.copyOf(key, 16);   //có thể hiểu là rút gọn mảng key

            secretKey = new SecretKeySpec(key, "AES"); //Khởi tao khóa
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Hàm mã hóa
     *
     * @param strToEncrypt chuỗi cần mã hóa
     * @param secret // chuỗi bí mật
     * @return chuỗi đã được mã hóa
     */
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret); // khởi tạo khóa
            Cipher cipher = Cipher.getInstance("AES");  //Lấy thuật toán mã hóa // hàm có sẵn
            //Khởi tạo thuật toán, để chế độ mã hóa với khóa được sinh ra ở trên
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] strToEncryptBytes = strToEncrypt.getBytes("UTF-8");// chuyển chuỗi cần mã hóa sang mảng byte
            byte[] encrypted = cipher.doFinal(strToEncryptBytes);//Mã hóa và lưu vào mảng byte
            // return: chuyển mảng byte đã mã hóa sang chuỗi
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println("Error while encrypting" + e.toString());
            return null;
        }
    }

    public static String decrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret); // khởi tạo khóa
            Cipher cipher = Cipher.getInstance("AES");//Lấy thuật toán mã hóa
            //Khởi tạo thuật toán, để chế độ giải hóa với khóa được sinh ra ở trên
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            // chuyển chuỗi đã mã hóa thành mảng byte
            byte[] encrypted = Base64.getDecoder().decode(strToEncrypt);
            byte[] decrypted = cipher.doFinal(encrypted); // giải mã
            return new String(decrypted); // chuyển mảng byte đã giải mã về dạng chuỗi

        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println("Error while encrypting" + e.toString());
            return null;
        }
    }
}
