package rutas.com.rutastransporte.Modelos;

public interface CRUD<T> {
    void insertar(T objeto);

    T buscarByCodigo(String codigo);

    void actualizar(T objeto);

    void eliminar(T objeto);

}
