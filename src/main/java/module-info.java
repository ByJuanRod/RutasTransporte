module rutas.com.rutastransporte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;

    opens rutas.com.rutastransporte to javafx.fxml;
    exports rutas.com.rutastransporte;
    exports rutas.com.rutastransporte.modelos;
    opens rutas.com.rutastransporte.modelos to javafx.fxml;
    exports rutas.com.rutastransporte.servicios;
    opens rutas.com.rutastransporte.servicios to javafx.fxml;
    exports rutas.com.rutastransporte.utilidades;
    opens rutas.com.rutastransporte.utilidades to javafx.fxml;
    exports rutas.com.rutastransporte.controladores;
    opens rutas.com.rutastransporte.controladores to javafx.fxml;
    exports rutas.com.rutastransporte.utilidades.alertas;
    opens rutas.com.rutastransporte.utilidades.alertas to javafx.fxml;
    exports rutas.com.rutastransporte.repositorio;
    opens rutas.com.rutastransporte.repositorio to javafx.fxml;
}