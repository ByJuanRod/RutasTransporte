package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.servicios.InformeParada;
import rutas.com.rutastransporte.utilidades.Confiabilidad;

import java.util.Objects;

public class InformeParadaController {
    private Stage stage;

    private Parada parada;

    @FXML
    private ImageView imgTransporte, imgConfiabilidad;

    @FXML
    private Label lblParada, lblCobertura, lblRutas, lblEficiencia, lblCosto, lblDistancia, lblTiempo, lblTipo, lblConfiabilidad;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParada(Parada parada) {
        this.parada = parada;
    }

    public void btnCerrarClick(){
        stage.close();
    }

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar los datos del informe.
        Retorno:  -
     */
    public void cargarDatos(){
        InformeParada ip = new InformeParada(parada);
        lblParada.setText(parada.getNombreParada() + " (" + parada.getUbicacion() + ")");
        lblCobertura.setText(ip.porcentajeCobertura() + "%");
        lblRutas.setText(ip.recuentoRuta() + "");
        lblEficiencia.setText(Math.round(ip.getEficienciaPromedio()) + "%");
        lblCosto.setText(Math.round(ip.getCostoPromedio()) + " (DOP)");
        lblDistancia.setText(Ruta.getDistanciaFormatado(ip.getDistanciaPromedio()));
        lblTiempo.setText(Ruta.getTiempoFormatado(ip.getTiempoPromedio()));
        lblTipo.setText(parada.getTipo().getFormateado());
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + parada.getTipo().getImagen())));
        imgTransporte.setImage(img);
        getConfiabilidad(ip);
    }

    /*
        Nombre: getConfiabilidad
        Argumentos:
            (InformeParada) ip: Representa objeto del informe de la parada.
        Objetivo: Aplicar los efectos esteticos respectivos a la confiabilidad
        Retorno: -
     */
    public void getConfiabilidad(InformeParada ip){
        int indConf = (int) ip.getIndiceConfiabilidad();
        lblConfiabilidad.setText(indConf + "%");
        Confiabilidad conf = Confiabilidad.getConfiabilidad(indConf);
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + conf.getImagen())));
        imgConfiabilidad.setImage(img);
    }

}
