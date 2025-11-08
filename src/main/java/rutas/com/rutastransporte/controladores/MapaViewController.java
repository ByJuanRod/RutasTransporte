package rutas.com.rutastransporte.controladores;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
    private SmartGraphPanel<Parada,Ruta> graphView;

    @FXML
    private VBox contenedorGeneral;

    @FXML
    private AnchorPane contenedorGrafo;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private ComboBox<Parada> cbxOrigen;

    @FXML
    private ComboBox<Parada> cbxDestino;

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

            Parada origen = cbxOrigen.getSelectionModel().getSelectedItem();
            Parada destino = cbxDestino.getSelectionModel().getSelectedItem();

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
    public void initialize() {
        scrollpane.fitToWidthProperty().set(true);
        cargarDatos();
    }

    public void cargarDatos(){
        for(Parada p : SistemaTransporte.getSistemaTransporte().getParadas()){
            cbxDestino.getItems().add(p);
            cbxOrigen.getItems().add(p);
        }

        Graph<Parada, Ruta> g = new GraphEdgeList<>();
        rellenarGrafo(g);

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel<>(g, strategy);
        graphView.setAutomaticLayout(true);
        aplicarEstilos(graphView);

        AnchorPane.setLeftAnchor(graphView, 0.0);
        AnchorPane.setTopAnchor(graphView, 0.0);
        AnchorPane.setRightAnchor(graphView, 0.0);
        AnchorPane.setBottomAnchor(graphView, 0.0);

        contenedorGrafo.getChildren().add(graphView);

        Platform.runLater(() -> {
            if (graphView.getScene() != null) {
                inicializarGrafo();
            } else {
                PauseTransition initialPause = new PauseTransition(Duration.millis(100));
                initialPause.setOnFinished(e -> {
                    if (graphView.getScene() != null) {
                        inicializarGrafo();
                    } else {
                        PauseTransition finalPause = new PauseTransition(Duration.millis(300));
                        finalPause.setOnFinished(e2 -> inicializarGrafo());
                        finalPause.play();
                    }
                });
                initialPause.play();
            }
        });
    }

    private void inicializarGrafo() {
        try {
            graphView.init();

            PauseTransition pause = new PauseTransition(Duration.millis(50));
            pause.setOnFinished(e -> {
                graphView.update();
            });
            pause.play();

        } catch (Exception e) {
            System.err.println("Error al inicializar el grafo: " + e.getMessage());
            PauseTransition retryPause = new PauseTransition(Duration.millis(200));
            retryPause.setOnFinished(ev -> {
                try {
                    graphView.init();
                    graphView.update();
                } catch (Exception ex) {
                    System.err.println("Error en reintento: " + ex.getMessage());
                }
            });
            retryPause.play();
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

    public void rellenarGrafo(Graph<Parada,Ruta> grafo){
        for(Parada parada : SistemaTransporte.getSistemaTransporte().getParadas()){
            grafo.insertVertex(parada);
        }

        for(Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()){
            grafo.insertEdge(ruta.getOrigen(),ruta.getDestino(),ruta);
        }
    }

    public void aplicarEstilos(SmartGraphPanel<Parada,Ruta> panel){
        for(Vertex<Parada> vertice : panel.getModel().vertices()){
            panel.setVertexDoubleClickAction(graphVertex -> {
                panel.getStylableVertex(graphVertex.getUnderlyingVertex().element()).addStyleClass("seleccionado");
                if(cbxOrigen.getSelectionModel().getSelectedIndex() == -1){
                    cbxOrigen.getSelectionModel().select(graphVertex.getUnderlyingVertex().element());
                }
                else{
                    cbxDestino.getSelectionModel().select(graphVertex.getUnderlyingVertex().element());
                }
            });

            panel.getStylableVertex(vertice).addStyleClass(vertice.element().getTipo().getClase());
        }
    }

    public void estilizarRuta(){

    }
}
