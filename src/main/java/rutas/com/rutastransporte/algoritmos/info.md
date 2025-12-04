<h1>📊 Algoritmos Complementarios</h1>

<p>
Los algoritmos complementarios son los algoritmos que dentro del código no se implementan por las características de 
los grafos dirigidos y las condiciones establecidas por el proyecto.
</p>

<h2>Bellman-Ford</h2>
<p>El objetivo principal del algoritmo de Bellman-Ford es encontrar el camino más corto desde un nodo origen a todos los demás nodos en un grafo ponderado dirigido (también funciona para no dirigidos si se tratan como dos aristas dirigidas), incluso cuando los pesos de las aristas pueden ser negativos.</p>

<h3>Casos de Uso</h3>
<ul>
    <li>Calcular distancias mínimas desde un nodo fuente a todos los demás vértices.</li>
    <li>Detectar ciclos negativos alcanzables desde el nodo origen.</li>
    <li>Funcionar con pesos negativos (a diferencia de Dijkstra que requiere pesos no negativos).</li>
</ul>

<h2>Algoritmo de Kruskal</h2>
<p>
Encontrar el árbol de expansión mínima (Minimum Spanning Tree - MST) de un grafo conexo ponderado no dirigido, es decir, un subconjunto de aristas que conecte todos los vértices,
 forme un árbol (sin ciclos) y minimice la suma total de los pesos de las aristas
</p>

<h3>Casos de Uso</h3>
<ul>
    <li>Cuando se desea garantizar el costo mínimo.</li>
    <li>Evita conexiones redundantes.</li>
    <li>Cuando se desea identificar conexiones críticas dentro un árbol.</li>
</ul>

<h2>Algoritmo de Prim</h2>
<p>
Encontrar el árbol de expansión mínima (Minimum Spanning Tree - MST) de un grafo conexo ponderado no dirigido, construyéndolo incrementalmente desde un nodo inicial.
</p>

<h3>Casos de Uso</h3>
<ul>
    <li>Cuando se desea optimizar grafos que son muy densos.</li>
    <li>Cuando se desea agrupar de forma jerárquica los resultados.</li>
    <li>Produce el mismo MST para un vértice inicial dado.</li>

</ul>