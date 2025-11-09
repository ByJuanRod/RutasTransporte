package rutas.com.rutastransporte.modelos;

public interface CRUD<T> {
    void insertar(T objeto);

    T buscarByCodigo(String codigo);

    void actualizar(T objeto);

    void eliminar(T objeto);

}
