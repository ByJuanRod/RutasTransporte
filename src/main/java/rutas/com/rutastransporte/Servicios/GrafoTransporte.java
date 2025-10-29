package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.Arista;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoTransporte {
    private Map<Parada, List<Ruta>> listaAdyacencia;

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

}
