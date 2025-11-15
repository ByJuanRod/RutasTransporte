package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.excepciones.NotRemovableException;
import rutas.com.rutastransporte.modelos.Vista;
import rutas.com.rutastransporte.RecursosVisuales;
import rutas.com.rutastransporte.servicios.ParadasDAO;
import rutas.com.rutastransporte.StageBuilder;
import rutas.com.rutastransporte.utilidades.Modalidad;
import rutas.com.rutastransporte.modelos.Parada;

import java.util.Objects;

public class ParadasViewController implements Vista<Parada> {
    private final ParadasDAO paradasDAO = new ParadasDAO();

    @FXML
    private TableView<Parada> tblParadas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableColumn<Parada, String> colCodigo, colDireccion,colNombre, colTipo;

    @FXML
    private TableColumn<Parada, ImageView> colImg;

    @FXML
    public void initialize(){
        configurarColumnas();
        cargarDatos();

        tblParadas.widthProperty().addListener((obs, oldWidth, newWidth) -> RecursosVisuales.ajustarAnchoColumnas(tblParadas));

    }

    public void btnEliminarClick(){
        Parada paradaSeleccionada = tblParadas.getSelectionModel().getSelectedItem();

        if (paradaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una parada para eliminar.");
            return;
        }

        try {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar la parada?");
            confirmacion.setContentText("Parada: " + paradaSeleccionada.getNombreParada());
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        paradasDAO.eliminar(paradaSeleccionada);
                        cargarDatos();
                        mostrarAlerta("Éxito", "Parada eliminada correctamente.");
                    } catch (NotRemovableException ex) {
                        mostrarAlerta("Error", ex.getMessage());
                    }
                }
            });

        } catch (Exception ex) {
            mostrarAlerta("Error", "Ocurrió un error al eliminar la parada: " + ex.getMessage());
        }
    }

    public void btnActualizarClick(){
        Parada parada = tblParadas.getSelectionModel().getSelectedItem();
        if (parada == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una parada para modificar.");
            return;
        }
        crearPantalla("Modificar Parada", Modalidad.ACTUALIZAR, parada);
    }

    public void btnInsertarClick(){
        crearPantalla("Insertar Parada", Modalidad.INSERTAR, null);
    }

    public void txtBuscarKeyPressed(){
        filtrar();
    }

    @Override
    public void crearPantalla(String titulo, Modalidad modalidad, Parada objeto) {
        StageBuilder sb = new StageBuilder();
        sb.setModalidad(Modality.APPLICATION_MODAL);
        sb.setTitulo(titulo);

        RegistroParadaController controlador = (RegistroParadaController) sb.setContenido("RegistroParada");
        controlador.setModalidad(modalidad);
        controlador.setParada(objeto);

        controlador.cargarDatos();
        Stage st = sb.construir();
        controlador.setStage(st);

        st.setOnHidden(event -> cargarDatos());

        st.show();
    }

    @Override
    public void cargarDatos() {
        tblParadas.getItems().clear();
        tblParadas.setItems(paradasDAO.getParadas());
    }

    @Override
    public void filtrar() {
        String textoBusqueda = txtBuscar.getText().trim();

        if (textoBusqueda.isEmpty()) {
            cargarDatos();
        }
    }

    @Override
    public void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreParada"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));

        colTipo.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getTipo().getTipo()
                )
        );

        colImg.setCellValueFactory(cellData -> {
            String nombreImagen = cellData.getValue().getTipo().getImagen();
            ImageView imageView = new ImageView();

            try {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + nombreImagen)));
                imageView.setImage(image);
                imageView.setFitWidth(24);
                imageView.setFitHeight(24);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}