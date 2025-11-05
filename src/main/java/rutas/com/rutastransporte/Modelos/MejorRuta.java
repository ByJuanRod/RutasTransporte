package rutas.com.rutastransporte.Modelos;

import java.util.LinkedList;
import java.util.Queue;

public class MejorRuta extends RutaPosible{
    Queue<Criterio> criteriosDestacados;

    public MejorRuta(RutaPosible rutaPosible){
        criteriosDestacados = new LinkedList<>();
        clonar(rutaPosible);
    }

    public void agregarCriterio(Criterio criterio){
        criteriosDestacados.add(criterio);
    }
}
