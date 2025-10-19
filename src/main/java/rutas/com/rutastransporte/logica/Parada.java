package rutas.com.rutastransporte.logica;

public class Parada {
    private String codigo;
    private String nombreParada;
    private TipoParada tipo;
    private String ubicacion;

    public Parada(String codigo, String nombreParada, TipoParada tipo, String ubicacion) {
        this.codigo = codigo;
        this.nombreParada = nombreParada;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreParada() {
        return nombreParada;
    }

    public void setNombreParada(String nombreParada) {
        this.nombreParada = nombreParada;
    }

    public TipoParada getTipo() {
        return tipo;
    }

    public void setTipo(TipoParada tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
