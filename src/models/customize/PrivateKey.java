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
public class PrivateKey {

    BigInteger p;//số nguyên tố thứ nhất
    BigInteger q; //số nguyên tố thứ 2
    BigInteger e; //số mũ bí mật

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public BigInteger getE() {
        return e;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public PrivateKey() {
    }

    public PrivateKey(BigInteger p, BigInteger q, BigInteger e) {
        this.p = p;
        this.q = q;
        this.e = e;
    }

    /**
     *
     * @return p*q
     */
    public BigInteger getN() {
        return p.multiply(q);
    }

    @Override
    public String toString() {
        return "PrivateKey:\tp=" + p + "\r\n\t\tq=" + q + "\r\n\t\te=" + e + '}';
    }
}
