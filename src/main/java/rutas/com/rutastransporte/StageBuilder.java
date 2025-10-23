package rutas.com.rutastransporte;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rutas.com.rutastransporte.logica.Builder;

import java.awt.*;

public class StageBuilder implements Builder<Stage> {
    private final Stage stage;

    public StageBuilder() {
        stage = new Stage();
        stage.setResizable(false);
        javafx.scene.image.Image icono = new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/mapa.png"));
        stage.getIcons().add(icono);
        stage.setWidth(840);
        stage.setHeight(570);
    }

    public void setTitulo(String titulo){
        stage.setTitle(titulo);
    }

    public void setSize(Dimension size){
        stage.setMinHeight(size.getHeight());
        stage.setMinWidth(size.getWidth());
    }

    public void setModalidad(Modality modalidad){
        stage.initModality(modalidad);
    }

    @Override
    public Stage construir(){
        return stage;
    }



}
