package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Registro;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Modelos.RutaBuilder;
import rutas.com.rutastransporte.RecursosVisuales;
import rutas.com.rutastransporte.Repositorio.SistemaTransporte;
import rutas.com.rutastransporte.Servicios.RutasDAO;
import rutas.com.rutastransporte.Utilidades.Alertas.AlertFactory;
import rutas.com.rutastransporte.Utilidades.Alertas.Alerta;
import rutas.com.rutastransporte.Utilidades.Modalidad;

public class RegistroRutaController implements Registro {
    private AlertFactory alertfactory = new AlertFactory();

    private RutasDAO servicioRutas = new RutasDAO();

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
    private Button btnRealizar;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cbxOrigen;

    @FXML
    private ComboBox<String> cbxDestino;

    @FXML
    private Spinner<Integer> spnKM;

    @FXML
    private Spinner<Integer> spnM;

    @FXML
    private Spinner<Integer> spnHoras;

    @FXML
    private Spinner<Integer> spnMinutos;

    @FXML
    private Spinner<Double> spnCosto;

    @FXML
    private Spinner<Integer> spnTrasbordos;

    @FXML
    private ImageView imgRealizar;

    @Override
    public void setModalidad(Modalidad modalidad){
        this.modalidad = modalidad;
    }

    private void cargarParadas(){
        for(Parada parada : SistemaTransporte.getSistemaTransporte().getParadas()){
            cbxOrigen.getItems().add(parada.getNombreParada());
            cbxDestino.getItems().add(parada.getNombreParada());
        }
    }

    @FXML
    public void initialize(){
        cargarDatos();
    }

    @Override
    public void cargarDatos() {
        cargarParadas();
        RecursosVisuales.configurarSpinnerNumerico(spnHoras,0,24,0);
        RecursosVisuales.configurarSpinnerNumerico(spnM,0,999,0);
        RecursosVisuales.configurarSpinnerNumerico(spnKM,0,100,0);
        RecursosVisuales.configurarSpinnerNumerico(spnMinutos,0,59,0);
        RecursosVisuales.configurarSpinnerFlotante(spnCosto,0,1000,10);
        RecursosVisuales.configurarSpinnerNumerico(spnTrasbordos,1,100,1);

        if(modalidad == Modalidad.ACTUALIZAR){
            txtNombre.setText(ruta.getNombre());
            cbxDestino.getSelectionModel().select(ruta.getDestino().getNombreParada());
            cbxOrigen.getSelectionModel().select(ruta.getOrigen().getNombreParada());
            spnTrasbordos.getValueFactory().setValue(ruta.getTrasbordos());
            spnHoras.getValueFactory().setValue(ruta.getHoras());
            spnMinutos.getValueFactory().setValue(ruta.getMinutos());
            spnCosto.getValueFactory().setValue((double)ruta.getCosto());
            spnM.getValueFactory().setValue(ruta.getMetros());
            spnKM.getValueFactory().setValue(ruta.getKilometros());
            btnRealizar.setText("Actualizar");
            imgRealizar.setImage(new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/editar.png")));
        }

    }

    public void btnRealizarClick(ActionEvent e){
        if(validar()){
            Alerta alerta = alertfactory.obtenerAlerta(Alert.AlertType.INFORMATION);
            if(modalidad == Modalidad.INSERTAR){
                RutaBuilder rb = new RutaBuilder();
                rb.setNombre(txtNombre.getText());
                rb.setOrigen(buscarParadaByNombre(cbxOrigen.getSelectionModel().getSelectedItem()));
                rb.setDestino(buscarParadaByNombre(cbxDestino.getSelectionModel().getSelectedItem()));
                rb.setCosto(Float.parseFloat(spnCosto.getValue().toString()));
                rb.setTiempo(Ruta.calcularTiempo(spnHoras.getValue(), spnMinutos.getValue()));
                rb.setDistancia(Ruta.calcularDistancia(spnKM.getValue(),spnM.getValue()));
                rb.setTrasbordos(spnTrasbordos.getValue());
                servicioRutas.insertar(rb.construir());
                alerta.crearAlerta("Ruta Insertada Exitosamente.","Registro Insertado.").show();
                limpiar();
            }
            else{
                ruta.setNombre(txtNombre.getText());
                ruta.setDestino(buscarParadaByNombre(cbxDestino.getSelectionModel().getSelectedItem()));
                ruta.setOrigen(buscarParadaByNombre(cbxOrigen.getValue()));
                ruta.setCosto(Float.parseFloat(spnCosto.getValue().toString()));
                ruta.setDistancia(Ruta.calcularDistancia(spnKM.getValue(), spnM.getValue()));
                ruta.setTiempo(Ruta.calcularTiempo(spnHoras.getValue(), spnMinutos.getValue()));
                ruta.setTrasbordos(spnTrasbordos.getValue());
                servicioRutas.actualizar(ruta);
                alerta.crearAlerta("Ruta Modificada Exitosamente.","Registro Modificado.").show();
            }
        }
    }

    public void btnLimpiarClick(ActionEvent e){
        limpiar();
    }

    public void btnCerrarClick(ActionEvent e){
        stage.close();
    }

    @Override
    public boolean validar() {
        if(txtNombre.getText().isEmpty()){
            return crearAlerta("El campo de nombre es obligatorio.");
        }
        else if(cbxDestino.getSelectionModel().getSelectedItem() == null){
            return crearAlerta("El campo de destino es obligatorio.");
        }
        else if(cbxOrigen.getSelectionModel().getSelectedItem() == null){
            return crearAlerta("El campo de origen es obligatorio.");
        }
        else if(spnKM.getValue().equals(0) && spnM.getValue().equals(0)){
            return crearAlerta("Los registros de distancia estan vacios, ingrese un valor.");
        }
        else if(spnHoras.getValue().equals(0) && spnMinutos.getValue().equals(0)){
            return crearAlerta("Los registros de tiempo estan vacios, ingrese un valor.");
        }
        else if(cbxDestino.getSelectionModel().getSelectedItem().equals(cbxOrigen.getSelectionModel().getSelectedItem())){
            return crearAlerta("El destino no puede ser igual que el origen, cambia alguno de estos valores.");
        }

        return true;
    }

    private boolean crearAlerta(String mensaje){
        Alerta alerta = alertfactory.obtenerAlerta(Alert.AlertType.WARNING);
        alerta.crearAlerta(mensaje,"Advertencia de Registro.").show();
        return false;
    }

    @Override
    public void limpiar() {
        txtNombre.setText("");
        spnM.getValueFactory().setValue(0);
        spnHoras.getValueFactory().setValue(0);
        spnMinutos.getValueFactory().setValue(0);
        spnKM.getValueFactory().setValue(0);
        spnCosto.getValueFactory().setValue((double)10);
        spnTrasbordos.getValueFactory().setValue(1);
    }

    public Parada buscarParadaByNombre(String nombre){
        for(Parada parada : SistemaTransporte.getSistemaTransporte().getParadas()){
            if(parada.getNombreParada().equals(nombre)){
                return parada;
            }
        }

        return null;
    }
}
