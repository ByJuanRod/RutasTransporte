package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.Parada;
import rutas.com.rutastransporte.modelos.Ruta;

import java.util.*;

public class GrafoTransporte {
    private final Map<Parada, List<Ruta>> listaAdyacencia;

    public Set<Parada> getParadas() {
        return listaAdyacencia.keySet();
    }

    public GrafoTransporte() {
        listaAdyacencia = new HashMap<>();
    }

    public void agregarParada(Parada parada) {
        listaAdyacencia.putIfAbsent(parada, new ArrayList<>());
    }

    public void agregarRuta(Ruta ruta) {
        Parada origen = ruta.getOrigen();
        listaAdyacencia.putIfAbsent(origen, new ArrayList<>());
        listaAdyacencia.get(origen).add(ruta);
    }

    public List<Ruta> getRutas(Parada parada) {
        return listaAdyacencia.getOrDefault(parada, new ArrayList<>());
    }

    public void eliminarParada(Parada parada) {
        listaAdyacencia.remove(parada);

        for (List<Ruta> rutas : listaAdyacencia.values()) {
            rutas.removeIf(ruta -> ruta.getDestino().equals(parada));
        }

    }

    public void eliminarRuta(Ruta ruta) {
        Parada origen = ruta.getOrigen();
        List<Ruta> rutas = listaAdyacencia.get(origen);

        if (rutas != null) {
            rutas.remove(ruta);
        }

    }

    public void actualizarRuta(Ruta rutaActualizada) {
        if (rutaActualizada == null) {
            throw new IllegalArgumentException("La ruta actualizada no puede ser nula");
        }

        Ruta rutaExistente = null;
        Parada origenExistente = null;

        for (Map.Entry<Parada, List<Ruta>> entry : listaAdyacencia.entrySet()) {
            for (Ruta ruta : entry.getValue()) {
                if (ruta.getCodigo() == rutaActualizada.getCodigo()) {
                    rutaExistente = ruta;
                    origenExistente = entry.getKey();
                    break;
                }
            }
            if (rutaExistente != null) break;
        }

        if (rutaExistente == null) {
            agregarRuta(rutaActualizada);
            return;
        }

        Parada nuevoOrigen = rutaActualizada.getOrigen();

        if (!origenExistente.equals(nuevoOrigen)) {
            eliminarRuta(rutaExistente);
            agregarRuta(rutaActualizada);
        } else {
            List<Ruta> rutasDelOrigen = listaAdyacencia.get(origenExistente);
            int index = rutasDelOrigen.indexOf(rutaExistente);
            if (index != -1) {
                rutasDelOrigen.set(index, rutaActualizada);
            }
        }

        agregarParada(rutaActualizada.getDestino());
    }

    public void actualizarParada(Parada paradaActualizada) {
        if (paradaActualizada == null) {
            throw new IllegalArgumentException("La parada actualizada no puede ser nula");
        }

        Parada paradaExistente = null;
        for (Parada p : listaAdyacencia.keySet()) {
            if (p.getCodigo() == paradaActualizada.getCodigo()) {
                paradaExistente = p;
                break;
            }
        }

        if (paradaExistente == null) {
            agregarParada(paradaActualizada);
            return;
        }

        if (paradaExistente == paradaActualizada) {
            return;
        }

        List<Ruta> rutasComoOrigen = listaAdyacencia.get(paradaExistente);
        listaAdyacencia.remove(paradaExistente);

        listaAdyacencia.put(paradaActualizada, Objects.requireNonNullElseGet(rutasComoOrigen, ArrayList::new));

        for (List<Ruta> rutas : listaAdyacencia.values()) {
            for (Ruta ruta : rutas) {
                if (ruta.getDestino().equals(paradaExistente)) {
                    ruta.setDestino(paradaActualizada);
                }
                if (ruta.getOrigen().equals(paradaExistente)) {
                    ruta.setOrigen(paradaActualizada);
                }
            }
        }
    }
}
