package rutas.com.rutastransporte.Servicios;

import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Modelos.TipoParada;

import java.util.*;

public class RepositorioDatos {

    private Map<String, Parada> paradas;
    private Map<String, Ruta> rutasByCodigo;
    private List<Ruta> rutas;

    public RepositorioDatos() {
        this.paradas = new HashMap<>();
        this.rutasByCodigo = new HashMap<>();
        this.rutas = new ArrayList<>();
    }

    public void cargarDatos(){

        Parada p1 = new Parada("P001", "Parada Central", TipoParada.BUS, "Calle 1");
        Parada p2 = new Parada("P002", "Estación Norte", TipoParada.TAXI, "Av. 2");
        Parada p3 = new Parada("P003", "Plaza Sur", TipoParada.TAXI, "Calle 3");

        paradas.put(p1.getCodigo(), p1);
        paradas.put(p2.getCodigo(), p2);
        paradas.put(p3.getCodigo(), p3);

        // --- Aquí creas tus rutas ---
        // (Nota: Las rutas ahora conectan los objetos Parada que ya creaste)
        Ruta r1 = new Ruta("R-A", "Ruta A", p1, p2, 10.5f, 50.0f, 15.0f);
        Ruta r2 = new Ruta("R-B", "Ruta B", p1, p3, 8.2f, 40.0f, 12.0f);
        Ruta r3 = new Ruta("R-C", "Ruta C", p2, p3, 5.1f, 25.0f, 8.0f);

        agregarRuta(r1);
        agregarRuta(r2);
        agregarRuta(r3);

    }

    public Parada getParadaPorCodigo(String codigo){
        return paradas.getOrDefault(codigo, null);
    }

    public Ruta getRutaPorCodigo(String codigo){
        return rutasByCodigo.getOrDefault(codigo, null);
    }

    public List<Ruta> getRutas(){
        return rutas;
    }

    public Collection<Parada> getParadas(){
        return paradas.values();
    }

    public void agregarRuta(Ruta ruta){
        this.rutas.add(ruta);
        this.rutasByCodigo.put(ruta.getCodigo(), ruta);
    }

    public void eliminarRuta(Ruta ruta){
        this.rutas.remove(ruta);
        this.rutasByCodigo.remove(ruta.getCodigo());
    }

}
