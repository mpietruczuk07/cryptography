package com.example.rsa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private Label bitSizeSliderValue;
    @FXML
    private Slider bitSizeSlider;
    @FXML
    private TextField inputP, inputQ, inputN1, inputN2, inputD, inputE;
    @FXML
    private TextArea messageToEncryptInput, encryptedMessageInput, messageToDecryptInput, decryptedMessageInput;
    @FXML
    private TableView<Variable> resultsTable;
    @FXML
    private TableColumn<Variable, String> tcVariable;
    @FXML
    private TableColumn<Variable, String> tcValue;
    @FXML
    private TableColumn<Variable, String> tcName;
    @FXML
    private TableColumn<Variable, String> tcFormula;
    private int bitSize;
    private final ObservableList<Variable> variableObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bitSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            bitSize = (int) bitSizeSlider.getValue();
            bitSizeSliderValue.setText(bitSize + " bits");
        });

        messageToEncryptInput.setWrapText(true);
        encryptedMessageInput.setWrapText(true);
        messageToDecryptInput.setWrapText(true);
        decryptedMessageInput.setWrapText(true);
    }

    @FXML
    private void autoBitSizeBtnAction() {
        int min = 8;
        int max = 4096;
        int range = max - min + 1;
        bitSize = (int) (Math.random() * range) + min;
        bitSizeSlider.setValue(bitSize);
        bitSizeSliderValue.setText(bitSize + " bits");
    }

    @FXML
    private void genPrimeNumbers() {
        bitSize = (int) bitSizeSlider.getValue();
        List<BigInteger> primes =  PrimeNumberGenerator.getPrimeNumbers(bitSize);
        inputP.setText(String.valueOf(primes.get(0)));
        inputQ.setText(String.valueOf(primes.get(1)));
    }

    @FXML
    private void genValuesBtnAction() {
        if (inputP.getText() == "" || inputQ.getText() == "")
            setAlert("Please generate numbers!");
        else {
            resultsTable.getItems().clear();

            BigInteger p = new BigInteger(inputP.getText());
            BigInteger q = new BigInteger(inputQ.getText());
            BigInteger n = RSA.findN(p, q);
            BigInteger phiN = RSA.findPhiN(p, q);
            BigInteger[] eArray = RSA.findE(phiN, 20);
            BigInteger e = eArray[new Random().nextInt(eArray.length)];
            int[] kArray = RSA.findK(phiN, e, 20);
            int k = kArray[new Random().nextInt(kArray.length)];
            BigInteger d = RSA.findD(k, phiN, e);

            variableObservableList.add(new Variable("n", String.valueOf(n), "modules", "n: p * q"));
            variableObservableList.add(new Variable("phiN", String.valueOf(phiN), "length", "phiN: (p - 1) * (q - 1)"));
            variableObservableList.add(new Variable("e", String.valueOf(e), "encryption key", "e: GCD(phiN, e) == 1 && e > 2"));
            variableObservableList.add(new Variable("d", String.valueOf(d), "decryption key", "d: k * phiN + 1 / e"));

            tcVariable.setCellValueFactory(new PropertyValueFactory<>("variable"));
            tcValue.setCellValueFactory(new PropertyValueFactory<>("value"));
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcFormula.setCellValueFactory(new PropertyValueFactory<>("formula"));

            resultsTable.setItems(variableObservableList);

            inputN1.setText(String.valueOf(n));
            inputN2.setText(String.valueOf(n));
            inputE.setText(String.valueOf(e));
            inputD.setText(String.valueOf(d));
        }
    }

    @FXML
    private void encryptBtnAction() {
        String textToEncrypt = messageToEncryptInput.getText();

        if (textToEncrypt == "")
            setAlert("Please enter text to encrypt!");
        else {
            byte[] textToEncryptBytes = textToEncrypt.getBytes();
            BigInteger e = new BigInteger(inputE.getText());
            BigInteger n = new BigInteger(inputN1.getText());

            encryptedMessageInput.clear();
            String output = (Arrays.toString((RSA.encrypt(n, e, textToEncryptBytes))).replace("[", "").replace("]", ""));
            encryptedMessageInput.setText(output);
        }
    }

    @FXML
    private void copyBtnAction() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        if (!(encryptedMessageInput.getText().isEmpty()))
            content.putString(encryptedMessageInput.getText());

        clipboard.setContent(content);
    }

    @FXML
    private void decryptBtnAction() {
        String textToDecrypt = messageToDecryptInput.getText();

        if (textToDecrypt == "")
            setAlert("Please enter text to decrypt!");
        else {
            Object[] textToDecryptArray = Arrays.stream(textToDecrypt.split(", ")).map(BigInteger::new).toArray();
            BigInteger[] textToDecryptInArray = new BigInteger[textToDecryptArray.length];

            for(int i = 0; i<textToDecryptArray.length; i++){
                textToDecryptInArray[i] = (BigInteger) textToDecryptArray[i];
            }

            BigInteger d = new BigInteger(inputD.getText());
            BigInteger n = new BigInteger(inputN1.getText());

            StringBuilder outputString = new StringBuilder();
            BigInteger[] decryptedTextArray = RSA.decrypt(n, d, textToDecryptInArray);

            for (int i = 0; i < decryptedTextArray.length; i++) {
                int pomValue = Integer.parseInt(decryptedTextArray[i].toString());
                char pomChar = (char) pomValue;
                outputString.append(pomChar);
            }

            decryptedMessageInput.setText(String.valueOf(outputString));
        }
    }

    @FXML
    private void clearAllBtnAction() {
        messageToEncryptInput.clear();
        encryptedMessageInput.clear();
        messageToDecryptInput.clear();
        decryptedMessageInput.clear();
    }

    private void setAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}