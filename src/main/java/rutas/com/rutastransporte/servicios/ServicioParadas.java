package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.ArrayList;

public class ServicioParadas {
    ArrayList<Ruta> rutasVinculadas;

    public ServicioParadas(Parada parada){
        rutasVinculadas = obtenerRutasVinculadas(parada);
    }

    public int porcentajeCobertura(){
        if(SistemaTransporte.getSistemaTransporte().getRutas().isEmpty()){
            return 0;
        }
        float total = (float) recuentoRuta() / SistemaTransporte.getSistemaTransporte().getRutas().size();
        return Math.round(total * 100);
    }

    public int recuentoRuta(){
        return rutasVinculadas.size();
    }

    public double getEficienciaPromedio() {
        return rutasVinculadas.stream()
                .mapToDouble(ruta -> (double) ruta.getDistancia() / ruta.getTiempo())
                .average()
                .orElse(0.0);
    }

    public ArrayList<Ruta> obtenerRutasVinculadas(Parada parada){
        ArrayList<Ruta> rutasVinculadas = new ArrayList<>();
        for(Ruta rt: SistemaTransporte.getSistemaTransporte().getRutas()){
            if(rt.getDestino().getCodigo() == parada.getCodigo() || rt.getOrigen().getCodigo() == parada.getCodigo()){
                rutasVinculadas.add(rt);
            }
        }

        return  rutasVinculadas;
    }

    public float getPromedios(String nombre){
        if(rutasVinculadas.isEmpty()){
            return 0;
        }
        else{
            float total = 0;
            for(Ruta rt : rutasVinculadas){
                total += getPeso(rt,nombre);
            }

            return total / rutasVinculadas.size();
        }
    }

    public float getPeso(Ruta ruta, String nombre){
        return switch (nombre){
            case "Tiempo" -> ruta.getTiempoConEvento();
            case "Costo" -> ruta.getCostoConEvento();
            case "Distancia" -> ruta.getDistanciaConEvento();
            default -> throw new IllegalStateException("Unexpected value: " + nombre);
        };
    }



    public float getTiempoPromedio() {
        return getPromedios("Tiempo");
    }

    public float getCostoPromedio() {
        return getPromedios("Costo");
    }

    public float getDistanciaPromedio(){
        return getPromedios("Distancia");
    }
}
