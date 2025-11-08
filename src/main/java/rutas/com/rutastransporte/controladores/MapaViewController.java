package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import rutas.com.rutastransporte.modelos.*;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;
import rutas.com.rutastransporte.servicios.Calculador;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;
import rutas.com.rutastransporte.utilidades.alertas.Alerta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class MapaViewController {
    private final AlertFactory alertFactory = new AlertFactory();

    @FXML
    private VBox contenedorGeneral;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private ComboBox<String> cbxOrigen;

    @FXML
    private ComboBox<String> cbxDestino;

    public void buscarClick() {
        if(cbxDestino.getSelectionModel().getSelectedItem().equals(cbxOrigen.getSelectionModel().getSelectedItem())){
            Alerta alert = alertFactory.obtenerAlerta(Alert.AlertType.WARNING);
            alert.crearAlerta("El origen y el destino no pueden ser el mismo seleccione un punto diferente.","Advertencia.").show();
        }
        else{
            Calculador calc = new Calculador();
            calc.setGrafo(SistemaTransporte.getSistemaTransporte().getGrafo());

            contenedorGeneral.getChildren().clear();
            Stack<RutaPosible> posiblesRutas = new Stack<>();

            Parada origen = buscarParada(cbxOrigen.getSelectionModel().getSelectedItem());
            Parada destino = buscarParada(cbxDestino.getSelectionModel().getSelectedItem());

            for(Criterio criterio : Criterio.values()){
                if(!criterio.equals(Criterio.MEJOR_RUTA)){
                    posiblesRutas.add(calc.dijkstra(origen,destino,criterio));
                }
            }

            RutaPosible mejorRuta = obtenerMejorRuta(posiblesRutas);

            ArrayList<RutaPosible> rutasUnicas = obtenerRutasUnicasExcluyendoMejor(posiblesRutas, mejorRuta);

            if (mejorRuta != null) {
                mejorRuta.agregarFirst(Criterio.MEJOR_RUTA);
                mejorRuta.setEsMejorRuta(true);
                crearPanel(mejorRuta);
            }

            for(RutaPosible rutaUnica : rutasUnicas){
                if(rutaUnica != null){
                    crearPanel(rutaUnica);
                }
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
        controller.setRutaPosible(ruta);

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

    public RutaPosible obtenerMejorRuta(Stack<RutaPosible> posiblesRutas) {
        if (posiblesRutas == null || posiblesRutas.isEmpty()) {
            return null;
        }

        ArrayList<RutaPosible> rutasUnicas = new ArrayList<>();

        for (RutaPosible rutaActual : posiblesRutas) {
            if (rutaActual == null) continue;

            boolean esDuplicada = false;
            for (RutaPosible rutaUnica : rutasUnicas) {
                if (rutaActual.sonIguales(rutaUnica.getCamino())) {
                    esDuplicada = true;
                    break;
                }
            }

            if (!esDuplicada) {
                rutasUnicas.add(rutaActual);
            }
        }

        if (rutasUnicas.size() == 1) {
            RutaPosible mejorRuta = new RutaPosible();
            mejorRuta.clonar(rutasUnicas.getFirst());

            for (RutaPosible posible : posiblesRutas) {
                if (posible != null && posible.sonIguales(rutasUnicas.getFirst().getCamino())) {
                    if (!posible.getCriteriosDestacados().isEmpty()) {
                        mejorRuta.agregarCriterioDestacado(posible.getCriteriosDestacados().getFirst());
                    }
                }
            }
            return mejorRuta;
        }

        // 3. Para múltiples rutas, encontrar la que aparece en más criterios
        RutaPosible mejorRutaBase = null;
        int maxCriteriosCoincidentes = 0;

        for (RutaPosible rutaUnica : rutasUnicas) {
            int criteriosCoincidentes = cantCriteriosParaMismoCamino(posiblesRutas, rutaUnica.getCamino());

            if (criteriosCoincidentes > maxCriteriosCoincidentes) {
                maxCriteriosCoincidentes = criteriosCoincidentes;
                mejorRutaBase = rutaUnica;
            }
        }

        if (mejorRutaBase != null) {
            RutaPosible mejorRuta = new RutaPosible();
            mejorRuta.clonar(mejorRutaBase);
            for (RutaPosible posible : posiblesRutas) {
                if (posible != null && posible.sonIguales(mejorRutaBase.getCamino())) {
                    if (!posible.getCriteriosDestacados().isEmpty()) {
                        mejorRuta.agregarCriterioDestacado(posible.getCriteriosDestacados().getFirst());
                    }
                }
            }

            return mejorRuta;
        }

        return null;
    }

    public int cantCriteriosParaMismoCamino(Stack<RutaPosible> posiblesRutas, LinkedList<Ruta> camino) {
        int cantidadCriterios = 0;
        for (RutaPosible posible : posiblesRutas) {
            if (posible != null && posible.sonIguales(camino)) {
                cantidadCriterios++;
            }
        }
        return cantidadCriterios;
    }

    public ArrayList<RutaPosible> obtenerRutasUnicasExcluyendoMejor(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta) {
        ArrayList<RutaPosible> rutasUnicas = new ArrayList<>();

        if (posiblesRutas == null || posiblesRutas.isEmpty()) {
            return rutasUnicas;
        }

        for (RutaPosible rutaActual : posiblesRutas) {
            if (rutaActual == null) continue;

            if (mejorRuta != null && mejorRuta.sonIguales(rutaActual.getCamino())) {
                continue;
            }

            boolean esDuplicada = false;
            for (RutaPosible rutaUnica : rutasUnicas) {
                if (rutaActual.sonIguales(rutaUnica.getCamino())) {
                    esDuplicada = true;
                    break;
                }
            }

            if (!esDuplicada) {
                rutasUnicas.add(rutaActual);
            }
        }

        return rutasUnicas;
    }
}
