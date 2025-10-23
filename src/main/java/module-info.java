module rutas.com.rutastransporte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens rutas.com.rutastransporte to javafx.fxml;
    exports rutas.com.rutastransporte;
    exports rutas.com.rutastransporte.logica;
    opens rutas.com.rutastransporte.logica to javafx.fxml;
}