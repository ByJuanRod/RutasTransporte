package rutas.com.rutastransporte.Utilidades.Alertas;

import javafx.scene.control.Alert;

/*
    Nombre: Alerta
    Tipo: Interface
    Objetivo: Segregar la logica de las alertas
 */
public interface Alerta {
    Alert crearAlerta(String mensaje, String cabecera);

}
