package rutas.com.rutastransporte.Modelos;

public class RutaBuilder implements Builder<Ruta> {
    private final Ruta ruta;

    public RutaBuilder() {
        this.ruta = new Ruta();
    }

    public void setCodigo(String codigo) {
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

    public void setDistancia(float distancia) {
        ruta.setDistancia(distancia);
    }

    public void setCosto(float costo) {
        ruta.setCosto(costo);
    }

    public void setTiempo(float tiempo) {
        ruta.setTiempo(tiempo);
    }

    @Override
    public Ruta construir() {
        return ruta;
    }
}
