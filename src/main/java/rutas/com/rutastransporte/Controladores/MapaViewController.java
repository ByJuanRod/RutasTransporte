package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import rutas.com.rutastransporte.Modelos.*;
import rutas.com.rutastransporte.Repositorio.SistemaTransporte;
import rutas.com.rutastransporte.Servicios.Calculador;
import rutas.com.rutastransporte.Servicios.GrafoTransporte;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

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
            Stack<RutaPosible> posiblesRutas = new Stack<>();

            Parada origen = buscarParada(cbxOrigen.getSelectionModel().getSelectedItem());
            Parada destino = buscarParada(cbxDestino.getSelectionModel().getSelectedItem());

            for(Criterio criterio : Criterio.values()){
                if(!criterio.equals(Criterio.MEJOR_RUTA)){
                    posiblesRutas.add(calc.dijkstra(origen,destino,criterio));
                }
            }

            MejorRuta mejorRuta = ObtenerMejorRuta(posiblesRutas);

            ArrayList<RutaPosible> rutasUnicas = obtenerRutasUnicasExcluyendoMejor(posiblesRutas, mejorRuta);

            if (mejorRuta != null) {
                mejorRuta.setCriterio(Criterio.MEJOR_RUTA);
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

    public MejorRuta ObtenerMejorRuta(Stack<RutaPosible> posiblesRutas){
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
            MejorRuta mejorRuta = new MejorRuta(rutasUnicas.get(0));
            mejorRuta.agregarCriterio(rutasUnicas.get(0).getCriterio());
            return mejorRuta;
        }

        RutaPosible mejorRutaBase = null;
        int maxCriteriosCoincidentes = 0;

        for (RutaPosible rutaUnica : rutasUnicas) {
            int criteriosCoincidentes = cantIguales(posiblesRutas, rutaUnica.getCamino());

            if (criteriosCoincidentes > maxCriteriosCoincidentes) {
                maxCriteriosCoincidentes = criteriosCoincidentes;
                mejorRutaBase = rutaUnica;
            }
        }

        if (mejorRutaBase != null) {
            MejorRuta mejorRuta = new MejorRuta(mejorRutaBase);

            for (RutaPosible posible : posiblesRutas) {
                if (posible != null && posible.sonIguales(mejorRutaBase.getCamino())) {
                    mejorRuta.agregarCriterio(posible.getCriterio());
                }
            }

            return mejorRuta;
        }

        return null;
    }

    public int cantIguales(Stack<RutaPosible> posiblesRutas, LinkedList<Ruta> camino){
        int cantidadCriterios = 0;
        for(RutaPosible posible : posiblesRutas){
            if(posible.sonIguales(camino)){
                cantidadCriterios++;
            }
        }

        return cantidadCriterios;
    }

    public ArrayList<RutaPosible> obtenerRutasUnicasExcluyendoMejor(Stack<RutaPosible> posiblesRutas, MejorRuta mejorRuta) {
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
