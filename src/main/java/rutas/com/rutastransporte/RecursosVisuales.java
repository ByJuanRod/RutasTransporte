package rutas.com.rutastransporte;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.Objects;

public class RecursosVisuales {
    private static final Image icono = new Image(Objects.requireNonNull(RecursosVisuales.class.getResourceAsStream("/rutas/com/rutastransporte/imagenes/mapa.png")));
    private final static Dimension registroDim = new Dimension(840,570);

    public static Image getIcono(){
        return icono;
    }

    public static Dimension getDimRegistro(){
        return registroDim;
    }

    public static void configurarSpinnerNumerico(Spinner<Integer> spinner, int minimo, int maximo, int inicial){
        spinner.setEditable(true);
        SpinnerValueFactory.IntegerSpinnerValueFactory factory =  new SpinnerValueFactory.IntegerSpinnerValueFactory(minimo,maximo ,inicial);
        spinner.setValueFactory(factory);
    }

    public static void configurarSpinnerFlotante(Spinner<Double> spinner, int minimo, int maximo, int inicial){
        spinner.setEditable(true);
        SpinnerValueFactory.DoubleSpinnerValueFactory factory =  new SpinnerValueFactory.DoubleSpinnerValueFactory(minimo,maximo ,inicial);
        spinner.setValueFactory(factory);
    }
}
