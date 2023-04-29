module com.alx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.alx to javafx.fxml;
    exports com.alx;
}
