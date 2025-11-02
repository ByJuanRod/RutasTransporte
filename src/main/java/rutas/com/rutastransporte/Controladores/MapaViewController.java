package rutas.com.rutastransporte.Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import rutas.com.rutastransporte.Modelos.Criterio;
import rutas.com.rutastransporte.Modelos.RutaPosible;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;

public class MapaViewController {
    private AlertFactory alertFactory = new AlertFactory();

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox pnlContenedor;

    @FXML
    public void initialize(){
        cargarDatos();
    }

    public void cargarDatos(){
        RutaPosible rutaPosible = new RutaPosible();
        rutaPosible.setCriterio(Criterio.MAS_RAPIDA);
        rutaPosible.setCantTrasbordos(1);
        rutaPosible.setCostoTotal(100);
        rutaPosible.setTiempoTotal(5000);
        rutaPosible.setDistanciaTotal(400);

        crearPanel(rutaPosible);

    }

    public void crearPanel(RutaPosible ruta){
        try{
            scrollpane.setFitToWidth(true);

            ResultadoRutaController  resultadoRutaController = new ResultadoRutaController();

            resultadoRutaController.setRutaPosible(ruta);
            pnlContenedor.getChildren().add(resultadoRutaController.crearInterfaz());
        }
        catch (Exception e){
            Alerta alt = alertFactory.obtenerAlerta(Alert.AlertType.ERROR);
            alt.crearAlerta("No se ha podido cargar el recurso.","Error al cargar.").show();
        }
    }
}
