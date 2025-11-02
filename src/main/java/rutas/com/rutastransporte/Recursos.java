package rutas.com.rutastransporte;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.Objects;

public class Recursos {
    private static final Image icono = new Image(Objects.requireNonNull(Recursos.class.getResourceAsStream("/rutas/com/rutastransporte/imagenes/mapa.png")));
    private final static Dimension registroDim = new Dimension(840,570);

    public static Image getIcono(){
        return icono;
    }

    public static Dimension getDimRegistro(){
        return registroDim;
    }
}
