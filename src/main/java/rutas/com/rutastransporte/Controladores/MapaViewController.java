package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import rutas.com.rutastransporte.Modelos.Criterio;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.RutaPosible;
import rutas.com.rutastransporte.Repositorio.SistemaTransporte;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;

public class MapaViewController {
    private AlertFactory alertFactory = new AlertFactory();

    @FXML
    private VBox contenedorGeneral;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private ComboBox<String> cbxOrigen;

    @FXML
    private ComboBox<String> cbxDestino;

    public void buscarClick(ActionEvent e) {
        contenedorGeneral.getChildren().clear();

    }

    @FXML
    public void initialize(){
        cargarDatos();
    }

    public void cargarDatos(){
        for(Parada p : SistemaTransporte.getSistemaTransporte().getParadas()){
            cbxDestino.getItems().add(p.getNombreParada());
            cbxOrigen.getItems().add(p.getNombreParada());
        }
    }

    public void crearPanel(RutaPosible ruta){
        scrollpane.fitToWidthProperty().set(true);
        ResultadoRutaController controller = new ResultadoRutaController();
        controller.setRutaPosible(ruta);

        AnchorPane contenido = controller.crearInterfaz();
        contenedorGeneral.getChildren().add(contenido);
    }
}
