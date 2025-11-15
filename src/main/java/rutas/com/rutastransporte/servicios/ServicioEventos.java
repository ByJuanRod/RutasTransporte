package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.utilidades.ConexionDB;

import java.sql.*;
import java.util.*;

public class ServicioEventos {
    private static ServicioEventos instancia;
    private final Map<Integer, EventoRuta> eventosActivos;
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

    public void crearSimulacionEventos() {
        System.out.println("Iniciando simulación de eventos aleatorios...");
        limpiarEventosExpirados();

        List<Ruta> todasLasRutas = obtenerTodasLasRutas();

        if (todasLasRutas.isEmpty()) {
            System.out.println("No hay rutas disponibles para generar eventos.");
            return;
        }

        int maxEventos = Math.min(3, Math.max(1, todasLasRutas.size() / 10));
        int eventosGenerados = 0;

        List<Ruta> rutasMezcladas = new ArrayList<>(todasLasRutas);
        Collections.shuffle(rutasMezcladas);

        for (Ruta ruta : rutasMezcladas) {
            if (!tieneEventoActivo(ruta) && random.nextDouble() < 0.3) {
                generarYGuardarEvento(ruta);
                eventosGenerados++;
            }

            if (eventosGenerados >= maxEventos) {
                break;
            }
        }

        System.out.println("Simulación completada. Eventos generados: " + eventosGenerados);
    }

    private List<Ruta> obtenerTodasLasRutas() {
        return new ArrayList<>(rutas.com.rutastransporte.repositorio.SistemaTransporte.getSistemaTransporte().getRutas());
    }

    private void generarYGuardarEvento(Ruta ruta) {
        try {
            generarEventoAleatorio(ruta);

            if (tieneEventoActivo(ruta)) {
                EventoRuta evento = getEvento(ruta);
                guardarEventoEnBD(evento);
                System.out.println("Evento generado para ruta " + ruta.getNombre() +
                        ": " + evento.getTipoEvento().getNombre());
            }
        } catch (Exception e) {
            System.out.println("Error al generar evento para ruta " + ruta.getNombre() + ": " + e.getMessage());
        }
    }

    public void generarEventoAleatorio(Ruta ruta) {
        if (tieneEventoActivo(ruta)) {
            System.out.println("La ruta " + ruta.getNombre() + " ya tiene un evento activo");
            return;
        }

        TipoEvento[] eventosPosibles = {
                TipoEvento.ACCIDENTE,
                TipoEvento.DESVIO,
                TipoEvento.CAMINO_LIBRE,
                TipoEvento.ZONA_CONCURRIDA
        };

        TipoEvento evento = eventosPosibles[random.nextInt(eventosPosibles.length)];
        int duracion = 5 + random.nextInt(25);

        aplicarEventoARuta(ruta, evento, duracion);
    }

    public void limpiarEventosExpirados() {
        Iterator<Map.Entry<Integer, EventoRuta>> it = eventosActivos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, EventoRuta> entry = it.next();
            if (!entry.getValue().estaActivo()) {
                entry.getValue().getRuta().removerEvento();
                it.remove();
            }
        }

        limpiarEventosExpiradosBD();
    }

    public boolean tieneEventoActivo(Ruta ruta) {
        return eventosActivos.containsKey(ruta.getCodigo()) &&
                eventosActivos.get(ruta.getCodigo()).estaActivo();
    }

    public EventoRuta getEvento(Ruta ruta) {
        return eventosActivos.get(ruta.getCodigo());
    }

    public void aplicarEventoARuta(Ruta ruta, TipoEvento evento, int duracionMinutos) {
        if (eventosActivos.containsKey(ruta.getCodigo())) {
            eventosActivos.get(ruta.getCodigo()).setActivo(false);
            ruta.removerEvento();
        }

        EventoRuta eventoRuta = new EventoRuta(ruta, evento, duracionMinutos);
        eventosActivos.put(ruta.getCodigo(), eventoRuta);
        ruta.aplicarEvento(evento);
    }

    public boolean rutaTieneEvento(Ruta ruta) {
        return tieneEventoActivo(ruta);
    }

    public void limpiarEventosExpiradosBD() {
        String sql = "DELETE FROM Eventos WHERE fecha_fin < NOW()";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement()) {

            int filasEliminadas = st.executeUpdate(sql);
            if (filasEliminadas > 0) {
                System.out.println("Eventos expirados eliminados de BD: " + filasEliminadas);
            }

        } catch (SQLException e) {
            System.out.println("Error al limpiar eventos expirados de BD: " + e.getMessage());
        }
    }

    public void guardarEventoEnBD(EventoRuta evento) {
        String sql = "INSERT INTO Eventos (ruta, tipo_evento, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, evento.getRuta().getCodigo());
            pst.setString(2, evento.getTipoEvento().name());
            pst.setTimestamp(3, new Timestamp(evento.getFechaInicio().getTime()));
            pst.setTimestamp(4, new Timestamp(evento.getFechaFin().getTime()));

            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Evento guardado en BD para ruta: " + evento.getRuta().getNombre());
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar evento en BD: " + e.getMessage());
        }
    }

    public void cargarEventosActivosDesdeBD() {
        String sql = "SELECT e.ruta, e.tipo_evento, e.fecha_fin FROM Eventos e WHERE e.fecha_fin > NOW()";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int eventosCargados = 0;

            while (rs.next()) {
                int codigoRuta = rs.getInt("ruta");
                TipoEvento tipoEvento = TipoEvento.valueOf(rs.getString("tipo_evento"));

                Ruta rutaEncontrada = null;
                for (Ruta ruta : obtenerTodasLasRutas()) {
                    if (ruta.getCodigo() == codigoRuta) {
                        rutaEncontrada = ruta;
                        break;
                    }
                }

                if (rutaEncontrada != null) {
                    Timestamp fechaFin = rs.getTimestamp("fecha_fin");
                    long duracionRestante = (fechaFin.getTime() - System.currentTimeMillis()) / (60 * 1000);

                    if (duracionRestante > 0) {
                        aplicarEventoARuta(rutaEncontrada, tipoEvento, (int) duracionRestante);
                        eventosCargados++;
                        System.out.println("Evento cargado para ruta: " + rutaEncontrada.getNombre() +
                                " - " + tipoEvento.getNombre());
                    }
                }
            }

            System.out.println("Eventos activos cargados desde BD: " + eventosCargados);

        } catch (SQLException e) {
            System.out.println("Error al cargar eventos desde BD: " + e.getMessage());
        }
    }

    public List<Ruta> getRutasConEventosActivos() {
        List<Ruta> rutasConEventos = new ArrayList<>();
        for (EventoRuta evento : eventosActivos.values()) {
            if (evento.estaActivo()) {
                rutasConEventos.add(evento.getRuta());
            }
        }
        return rutasConEventos;
    }

    public void removerEventoDeRuta(Ruta ruta) {
        if (eventosActivos.containsKey(ruta.getCodigo())) {
            eventosActivos.get(ruta.getCodigo()).setActivo(false);
            ruta.removerEvento();
            eventosActivos.remove(ruta.getCodigo());
            System.out.println("Evento removido de la ruta: " + ruta.getNombre());
        }
    }
}