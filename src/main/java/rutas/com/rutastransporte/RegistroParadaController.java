package rutas.com.rutastransporte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import rutas.com.rutastransporte.logica.Parada;
import rutas.com.rutastransporte.logica.TipoParada;

public class RegistroParadaController {

    private Parada parada;

    private String modalidad;

    @FXML
    private Button btnRealizar;

    @FXML
    private Button btnCerrar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDireccion;

    @FXML
    private ChoiceBox<TipoParada> cbxTipoParada;

    @FXML
    private ImageView imgTransporte;

    public void initialize(){

    }

    public void btnRealizarClick(ActionEvent e){

    }

    public void btnCerrarClick(ActionEvent e){

    }

    public void btnLimpiarClick(ActionEvent e){

    }

    public void cbxTipoParadaSeleccionado(ActionEvent e){

    }



}
