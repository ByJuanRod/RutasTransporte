package rutas.com.rutastransporte.controladores;

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
import rutas.com.rutastransporte.modelos.ParadaBuilder;
import rutas.com.rutastransporte.modelos.Registro;
import rutas.com.rutastransporte.servicios.ParadasDAO;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;
import rutas.com.rutastransporte.utilidades.alertas.Alerta;
import rutas.com.rutastransporte.utilidades.Modalidad;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.TipoParada;

public class RegistroParadaController implements Registro {
    private ParadasDAO servicioParadas = new ParadasDAO();

    private Parada parada;

    private Modalidad modalidad = Modalidad.INSERTAR;

    private Stage pantalla;

    private AlertFactory alertFactory = new AlertFactory();

    @FXML
    private Button btnRealizar;

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

    @Override
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
                ParadaBuilder parada = new ParadaBuilder();
                parada.setNombreParada(txtNombre.getText());
                parada.setTipo(cbxTipoParada.getSelectionModel().getSelectedItem());
                parada.setUbicacion(txtDireccion.getText());
                servicioParadas.insertar(parada.construir());
                alerta.crearAlerta("Parada Insertada Exitosamente.","Registro Insertado.").show();
                limpiar();
            }
            else{
                parada.setNombreParada(txtNombre.getText());
                parada.setTipo(cbxTipoParada.getSelectionModel().getSelectedItem());
                parada.setUbicacion(txtDireccion.getText());
                servicioParadas.actualizar(parada);
                alerta.crearAlerta("Parada Modificada Exitosamente.","Registro Modificado.").show();
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

    @Override
    public void cargarDatos(){
        txtNombre.requestFocus();
        if(modalidad.equals(Modalidad.ACTUALIZAR)){
            txtDireccion.setText(parada.getUbicacion());
            txtNombre.setText(parada.getNombreParada());
            cbxTipoParada.setValue(parada.getTipo());
            btnRealizar.setText("Actualizar");
            imgRealizar.setImage(new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/editar.png")));
        }
        else{
            cbxTipoParada.setValue(TipoParada.BUS);
        }

        cbxTipoParadaSeleccionado(null);
    }

    @Override
    public boolean validar() {
        Alerta alerta = alertFactory.obtenerAlerta(Alert.AlertType.WARNING);

        if(txtNombre.getText().trim().isEmpty()){
            alerta.crearAlerta("El campo de nombre es obligatorio.","Registro Obligatorio.").show();
            return false;
        }
        else if(txtDireccion.getText().trim().isEmpty()){
            alerta.crearAlerta("El campo de dirección es obligatorio.","Registro Obligatorio.").show();
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
