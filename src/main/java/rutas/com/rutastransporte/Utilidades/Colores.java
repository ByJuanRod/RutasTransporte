package rutas.com.rutastransporte.Utilidades;

/*
    Nombre: Colores
    Tipo: (Enum)
    Objetivo: Obtener facilmente los colores que se utilizan frecuentemente en la aplicación.
 */
public enum Colores {
    FONDO("#FAFAFA"),
    ENFASIS("#19173C"),
    DETALLES("#49808E"),
    DECORATIVOS("#1B4B6B");

    private final String color;

    Colores(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
