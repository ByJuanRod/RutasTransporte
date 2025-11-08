package rutas.com.rutastransporte.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;
import rutas.com.rutastransporte.utilidades.alertas.Alerta;
import rutas.com.rutastransporte.utilidades.Colores;

public class MenuPrincipalController {
    private AlertFactory alert = new AlertFactory();

    @FXML
    private BorderPane pnlContenedor;

    @FXML
    private Button btnPrincipal;

    @FXML
    private Button btnRutas;

    @FXML
    private Button btnMapa;

    @FXML
    private Button btnParadas;

    @FXML
    private VBox pnlMenu;

    private void cambiarSeleccion(Button btnSeleccionado){
        for(Node node : pnlMenu.getChildren()){
            if(node instanceof Button){
                if(((Button) node).getText().equals(btnSeleccionado.getText())){
                    node.setStyle("-fx-background-color: " + Colores.DECORATIVOS.getColor());
                }
                else{
                    node.setStyle("-fx-background-color: " + Colores.ENFASIS.getColor());
                }
            }
        }

    }

    public void btnParadasClick(ActionEvent e){
        cambiarSeleccion(btnParadas);
        cambiarPanel("Paradas");
    }

    public void btnRutasClick(ActionEvent e){
        cambiarSeleccion(btnRutas);
        cambiarPanel("Rutas");
    }

    public void btnMapaClick(ActionEvent e){
        cambiarSeleccion(btnMapa);
        cambiarPanel("Mapa");
    }

    public void btnPrincipalClick(ActionEvent e){
        cambiarSeleccion(btnPrincipal);
        cambiarPanel("Principal");
    }

    @FXML
    public void initialize(){
        cambiarPanel("Principal");
    }

    public void cambiarPanel(String nombreSeccion){
        try{
            pnlContenedor.getChildren().clear();
            AnchorPane contenido = FXMLLoader.load(getClass().getResource("/rutas/com/rutastransporte/" + nombreSeccion + "-view.fxml"));
            pnlContenedor.setCenter(contenido);
        }
        catch (Exception e){
            e.printStackTrace();
            Alerta alt = alert.obtenerAlerta(Alert.AlertType.ERROR);
            alt.crearAlerta("Error al cargar el apartado seleccionado.","Error").show();
        }
    }

}
