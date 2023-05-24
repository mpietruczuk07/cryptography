module com.example.lfsr {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.lfsr to javafx.fxml;
    exports com.example.lfsr;
}