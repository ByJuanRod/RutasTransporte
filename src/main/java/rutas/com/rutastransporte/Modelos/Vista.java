package rutas.com.rutastransporte.Modelos;

import rutas.com.rutastransporte.Utilidades.Modalidad;

public interface Vista<T> {
    void crearPantalla(String titulo, Modalidad modalidad, T objeto);
}
