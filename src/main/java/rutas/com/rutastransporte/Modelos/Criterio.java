package rutas.com.rutastransporte.Modelos;

public enum Criterio {
    MAS_ECONOMICO("Más Económica",""),
    MAS_CORTA("Más Corta",""),
    MAS_RAPIDA("Más Rápida",""),
    MENOS_TRASBORDOS("Menos Trasbordos","");

    private final String nombre;
    private final String imagen;

    Criterio(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
