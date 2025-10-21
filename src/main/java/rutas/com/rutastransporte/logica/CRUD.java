package rutas.com.rutastransporte.logica;

public interface CRUD<T> {
    void insertar(T objeto);
    T actualizar();
    T eliminar();
}
