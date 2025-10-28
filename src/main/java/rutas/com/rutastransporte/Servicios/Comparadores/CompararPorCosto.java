package rutas.com.rutastransporte.Servicios.Comparadores;

import rutas.com.rutastransporte.Modelos.Arista;

import java.util.Comparator;

public class CompararPorCosto implements Comparator<Arista> {
    @Override
    public int compare(Arista o1, Arista o2) {
        return Float.compare(o1.getRuta().getCosto(), o2.getRuta().getCosto());
    }
}
