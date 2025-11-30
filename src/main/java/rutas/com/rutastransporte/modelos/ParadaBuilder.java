package rutas.com.rutastransporte.modelos;

/*
    Nombre: ParadaBuilder
    Tipo: Clase -> Implementa Builder<Parada>
    Objetivo: Facilitar la creación de objetos de paradas.
 */
public class ParadaBuilder implements Builder<Parada> {
    private final Parada parada = new Parada();

    public ParadaBuilder setNombreParada(String nombreParada) {
        parada.setNombreParada(nombreParada);
        return this;
    }

    public ParadaBuilder setTipo(TipoParada tipo) {
        parada.setTipo(tipo);
        return this;
    }

    public ParadaBuilder setUbicacion(String ubicacion) {
        parada.setUbicacion(ubicacion);
        return this;
    }

    public ParadaBuilder setCodigo(int codigo){
        parada.setCodigo(codigo);
        return this;
    }

    @Override
    public Parada construir() {
        return parada;
    }
}
