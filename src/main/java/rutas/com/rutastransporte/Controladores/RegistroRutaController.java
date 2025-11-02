package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Registro;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Utilidades.Modalidad;

public class RegistroRutaController implements Registro {
    private Modalidad modalidad = Modalidad.INSERTAR;

    private Ruta ruta;

    private Stage stage;

    public void setRuta(Ruta ruta){
        this.ruta = ruta;
    }

    public void setStage(Stage st){
        this.stage = st;
    }

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Parada> cbxOrigen;

    @FXML
    private ComboBox<Parada> cbxDestino;

    @FXML
    private Spinner<Integer> spnKM;

    @FXML
    private Spinner<Integer> snpM;

    @FXML
    private Spinner<Integer> snpHoras;

    @FXML
    private Spinner<Integer> snpMinutos;

    @FXML
    private Spinner<Float> spnCosto;

    @FXML
    private ImageView imgRealizar;

    @Override
    public void setModalidad(Modalidad modalidad){
        this.modalidad = modalidad;
    }

    @Override
    public void cargarDatos() {

    }

    public void btnRealizarClick(ActionEvent e){

    }

    public void btnLimpiarClick(ActionEvent e){
        limpiar();
    }

    public void btnCerrarClick(ActionEvent e){

    }

    @Override
    public boolean validar() {
        return false;
    }

    @Override
    public void limpiar() {

    }
}
