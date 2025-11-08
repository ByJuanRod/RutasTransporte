package rutas.com.rutastransporte.utilidades.alertas;

import javafx.scene.control.Alert;

/*
    Nombre: AlertaAdvertencia
    Tipo: Clase -> Implementa a Alerta
    Objetivo: Segregar la logica de las alertas de advertencia.
 */
public class AlertaAdvertencia implements Alerta{
    @Override
    public Alert crearAlerta(String mensaje, String cabecera) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(cabecera);

        return alerta;
    }
}
