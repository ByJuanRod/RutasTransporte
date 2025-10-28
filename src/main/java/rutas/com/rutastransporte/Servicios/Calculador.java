package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.*;

import java.util.PriorityQueue;

public class Calculador {

    private GrafoTransporte grafo;

    public RutaPosible dijkstra(Parada origen, Parada destino, Criterio criterio) {
        RutaPosible rutaPosible = new RutaPosible();
        PriorityQueue<Arista> aristas = new PriorityQueue<>();

        aristas.addAll(grafo.getAristas(origen));


        return rutaPosible;
    }

    public void setGrafo(GrafoTransporte grafo) {
        this.grafo = grafo;
    }
}
