package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.Arista;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoTransporte {
    private Map<Parada, List<Arista>> listaAdyacencia;

    public GrafoTransporte() {
        listaAdyacencia = new HashMap<>();
    }

    public void agregarParada(Parada parada) {
        listaAdyacencia.putIfAbsent(parada, new ArrayList<>());
    }

    public void agregarArista(Parada origen, Parada destino, Ruta ruta) {
        Arista arista = new Arista(destino, ruta);
        listaAdyacencia.get(origen).add(arista);
    }

    public List<Arista> getAristas(Parada parada) {
        return listaAdyacencia.getOrDefault(parada, new ArrayList<>());
    }

    public void eliminarParada(Parada parada) {
        listaAdyacencia.remove(parada);

        for (List<Arista> aristas : listaAdyacencia.values()) {
            aristas.removeIf(arista -> arista.getRuta().getDestino().equals(parada));
        }
    }


}
