package rutas.com.rutastransporte.algoritmos;

import java.util.*;

/*
    Nombre: Prim
    Tipo: Clase
    Objetivo: Aplicar el algoritmo de Prim para encontrar el Árbol de Expansión Mínima (MST)
 */
public class Prim {

    /*
        Nombre: ResultadoPrim
        Tipo: Clase
        Objetivo: Almacenar los resultados obtenidos del algoritmo de Prim
     */
    static class ResultadoPrim {
        List<Arista> aristasMST;   // Lista de aristas en el Árbol de Expansión Mínima
        int costoTotal;             // Costo total del MST
        int[] padre;                // Padre de cada nodo en el MST
        int[] key;                  // Peso mínimo para conectar cada nodo al MST

        public ResultadoPrim(List<Arista> aristasMST, int costoTotal, int[] padre, int[] key) {
            this.aristasMST = aristasMST;
            this.costoTotal = costoTotal;
            this.padre = padre;
            this.key = key;
        }

        /*
            Nombre: imprimirResultados
            Argumentos:
                (int) numNodos: Representa el número total de nodos en el grafo
                (int) origen: Representa el nodo de inicio del algoritmo
            Objetivo: Imprimir los resultados obtenidos del algoritmo de Prim
            Retorno: -
         */
        public void imprimirResultados(int numNodos, int origen) {
            System.out.println("\n=== RESULTADOS ALGORITMO PRIM ===");
            System.out.println("Número de nodos en el grafo: " + numNodos);
            System.out.println("Nodo de inicio: " + origen);
            System.out.println("Costo total del MST: " + costoTotal);
            System.out.println("Número de aristas en el MST: " + aristasMST.size());

            if (aristasMST.size() == numNodos - 1) {
                System.out.println(" MST completo encontrado (V-1 aristas)");
            } else {
                System.out.println("MST incompleto. El grafo puede no ser conexo");
            }

            System.out.println("\nAristas del Árbol de Expansión Mínima:");
            for (Arista arista : aristasMST) {
                System.out.println(arista.origen + " -- " + arista.destino + " (peso: " + arista.peso + ")");
            }

            System.out.println("\nEstructura del MST (padre de cada nodo):");
            for (int i = 0; i < numNodos; i++) {
                if (i == origen) {
                    System.out.println("Nodo " + i + ": RAÍZ (origen)");
                } else if (padre[i] != -1) {
                    System.out.println("Nodo " + i + ": padre = " + padre[i] + " (peso: " + key[i] + ")");
                } else {
                    System.out.println("Nodo " + i + ": NO ALCANZABLE desde el origen");
                }
            }

            System.out.println("\nResumen por nodo (grado en el MST):");
            Map<Integer, Integer> grados = calcularGradosMST(numNodos);
            for (Map.Entry<Integer, Integer> entry : grados.entrySet()) {
                System.out.println("Nodo " + entry.getKey() + ": grado " + entry.getValue());
            }
        }

        /*
            Nombre: calcularGradosMST
            Argumentos:
                (int) numNodos: Representa el número total de nodos
            Objetivo: Calcular el grado de cada nodo en el MST
            Retorno: (Map<Integer, Integer>) Mapa con nodo -> grado
         */
        private Map<Integer, Integer> calcularGradosMST(int numNodos) {
            Map<Integer, Integer> grados = new HashMap<>();

            for (int i = 0; i < numNodos; i++) {
                grados.put(i, 0);
            }

            for (Arista arista : aristasMST) {
                grados.put(arista.origen, grados.get(arista.origen) + 1);
                grados.put(arista.destino, grados.get(arista.destino) + 1);
            }

            return grados;
        }
    }

    /*
        Clase interna para representar un elemento en la cola de prioridad
        Utilizada para seleccionar el nodo con el menor peso
     */
    static class NodoPrio implements Comparable<NodoPrio> {
        public int id;          // ID del nodo
        public int peso;        // Peso mínimo para conectar al MST

        public NodoPrio(int id, int peso) {
            this.id = id;
            this.peso = peso;
        }

        /*
            Nombre: compareTo
            Argumentos:
                (NodoPrio) otro: Otro nodo a comparar
            Objetivo: Comparar dos nodos por su peso (para la cola de prioridad)
            Retorno: (int) Resultado de la comparación
         */
        @Override
        public int compareTo(NodoPrio otro) {
            return Integer.compare(this.peso, otro.peso);
        }
    }

    /*
        Nombre: primListaAdyacencia
        Argumentos:
            (List<List<Arista>>) grafo: Representa el grafo como lista de adyacencia
            (int) numNodos: Representa el número total de nodos en el grafo
            (int) origen: Representa el nodo de inicio del algoritmo
        Objetivo: Aplicar el algoritmo de Prim usando lista de adyacencia (más eficiente para grafos densos)
        Retorno: (ResultadoPrim) Resultados del algoritmo
     */
    public static ResultadoPrim primListaAdyacencia(List<List<Arista>> grafo, int numNodos, int origen) {
        if (numNodos <= 0) {
            throw new IllegalArgumentException("Número de nodos debe ser positivo");
        }

        if (origen < 0 || origen >= numNodos) {
            throw new IllegalArgumentException("Nodo origen inválido");
        }

        if (grafo == null) {
            throw new IllegalArgumentException("El grafo no puede ser nulo");
        }

        int[] padre = new int[numNodos];
        int[] key = new int[numNodos];
        boolean[] enMST = new boolean[numNodos];

        for (int i = 0; i < numNodos; i++) {
            key[i] = Integer.MAX_VALUE;
            padre[i] = -1;
            enMST[i] = false;
        }

        key[origen] = 0;

        PriorityQueue<NodoPrio> pq = new PriorityQueue<>();
        pq.add(new NodoPrio(origen, 0));

        List<Arista> mst = new ArrayList<>();
        int costoTotal = 0;

        while (!pq.isEmpty()) {
            NodoPrio nodoPrio = pq.poll();
            int u = nodoPrio.id;

            if (enMST[u]) {
                continue;
            }

            enMST[u] = true;

            if (padre[u] != -1) {
                mst.add(new Arista(padre[u], u, key[u]));
                costoTotal += key[u];
            }

            for (Arista arista : grafo.get(u)) {
                int v = arista.destino;
                int peso = arista.peso;

                if (!enMST[v] && peso < key[v]) {
                    key[v] = peso;
                    padre[v] = u;
                    pq.add(new NodoPrio(v, key[v]));
                }
            }
        }

        boolean esConexo = true;
        for (int i = 0; i < numNodos; i++) {
            if (!enMST[i]) {
                esConexo = false;
                break;
            }
        }

        if (!esConexo) {
            System.out.println("Advertencia: El grafo no es conexo. MST incompleto.");
        }

        return new ResultadoPrim(mst, costoTotal, padre, key);
    }

    /*
        Nombre: primMatrizAdyacencia
        Argumentos:
            (int[][]) matriz: Representa el grafo como matriz de adyacencia
            (int) numNodos: Representa el número total de nodos en el grafo
            (int) origen: Representa el nodo de inicio del algoritmo
        Objetivo: Aplicar el algoritmo de Prim usando matriz de adyacencia (más simple para grafos pequeños)
        Retorno: (ResultadoPrim) Resultados del algoritmo
     */
    public static ResultadoPrim primMatrizAdyacencia(int[][] matriz, int numNodos, int origen) {
        if (numNodos <= 0) {
            throw new IllegalArgumentException("Número de nodos debe ser positivo");
        }

        if (origen < 0 || origen >= numNodos) {
            throw new IllegalArgumentException("Nodo origen inválido");
        }

        if (matriz == null) {
            throw new IllegalArgumentException("La matriz no puede ser nula");
        }

        int[] padre = new int[numNodos];
        int[] key = new int[numNodos];
        boolean[] enMST = new boolean[numNodos];

        for (int i = 0; i < numNodos; i++) {
            key[i] = Integer.MAX_VALUE;
            padre[i] = -1;
            enMST[i] = false;
        }

        key[origen] = 0;

        List<Arista> mst = new ArrayList<>();
        int costoTotal = 0;


        for (int count = 0; count < numNodos; count++) {
            int u = -1;
            int minKey = Integer.MAX_VALUE;

            for (int v = 0; v < numNodos; v++) {
                if (!enMST[v] && key[v] < minKey) {
                    minKey = key[v];
                    u = v;
                }
            }

            if (u == -1) {
                break;
            }

            enMST[u] = true;

            if (padre[u] != -1) {
                mst.add(new Arista(padre[u], u, key[u]));
                costoTotal += key[u];
            }

            for (int v = 0; v < numNodos; v++) {
                if (matriz[u][v] != 0 && !enMST[v] && matriz[u][v] < key[v]) {
                    key[v] = matriz[u][v];
                    padre[v] = u;
                }
            }
        }

        boolean esConexo = true;
        for (int i = 0; i < numNodos; i++) {
            if (!enMST[i]) {
                esConexo = false;
                break;
            }
        }

        if (!esConexo) {
            System.out.println("Advertencia: El grafo no es conexo. MST incompleto.");
        }

        return new ResultadoPrim(mst, costoTotal, padre, key);
    }

    /*
        Nombre: convertirAristasAListaAdyacencia
        Argumentos:
            (List<Arista>) aristas: Lista de aristas del grafo
            (int) numNodos: Número total de nodos
            (boolean) dirigido: Indica si el grafo es dirigido (false para MST)
        Objetivo: Convertir una lista de aristas a una lista de adyacencia
        Retorno: (List<List<Arista>>) Grafo como lista de adyacencia
     */
    public static List<List<Arista>> convertirAristasAListaAdyacencia(List<Arista> aristas, int numNodos, boolean dirigido) {
        List<List<Arista>> grafo = new ArrayList<>();

        for (int i = 0; i < numNodos; i++) {
            grafo.add(new ArrayList<>());
        }

        for (Arista arista : aristas) {
            if (arista.origen < 0 || arista.origen >= numNodos ||
                    arista.destino < 0 || arista.destino >= numNodos) {
                throw new IllegalArgumentException("Nodo inválido en arista: " +
                        arista.origen + " -> " + arista.destino);
            }
            grafo.get(arista.origen).add(arista);
            if (!dirigido) {
                grafo.get(arista.destino).add(new Arista(arista.destino, arista.origen, arista.peso));
            }
        }

        return grafo;
    }

    /*
        Nombre: convertirAristasAMatrizAdyacencia
        Argumentos:
            (List<Arista>) aristas: Lista de aristas del grafo
            (int) numNodos: Número total de nodos
            (boolean) dirigido: Indica si el grafo es dirigido (false para MST)
        Objetivo: Convertir una lista de aristas a una matriz de adyacencia
        Retorno: (int[][]) Matriz de adyacencia
     */
    public static int[][] convertirAristasAMatrizAdyacencia(List<Arista> aristas, int numNodos, boolean dirigido) {
        int[][] matriz = new int[numNodos][numNodos];

        for (int i = 0; i < numNodos; i++) {
            Arrays.fill(matriz[i], 0);
        }

        for (Arista arista : aristas) {
            if (arista.origen < 0 || arista.origen >= numNodos ||
                    arista.destino < 0 || arista.destino >= numNodos) {
                throw new IllegalArgumentException("Nodo inválido en arista: " +
                        arista.origen + " -> " + arista.destino);
            }

            matriz[arista.origen][arista.destino] = arista.peso;
            if (!dirigido) {
                matriz[arista.destino][arista.origen] = arista.peso;
            }
        }

        return matriz;
    }
}