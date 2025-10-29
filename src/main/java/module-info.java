module rutas.com.rutastransporte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires rutas.com.rutastransporte;


    opens rutas.com.rutastransporte to javafx.fxml;
    exports rutas.com.rutastransporte;
    exports rutas.com.rutastransporte.Modelos;
    opens rutas.com.rutastransporte.Modelos to javafx.fxml;
    exports rutas.com.rutastransporte.Servicios;
    opens rutas.com.rutastransporte.Servicios to javafx.fxml;
    exports rutas.com.rutastransporte.Utilidades;
    opens rutas.com.rutastransporte.Utilidades to javafx.fxml;
    exports rutas.com.rutastransporte.Controladores;
    opens rutas.com.rutastransporte.Controladores to javafx.fxml;
    exports rutas.com.rutastransporte.Utilidades.Alertas;
    opens rutas.com.rutastransporte.Utilidades.Alertas to javafx.fxml;
}