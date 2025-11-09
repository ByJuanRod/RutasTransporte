<h1>🚎 Programa de Gestión de Rutas de Transporte Público</h1>
<hr>

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


