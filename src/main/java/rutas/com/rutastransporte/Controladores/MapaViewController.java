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
import rutas.com.rutastransporte.Servicios.Calculador;
import rutas.com.rutastransporte.Servicios.GrafoTransporte;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;

import java.util.ArrayList;

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
        if(cbxDestino.getSelectionModel().getSelectedItem().equals(cbxOrigen.getSelectionModel().getSelectedItem())){
            Alerta alert = alertFactory.obtenerAlerta(Alert.AlertType.WARNING);
            alert.crearAlerta("El origen y el destino no pueden ser el mismo seleccione un punto diferente.","Advertencia.").show();
        }
        else{
            Calculador calc = new Calculador();
            calc.setGrafo(SistemaTransporte.getSistemaTransporte().getGrafo());

            contenedorGeneral.getChildren().clear();
            ArrayList<RutaPosible> posiblesRutas = new ArrayList<>();

            Parada origen = buscarParada(cbxOrigen.getSelectionModel().getSelectedItem());
            Parada destino = buscarParada(cbxDestino.getSelectionModel().getSelectedItem());

            for(Criterio criterio : Criterio.values()){
                if(!criterio.equals(Criterio.MEJOR_RUTA)){

                    Thread hilito = new Thread(new Runnable(){
                        @Override
                        public void run(){
                            posiblesRutas.add(calc.dijkstra(origen,destino,criterio));
                        }
                    });

                    hilito.run();
                }
            }

            posiblesRutas.get(0).setCriterio(Criterio.MEJOR_RUTA);

            for(RutaPosible posible : posiblesRutas){
                crearPanel(posible);
            }
        }
    }

    @FXML
    public void initialize(){
        scrollpane.fitToWidthProperty().set(true);
        cargarDatos();
    }

    public void cargarDatos(){
        for(Parada p : SistemaTransporte.getSistemaTransporte().getParadas()){
            cbxDestino.getItems().add(p.getNombreParada());
            cbxOrigen.getItems().add(p.getNombreParada());
        }
    }

    public void crearPanel(RutaPosible ruta){
        ResultadoRutaController controller = new ResultadoRutaController();

        AnchorPane contenido = controller.crearInterfaz(ruta);
        contenedorGeneral.getChildren().add(contenido);
    }

    public Parada buscarParada(String nombre){
        for(Parada p : SistemaTransporte.getSistemaTransporte().getParadas()){
            if(p.getNombreParada().equals(nombre)){
                return p;
            }
        }

        return null;
    }
}
