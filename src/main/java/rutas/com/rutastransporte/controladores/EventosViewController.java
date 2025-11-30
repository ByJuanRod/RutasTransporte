package rutas.com.rutastransporte.controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.utilidades.RecursosVisuales;
import rutas.com.rutastransporte.modelos.StageBuilder;
import rutas.com.rutastransporte.servicios.ServicioEventos;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;

public class EventosViewController {
    private final AlertFactory alert = new AlertFactory();

    @FXML
    private TableView<EventoRuta> tblEventos;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<EventoRuta, String> colCodigo, colRuta, colTipoEvento, colDuracion;

    private final ServicioEventos servicioEventos = ServicioEventos.getInstancia();

    private FilteredList<EventoRuta> filteredData;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();

        tblEventos.widthProperty().addListener((obs, oldWidth, newWidth) -> RecursosVisuales.ajustarAnchoColumnas(tblEventos));
    }

    public void btnEliminarClick(){
        EventoRuta eventoSeleccionado = tblEventos.getSelectionModel().getSelectedItem();

        if(eventoSeleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un evento para eliminar.");
            return;
        }

        procesarEliminacion(eventoSeleccionado);
    }

    /*
    Nombre: procesarEliminacion
    Argumentos:
        (EventoRuta) eventoSeleccionado: Representa el evento seleccionado.
    Objetivo: Procesar la eliminación de un evento y confirmar la eliminación.
    Retorno: -
    */
    public void procesarEliminacion(EventoRuta eventoSeleccionado){
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el evento?");
        confirmacion.setContentText("Tipo: " + eventoSeleccionado.getTipoEvento().getNombre() +
                " - Ruta: " + eventoSeleccionado.getRuta().getNombre());
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    servicioEventos.eliminar(eventoSeleccionado);
                    cargarDatos();
                    mostrarAlerta("Éxito", "Evento eliminado correctamente.");
                } catch (Exception ex) {
                    mostrarAlerta("Error", "Error al eliminar el evento: " + ex.getMessage());
                }
            }
        });
    }

    public void btnInsertarClick(){
        crearPantalla();
    }

    public void txtBuscarKeyPressed(){
        filtrar();
    }

    /*
    Nombre: crearPantalla
    Argumentos: -
    Objetivo: Crear las pantallas de los formularios de registro.
    Retorno: -
 */
    public void crearPantalla() {
        StageBuilder sb = new StageBuilder();
        sb.setModalidad(Modality.APPLICATION_MODAL);
        sb.setTitulo("Registrar Evento");

        RegistroEventoController controlador = (RegistroEventoController) sb.setContenido("RegistroEvento");
        controlador.cargarDatos();

        Stage st = sb.construir();
        controlador.setStage(st);

        st.setOnHidden(event -> cargarDatos());
        st.show();
    }

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar las paradas en la tabla.
        Retorno: -
    */
    public void cargarDatos() {
        ObservableList<EventoRuta> eventosActivos = FXCollections.observableArrayList();

        for (Ruta ruta : servicioEventos.getRutasConEventosActivos()) {
            EventoRuta evento = servicioEventos.getEvento(ruta);
            if (evento != null && evento.estaActivo()) {
                eventosActivos.add(evento);
            }
        }

        filteredData = new FilteredList<>(eventosActivos, p -> true);
        tblEventos.setItems(filteredData);
        filtrar();
    }

    /*
    Nombre: filtrar
    Argumentos: -
    Objetivo: Filtrar los datos de le tabla tomando como referencia el valor ingresado como texto de busqueda.
    Retorno: -
 */
    public void filtrar() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        filteredData.setPredicate(evento -> {
            if (textoBusqueda.isEmpty()) {
                return true;
            }

            if (evento == null || evento.getRuta() == null) {
                return false;
            }

            Ruta ruta = evento.getRuta();

            boolean coincideCodigo = String.valueOf(ruta.getCodigo()).contains(textoBusqueda);
            boolean coincideNombre = ruta.getNombre() != null &&
                    ruta.getNombre().toLowerCase().contains(textoBusqueda);
            boolean coincideTipoEvento = evento.getTipoEvento() != null &&
                    evento.getTipoEvento().getNombre().toLowerCase().contains(textoBusqueda);

            return coincideCodigo || coincideNombre || coincideTipoEvento;
        });
    }

    /*
        Nombre: configurarColumnas
        Argumentos: -
        Objetivo: Configurar las columnas de los campos de la tabla para asociarlas a un una propiedad.
        Retorno: -
     */
    public void configurarColumnas() {
        colCodigo.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        String.valueOf(cellData.getValue().getRuta().getCodigo())
                )
        );

        colRuta.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getRuta().getNombre()
                )
        );

        colTipoEvento.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getTipoEvento().getNombre()
                )
        );

        colDuracion.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() -> {
                    long duracionRestante = (cellData.getValue().getFechaFin().getTime() - System.currentTimeMillis()) / (60 * 1000);
                    return duracionRestante + " min";
                })
        );
    }


    /*
        Nombre: mostrarAlerta
        Argumentos:
            (String) titulo: Representa el titulo de la alerta.
            (String) mensaje: Representa el mensaje de la alerta.
       Objetivo: Mostrar las alertas del programa
       Retorno: -
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        alert.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(titulo, mensaje).showAndWait();
    }

    public void btnInformeClick(){
        EventoRuta eventoSeleccionado = tblEventos.getSelectionModel().getSelectedItem();
        if(eventoSeleccionado != null){
            StageBuilder sb = new StageBuilder();
            sb.setModalidad(Modality.APPLICATION_MODAL);
            sb.setTitulo("Informe de Evento");

            InformeEventoController controlador = (InformeEventoController) sb.setContenido("InformeEvento");
            controlador.setEventoSeleccionado(eventoSeleccionado);
            controlador.cargarDatos();

            Stage st = sb.construir();
            controlador.setStage(st);
            st.show();
        }
        else{
            alert.obtenerAlerta(Alert.AlertType.WARNING).crearAlerta("Para crear un informe debe tene run evento seleccionado.").show();
        }

    }
}