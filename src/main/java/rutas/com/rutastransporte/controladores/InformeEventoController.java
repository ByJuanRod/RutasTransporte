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
    private Label lblNombre, lblCosto, lblTiempo, lblDistancia, lblRuta, lblDescTiempo, lblDescDistancia, lblDescCosto;

    @FXML
    private ImageView imgEvento;

    public void btnCerrarClick(){
        stage.close();
    }

    public void setEventoSeleccionado(EventoRuta eventoSeleccionado) {
        evento = eventoSeleccionado;
    }

    public void cargarDatos(){
        lblNombre.setText(evento.getTipoEvento().getNombre());
        lblCosto.setText(evento.getRuta().getCostoDiff() + " (DOP)");
        lblTiempo.setText(Ruta.getTiempoFormatado(evento.getRuta().getTiempoDiff()));
        lblDistancia.setText(Ruta.getDistanciaFormatado(evento.getRuta().getDistanciaDiff()));
        lblRuta.setText(evento.getRuta().getNombre());
        cargarDescripciones();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + evento.getTipoEvento().getImagen())));
        imgEvento.setImage(img);
    }

    private void cargarDescripciones(){
        switch (evento.getTipoEvento()){
            case ACCIDENTE:
                lblDescTiempo.setText("50% más de Tiempo.");
                lblDescDistancia.setText("100% más de Distancia.");
                lblDescCosto.setText("Mismo Costo.");
                break;

            case DESVIO:
                lblDescTiempo.setText("30% más de Tiempo.");
                lblDescDistancia.setText("50% más de Distancia.");
                lblDescCosto.setText("10% más de Costo.");
                break;

            case CAMINO_LIBRE:
                lblDescTiempo.setText("30% menos de Tiempo.");
                lblDescDistancia.setText("10% menos de Distancia.");
                lblDescCosto.setText("Mismo Costo.");
                break;

            case ZONA_CONCURRIDA:
                lblDescTiempo.setText("40% más de Tiempo.");
                lblDescDistancia.setText("10% más de Distancia");
                lblDescCosto.setText("20% más de Costo.");
        }
    }
}
