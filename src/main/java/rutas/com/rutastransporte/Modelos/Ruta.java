package rutas.com.rutastransporte.Modelos;

public class Ruta {
    private String codigo;
    private String nombre;
    private Parada origen;
    private Parada destino;
    private float distancia;
    private float costo;
    private float tiempo;

    public Ruta(String codigo, String nombre, Parada origen, Parada destino, float distancia, float costo, float tiempo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.costo = costo;
        this.tiempo = tiempo;
    }

    public Ruta(){

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Parada getOrigen() {
        return origen;
    }

    public void setOrigen(Parada origen) {
        this.origen = origen;
    }

    public Parada getDestino() {
        return destino;
    }

    public void setDestino(Parada destino) {
        this.destino = destino;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getTiempo() {
        return tiempo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        return "Ruta [" + nombre + "]: " + origen.getNombreParada() + " -> " + destino.getNombreParada();
    }
}
