package rutas.com.rutastransporte.servicios;

import javafx.scene.control.Alert;
import rutas.com.rutastransporte.modelos.EventoRuta;
import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.TipoEvento;
import rutas.com.rutastransporte.repositorio.ConexionDB;
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

    /*
        Nombre: getInstancia
        Argumentos: -
        Objetivo: Obtener la instancia de ServicioEventos, en caso de no existir una instancia creará una.
        Retorno: (ServicioEventos) Retorna la instancia de servicio eventos existente.
     */
    public static ServicioEventos getInstancia(){
        if(instancia == null){
            instancia = new ServicioEventos();
        }
        return instancia;
    }

    /*
        Nombre: insertar
        Argumentos:
            (EventoRuta) nuevoEvento: Representa el evento que se va a agregar.
        Objetivo: Insertar un nuevo evento a una ruta.
        Retorno: -
     */
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

    /*
        Nombre: eliminar
        Argumentos:
            (EventoRuta) evento: Representa el evento que se va a eliminar
        Objetivo: Eliminar un evento de una ruta y de la base de datos.
        Retorno: -
     */
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

    /*
        Nombre: eliminarEventoDeDB
        Argumentos:
            (int) codigoRuta: Representa el codigo de la ruta.
         Objetivo: Eliminar un evento de la base de datos.
         Retorno: -
     */
    private void eliminarEventoDeBD(int codigoRuta) {
        String sql = "DELETE FROM Eventos WHERE ruta = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, codigoRuta);
            pst.execute();

        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR).crearAlerta("Error al eliminar el evento.","Error.").show();
        }
    }

    /*
        Nombre: eliminar
        Argumentos:
            (Ruta) ruta: Representa la ruta de la que se eliminara el evento
         Objetivo: Eliminar el evento de una ruta.
         Retorno: -
     */
    public void eliminar(Ruta ruta) {
        if (ruta == null) {
            return;
        }

        EventoRuta evento = eventosActivos.get(ruta.getCodigo());
        if (evento != null) {
            eliminar(evento);
        }
    }

    /*
        Nombre: crearSimulacionEventos
        Argumentos: -
        Objetivo: Crear la simulacion de los eventos en la aplicación.
        Retorno: -
     */
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

    /*
        Nombre: obtenerTodasLasRutas
        Argumentos: -
        Objetivo: Obtener todas las rutas existentes.
        Retorno: (List<Ruta>) Retorna la lista de todas las rutas existentes.
     */
    private List<Ruta> obtenerTodasLasRutas() {
        return new ArrayList<>(rutas.com.rutastransporte.repositorio.SistemaTransporte.getSistemaTransporte().getRutas());
    }

    /*
        Nombre: generarYGuardarEvento
        Argumento:
            (Ruta) ruta: Representa la ruta a la que se le aplicará un evento aleatorio.
         Objetivo: Generar un evento aleatorio en una ruta y guardarlo en la base de datos.
         Retorno: -
     */
    private void generarYGuardarEvento(Ruta ruta) {
        try {
            generarEventoAleatorio(ruta);

            if (tieneEventoActivo(ruta)) {
                EventoRuta evento = getEvento(ruta);
                guardarEventoEnBD(evento);
            }
        } catch (Exception ignored) {}
    }

    /*
        Nombre: generarEventoAleatorio
        Argumentos:
            (Ruta) ruta: Representa la ruta a la que se le generará el evento aleatorio.
         Objetivo: Generar un evento aleatorio en una ruta.
         Retorno: -
     */
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

    /*
        Nombre: limpiarEventosExpirados
        Argumentos: -
        Objetivo: Eliminar los eventos que ya estan expirados de la aplicación
        Retorno: -
     */
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

    /*
        Nombre: tieneEventoActivo
        Argumento:
            (Ruta) ruta: Representa la ruta que se verificará.
        Objetivo: Verificar si una ruta tiene un evento activo.
        Retorno: (boolean) Retorna true la ruta ya tiene un evento asociado.
                           Retorna false si la ruta no tiene eventos asociados.
     */
    public boolean tieneEventoActivo(Ruta ruta) {
        return eventosActivos.containsKey(ruta.getCodigo()) &&
                eventosActivos.get(ruta.getCodigo()).estaActivo();
    }

    /*
        Nombre: getEvento
        Argumentos:
            (Ruta) ruta: Representa la ruta que se va a obtener su evento.
        Objetivo: Obtener el evento activo asociado a una ruta.
        Retorno: (EventoRuta) Retorna el evento que una ruta tiene activo.
     */
    public EventoRuta getEvento(Ruta ruta) {
        return eventosActivos.get(ruta.getCodigo());
    }

    /*
        Nombre: aplicarEventoARuta
        Argumentos:
            (Ruta) ruta: Representa la ruta a la que se le va a aplicar el evento.
            (TipoEvento) evento: Representa el evento que se va a aplicar.
            (int) duracionMinutos: Representa la duración que tendra el evento.
        Objetivo: Aplicarle un evento a una ruta.
        Retorno: -
     */
    public void aplicarEventoARuta(Ruta ruta, TipoEvento evento, int duracionMinutos) {
        if (eventosActivos.containsKey(ruta.getCodigo())) {
            eventosActivos.get(ruta.getCodigo()).setActivo(false);
        }

        EventoRuta eventoRuta = new EventoRuta(ruta, evento, duracionMinutos);
        eventosActivos.put(ruta.getCodigo(), eventoRuta);
        ruta.aplicarEvento(evento);
    }

    /*
        Nombre: limpiarEventosExpiradosDB
        Argumentos: -
        Objetivo: Eliminar los eventos expirados de la base de datos.
        Retorno: -
     */
    public void limpiarEventosExpiradosBD() {
        String sql = "DELETE FROM Eventos WHERE fecha_fin < ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setTimestamp(1, new Timestamp(new Date().getTime()));
            pst.executeUpdate();

        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR)
                    .crearAlerta("Error al limpiar los eventos.", "Error.")
                    .show();
        }
    }

    /*
        Nombre: guardarEventoEnDB
        Argumentos:
            (EventoRuta) evento: Representa el evento que se va a almacenar.
         Objetivo: Guardar un evento en la base de datos.
         Retorno: -
     */
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

    /*
        Nombre: cargarEventosActivosDesdeDB
        Argumentos: -
        Objetivo: Cargar los eventos activos de la base de datos.
        Retorno: -
     */
    public void cargarEventosActivosDesdeBD() {
        String sql = "SELECT e.ruta, e.tipo_evento, e.fecha_inicio, e.fecha_fin FROM Eventos e WHERE e.fecha_fin > ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

            try (ResultSet rs = pst.executeQuery()) {
                procesarDatos(rs);
            }
        } catch (SQLException e) {
            alt.obtenerAlerta(Alert.AlertType.ERROR)
                    .crearAlerta("Error al cargar eventos.", "Error.")
                    .show();
        }
    }

    /*
        procesarDatos
        Argumentos:
            (ResultSet) rs: Representa los datos que se van a procesar de la consulta.
        Objetivo: Cargar los eventos de la base de datos y aplicarlos a la ruta asociada.
        Retorno: -
     */
    private void procesarDatos(ResultSet rs) throws SQLException {
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
    }

    /*
    Nombre: crearEventoDesdeBD
    Argumentos:
        (Ruta) ruta: Reprsenta la ruta a la quee se le asociará el evento.
        (Timestamp) fechaInicio: Representa la fecha de inicio del evento.
        (Timestamp) fechaFin: Representa la fecha fin del evento.
     Objetivo: Crear un nuevo evento desde los datos de la base de datos.
     Retorno: (EventoRuta) Representa el evento que se creo de la base de datos.
     */
    private EventoRuta crearEventoDesdeBD(Ruta ruta, TipoEvento tipoEvento, Timestamp fechaInicio, Timestamp fechaFin) {
        Date fechaInicioDate = new Date(fechaInicio.getTime());
        Date fechaFinDate = new Date(fechaFin.getTime());

        return new EventoRuta(ruta, tipoEvento, fechaInicioDate, fechaFinDate);
    }

    /*
    Nombre: getRutasConEventosActivos
    Argumentos: -
    Objetivo: Obtener todas las rutas con eventos activos.
    Retorno: (List<Ruta>) Retorna una lista de todas las rutas que tienen eventos activos.
     */
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

    /*
        Nombre: getRutasSinEventosActivos
        Argumentos:
        Objetivo: Obtener las rutas que no tienen eventos activos.
        Retorno: (List<Ruta>) Retorna una lista con todas las rutas sin eventos activos.
     */
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