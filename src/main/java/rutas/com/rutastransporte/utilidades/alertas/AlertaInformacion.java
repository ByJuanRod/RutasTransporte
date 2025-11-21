package rutas.com.rutastransporte.utilidades.alertas;

import javafx.scene.control.Alert;

/*
    Nombre: AlertaInformacion
    Tipo: Clase -> Implementa a Alerta
    Objetivo: Segregar la logica de las alertas informativas.
 */
public class AlertaInformacion implements Alerta {

    @Override
    public Alert crearAlerta(String mensaje, String cabecera) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(cabecera);

        return alerta;
    }

    public Alert crearAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText("Información.");

        return alerta;
    }
}
