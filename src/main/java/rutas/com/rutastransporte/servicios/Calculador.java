package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.*;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.*;

public class Calculador {

    private GrafoTransporte grafo;

    private float getPeso(Ruta ruta, Criterio criterio){
        return switch (criterio) {
            case MAS_ECONOMICO -> ruta.getCostoConEvento();
            case MAS_CORTA -> ruta.getDistanciaConEvento();
            case MAS_RAPIDA -> ruta.getTiempoConEvento();
            default -> 1.0f;
        };
    }

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
            rutaPosible.agregarCriterioDestacado(criterio);

            pasoActual = ruta.getOrigen();
        }

        return rutaPosible;
    }

    public void setGrafo(GrafoTransporte grafo) {
        this.grafo = grafo;
    }

    public RutaPosible floidWarshall(Criterio criterio) {
        ServicioEventos.getInstancia().limpiarEventosExpirados();

        Map<Parada, Map<Parada, Float>> distancias = new HashMap<>();
        Map<Parada, Map<Parada, Ruta>> rutasIntermedias = new HashMap<>();

        for (Parada origen : grafo.getParadas()) {
            distancias.put(origen, new HashMap<>());
            rutasIntermedias.put(origen, new HashMap<>());

            for (Parada destino : grafo.getParadas()) {
                if (origen.equals(destino)) {
                    distancias.get(origen).put(destino, 0.0f);
                } else {
                    Ruta rutaDirecta = encontrarRutaDirecta(origen, destino);
                    if (rutaDirecta != null) {
                        float peso = getPeso(rutaDirecta, criterio);
                        distancias.get(origen).put(destino, peso);
                        rutasIntermedias.get(origen).put(destino, rutaDirecta);
                    } else {
                        distancias.get(origen).put(destino, Float.POSITIVE_INFINITY);
                    }
                }
            }
        }

        for (Parada k : grafo.getParadas()) {
            for (Parada i : grafo.getParadas()) {
                for (Parada j : grafo.getParadas()) {
                    float distanciaIK = distancias.get(i).get(k);
                    float distanciaKJ = distancias.get(k).get(j);
                    float distanciaIJ = distancias.get(i).get(j);

                    if (distanciaIK != Float.POSITIVE_INFINITY &&
                            distanciaKJ != Float.POSITIVE_INFINITY &&
                            distanciaIK + distanciaKJ < distanciaIJ) {

                        distancias.get(i).put(j, distanciaIK + distanciaKJ);
                        rutasIntermedias.get(i).put(j, rutasIntermedias.get(i).get(k));
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

        return reconstruirRutaFloydWarshall(mejorOrigen, mejorDestino, rutasIntermedias, criterio);
    }

    private Ruta encontrarRutaDirecta(Parada origen, Parada destino) {
        for (Ruta ruta : grafo.getRutas(origen)) {
            if (ruta.getDestino().equals(destino)) {
                return ruta;
            }
        }
        return null;
    }

    private RutaPosible reconstruirRutaFloydWarshall(Parada origen, Parada destino,
                                                     Map<Parada, Map<Parada, Ruta>> rutasIntermedias,
                                                     Criterio criterio) {
        RutaPosible rutaPosible = new RutaPosible();

        Parada actual = origen;
        while (!actual.equals(destino)) {
            Ruta ruta = rutasIntermedias.get(actual).get(destino);
            if (ruta == null) {
                return null;
            }

            rutaPosible.agregarAlCamino(ruta);
            rutaPosible.agregarCosto(ruta.getCostoConEvento());
            rutaPosible.agregarTrasbordos(ruta.getTrasbordos());
            rutaPosible.agregarDistancia(ruta.getDistanciaConEvento());
            rutaPosible.agregarTiempo(ruta.getTiempoConEvento());
            rutaPosible.agregarCriterioDestacado(criterio);

            actual = ruta.getDestino();
        }

        return rutaPosible;
    }

    //kruskal-algorithm para el MST
    public List<Ruta> calcularMST() {
        List<Ruta> rutas = new ArrayList<>(SistemaTransporte.getSistemaTransporte().getRutas());
        List<Parada> paradas = new ArrayList<>(SistemaTransporte.getSistemaTransporte().getParadas());

        rutas.sort(Comparator.comparingInt(Ruta::getDistancia));

        Map<Integer, Integer> padres = new HashMap<>();
        for (Parada parada : paradas) {
            padres.put(parada.getCodigo(), parada.getCodigo());
        }

        List<Ruta> mst = new ArrayList<>();
        int numAristas = paradas.size() - 1;

        for(Ruta ruta : rutas) {

            int raizOrigen = find(padres, ruta.getOrigen().getCodigo());
            int raizDestino = find(padres, ruta.getDestino().getCodigo());

            if(raizOrigen != raizDestino){
                mst.add(ruta);

                union(padres, raizOrigen, raizDestino);

            }

            if(mst.size() == numAristas){
                break;
            }

        }

        return mst;

    }

    private int find(Map<Integer, Integer> padres, int i) {

        if (padres.get(i) == i) {
            return i;
        }

        return find(padres, padres.get(i));
    }

    private void union(Map<Integer, Integer> padres, int raizI, int raizJ) {
        padres.put(raizI, raizJ);
    }

    public List<Ruta> calcularArborescencia(Parada raiz) {
        List<Ruta> arborescencia = new ArrayList<>();

        Map<Parada, Ruta> mejoresEntrantes = new HashMap<>();

        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            Parada v = ruta.getDestino();
            if (v.equals(raiz)) continue;

            float costo = ruta.getCosto();

            if (!mejoresEntrantes.containsKey(v) || costo < mejoresEntrantes.get(v).getCosto()) {
                mejoresEntrantes.put(v, ruta);
            }
        }

        arborescencia.addAll(mejoresEntrantes.values());
        return arborescencia;
    }

    public List<Ruta> calcularArbolDijkstra(Parada raiz) {
        Criterio criterio = Criterio.MENOS_TRASBORDOS;

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
