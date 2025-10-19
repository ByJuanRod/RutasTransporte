package rutas.com.rutastransporte.logica;

public enum TipoParada {
    BUS("Parada de Bus","recursos/bus.png"),
    TAXI("Parada de Taxi","recursos/taxi.png"),
    TREN("Estación de Tren ","recursos/tren.png");

    private final String nombreTipo;
    private final String imagenTipo;

    TipoParada(String nombreTipo, String imagenTipo) {
        this.nombreTipo = nombreTipo;
        this.imagenTipo = imagenTipo;
    }

    public String getTipo() {
        return nombreTipo;
    }

    public String getImagen() {
        return imagenTipo;
    }
}
