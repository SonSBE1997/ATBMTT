/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.customize;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author SBE
 */
public class RSACustomize {

    /**
     * keySize >1024,2048
     */
    private int keySize;

    public RSACustomize(int keySize) {
        this.keySize = keySize;
    }

    public RSACustomize() {
        this.keySize = 2048;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public PrivateKey generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        // Hàm sinh 1 số BigInteger với độ tin cậy của thuật toán Rabin Miller là 100 và có số bit = keySize
        BigInteger p = new BigInteger(keySize, 100, random);
        BigInteger q = new BigInteger(keySize, 100, random);

        BigInteger e;
        BigInteger fi = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));

        do {
            e = new BigInteger(keySize, 100, random);
        } while (!(fi.gcd(e)).equals(BigInteger.ONE) || fi.compareTo(e) < 0 || e.intValue() < 0);
        return new PrivateKey(p, q, e);
    }

    public PublicKey generatePublicKey(PrivateKey privateKey) {
        BigInteger p = privateKey.getP();
        BigInteger q = privateKey.getQ();
        BigInteger e = privateKey.getE();

        BigInteger n = privateKey.getN();
        BigInteger fi = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
        BigInteger d = e.modInverse(fi);
        return new PublicKey(n, d);
    }

    /**
     * Mã hóa bằng khóa công khai
     *
     * @param message
     * @param publicKey
     * @return
     */
    public byte[] encrypt(String message, PublicKey publicKey) {
        BigInteger messToBigInt = new BigInteger(message.getBytes());
        //C = M^d  mod n
        BigInteger encypted = messToBigInt.modPow(publicKey.getD(), publicKey.getN());
        return encypted.toByteArray();
    }

    /**
     * Giải mã bằng khóa riêng
     *
     * @param encrypted
     * @param privateKey
     * @return
     */
    public String decrypt(byte[] encrypted, PrivateKey privateKey) {
        BigInteger cipher = new BigInteger(encrypted);
        BigInteger original = cipher.modPow(privateKey.getE(), privateKey.getN());
        return new String(original.toByteArray());
    }

    /**
     *
     * @param message
     * @param privateKey
     * @return
     */
    public byte[] sign(String message, PrivateKey privateKey) {
        BigInteger messToBigInt = new BigInteger(message.getBytes());
        //C = M^d  mod n
        BigInteger encypted = messToBigInt.modPow(privateKey.getE(), privateKey.getN());
        return encypted.toByteArray();
    }

    /**
     *
     * @param encrypted
     * @param publicKey
     * @return
     */
    public String verifySignature(byte[] encrypted, PublicKey publicKey) {
        BigInteger cipher = new BigInteger(encrypted);

        BigInteger original = cipher.modPow(publicKey.getD(), publicKey.getN());
        return new String(original.toByteArray());
    }

    public static void main(String[] args) {
        String text = "I love you so much, Samyy";

        RSACustomize rsa = new RSACustomize();
        PrivateKey privateKey = rsa.generatePrivateKey();
//        System.out.println(privateKey.toString());
        PublicKey publicKey = rsa.generatePublicKey(privateKey);
//        System.out.println(publicKey.toString());

//        byte[] encrypt = rsa.encrypt(text, publicKey);
//        System.out.println("Encrypt:\n" + new String(encrypt));
//
//        System.out.println("\nOriginal Message: " + rsa.decrypt(encrypt, privateKey));
        byte[] encrypt = rsa.sign(text, privateKey);
        System.out.println("Encrypt:\n" + new String(encrypt));

        System.out.println("\nOriginal Message: " + rsa.verifySignature(encrypt, publicKey));

    }
}
