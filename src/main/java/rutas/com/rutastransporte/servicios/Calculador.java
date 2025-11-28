package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.*;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.*;

/*
    Nombre: Calculador
    Tipo: Clase
    Objetivo: Manejar todos los metodos de la logica de calculo de caminos.
 */
public class Calculador {

    private GrafoTransporte grafo;

    /*
        Nombre: getPeso
        Argumentos:
            (Ruta) ruta: Reprsenta la ruta que se le tomará el peso.
            (Criterio) criterio: Representa la propiedad que sera utilizada para el calculo.
        Objetivo: Obtener la propiedad de la ruta que sera evaluada en el calculo
        Retorno: (float) Retorna el valor del criterio seleccionado.
     */
    private float getPeso(Ruta ruta, Criterio criterio){
        return switch (criterio) {
            case MAS_ECONOMICO -> ruta.getCostoConEvento();
            case MAS_CORTA -> ruta.getDistanciaConEvento();
            case MAS_RAPIDA -> ruta.getTiempoConEvento();
            case MENOS_TRASBORDOS -> ruta.getTrasbordos();
            default -> 1.0f;
        };
    }

    /*
        Nombre: dijkstra
        Argumentos:
            (Parada) origen: Representa el punto de origen del camino.
            (Parada) destino: Representa el punto de fin del camino.
            (Criterio) criterio: Representa el criterio que se evaluará en el algoritmo.
        Objetivo: Obtener el mejor camino desde un punto de origen hasta un destino tomando como referencia un criterio.
        Retorno: (RutaPosible) Retorna el camino que es la mejor ruta segun el criterio selccionado.
     */
    public RutaPosible dijkstra(Parada origen, Parada destino, Criterio criterio) {
        ServicioEventos.getInstancia().limpiarEventosExpirados();

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

                if(newDist < oldDist) {
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
            pasoActual = ruta.getOrigen();
        }

        rutaPosible.agregarCriterioDestacado(criterio);

        return rutaPosible;
    }

    public void setGrafo(GrafoTransporte grafo) {
        this.grafo = grafo;
    }

    /*
        Nombre: floydWarshall
        Argumentos:
            (Criterio) criterio: Representa el criterio que será tomado en cuenta para obtener la mejor ruta en el grafo.
        Objetivo: Obtener la mejor ruta del el grafo segun un criterio seleccionado.
        Retorno: (RutaPosible) Retorna el camino destacado del criterio seleccionado.
     */
    public RutaPosible floydWarshall(Criterio criterio) {
        ServicioEventos.getInstancia().limpiarEventosExpirados();

        Map<Parada, Map<Parada, Float>> distancias = new HashMap<>();
        Map<Parada, Map<Parada, Parada>> intermedias = new HashMap<>();

        for (Parada i : grafo.getParadas()) {
            distancias.put(i, new HashMap<>());
            intermedias.put(i, new HashMap<>());

            for (Parada j : grafo.getParadas()) {
                if (i.equals(j)) {
                    distancias.get(i).put(j, 0.0f);
                    intermedias.get(i).put(j, null);
                } else {
                    Ruta rutaDirecta = encontrarRutaDirecta(i, j);
                    if (rutaDirecta != null) {
                        float peso = getPeso(rutaDirecta, criterio);
                        distancias.get(i).put(j, peso);
                        intermedias.get(i).put(j, j);
                    } else {
                        distancias.get(i).put(j, Float.POSITIVE_INFINITY);
                        intermedias.get(i).put(j, null);
                    }
                }
            }
        }

        for (Parada k : grafo.getParadas()) {
            for (Parada i : grafo.getParadas()) {
                if (distancias.get(i).get(k) == Float.POSITIVE_INFINITY) {
                    continue;
                }

                for (Parada j : grafo.getParadas()) {
                    float distanciaIK = distancias.get(i).get(k);
                    float distanciaKJ = distancias.get(k).get(j);
                    float distanciaIJ = distancias.get(i).get(j);

                    if (distanciaIK != Float.POSITIVE_INFINITY &&
                            distanciaKJ != Float.POSITIVE_INFINITY &&
                            distanciaIK + distanciaKJ < distanciaIJ) {

                        distancias.get(i).put(j, distanciaIK + distanciaKJ);
                        intermedias.get(i).put(j, intermedias.get(i).get(k));
                    }
                }
            }
        }

        Parada mejorOrigen = null;
        Parada mejorDestino = null;
        float mejorDistancia = Float.POSITIVE_INFINITY;

        for (Parada origen : grafo.getParadas()) {
            for (Parada destino : grafo.getParadas()) {
                if (!origen.equals(destino)) {
                    float distancia = distancias.get(origen).get(destino);
                    if (distancia < mejorDistancia) {
                        mejorDistancia = distancia;
                        mejorOrigen = origen;
                        mejorDestino = destino;
                    }
                }
            }
        }

        if (mejorOrigen == null || mejorDestino == null) {
            return null;
        }

        return reconstruirRutaFloydWarshall(mejorOrigen, mejorDestino, intermedias, criterio);
    }

    /*
        Nombre: encontrarRutaDirecta
        Argumentos:
            (Parada) origen: Representa el punto de origen.
            (Parada) destino: Representa la parada de destino.
        Objetivo: Obtener una ruta directa que valla desde el punto de origen al destino directamente.
        Retorno: (Ruta) Retorna una ruta que valla desde el punto de origen hasta el destino directamente
                        Retorna null si no existe una ruta que valla desde el punto de origen al destino.
     */
    private Ruta encontrarRutaDirecta(Parada origen, Parada destino) {
        for (Ruta ruta : grafo.getRutas(origen)) {
            if (ruta.getDestino().equals(destino)) {
                return ruta;
            }
        }
        return null;
    }

    /*
    Nombre: reconstruirRutaFloydWarshall
    Argumentos:
        (Parada) origen: Representa la parada de origen.
        (Parada) destino: Representa la parada de destino.
        (Map<Parada, Map<Parada, Parada>>) intermedias: Mapa de nodos intermedios para reconstruir rutas.
        (Criterio) criterio: Representa el criterio destacado.
    Objetivo: Reconstruir la ruta destacada obtenida por el algoritmo de Floyd Warshall.
    Retorno: (RutaPosible) Retorna el camino destacado del algoritmo FloydWarshall.
 */
    private RutaPosible reconstruirRutaFloydWarshall(Parada origen, Parada destino,
                                                     Map<Parada, Map<Parada, Parada>> intermedias,
                                                     Criterio criterio) {
        RutaPosible rutaPosible = new RutaPosible();

        if (intermedias.get(origen).get(destino) == null) {
            return null;
        }

        Parada actual = origen;
        List<Parada> camino = new ArrayList<>();
        camino.add(actual);

        while (!actual.equals(destino)) {
            Parada siguiente = intermedias.get(actual).get(destino);
            if (siguiente == null) {
                return null;
            }
            camino.add(siguiente);
            actual = siguiente;
        }

        for (int i = 0; i < camino.size() - 1; i++) {
            Parada desde = camino.get(i);
            Parada hasta = camino.get(i + 1);

            Ruta ruta = encontrarRutaDirecta(desde, hasta);
            if (ruta == null) {
                return null;
            }

            rutaPosible.agregarAlCamino(ruta);
        }

        rutaPosible.agregarCriterioDestacado(criterio);
        return rutaPosible;
    }

    /*
        Nombre: calcularArbolDijkstra
        Argumentos:
            (Parada) raiz: Representa el punto de origen del arbol.
        Objetivo: Crear el arbol de conexiones que tiene una parada a traves de las rutas.
        Retorno: (List<Ruta>) Retorna una lista de todas las rutas involucradas en el camino.
     */
    public List<Ruta> calcularArbolDijkstra(Parada raiz) {
        Criterio criterio = Criterio.DEFAULT;

        Map<Parada, Ruta> edgeTo = new HashMap<>();

        Map<Parada, Float> distTo = new HashMap<>();

        PriorityQueue<Parada> pq = new PriorityQueue<>(Comparator.comparing(distTo::get));

        for (Parada p : SistemaTransporte.getSistemaTransporte().getParadas()) {
            distTo.put(p, Float.POSITIVE_INFINITY);
        }
        distTo.put(raiz, 0.0f);
        pq.add(raiz);

        while (!pq.isEmpty()) {
            Parada actual = pq.poll();

            for (Ruta ruta : grafo.getRutas(actual)) {
                Parada vecino = ruta.getDestino();
                float peso = getPeso(ruta, criterio);

                if (distTo.get(actual) + peso < distTo.get(vecino)) {
                    distTo.put(vecino, distTo.get(actual) + peso);

                    edgeTo.put(vecino, ruta);

                    pq.remove(vecino);
                    pq.add(vecino);
                }
            }
        }

        return new ArrayList<>(edgeTo.values());
    }

}
