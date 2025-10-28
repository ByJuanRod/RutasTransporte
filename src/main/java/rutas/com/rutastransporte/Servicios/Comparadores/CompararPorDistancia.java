package rutas.com.rutastransporte.Servicios.Comparadores;

import rutas.com.rutastransporte.Modelos.Arista;

import java.util.Comparator;

public class CompararPorDistancia implements Comparator<Arista> {
    @Override
    public int compare(Arista o1, Arista o2) {
        return Float.compare(o1.getRuta().getDistancia(), o2.getRuta().getDistancia());
    }
}
