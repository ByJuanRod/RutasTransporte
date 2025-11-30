package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.excepciones.NotRemovableException;
import rutas.com.rutastransporte.utilidades.RecursosVisuales;
import rutas.com.rutastransporte.servicios.ParadasDAO;
import rutas.com.rutastransporte.modelos.StageBuilder;
import rutas.com.rutastransporte.utilidades.Modalidad;
import rutas.com.rutastransporte.modelos.Parada;

import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;

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

    private FilteredList<Parada> filteredData;

    @FXML
    public void initialize(){
        configurarColumnas();
        cargarDatos();

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> filtrar());
        tblParadas.widthProperty().addListener((obs, oldWidth, newWidth) -> RecursosVisuales.ajustarAnchoColumnas(tblParadas));
    }

    public void btnEliminarClick(){
        Parada paradaSeleccionada = tblParadas.getSelectionModel().getSelectedItem();

        if (paradaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una parada para eliminar.");
            return;
        }

        try {
            procesarEliminacion(paradaSeleccionada);
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

    public void btnInformeClick(){
        Parada paradaSeleccionado = tblParadas.getSelectionModel().getSelectedItem();
        if(paradaSeleccionado != null){
            StageBuilder sb = new StageBuilder();
            sb.setModalidad(Modality.APPLICATION_MODAL);
            sb.setTitulo("Informe de Parada");

            InformeParadaController controlador = (InformeParadaController) sb.setContenido("InformeParada");
            controlador.setParada(paradaSeleccionado);
            controlador.cargarDatos();

            Stage st = sb.construir();
            controlador.setStage(st);
            st.show();
        }
        else{
            mostrarAlerta("Conflicto de Selección.", "Por favor, seleccione una parada para modificar.");
        }
    }

    public void btnInsertarClick(){
        crearPantalla("Insertar Parada", Modalidad.INSERTAR, null);
    }

    public void txtBuscarKeyReleased(){
        filtrar();
    }

    /*
        Nombre: crearPantalla
        Argumentos:
            (String) titulo: Representa el titulo del apartado.
            (Modalidad) modalidad: Representa la modalidad en la que se utilizara el formulario.
            (Parada) objeto: Representa el objeto a insertar o modificar.
        Objetivo: Crear las pantallas de los formularios de registro.
        Retorno: -
     */
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

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar las paradas en la tabla.
        Retorno: -
     */
    @Override
    public void cargarDatos() {
        ObservableList<Parada> datosOriginales = paradasDAO.getParadas();
        filteredData = new FilteredList<>(datosOriginales, p -> true);

        tblParadas.setItems(filteredData);
        filtrar();
    }

    /*
        Nombre: filtrar
        Argumentos: -
        Objetivo: Filtrar los datos de le tabla tomando como referencia el valor ingresado como texto de busqueda.
        Retorno: -
     */
    @Override
    public void filtrar() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();

        filteredData.setPredicate(parada -> {
            if (textoBusqueda.isEmpty()) {
                return true;
            }

            boolean coincideCodigo = String.valueOf(parada.getCodigo()).contains(textoBusqueda);

            boolean coincideNombre = parada.getNombreParada() != null &&
                    parada.getNombreParada().toLowerCase().contains(textoBusqueda);

            boolean coincideDireccion = parada.getUbicacion() != null &&
                    parada.getUbicacion().toLowerCase().contains(textoBusqueda);

            boolean coincideTipo = parada.getTipo() != null &&
                    parada.getTipo().getTipo() != null &&
                    parada.getTipo().getTipo().toLowerCase().contains(textoBusqueda);

            return coincideCodigo || coincideNombre || coincideDireccion || coincideTipo;
        });
    }

    /*
        Nombre: configurarColumnas
        Argumentos: -
        Objetivo: Configurar las columnas de los campos de la tabla para asociarlas a un una propiedad.
        Retorno: -
     */
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
            } catch (Exception ignored) {}

            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });
    }

    /*
        Nombre: mostrarAlerta
        Argumentos:
            (String) titulo: Representa el titulo de la alerta.
            (String) mensaje: Representa el mensaje de la alerta.
        Objetivo: Mostrar las alertas informativas asociadas al apartado.
        Retorno: -
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /*
        Nombre: procesarEliminacion
        Argumentos:
            (Parada) paradaSeleccionada: Representa la parada que se va a eliminar.
        Objetivo: Procesar la eliminación de una parada y confirmarla.
        Retorno: -
     */
    private void procesarEliminacion(Parada paradaSeleccionada){
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

    }
}