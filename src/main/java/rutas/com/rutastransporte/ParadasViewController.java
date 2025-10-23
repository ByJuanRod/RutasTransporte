package rutas.com.rutastransporte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.logica.Parada;

import java.awt.*;
import java.io.IOException;

public class ParadasViewController {

    @FXML
    private TableView<Parada> tblParadas;

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
        StageBuilder sb = new StageBuilder();
        sb.setModalidad(Modality.APPLICATION_MODAL);
        sb.setTitulo("Insertar Parada");
        Stage st = sb.construir();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroParada.fxml"));
        try{
            Scene contenido = new Scene(fxmlLoader.load());
            st.setScene(contenido);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        st.show();
    }

    public void txtBuscarKeyPressed(KeyEvent e){

    }
}
