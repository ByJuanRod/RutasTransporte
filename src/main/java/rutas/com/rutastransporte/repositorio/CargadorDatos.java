package rutas.com.rutastransporte.repositorio;

import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoParada;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.servicios.GrafoTransporte;
import rutas.com.rutastransporte.servicios.ServicioEventos;
import rutas.com.rutastransporte.utilidades.ConexionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class CargadorDatos {

    public static void cargarDatos() {
        GrafoTransporte grafo = SistemaTransporte.getSistemaTransporte().getGrafo();
        ServicioEventos servicioEventos = ServicioEventos.getInstancia();

        Map<Integer, Parada> paradasPorCodigo = new HashMap<>();
        Map<Integer, Ruta> rutasPorCodigo = new HashMap<>();

        String sqlParadas = "SELECT * FROM Paradas";
        String sqlRutas = "SELECT * FROM Rutas";
        String sqlEventosActivos = "SELECT * FROM Eventos WHERE fecha_fin > NOW()";

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
                        rutasPorCodigo.put(codigo, ruta);
                    }
                }
            }

            try (Statement stEventos = con.createStatement();
                 ResultSet rsEventos = stEventos.executeQuery(sqlEventosActivos)) {

                while (rsEventos.next()) {
                    int codigoRuta = rsEventos.getInt("ruta");
                    String tipoEventoStr = rsEventos.getString("tipo_evento");
                    Timestamp fechaFin = rsEventos.getTimestamp("fecha_fin");

                    Ruta ruta = rutasPorCodigo.get(codigoRuta);

                    if (ruta != null) {
                        try {
                            TipoEvento tipoEvento = TipoEvento.valueOf(tipoEventoStr);

                            long duracionRestante = (fechaFin.getTime() - System.currentTimeMillis()) / (60 * 1000);

                            if (duracionRestante > 0) {
                                servicioEventos.aplicarEventoARuta(ruta, tipoEvento, (int) duracionRestante);
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println("ADVERTENCIA: Tipo de evento no válido '" + tipoEventoStr +
                                    "' para ruta " + ruta.getNombre());
                        }
                    }
                }
            }
            servicioEventos.limpiarEventosExpiradosBD();

        } catch (Exception e) {
            System.out.println("ERROR FATAL AL CARGAR DATOS DE LA BBDD:");
        }
    }
}