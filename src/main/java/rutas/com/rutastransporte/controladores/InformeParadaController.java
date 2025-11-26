package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.servicios.ServicioParadas;

import java.util.Objects;


public class InformeParadaController {
    private Stage stage;

    private Parada parada;

    @FXML
    private ImageView imgTransporte;

    @FXML
    private Label lblParada, lblCobertura, lblRutas, lblEficiencia, lblCosto, lblDistancia, lblTiempo, lblTipo;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParada(Parada parada) {
        this.parada = parada;
    }

    public void btnCerrarClick(){
        stage.close();
    }

    public void cargarDatos(){
        ServicioParadas sp = new ServicioParadas(parada);
        lblParada.setText(parada.getNombreParada() + " (" + parada.getTipo().getTipo() + ")");
        lblCobertura.setText(sp.porcentajeCobertura() + "%");
        lblRutas.setText(sp.recuentoRuta() + "");
        lblEficiencia.setText(Math.round(sp.getEficienciaPromedio()) + "%");
        lblCosto.setText(sp.getCostoPromedio() + "(DOP)");
        lblDistancia.setText(Ruta.getDistanciaFormatado(sp.getDistanciaPromedio()));
        lblTiempo.setText(Ruta.getTiempoFormatado(sp.getTiempoPromedio()));
        lblTipo.setText(parada.getTipo().getFormateado());

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + parada.getTipo().getImagen())));
        imgTransporte.setImage(img);
    }


}
