package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.utilidades.Criterio;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.RutaPosible;
import rutas.com.rutastransporte.utilidades.RecursosVisuales;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.utilidades.Factor;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class DetallesRutaController {
    private final AlertFactory alertFactory = new AlertFactory();

    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public RutaPosible ruta;

    public void setRuta(RutaPosible ruta) {
        this.ruta = ruta;
    }

    public String mensajeSimulacion;

    @FXML
    private Label lblIndicador, lblCamino, lblCosto, lblDistancia, lblTrasbordos, lblTiempo;

    @FXML
    private ImageView imgRapida, imgCorta, imgEconomico, imgAccidente, imgDesvio, imgLibre, imgConcurrido, imgTrasbordos, imgIndicador;

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar los datos de los detalles de las rutas.
        Retorno: -
     */
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
            mensajeSimulacion = crearMensajeSimulacion();
        }
        catch (Exception e){
            alertFactory.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("Ha ocurrido un error.");
        }
    }

    /*
        Nombre: verificarCriterio
        Argumentos:
            (Criterio) criterio: Representa el criterio que se va a evaluar.
            (LinkedList<Criterio>) criteriosDestacadas: Representa la lista de criterios destacados.
            (ImageView) img: Representa el objeto que contiene la imagen del criterio.
     */
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

    /*
        Nombre: evaluarEventosCriterios
        Argumentos:
            (String) mensajeSi: Representa el mensaje que se mostrara si se cumple el criterio.
            (String) mensajeNo: Representa el mensaje que se mostrara si no se cumple el criterio.
            (Criterio) criterio: Representa el criterio que se va a evaluar.
        Objetivo: Determinar el mensaje apropiado para el estado de los criterios.
        Retorno: -
     */
    public void evaluarEventosCriterios(String mensajeSi, String mensajeNo, Criterio criterio){
        if(ruta.getCriteriosDestacados().contains(criterio)){
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

    /*
        Nombre: evaluarEventosSimulaciones
        Argumentos:
            (String) mensajeSi: Representa el mensaje si el camino tiene la simulacion deseada.
            (String) mensajeNo: Representa el menesaje si el camino no tiene la simulación deseada.
            (TipoEvento) tipoEvento: Representa el tipo de evento..
        Objetivo: Crear el mensaje correspondiente a si el camino contiene el evento.
        Retorno: -
     */
    public void evaluarEventosSimulaciones(String mensajeSi, String mensajeNo, TipoEvento tipoEvento){
        if(ruta.getRegistroEventos().contains(tipoEvento)){
            alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeSi,"Evento.").show();
        }
        else{
            alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeNo,"Evento.").show();
        }
    }

    /*
        Nombre: aplicarCriterios
        Argumentos: -
        Objetivo: Verificar los criterios que tiene vinculado el camino.
        Retorno: -
     */
    public void aplicarCriterios(){
        verificarCriterio(Criterio.MAS_RAPIDA, ruta.getCriteriosDestacados(), imgRapida);
        verificarCriterio(Criterio.MAS_CORTA, ruta.getCriteriosDestacados(), imgCorta);
        verificarCriterio(Criterio.MAS_ECONOMICO, ruta.getCriteriosDestacados(), imgEconomico);
        verificarCriterio(Criterio.MENOS_TRASBORDOS, ruta.getCriteriosDestacados(), imgTrasbordos);
    }

    /*
        Nombre: verificarSimulacion
        Argumentos:
            (TipoEvento) tipoEvento: Representa el tipo de eventos que se va a verificar.
            (Set<TipoEvento>) eventos: Representa la lista de eventos.
            (ImageView) image: Representa el objeto de imagen que almacenara la imagen.
        Objetivo: Verificar si un camino contiene un evento
        Retorno: -
     */
    public void verificarSimulacion(TipoEvento tipoEvento, Set<TipoEvento> eventos, ImageView image){
        if(eventos.contains(tipoEvento)){
            image.setImage(RecursosVisuales.getTieneCriterio());
        }
    }

    /*
        Nombre: aplicarSimulaciones
        Argumentos: -
        Objetivo: Verificar si un evento tiene tiene cada simulacion.
        Retorno: -
     */
    public void aplicarSimulaciones(){
        verificarSimulacion(TipoEvento.DESVIO,ruta.getRegistroEventos(),imgDesvio);
        verificarSimulacion(TipoEvento.ACCIDENTE,ruta.getRegistroEventos(),imgAccidente);
        verificarSimulacion(TipoEvento.CAMINO_LIBRE,ruta.getRegistroEventos(),imgLibre);
        verificarSimulacion(TipoEvento.ZONA_CONCURRIDA,ruta.getRegistroEventos(),imgConcurrido);
    }

    /*
        Nombre: crearMensajeSimulacion
        Argumentos: -
        Objetivo: Crear los mensajes asociados a las simulaciones.
        Retorno: (String) Retorna el mensaje que resume los detalles asociados a las simulaciones.
     */
    public String crearMensajeSimulacion(){
        float costo = ruta.getDiffCosto();
        float tiempo = ruta.getDiffTiempo();
        float distancia = ruta.getDiffDistancia();

        return "\t\tDetalle de los Eventos\n" +
                getFactorFormateado(Factor.COSTO,costo) +
                getFactorFormateado(Factor.TIEMPO,tiempo) +
                getFactorFormateado(Factor.DISTANCIA,distancia);
    }

    private String getFactorFormateado(Factor factor, float valor){
        if(factor == Factor.DISTANCIA && valor < 0){
            return "\nDistancia Reducida: " + Ruta.getDistanciaFormatado(valor);
        }
        else if(factor == Factor.DISTANCIA && valor >= 0){
            return "\nDistancia Agregada: " + Ruta.getDistanciaFormatado(valor);
        }
        else if(factor == Factor.TIEMPO && valor < 0){
            return "\nTiempo Reducido: " +  Ruta.getTiempoFormatado(valor);
        }
        else if(factor == Factor.TIEMPO && valor >= 0){
            return "\nTiempo Agregado: " +  Ruta.getTiempoFormatado(valor);
        }
        else if(factor == Factor.COSTO && valor < 0){
            return "\nCosto Reducido: " + valor + " (DOP)";
        }
        else if(factor == Factor.COSTO && valor >= 0){
            return "\nCosto Agregado: " + valor + " (DOP)";
        }

        return "";
    }


    public void mostrarDetallesEventosClick(){
        alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(mensajeSimulacion,"Detalles de la Ruta.").show();
    }
}
