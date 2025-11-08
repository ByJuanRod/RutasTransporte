package rutas.com.rutastransporte.modelos;

import rutas.com.rutastransporte.utilidades.Modalidad;

/*
    Nombre: Registro
    Tipo: Interface
    Objetivo: Obligar a las pantallas que sirvan como formulario de registro a implementar un metodo para validar,
     un metodo para definir la modalidad y otro para limpiar el contenido.
 */
public interface Registro {

    boolean validar();

    void limpiar();

    void setModalidad(Modalidad modalidad);

    void cargarDatos();
}
