package rutas.com.rutastransporte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class MenuPrincipal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuPrincipal.class.getResource("MenuPrincipal-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sistema de Gestión de Transporte Público");
        stage.setScene(scene);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stage.setWidth(screenSize.getWidth() - 20);
        stage.setHeight(screenSize.getHeight() - 50);

       /* Image icono = new  Image(getClass().getResourceAsStream("@/magenes/mapa.png"));
        stage.getIcons().add(icono);*/
        stage.show();
    }
}
