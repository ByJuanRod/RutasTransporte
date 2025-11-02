package rutas.com.rutastransporte;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Builder;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;

import java.awt.*;

/*
    Nombre: StageBuilder
    Tipo: Clase -> Implementa a Builder<Stage>
    Objetivo: Permitir crear las pantallas de la aplicación de una forma mas sencilla.
 */
public class StageBuilder implements Builder<Stage> {
    private final Stage stage;
    AlertFactory alertFactory = new AlertFactory();

    public StageBuilder() {
        stage = new Stage();
        stage.setResizable(false);
        stage.getIcons().add(Recursos.getIcono());
        setSize(Recursos.getDimRegistro());
    }

    /*
        Nombre: setTitulo
        Argumentos:
            (String) titulo: Representa el titulo del formulario
        Objetivo: Asignarle un titulo a la pantalla
        Retorno: -
     */
    public void setTitulo(String titulo){
        stage.setTitle(titulo);
    }

    /*
        Nombre: setSize
        Argumentos:
            (Dimension) size: Representa las propiedades de ancho y alto de la pantalla.
        Objetivo: Asignarle unas dimensiones a la pantalla
        Retorno: -
     */
    public void setSize(Dimension size){
        stage.setMinHeight(size.getHeight());
        stage.setMinWidth(size.getWidth());
    }

    /*
        Nombre: setModalidad
        Argumentos:
            (Modality) modalidad: Representa la modalidad en la que se mostrara la pantalla.
        Objetivo: Asignarle una modalidad a la pantalla a mostrar para que se ajuste a sus objetivos.
        Retorno: -
     */
    public void setModalidad(Modality modalidad){
        stage.initModality(modalidad);
    }

    /*
        Nombre: setContenido
        Argumentos:
            (String) nombreRecurso: Representa el nombre del archivo FXML se que va a cargar como contenido principal.
        Objetivo: Cargar el contenido principal de la pantalla a construir
        Retorno: (Object) Retorna el objeto controlador del contenido.
     */
    public Object setContenido(String nombreRecurso){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(nombreRecurso + ".fxml"));
        try{
            Scene contenido = new Scene(fxmlLoader.load());
            stage.setScene(contenido);
        }
        catch(Exception e){
            Alerta alt = alertFactory.obtenerAlerta(Alert.AlertType.ERROR);
            alt.crearAlerta("El contenido de este apartado no esta disponible en este momento.","Error.").show();
        }
        return fxmlLoader.getController();

    }

    /*
        Nombre: construir (Builder<Stage>)
        Argumentos: -
        Objetivo: Retornar la pantalla luego de haberle aplicado la configuración.
        Retorno: (Stage) Retorna la pantalla que se va a mostrar.
     */
    @Override
    public Stage construir(){
        return stage;
    }
}
