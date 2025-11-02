package rutas.com.rutastransporte.Modelos;

public class ParadaBuilder implements Builder<Parada> {
    private final Parada parada = new Parada();

    public void setNombreParada(String nombreParada) {
        parada.setUbicacion(nombreParada);
    }

    public void setTipo(TipoParada tipo) {
        parada.setTipo(tipo);
    }

    public void setUbicacion(String ubicacion) {
        parada.setUbicacion(ubicacion);
    }

    @Override
    public Parada construir() {
        return parada;
    }
}
