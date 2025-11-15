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

    public void setCodigo(int codigo) {
        ruta.setCodigo(codigo);
    }

    public void setNombre(String nombre) {
        ruta.setNombre(nombre);
    }

    public void setOrigen(Parada origen) {
        ruta.setOrigen(origen);
    }

    public void setDestino(Parada destino) {
        ruta.setDestino(destino);
    }

    public void setDistancia(int distancia) {
        ruta.setDistancia(distancia);
    }

    public void setCosto(float costo) {
        ruta.setCosto(costo);
    }

    public void setTiempo(int tiempo) {
        ruta.setTiempo(tiempo);
    }

    @Override
    public Ruta construir() {
        return ruta;
    }

    public void setTrasbordos(int trasbordos) {
        ruta.setTrasbordos(trasbordos);
    }
}
