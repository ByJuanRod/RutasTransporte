<h1>🚎 Programa de Gestión de Rutas de Transporte Público</h1>
<h2>✅ Objetivos</h2>
<p>El programa tiene como objetivo simular un sistema de transporte público,
utilizando un grafo para la representación de paradas (equivalente a un vertice) y rutas (equivalente a una arista).
</p>

<p>El programa utiliza los algoritmos de Dijkstra y Bellman-Ford para obtener la mejor ruta disponible entre 2 paradas.
</p>

<h2>🔰 Descripción del funcionamiento</h2>

<p> El programa tiene de apartados las secciones <b>Principal, Paradas, Rutas y Mapa</b> en estas vistas se plantean las bases
del funcionamiento general.<br><br>

<b>📊 Principal: </b> En este apartado se muestran los resumenes del programa, mostrando la cantidad de rutas y paradas existentes,
el precio promedio de las rutas y la segmentación de las paradas por su método de transporte.
<br><br>
<b>🏣 Paradas: </b> En este apartado se muestra el listado de todas las paradas disponibles y se permite insertar, eliminar o actualizar
una parada. Para poder eliminar una parada se debe asegurar de que esta no se cuentre vinculada con alguna ruta.
<br><br>
<b>🌐 Rutas: </b> En esta sección se muestra un listado de todas las rutas existentes y al igual que en la vista de las paradas se
permite actualizar, insertar y eliminar una ruta. Para insertar o modificar una ruta deben existir aunque sea 2 paradas (correspondientes a
origen y destino).
<br><br>
<b>📜 Mapa: </b>El apartado de mapa engloba la lógica principal permitiendo al usuario seleccionar 2 paradas dentro del grafo y luego calcular la ruta
que mejor se adapte a ser la ruta más económica, más rápida, más corta y la que menos trasbordos tiene. Dependiendo del escenario
se puede producir una mejor ruta (que es aquella que cumple con más de un criterio) o puede  que retorne opciones alternativas, en este caso 
el usuario podra seleccionar la opción que más se adapte a lo que necesite.

Luego de que el usuario seleccione el camino este podra visualizar el resumen de los resultados. En estos resultados se visualizara detalladamente
los criterios con los que el camino logro ser una opción destacada, los detalles respecto a la cantidad de trasbordos, el tiempo estimado de la ruta, la distancia y el costo.
Además, este resumen muestra si dentro del camino suceden, desvios, accidentes, zonas concurridas o calles libres que reduzcan el tiempo el de la ruta.
</p>

<h2>🚦 Simulaciones y Criterios</h2>

<h3>🚧 Simulaciones</h3>
<p>
Las simulaciones son eventos que suceden en la vida real. En el programa se maneja de forma aleatoria la simulación de como estos
acontecen y pueden suceder 4 escenarios que pueden afectar el tiempo, costo y distancia que toma un camino.<br>
<br>
<b>⚠️ Accidentes: </b> Los accidentes afectan a las rutas agregandole un 50% más de tiempo y un 100% a la distancia forzando a utilizar una ruta alternativa en el peor de los casos.<br>
<b>↪️ Desvios: </b> Los desvios incrementan un 30% el tiempo que toma completar una ruta y un 50% la distancia de la ruta.<br>
<b>🏁 Camino Libre: </b> Los caminos libres reducen el tiempo en un 30% y la distancia en un 10%.<br>
<b>🚘 Zonas concurridas: </b> Las zonas concurridas incrementan un 40% el tiempo que toma completar una ruta y aumenta la tarifa de costo en un 20%.<br>

</p>

<h3>📍 Criterios</h3>
<p>
Los criterios son los métodos que utiliza el programa para determinar bajo que contexto se produce la mejor ruta o las posibles rutas alternativas que cumplen
con algun criterio en especifico. Los criterios disponibles dentro del programa son:
</p>
<ul>
    <li>Ruta Más Corta</li>
    <li>Ruta Más Rápida</li>
    <li>Ruta Más Económica</li>
    <li>Ruta Con Menos Trasbordos</li>
</ul>

