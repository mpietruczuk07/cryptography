package com.example.lfsr;

public class LFSRGenerator {
    private final int[] register;
    private final int[] taps;
    private final String initialState;

    public LFSRGenerator(int[] polynomial, String seed) {
        this.register = new int[polynomial[0]];
        this.taps = polynomial;
        this.initialState = seed;
    }

    public void setInitialState() {
        if (initialState.length() != register.length)
            System.out.println("The initial state value is invalid!");
        else {
            for (int i = 0; i < register.length; i++) {
                register[i] = Integer.parseInt(String.valueOf(initialState.charAt(i)));
            }
        }
    }

    public int generateRandomBit() {
        int outputBit = register[register.length - 1];
        int feedbackBit = 0;

        for (int i = 0; i < taps.length; i++) {
            feedbackBit ^= register[taps[i] - 1];
        }

        for (int i = register.length - 1; i > 0; i--) {
            register[i] = register[i - 1];
        }

        register[0] = feedbackBit;

        return outputBit;
    }

    public byte[] encrypt(byte[] dataToEncrypt) {
        setInitialState();
        byte[] encryptedMessage = new byte[dataToEncrypt.length];

        for (int i = 0; i < dataToEncrypt.length; i++) {
            encryptedMessage[i] = (byte) (dataToEncrypt[i] ^ generateRandomBit());
        }

        return encryptedMessage;
    }

    //decrypt is th same method as encrypt
    public byte[] decrypt(byte[] dataToDecrypt) {
        setInitialState();
        byte[] decryptedMessage = new byte[dataToDecrypt.length];

        for (int i = 0; i < dataToDecrypt.length; i++) {
            decryptedMessage[i] = (byte) (dataToDecrypt[i] ^ generateRandomBit());
        }

        return decryptedMessage;
    }

}
