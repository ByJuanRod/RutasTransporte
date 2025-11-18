package rutas.com.rutastransporte.controladores;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
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
import rutas.com.rutastransporte.servicios.ServicioMapa;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;
import rutas.com.rutastransporte.utilidades.alertas.Alerta;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MapaViewController {
    private final ServicioMapa sm = new  ServicioMapa();
    private final AlertFactory alertFactory = new AlertFactory();
    private SmartGraphPanel<Parada,Ruta> graphView;

    private RutaPosible estilizada = null;

    private Parada origen = null;

    private Parada destino = null;

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
        if(estilizada != null) {
            aplicarPorDefecto();
        }

        if(cbxDestino.getSelectionModel().getSelectedItem().equals(cbxOrigen.getSelectionModel().getSelectedItem())){
            Alerta alert = alertFactory.obtenerAlerta(Alert.AlertType.WARNING);
            alert.crearAlerta("El origen y el destino no pueden ser el mismo seleccione un punto diferente.","Advertencia.").show();
        }
        else{
            Calculador calc = new Calculador();
            calc.setGrafo(SistemaTransporte.getSistemaTransporte().getGrafo());

            Stack<RutaPosible> posiblesRutas = new Stack<>();

            Parada origen = cbxOrigen.getSelectionModel().getSelectedItem();
            Parada destino = cbxDestino.getSelectionModel().getSelectedItem();

            for(Criterio criterio : Criterio.values()){
                if(!criterio.equals(Criterio.MEJOR_RUTA)){
                    RutaPosible rt = calc.dijkstra(origen,destino,criterio);

                    if(rt != null){
                        contenedorGeneral.getChildren().clear();
                        posiblesRutas.add(rt);
                    }
                    else{
                        alertFactory.obtenerAlerta(Alert.AlertType.WARNING).crearAlerta("Todavía no existe un camino para llegar desde " + origen.getNombreParada() + " a " + destino.getNombreParada() + ".","Advertencia de calculo.").show();
                        return;
                    }
                }
            }

            RutaPosible mejorRuta = sm.obtenerMejorRuta(posiblesRutas);

            ArrayList<RutaPosible> rutasUnicas = sm.obtenerRutasUnicasExcluyendoMejor(posiblesRutas, mejorRuta);

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

            if(mejorRuta != null){
                estilizarRuta(mejorRuta);
                estilizada = mejorRuta;
            }
            else{
                estilizarRuta(rutasUnicas.getFirst());
                estilizada = rutasUnicas.getFirst();
            }

            this.origen =  cbxOrigen.getSelectionModel().getSelectedItem();
            this.destino = cbxDestino.getSelectionModel().getSelectedItem();

        }
    }
    @FXML
    public void initialize() {
        scrollpane.fitToWidthProperty().set(true);
        cargarDatos();
        cbxOrigen.setEditable(false);
        cbxDestino.setEditable(false);
    }

    public void cargarDatos(){
        for(Parada p : SistemaTransporte.getSistemaTransporte().getParadas()){
            cbxDestino.getItems().add(p);
            cbxOrigen.getItems().add(p);
        }

        Digraph<Parada, Ruta> g = new DigraphEdgeList<>();
        for(Parada p : SistemaTransporte.getSistemaTransporte().getParadas()) {
            g.insertVertex(p);
        }

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel<>(g, strategy);

        double tamanoLienzo = Math.max(1200, g.numVertices() * 50);
        graphView.setPrefSize(tamanoLienzo, tamanoLienzo);

        graphView.setAutomaticLayout(true);
        aplicarEstilos(graphView);

        ContentZoomScrollPane zoomPane = new ContentZoomScrollPane(graphView);

        AnchorPane.setLeftAnchor(zoomPane, 0.0);
        AnchorPane.setTopAnchor(zoomPane, 0.0);
        AnchorPane.setRightAnchor(zoomPane, 0.0);
        AnchorPane.setBottomAnchor(zoomPane, 0.0);

        contenedorGrafo.getChildren().add(zoomPane);

        Platform.runLater(() -> {
            if (graphView.getScene() != null) {
                inicializarGrafo();
            } else {
                PauseTransition initialPause = new PauseTransition(Duration.millis(100));
                initialPause.setOnFinished(e -> {
                    if (graphView.getScene() != null) {
                        inicializarGrafo();
                    } else {
                        PauseTransition finalPause = new PauseTransition(Duration.millis(100));
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
            Transition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(e -> graphView.update());
            pause.play();
        } catch (Exception e) {
            System.err.println("Error al inicializar el grafo: " + e.getMessage());
        }
    }

    public void crearPanel(RutaPosible ruta){
        ResultadoRutaController controller = new ResultadoRutaController();
        controller.setRutaPosible(ruta);

        AnchorPane contenido = controller.crearInterfaz(ruta);
        contenedorGeneral.getChildren().add(contenido);
    }

    public void aplicarEstilos(SmartGraphPanel<Parada,Ruta> panel){
        for(Vertex<Parada> vertice : panel.getModel().vertices()){
            panel.getStylableVertex(vertice).addStyleClass(vertice.element().getTipo().getClase());
        }

        configurarEventosGrafo(panel);
    }

    private void configurarEventosGrafo(SmartGraphPanel<Parada,Ruta> panel) {
        panel.setVertexDoubleClickAction(graphVertex -> evaluarEventos(panel, graphVertex.getUnderlyingVertex().element()));
    }

    private void evaluarEventos(SmartGraphPanel<Parada,Ruta> panel, Parada elemento){
        if (estilizada != null) {
            aplicarPorDefecto();
        }

        if (origen == null) {
            panel.getStylableVertex(elemento).addStyleClass("seleccionado");
            cbxOrigen.getSelectionModel().select(elemento);
            origen = elemento;

            construirArbolVisual(origen);
        }
        else if (destino == null && !origen.equals(elemento)) {
            panel.getStylableVertex(elemento).addStyleClass("seleccionado");
            cbxDestino.getSelectionModel().select(elemento);
            destino = elemento;

        }
        else if (origen.equals(elemento)) {
            panel.getStylableVertex(elemento).removeStyleClass("seleccionado");
            cbxOrigen.getSelectionModel().clearSelection();
            origen = null;

            limpiarAristasVisuales();
        }
        else if (destino != null && destino.equals(elemento)) {
            panel.getStylableVertex(elemento).removeStyleClass("seleccionado");
            cbxDestino.getSelectionModel().clearSelection();
            destino = null;
        }
    }

    private void construirArbolVisual(Parada raiz) {
        limpiarAristasVisuales();

        Calculador calc = new Calculador();
        calc.setGrafo(SistemaTransporte.getSistemaTransporte().getGrafo());

        List<Ruta> arbol = calc.calcularArbolDijkstra(raiz);

        for (Ruta r : arbol) {
            try {
                graphView.getModel().insertEdge(r.getOrigen(), r.getDestino(), r);
            } catch (Exception ignored) { }
        }

        graphView.update();
    }

    private void limpiarAristasVisuales() {
        if (graphView != null && graphView.getModel() != null) {
            List<Edge<Ruta, Parada>> aristas = new ArrayList<>(graphView.getModel().edges());

            for (Edge<Ruta, Parada> arista : aristas) {
                graphView.getModel().removeEdge(arista);
            }
            graphView.update();
        }
    }

    public void estilizarRuta(RutaPosible ruta){
        for(Ruta rt : ruta.getCamino()){
            SmartStylableNode visualRoute = graphView.getStylableEdge(rt);

            if (visualRoute != null) {
                visualRoute.addStyleClass("camino");
            } else {
                System.out.println("La ruta visual aún no está lista: " + rt.getNombre());
            }
        }
    }

    public void aplicarPorDefecto(){
        if (estilizada != null) {
            for (Ruta rt : estilizada.getCamino()) {
                graphView.getStylableEdge(rt).removeStyleClass("camino");
            }
            estilizada = null;
        }

        if (origen != null) {
            graphView.getStylableVertex(origen).removeStyleClass("seleccionado");
            cbxOrigen.getSelectionModel().clearSelection();
            origen = null;
        }

        if (destino != null) {
            graphView.getStylableVertex(destino).removeStyleClass("seleccionado");
            cbxDestino.getSelectionModel().clearSelection();
            destino = null;
        }
    }
}