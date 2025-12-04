package rutas.com.rutastransporte.algoritmos;
import java.util.*;

/*
    Nombre: Kruskal
    Tipo: Clase
    Objetivo: Aplicar el algoritmo de Kruskal para encontrar el Árbol de Expansión Mínima (MST)
 */
public class Kruskal {

    /*
        Nombre: ResultadoKruskal
        Tipo: Clase
        Objetivo: Almacenar los resultados obtenidos del algoritmo de Kruskal
     */
    static class ResultadoKruskal {
        List<Arista> aristasMST;   // Lista de aristas en el Árbol de Expansión Mínima
        int costoTotal;             // Costo total del MST
        int aristasAgregadas;       // Número de aristas agregadas al MST

        public ResultadoKruskal(List<Arista> aristasMST, int costoTotal, int aristasAgregadas) {
            this.aristasMST = aristasMST;
            this.costoTotal = costoTotal;
            this.aristasAgregadas = aristasAgregadas;
        }

        /*
            Nombre: imprimirResultados
            Argumentos:
                (int) numNodos: Representa el número total de nodos en el grafo
            Objetivo: Imprimir los resultados obtenidos del algoritmo de Kruskal
            Retorno: -
         */
        public void imprimirResultados(int numNodos) {
            System.out.println("\n=== RESULTADOS ALGORITMO KRUSKAL ===");
            System.out.println("Número de nodos en el grafo: " + numNodos);
            System.out.println("Número de aristas en el MST: " + aristasAgregadas);
            System.out.println("Costo total del MST: " + costoTotal);

            if (aristasAgregadas == numNodos - 1) {
                System.out.println("MST completo encontrado (V-1 aristas)");
            } else {
                System.out.println("MST incompleto. El grafo puede no ser conexo");
            }

            System.out.println("\nAristas del Árbol de Expansión Mínima:");
            for (Arista arista : aristasMST) {
                System.out.println(arista.origen + " -- " + arista.destino + " (peso: " + arista.peso + ")");
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
        Clase interna para implementar la estructura Union-Find (Conjuntos Disjuntos)
        utilizada en el algoritmo de Kruskal
     */
    static class UnionFind {
        private int[] parent;  // Padre de cada nodo
        private int[] rank;    // Rango (altura) de cada árbol

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        /*
            Nombre: find
            Argumentos:
                (int) x: Elemento a buscar
            Objetivo: Encontrar el representante (raíz) del conjunto que contiene a x
            Retorno: (int) Representante del conjunto
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        /*
            Nombre: union
            Argumentos:
                (int) x: Primer elemento
                (int) y: Segundo elemento
            Objetivo: Unir los conjuntos que contienen a x e y
            Retorno: (boolean) true si la unión fue exitosa, false si ya estaban en el mismo conjunto
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false;
            }

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }
    }

    /*
        Nombre: kruskal
        Argumentos:
            (List<Arista>) aristas: Representa todas las aristas del grafo
            (int) numNodos: Representa el número total de nodos en el grafo
        Objetivo: Aplicar el algoritmo de Kruskal para encontrar el MST
        Retorno: (ResultadoKruskal) Resultados del algoritmo
     */
    public static ResultadoKruskal kruskal(List<Arista> aristas, int numNodos) {
        if (numNodos <= 0) {
            throw new IllegalArgumentException("Número de nodos debe ser positivo");
        }

        if (aristas == null || aristas.isEmpty()) {
            throw new IllegalArgumentException("La lista de aristas no puede estar vacía");
        }

        List<Arista> mst = new ArrayList<>();

        aristas.sort(new Comparator<Arista>() {
            @Override
            public int compare(Arista a1, Arista a2) {
                return Integer.compare(a1.peso, a2.peso);
            }
        });

        UnionFind uf = new UnionFind(numNodos);

        int costoTotal = 0;
        int aristasAgregadas = 0;

        for (Arista arista : aristas) {
            if (arista.origen < 0 || arista.origen >= numNodos ||
                    arista.destino < 0 || arista.destino >= numNodos) {
                throw new IllegalArgumentException("Nodo inválido en arista: " +
                        arista.origen + " -> " + arista.destino);
            }

            if (uf.union(arista.origen, arista.destino)) {
                mst.add(arista);
                costoTotal += arista.peso;
                aristasAgregadas++;

                if (aristasAgregadas == numNodos - 1) {
                    break;
                }
            }
        }

        return new ResultadoKruskal(mst, costoTotal, aristasAgregadas);
    }

}