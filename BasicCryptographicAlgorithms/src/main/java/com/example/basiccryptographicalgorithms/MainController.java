package com.example.basiccryptographicalgorithms;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final RailFence railFence = new RailFence();
    private final ColumnarTranspositionNumbers transpositionNumbers = new ColumnarTranspositionNumbers();
    private final ColumnarTranspositionText transpositionText = new ColumnarTranspositionText();
    private int selectedAlgorithmFlag, fenceHeight;
    private String text, key;
    @FXML
    private ToggleGroup selectedAlgorithm;
    @FXML
    private RadioButton railFenceBtn;
    @FXML
    private TextField messageInput, fenceHeightInput, keyInput;
    @FXML
    private TextArea resultTextBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageInput.setDisable(false);
        keyInput.setDisable(true);
        fenceHeightInput.setDisable(false);
        railFenceBtn.setSelected(true);
        selectedAlgorithmFlag = 0;

        selectedAlgorithm.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedBtn = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
            switch (selectedBtn.getId()) {
                case "railFenceBtn":
                    messageInput.setDisable(false);
                    keyInput.setDisable(true);
                    fenceHeightInput.setDisable(false);
                    selectedAlgorithmFlag = 0;
                    break;
                case "columnTransNumberBtn":
                    messageInput.setDisable(false);
                    keyInput.setDisable(false);
                    fenceHeightInput.setDisable(true);
                    selectedAlgorithmFlag = 1;
                    break;
                case "columnTransTextBtn":
                    messageInput.setDisable(false);
                    keyInput.setDisable(false);
                    fenceHeightInput.setDisable(true);
                    selectedAlgorithmFlag = 2;
                    break;
            }
        });
    }

    @FXML
    private void encryptBtnAction() {
        switch (selectedAlgorithmFlag) {
            case 0:
                text = messageInput.getText();
                fenceHeight = Integer.parseInt(fenceHeightInput.getText());
                clearData();
                resultTextBox.setText(railFence.encrypt(text, fenceHeight));
                break;
            case 1:
                text = messageInput.getText();
                key = keyInput.getText();
                clearData();
                resultTextBox.setText(transpositionNumbers.encrypt(text, key));
                break;
            case 2:
                text = messageInput.getText();
                key = keyInput.getText();
                clearData();
                resultTextBox.setText(transpositionText.encrypt(text, key));
                break;
        }
    }

    @FXML
    private void decryptBtnAction() {
        switch (selectedAlgorithmFlag) {
            case 0:
                text = messageInput.getText();
                fenceHeight = Integer.parseInt(fenceHeightInput.getText());
                clearData();
                resultTextBox.setText(railFence.decrypt(text, fenceHeight));
                break;
            case 1:
                text = messageInput.getText();
                key = keyInput.getText();
                clearData();
                resultTextBox.setText(transpositionNumbers.decrypt(text, key));
                break;
            case 2:
                text = messageInput.getText();
                key = keyInput.getText();
                clearData();
                resultTextBox.setText(transpositionText.decrypt(text, key));
                break;
        }
    }

    @FXML
    private void copyBtnAction() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        if (!(resultTextBox.getText().isEmpty()))
            content.putString(resultTextBox.getText());

        clipboard.setContent(content);
    }

    @FXML
    private void clearBtnAction() {
        resultTextBox.clear();
    }

    private void clearData() {
        messageInput.clear();
        fenceHeightInput.clear();
        keyInput.clear();
        resultTextBox.clear();
    }

    public void messageInputAction() {
    }

    public void keyInputAction() {
    }

    public void fenceHeightInputAction() {
    }

}