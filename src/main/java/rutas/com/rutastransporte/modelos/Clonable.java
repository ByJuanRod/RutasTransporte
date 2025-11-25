package rutas.com.rutastransporte.modelos;

/*
    Nombre: Clonable
    Tipo: Interface
    Objetivo: Hacer que los objetos que sean duplicables implementen un metodo para clonar.
 */
public interface Clonable<T> {
    void clonar(T objeto);
}
