module com.example.basiccryptographicalgorithms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.basiccryptographicalgorithms to javafx.fxml;
    exports com.example.basiccryptographicalgorithms;
}