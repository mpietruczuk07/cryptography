package com.example.rsa;

import java.math.BigInteger;

public class RSA {

    public static BigInteger findN(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }

    public static BigInteger findPhiN(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    //Greatest Common Divisor
    public static BigInteger findGCD(BigInteger a, BigInteger b) {
        BigInteger temp;

        while (b.compareTo(BigInteger.ZERO) == 1) {
            temp = a.mod(b);
            a = b;
            b = temp;
        }
        return a;
    }

    public static BigInteger[] findE(BigInteger phiN, int quantity) {
        BigInteger possibleENumbers[] = new BigInteger[quantity];
        int iNum = 0;

        for (int i = 3; iNum < quantity && BigInteger.valueOf(i).compareTo(phiN) < 0; i += 2) {
            if (findGCD(phiN, BigInteger.valueOf(i)).equals(BigInteger.ONE))
                possibleENumbers[iNum++] = BigInteger.valueOf(i);
        }

        return possibleENumbers;
    }

    public static int[] findK(BigInteger phiN, BigInteger e, int quantity) {
        int possibleKNumbers[] = new int[quantity];
        int iNum = 0;

        for (int i = 0; iNum < quantity; i++) {
            if ((phiN.multiply(BigInteger.valueOf(i)).add(BigInteger.ONE).mod(e).equals(BigInteger.ZERO))) {
                possibleKNumbers[iNum++] = i;
            }
        }

        return possibleKNumbers;
    }

    public static BigInteger findD(int k, BigInteger phiN, BigInteger e) {
        return ((phiN.multiply(BigInteger.valueOf(k)).add(BigInteger.ONE)).divide(e));
    }

    public static BigInteger[] encrypt(BigInteger n, BigInteger e, byte[] toEncrypt) {
        BigInteger pomValue;
        BigInteger[] encrypted = new BigInteger[toEncrypt.length];

        for (int i = 0; i < toEncrypt.length; i++) {
            pomValue = BigInteger.valueOf(toEncrypt[i]);
            encrypted[i] = pomValue.modPow(e, n);
        }

        return encrypted;
    }

    public static BigInteger[] decrypt(BigInteger n, BigInteger d, BigInteger[] toDecrypt) {
        BigInteger pomValue;
        BigInteger[] decrypted = new BigInteger[toDecrypt.length];

        for (int i = 0; i < toDecrypt.length; i++) {
            pomValue = toDecrypt[i];
            decrypted[i] = pomValue.modPow(d, n);
        }

        return decrypted;
    }
}
