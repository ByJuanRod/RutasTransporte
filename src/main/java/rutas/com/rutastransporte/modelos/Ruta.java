package rutas.com.rutastransporte.modelos;

/*
    Nombre: Ruta
    Tipo: Clase
    Objetivo: Representar el modelo de las rutas.
 */
public class Ruta {
    private String codigo;
    private String nombre;
    private Parada origen;
    private Parada destino;
    private int distancia;
    private float costo;
    private int tiempo;
    private int trasbordos;
    private TipoEvento eventoActual;
    private boolean tieneEvento;

    public Ruta(String codigo, String nombre, Parada origen, Parada destino, int distancia, float costo, int tiempo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.costo = costo;
        this.tiempo = tiempo;
        this.trasbordos = 1;
        this.eventoActual = TipoEvento.NORMAL;
        this.tieneEvento = false;
    }

    public Ruta(){

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Parada getOrigen() {
        return origen;
    }

    public void setOrigen(Parada origen) {
        this.origen = origen;
    }

    public Parada getDestino() {
        return destino;
    }

    public void setDestino(Parada destino) {
        this.destino = destino;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getMinutos(){
        return (tiempo % 60);
    }

    public int getHoras(){
        return (tiempo / 60);
    }

    public int getKilometros(){
        return (distancia / 1000);
    }

    public int getMetros(){
        return (distancia % 1000);
    }


    @Override
    public String toString() {
        return nombre;
    }

    public static int calcularTiempo(int horas, int minutos){
        return horas * 60 + minutos;
    }

    public static int calcularDistancia(int kilometros, int metros){
        return kilometros * 1000 + metros;
    }

    public void setTrasbordos(int trasbordos) {
        this.trasbordos = trasbordos;
    }

    public int getTrasbordos(){
        return trasbordos;
    }

    public void aplicarEvento(TipoEvento evento) {
        this.eventoActual = evento;
        this.tieneEvento = true;
    }

    public void removerEvento() {
        this.eventoActual = TipoEvento.NORMAL;
        this.tieneEvento = false;
    }

    public boolean getTieneEvento(){
        return tieneEvento;
    }

    public TipoEvento getTipoEvento(){
        return eventoActual;
    }

    public float getCostoConEvento() {
        float costoBase = getCosto();
        return tieneEvento ? costoBase * eventoActual.getFactorCosto() : costoBase;
    }

    public int getTiempoConEvento() {
        int tiempoBase = getTiempo();
        return tieneEvento ? (int)(tiempoBase * eventoActual.getFactorTiempo()) : tiempoBase;
    }

    public int getDistanciaConEvento() {
        int distanciaBase = getDistancia();
        return tieneEvento ? (int)(distanciaBase * eventoActual.getFactorDistancia()) : distanciaBase;
    }
}

