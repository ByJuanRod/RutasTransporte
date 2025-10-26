package rutas.com.rutastransporte.Servicios;

public interface Servicio<T> {
    void insertar(T objeto);

    T buscarByCodigo(String codigo);

    void actualizar(T objeto);

    void eliminar(T objeto);

    boolean esEliminable(T objeto);
}
