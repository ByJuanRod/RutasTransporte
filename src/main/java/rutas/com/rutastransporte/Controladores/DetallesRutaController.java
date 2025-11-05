package rutas.com.rutastransporte.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Criterio;
import rutas.com.rutastransporte.Modelos.MejorRuta;
import rutas.com.rutastransporte.Modelos.RutaPosible;
import rutas.com.rutastransporte.RecursosVisuales;

import java.util.Queue;

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
            lblIndicador.setText(ruta.getCriterio().getNombre());
            if(ruta instanceof MejorRuta){
                imgIndicador.setImage(new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + ruta.getCriterio().getImagen())));
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

    public void verificarCriterio(Criterio criterio, Queue<Criterio> criteriosDestacados, ImageView img){
        if(criteriosDestacados.contains(criterio)){
            img.setImage(RecursosVisuales.getTieneCriterio());
        }
    }

    public void aplicarEfectos(){
        if(ruta instanceof MejorRuta mejorRuta){
            verificarCriterio(Criterio.MAS_RAPIDA, mejorRuta.getCriteriosDestacados(), imgRapida);
            verificarCriterio(Criterio.MAS_CORTA, mejorRuta.getCriteriosDestacados(), imgCorta);
            verificarCriterio(Criterio.MAS_ECONOMICO,mejorRuta.getCriteriosDestacados(), imgEconomico);
            verificarCriterio(Criterio.MENOS_TRASBORDOS,mejorRuta.getCriteriosDestacados(), imgTrasbordos);
        }
        else{
            if(ruta.getCriterio().equals(Criterio.MAS_RAPIDA)){
                imgRapida.setImage(RecursosVisuales.getTieneCriterio());
            }
            else if(ruta.getCriterio().equals(Criterio.MAS_CORTA)){
                imgCorta.setImage(RecursosVisuales.getTieneCriterio());
            }
            else if(ruta.getCriterio().equals(Criterio.MAS_ECONOMICO)){
                imgEconomico.setImage(RecursosVisuales.getTieneCriterio());
            }
            else{
                imgTrasbordos.setImage(RecursosVisuales.getTieneCriterio());
            }
        }
    }
}
