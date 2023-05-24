package com.example.lfsr;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    private TextField inputPolynomial, inputState;
    @FXML
    private TextArea randomBitOutput, messageInput, messageOutput;
    @FXML
    private Button stopBtn;
    @FXML
    private Label filePathLabel;

    private LFSRGenerator lfsrGenerator;
    private String filePath;
    private StringProperty stringProperty = new SimpleStringProperty();
    private SimpleBooleanProperty stopUpdatingProperty = new SimpleBooleanProperty();
    private Thread randomBitsUpdateThread;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        randomBitOutput.textProperty().bind(stringProperty);
        randomBitOutput.setWrapText(true);
        stopBtn.setOnAction(e -> stopUpdatingProperty.set(true));
    }

    @FXML
    private void onSaveBtnAction() {
        lfsrGenerator = null;

        if (!(inputPolynomial.getText().length() == 0 || inputState.getText().length() == 0)) {
            String inputPoly = inputPolynomial.getText().replace(",", "");
            String inputSeed = inputState.getText();
            int[] inputPolyBits = new int[inputPoly.length()];

            if (!(inputPolyBits[0] == inputSeed.length())) {

                for (int i = 0; i < inputPoly.length(); i++) {
                    inputPolyBits[i] = Integer.parseInt(String.valueOf(inputPoly.charAt(i)));
                }

                lfsrGenerator = new LFSRGenerator(inputPolyBits, inputSeed);
                lfsrGenerator.setInitialState();

            } else {
                setAlert("The length of initial state must be the same as the highest power of a polynomial!");
            }

        } else {
            setAlert("Please enter the polynomial and the initial state!");
        }
    }

    @FXML
    private void onStartBtnAction() {
        if (!(inputPolynomial.getText().length() == 0 || inputState.getText().length() == 0)) {
            lfsrGenerator.setInitialState();
            updateDynamicText(stringProperty, stopUpdatingProperty);
        } else {
            setAlert("Please enter the polynomial and the initial state and press Save!");
        }
    }

    @FXML
    private void onStopBtnAction() {
        if (randomBitsUpdateThread != null) {
            randomBitsUpdateThread.interrupt();
        }
    }

    @FXML
    private void onClearBtnAction() {
        stringProperty.set("");
        stopUpdatingProperty.set(false);
    }

    @FXML
    private void onSelectFileBtnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the file");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            filePathLabel.setText(file.getAbsolutePath());
            filePath = file.getAbsolutePath();
        }
    }

    @FXML
    private void onEncryptFileBtnAction() throws IOException {
        handleWithFile(true);
    }

    @FXML
    private void onDecryptFileBtnAction() throws IOException {
        handleWithFile(false);
    }

    @FXML
    private void onEncryptBtnAction() {
        handleWithMessage(true);
    }

    @FXML
    private void onDecryptBtnAction() {
        handleWithMessage(false);
    }

    @FXML
    private void onCopyBtnAction() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        if (!(messageOutput.getText().isEmpty())) content.putString(messageOutput.getText());

        clipboard.setContent(content);
    }

    @FXML
    private void onResultClearBtnAction() {
        messageInput.clear();
        messageOutput.clear();
    }

    private void updateDynamicText(StringProperty dynamicTextProperty, SimpleBooleanProperty stopUpdatingProperty) {
        randomBitsUpdateThread = new Thread(() -> {
            String dynamicText = "";
            while (!stopUpdatingProperty.get()) {
                int bit = lfsrGenerator.generateRandomBit();
                dynamicText = dynamicText + bit;
                dynamicTextProperty.set(dynamicText);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        randomBitsUpdateThread.setDaemon(true);
        randomBitsUpdateThread.start();
    }

    private void handleWithFile(Boolean isEncryptBtn) throws IOException {
        if (!(filePath.length() == 0)) {
            byte[] fileInBytes = Files.readAllBytes(Path.of(filePath));
            byte[] resultFileInBytes;

            if (isEncryptBtn == true) resultFileInBytes = lfsrGenerator.encrypt(fileInBytes);
            else resultFileInBytes = lfsrGenerator.decrypt(fileInBytes);

            String fileExtension = filePath.substring(filePath.length() - 4);
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";

            Files.write(Path.of(desktopPath + "\\output" + fileExtension), resultFileInBytes);
            Runtime.getRuntime().exec("explorer.exe /select," + desktopPath + "\\output"+ fileExtension);
        }

    }

    private void handleWithMessage(Boolean isEncryptBtn) {
        if (!(messageInput.getText().length() == 0)) {
            lfsrGenerator.setInitialState();
            String message = messageInput.getText();
            StringBuilder result = new StringBuilder();
            byte[] messageInBytes = message.getBytes();
            byte[] resultMessageInBytes;

            if (isEncryptBtn == true) resultMessageInBytes = lfsrGenerator.encrypt(messageInBytes);
            else resultMessageInBytes = lfsrGenerator.decrypt(messageInBytes);

            for (int i = 0; i < resultMessageInBytes.length; i++) {
                result.append((char) resultMessageInBytes[i]);
            }

            messageOutput.setText(String.valueOf(result));

        } else {
            setAlert("Please enter the message!");
        }
    }

    private void setAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}