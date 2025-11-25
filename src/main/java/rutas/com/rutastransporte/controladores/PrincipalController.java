package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import rutas.com.rutastransporte.servicios.ServicioPrincipal;

public class PrincipalController {
    private final ServicioPrincipal servicioPrincipal = new ServicioPrincipal();

    @FXML
    private Label lblCantRutas;

    @FXML
    private Label lblCantParadas;

    @FXML
    private Label lblPrecioProm;

    @FXML
    private PieChart grSegmentacion;

    @FXML
    public void initialize(){
        cargarDatos();
    }

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar los datos base de para que se muestren los resumenes de principal.
        Retorno: -
     */
    public void cargarDatos(){
        lblCantRutas.setText(String.valueOf(servicioPrincipal.getCantRutas()));
        lblCantParadas.setText(String.valueOf(servicioPrincipal.getCantParadas()));
        grSegmentacion.setLabelsVisible(true);
        Thread thread2 = new Thread(() -> grSegmentacion.setData(servicioPrincipal.crearSeries()));
        lblPrecioProm.setText(String.valueOf(servicioPrincipal.getCostoPromedio()));

       thread2.start();
    }
}
