package rutas.com.rutastransporte.controladores;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    private ContentZoomScrollPane zoomPane;

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
    private Label lblOrigen;

    @FXML
    private Label lblDestino;

    public void buscarClick() {
        if(estilizada != null) {
            aplicarPorDefecto();
        }

        if(origen == null || destino == null){
            Alerta alert = alertFactory.obtenerAlerta(Alert.AlertType.WARNING);
            alert.crearAlerta("Debe seleccionar tanto origen como destino en el mapa.").show();
            return;
        }

        if(destino.equals(origen)){
            Alerta alert = alertFactory.obtenerAlerta(Alert.AlertType.WARNING);
            alert.crearAlerta("El origen y el destino no pueden ser el mismo seleccione un punto diferente.").show();
            return;
        }

        cargarResultados();
    }


    @FXML
    public void initialize() {
        scrollpane.fitToWidthProperty().set(true);
        cargarDatos();

        lblOrigen.setText("Seleccione origen en el mapa");
        lblDestino.setText("Seleccione destino en el mapa");
    }

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar los datos para que el apartado del mapa funcione correctamente.
        Retorno: -
     */
    public void cargarDatos(){
        cargarElementos();

        try{
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
        catch (Exception ignored){}
    }

    /*
        Nombre: cargarElementos
        Argumentos: -
        Objetivo: Cargar los elementos del grafo con los datos existentes.
        Retorno: -
     */
    public void cargarElementos(){
        sm.crearSimulacion();

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

        zoomPane = new ContentZoomScrollPane(graphView);

        AnchorPane.setLeftAnchor(zoomPane, 0.0);
        AnchorPane.setTopAnchor(zoomPane, 0.0);
        AnchorPane.setRightAnchor(zoomPane, 0.0);
        AnchorPane.setBottomAnchor(zoomPane, 0.0);

        contenedorGrafo.getChildren().add(zoomPane);
    }

    /*
        Nombre: inicializarGrafo
        Argumentos: -
        Objetivo: Inializar el grafo de forma estructurada.
        Retorno: -
     */
    private void inicializarGrafo() {
        try {
            graphView.init();
            graphView.requestFocus();
            Platform.runLater(() -> {
                zoomPane.setHvalue(0.5);
                zoomPane.setVvalue(0.5);
            });
            Transition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(e -> graphView.update());
            pause.play();
        } catch (Exception ignored) { }
    }

    /*
        Nombre: crearPanel
        Argumentos:
            (RutaPosible) ruta: Represnta el camino que se va a representar.
        Objetivos: Crear el panel que se va a utilizar para representar los resultados.
        Retorno: -
     */
    public void crearPanel(RutaPosible ruta){
        ResultadoRutaController controller = new ResultadoRutaController();
        controller.setRutaPosible(ruta);

        AnchorPane contenido = controller.crearInterfaz(ruta);
        contenedorGeneral.getChildren().add(contenido);
    }

    /*
        Nombre: aplicarEstilos
        Argummentos:
            (SmartGraphPanel<Parada,Ruta>) panel: Representa el panel al que se le aplicaran los estilos.
        Objetivo: Aplicar los estos esteticos a los vertices del grafo.
        Retorno: -
     */
    public void aplicarEstilos(SmartGraphPanel<Parada,Ruta> panel){
        for(Vertex<Parada> vertice : panel.getModel().vertices()){
            panel.getStylableVertex(vertice).addStyleClass(vertice.element().getTipo().getClase());
        }

        configurarEventosGrafo(panel);
    }

    /*
        Nombre: configurarEventosGrafo
        Argumentos:
            (SmartGraphPanel<Parada,Ruta>) panel: Representa el panel al que se asocian los eventos.
        Objetivo: Configurar los eventos vinculados al grafo al hacer doble click.
        Retorno: -
     */
    private void configurarEventosGrafo(SmartGraphPanel<Parada,Ruta> panel) {
        panel.setVertexDoubleClickAction(graphVertex -> evaluarEventos(panel, graphVertex.getUnderlyingVertex().element()));
    }

    /*
        Nombre: evaluarEventos
        Argumentos:
            (SmartGraphPanel<Parada,Ruta>) panel: Representa el panel que contendra el grafo.
            (Parada) elemento: Representa la parada seleccionada.
        Objetivo: Evaluar los eventos de selección y deseleccion.
        Retorno: -
     */
    private void evaluarEventos(SmartGraphPanel<Parada,Ruta> panel, Parada elemento){
        if (estilizada != null) {
            aplicarPorDefecto();
        }

        if (origen != null && origen.equals(elemento)) {
            panel.getStylableVertex(elemento).removeStyleClass("seleccionado");
            lblOrigen.setText("Seleccione origen en el mapa");
            origen = null;
            limpiarAristasVisuales();
            return;
        }

        if (destino != null && destino.equals(elemento)) {
            panel.getStylableVertex(elemento).removeStyleClass("seleccionado");
            lblDestino.setText("Seleccione destino en el mapa");
            destino = null;
            return;
        }

        if (origen == null) {
            panel.getStylableVertex(elemento).addStyleClass("seleccionado");
            lblOrigen.setText(elemento.getNombreParada());
            origen = elemento;
            construirArbolVisual(origen);
        }
        else if (destino == null && !origen.equals(elemento)) {
            panel.getStylableVertex(elemento).addStyleClass("seleccionado");
            lblDestino.setText(elemento.getNombreParada());
            destino = elemento;
        }
    }

    /*
        Nombre: construirArbolVisual
        Argumentos:
            (Parada) raiz: Representa el nodo de raiz del camino a construir.
        Objetivo: Construir un camino visual.
        Retorno: -
     */
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

    /*
        Nombre: limpiarAristasVisuales
        Argumentos: -
        Objetivo: Limpiar las aristas visuales del grafo cuando no sean requeridas.
        Retorno: -
     */
    private void limpiarAristasVisuales() {
        if (graphView != null && graphView.getModel() != null) {
            List<Edge<Ruta, Parada>> aristas = new ArrayList<>(graphView.getModel().edges());

            for (Edge<Ruta, Parada> arista : aristas) {
                graphView.getModel().removeEdge(arista);
            }
            graphView.update();
        }
    }

    /*
        Nombre: estilizarRuta
        Argumentos:
            (RutaPosible) ruta: Representa la ruta posible que se va a estilizar.
        Objetivo: Aplicar los efectos visuales a un camino.
        Retorno: -
     */
    public void estilizarRuta(RutaPosible ruta){
        for(Ruta rt : ruta.getCamino()){
            SmartStylableNode visualRoute = graphView.getStylableEdge(rt);

            if (visualRoute != null) {
                visualRoute.addStyleClass("camino");
            }
        }
    }

    /*
        Nombre: aplicarPorDefecto
        Argumentos: -
        Objetivo: Aplicar los efectos por defecto de una ruta estilizada.
        Retorno: -
     */
    public void aplicarPorDefecto(){
        if (estilizada != null) {
            for (Ruta rt : estilizada.getCamino()) {
                graphView.getStylableEdge(rt).removeStyleClass("camino");
            }
            estilizada = null;
        }

        if (origen != null) {
            graphView.getStylableVertex(origen).removeStyleClass("seleccionado");
            lblOrigen.setText("Seleccione origen en el mapa");
            origen = null;
        }

        if (destino != null) {
            graphView.getStylableVertex(destino).removeStyleClass("seleccionado");
            lblDestino.setText("Seleccione destino en el mapa");
            destino = null;
        }

        limpiarAristasVisuales();
    }

    /*
        Nombre: cargarResultados
        Argumentos: -
        Objetivo: Cargar los resultados vinculados a los posibles caminos que hay como resultados.
        Retorno: -
    */
    public void cargarResultados() {
        Calculador calc = crearCalculador();
        Stack<RutaPosible> posiblesRutas = new Stack<>();

        if (!procesarCriterios(calc, posiblesRutas)) {
            return;
        }

        RutaPosible mejorRuta = procesarMejorRuta(posiblesRutas);
        List<RutaPosible> rutasUnicas = procesarRutasUnicas(posiblesRutas, mejorRuta);
        RutaPosible rutaADibujar = determinarRutaADibujar(posiblesRutas, mejorRuta, rutasUnicas);

        crearPanelesDeRutas(mejorRuta, rutasUnicas);
        dibujarRutaEnGrafo(rutaADibujar);
    }

    /*
        Nombre: crearCalcuador
        Argumentos: -
        Objetivo: Crear el objeto calculador y ubicar el grafo como base para resolver el calculo de rutas.
        Retorno: (Calculador) Retorna el calculador que sera utilizado para el proceso.
     */
    private Calculador crearCalculador() {
        Calculador calc = new Calculador();
        calc.setGrafo(SistemaTransporte.getSistemaTransporte().getGrafo());
        return calc;
    }

    /*
        Nomrbe: procesarCriterios
        Argumentos:
            (Calculador) calc: Representa el objeto calculador del programa.
            (Stack<RutaPosible>) posiblesRutas: Representa el listado de todas las rutas posibles existentes.
        Objetivo: Determinar si existen resultados posibles que porcesar.
        Retorno: (boolean) Retorna true si hay un resultado posible para procesar.
                           Retorna false si no hay resultado posible para procesar.
     */
    private boolean procesarCriterios(Calculador calc, Stack<RutaPosible> posiblesRutas) {
        for (Criterio criterio : Criterio.values()) {
            if (criterio.equals(Criterio.MEJOR_RUTA)) {
                continue;
            }

            RutaPosible ruta = calc.dijkstra(origen, destino, criterio);

            if (ruta == null) {
                mostrarAlertaNoHayCamino();
                return false;
            }

            contenedorGeneral.getChildren().clear();
            posiblesRutas.add(ruta);
        }
        return true;
    }

    /*
        Nombre: procesarMejorRuta
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las posibles rutas que hay seleccionada.
        Objetivo: Determinar cual es la mejor ruta entre la existentes.
        Retorno: (RutaPosible) Representa la ruta que es destacada como la mejor ruta.
     */
    private RutaPosible procesarMejorRuta(Stack<RutaPosible> posiblesRutas) {
        RutaPosible mejorRuta = sm.obtenerMejorRuta(posiblesRutas);

        if (mejorRuta != null) {
            mejorRuta.agregarFirst(Criterio.MEJOR_RUTA);
            mejorRuta.setEsMejorRuta(true);
        }

        return mejorRuta;
    }

    /*
        Nombre: procesarRutasUnicas
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las posibles rutas seleccionada.
            (RutaPosible) mejorRuta: Representa la mejor ruta actual.
        Objetivo: Procesar las rutas que resultan ser unicas.
        Retorno: (List<RutaPosible>) Retorna una lista de rutas posibles que excluyen a la mejor ruta.
     */
    private List<RutaPosible> procesarRutasUnicas(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta) {
        return sm.obtenerRutasExceptoMejor(posiblesRutas, mejorRuta);
    }

    /*
        Nombre: determinarRutaADibujar
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las posibles rutas existentes.
            (RutaPosible) mejorRuta: Representa la mejor ruta.
            (List<RutaPosible>) rutasUnicas: Representa las distintas rutas unicas que hay.
        Objetivo: Determinar cual ruta sera dibujada en el programa.
        Retorno: (RutaPosible) Retorna la mejor ruta o la primera ruta que hay entre las disponibles.
     */
    private RutaPosible determinarRutaADibujar(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta, List<RutaPosible> rutasUnicas) {
        if (mejorRuta != null) {
            return mejorRuta;
        }

        RutaPosible rutaEconomica = posiblesRutas.stream()
                .filter(ruta -> ruta.getCriteriosDestacados().contains(Criterio.MAS_ECONOMICO))
                .findFirst()
                .orElse(null);

        if (rutaEconomica != null) {
            return rutaEconomica;
        }

        return !rutasUnicas.isEmpty() ? rutasUnicas.getFirst() : null;
    }

    /*
        Nombre: crearPanelesDeRutas
        Argumentos:
            (RutaPosible) mejorRuta: Representa la mejor ruta existente.
            (List<RutaPosible>) rutasUnicas: Representa las rutas unicas existentes.
        Objetivo: Crear los controles de los resultados de ruta.
        Retorno: -
     */
    private void crearPanelesDeRutas(RutaPosible mejorRuta, List<RutaPosible> rutasUnicas) {
        if (mejorRuta != null) {
            crearPanel(mejorRuta);
        }

        for (RutaPosible rutaUnica : rutasUnicas) {
            if (rutaUnica != null) {
                crearPanel(rutaUnica);
            }
        }
    }

    /*
        Nombre: dibujarRutaEnGrafo
        Argumentos:
            (RutaPosible) rutaADibujar: Representa la ruta que se va a dibujar.
        Objetivo: Aplicar los efectos visuales y esteticos al camino a dibujar.
        Retorno: -
     */
    private void dibujarRutaEnGrafo(RutaPosible rutaADibujar) {
        if (rutaADibujar == null) {
            return;
        }

        estilizada = rutaADibujar;
        insertarVerticesYAristas(rutaADibujar);
        actualizarYEstilizarGrafo(rutaADibujar);
    }

    /*
         Nombre: insertarVerticesYAristas
         Argumentos:
            (RutaPosible) ruta: Representa la ruta que se va a insertar.
         Objetivo: Insertar vertices y aristas utilizando de referencia una ruta.
         Retorno: -
     */
    private void insertarVerticesYAristas(RutaPosible ruta) {
        for (Ruta rt : ruta.getCamino()) {
            try {
                graphView.getModel().insertVertex(rt.getOrigen());
                graphView.getModel().insertVertex(rt.getDestino());
                graphView.getModel().insertEdge(rt.getOrigen(), rt.getDestino(), rt);
            } catch (Exception ignored) {}
        }
    }


    /*
        Nombre: actualizarYEstilizarGrafo
        Argumentos:
            (RutaPosible) ruta: Representa la ruta que se agregara al grafo.
        Objetivo: Actualizar el grafo luego de que se apliquen los efectos esteticos.
        Retorno: -
     */
    private void actualizarYEstilizarGrafo(RutaPosible ruta) {
        graphView.update();

        Platform.runLater(() -> {
            try {
                graphView.update();
                estilizarRuta(ruta);
            } catch (Exception ignored) {}
        });
    }

    /*

     */
    private void mostrarAlertaNoHayCamino() {
        alertFactory.obtenerAlerta(Alert.AlertType.WARNING)
                .crearAlerta("Todavía no existe un camino para llegar desde " +
                                origen.getNombreParada() + " a " +
                                destino.getNombreParada() + ".",
                        "Advertencia de cálculo.")
                .show();
    }
}