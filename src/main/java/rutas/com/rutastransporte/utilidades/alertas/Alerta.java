package rutas.com.rutastransporte.utilidades.alertas;

import javafx.scene.control.Alert;

/*
    Nombre: Alerta
    Tipo: Interface
    Objetivo: Segregar la logica de las alertas
 */
public interface Alerta {
    Alert crearAlerta(String mensaje, String cabecera);

    Alert crearAlerta(String mensaje);

}
