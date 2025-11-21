package rutas.com.rutastransporte.modelos;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/*
    Nombre: RutaPosible
    Tipo: Clase
    Objetivo: Representar una de las rutas posibles que el usuario puede utilizar para llegar de un punto A a un punto B en el grafo.
 */
public class RutaPosible implements Clonable<RutaPosible> {
    private LinkedList<Ruta> camino;
    private float costoTotal;
    private float distanciaTotal;
    private float tiempoTotal;
    private int cantTrasbordos;
    private LinkedList<Criterio> criteriosDestacados;
    private boolean esMejorRuta;
    private Set<TipoEvento> registroEventos;
    private float costoBase;
    private float distanciaBase;
    private float tiempoBase;

    public RutaPosible() {
        camino = new LinkedList<>();
        criteriosDestacados = new LinkedList<>();
        costoTotal = 0;
        distanciaTotal = 0;
        tiempoTotal = 0;
        cantTrasbordos = 0;
        esMejorRuta = false;
        costoBase = 0;
        distanciaBase = 0;
        tiempoBase = 0;
        registroEventos = new HashSet<>();
    }

    public LinkedList<Ruta> getCamino() {
        return camino;
    }

    public  float getCostoTotal() {
        return costoTotal;
    }

    public int getCantTrasbordos() {
        return cantTrasbordos;
    }

    public void agregarTiempo(float tiempoEvento, float tiempoBase){
        tiempoTotal+=tiempoEvento;
        this.tiempoBase += tiempoBase;
    }

    public void agregarCosto(float costoEvento, float costoBase){
        costoTotal += costoEvento;
        this.costoBase += costoBase;
    }

    public void agregarDistancia(float distanciaEvento, float distanciaBase){
        distanciaTotal += distanciaEvento;
        this.distanciaBase += distanciaBase;
    }

    public void agregarTrasbordos(int trasbordos){
        cantTrasbordos+=trasbordos;
    }

    public boolean isEsMejorRuta() {
        return esMejorRuta;
    }

    public void agregarCriterioDestacado(Criterio criterio) {
        criteriosDestacados.add(criterio);
    }

    public LinkedList<Criterio> getCriteriosDestacados() {
        return criteriosDestacados;
    }

    public void setEsMejorRuta(boolean esMejorRuta) {
        this.esMejorRuta = esMejorRuta;
    }

    public void agregarFirst(Criterio criterio){
        criteriosDestacados.addFirst(criterio);
    }

    public Set<TipoEvento> getRegistroEventos(){
        return registroEventos;
    }

    /*
        Nombre: getTiempoFormatado
        Argumentos: -
        Objetivo: Retornar la cantidad de tiempo que tomara el trayecto en el formato de tiempo correcto.
        Retorno: (String) Retorna la cadena que contiene los datos con el formato correcto.
     */
    public String getTiempoFormatado(){
        return Ruta.getTiempoFormatado(tiempoTotal);
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
        tiempoTotal += ruta.getTiempoConEvento();
        costoTotal += ruta.getCostoConEvento();
        distanciaTotal += ruta.getDistanciaConEvento();
        tiempoBase += ruta.getTiempo();
        distanciaBase += ruta.getDistancia();
        cantTrasbordos += ruta.getTrasbordos();
        costoBase += ruta.getCosto();
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

        tiempoTotal += ruta.getTiempoConEvento();
        costoTotal += ruta.getCostoConEvento();
        distanciaTotal += ruta.getDistanciaConEvento();
        tiempoBase += ruta.getTiempo();
        costoBase += ruta.getCosto();
        distanciaBase += ruta.getDistancia();

        if(!ruta.getTipoEvento().equals(TipoEvento.NORMAL)){
            registroEventos.add(ruta.getTipoEvento());
        }
    }

    /*
        Nombre: getDistanciaFormatado
        Argumentos: -
        Objetivo: Retornar la distancia aproximada que toma el camino.
        Retorno: (String) Retorna una cadena con la cantidad metros y kilometros que toma el camino.
     */
    public String getDistanciaFormatado(){
        return Ruta.getDistanciaFormatado(distanciaTotal);
    }

    public boolean sonIguales(LinkedList<Ruta> camino){
        if(camino.size() == this.camino.size()){
            for(int i = 0; i < camino.size(); i++){
                if(!this.camino.get(i).getNombre().equals(camino.get(i).getNombre())){
                    return false;
                }
            }
        }
        else{
            return false;
        }

        return true;
    }

    public String getCaminoTexto(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < camino.size() - 1; i++){
            sb.append(camino.get(i).getNombre()).append(" → ");
        }
        sb.append(camino.getLast().getNombre());

        return sb.toString();
    }

    @Override
    public void clonar(RutaPosible rutaPosible){
        this.camino = rutaPosible.camino;
        this.costoTotal = rutaPosible.costoTotal;
        this.distanciaTotal = rutaPosible.distanciaTotal;
        this.tiempoTotal = rutaPosible.tiempoTotal;
        this.cantTrasbordos = rutaPosible.cantTrasbordos;
        this.esMejorRuta = rutaPosible.esMejorRuta;
        this.criteriosDestacados = new LinkedList<>(rutaPosible.criteriosDestacados);
        this.registroEventos = new HashSet<>(rutaPosible.registroEventos);
    }

    public float getDiffCosto(){
        return costoTotal - costoBase;
    }

    public float getDiffTiempo(){
        return tiempoTotal - tiempoBase;
    }

    public float getDiffDistancia(){
        return distanciaTotal - distanciaBase;
    }
}
