package rutas.com.rutastransporte.Utilidades.Alertas;

import javafx.scene.control.Alert;

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
