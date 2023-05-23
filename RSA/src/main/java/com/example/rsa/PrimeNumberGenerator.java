package com.example.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PrimeNumberGenerator {
    private static int[] first100Primes = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29,
            31, 37, 41, 43, 47, 53, 59, 61, 67,
            71, 73, 79, 83, 89, 97, 101, 103,
            107, 109, 113, 127, 131, 137, 139,
            149, 151, 157, 163, 167, 173, 179,
            181, 191, 193, 197, 199, 211, 223,
            227, 229, 233, 239, 241, 251, 257,
            263, 269, 271, 277, 281, 283, 293,
            307, 311, 313, 317, 331, 337, 347,
            349, 353, 359, 367, 373, 379, 383,
            389, 397, 401, 409, 419, 421, 431,
            433, 439, 443, 449, 457, 461, 463,
            467, 479, 487, 491, 499, 503, 509,
            521, 523, 541};

    public static BigInteger getNBitRandom(int size) {
        SecureRandom secureRandom = new SecureRandom();
        BigInteger randomNumber = new BigInteger(size, secureRandom);
        return randomNumber;
    }

    public static BigInteger getLowLevelPrime(int size) {
        while (true) {
            BigInteger primeCandidate = getNBitRandom(size);

            for (int primeNumber : PrimeNumberGenerator.first100Primes) {
                if (primeCandidate.mod(BigInteger.valueOf(primeNumber)).equals(BigInteger.ZERO) && BigInteger.valueOf((long) Math.pow(primeNumber, 2)).compareTo(primeCandidate) < 0)
                    break;
                else
                    return primeCandidate;
            }
        }
    }

    //fast modular exponentiation algorithm
    private static BigInteger fastExpMod(BigInteger base, BigInteger exp, BigInteger mod) {
        if (exp.equals(BigInteger.ZERO))
            return BigInteger.ONE;
        if (!(exp.mod(BigInteger.TWO).equals(BigInteger.ZERO)))
            return (base.multiply(fastExpMod(base, (exp.subtract(BigInteger.ONE)), mod)).mod(mod));
        return ((fastExpMod(base, (exp.divide(BigInteger.TWO)), mod)).pow(2)).mod(mod);
    }

    private static boolean isComposite(BigInteger roundTester, BigInteger evenComponent, BigInteger primeCandidate, long maxDivisionsByTwo) {
        if (fastExpMod(roundTester, evenComponent, primeCandidate).equals(BigInteger.ONE))
            return false;

        for (int i = 0; i < maxDivisionsByTwo; i++) {
            if (fastExpMod(roundTester, evenComponent.multiply(BigInteger.ONE.shiftLeft(i)), primeCandidate).equals(primeCandidate.subtract(BigInteger.valueOf(1))))
                return false;
        }
        return true;
    }

    private static boolean isMillerRabinPassed(BigInteger millerRabinCandidate, int size) {
        long maxDivisionsByTwo = 0;
        BigInteger evenComponent = millerRabinCandidate.subtract(BigInteger.valueOf(1));

        while (evenComponent.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            evenComponent = evenComponent.shiftRight(1);
            maxDivisionsByTwo += 1;
        }

        int numberOfRabinTrials = 10;
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < numberOfRabinTrials; i++) {
            BigInteger roundTester = new BigInteger(size, secureRandom);
            if (isComposite(roundTester, evenComponent, millerRabinCandidate, maxDivisionsByTwo))
                return false;
        }

        return true;
    }
    private static BigInteger getPrimeNumber(int size) {
        while (true) {
            BigInteger prime = getLowLevelPrime(size);
            if (isMillerRabinPassed(prime, size)) {
                return prime;
            } else {
            }
        }
    }

    public static List<BigInteger> getPrimeNumbers(int size) {
        List<BigInteger> numbers = new ArrayList<>();
        BigInteger p = getPrimeNumber(size);
        BigInteger q;

        do {
            q = getPrimeNumber(size);
        }
        while (p == q);

        numbers.add(p);
        numbers.add(q);

        return numbers;
    }
}
