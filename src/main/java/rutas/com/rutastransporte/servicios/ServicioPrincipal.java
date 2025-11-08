package rutas.com.rutastransporte.servicios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoParada;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.Hashtable;

public class ServicioPrincipal {
    public int getCantRutas(){
        return SistemaTransporte.getSistemaTransporte().getRutas().size();
    }

    public int getCantParadas(){
        return SistemaTransporte.getSistemaTransporte().getParadas().size();
    }

    public float getCostoPromedio(){
        if(getCantRutas() == 0){
            return 0;
        }
        else{
            float costoTotal = 0;
            for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) costoTotal += ruta.getCosto();

            return Math.round(costoTotal  / getCantRutas());
        }

    }

    public Hashtable<TipoParada, Integer> getSegmentacion(){
        Hashtable<TipoParada,Integer> cantidadPorTipo =  new Hashtable<>();

        for(TipoParada tipo :  TipoParada.values()){
            cantidadPorTipo.put(tipo,0);
        }

        for(Parada parada :  SistemaTransporte.getSistemaTransporte().getParadas()){
            cantidadPorTipo.put(parada.getTipo(), cantidadPorTipo.get(parada.getTipo()) + 1);
        }

        return cantidadPorTipo;
    }

    public ObservableList<PieChart.Data> crearSeries(){
        ObservableList<PieChart.Data>  datos = FXCollections.observableArrayList();
        Hashtable<TipoParada, Integer> cantidadPorTipo = getSegmentacion();

        for(TipoParada tipo :  TipoParada.values()){
            datos.add(new PieChart.Data(tipo.getTipo(),cantidadPorTipo.get(tipo)));
        }

        return datos;
    }

}
