/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.customize;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author SBE
 */
public class DiffieHellman {

    //keySize = 1024
    //Private key
    private BigInteger privateKey;
    //Common Key
    BigInteger p;
    BigInteger g;

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public BigInteger getPublicKey() {
        return g.modPow(privateKey, p);
    }

    public BigInteger getPrivateCommonKey(BigInteger publicKey) {
        return publicKey.modPow(privateKey, p);
    }

    private void generateCommonKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DiffieHellman");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

//        BigInteger x = ((DHPrivateKey) keyPair.getPrivate()).getX();
//        BigInteger y = ((DHPublicKey) keyPair.getPublic()).getY();
//        System.out.println(x);
//        System.out.println(y);
//        DHParameterSpec parameter = ((DHPrivateKey) keyPair.getPrivate()).getParams();
//        System.out.println(parameter.getG());
//        System.out.println(parameter.getP());
//        //y = (G ^ x) mod P
//        System.out.println("\n" + g.modPow(x, p));
        DHParameterSpec params = ((DHPublicKey) keyPair.getPublic()).getParams();
        p = params.getP(); //modules
        g = params.getG(); //prime of p

        System.out.println(p);
        System.out.println(g);

    }

    public static void main(String[] args) {
        try {
            DiffieHellman dh = new DiffieHellman();
            dh.generateCommonKey();

            dh.setPrivateKey(BigInteger.valueOf(25));
            BigInteger pubA = dh.getPublicKey();

            dh.setPrivateKey(BigInteger.valueOf(45));
            BigInteger pubB = dh.getPublicKey();

            System.out.println("Private Common Key: B calculate:" + dh.getPrivateCommonKey(pubA));
            dh.setPrivateKey(BigInteger.valueOf(25));
            System.out.println("Private Common Key: A calculate:" + dh.getPrivateCommonKey(pubB));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DiffieHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
