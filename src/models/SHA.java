/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author SBE
 */
public class SHA {

    public static String encrytSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1"); //Lấy thuật toán băm SHA
            //Băm mảng byte input (input.getBytes()) và lưu vào messageDigest 
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest); //chuyển mảng băm sang số lớn // dòng này ko hiểu lắm :((
            String hashtext = number.toString(16); //Chuyển số lớn sang hệ cơ số 16 ,sau đó chuyển thành dang chuỗi  
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String a = "2018";
        String b = "2018";
        String c = "2017";
        System.out.println("A equals B?? +" + encrytSHA1(a).equals(encrytSHA1(b)));//so sánh mảng băm a vs mảng băm b
        System.out.println("A equals C?? +" + encrytSHA1(a).equals(encrytSHA1(c)));//so sánh mảng băm a vs mảng băm c
    }
}
