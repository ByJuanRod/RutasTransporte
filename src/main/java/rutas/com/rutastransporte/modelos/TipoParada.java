package rutas.com.rutastransporte.modelos;

/*
    Nombre: TipoParada
    Tipo: Enum
    Valores:
        BUS,TAXI,TREN
    Estructura:
        (String) nombreTipo: Representa el nombre del tipo de parada.
        (String) imagenTipo: Representa la imagen del tipo de parada.
    Objetivo: Representar los tipos de parada que existen.
 */
public enum TipoParada {
    BUS("Parada de Bus","bus.png","bus"),
    TAXI("Parada de Taxi","taxi.png","taxi"),
    TREN("Estación de Tren ","tren.png","tren");

    private final String nombreTipo;
    private final String imagenTipo;
    private final String claseCSS;

    TipoParada(String nombreTipo, String imagenTipo, String claseCSS) {
        this.nombreTipo = nombreTipo;
        this.imagenTipo = imagenTipo;
        this.claseCSS = claseCSS;
    }

    public String getTipo() {
        return nombreTipo;
    }

    public String getImagen() {
        return imagenTipo;
    }

    public String getClase() {
        return claseCSS;
    }
}
