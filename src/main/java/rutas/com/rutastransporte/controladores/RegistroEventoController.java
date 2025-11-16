package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.RecursosVisuales;
import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.servicios.ServicioEventos;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;
import java.util.List;
import java.util.Objects;

public class RegistroEventoController {

    private Stage stage;

    @FXML
    private ComboBox<Ruta> cbxRuta;

    @FXML
    private Spinner<Integer> spnDuracion;

    @FXML
    private ComboBox<TipoEvento> cbxTipo;

    @FXML
    private ImageView imgVista;

    private final AlertFactory af = new AlertFactory();

    public void cargarDatos(){
        List<Ruta> rutas = ServicioEventos.getInstancia().getRutasSinEventosActivos();
        if(rutas.isEmpty()){
            af.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("No se puede insertar un nuevo evento ya que no existen rutas o todas las existentes ya tienen un evento asociado.","Error.").show();
            stage.close();
            return;
        }
        cbxRuta.getItems().addAll(rutas);
        cbxTipo.getItems().addAll(TipoEvento.values());
        cbxTipo.getItems().remove(TipoEvento.NORMAL);
        RecursosVisuales.configurarSpinnerNumerico(spnDuracion,1,60,1);
        btnLimpiarClick();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void btnCerrarClick(){
        stage.close();
    }

    public void btnRealizarClick(){
        EventoRuta ev = new EventoRuta(cbxRuta.getValue(),cbxTipo.getValue(),spnDuracion.getValue());
        ServicioEventos.getInstancia().insertar(ev);
        af.obtenerAlerta(Alert.AlertType.INFORMATION).crearAlerta("Evento Insertado Exitosamente.","Inserción Correcta.").show();
    }

    public void btnLimpiarClick(){
        cbxTipo.getSelectionModel().select(0);
        cbxRuta.getSelectionModel().select(0);
        spnDuracion.getValueFactory().setValue(1);
    }

    public void tipoSelChange(){
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + cbxTipo.getSelectionModel().getSelectedItem().getImagen())));
        imgVista.setImage(img);
    }

}
