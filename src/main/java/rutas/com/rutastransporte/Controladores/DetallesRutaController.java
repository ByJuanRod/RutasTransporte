package rutas.com.rutastransporte.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.MejorRuta;
import rutas.com.rutastransporte.Modelos.RutaPosible;

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
    private ImageView imgTiempo;

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
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


}
