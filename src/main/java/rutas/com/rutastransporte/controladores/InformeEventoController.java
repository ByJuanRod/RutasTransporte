package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;

import java.util.Objects;

public class InformeEventoController {
    private Stage stage;

    private EventoRuta evento;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label lblNombre, lblCosto, lblTiempo, lblDistancia, lblRuta, lblDescTiempo, lblDescDistancia, lblDescCosto, lblDuracion;

    @FXML
    private ImageView imgEvento;

    public void btnCerrarClick(){
        stage.close();
    }

    public void setEventoSeleccionado(EventoRuta eventoSeleccionado) {
        evento = eventoSeleccionado;
    }

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar los datos del informe del evento.
        Retorno: -
     */
    public void cargarDatos(){
        lblNombre.setText(evento.getTipoEvento().getNombre());
        lblCosto.setText(evento.getRuta().getCostoDiff() + " (DOP)");
        lblTiempo.setText(Ruta.getTiempoFormatado(evento.getRuta().getTiempoDiff()));
        lblDistancia.setText(Ruta.getDistanciaFormatado(evento.getRuta().getDistanciaDiff()));
        lblRuta.setText(evento.getRuta().getNombre());
        lblDuracion.setText(String.valueOf(evento.getTiempoRestanteMinutos()));
        cargarDescripciones();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + evento.getTipoEvento().getImagen())));
        imgEvento.setImage(img);
    }

    /*
        Nombre: cargarDescripciones
        Argumentos: -
        Objetivo: Representa las descripciones a cargar.
        Retorno: -
     */
    private void cargarDescripciones(){
        switch (evento.getTipoEvento()){
            case ACCIDENTE:
                cargarMensajes("50% más de Tiempo.","100% más de Distancia.","Mismo Costo.");
                break;

            case DESVIO:
                cargarMensajes("30% más de Tiempo.","50% más de Distancia.","10% más de Costo.");
                break;

            case CAMINO_LIBRE:
                cargarMensajes("30% menos de Tiempo.","10% menos de Distancia.","Mismo Costo.");
                break;

            case ZONA_CONCURRIDA:
                cargarMensajes("40% más de Tiempo.","10% más de Distancia","20% más de Costo.");
                break;
        }
    }

    /*
        Nombre: cargarMensajes
        Argumentos:
            (String) tiempo: Representa el mensaje de tiempo.
            (String) distancia: Representa el mensaje de distancia.
            (String) costo: Representa el mensaje de costo.
        Objetivo: Cargar los mensajes descriptivos de los eventos.
        Retorno: -
     */
    void cargarMensajes(String tiempo, String distancia, String costo){
        lblDescTiempo.setText(tiempo);
        lblDescDistancia.setText(distancia);
        lblDescCosto.setText(costo);
    }
}
