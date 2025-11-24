package rutas.com.rutastransporte.modelos;

/*
    Nombre: TipoEvento
    Tipo: Enum
    Valores:
        (ACCIDENTE, DESVIO, CAMINO_LIBRE, ZONA_CONCURRIDA, NORMAL
    Estructura:
        (String) nombre: Representa el nombre del tipo de evento.
        (float) factorTiempo: Representa el costo agregado o disminuido al factor del tiempo.
        (float) factorDistancia: Representa el factor agregado o disminuido a la distancia.
        (float) factorCosto: Representa el factor de costo agregado o disminuido al costo.
        (String) imagen: Representa la imagen asociada al evento.
   Objetivo: Asociar una ruta a un tipo de evento como parte de la simulación.
 */
public enum TipoEvento {
    ACCIDENTE("Accidente","accidente.png", 1.5f, 2.0f, 1.0f),
    DESVIO("Desvío","desvio.png", 1.3f, 1.5f, 1.1f),
    CAMINO_LIBRE("Camino Libre","libre.png", 0.7f, 0.9f, 1.0f),
    ZONA_CONCURRIDA("Zona Concurrida","concurrido.png",1.4f, 1.1f, 1.2f),
    NORMAL("Normal","",1.0f, 1.0f, 1.0f);

    private final String nombre;
    private final float factorTiempo;
    private final float factorDistancia;
    private final float factorCosto;
    private final String imagen;

    TipoEvento(String nombre, String img, float factorTiempo, float factorDistancia, float factorCosto) {
        this.nombre = nombre;
        this.imagen = img;
        this.factorTiempo = factorTiempo;
        this.factorDistancia = factorDistancia;
        this.factorCosto = factorCosto;
    }

    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public float getFactorTiempo() {
        return factorTiempo;
    }

    public float getFactorDistancia() {
        return factorDistancia;
    }

    public float getFactorCosto() {
        return factorCosto;
    }

    @Override
    public String toString() {
        return nombre;
    }
}