module rutas.com.rutastransporte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens rutas.com.rutastransporte to javafx.fxml;
    exports rutas.com.rutastransporte;
}