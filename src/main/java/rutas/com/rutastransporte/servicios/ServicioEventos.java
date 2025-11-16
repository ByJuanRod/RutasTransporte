package rutas.com.rutastransporte.servicios;

import javafx.scene.control.Alert;
import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.utilidades.ConexionDB;
import rutas.com.rutastransporte.utilidades.alertas.AlertFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ServicioEventos {
    private static ServicioEventos instancia;
    private final Map<Integer, EventoRuta> eventosActivos;
    private final Random random;
    private final AlertFactory alt = new AlertFactory();

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

    public void insertar(EventoRuta nuevoEvento) {
        if (nuevoEvento == null || nuevoEvento.getRuta() == null) {
            return;
        }

        Ruta ruta = nuevoEvento.getRuta();
        int codigoRuta = ruta.getCodigo();

        if(tieneEventoActivo(ruta)) {
            return;
        }

        eventosActivos.put(codigoRuta, nuevoEvento);
        ruta.aplicarEvento(nuevoEvento.getTipoEvento());

        guardarEventoEnBD(nuevoEvento);
    }

    public void eliminar(EventoRuta evento) {
        if (evento == null || evento.getRuta() == null) {
            return;
        }

        Ruta ruta = evento.getRuta();
        int codigoRuta = ruta.getCodigo();

        if (eventosActivos.containsKey(codigoRuta)) {
            eventosActivos.remove(codigoRuta);
            ruta.removerEvento();
        }

        eliminarEventoDeBD(codigoRuta);
    }

    private void eliminarEventoDeBD(int codigoRuta) {
        String sql = "DELETE FROM Eventos WHERE ruta = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, codigoRuta);
            pst.execute();

        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("Error al eliminar el evento.","Error.");
        }
    }

    public void eliminar(Ruta ruta) {
        if (ruta == null) {
            return;
        }

        EventoRuta evento = eventosActivos.get(ruta.getCodigo());
        if (evento != null) {
            eliminar(evento);
        }
    }

    public void crearSimulacionEventos() {
        limpiarEventosExpirados();

        List<Ruta> todasLasRutas = obtenerTodasLasRutas();

        if (todasLasRutas.isEmpty()) {
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
            }
        } catch (Exception e) {
            System.out.println("Error al generar evento para ruta " + ruta.getNombre() + ": " + e.getMessage());
        }
    }

    public void generarEventoAleatorio(Ruta ruta) {
        if (tieneEventoActivo(ruta)) {
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
            if (entry.getValue().estaExpirado()) {
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
        }

        EventoRuta eventoRuta = new EventoRuta(ruta, evento, duracionMinutos);
        eventosActivos.put(ruta.getCodigo(), eventoRuta);
        ruta.aplicarEvento(evento);
    }

    public void limpiarEventosExpiradosBD() {
        String sql = "DELETE FROM Eventos WHERE fecha_fin < NOW()";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement()) {

            st.execute(sql);

        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("Error al limpiar los eventos.","Error.").show();
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

            pst.execute();

        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("Error al guardar el evento.","Error.").show();
        }
    }

    public void cargarEventosActivosDesdeBD() {
        String sql = "SELECT e.ruta, e.tipo_evento, e.fecha_inicio, e.fecha_fin FROM Eventos e WHERE e.fecha_fin > NOW()";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int codigoRuta = rs.getInt("ruta");
                TipoEvento tipoEvento = TipoEvento.valueOf(rs.getString("tipo_evento"));
                Timestamp fechaInicio = rs.getTimestamp("fecha_inicio");
                Timestamp fechaFin = rs.getTimestamp("fecha_fin");

                Ruta rutaEncontrada = null;
                for (Ruta ruta : obtenerTodasLasRutas()) {
                    if (ruta.getCodigo() == codigoRuta) {
                        rutaEncontrada = ruta;
                        break;
                    }
                }

                if (rutaEncontrada != null) {
                    long duracionRestante = (fechaFin.getTime() - System.currentTimeMillis()) / (60 * 1000);

                    if (duracionRestante > 0) {
                        EventoRuta eventoRuta = crearEventoDesdeBD(rutaEncontrada, tipoEvento, fechaInicio, fechaFin);
                        eventosActivos.put(codigoRuta, eventoRuta);
                        rutaEncontrada.aplicarEvento(tipoEvento);
                    }
                }
            }
        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("Error al cargar eventos.","Error.").show();
        }
    }

    private EventoRuta crearEventoDesdeBD(Ruta ruta, TipoEvento tipoEvento, Timestamp fechaInicio, Timestamp fechaFin) {
        Date fechaInicioDate = new Date(fechaInicio.getTime());
        Date fechaFinDate = new Date(fechaFin.getTime());

        return new EventoRuta(ruta, tipoEvento, fechaInicioDate, fechaFinDate);
    }

    public List<Ruta> getRutasConEventosActivos() {
        limpiarEventosExpirados();

        List<Ruta> rutasConEventos = new ArrayList<>();
        for (EventoRuta evento : eventosActivos.values()) {
            if (evento.estaActivo()) {
                rutasConEventos.add(evento.getRuta());
            }
        }
        return rutasConEventos;
    }

    public List<Ruta> getRutasSinEventosActivos() {
        limpiarEventosExpirados();
        List<Ruta> rutasSinEventos = new ArrayList<>();
        for (Ruta ruta : obtenerTodasLasRutas()) {
            if(!ruta.getTieneEvento()){
                rutasSinEventos.add(ruta);
            }
        }
        return rutasSinEventos;
    }

}