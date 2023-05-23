package com.example.basiccryptographicalgorithms;

import java.util.Arrays;

public class RailFence {
    private StringBuilder result = new StringBuilder();

    public String encrypt(String text, int key) {
        int index = 0;

        if (key < 2) {
            return text;
        } else {
            char[][] railMatrix = new char[key][text.length()];

            for (int i = 0; i < key; i++) {
                Arrays.fill(railMatrix[i], '*');
            }

            for (int i = 0; i < text.length(); ) {
                for (int j = 0; j < key && i < text.length(); j++) {
                    railMatrix[j][i] = text.charAt(index);
                    i++;
                    index++;
                }
                for (int j = key - 2; j > 0 && i < text.length(); j--, i++) {
                    railMatrix[j][i] = text.charAt(index);
                    index++;
                }
            }

            result.setLength(0);

            for (int i = 0; i < key; i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (railMatrix[i][j] != '*') {
                        result.append(railMatrix[i][j]);
                    }
                }
            }
        }

        return String.valueOf(result);
    }

    public String decrypt(String text, int key) {
        char[][] railMatrix = new char[key][text.length()];
        int index = 0;

        for (int i = 0; i < key; i++) {
            Arrays.fill(railMatrix[i], '*');
        }

        for (int i = 0; i < text.length(); ) {
            for (int j = 0; j < key & i < text.length(); j++) {
                railMatrix[j][i] = '#';
                i++;
            }
            for (int j = key - 2; j > 0 && i < text.length(); j--, i++) {
                railMatrix[j][i] = '#';
            }
        }

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (railMatrix[i][j] == '#') {
                    railMatrix[i][j] = text.charAt(index);
                    index++;
                }
            }
        }

        index = 0;
        result.setLength(0);

        for (int i = 0; i < text.length(); ) {
            for (int j = 0; j < key & i < text.length(); i++, j++) {
                result.append(railMatrix[j][i]);
                index++;
            }
            for (int j = key - 2; j > 0 && i < text.length(); i++, j--) {
                result.append(railMatrix[j][i]);
                index++;
            }
        }

        return String.valueOf(result);
    }
}
