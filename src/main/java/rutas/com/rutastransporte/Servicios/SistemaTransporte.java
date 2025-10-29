package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.*;

import java.util.ArrayList;

public class SistemaTransporte {
    public static int genCodigoParada = 1;

    public static int genCodigoRuta = 1;

    public ArrayList<Parada> paradas;

    public ArrayList<Ruta> rutas;

    private static SistemaTransporte instancia = null;

    private GrafoTransporte grafo;

    private Calculador calculador;

    private SistemaTransporte() {
        paradas = new ArrayList<>();
        rutas = new ArrayList<>();
        grafo = new GrafoTransporte();
    }

    public static SistemaTransporte getSistemaTransporte(){
        if(instancia == null){
            instancia = new SistemaTransporte();
        }
        return instancia;
    }

    public ArrayList<Parada> getParadas(){
        return paradas;
    }

    public GrafoTransporte getGrafo(){
        return grafo;
    }

    public ArrayList<Ruta> getRutas(){
        return rutas;
    }

    public void conectarParadas(Parada origen, Parada destinoa) {
        RutaBuilder rutaBuilder = new RutaBuilder();
        Ruta ruta = rutaBuilder.construir();
        grafo.agregarRuta(ruta);
        rutas.add(ruta);
    }

   public RutaPosible calcularRutaOptima(Parada origen, Parada destino, Criterio criterio) {
        calculador.setGrafo(grafo);
        return calculador.dijkstra(origen, destino, criterio);
    }

}