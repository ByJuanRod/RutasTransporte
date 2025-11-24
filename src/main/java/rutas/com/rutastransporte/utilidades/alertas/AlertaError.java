package rutas.com.rutastransporte.utilidades.alertas;

import javafx.scene.control.Alert;

/*
    Nombre: AlertaError
    Tipo: Clase -> Implementa a Alerta
    Objetivo: Segregar la logica de las alertas que informan sobre errores.
 */
public class AlertaError implements Alerta {
    /*
    Nombre: crearAlerta
    Argumentos:
        (String) mensaje: Representa el mensaje de la alerta.
        (String) cabecera: Representa la cabecera de la alerta.
    Objetivo: Crear una alerta con el mensaje y cabecera establecida.
    Retorno: (Alert) Retorna un objeto Alert de JavaFX con la alerta preparada a mostrar.
 */
    @Override
    public Alert crearAlerta(String mensaje, String cabecera) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(cabecera);

        return alerta;
    }

    /*
    Nombre: crearAlerta
    Argumentos:
        (String) mensaje: Representa el mensaje de la alerta.
    Objetivo: Crear una alerta con el mensaje establecido y la cabecera por defecto.
    Retorno: (Alert) Retorna un objeto Alert de JavaFX con la alerta preparada a mostrar.
*/
    public Alert crearAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setResizable(false);
        alerta.setContentText(mensaje);
        alerta.setHeaderText("Error.");

        return alerta;
    }
}
