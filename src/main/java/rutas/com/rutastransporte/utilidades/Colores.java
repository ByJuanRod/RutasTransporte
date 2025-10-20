package rutas.com.rutastransporte.utilidades;

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
