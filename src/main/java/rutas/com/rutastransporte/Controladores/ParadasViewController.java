package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Vista;
import rutas.com.rutastransporte.StageBuilder;
import rutas.com.rutastransporte.Utilidades.Modalidad;
import rutas.com.rutastransporte.Modelos.Parada;

public class ParadasViewController implements Vista<Parada> {

    @FXML
    private static TableView<Parada> tblParadas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnActualizar;

    @FXML
    private TableColumn<Parada, String> colCodigo;

    @FXML
    private TableColumn<Parada, String> colNombre;

    @FXML
    private TableColumn<Parada, String> colDireccion;

    @FXML
    private TableColumn<Parada, String> colTipo;

    @FXML
    private TableColumn<Parada, String> colImg;

    public void btnEliminarClick(ActionEvent e){

    }

    public void btnActualizarClick(ActionEvent e){
        Parada parada = tblParadas.getSelectionModel().getSelectedItem();
        crearPantalla("Modificar Parada", Modalidad.ACTUALIZAR,parada);
    }

    public void btnInsertarClick(ActionEvent e){
        crearPantalla("Insertar Parada", Modalidad.INSERTAR, null);
    }

    public void txtBuscarKeyPressed(KeyEvent e){

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

        st.show();
    }
}
