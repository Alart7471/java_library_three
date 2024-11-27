module com.example.library_three {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.library_three to javafx.fxml;
    exports com.example.library_three;
}
