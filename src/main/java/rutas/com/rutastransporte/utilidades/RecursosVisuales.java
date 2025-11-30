package rutas.com.rutastransporte.utilidades;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import java.awt.*;
import java.util.Objects;

/*
    Nombre: RecursosVisuales
    Tipo: Clase
    Objetivo: Almacenar los recursos que el programa utiliza frecuentemente.
*/
public class RecursosVisuales {
    private static final Image icono = new Image(Objects.requireNonNull(RecursosVisuales.class.getResourceAsStream("/rutas/com/rutastransporte/imagenes/mapa.png")));
    private static final Image tieneCriterio = new Image(Objects.requireNonNull(RecursosVisuales.class.getResourceAsStream("/rutas/com/rutastransporte/imagenes/si.png")));
    private final static Dimension registroDim = new Dimension(840,570);

    public static Image getTieneCriterio(){
        return tieneCriterio;
    }

    public static Image getIcono(){
        return icono;
    }

    public static Dimension getDimRegistro(){
        return registroDim;
    }

    /*
        Nombre: ConfigurarSpinnerNumerico
        Argumentos:
            (Spinner<Integer>) spinner: Representa el spinner al que se le aplicaran las propiedades.
            (int) minimo: Representa el valor minimo que puede tomar el spinner
            (int) maximo: Representa el valor maximo que puede tomar el spinner
            (int) inicial: Representa el valor inicial que tiene el spinner.
        Objetivo: Aplicarle las propiedades base a un spinner.
        Retorno: -
     */
    public static void configurarSpinnerNumerico(Spinner<Integer> spinner, int minimo, int maximo, int inicial){
        spinner.setEditable(true);
        SpinnerValueFactory.IntegerSpinnerValueFactory factory =  new SpinnerValueFactory.IntegerSpinnerValueFactory(minimo,maximo ,inicial);
        spinner.setValueFactory(factory);
    }

    /*
    Nombre: ConfigurarSpinnerDecimal
    Argumentos:
        (Spinner<Double>) spinner: Representa el spinner al que se le aplicaran las propiedades.
        (int) minimo: Representa el valor minimo que puede tomar el spinner
        (int) maximo: Representa el valor maximo que puede tomar el spinner
        (int) inicial: Representa el valor inicial que tiene el spinner.
    Objetivo: Aplicarle las propiedades base a un spinner.
    Retorno: -
 */
    public static void configurarSpinnerDecimal(Spinner<Double> spinner, int minimo, int maximo, int inicial){
        spinner.setEditable(true);
        SpinnerValueFactory.DoubleSpinnerValueFactory factory =  new SpinnerValueFactory.DoubleSpinnerValueFactory(minimo,maximo ,inicial);
        spinner.setValueFactory(factory);
    }

    /*
    Nombre: AjustarAnchoColumnas
    Argumentos:
        (TableView<?>) tabla: Representa la tabla a la que se le aplicara el ancho en cada columna.
    Objetivo: Aplicar el ancho correspoondiente a todas las columnas de una tabla.
    Retorno: -
 */
    public static void ajustarAnchoColumnas(TableView<?> tabla) {
        double anchoTabla = tabla.getWidth();
        if (anchoTabla > 0) {
            int numColumnas = tabla.getColumns().size();
            double anchoPorColumna;
            if(numColumnas == 4){
                anchoPorColumna = (anchoTabla / numColumnas) - 4;
            }
            else{
                anchoPorColumna = (anchoTabla / numColumnas) - 7;
            }

            for (TableColumn<?, ?> columna : tabla.getColumns()) {
                columna.setPrefWidth(anchoPorColumna);
            }
        }
    }
}
