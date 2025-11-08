package rutas.com.rutastransporte.servicios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rutas.com.rutastransporte.excepciones.NotRemovableException;
import rutas.com.rutastransporte.modelos.CRUD;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.repositorio.SistemaTransporte;

import java.util.Iterator;

public class RutasDAO implements CRUD<Ruta> {

    @Override
    public void insertar(Ruta ruta) {
        SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();

        ruta.setCodigo("R0" + (sistema.getRutas().size() + 1));
        sistema.getRutas().add(ruta);
        sistema.getGrafo().agregarRuta(ruta);
    }

    @Override
    public Ruta buscarByCodigo(String codigo) {
        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getCodigo().equals(codigo)) {
                return ruta;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Ruta rutaActualizada) {
        SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();

        for (int i = 0; i < sistema.getRutas().size(); i++) {
            Ruta rutaExistente = sistema.getRutas().get(i);
            if (rutaExistente.getCodigo().equals(rutaActualizada.getCodigo())) {
                rutaExistente.setNombre(rutaActualizada.getNombre());
                rutaExistente.setOrigen(rutaActualizada.getOrigen());
                rutaExistente.setDestino(rutaActualizada.getDestino());
                rutaExistente.setDistancia(rutaActualizada.getDistancia());
                rutaExistente.setCosto(rutaActualizada.getCosto());
                rutaExistente.setTiempo(rutaActualizada.getTiempo());
                break;
            }
        }

        sistema.getGrafo().eliminarRuta(buscarByCodigo(rutaActualizada.getCodigo()));
        sistema.getGrafo().agregarRuta(rutaActualizada);
    }

    @Override
    public void eliminar(Ruta ruta) throws NotRemovableException {
        SistemaTransporte sistema = SistemaTransporte.getSistemaTransporte();

        if (!sistema.getRutas().contains(ruta)) {
            throw new NotRemovableException("La ruta no existe en el sistema.");
        }

        Iterator<Ruta> iterator = sistema.getRutas().iterator();
        while (iterator.hasNext()) {
            Ruta r = iterator.next();
            if (r.equals(ruta)) {
                iterator.remove();
                break;
            }
        }

        sistema.getGrafo().eliminarRuta(ruta);
    }

    public ObservableList<Ruta> getRutas() {
        ObservableList<Ruta> rutas = FXCollections.observableArrayList();
        rutas.addAll(SistemaTransporte.getSistemaTransporte().getRutas());
        return rutas;
    }

    public ObservableList<Ruta> buscarPorNombre(String nombre) {
        ObservableList<Ruta> resultado = FXCollections.observableArrayList();
        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(ruta);
            }
        }
        return resultado;
    }

    public ObservableList<Ruta> buscarPorOrigen(String origen) {
        ObservableList<Ruta> resultado = FXCollections.observableArrayList();
        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getOrigen().getNombreParada().toLowerCase().contains(origen.toLowerCase())) {
                resultado.add(ruta);
            }
        }
        return resultado;
    }

    public ObservableList<Ruta> buscarPorDestino(String destino) {
        ObservableList<Ruta> resultado = FXCollections.observableArrayList();
        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getDestino().getNombreParada().toLowerCase().contains(destino.toLowerCase())) {
                resultado.add(ruta);
            }
        }
        return resultado;
    }
}