package rutas.com.rutastransporte.Excepciones;

/*
    Nombre: NotEliminableException
    Tipo: Clase -> Extiende de RunTimeException
    Objetivo: Representar un error cuando un registro no puede ser eliminado.
 */
public class NotEliminableException extends RuntimeException {
    public NotEliminableException(String message) {
        super(message);
    }
}
