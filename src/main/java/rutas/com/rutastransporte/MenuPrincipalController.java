package rutas.com.rutastransporte;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import rutas.com.rutastransporte.utilidades.Colores;

public class MenuPrincipalController {

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
    }

    public void btnRutasClick(ActionEvent e){
        cambiarSeleccion(btnRutas);
    }

    public void btnMapaClick(ActionEvent e){
        cambiarSeleccion(btnMapa);
    }

    public void btnPrincipalClick(ActionEvent e){
        cambiarSeleccion(btnPrincipal);
    }



    @FXML
    public void initialize(){
        try{
            AnchorPane contenido = FXMLLoader.load(getClass().getResource("Principal-view.fxml"));
            pnlContenedor.setCenter(contenido);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
