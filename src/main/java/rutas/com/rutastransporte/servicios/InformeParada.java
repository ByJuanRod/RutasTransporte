package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.ArrayList;

/*
    Nombre: InformeParada
    Tipo: Clase
    Objetivo: Almacenar los metodos de la lógica de los informes de las paradas.
 */
public class InformeParada {
    ArrayList<Ruta> rutasVinculadas;

    public InformeParada(Parada parada){
        rutasVinculadas = obtenerRutasVinculadas(parada);
    }

    /*
        Nombre: porcentajeCobertura
        Argumentos: -
        Objetivo: Obtener el porcentaje de cobertura que tiene una parada en todas las rutas.
        Retorna: (int) Retorna el balance entre la cantidad de rutas vinculadas a una parada y todas las rutas existentes.
                       Retorna 0 si la parada no tiene rutas vinculadas.
     */
    public int porcentajeCobertura(){
        if(SistemaTransporte.getSistemaTransporte().getRutas().isEmpty()){
            return 0;
        }
        float total = (float) recuentoRuta() / SistemaTransporte.getSistemaTransporte().getRutas().size();
        return Math.round(total * 100);
    }

    /*
        Nombre: recuentoRuta
        Argumentos: -
        Objetivo: Retornar la cantidad de rutas que existen vinculadas a una parada como origen o destino.
        Retorno: (int) Retorna la cantidad de rutas vinculadas a una parada.
     */
    public int recuentoRuta(){
        return rutasVinculadas.size();
    }

    /*
        Nombre: getEficienciaPromedio
        Argumentos: -
        Objetivo: Obtener el procentaje de eficiencia que tienen las rutas en promedio en comparación con la mas eficiente.
        Retorno: (double) Retorna el balance entre la ruta más eficiente y el promedio.
     */
    public double getEficienciaPromedio() {
        if (rutasVinculadas.isEmpty()) {
            return 0.0;
        }

        double eficienciaMaxima = rutasVinculadas.stream()
                .mapToDouble(ruta -> {
                    double distanciaKm = ruta.getDistanciaConEvento() / 1000.0;
                    double tiempoHoras = ruta.getTiempoConEvento() / 60.0;
                    return tiempoHoras > 0 ? distanciaKm / tiempoHoras : 0;
                })
                .max()
                .orElse(0.0);

        double eficienciaPromedio = rutasVinculadas.stream()
                .mapToDouble(ruta -> {
                    double distanciaKm = ruta.getDistanciaConEvento() / 1000.0;
                    double tiempoHoras = ruta.getTiempoConEvento() / 60.0;
                    return tiempoHoras > 0 ? distanciaKm / tiempoHoras : 0;
                })
                .average()
                .orElse(0.0);

        return eficienciaMaxima > 0 ? (eficienciaPromedio / eficienciaMaxima) * 100 : 0;
    }

    /*
        Nombre: obtenerRutasVinculadas
        Argumentos:
            (Parada) parada: Representa la parada que se utilizara para el informe.
        Objetivo: Obtener todas las rutas vinculadas a una parada.
        Retorno: (ArrayList<Ruta>) Retorna un arreglo que tiene todas las rutas que utilizan la parada como origen o destino.
     */
    public ArrayList<Ruta> obtenerRutasVinculadas(Parada parada){
        ArrayList<Ruta> rutasVinculadas = new ArrayList<>();
        for(Ruta rt: SistemaTransporte.getSistemaTransporte().getRutas()){
            if(rt.getDestino().getCodigo() == parada.getCodigo() || rt.getOrigen().getCodigo() == parada.getCodigo()){
                rutasVinculadas.add(rt);
            }
        }

        return  rutasVinculadas;
    }

    /*
         Nombre: getPromedios
         Argumentos:
            (String) nombre: Representa el nombre del argumento que se utilizará para obtener el promedio.
         Objetivo: Obtener el promedio de todas las rutas respecto a un criterio.
         Retorno: (float) Retorna el valor promedio de las rutas vinculadas respecto al criterio seleccionado.
     */
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

    /*
        Nombre: getPeso
        Argumento:
            (Ruta) ruta: Representa la ruta de la que se extraera el valor.
            (String) nombre: Representa el nombre del campo.
        Objetivo: Obtener el criterio que se utilizara para los promedios.
        Retorno: (float) Retorna el valor de una propiedad dependiendo del criterio.
     */
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

    public double getIndiceConfiabilidad() {
        long rutasSinEventos = rutasVinculadas.stream()
                .filter(ruta -> !ruta.tieneEfectosNegativos())
                .count();

        return recuentoRuta() > 0 ?
                (double) rutasSinEventos / recuentoRuta() * 100 : 0;
    }

}
