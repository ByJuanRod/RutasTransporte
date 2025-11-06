package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Modelos.Vista;
import rutas.com.rutastransporte.RecursosVisuales;
import rutas.com.rutastransporte.StageBuilder;
import rutas.com.rutastransporte.Utilidades.Modalidad;
import rutas.com.rutastransporte.Servicios.RutasDAO;
import rutas.com.rutastransporte.Excepciones.NotRemovableException;

public class RutasViewController implements Vista<Ruta> {

    @FXML
    private TableView<Ruta> tblRutas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnActualizar;

    @FXML
    private TableColumn<Ruta, String> colCodigo;

    @FXML
    private TableColumn<Ruta, String> colNombre;

    @FXML
    private TableColumn<Ruta, String> colOrigen;

    @FXML
    private TableColumn<Ruta, String> colDestino;

    private RutasDAO rutasDAO = new RutasDAO();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();

        tblRutas.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            RecursosVisuales.ajustarAnchoColumnas(tblRutas);
        });
    }

    public void btnEliminarClick(ActionEvent e){
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
                    mostrarAlerta("Éxito", "Ruta eliminada correctamente.");
                } catch (NotRemovableException ex) {
                    mostrarAlerta("Error", ex.getMessage());
                }
            }
        });
    }

    public void btnActualizarClick(ActionEvent e){
        Ruta ruta = tblRutas.getSelectionModel().getSelectedItem();
        if (ruta == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una ruta para modificar.");
            return;
        }
        crearPantalla("Modificar Ruta", Modalidad.ACTUALIZAR, ruta);
    }

    public void btnInsertarClick(ActionEvent e){
        crearPantalla("Insertar Ruta", Modalidad.INSERTAR, null);
    }

    public void txtBuscarKeyPressed(KeyEvent e){
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

        st.setOnHidden(event -> cargarDatos());

        st.show();
    }

    @Override
    public void cargarDatos() {
        tblRutas.getItems().clear();
        tblRutas.setItems(rutasDAO.getRutas());
    }

    @Override
    public void filtrar() {
        String textoBusqueda = txtBuscar.getText().trim();

        if (textoBusqueda.isEmpty()) {
            cargarDatos();
        } else {
            tblRutas.setItems(rutasDAO.buscarPorNombre(textoBusqueda));
        }
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
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void refrescarDatos() {
        cargarDatos();
    }
}