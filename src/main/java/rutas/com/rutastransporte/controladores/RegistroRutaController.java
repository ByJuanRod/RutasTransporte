package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.RutaBuilder;
import rutas.com.rutastransporte.utilidades.RecursosVisuales;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;
import rutas.com.rutastransporte.servicios.RutasDAO;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;
import rutas.com.rutastransporte.utilidades.alertas.Alerta;
import rutas.com.rutastransporte.utilidades.Modalidad;

import java.util.Objects;

public class RegistroRutaController implements Registro {
    private final AlertFactory alertfactory = new AlertFactory();

    private final RutasDAO servicioRutas = new RutasDAO();

    private Modalidad modalidad = Modalidad.INSERTAR;

    private Ruta ruta;

    private Stage stage;

    public void setRuta(Ruta ruta){
        this.ruta = ruta;
    }

    @Override
    public void setStage(Stage st){
        this.stage = st;
    }

    @FXML
    private Button btnRealizar;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Parada> cbxOrigen;

    @FXML
    private ComboBox<Parada> cbxDestino;

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

    @FXML
    private ImageView imgTransporte;

    @Override
    public void setModalidad(Modalidad modalidad){
        this.modalidad = modalidad;
    }

    @FXML
    public void initialize(){
        cargarDatos();
    }

    public void cbxOrigenChange(){
        imgTransporte.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + cbxOrigen.getSelectionModel().getSelectedItem().getTipo().getImagen()))));
    }

    public void btnRealizarClick(){
        if(validar()){
            Alerta alerta = alertfactory.obtenerAlerta(Alert.AlertType.INFORMATION);
            if(modalidad == Modalidad.INSERTAR){
                RutaBuilder rb = getRutaBuilder();
                if(servicioRutas.insertar(rb.construir())){
                    alerta.crearAlerta("Ruta Insertada Exitosamente.","Registro Insertado.").show();
                    limpiar();
                }
                else{
                    alertfactory.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("No se logró insertar la ruta, intente nuevamente.").show();
                }
            }
            else{
                aplicarNuevosValores();
                if(servicioRutas.actualizar(ruta)){
                    alerta.crearAlerta("Ruta Modificada Exitosamente.","Registro Modificado.").show();
                }
                else{
                    alertfactory.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("No se logró actualizar la ruta, intente nuevamente.").show();
                }
            }
        }
    }

    public void btnLimpiarClick(){
        limpiar();
    }

    public void btnCerrarClick(){
        stage.close();
    }

    public void btnAutomaticoClick(){
        if(cbxOrigen.getSelectionModel().getSelectedItem() != null && cbxDestino.getSelectionModel().getSelectedItem() != null){
            txtNombre.setText("Ruta " + cbxOrigen.getSelectionModel().getSelectedItem() + "-" + cbxDestino.getSelectionModel().getSelectedItem());
        }
        else{
            alertfactory.obtenerAlerta(Alert.AlertType.WARNING).crearAlerta("El origen y destino deben ser paradas existentes para asignar el automático.").show();
        }
    }

    /*
        Nombre: cargarParadas
        Argumentos: -
        Objetivo: Cargar todas las paradas y ubicarlas como opciones de destino y origen.
        Retorno: -
     */
    private void cargarParadas(){
        cbxDestino.getItems().clear();
        cbxOrigen.getItems().clear();
        for(Parada parada : SistemaTransporte.getSistemaTransporte().getParadas()){
            cbxOrigen.getItems().add(parada);
            cbxDestino.getItems().add(parada);
        }
    }

    /*
        Nombre: validar
        Argumentos: -
        Objetivo: Validar que los campos obligatorios tengan un valor correcto.
        Retorno: (boolean) Retorna true si todos los campos son correctos.
                           Retorna false si algun registro obligatorio no tiene el valor indicado.
     */
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

    /*
        Nombre: crearAlerta
        Argumentos:
            (String) mensaje: Representa el mensaje de la alerta.
        Objetivo: Mostrar las alertas de advertencias.
        Retorno: (boolean) Retorna el resultado de una advertencia (Por defecto es false)
     */
    private boolean crearAlerta(String mensaje){
        Alerta alerta = alertfactory.obtenerAlerta(Alert.AlertType.WARNING);
        alerta.crearAlerta(mensaje,"Advertencia de Registro.").show();
        return false;
    }

    /*
        Nombre: limpiar
        Argumentos: -
        Objetivo: Limpiar los valores de los registros.
        Retorno: -
     */
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

    /*
        Nombre: getRutaBuilder
        Argumentos: -
        Objetivo: Facilitar la creación de objetos de rutas.
        Retorno: (RutaBuilder) Retorna el builder de las rutas con los datos apropiados.
     */
    private RutaBuilder getRutaBuilder() {
        return new RutaBuilder()
                .setNombre(txtNombre.getText())
                .setOrigen(cbxOrigen.getSelectionModel().getSelectedItem())
                .setDestino(cbxDestino.getSelectionModel().getSelectedItem())
                .setCosto(Float.parseFloat(spnCosto.getValue().toString()))
                .setTiempo(Ruta.calcularTiempo(spnHoras.getValue(), spnMinutos.getValue()))
                .setDistancia(Ruta.calcularDistancia(spnKM.getValue(),spnM.getValue()))
                .setTrasbordos(spnTrasbordos.getValue());
    }

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar los datos necesarios para el funcionamiento de los registros.
        Retorno: -
     */
    @Override
    public void cargarDatos() {
        cbxOrigen.requestFocus();
        cargarParadas();
        RecursosVisuales.configurarSpinnerNumerico(spnHoras,0,24,0);
        RecursosVisuales.configurarSpinnerNumerico(spnM,0,999,0);
        RecursosVisuales.configurarSpinnerNumerico(spnKM,0,100,0);
        RecursosVisuales.configurarSpinnerNumerico(spnMinutos,0,59,0);
        RecursosVisuales.configurarSpinnerDecimal(spnCosto,0,1000,10);
        RecursosVisuales.configurarSpinnerNumerico(spnTrasbordos,1,100,1);

        if(modalidad == Modalidad.ACTUALIZAR){
            rellenarCampos();
            aplicarEsteticos(Modalidad.ACTUALIZAR);
        }
        else{
            aplicarEsteticos(Modalidad.INSERTAR);
        }
    }

    /*
        Nombre: aplicarEsteticos
        Argumentos:
            (Modalidad) modalidad: Representa la modalidad en la que se inicio el formulario.
        Objetivo: Aplicar los elementos esteticos al formulario.
        Retorno: -
     */
    @Override
    public void aplicarEsteticos(Modalidad modalidad){
        if(modalidad == Modalidad.ACTUALIZAR){
            imgTransporte.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + ruta.getOrigen().getTipo().getImagen()))));
            btnRealizar.setText("Actualizar");
            imgRealizar.translateXProperty().setValue(-20);
            imgRealizar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/editar.png"))));
        }
        else{
            cbxOrigen.getSelectionModel().select(cbxOrigen.getItems().getFirst());
            imgTransporte.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + cbxOrigen.getSelectionModel().getSelectedItem().getTipo().getImagen()))));
            cbxDestino.getSelectionModel().select(cbxDestino.getItems().get(1));
        }
    }

    /*
        Nombre: rellenarCampos
        Argumentos: -
        Objetivo: Rellenar los campos del formulario con los valores necesarios
     */
    @Override
    public void rellenarCampos(){
        txtNombre.setText(ruta.getNombre());
        cbxDestino.getSelectionModel().select(ruta.getDestino());
        cbxOrigen.getSelectionModel().select(ruta.getOrigen());
        spnTrasbordos.getValueFactory().setValue(ruta.getTrasbordos());
        spnHoras.getValueFactory().setValue(ruta.getHoras());
        spnMinutos.getValueFactory().setValue(ruta.getMinutos());
        spnCosto.getValueFactory().setValue((double)ruta.getCosto());
        spnM.getValueFactory().setValue(ruta.getMetros());
        spnKM.getValueFactory().setValue(ruta.getKilometros());
    }

    @Override
    public void aplicarNuevosValores(){
        ruta.setNombre(txtNombre.getText());
        ruta.setDestino(cbxDestino.getSelectionModel().getSelectedItem());
        ruta.setOrigen(cbxOrigen.getValue());
        ruta.setCosto(Float.parseFloat(spnCosto.getValue().toString()));
        ruta.setDistancia(Ruta.calcularDistancia(spnKM.getValue(), spnM.getValue()));
        ruta.setTiempo(Ruta.calcularTiempo(spnHoras.getValue(), spnMinutos.getValue()));
        ruta.setTrasbordos(spnTrasbordos.getValue());
    }
}
