package rutas.com.rutastransporte.modelos;

/*
    Nombre: Criterio
    Tipo: Enum
    Valores: MAS_ECONOMICO, MAS_CORTA, MAS_RAPIDA, MENOS_TRASBORDOS
    Estructura:
        (String) nombre: Representa el nombre del criterio indicador.
        (String) imagen: Representa la imagen que representa ese criterio.
    Objetivo: Representa los distintos criterios por los que se puede filtrar una ruta.
 */
public enum Criterio {
    MAS_ECONOMICO("Ruta Más Económica","ahorro.png"),
    MAS_CORTA("Ruta Más Corta","distancia.png"),
    MAS_RAPIDA("Ruta Más Rápida","tiempo.png"),
    MENOS_TRASBORDOS("Ruta con Menos Trasbordos","trasbordos.png"),
    MEJOR_RUTA("Mejor Ruta","mejor.png" );

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
