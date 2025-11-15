package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;

import java.util.*;

public class GrafoTransporte {
    private final Map<Parada, List<Ruta>> listaAdyacencia;

    public Set<Parada> getParadas(){
        return listaAdyacencia.keySet();
    }

    public GrafoTransporte() {
        listaAdyacencia = new HashMap<>();
    }

    public void agregarParada(Parada parada) {
        listaAdyacencia.putIfAbsent(parada, new ArrayList<>());
    }

    public void agregarRuta(Ruta ruta) {
        Parada origen = ruta.getOrigen();
        listaAdyacencia.putIfAbsent(origen, new ArrayList<>());
        listaAdyacencia.get(origen).add(ruta);
    }

    public List<Ruta> getRutas(Parada parada) {
        return listaAdyacencia.getOrDefault(parada, new ArrayList<>());
    }

    public void eliminarParada(Parada parada) {
        listaAdyacencia.remove(parada);

        for (List<Ruta> rutas : listaAdyacencia.values()) {
            rutas.removeIf(ruta -> ruta.getDestino().equals(parada));
        }

    }

    public void eliminarRuta(Ruta ruta) {
        Parada origen = ruta.getOrigen();
        List<Ruta> rutas = listaAdyacencia.get(origen);

        if(rutas != null) {
            rutas.remove(ruta);
        }

    }

}
