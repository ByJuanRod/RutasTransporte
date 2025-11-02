package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.Modelos.Registro;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Modelos.Vista;
import rutas.com.rutastransporte.StageBuilder;
import rutas.com.rutastransporte.Utilidades.Modalidad;

public class RutasViewController implements Vista<Ruta> {

    @FXML
    private TableView<Ruta> tblRutas;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnActualizar;

    public void btnEliminarClick(ActionEvent e){

    }

    public void btnActualizarClick(ActionEvent e){

    }

    public void btnInsertarClick(ActionEvent e){
        crearPantalla("Insertar Ruta",Modalidad.INSERTAR,null);
    }

    public void txtBuscarKeyPressed(KeyEvent e){

    }

    @Override
    public void crearPantalla(String titulo, Modalidad modalidad, Ruta ruta) {
        StageBuilder sb = new StageBuilder();
        sb.setModalidad(Modality.APPLICATION_MODAL);
        sb.setTitulo(titulo);

        RegistroRutaController controlador = (RegistroRutaController) sb.setContenido("RegistroRuta");
        controlador.setModalidad(modalidad);
        controlador.setRuta(ruta);

        controlador.cargarDatos();
        Stage st = sb.construir();
        controlador.setStage(st);

        st.show();
    }
}
