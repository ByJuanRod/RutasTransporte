
package rutas.com.rutastransporte.Repositorio;

import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Modelos.TipoParada;
import rutas.com.rutastransporte.Servicios.GrafoTransporte;

public class CargadorDatos {

    public static void cargarDatosEjemplo() {
        GrafoTransporte grafo = SistemaTransporte.getSistemaTransporte().getGrafo();

        Parada[] paradas = new Parada[10];

        paradas[0] = new Parada("P001", "Estación Central", TipoParada.TREN, "Centro Ciudad");
        paradas[1] = new Parada("P002", "Plaza Mayor", TipoParada.BUS, "Zona Centro");
        paradas[2] = new Parada("P003", "Universidad", TipoParada.BUS, "Campus Norte");
        paradas[3] = new Parada("P004", "Hospital General", TipoParada.BUS, "Zona Médica");
        paradas[4] = new Parada("P005", "Centro Comercial", TipoParada.TREN, "Area Comercial");
        paradas[5] = new Parada("P006", "Aeropuerto", TipoParada.BUS, "Terminal Aérea");
        paradas[6] = new Parada("P007", "Terminal Norte", TipoParada.TREN, "Zona Norte");
        paradas[7] = new Parada("P008", "Parque Industrial", TipoParada.BUS, "Zona Industrial");
        paradas[8] = new Parada("P009", "Playa", TipoParada.TAXI, "Costa Este");
        paradas[9] = new Parada("P010", "Estadio", TipoParada.BUS, "Complejo Deportivo");

        for (Parada parada : paradas) {
            SistemaTransporte.getSistemaTransporte().getParadas().add(parada);
            grafo.agregarParada(parada);
        }

        Ruta[] rutas = new Ruta[10];

        rutas[0] = new Ruta("R001", "Ruta Centro-Universidad",
                paradas[0], paradas[2], 5.2f, 500f, 15.0f);

        rutas[1] = new Ruta("R002", "Ruta Universidad-Hospital",
                paradas[2], paradas[3], 3.8f, 500f, 10.0f);

        rutas[2] = new Ruta("R003", "Ruta Hospital-Centro Comercial",
                paradas[3], paradas[4], 4.5f, 900f, 12.0f);

        rutas[3] = new Ruta("R004", "Ruta Centro Comercial-Aeropuerto",
                paradas[4], paradas[5], 12.7f, 350f, 25.0f);

        rutas[4] = new Ruta("R005", "Ruta Aeropuerto-Terminal Norte",
                paradas[5], paradas[6], 8.3f, 330f, 18.0f);

        rutas[5] = new Ruta("R006", "Ruta Terminal Norte-Parque Industrial",
                paradas[6], paradas[7], 6.1f, 350f, 14.0f);

        rutas[6] = new Ruta("R007", "Ruta Parque Industrial-Playa",
                paradas[7], paradas[8], 9.4f, 540f, 22.0f);

        rutas[7] = new Ruta("R008", "Ruta Playa-Estadio",
                paradas[8], paradas[9], 7.2f, 350f, 16.0f);

        rutas[8] = new Ruta("R009", "Ruta Estadio-Plaza Mayor",
                paradas[9], paradas[1], 4.8f, 580f, 11.0f);

        rutas[9] = new Ruta("R010", "Ruta Plaza Mayor-Estación Central",
                paradas[1], paradas[0], 2.5f, 950f, 8.0f);

        for (Ruta ruta : rutas) {
            SistemaTransporte.getSistemaTransporte().getRutas().add(ruta);
            grafo.agregarRuta(ruta);
        }
    }
}