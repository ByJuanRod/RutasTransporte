package rutas.com.rutastransporte.repositorio;

import rutas.com.rutastransporte.modelos.*;
import rutas.com.rutastransporte.servicios.GrafoTransporte;

import java.util.ArrayList;

/*
    Nombre: SistemaTransporte
    Tipo: Clase
    Objetivo: Almacenar la logica del sistema de transporte público.
 */
public class SistemaTransporte {

    private final ArrayList<Parada> paradas;

    private final ArrayList<Ruta> rutas;

    private static SistemaTransporte instancia = null;

    private final GrafoTransporte grafo;

    private SistemaTransporte() {
        paradas = new ArrayList<>();
        rutas = new ArrayList<>();
        grafo = new GrafoTransporte();
    }

    /*
        Nombre: getSistemaTransporte
        Argumentos: -
        Objetivo: Obtener la instancia del sistema de transporte publico
        Retorno: (SistemaTransporte) Retorna la instancia del sistema de transporte actual,
                                     Si no existe una instancia creara una instancia.
     */
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