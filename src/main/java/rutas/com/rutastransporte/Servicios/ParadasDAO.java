package rutas.com.rutastransporte.Servicios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rutas.com.rutastransporte.Excepciones.NotEliminableException;
import rutas.com.rutastransporte.Modelos.CRUD;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Repositorio.SistemaTransporte;

import java.util.Iterator;

public class ParadasDAO implements CRUD<Parada> {

    @Override
    public void insertar(Parada parada) {
        SistemaTransporte.getSistemaTransporte().getParadas().add(parada);
        SistemaTransporte.getSistemaTransporte().getGrafo().agregarParada(parada);
    }

    @Override
    public Parada buscarByCodigo(String codigo) {
        for(Parada parada :SistemaTransporte.getSistemaTransporte().getParadas()){
            if(parada.getCodigo().equals(codigo))
                return parada;
        }

        return null;
    }

    @Override
    public void actualizar(Parada parada) {
        for (int i = 0; i < SistemaTransporte.getSistemaTransporte().getParadas().size(); i++) {
            Parada paradaExistente = SistemaTransporte.getSistemaTransporte().getParadas().get(i);
            if (paradaExistente.getCodigo().equals(parada.getCodigo())) {
                paradaExistente.setNombreParada(parada.getNombreParada());
                paradaExistente.setTipo(parada.getTipo());
                paradaExistente.setUbicacion(parada.getUbicacion());
                break;
            }
        }

        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getOrigen().getCodigo().equals(parada.getCodigo())) {
                ruta.setOrigen(parada);
            }
            if (ruta.getDestino().getCodigo().equals(parada.getCodigo())) {
                ruta.setDestino(parada);
            }
        }
    }

    @Override
    public void eliminar(Parada parada) throws NotEliminableException {
        boolean tieneRutasAsociadas = false;

        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getOrigen().equals(parada) || ruta.getDestino().equals(parada)) {
                tieneRutasAsociadas = true;
                break;
            }
        }

        if (tieneRutasAsociadas) {
            throw new NotEliminableException("No se puede eliminar la parada porque está siendo utilizada en una o más rutas.");
        }

        Iterator<Parada> iterator = SistemaTransporte.getSistemaTransporte().getParadas().iterator();
        while (iterator.hasNext()) {
            Parada p = iterator.next();
            if (p.equals(parada)) {
                iterator.remove();
                break;
            }
        }

        SistemaTransporte.getSistemaTransporte().getGrafo().eliminarParada(parada);
    }

    public ObservableList<Parada> getParadas() {
        ObservableList<Parada> paradas = FXCollections.observableArrayList();
        paradas.addAll(SistemaTransporte.getSistemaTransporte().getParadas());

        return paradas;
    }

}
