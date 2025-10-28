package rutas.com.rutastransporte.Servicios.Comparadores;

import rutas.com.rutastransporte.Modelos.Arista;

import java.util.Comparator;

public class CompararPorTiempo implements Comparator<Arista> {
    @Override
    public int compare(Arista o1, Arista o2) {
        return Float.compare(o1.getRuta().getTiempo(), o2.getRuta().getTiempo());
    }
}
