package com.example.basiccryptographicalgorithms;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Arrays;

public class ColumnarTranspositionNumbers {
    private StringBuilder result = new StringBuilder();
    int[] orderKey;

    public String encrypt(String text, String key) {
        if (text.isEmpty() || key.isEmpty()) {
            showAlert("Please fill in all necessary fields! ");
            return "";
        } else {
            try {
                orderKey = Arrays.stream(key.split("-")).mapToInt(Integer::parseInt).toArray();
            } catch (NumberFormatException numberFormatException) {
                Platform.runLater(() -> showAlert(String.valueOf(numberFormatException)));
                throw numberFormatException;
            }

            int cols = orderKey.length;
            int rows = text.length() / cols;
            if (text.length() % cols != 0) rows += 1;

            char[][] textMatrix = new char[rows][cols];
            int index = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols && index < text.length(); j++) {
                    textMatrix[i][j] = text.charAt(index);
                    index++;
                }
            }

            result.setLength(0);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int pom = orderKey[j] - 1;
                    if (textMatrix[i][pom] == '\0')
                        result.append(" ");
                    else
                        result.append(textMatrix[i][pom]);
                }
            }

            return String.valueOf(result);
        }
    }

    public String decrypt(String text, String key) {
        if (text.isEmpty() || key.isEmpty()) {
            showAlert("Please fill in all necessary fields! ");
            return "";
        } else {
            try {
                orderKey = Arrays.stream(key.split("-")).mapToInt(Integer::parseInt).toArray();
            } catch (NumberFormatException numberFormatException) {
                Platform.runLater(() -> showAlert(String.valueOf(numberFormatException)));
                throw numberFormatException;
            }

            int cols = orderKey.length;
            int rows = text.length() / cols;
            if (text.length() % cols != 0) rows += 1;

            char[][] textMatrix = new char[rows][cols];
            int index = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols && index < text.length(); j++) {
                    int pom = orderKey[j] - 1;
                    textMatrix[i][pom] = text.charAt(index);
                    index++;
                }
            }

            result.setLength(0);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!(textMatrix[i][j] == '*'))
                        result.append(textMatrix[i][j]);
                }
            }

            return String.valueOf(result);
        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.CLOSE);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred...");
        alert.setContentText(String.valueOf(alertMessage));
        alert.showAndWait();
    }
}
