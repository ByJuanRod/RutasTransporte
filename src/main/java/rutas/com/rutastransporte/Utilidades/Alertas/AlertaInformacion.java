package rutas.com.rutastransporte.Utilidades.Alertas;

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
        alerta.setTitle("Informacion");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(cabecera);

        return alerta;
    }
}
