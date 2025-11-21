package rutas.com.rutastransporte.controladores;

import rutas.com.rutastransporte.utilidades.Modalidad;

/*
    Nombre: Vista<T>
    Tipo: Interface
    Objetivo: Obligar a los apartados que sirven como vistas a implementar un metodo para crear las pantallas de la aplicacion
 */
public interface Vista<T> {
    void crearPantalla(String titulo, Modalidad modalidad, T objeto);

    void cargarDatos();

    void filtrar();

    void configurarColumnas();
}
