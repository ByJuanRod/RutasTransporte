package rutas.com.rutastransporte.servicios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rutas.com.rutastransporte.excepciones.NotRemovableException;
import rutas.com.rutastransporte.modelos.CRUD;
import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;
import rutas.com.rutastransporte.utilidades.ConexionDB;

import java.sql.*;
import java.util.Iterator;

public class ParadasDAO implements CRUD<Parada> {
    @Override
    public boolean insertar(Parada parada) {
        String sql = "INSERT INTO Paradas (nombre_parada, tipo_parada, ubicacion) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, parada.getNombreParada());
            pst.setString(2, parada.getTipo().name());
            pst.setString(3, parada.getUbicacion());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        parada.setCodigo(generatedKeys.getInt(1));
                        SistemaTransporte.getSistemaTransporte().getParadas().add(parada);
                        SistemaTransporte.getSistemaTransporte().getGrafo().agregarParada(parada);
                    }
                }
            }

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean actualizar(Parada paradaActualizada) {
        String sql = "UPDATE Paradas SET nombre_parada = ?, tipo_parada = ?, ubicacion = ? WHERE codigo = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, paradaActualizada.getNombreParada());
            pst.setString(2, paradaActualizada.getTipo().name());
            pst.setString(3, paradaActualizada.getUbicacion());
            pst.setInt(4, paradaActualizada.getCodigo());

            pst.executeUpdate();

            SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();
            for (int i = 0; i < sistema.getParadas().size(); i++) {
                if (sistema.getParadas().get(i).getCodigo() == paradaActualizada.getCodigo()) {
                    sistema.getParadas().set(i, paradaActualizada);
                    break;
                }
            }

            sistema.getGrafo().actualizarParada(paradaActualizada);
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void eliminar(Parada parada) throws NotRemovableException {
        String sqlCheck = "SELECT COUNT(*) as count FROM Rutas WHERE origen = ? OR destino = ?";
        String sqlDelete = "DELETE FROM Paradas WHERE codigo = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pstCheck = con.prepareStatement(sqlCheck);
             PreparedStatement pstDelete = con.prepareStatement(sqlDelete)) {

            pstCheck.setInt(1, parada.getCodigo());
            pstCheck.setInt(2, parada.getCodigo());
            ResultSet rs = pstCheck.executeQuery();

            if (rs.next() && rs.getInt("count") > 0) {
                throw new NotRemovableException("No se puede eliminar la parada porque está siendo utilizada en una o más rutas");
            }

            pstDelete.setInt(1, parada.getCodigo());
            pstDelete.executeUpdate();

            SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();
            Iterator<Parada> iterator = sistema.getParadas().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getCodigo() == parada.getCodigo()) {
                    iterator.remove();
                    break;
                }
            }

            sistema.getGrafo().eliminarParada(parada);
        } catch (SQLException e) {
            throw new NotRemovableException("Error al eliminar la parada: " + e.getMessage());
        }
    }

    public ObservableList<Parada> getParadas() {
        ObservableList<Parada> paradas = FXCollections.observableArrayList();
        paradas.addAll(SistemaTransporte.getSistemaTransporte().getParadas());
        return paradas;
    }
}