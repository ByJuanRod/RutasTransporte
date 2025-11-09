package rutas.com.rutastransporte.modelos;

import java.util.Date;

public class EventoRuta {
    private Ruta ruta;
    private TipoEvento tipoEvento;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;

    public EventoRuta(Ruta ruta, TipoEvento tipoEvento, int duracionMinutos) {
        this.ruta = ruta;
        this.tipoEvento = tipoEvento;
        this.fechaInicio = new Date();
        this.fechaFin = new Date(fechaInicio.getTime() + duracionMinutos * 60 * 1000);
        this.activo = true;
    }

    public boolean estaActivo() {
        Date ahora = new Date();
        return activo && ahora.before(fechaFin);
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}