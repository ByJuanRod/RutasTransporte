package rutas.com.rutastransporte.utilidades.alertas;

import javafx.scene.control.Alert;

/*
    Nombre: AlertaError
    Tipo: Clase -> Implementa a Alerta
    Objetivo: Segregar la logica de las alertas que informan sobre errores.
 */
public class AlertaError implements Alerta {
    @Override
    public Alert crearAlerta(String mensaje, String cabecera) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(cabecera);

        return alerta;
    }
}
