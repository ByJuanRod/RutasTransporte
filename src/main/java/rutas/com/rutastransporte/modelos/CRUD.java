package rutas.com.rutastransporte.modelos;

/*
    Nombre: CRUD
    Tipo: Interface
    Objetivo: Hacer que los servicios que implementan CRUD tengan que tener metodos para insertar, eliminar y actualizar.
 */
public interface CRUD<T> {
    boolean insertar(T objeto);

    boolean actualizar(T objeto);

    void eliminar(T objeto);
}
