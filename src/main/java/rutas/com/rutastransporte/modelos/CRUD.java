package rutas.com.rutastransporte.modelos;

public interface CRUD<T> {
    boolean insertar(T objeto);

    boolean actualizar(T objeto);

    void eliminar(T objeto);

}
