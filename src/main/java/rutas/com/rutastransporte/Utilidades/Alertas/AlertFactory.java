package rutas.com.rutastransporte.Utilidades.Alertas;

import javafx.scene.control.Alert.AlertType;

/*
    Nombre: AlertFactory
    Tipo: Clase
    Objetivo: Fabricar las alertas dependiendo de que tipo se necesiten dentro del programa
 */
public class AlertFactory {

    /*
        Nombre: ObtenerAlerta
        Argumentos:
            (AlertType) tipoAlerta: Representa el tipo de alerta que se debe crear
        Objetivo: Retornar la alerta que se necesita crear
        Retorno: (Alerta) Representa la alerta solicitada.
     */
    public Alerta obtenerAlerta(AlertType tipoAlerta) {
        return switch (tipoAlerta){
            case INFORMATION -> new AlertaInformacion();
            case WARNING -> new AlertaAdvertencia();
            case ERROR -> new AlertaError();
            default -> null;
        };
    }
}
