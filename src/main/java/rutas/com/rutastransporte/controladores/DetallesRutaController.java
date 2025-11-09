package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Criterio;
import rutas.com.rutastransporte.modelos.RutaPosible;
import rutas.com.rutastransporte.RecursosVisuales;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class DetallesRutaController {
    public AlertFactory alertFactory = new AlertFactory();

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
            lblIndicador.setText(ruta.getCriteriosDestacados().getFirst().getNombre());
            if(ruta.isEsMejorRuta()){
                imgIndicador.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + ruta.getCriteriosDestacados().getFirst().getImagen()))));
            }
            lblCamino.setText(ruta.getCaminoTexto());
            lblCosto.setText(ruta.getCostoTotal() + " (DOP)");
            lblDistancia.setText(ruta.getDistanciaFormatado());
            lblTrasbordos.setText(String.valueOf(ruta.getCantTrasbordos()));
            lblTiempo.setText(ruta.getTiempoFormatado());
            aplicarCriterios();
            aplicarSimulaciones();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void verificarCriterio(Criterio criterio, LinkedList<Criterio> criteriosDestacados, ImageView img){
        if(criteriosDestacados.contains(criterio)){
            img.setImage(RecursosVisuales.getTieneCriterio());
        }
    }

    public void imgEconomicoClick(){
        evaluarEventosCriterios("Ruta destacada por ser la más económica.","No es la ruta más económica.",Criterio.MAS_ECONOMICO);
    }

    public void imgRapidaClick(){
        evaluarEventosCriterios("Ruta destacada por ser la más rápida.","No es la ruta más rápida.",Criterio.MAS_RAPIDA);
    }

    public void imgTrasbordosClick(){
        evaluarEventosCriterios("Ruta destacada por ser la que menos trasbordos tiene.","No es la con menos trasbordos.",Criterio.MENOS_TRASBORDOS);
    }

    public void imgCortaClick(){
        evaluarEventosCriterios("Ruta destacada por tener la distancia más corta.","No es la ruta con la menos distancia.",Criterio.MAS_CORTA);
    }

    public void evaluarEventosCriterios(String mensajeSi, String mensajeNo, Criterio criterio){
        if(ruta.getCriteriosDestacados().contains(Criterio.MAS_ECONOMICO)){
            alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeSi,"Indicador.").show();
        }
        else{
            alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeNo,"Indicador.").show();
        }
    }

    public void imgConcurridoClick(){
        evaluarEventosSimulaciones("Se encontró una zona concurrida dentro del camino.","No se encontraron zonas concurridas en el camino.",TipoEvento.ZONA_CONCURRIDA);
    }

    public void imgDesvioClick(){
        evaluarEventosSimulaciones("Se encontró un desvio dentro del camino.","No se encontraron desvios en el camino.",TipoEvento.DESVIO);
    }

    public void imgLibreClick(){
        evaluarEventosSimulaciones("Se encontraron caminos libres que facilitan el tránsito.","No se encontraron caminos libres que faciliten el tránsito.",TipoEvento.CAMINO_LIBRE);
    }

    public void imgAccidenteClick(){
        evaluarEventosSimulaciones("Se encontró un accidente dentro del camino.","No se encontraron accidentes en el camino.",TipoEvento.ACCIDENTE);
    }



    public void evaluarEventosSimulaciones(String mensajeSi, String mensajeNo, TipoEvento tipoEvento){
        if(ruta.getRegistroEventos().contains(tipoEvento)){
            alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeSi,"Evento.").show();
        }
        else{
            alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeNo,"Evento.").show();
        }
    }

    public void aplicarCriterios(){
        verificarCriterio(Criterio.MAS_RAPIDA, ruta.getCriteriosDestacados(), imgRapida);
        verificarCriterio(Criterio.MAS_CORTA, ruta.getCriteriosDestacados(), imgCorta);
        verificarCriterio(Criterio.MAS_ECONOMICO, ruta.getCriteriosDestacados(), imgEconomico);
        verificarCriterio(Criterio.MENOS_TRASBORDOS, ruta.getCriteriosDestacados(), imgTrasbordos);
    }

    public void verificarSimulacion(TipoEvento tipoEvento, Set<TipoEvento> eventos, ImageView image){
        if(eventos.contains(tipoEvento)){
            image.setImage(RecursosVisuales.getTieneCriterio());
        }
    }

    public void aplicarSimulaciones(){
        verificarSimulacion(TipoEvento.DESVIO,ruta.getRegistroEventos(),imgDesvio);
        verificarSimulacion(TipoEvento.ACCIDENTE,ruta.getRegistroEventos(),imgAccidente);
        verificarSimulacion(TipoEvento.CAMINO_LIBRE,ruta.getRegistroEventos(),imgLibre);
        verificarSimulacion(TipoEvento.ZONA_CONCURRIDA,ruta.getRegistroEventos(),imgConcurrido);
    }
}
