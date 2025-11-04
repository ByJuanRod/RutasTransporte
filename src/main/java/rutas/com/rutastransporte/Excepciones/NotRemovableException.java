package rutas.com.rutastransporte.Excepciones;

/*
    Nombre: NotEliminableException
    Tipo: Clase -> Extiende de RunTimeException
    Objetivo: Representar un error cuando un registro no puede ser eliminado.
 */
public class NotRemovableException extends RuntimeException {
    public NotRemovableException(String message) {
        super(message);
    }
}
