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
 * @author SBE Khác với version 1 ở method generate Key
 */
public class RSACustomizeVersion2 {

    private BigInteger n, d, e;

    private int keySize;

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    public BigInteger getE() {
        return e;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public int getBitlen() {
        return keySize;
    }

    public void setBitlen(int bitlen) {
        this.keySize = bitlen;
    }

    public RSACustomizeVersion2() {
        keySize = 2048;
        SecureRandom r = new SecureRandom();
        BigInteger p = new BigInteger(keySize, 100, r);
        BigInteger q = new BigInteger(keySize, 100, r);
        n = p.multiply(q);
        BigInteger fi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        do {
            e = new BigInteger(keySize, 100, r);
        } while ((fi.gcd(e)).intValue() > 1 || fi.compareTo(e) < 0 || e.intValue() < 0);
        d = e.modInverse(fi);
    }

    /**
     *
     * @param message
     * @return
     */
    public String encrypt(String message) {
        return (new BigInteger(message.getBytes())).modPow(e, n).toString();
    }

    /**
     * @param message
     * @return
     */
    public BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    /**
     *
     * @param message
     * @return
     */
    public synchronized String decrypt(String message) {
        return new String((new BigInteger(message)).modPow(d, n).toByteArray());
    }

    /**
     * @param message
     * @return
     */
    public synchronized BigInteger decrypt(BigInteger message) {
        return message.modPow(d, n);
    }

    /**
     *
     */
    public synchronized void generateKeys() {
        SecureRandom r = new SecureRandom();
        BigInteger p = new BigInteger(keySize, 100, r);
        BigInteger q = new BigInteger(keySize, 100, r);
        n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
                .subtract(BigInteger.ONE));
        e = new BigInteger("3");
        while (m.gcd(e).intValue() > 1) {
            e = e.add(new BigInteger("2"));
        }
        d = e.modInverse(m);
    }

    /**
     * Testing
     *
     * @param args
     */
    public static void main(String[] args) {
        RSACustomizeVersion2 rsa = new RSACustomizeVersion2();

        String text1 = "Some thing just like this!";
        System.out.println("Plaintext: " + text1);
        BigInteger plaintext = new BigInteger(text1.getBytes());

        BigInteger ciphertext = rsa.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);
        plaintext = rsa.decrypt(ciphertext);

        String text2 = new String(plaintext.toByteArray());
        System.out.println("Plaintext: " + text2);
    }
}
