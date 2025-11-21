package rutas.com.rutastransporte.controladores;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.RecursosVisuales;
import rutas.com.rutastransporte.StageBuilder;
import rutas.com.rutastransporte.utilidades.Modalidad;
import rutas.com.rutastransporte.servicios.RutasDAO;
import rutas.com.rutastransporte.excepciones.NotRemovableException;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;

public class RutasViewController implements Vista<Ruta> {
    private final AlertFactory alert = new AlertFactory();

    @FXML
    private TableView<Ruta> tblRutas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<Ruta, String> colCodigo, colNombre, colOrigen,colDestino;

    private final RutasDAO rutasDAO = new RutasDAO();

    private FilteredList<Ruta> filteredData;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();

        tblRutas.widthProperty().addListener((obs, oldWidth, newWidth) -> RecursosVisuales.ajustarAnchoColumnas(tblRutas));
    }

    public void btnEliminarClick(){
        Ruta rutaSeleccionada = tblRutas.getSelectionModel().getSelectedItem();

        if (rutaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una ruta para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar la ruta?");
        confirmacion.setContentText("Ruta: " + rutaSeleccionada.getNombre());
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    rutasDAO.eliminar(rutaSeleccionada);
                    cargarDatos();
                    mostrarAlerta("Éxito.", "Ruta eliminada correctamente.");
                } catch (NotRemovableException ex) {
                    mostrarAlerta("Error", ex.getMessage());
                }
            }
        });
    }

    public void btnActualizarClick(){
        Ruta ruta = tblRutas.getSelectionModel().getSelectedItem();
        if (ruta == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una ruta para modificar.");
            return;
        }
        crearPantalla("Modificar Ruta", Modalidad.ACTUALIZAR, ruta);
    }

    public void btnInsertarClick(){
        crearPantalla("Insertar Ruta", Modalidad.INSERTAR, null);
    }

    public void txtBuscarKeyPressed(){
        filtrar();
    }

    @Override
    public void crearPantalla(String titulo, Modalidad modalidad, Ruta ruta) {
        StageBuilder sb = new StageBuilder();
        sb.setModalidad(Modality.APPLICATION_MODAL);
        sb.setTitulo(titulo);

        RegistroRutaController controlador = (RegistroRutaController) sb.setContenido("RegistroRuta");
        controlador.setModalidad(modalidad);
        controlador.setRuta(ruta);

        controlador.cargarDatos();
        Stage st = sb.construir();
        controlador.setStage(st);

        if(modalidad.equals(Modalidad.ACTUALIZAR) || modalidad.equals(Modalidad.INSERTAR)){
            st.setOnHidden(event -> cargarDatos());
            st.show();
        }
    }

    @Override
    public void cargarDatos() {
        ObservableList<Ruta> datosOriginales = rutasDAO.getRutas();
        filteredData = new FilteredList<>(datosOriginales, p -> true);
        tblRutas.setItems(filteredData);
        filtrar();
    }

    @Override
    public void filtrar() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        filteredData.setPredicate(ruta -> {
            if (textoBusqueda.isEmpty()) {
                return true;
            }

            boolean coincideCodigo = String.valueOf(ruta.getCodigo()).contains(textoBusqueda);

            boolean coincideNombre = ruta.getNombre() != null &&
                    ruta.getNombre().toLowerCase().contains(textoBusqueda);

            boolean coincideOrigen = ruta.getOrigen() != null &&
                    ruta.getOrigen().getNombreParada() != null &&
                    ruta.getOrigen().getNombreParada().toLowerCase().contains(textoBusqueda);

            boolean coincideDestino = ruta.getDestino() != null &&
                    ruta.getDestino().getNombreParada() != null &&
                    ruta.getDestino().getNombreParada().toLowerCase().contains(textoBusqueda);

            return coincideCodigo || coincideNombre || coincideOrigen || coincideDestino;
        });
    }

    @Override
    public void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colOrigen.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getOrigen().getNombreParada()
                )
        );
        colDestino.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getDestino().getNombreParada()
                )
        );
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        alert.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta(titulo, mensaje).showAndWait();
    }

    public void btnDestacadasClick(){
        StageBuilder sb = new StageBuilder();
        sb.setModalidad(Modality.APPLICATION_MODAL);
        sb.setTitulo("Rutas Destacadas");

        RutasDestacadasController controlador = (RutasDestacadasController) sb.setContenido("RutasDestacadas");
        Stage st = sb.construir();
        controlador.setStage(st);
        st.show();
    }
}