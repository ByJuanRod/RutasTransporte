package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Criterio;
import rutas.com.rutastransporte.modelos.RutaPosible;
import rutas.com.rutastransporte.RecursosVisuales;

import java.util.LinkedList;

public class DetallesRutaController {
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public RutaPosible ruta;

    public void setRuta(RutaPosible ruta) {
        this.ruta = ruta;
    }

    @FXML
    private Label lblIndicador;

    @FXML
    private ImageView imgIndicador;

    @FXML
    private Label lblCamino;

    @FXML
    private Label lblCosto;

    @FXML
    private Label lblDistancia;

    @FXML
    private Label lblTrasbordos;

    @FXML
    private Label lblTiempo;

    @FXML
    private ImageView imgTrasbordos;

    @FXML
    private ImageView imgRapida;

    @FXML
    private ImageView imgCorta;

    @FXML
    private ImageView imgEconomico;

    @FXML
    private ImageView imgAccidente;

    @FXML
    private ImageView imgDesvio;

    @FXML
    private ImageView imgLibre;

    @FXML
    private ImageView imgConcurrido;

    public void cargarDatos(){
        try{
            lblIndicador.setText(ruta.getCriteriosDestacados().getFirst().getNombre());
            if(ruta.isEsMejorRuta()){
                imgIndicador.setImage(new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + ruta.getCriteriosDestacados().getFirst().getImagen())));
            }
            lblCamino.setText(ruta.getCaminoTexto());
            lblCosto.setText(ruta.getCostoTotal() + " (DOP)");
            lblDistancia.setText(ruta.getDistanciaFormatado());
            lblTrasbordos.setText(String.valueOf(ruta.getCantTrasbordos()));
            lblTiempo.setText(ruta.getTiempoFormatado());
            aplicarEfectos();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void verificarCriterio(Criterio criterio, LinkedList<Criterio> criteriosDestacados, ImageView img){
        if(criteriosDestacados.contains(criterio)){
            img.setImage(RecursosVisuales.getTieneCriterio());
        }
    }

    public void aplicarEfectos(){
        for(Criterio cr: ruta.getCriteriosDestacados()){
            System.out.println(cr.getNombre());
        }
        verificarCriterio(Criterio.MAS_RAPIDA, ruta.getCriteriosDestacados(), imgRapida);
        verificarCriterio(Criterio.MAS_CORTA, ruta.getCriteriosDestacados(), imgCorta);
        verificarCriterio(Criterio.MAS_ECONOMICO, ruta.getCriteriosDestacados(), imgEconomico);
        verificarCriterio(Criterio.MENOS_TRASBORDOS, ruta.getCriteriosDestacados(), imgTrasbordos);
    }
}
