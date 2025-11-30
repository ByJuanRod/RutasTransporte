package rutas.com.rutastransporte.modelos;

import rutas.com.rutastransporte.utilidades.Criterio;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/*
    Nombre: RutaPosible
    Tipo: Clase Implementa -> Clonable<RutaPosible>
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

    /*
        Nombre: agregarTiempo
        Argumentos:
            (float) tiempoEvento: Representa el tiempo que toma con el evento agregado.
            (float) tiempoBase: Representa el tiempo asociado sin evento.
       Objetivo: Agregar al registro de tiempo cuanto toma el tiempo total con evento y sin evento.
       Retorno: -
     */
    public void agregarTiempo(float tiempoEvento, float tiempoBase){
        tiempoTotal+=tiempoEvento;
        this.tiempoBase += tiempoBase;
    }

    /*
    Nombre: agregarCosto
    Argumentos:
        (float) costoEvento: Representa el costo que toma con el evento agregado.
        (float) costoBase: Representa el costo asociado sin evento.
    Objetivo: Agregar al registro de costo cuanto toma el costo total con evento y sin evento.
    Retorno: -
    */
    public void agregarCosto(float costoEvento, float costoBase){
        costoTotal += costoEvento;
        this.costoBase += costoBase;
    }

    /*
        Nombre: agregarDistancia
        Argumentos:
            (float) distanciaEvento: Representa la distancia que toma con el evento agregado.
            (float) distanciaBase: Representa la distancia asociado sin evento.
        Objetivo: Agregar al registro de distancia cuanto toma la distancia total con evento y sin evento.
        Retorno: -
     */
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

    /*
        Nombre: agregarFirst
        Argumentos:
            (Criterio) criterio: Representa el criterio que se va a agregar.
        Objetivo: Agregar a la lista de criterios destacados el criterio seleccionado.
        Retorno: -
     */
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
        agregarCosto(ruta.getCostoConEvento(),ruta.getCosto());
        agregarTrasbordos(ruta.getTrasbordos());
        agregarDistancia(ruta.getDistanciaConEvento(),ruta.getDistancia());
        agregarTiempo(ruta.getTiempoConEvento(),ruta.getTiempo());
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
        agregarCosto(ruta.getCostoConEvento(),ruta.getCosto());
        agregarTrasbordos(ruta.getTrasbordos());
        agregarDistancia(ruta.getDistanciaConEvento(),ruta.getDistancia());
        agregarTiempo(ruta.getTiempoConEvento(),ruta.getTiempo());

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

    /*
        Nombre: getCaminoTexto
        Argumentos: -
        Objetivo: Representar en un texto el camino necesario para recorrer completar la ruta destacada.
        Retorno: (String) Retorna la cadena que representa el camino en formato de texto.
     */
    public String getCaminoTexto(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < camino.size() - 1; i++){
            sb.append(camino.get(i).getNombre()).append(" → ");
        }
        sb.append(camino.getLast().getNombre());

        return sb.toString();
    }

    /*
        Nombre: clonar
        Argumentos:
            (RutaPosible) rutaPosible: Representa la ruta a clonar
        Objetivo: Clonar el contenido de una ruta y aplicarselo a otra ruta.
        Retorno: -
     */
    @Override
    public void clonar(RutaPosible rutaPosible){
        this.camino = rutaPosible.camino;
        this.costoBase = rutaPosible.costoBase;
        this.distanciaBase = rutaPosible.distanciaBase;
        this.tiempoBase = rutaPosible.tiempoBase;
        this.costoTotal = rutaPosible.costoTotal;
        this.distanciaTotal = rutaPosible.distanciaTotal;
        this.tiempoTotal = rutaPosible.tiempoTotal;
        this.cantTrasbordos = rutaPosible.cantTrasbordos;
        this.esMejorRuta = rutaPosible.esMejorRuta;
        this.criteriosDestacados = new LinkedList<>(rutaPosible.criteriosDestacados);
        this.registroEventos = new HashSet<>(rutaPosible.registroEventos);
    }

    /*
        Nombre: getDiffCosto
        Argumentos: -
        Objetivo: Obtener la diferencia que tiene costo total del camino respecto a su costo base.
        Retorno: (float) Retorna la diferencia entre el costo total y el costo base.
     */
    public float getDiffCosto(){
        return costoTotal - costoBase;
    }

    /*
        Nombre: getDiffTiempo
        Argumentos: -
        Objetivo: Obtener la diferencia de tiempo que existe entre el tiempo total y el tiempo base.
        Retorno: (float) Retorna la diferencia entre el tiempo total y el tiempo base.
     */
    public float getDiffTiempo(){
        return tiempoTotal - tiempoBase;
    }

    /*
        Nombre: getDiffDistancia
        Argumentos: -
        Objetivo: Obtener la diferencia de distancia que existe entre la distancia total y la distancia base.
        Retorno: (float) Retorna la diferencia entre la distancia total y la distancia base.
     */
    public float getDiffDistancia(){
        return distanciaTotal - distanciaBase;
    }
}
