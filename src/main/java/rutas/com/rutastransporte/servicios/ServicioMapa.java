package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.RutaPosible;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class ServicioMapa {
    private final ServicioEventos servicioEventos = ServicioEventos.getInstancia();

    public void crearSimulacion(){
        servicioEventos.crearSimulacionEventos();
    }

    public RutaPosible obtenerMejorRuta(Stack<RutaPosible> posiblesRutas) {
        if (posiblesRutas == null || posiblesRutas.isEmpty()) {
            return null;
        }

        ArrayList<RutaPosible> rutasUnicas = new ArrayList<>();

        for (RutaPosible rutaActual : posiblesRutas) {
            if (rutaActual == null) continue;

            boolean esDuplicada = false;
            for (RutaPosible rutaUnica : rutasUnicas) {
                if (rutaActual.sonIguales(rutaUnica.getCamino())) {
                    esDuplicada = true;
                    break;
                }
            }

            if (!esDuplicada) {
                rutasUnicas.add(rutaActual);
            }
        }

        if (rutasUnicas.size() == 1) {
            RutaPosible mejorRuta = new RutaPosible();
            mejorRuta.clonar(rutasUnicas.getFirst());

            for (RutaPosible posible : posiblesRutas) {
                if (posible != null && posible.sonIguales(rutasUnicas.getFirst().getCamino())) {
                    if (!posible.getCriteriosDestacados().isEmpty()) {
                        mejorRuta.agregarCriterioDestacado(posible.getCriteriosDestacados().getFirst());
                    }
                }
            }
            return mejorRuta;
        }

        RutaPosible mejorRutaBase = null;
        int maxCriteriosCoincidentes = 0;

        for (RutaPosible rutaUnica : rutasUnicas) {
            int criteriosCoincidentes = cantCriteriosParaMismoCamino(posiblesRutas, rutaUnica.getCamino());

            if (criteriosCoincidentes > maxCriteriosCoincidentes) {
                maxCriteriosCoincidentes = criteriosCoincidentes;
                mejorRutaBase = rutaUnica;
            }
        }

        if (mejorRutaBase != null) {
            RutaPosible mejorRuta = new RutaPosible();
            mejorRuta.clonar(mejorRutaBase);
            for (RutaPosible posible : posiblesRutas) {
                if (posible != null && posible.sonIguales(mejorRutaBase.getCamino())) {
                    if (!posible.getCriteriosDestacados().isEmpty()) {
                        mejorRuta.agregarCriterioDestacado(posible.getCriteriosDestacados().getFirst());
                    }
                }
            }

            return mejorRuta;
        }

        return null;
    }

    private int cantCriteriosParaMismoCamino(Stack<RutaPosible> posiblesRutas, LinkedList<Ruta> camino) {
        int cantidadCriterios = 0;
        for (RutaPosible posible : posiblesRutas) {
            if (posible != null && posible.sonIguales(camino)) {
                cantidadCriterios++;
            }
        }
        return cantidadCriterios;
    }

    public ArrayList<RutaPosible> obtenerRutasUnicasExcluyendoMejor(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta) {
        ArrayList<RutaPosible> rutasUnicas = new ArrayList<>();

        if (posiblesRutas == null || posiblesRutas.isEmpty()) {
            return rutasUnicas;
        }

        for (RutaPosible rutaActual : posiblesRutas) {
            if (rutaActual == null) continue;

            if (mejorRuta != null && mejorRuta.sonIguales(rutaActual.getCamino())) {
                continue;
            }

            boolean esDuplicada = false;
            for (RutaPosible rutaUnica : rutasUnicas) {
                if (rutaActual.sonIguales(rutaUnica.getCamino())) {
                    esDuplicada = true;
                    break;
                }
            }

            if (!esDuplicada) {
                rutasUnicas.add(rutaActual);
            }
        }

        return rutasUnicas;
    }
}