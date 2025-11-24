package rutas.com.rutastransporte.modelos;

/*
    Nombre: Ruta
    Tipo: Clase
    Objetivo: Representar el modelo de las rutas.
 */
public class Ruta {
    private int codigo;
    private String nombre;
    private Parada origen;
    private Parada destino;
    private int distancia;
    private float costo;
    private int tiempo;
    private int trasbordos;
    private TipoEvento eventoActual;
    private boolean tieneEvento;

    public Ruta(int codigo, String nombre, Parada origen, Parada destino, int distancia, float costo, int tiempo, int trasbordos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.costo = costo;
        this.tiempo = tiempo;
        this.trasbordos = trasbordos;
        this.eventoActual = TipoEvento.NORMAL;
        this.tieneEvento = false;
    }

    public Ruta(){
        this.eventoActual = TipoEvento.NORMAL;
        this.tieneEvento = false;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
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

    /*
        Nombre: getMinutos
        Argumentos: -
        Objetivo: Obtener la cantidad de minutos que hay en el registro de tiempo.
        Retorno: (int) Retorna la cantidad de minutos.
     */
    public int getMinutos(){
        return (tiempo % 60);
    }

    /*
        Nombre: getHoras
        Argumentos: -
        Objetivo: Obtener la cantidad de horas que hay en el registro de tiempo
        Retorno: (int) Retorna la cantidad de horas que hay en el registro de tiempo.
     */
    public int getHoras(){
        return (tiempo / 60);
    }

    /*
        Nombre: getKilometros
        Argumentos: -
        Objetivo: Obtener la cantidad de kilometros que hay en el registro de distancia.
        Retorno: (int) Retorna la cantidad de kilometros que hay en el registro de distancia.
     */
    public int getKilometros(){
        return (distancia / 1000);
    }

    /*
        Nombre: getMetros
        Argumentos: -
        Objetivo: Obtener la cantidad de metros que hay en el registro de distancia.
        Retorno: (int) Retorna la cantidad de metros que hay en el registro de distancia.
     */
    public int getMetros(){
        return (distancia % 1000);
    }


    @Override
    public String toString() {
        return nombre;
    }

    /*
        Nombre: calcularTiempo
        Argumentos:
            (int) horas: Representa la cantidad de horas ingresadas.
            (int) minutos: Representa la cantidad de minutos ingresadas.
        Objetivo: Calcular la cantidad total de minutos en el registro de tiempo.
        Retorno: (int) Retorna la cantidad total de minutos del registro de tiempo.
     */
    public static int calcularTiempo(int horas, int minutos){
        return horas * 60 + minutos;
    }

    /*
        Nombre: calcularDistancia
        Argumentos:
           (int) kilometros: Representa la cantidad de kilometros que se ingresaron.
           (int) metros: Representa la cantidad de metros que se ingresaron.
        Objetivo: Calcular la cantidad total de metros que hay en el registro de distancia.
     */
    public static int calcularDistancia(int kilometros, int metros){
        return kilometros * 1000 + metros;
    }

    public void setTrasbordos(int trasbordos) {
        this.trasbordos = trasbordos;
    }

    public int getTrasbordos(){
        return trasbordos;
    }

    /*
        Nombre: aplicarEvento
        Argumentos:
            (TipoEvento) evento: Representa el tipo de evento que se va a agregar.
        Objetivo: Asociar un evento a una ruta.
        Retorno: -
     */
    public void aplicarEvento(TipoEvento evento) {
        this.eventoActual = evento;
        this.tieneEvento = true;
    }

    /*
        Nombre: removerEvento
        Argumentos: -
        Objetivo: Eliminar un evento de una ruta.
        Retorno: -
     */
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

    /*
        Nombre: getCostoConEvento
     */
    public float getCostoConEvento() {
        float costoBase = getCosto();
        return tieneEvento ? Math.round(costoBase * eventoActual.getFactorCosto()) : costoBase;
    }

    public int getTiempoConEvento() {
        int tiempoBase = getTiempo();
        return tieneEvento ? (int)(tiempoBase * eventoActual.getFactorTiempo()) : tiempoBase;
    }

    public int getDistanciaConEvento() {
        int distanciaBase = getDistancia();
        return tieneEvento ? (int)(distanciaBase * eventoActual.getFactorDistancia()) : distanciaBase;
    }

    public int getTiempoDiff(){
        return getTiempoConEvento() - getTiempo();
    }

    public float getCostoDiff(){
        return getCostoConEvento() - getCosto();
    }

    public int getDistanciaDiff(){
        return getDistanciaConEvento() - getDistancia();
    }

    public static String getTiempoFormatado(float tiempoTotal){
        int horas = (int) (tiempoTotal / 60);
        int minutos = (int) (tiempoTotal % 60);

        return horas + "h " + minutos + "min";
    }

    public static String getDistanciaFormatado(float distanciaTotal){
        int kilometros = (int) (distanciaTotal / 1000);
        int metros =  (int) (distanciaTotal % 1000);
        return kilometros + " km " + metros + " m";
    }

}

