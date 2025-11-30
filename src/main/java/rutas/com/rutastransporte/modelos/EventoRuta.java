package rutas.com.rutastransporte.modelos;

import java.util.Date;

/*
    Nombre: EventoRuta
 */
public class EventoRuta {
    private final Ruta ruta;
    private final TipoEvento tipoEvento;
    private final Date fechaInicio;
    private final Date fechaFin;
    private boolean activo;

    public EventoRuta(Ruta ruta, TipoEvento tipoEvento, Date fechaInicio, Date fechaFin) {
        this.ruta = ruta;
        this.tipoEvento = tipoEvento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = new Date().before(fechaFin);
    }

    public EventoRuta(Ruta ruta, TipoEvento tipoEvento, int duracionMinutos) {
        this.ruta = ruta;
        this.tipoEvento = tipoEvento;
        this.fechaInicio = new Date();
        this.fechaFin = new Date(fechaInicio.getTime() + (long) duracionMinutos * 60 * 1000);
        this.activo = true;
    }

    public boolean estaActivo() {
        Date ahora = new Date();
        return activo && ahora.before(fechaFin);
    }

    public boolean estaExpirado() {
        return !estaActivo();
    }

    /*
        Nombre: getTiempoRestanteMinutos
        Argumentos: -
        Objetivo: Obtener la duracion restante en minutos de un evento.
        Retorno: (long) Retorna la duracion restante del evento.
     */
    public long getTiempoRestanteMinutos() {
        Date ahora = new Date();
        if (ahora.after(fechaFin)) {
            return 0;
        }
        return (fechaFin.getTime() - ahora.getTime()) / (60 * 1000);
    }

    public Ruta getRuta() {
        return ruta;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}