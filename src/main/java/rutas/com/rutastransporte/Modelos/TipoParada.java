package rutas.com.rutastransporte.Modelos;

public enum TipoParada {
    BUS("Parada de Bus","bus.png"),
    TAXI("Parada de Taxi","taxi.png"),
    TREN("Estación de Tren ","tren.png");

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
