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

    public void cargarDatos(){
        lblCantRutas.setText(String.valueOf(servicioPrincipal.getCantRutas()));
        lblCantParadas.setText(String.valueOf(servicioPrincipal.getCantParadas()));
        grSegmentacion.setLabelsVisible(true);

        Thread thread = new Thread(() -> lblPrecioProm.setText(String.valueOf(servicioPrincipal.getCostoPromedio())));

        Thread thread2 = new Thread(() -> grSegmentacion.setData(servicioPrincipal.crearSeries()));


       thread.start();
       thread2.start();
    }
}
