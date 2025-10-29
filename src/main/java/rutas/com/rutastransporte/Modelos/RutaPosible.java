package rutas.com.rutastransporte.Modelos;

import java.util.LinkedList;
import java.util.Queue;

public class RutaPosible {
    private LinkedList<Ruta> camino;
    private float costoTotal;
    private float distanciaTotal;
    private float tiempoTotal;
    private int cantTrasbordos;

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

    public void agregarAlCamino(Ruta ruta){
        camino.add(ruta);
        cantTrasbordos++;
    }

    public void agregarAlCaminoFirst(Ruta ruta){
        camino.addFirst(ruta);
        cantTrasbordos++;
    }
    
}
