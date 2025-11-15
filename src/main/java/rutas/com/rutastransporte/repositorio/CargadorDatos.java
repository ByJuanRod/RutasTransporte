
package rutas.com.rutastransporte.repositorio;

import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoParada;
import rutas.com.rutastransporte.servicios.GrafoTransporte;
import rutas.com.rutastransporte.utilidades.ConexionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CargadorDatos {

    public static void cargarDatos() {

        GrafoTransporte grafo = SistemaTransporte.getSistemaTransporte().getGrafo();

        Map<Integer, Parada> paradasPorCodigo = new HashMap<>();

        String sqlParadas = "SELECT * FROM Paradas";
        String sqlRutas = "SELECT * FROM Rutas";

        System.out.println("Iniciando carga de datos desde MySQL...");

        try (Connection con = ConexionDB.getConexion();
             Statement stParadas = con.createStatement();
             ResultSet rsParadas = stParadas.executeQuery(sqlParadas)) {

            while (rsParadas.next()) {
                int codigo = rsParadas.getInt("codigo");
                String nombre = rsParadas.getString("nombre_parada");
                String ubicacion = rsParadas.getString("ubicacion");
                TipoParada tipo = TipoParada.valueOf(rsParadas.getString("tipo_parada"));

                Parada parada = new Parada(codigo, nombre, tipo, ubicacion);

                grafo.agregarParada(parada);
                SistemaTransporte.getSistemaTransporte().getParadas().add(parada);
                paradasPorCodigo.put(codigo, parada);
            }

            try (Statement stRutas = con.createStatement();
                 ResultSet rsRutas = stRutas.executeQuery(sqlRutas)) {

                int rutasCargadas = 0;
                while (rsRutas.next()) {
                    int codigo = rsRutas.getInt("codigo");
                    String nombre = rsRutas.getString("nombre_ruta");

                    int origenCodigo = rsRutas.getInt("origen");
                    int destinoCodigo = rsRutas.getInt("destino");

                    Parada origen = paradasPorCodigo.get(origenCodigo);
                    Parada destino = paradasPorCodigo.get(destinoCodigo);

                    if (origen != null && destino != null) {
                        Ruta ruta = new Ruta(
                                codigo,
                                nombre,
                                origen,
                                destino,
                                rsRutas.getInt("distancia"),
                                rsRutas.getFloat("costo"),
                                rsRutas.getInt("tiempo"),
                                rsRutas.getInt("trasbordos")
                        );

                        SistemaTransporte.getSistemaTransporte().getRutas().add(ruta);
                        grafo.agregarRuta(ruta);
                        rutasCargadas++;
                    } else {
                        System.out.println("ADVERTENCIA: No se pudo cargar ruta " + codigo +
                                " (origen o destino no encontrado).");
                    }
                }
                System.out.println("Cargadas " + rutasCargadas + " rutas desde la BBDD.");
            }

        } catch (Exception e) {
            System.out.println("ERROR FATAL AL CARGAR DATOS DE LA BBDD:");
            e.printStackTrace();
        }
    }
}