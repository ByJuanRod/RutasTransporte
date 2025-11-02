package rutas.com.rutastransporte.Modelos;

/*
    Nombre: Registro
    Tipo: Interface
    Objetivo: Obligar a las pantallas que sirvan como formulario de registro a implementar un metodo para validar y otro para limpiar el contenido.
 */
public interface Registro {
    boolean validar();

    void limpiar();
}
