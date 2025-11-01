package rutas.com.rutastransporte.Controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Servicios.Registro;
import rutas.com.rutastransporte.Servicios.ParadasDAO;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;
import rutas.com.rutastransporte.Utilidades.Modalidad;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.TipoParada;
import rutas.com.rutastransporte.Servicios.SistemaTransporte;

public class RegistroParadaController implements Registro {
    private ParadasDAO servicioParadas = new ParadasDAO();

    private Parada parada;

    private Modalidad modalidad = Modalidad.INSERTAR;

    private Stage pantalla;

    private AlertFactory alertFactory = new AlertFactory();

    @FXML
    private Button btnRealizar;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDireccion;

    @FXML
    private ComboBox<TipoParada> cbxTipoParada;

    @FXML
    private ImageView imgTransporte;

    @FXML
    private ImageView imgRealizar;

    public void setParada(Parada parada){
        this.parada = parada;
    }

    public void setModalidad(Modalidad modalidad){
        this.modalidad = modalidad;
    }

    public void setStage(Stage pantalla){
        this.pantalla = pantalla;
    }

    public void setTipos(){
        ObservableList<TipoParada> lista = FXCollections.observableArrayList(TipoParada.values());
        cbxTipoParada.setItems(lista);
    }

    public void initialize(){
        setTipos();
    }

    public void btnRealizarClick(ActionEvent e){
        if(validar()){
            Alerta alerta = alertFactory.obtenerAlerta(Alert.AlertType.INFORMATION);
            if(modalidad == Modalidad.INSERTAR){
                Parada parada = new Parada(txtCodigo.getText(),txtNombre.getText(),cbxTipoParada.getSelectionModel().getSelectedItem(),txtDireccion.getText());
                servicioParadas.insertar(parada);
                alerta.crearAlerta("Parada Insertada Exitosamente.","Registro Insertado.").showAndWait();
                limpiar();
                txtCodigo.setText(String.valueOf(SistemaTransporte.genCodigoParada));
            }
            else{
                parada.setNombreParada(txtNombre.getText());
                parada.setTipo(cbxTipoParada.getSelectionModel().getSelectedItem());
                parada.setUbicacion(txtDireccion.getText());
                servicioParadas.actualizar(parada);
                alerta.crearAlerta("Parada Modificada Exitosamente.","Registro Modificado.").showAndWait();

            }
        }
    }

    public void btnCerrarClick(ActionEvent e){
        pantalla.close();
    }

    public void btnLimpiarClick(ActionEvent e){
        limpiar();
    }

    public void cbxTipoParadaSeleccionado(ActionEvent e){
        Image img = new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + cbxTipoParada.getSelectionModel().selectedItemProperty().getValue().getImagen()));
        imgTransporte.setImage(img);
    }

    void cargarDatos(){
        txtNombre.requestFocus();
        if(modalidad.equals(Modalidad.ACTUALIZAR)){
            txtCodigo.setText(parada.getCodigo());
            txtDireccion.setText(parada.getUbicacion());
            txtNombre.setText(parada.getNombreParada());
            cbxTipoParada.setValue(parada.getTipo());
            btnRealizar.setText("Actualizar");
            imgRealizar.setImage(new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/editar.png")));
        }
        else{
            txtCodigo.setText(String.valueOf(SistemaTransporte.genCodigoParada));
            cbxTipoParada.setValue(TipoParada.BUS);
        }

        cbxTipoParadaSeleccionado(null);
    }

    @Override
    public boolean validar() {
        Alerta alerta = alertFactory.obtenerAlerta(Alert.AlertType.CONFIRMATION);

        if(txtNombre.getText().trim().isEmpty()){
            alerta.crearAlerta("El campo de nombre es obligatorio.","Registro Obligatorio.").showAndWait();
            return false;
        }
        else if(txtDireccion.getText().trim().isEmpty()){
            alerta.crearAlerta("El campo de dirección es obligatorio.","Registro Obligatorio.").showAndWait();
            return false;
        }

        return true;
    }

    @Override
    public void limpiar() {
        txtDireccion.setText("");
        txtNombre.setText("");
        cbxTipoParada.setValue(TipoParada.BUS);
    }
}
