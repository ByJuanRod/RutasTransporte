package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;

public class ServicioParadas implements Servicio<Parada>{

    @Override
    public void insertar(Parada parada) {
        SistemaTransporte.getSistemaTransporte().getParadas().add(parada);
        SistemaTransporte.getSistemaTransporte().getGrafo().agregarParada(parada);
        SistemaTransporte.genCodigoParada++;
    }

    @Override
    public Parada buscarByCodigo(String codigo) {
        for(Parada parada : SistemaTransporte.getSistemaTransporte().getParadas()){
            if(parada.getCodigo().equals(codigo)){
                return parada;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Parada parada) {

    }

    @Override
    public void eliminar(Parada parada) {
        if(esEliminable(parada)){
            SistemaTransporte.getSistemaTransporte().getParadas().remove(parada);
            SistemaTransporte.getSistemaTransporte().getGrafo().eliminarParada(parada);
        }
    }

    @Override
    public boolean esEliminable(Parada parada) {
        for (Ruta ruta : SistemaTransporte.getSistemaTransporte().getRutas()) {
            if (ruta.getOrigen().equals(parada) || ruta.getDestino().equals(parada)) {
                return false;
            }
        }

        return true;
    }

}
