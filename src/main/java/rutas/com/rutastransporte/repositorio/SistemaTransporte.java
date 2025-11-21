package rutas.com.rutastransporte.repositorio;

import rutas.com.rutastransporte.modelos.*;
import rutas.com.rutastransporte.servicios.GrafoTransporte;

import java.util.ArrayList;

public class SistemaTransporte {

    public ArrayList<Parada> paradas;

    public ArrayList<Ruta> rutas;

    private static SistemaTransporte instancia = null;

    private final GrafoTransporte grafo;

    private SistemaTransporte() {
        paradas = new ArrayList<>();
        rutas = new ArrayList<>();
        grafo = new GrafoTransporte();
    }

    public static SistemaTransporte getSistemaTransporte(){
        if(instancia == null){
            instancia = new SistemaTransporte();
            CargadorDatos.cargarDatos();
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

}