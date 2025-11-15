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

public class RutasDAO implements CRUD<Ruta> {

    @Override
    public void insertar(Ruta ruta) {
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
        } catch (SQLException e) {
            System.out.println("Error al insertar ruta: " + e.getMessage());
        }
    }

    private void setDatos(Ruta ruta, PreparedStatement pst) throws SQLException {
        pst.setString(1, ruta.getNombre());
        pst.setInt(2, ruta.getDistancia());
        pst.setFloat(3, ruta.getCosto());
        pst.setInt(4, ruta.getTiempo());
        pst.setInt(5, ruta.getTrasbordos());
        pst.setInt(6, ruta.getOrigen().getCodigo());
        pst.setInt(7, ruta.getDestino().getCodigo());
    }

    @Override
    public void actualizar(Ruta rutaActualizada) {
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

        } catch (SQLException e) {
            System.out.println("Error al actualizar ruta: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Ruta ruta) throws NotRemovableException {
        String sqlCheck = "SELECT COUNT(*) as count FROM Rutas WHERE origen = ? OR destino = ?";
        String sqlDelete = "DELETE FROM Rutas WHERE codigo = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pstCheck = con.prepareStatement(sqlCheck);
             PreparedStatement pstDelete = con.prepareStatement(sqlDelete)) {

            pstCheck.setInt(1, ruta.getCodigo());
            pstCheck.setInt(2, ruta.getCodigo());
            ResultSet rs = pstCheck.executeQuery();

            if (rs.next() && rs.getInt("count") > 0) {
                throw new NotRemovableException("No se puede eliminar la ruta porque es referenciada por otras rutas.");
            }

            pstDelete.setInt(1, ruta.getCodigo());
            pstDelete.executeUpdate();

            SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();
            Iterator<Ruta> iterator = sistema.getRutas().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getCodigo() == ruta.getCodigo()) {
                    iterator.remove();
                    break;
                }
            }

            sistema.getGrafo().eliminarRuta(ruta);

        } catch (SQLException e) {
            System.out.println("Error al eliminar ruta: " + e.getMessage());
            throw new NotRemovableException("Error al eliminar la ruta: " + e.getMessage());
        }
    }

    public ObservableList<Ruta> getRutas() {
        ObservableList<Ruta> rutas = FXCollections.observableArrayList();
        rutas.addAll(SistemaTransporte.getSistemaTransporte().getRutas());
        return rutas;
    }
}