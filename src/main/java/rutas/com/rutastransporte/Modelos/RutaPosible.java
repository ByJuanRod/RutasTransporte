package rutas.com.rutastransporte.Modelos;

import java.util.LinkedList;
import java.util.Queue;

/*
    Nombre: RutaPosible
    Tipo: Clase
    Objetivo: Representar una de las rutas posibles que el usuario puede utilizar para llegar de un punto A a un punto B en el grafo.
 */
public class RutaPosible {
    private LinkedList<Ruta> camino;
    private float costoTotal;
    private float distanciaTotal;
    private float tiempoTotal;
    private int cantTrasbordos;
    private Criterio criterio;

    public RutaPosible() {
        camino = new LinkedList<>();
        costoTotal = 0;
        distanciaTotal = 0;
        tiempoTotal = 0;
        cantTrasbordos = 0;
    }

    public LinkedList<Ruta> getCamino() {
        return camino;
    }

    public  void setCamino(LinkedList<Ruta> camino) {
        this.camino = camino;
    }

    public  float getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public  float getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(float distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }

    public  float getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(float tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public int getCantTrasbordos() {
        return cantTrasbordos;
    }

    public void setCantTrasbordos(int cantTrasbordos) {
        this.cantTrasbordos = cantTrasbordos;
    }

    public void agregarTiempo(float tiempo){
        tiempoTotal+=tiempo;
    }

    public void agregarCosto(float costo){
        costoTotal+=costo;
    }

    public void agregarDistancia(float distancia){
        distanciaTotal += distancia;
    }

    public void setCriterio(Criterio criterio) {
        this.criterio = criterio;
    }

    public Criterio getCriterio() {
        return criterio;
    }

    /*
        Nombre: getTiempoFormatado
        Argumentos: -
        Objetivo: Retornar la cantidad de tiempo que tomara el trayecto en el formato de tiempo correcto.
        Retorno: (String) Retorna la cadena que contiene los datos con el formato correcto.
     */
    public String getTiempoFormatado(){
        StringBuilder sb = new StringBuilder();
        int horas = (int) (tiempoTotal / 60);
        int minutos = (int) (tiempoTotal % 60);

        sb.append(horas).append("h ").append(minutos).append("m");
        return sb.toString();
    }

    /*
        Nombre: agregarAlCamino
        Argumentos:
            (Ruta) ruta: Representa la ruta que se va a agregar a un camino posible.
        Objetivo: Permitir agregar una ruta como parte del camino para llegar al destino
        Retorno: -
     */
    public void agregarAlCamino(Ruta ruta){
        camino.add(ruta);
        cantTrasbordos += ruta.getTrasbordos();
    }

    /*
        Nombre: AgregarAlCaminoFirst
        Argumentos:
            (Ruta) ruta: Representa la ruta que se va a agregar al camino.
        Objetivo: Agregar una ruta como la primera parte de camino.
        Retorno: -
     */
    public void agregarAlCaminoFirst(Ruta ruta){
        camino.addFirst(ruta);
        cantTrasbordos += ruta.getTrasbordos();
    }

    /*
        Nombre: getDistanciaFormatado
        Argumentos: -
        Objetivo: Retornar la distancia aproximada que toma el camino.
        Retorno: (String) Retorna una cadena con la cantidad metros y kilometros que toma el camino.
     */
    public String getDistanciaFormatado(){
        StringBuilder sb = new StringBuilder();
        int kilometros = (int) (distanciaTotal / 100);
        int metros =  (int) (distanciaTotal % 100);
        sb.append(kilometros).append(" km ").append(metros).append(" m");
        return sb.toString();
    }
    
}
