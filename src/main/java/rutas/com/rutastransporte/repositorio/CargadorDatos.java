package rutas.com.rutastransporte.repositorio;

import rutas.com.rutastransporte.modelos.*;
import rutas.com.rutastransporte.servicios.GrafoTransporte;
import rutas.com.rutastransporte.servicios.ServicioEventos;
import rutas.com.rutastransporte.utilidades.ConexionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/*
    Nombre: CargadorDatos
    Tipo: Clase
    Objetivo: Cargar los datos existentes de la base de datos.
 */
public class CargadorDatos {

    /*
        Nombre: cargarDatos
        Argumentos: -
        Objetivo: Cargar las paradas, las rutas y los eventos de la base de datos.
        Retorno: -
     */
    public static void cargarDatos() {
        GrafoTransporte grafo = SistemaTransporte.getSistemaTransporte().getGrafo();
        ServicioEventos servicioEventos = ServicioEventos.getInstancia();

        Map<Integer, Parada> paradasPorCodigo = new HashMap<>();

        String sqlParadas = "SELECT * FROM Paradas";
        String sqlRutas = "SELECT * FROM Rutas";

        try (Connection con = ConexionDB.getConexion();
             Statement stParadas = con.createStatement();
             ResultSet rsParadas = stParadas.executeQuery(sqlParadas)) {

            cargarParadas(rsParadas,grafo,paradasPorCodigo);

            try (Statement stRutas = con.createStatement();
                 ResultSet rsRutas = stRutas.executeQuery(sqlRutas)) {

                cargarRutas(rsRutas,paradasPorCodigo,grafo);
            }

            servicioEventos.cargarEventosActivosDesdeBD();

        } catch (Exception e) {
            System.out.println("ERROR FATAL AL CARGAR DATOS DE LA BBDD:");
        }
    }

    /*
        Nombre: cargarRutas
        Argumentos:
            (ResultSet) rsRutas: Representa los resultados de la operacion SQL.
            (Map<Integer,Parada>) paradasPorCodigo: Representa las paradas.
            (GrafoTransporte) grafo: Representa el grafo que tiene el sistema de transporte.
        Objetivo: Cargar las rutas al sistema de transporte.
        Retorno: -
     */
    private static void cargarRutas(ResultSet rsRutas, Map<Integer,Parada> paradasPorCodigo, GrafoTransporte grafo) throws SQLException {
        while (rsRutas.next()) {

            int origenCodigo = rsRutas.getInt("origen");
            int destinoCodigo = rsRutas.getInt("destino");

            Parada origen = paradasPorCodigo.get(origenCodigo);
            Parada destino = paradasPorCodigo.get(destinoCodigo);

            RutaBuilder ruta = new RutaBuilder()
                    .setCodigo(rsRutas.getInt("codigo"))
                    .setNombre(rsRutas.getString("nombre_ruta"))
                    .setOrigen(origen)
                    .setDestino(destino)
                    .setDistancia(rsRutas.getInt("distancia"))
                    .setCosto(rsRutas.getFloat("costo"))
                    .setTiempo(rsRutas.getInt("tiempo"))
                    .setTrasbordos(rsRutas.getInt("trasbordos"));



            if (origen != null && destino != null) {
                Ruta rt = ruta.construir();
                SistemaTransporte.getSistemaTransporte().getRutas().add(rt);
                grafo.agregarRuta(rt);
            }
        }
    }

    /*
        Nombre: cargarParadas
        Argumentos:
            (ResultSet) rsParadas: Representa los resultados de la operacion SQL.
            (GrafoTransporte) grafo: Representa el grafo de la aplicación.
            (Map<Integer,Parada>) paradasPorCodigo: Representa el mapa que contiene todas las paradas.
        Objetivo: Cargar las paradas al sistema de transporte.
        Retorno: -
     */
    private static void cargarParadas(ResultSet rsParadas, GrafoTransporte grafo, Map<Integer,Parada> paradasPorCodigo) throws SQLException {
        while (rsParadas.next()) {
            ParadaBuilder parada = new ParadaBuilder()
                    .setCodigo(rsParadas.getInt("codigo"))
                    .setNombreParada(rsParadas.getString("nombre_parada"))
                    .setUbicacion(rsParadas.getString("ubicacion"))
                    .setTipo(TipoParada.valueOf(rsParadas.getString("tipo_parada")));


            Parada resultado = parada.construir();
            grafo.agregarParada(resultado);
            SistemaTransporte.getSistemaTransporte().getParadas().add(resultado);
            paradasPorCodigo.put(resultado.getCodigo(), resultado);
        }
    }
}