module com.alx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.almasb.fxgl.all;

    opens com.alx to javafx.fxml;
    exports com.alx;
}
