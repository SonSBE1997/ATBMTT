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
public class PhanDuTrungHoa {

    /**
     * twoFactor: 2 thừa số
     *
     * @param m1
     * @param m2
     * @param A
     * @return result = A mod (m1 * m2)
     */
    public static BigInteger twoFactor(BigInteger m1, BigInteger m2, BigInteger A) {
        BigInteger M = m1.multiply(m2);

        BigInteger a1 = A.mod(m1).mod(M);
        BigInteger a2 = A.mod(m2).mod(M);

        BigInteger temp1 = m2.modInverse(m1).mod(M);
        BigInteger temp2 = m1.modInverse(m2).mod(M);

        BigInteger t11 = ((a1.multiply(m2)).multiply(temp1)).mod(M);
        BigInteger t12 = ((a2.multiply(m1)).multiply(temp2)).mod(M);

        return (t11.add(t12)).mod(M);
    }

    /**
     *
     * @param m: list factor of M
     * @param A
     * @return c = A mod (m0 * m1 * ... )
     */
    public static BigInteger Calculate(BigInteger[] m, BigInteger A) {
        BigInteger a[] = new BigInteger[m.length];
        BigInteger Mi[] = new BigInteger[m.length];
        BigInteger M = BigInteger.ONE;
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < m.length; i++) {
            M = M.multiply(m[i]);
            a[i] = A.mod(m[i]);
        }

        for (int i = 0; i < m.length; i++) {
            Mi[i] = M.divide(m[i]);
            Mi[i] = (Mi[i].multiply((Mi[i].modInverse(m[i])).mod(M))).mod(M);

            sum = sum.add(a[i].multiply(Mi[i]));
        }

        return sum.mod(M);
    }

    public static void main(String[] args) {
        BigInteger num1 = BigInteger.valueOf(19);
        BigInteger num2 = BigInteger.valueOf(29);
        BigInteger A = BigInteger.valueOf(91);

        BigInteger[] m = {num1, num2};
        System.out.println(Calculate(m, A.pow(109)));
        System.out.println(twoFactor(num1, num2, A.pow(109)));
    }
}
