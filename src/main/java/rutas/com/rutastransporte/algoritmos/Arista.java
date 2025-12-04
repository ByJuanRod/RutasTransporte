package rutas.com.rutastransporte.algoritmos;

/*
    Nombre: Arista
    Tipo: Clase
    Objetivo: Representar una arista de un grafo.
 */
public class Arista {
    public int origen;         // Punto de origen
    public int destino;        // Punto de destino
    public int peso;           // Peso que tiene la arista

    public Arista(int origen, int destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }
}
