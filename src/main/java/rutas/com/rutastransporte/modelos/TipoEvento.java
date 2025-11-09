package rutas.com.rutastransporte.modelos;

public enum TipoEvento {
    ACCIDENTE("Accidente", 1.5f, 2.0f, 1.0f),    // +50% tiempo, +100% distancia
    DESVIO("Desvío", 1.3f, 1.5f, 1.0f),         // +30% tiempo, +50% distancia
    CAMINO_LIBRE("Camino Libre", 0.7f, 0.9f, 1.0f), // -30% tiempo, -10% distancia
    ZONA_CONCURRIDA("Zona Concurrida", 1.4f, 1.1f, 1.2f), // +40% tiempo, +20% costo
    NORMAL("Normal", 1.0f, 1.0f, 1.0f);         // Sin cambios

    private final String nombre;
    private final float factorTiempo;
    private final float factorDistancia;
    private final float factorCosto;

    TipoEvento(String nombre, float factorTiempo, float factorDistancia, float factorCosto) {
        this.nombre = nombre;
        this.factorTiempo = factorTiempo;
        this.factorDistancia = factorDistancia;
        this.factorCosto = factorCosto;
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
}