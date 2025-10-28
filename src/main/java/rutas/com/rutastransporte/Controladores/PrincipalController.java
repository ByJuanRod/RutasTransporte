package rutas.com.rutastransporte.Controladores;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import rutas.com.rutastransporte.Servicios.ServicioPrincipal;
import rutas.com.rutastransporte.Servicios.SistemaTransporte;

public class PrincipalController {
    private ServicioPrincipal servicioPrincipal = new ServicioPrincipal();

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

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                lblPrecioProm.setText(String.valueOf(servicioPrincipal.getCostoPromedio()));
            }
        });

        Thread thread2 = new Thread(new Runnable(){
            @Override
            public void run(){
                grSegmentacion.setData(servicioPrincipal.crearSeries());
            }
        });


       thread.run();
       thread2.run();
    }
}
