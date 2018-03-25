/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.customize;

import java.math.BigInteger;

/**
 *
 * @author SBE
 */
public class PublicKey {

    BigInteger n; //module (công bố công khai)
    BigInteger d; //số mũ công khai

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

    public PublicKey(BigInteger n, BigInteger d) {
        this.n = n;
        this.d = d;
    }

    public PublicKey() {
    }

    @Override
    public String toString() {
        return "PublicKey:\t" + "n=" + n + "\r\n\t\td=" + d;
    }
}
