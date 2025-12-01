<h1>🚎 Programa de Gestión de Rutas de Transporte Público</h1>

<h2>⚙️ Configuración del Entorno (¡Importante!)</h2>
<p>Este proyecto ahora utiliza una base de datos <b>MySQL</b> que corre en un contenedor de <b>Docker</b> para manejar todos los datos de paradas y rutas.
</p>
<p>Para ejecutar el proyecto localmente, sigue estos <b>3 pasos obligatorios</b>:</p>

<h3>🐋 1. Instalar Docker</h3>
<p>Asegúrate de tener <b>Docker Desktop</b> instalado y corriendo en tu computadora.<br>
<a href="https://www.docker.com/products/docker-desktop/">Descargar Docker Desktop</a>
</p>

<h3>📁 2. Iniciar el Contenedor de MySQL</h3>
<p>Abre una terminal (PowerShell o Terminal) y ejecuta el siguiente comando. Esto descargará la imagen de MySQL y la iniciará con la configuración correcta:</p>

<pre><code>docker run -d --name mi-proyecto-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=claveruta mysql:8</code></pre>
<p><i>(Espera un minuto la primera vez que lo ejecutes mientras descarga la imagen).</i></p>

<h3>📚 3. Crear y Poblar las Tablas</h3>
<p>Una vez que el contenedor esté corriendo (puedes verlo en Docker Desktop):</p>
<ol>
    <li>Conéctate a la base de datos local (<code>localhost:3306</code>, usuario <code>root</code>, contraseña <code>claveruta</code>) usando un cliente como DBeaver.</li>
    <li>Ejecuta el script <code><b>database/schema.sql</b></code> para crear las tablas (<code>paradas</code>, <code>rutas</code>, <code>eventos</code>).</li>
    <li>Ejecuta el script <code><b>database/data.sql</b></code> para insertar los datos de prueba.</li>
</ol>
<p>¡Después de esto, ya puedes ejecutar la aplicación desde IntelliJ!</p>

<hr>

<h2>✅ Objetivos</h2>
<p>El programa tiene como objetivo simular un sistema de transporte público,
utilizando un grafo para la representación de paradas (equivalente a un vertice) y rutas (equivalente a una arista).
</p>

<p>El programa utiliza los algoritmos de Dijkstra y Bellman-Ford para obtener la mejor ruta disponible entre 2 paradas.
</p>

<hr>

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

<hr>

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

<h2>⚓ Estructura del Proyecto</h2>
<p>
    El proyecto se encuentra dividido en una estructura que engloba las funcionalidades de acuerdo a los objetivos generales que tienen dentro del proyecto.
    en la definición general del proyecto se encuentran los archivos <coode>Informe.pdf</coode> que contiene la descripción y funcionamiento general detallado del proyecto,
    el archivo <code>uml.puml</code> que contiene el diagrama UML de la lógica utilizada en el proyecto y las carpetas <code>database y src</code> que contienen la lógica vinculada al programa.
</p>

<h3>🔗 Elementos de Codificación</h3>
<p>
    En la carpeta <code>java</code> se encuentran los elementos vinculados con la lógica del programa y su funcionamiento.
    La estructura que sigue el proyecto en general es la siguiente:
</p>
<ol>
    <li><b>Controladores:</b> Contiene la lógica de los componentes visuales del programa.</li>
    <li><b>Excepciones:</b> Contiene la lógica de las excepciones personalizadas que utiliza el programa.</li>
    <li><b>Modelos:</b> Almacena los componentes estructurales del programa.</li>
    <li><b>Repositorio:</b> Almacena las clases que se utilizan como fuentes de datos.</li>
    <li><b>Servicios:</b> Contiene la definición de las clases que se utilizan para registionar el funcionamiento del programa.</li>
    <li><b>Utilidades:</b> Almacena los componentes que sirven para complementar el comportamiento de otros apartados.</li>
    <li><b>Utilidades > Alertas:</b> Contiene la definición de las alertas generales que utiliza el programa.</li>
</ol>

<h3>🎬 Recursos Visuales</h3>
<p>
    La carpeta <code>resources</code> contiene todos los recursos visuales que el programa utiliza, contiene los estilos CSS, las imágenes y los archivos FXML asociados a los apartados del programa.
    La estructura de los recursos visuales tiene la siguiente lógica:
</p>
<ol>
    <li><b>Estilos:</b> Contiene los documentos CSS que estan vinculados a utilidades específicas del programa.</li>
    <li><b>Imágenes:</b> Contiene las imágenes que se utilizan como complemento del diseño.</li>
    <li><b>rutastransporte</b> Almacena los documentos FXML que se utilizan como recursos visuales.</li>
</ol>

<h2>⏩  Pruebas Realizadas</h2>
<p>
    El programa fue probado en dsitentas condiciones para evidenciar el funcionamiento 
    ante circunstancias específicas. El caso base que se proporciona en <code>schema.sql</code> plantea un total
    de 20 paradas y 40 rutas paran lograr probar el comportamiento del mapa de rutas de transporte. El programa también fue probado en escenarios con
    100 paradasy 200 rutas de transporte.
</p>