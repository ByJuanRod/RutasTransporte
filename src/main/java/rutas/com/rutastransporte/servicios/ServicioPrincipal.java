package rutas.com.rutastransporte.servicios;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoParada;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.Hashtable;

/*
    Nombre: ServicioPrincipal
    Objetivo: Ofrecer las funciones que permiten que el apartado principal funcione correctamente.
 */
public class ServicioPrincipal {
    public int getCantRutas(){
        return SistemaTransporte.getSistemaTransporte().getRutas().size();
    }

    public int getCantParadas(){
        return SistemaTransporte.getSistemaTransporte().getParadas().size();
    }

    /*
        Nombre: getCostoPromedio
        Argumentos: -
        Objetivo: Obtener el costo promedio de las rutas existentes.
        Retorno: (float) Retorna el costo promedio de las rutas existentes.
     */
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

    /*
        Nombre: getSegmentacion
        Argumentos: -
        Objetivo: Obtener la segmentación de los tipos de paradas que existen en el programa.
        Retorno: (Hashtable<TipoParada,Integer>) Retorna un tabla hash que contiene la cantidad que existe de cada parada.
     */
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

    /*
        Nombre: crearSeries
        Argumentos: -
        Objetivo: Crear las series del grafico de pie para permitir su visualizacion
        Retorno: (ObservableList<PieChart.Data>) Retorna un lista que contiene todas las series de datos.
     */
    public ObservableList<PieChart.Data> crearSeries(){
        ObservableList<PieChart.Data>  datos = FXCollections.observableArrayList();
        Hashtable<TipoParada, Integer> cantidadPorTipo = getSegmentacion();

        for(TipoParada tipo :  TipoParada.values()){
            datos.add(new PieChart.Data(tipo.getTipo(),cantidadPorTipo.get(tipo)));
        }

        return datos;
    }

}
