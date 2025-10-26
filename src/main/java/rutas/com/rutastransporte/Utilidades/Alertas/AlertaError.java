package rutas.com.rutastransporte.Utilidades.Alertas;

import javafx.scene.control.Alert;

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
