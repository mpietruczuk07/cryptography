package com.example.basiccryptographicalgorithms;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;

import java.util.*;

public class ColumnarTranspositionText {
    private StringBuilder result = new StringBuilder();

    public String encrypt(String text, String key) {
        if(text.isEmpty() || key.isEmpty())
            showAlert();

        String combinedText = text.replace(" ", "");
        int cols = key.length();
        int rows = combinedText.length() / key.length();

        if (combinedText.length() % cols != 0) rows += 1;

        char[][] textMatrix = new char[rows][cols];
        int index = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols && index < combinedText.length(); j++) {
                textMatrix[i][j] = combinedText.charAt(index);
                index++;
            }
        }

        List<Pair<Character, Integer>> keyPair = new ArrayList<Pair<Character, Integer>>();
        for (int i = 0; i < key.length(); i++) {
            keyPair.add(new Pair<>(key.charAt(i), i));
        }

        Collections.sort(keyPair, Comparator.comparing(Pair::getKey));
        result.setLength(0);

        for (int i = 0; i < key.length(); i++) {
            int j = keyPair.get(i).getValue();
            for (int k = 0; k < rows; k++) {
                if (textMatrix[k][j] == '\0')
                    result.append(" ");
                else
                    result.append(textMatrix[k][j]);
            }
        }
        return String.valueOf(result);
    }

    public String decrypt(String text, String key) {
        if(text.isEmpty() || key.isEmpty())
            showAlert();

        String combinedText = text.replace(" ", "");
        List<Pair<Character, Integer>> keyPair = new ArrayList<Pair<Character, Integer>>();
        int cols = key.length();
        int rows = combinedText.length() / key.length();

        if (combinedText.length() % cols != 0) rows += 1;

        for (int i = 0; i < key.length(); i++) {
            keyPair.add(new Pair<>(key.charAt(i), i));
        }

        Collections.sort(keyPair, Comparator.comparing(Pair::getKey));

        int index = 0;
        char[][] textMatrix = new char[rows][cols];
        result.setLength(0);

        for (int i = 0; i < cols; i++) {
            int j = keyPair.get(i).getValue();
            for (int k = 0; k < rows && index < text.length(); k++) {
                textMatrix[k][j] = text.charAt(index);
                index++;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!(textMatrix[i][j] == '\0'))
                    result.append(textMatrix[i][j]);
            }
        }

        return String.valueOf(result);
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.CLOSE);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred...");
        alert.setContentText("Please fill in all necessary fields!");
        alert.showAndWait();
    }
}
