package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoEvento;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ServicioEventos {
    private static ServicioEventos instancia;
    private final Map<String, EventoRuta> eventosActivos;
    private final Random random;

    private ServicioEventos(){
        eventosActivos = new HashMap<>();
        random = new Random();
    }

    public static ServicioEventos getInstancia(){
        if(instancia == null){
            instancia = new ServicioEventos();
        }
        return instancia;
    }

    public void generarEventoAleatorio(Ruta ruta) {
        TipoEvento[] eventosPosibles = {
                TipoEvento.ACCIDENTE,
                TipoEvento.DESVIO,
                TipoEvento.CAMINO_LIBRE,
                TipoEvento.ZONA_CONCURRIDA
        };

        TipoEvento evento = eventosPosibles[random.nextInt(eventosPosibles.length)];
        int duracion = 5 + random.nextInt(25);

        EventoRuta eventoRuta = new EventoRuta(ruta, evento, duracion);
        eventosActivos.put(ruta.getCodigo(), eventoRuta);
        ruta.aplicarEvento(evento);
    }

    public void limpiarEventosExpirados() {
        Iterator<Map.Entry<String, EventoRuta>> it = eventosActivos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, EventoRuta> entry = it.next();
            if (!entry.getValue().estaActivo()) {
                entry.getValue().getRuta().removerEvento();
                it.remove();
            }
        }
    }

    public boolean tieneEventoActivo(Ruta ruta) {
        return eventosActivos.containsKey(ruta.getCodigo());
    }

    public EventoRuta getEvento(Ruta ruta) {
        return eventosActivos.get(ruta.getCodigo());
    }

    public void aplicarEventoARuta(Ruta ruta, TipoEvento evento, int duracionMinutos) {
        EventoRuta eventoRuta = new EventoRuta(ruta, evento, duracionMinutos);
        eventosActivos.put(ruta.getCodigo(), eventoRuta);
        ruta.aplicarEvento(evento);
    }

    public boolean rutaTieneEvento(Ruta ruta) {
        return eventosActivos.containsKey(ruta.getCodigo()) &&
                eventosActivos.get(ruta.getCodigo()).estaActivo();
    }
}
