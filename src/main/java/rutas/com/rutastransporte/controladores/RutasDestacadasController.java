package rutas.com.rutastransporte.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rutas.com.rutastransporte.modelos.Criterio;
import rutas.com.rutastransporte.modelos.RutaPosible;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;
import rutas.com.rutastransporte.servicios.Calculador;

import java.util.*;

public class RutasDestacadasController {
    private final Calculador calc = new  Calculador();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private VBox contenedorRutas;

    public Map<Criterio, AnchorPane> rutas = new HashMap<>();

    public Map<Criterio,Button> botones = new HashMap<>();

    @FXML
    private Button btnEconomica, btnRapida, btnCorta, btnTrasbordos;

    private Criterio ultimoSeleccionado = null;

    @FXML
    public void initialize() {
        calc.setGrafo(SistemaTransporte.getSistemaTransporte().getGrafo());
        for(Criterio crt : Criterio.values()){
            if(!crt.equals(Criterio.MEJOR_RUTA)){
                RutaPosible resultado = calc.floidWarshall(crt);
                ResultadoRutaController rest = new ResultadoRutaController();
                AnchorPane panel = rest.crearInterfaz(resultado);
                rutas.put(crt,panel);
            }
        }

        botones.put(Criterio.MAS_CORTA,btnCorta);
        botones.put(Criterio.MENOS_TRASBORDOS,btnTrasbordos);
        botones.put(Criterio.MAS_RAPIDA,btnRapida);
        botones.put(Criterio.MAS_ECONOMICO,btnEconomica);
        cambiarOpcion(Criterio.MAS_ECONOMICO);
    }

    private void cambiarOpcion(Criterio crit){
        if(ultimoSeleccionado != null){
            if(ultimoSeleccionado.equals(crit)){
                return;
            }
            botones.get(ultimoSeleccionado).getStyleClass().remove("seleccionado");
            contenedorRutas.getChildren().clear();
        }

        ultimoSeleccionado = crit;
        botones.get(crit).getStyleClass().add("seleccionado");
        contenedorRutas.getChildren().add(rutas.get(ultimoSeleccionado));
    }

    public void btnEconomicaClick(){
        cambiarOpcion(Criterio.MAS_ECONOMICO);
    }

    public void btnRapidaClick(){
        cambiarOpcion(Criterio.MAS_RAPIDA);
    }

    public void btnCortaClick(){
        cambiarOpcion(Criterio.MAS_CORTA);
    }

    public void btnTrasbordosClick(){
        cambiarOpcion(Criterio.MENOS_TRASBORDOS);
    }

    public void btnCerrarClick(){
        stage.close();
    }

}
