package rutas.com.rutastransporte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuPrincipalController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
