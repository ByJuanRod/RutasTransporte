package rutas.com.rutastransporte.modelos;

/*
    Nombre: RutaBuilder
    Tipo: Clase -> Implementar a Builder<Ruta>
    Objetivo: Facilitar la construcción de objetos de tipo ruta.
 */
public class RutaBuilder implements Builder<Ruta> {
    private final Ruta ruta;

    public RutaBuilder() {
        this.ruta = new Ruta();
    }

    public RutaBuilder setNombre(String nombre) {
        ruta.setNombre(nombre);
        return this;
    }

    public RutaBuilder setOrigen(Parada origen) {
        ruta.setOrigen(origen);
        return this;
    }

    public RutaBuilder setDestino(Parada destino) {
        ruta.setDestino(destino);
        return this;
    }

    public RutaBuilder setDistancia(int distancia) {
        ruta.setDistancia(distancia);
        return this;
    }

    public RutaBuilder setCosto(float costo) {
        ruta.setCosto(costo);
        return this;
    }

    public RutaBuilder setTiempo(int tiempo) {
        ruta.setTiempo(tiempo);
        return this;
    }

    public RutaBuilder setTrasbordos(int trasbordos) {
        ruta.setTrasbordos(trasbordos);
        return this;
    }

    public RutaBuilder setCodigo(int codigo) {
        ruta.setCodigo(codigo);
        return this;
    }

    @Override
    public Ruta construir() {
        return ruta;
    }
}
