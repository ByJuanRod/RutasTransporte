package rutas.com.rutastransporte.Modelos;

public class Arista {
    private Parada origen;
    private Ruta ruta;

    public  Arista(Parada origen, Ruta ruta) {
        this.origen = origen;
        this.ruta = ruta;
    }

    public Parada getOrigen() {
        return origen;
    }

    public void setOrigen(Parada origen) {
        this.origen = origen;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }
}
