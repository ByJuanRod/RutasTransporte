package rutas.com.rutastransporte.algoritmos;

import java.util.*;

/*
    Nombre: BellmanFord
    Tipo: Clase
    Objetivo: Aplicar el algoritmo de Bellman-Ford
 */
public class BellmanFord {
    /*
        Nombre: ResultadoBellmanFord
        Tipo: Clase
        Objetivo: Almacenar los resultados obtenidos del algoritmo de Bellman-Ford
     */
    static class ResultadoBellmanFord {
        public int[] distancias;          // Distancias más cortas desde el origen
        public int[] predecesores;        // Nodos predecesores en el camino más corto
        public boolean tieneCicloNegativo; // Indica si se encontró un ciclo negativo
        public ResultadoBellmanFord(int[] distancias, int[] predecesores, boolean tieneCicloNegativo) {
            this.distancias = distancias;
            this.predecesores = predecesores;
            this.tieneCicloNegativo = tieneCicloNegativo;
        }

        /*
            Nombre: imprimirResultados
            Argumentos:
                (int) origen: Representa el punto de origen del algoritmo
            Objetivo: Imprimir los resultados obtenidos del algoritmo de Bellman-Ford
            Retorno: -
         */
        public void imprimirResultados(int origen) {
            System.out.println("\n=== RESULTADOS ALGORITMO BELLMAN-FORD ===");
            System.out.println("Nodo origen: " + origen);
            System.out.println("¿Ciclo negativo alcanzable?: " + (tieneCicloNegativo ? "SÍ" : "NO"));

            System.out.println("\nDistancias mínimas desde el origen:");
            for (int i = 0; i < distancias.length; i++) {
                if (distancias[i] == Integer.MAX_VALUE) {
                    System.out.println("Nodo " + i + ": INFINITO (no alcanzable)");
                } else {
                    System.out.println("Nodo " + i + ": " + distancias[i]);
                }
            }

            System.out.println("\nCaminos más cortos:");
            for (int i = 0; i < distancias.length; i++) {
                if (distancias[i] != Integer.MAX_VALUE && i != origen) {
                    System.out.print("Camino a " + i + ": ");
                    imprimirCamino(origen, i, predecesores);
                    System.out.println(" (costo: " + distancias[i] + ")");
                }
            }
        }

        /*
            Nombre: imprimirCamino
            Argumentos:
                (int) origen: Representa el punto de origen del algoritmo.
                (int) destino: Representa el destino del algoritmo.
             Objetivo: Imprimir el posible camino encontrado del algoritmo de Bellman Ford
             Retorno: -
         */
        private void imprimirCamino(int origen, int destino, int[] predecesores) {
            if (destino == origen) {
                System.out.print(origen);
            } else if (predecesores[destino] == -1) {
                System.out.print("No existe camino");
            } else {
                imprimirCamino(origen, predecesores[destino], predecesores);
                System.out.print(" -> " + destino);
            }
        }
    }

    /*
        Nombre: bellmanFord
        Argumentos:
            (List<Arista>) aristas: Representa las aristas del grafo.
            (int) numNodos: Representa la cantidad de nodos existentes.
            (int) origen: Representa el punto de origen del algoritmo.
        Objetivo: Aplicar el algoritmo de Bellman Ford a un grafo.
        Retorno: (ResultadoBellmanFord) Retorna los resultados posibles del camino.
     */
    public static ResultadoBellmanFord bellmanFord(List<Arista> aristas, int numNodos, int origen) {
        if (origen < 0 || origen >= numNodos) {
            throw new IllegalArgumentException("Nodo origen inválido");
        }

        int[] distancias = new int[numNodos];
        int[] predecesores = new int[numNodos];

        for (int i = 0; i < numNodos; i++) {
            distancias[i] = Integer.MAX_VALUE;
            predecesores[i] = -1;
        }
        distancias[origen] = 0;

        for (int i = 0; i < numNodos - 1; i++) {
            boolean huboCambio = false;

            for (Arista arista : aristas) {
                int u = arista.origen;
                int v = arista.destino;
                int peso = arista.peso;

                if (distancias[u] != Integer.MAX_VALUE &&
                        distancias[u] + peso < distancias[v]) {

                    distancias[v] = distancias[u] + peso;
                    predecesores[v] = u;
                    huboCambio = true;
                }
            }

            if (!huboCambio) {
                System.out.println("Optimización: Terminación temprana en iteración " + (i + 1));
                break;
            }
        }

        boolean tieneCicloNegativo = false;
        for (Arista arista : aristas) {
            int u = arista.origen;
            int v = arista.destino;
            int peso = arista.peso;

            if (distancias[u] != Integer.MAX_VALUE &&
                    distancias[u] + peso < distancias[v]) {
                tieneCicloNegativo = true;
                System.out.println("Ciclo detectado en la arista: " + u + " -> " + v + " (peso: " + peso + ")");
                break;
            }
        }

        return new ResultadoBellmanFord(distancias, predecesores, tieneCicloNegativo);
    }

}