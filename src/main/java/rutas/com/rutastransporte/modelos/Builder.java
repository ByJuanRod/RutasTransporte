package rutas.com.rutastransporte.modelos;

/*
    Nombre: Builder
    Tipo: Interface<T>
    Objetivo: Obligar a los modelos que extienden del patron Builder a implementar un metodo de construccion.
 */
public interface Builder<T> {
    T construir();
}
