package rutas.com.rutastransporte.servicios;

import rutas.com.rutastransporte.modelos.Ruta;
import rutas.com.rutastransporte.modelos.RutaPosible;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/*
    Nombre: ServicioMapa
    Tipo: Clase
    Objetivo: Almacenar los metodos correspondientes al apartado de mapa.
 */
public class ServicioMapa {
    private final ServicioEventos servicioEventos = ServicioEventos.getInstancia();

    /*
        Nombre: crearSimulacion
        Argumentos: -
        Objetivo: Crear la simulaciones de eventos en la aplicación.
        Retorno: -
     */
    public void crearSimulacion(){
        servicioEventos.crearSimulacionEventos();
    }

    /*
        Nombre: obtenerMejorRuta
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa la coleccion que contiene todas las rutas posibles.
        Objetivo: Obtener la mejor rutas entre las que fueron destacadas.
        Retorno: (RutaPosible) Retorna la mejor ruta entre las destacadas.
     */
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

    /*
        Nombre: cantCriteriosParaMismoCamino
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa la lista de rutas destacadas.
            (LinkedList<Ruta>) camino: Representa el camino a evaluar.
        Objetivo: Determinar cuantas de las rutas destacadas tienen el mismo camino.
        Retorno: (int) Retorna la cantidad de rutas que tienen el mismo camino en las rutas destacadas.
     */
    private int cantCriteriosParaMismoCamino(Stack<RutaPosible> posiblesRutas, LinkedList<Ruta> camino) {
        int cantidadCriterios = 0;
        for (RutaPosible posible : posiblesRutas) {
            if (posible != null && posible.sonIguales(camino)) {
                cantidadCriterios++;
            }
        }
        return cantidadCriterios;
    }

    /*
        Nombre: obtenerRutasUnicasExcluyendoMejor
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa la lista de todas las rutas destacadas.
            (RutaPosible) mejorRuta: Representa la ruta que fue destacada como la mejor.
        Objetivo: Obtener las rutas que son distintas a la mejor ruta para evitar duplicados.
        Retorno: (ArrayList<RutaPosible>) Retorna una coleccion de todas las rutas que son distintas a la mejor ruta.
     */
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