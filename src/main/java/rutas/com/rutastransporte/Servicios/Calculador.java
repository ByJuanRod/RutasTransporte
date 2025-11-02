package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.*;

import java.util.*;

public class Calculador {

    private GrafoTransporte grafo;

    private float getPeso(Ruta ruta, Criterio criterio){
        return switch (criterio) {
            case MAS_ECONOMICO -> ruta.getCosto();
            case MAS_CORTA -> ruta.getDistancia();
            case MAS_RAPIDA -> ruta.getTiempo();
            default -> 1.0f;
        };
    }

    public RutaPosible dijkstra(Parada origen, Parada destino, Criterio criterio) {
        RutaPosible rutaPosible = new RutaPosible();
        Map<Parada, Float> distancias = new HashMap<>();
        Map<Parada, Ruta> anterior = new HashMap<>();
        Set<Parada> visited = new HashSet<>();

        Comparator<Parada> comparador = Comparator.comparing(distancias::get);
        PriorityQueue<Parada> paradas = new PriorityQueue<>(comparador);

        distancias.put(origen, 0.0f);
        paradas.add(origen);

        while (!paradas.isEmpty()) {
            Parada actual = paradas.poll();

            if(visited.contains(actual)) continue;
            visited.add(actual);

            if(actual.equals(destino)) {
                break;
            }

            for(Ruta ruta : grafo.getRutas(actual)) {
                Parada vecino = ruta.getDestino();

                if(visited.contains(vecino)) continue;

                float peso = getPeso(ruta, criterio);
                float newDist = distancias.get(actual) + peso;
                float oldDist = distancias.getOrDefault(vecino, Float.POSITIVE_INFINITY);

                if(newDist <  oldDist) {
                    distancias.put(vecino, newDist);
                    anterior.put(vecino, ruta);
                    paradas.add(vecino);
                }

            }

        }

        if(!distancias.containsKey(destino)) {
            return null;
        }

        Parada pasoActual = destino;

        while(anterior.containsKey(pasoActual)) {
            Ruta ruta = anterior.get(pasoActual);

            rutaPosible.agregarAlCaminoFirst(ruta);
            rutaPosible.agregarCosto(ruta.getCosto());
            rutaPosible.agregarDistancia(ruta.getDistancia());
            rutaPosible.agregarTiempo(ruta.getTiempo());
            rutaPosible.setCriterio(criterio);

            pasoActual = ruta.getOrigen();
        }

        return rutaPosible;
    }

    public void setGrafo(GrafoTransporte grafo) {
        this.grafo = grafo;
    }
}
