package rutas.com.rutastransporte.Utilidades.Alertas;

import javafx.scene.control.Alert.AlertType;

public class AlertFactory {

    public Alerta obtenerAlerta(AlertType tipoAlerta) {
        return switch (tipoAlerta){
            case INFORMATION -> new AlertaInformacion();
            case WARNING -> new AlertaAdvertencia();
            case ERROR -> new AlertaError();
            default -> null;
        };
    }
}
