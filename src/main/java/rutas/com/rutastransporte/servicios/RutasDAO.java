package rutas.com.rutastransporte.servicios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rutas.com.rutastransporte.excepciones.NotRemovableException;
import rutas.com.rutastransporte.modelos.CRUD;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;
import rutas.com.rutastransporte.utilidades.ConexionDB;

import java.sql.*;
import java.util.Iterator;

/*
    Nombre: RutasDAO
    Tipo: Clase Implementa -> CRUD<Ruta>
    Objetivos: Almacenar la logica del servicio de gestión de rutas.
 */
public class RutasDAO implements CRUD<Ruta> {

    /*
        Nombre: insertar
        Argumentos:
            (Ruta) ruta: Representa la ruta que se va a insertar.
        Objetivo: Insertar una parada a la base de datos.
        Retorno: (boolean) Retorna true si la ruta se insertó correctamente.
                           Retorna false si la ruta no se logró insertar correctamente.
     */
    @Override
    public boolean insertar(Ruta ruta) {
        String sql = "INSERT INTO Rutas (nombre_ruta, distancia, costo, tiempo, trasbordos, origen, destino) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setDatos(ruta, pst);

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ruta.setCodigo(generatedKeys.getInt(1));
                        SistemaTransporte.getSistemaTransporte().getRutas().add(ruta);
                        SistemaTransporte.getSistemaTransporte().getGrafo().agregarRuta(ruta);
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /*
        Nombre: setDatos
        Argumentos:
            (Ruta) ruta: Representa la ruta que se tomará de referencia de los datos.
            (PreparedStatement) pst: Representa el comando SQL.
        Objetivo: Aplicar los valores de los datos de la ruta.
        Retorno: -
        Arroja: (SQLException)
     */
    private void setDatos(Ruta ruta, PreparedStatement pst) throws SQLException {
        pst.setString(1, ruta.getNombre());
        pst.setInt(2, ruta.getDistancia());
        pst.setFloat(3, ruta.getCosto());
        pst.setInt(4, ruta.getTiempo());
        pst.setInt(5, ruta.getTrasbordos());
        pst.setInt(6, ruta.getOrigen().getCodigo());
        pst.setInt(7, ruta.getDestino().getCodigo());
    }

    /*
        Nombre: actualizar
        Argumentos:
            (Ruta) rutaActualizada: Representa la ruta que se va a actualizar.
        Objetivo: Actualizar una ruta en la base de datos.
        Retorno: (boolean) Retorna true si la ruta se logró actualizar exitosamente.
                           Retorna false si la ruta no se logró eliminar.
     */
    @Override
    public boolean actualizar(Ruta rutaActualizada) {
        String sql = "UPDATE Rutas SET nombre_ruta = ?, distancia = ?, costo = ?, tiempo = ?, trasbordos = ?, origen = ?, destino = ? WHERE codigo = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            setDatos(rutaActualizada, pst);
            pst.setInt(8, rutaActualizada.getCodigo());

            pst.executeUpdate();

            SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();
            for (int i = 0; i < sistema.getRutas().size(); i++) {
                if (sistema.getRutas().get(i).getCodigo() == rutaActualizada.getCodigo()) {
                    sistema.getRutas().set(i, rutaActualizada);
                    break;
                }
            }

            sistema.getGrafo().actualizarRuta(rutaActualizada);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /*
        Nombre: eliminar
        Argumentos:
            (Ruta) ruta: Representa la ruta que se va a eliminar
        Objetivo: Eliminar una ruta de la base de datos.
        Retorno: -
        Arroja (NotRemovableException)
     */
    @Override
    public void eliminar(Ruta ruta) throws NotRemovableException {

        String sqlDelete = "DELETE FROM Rutas WHERE codigo = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pstDelete = con.prepareStatement(sqlDelete)) {

            pstDelete.setInt(1, ruta.getCodigo());
            int filasAfectadas = pstDelete.executeUpdate();

            if (filasAfectadas > 0) {
                SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();
                Iterator<Ruta> iterator = sistema.getRutas().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getCodigo() == ruta.getCodigo()) {
                        iterator.remove();
                        break;
                    }
                }
                sistema.getGrafo().eliminarRuta(ruta);

            }
        } catch (SQLException e) {
            throw new NotRemovableException("Error de SQL al eliminar la ruta: " + e.getMessage());
        }
    }

    /*
        Nombre: getRutas
        Argumentos: -
        Objetivo: Obtener un listado de todas las rutas.
        Retorno: (ObservableList<Ruta>) Retorna una lista de todas las rutas.
     */
    public ObservableList<Ruta> getRutas() {
        ObservableList<Ruta> rutas = FXCollections.observableArrayList();
        rutas.addAll(SistemaTransporte.getSistemaTransporte().getRutas());
        return rutas;
    }
}