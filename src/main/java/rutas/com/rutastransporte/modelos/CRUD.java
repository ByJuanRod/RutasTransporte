package rutas.com.rutastransporte.modelos;

public interface CRUD<T> {
    void insertar(T objeto);

    void actualizar(T objeto);

    void eliminar(T objeto);

}
