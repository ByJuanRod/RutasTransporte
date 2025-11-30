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

        ArrayList<RutaPosible> rutasUnicas = obtenerRutasUnicas(posiblesRutas);

        if (rutasUnicas.size() == 1) {
            return procesarUnicaRuta(posiblesRutas, rutasUnicas.getFirst());
        }

        return procesarMultiplesRutas(posiblesRutas, rutasUnicas);
    }

    /*
        Nombre: obtenerRutasUnicas
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representan las posibles rutas que se pueden realizar.
        Objetivo: Obtener las rutas que no son duplicadas.
        Retorno: (ArrayList<RutaPosible>) Retorna un arreglo con las rutas unicas.
     */
    private ArrayList<RutaPosible> obtenerRutasUnicas(Stack<RutaPosible> posiblesRutas) {
        ArrayList<RutaPosible> rutasUnicas = new ArrayList<>();

        for (RutaPosible rutaActual : posiblesRutas) {
            if (rutaActual == null) continue;

            if (!esRutaDuplicada(rutaActual, rutasUnicas)) {
                rutasUnicas.add(rutaActual);
            }
        }

        return rutasUnicas;
    }

    /*
        Nombre: esRutaDuplicada
        Argumentos:
            (RutaPosible) rutaActual: Representa la ruta que se va a comparar.
            (ArrayList<RutaPosible>) rutasUnicas: representa las rutas unicas.
        Objetivo: Verificar si la ruta seleccionada es una ruta duplicada.
        Retorno: (boolean) Retorna true si la ruta es un duplicado.
                           Retorna false si la ruta es unica.
     */
    private boolean esRutaDuplicada(RutaPosible rutaActual, ArrayList<RutaPosible> rutasUnicas) {
        for (RutaPosible rutaUnica : rutasUnicas) {
            if (rutaActual.sonIguales(rutaUnica.getCamino())) {
                rutaUnica.agregarCriterioDestacado(rutaActual.getCriteriosDestacados().getFirst());
                return true;
            }
        }
        return false;
    }

    /*
        Nombre: procesarUnicaRuta
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representan las posibles rutas.
            (RutaPosible) rutaUnica: Representa una ruta unica que sera identificada como la mejor.
       Objetivo: Procesar una ruta unica y convertirla en la mejor.
       Retorno: (RutaPosible) Retorna la ruta ubica que coincide con ser la mejor ruta.
     */
    private RutaPosible procesarUnicaRuta(Stack<RutaPosible> posiblesRutas, RutaPosible rutaUnica) {
        RutaPosible mejorRuta = new RutaPosible();
        mejorRuta.clonar(rutaUnica);
        agregarCriteriosDestacados(posiblesRutas, mejorRuta, rutaUnica.getCamino());
        return mejorRuta;
    }

    /*
        Nombre: procesarMultiplesRutas
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las posibles rutas.
            (ArrayList<RutaPosible>) rutasUnicas: Representa las rutas que son unicas.
         Objetivo: Procesar el escenario donde hay varias rutas y construir la mejor en base a esa.
         Retorno: (RutaPosible) Retorna la mejor ruta posible.
     */
    private RutaPosible procesarMultiplesRutas(Stack<RutaPosible> posiblesRutas, ArrayList<RutaPosible> rutasUnicas) {
        RutaPosible mejorRutaBase = encontrarMejorRutaBase(posiblesRutas, rutasUnicas);

        if (mejorRutaBase != null) {
            return construirMejorRuta(posiblesRutas, mejorRutaBase);
        }

        return null;
    }

    /*
        Nombre: encontrarMejorRutaBase
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las rutas posibles.
            (ArrayList<RutaPosible>) rutasUnicas: Representa las rutas unicas.
        Objetivo: Encontrar la mejor ruta entre las que son posibles y unicas.
        Retorno: (RutaPosible) Retorna la mejor ruta.
     */
    private RutaPosible encontrarMejorRutaBase(Stack<RutaPosible> posiblesRutas, ArrayList<RutaPosible> rutasUnicas) {
        RutaPosible mejorRutaBase = null;
        int maxCriteriosCoincidentes = 0;

        for (RutaPosible rutaUnica : rutasUnicas) {
            int criteriosCoincidentes = cantCritiriosPorCamino(posiblesRutas, rutaUnica.getCamino());

            if (criteriosCoincidentes > maxCriteriosCoincidentes) {
                maxCriteriosCoincidentes = criteriosCoincidentes;
                mejorRutaBase = rutaUnica;
            }
        }

        return mejorRutaBase;
    }

    /*
        Nombre: construirMejorRuta
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las posibles rutas.
            (RutaPosible) mejorRutaBase: Representa la mejor ruta actual.
        Objetivo: Construir el objeto de la mejor ruta.
        Retorno: (RutaPosible) Retorna el objeto identificado como la mejor ruta.
     */
    private RutaPosible construirMejorRuta(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRutaBase) {
        RutaPosible mejorRuta = new RutaPosible();
        mejorRuta.clonar(mejorRutaBase);
        agregarCriteriosDestacados(posiblesRutas, mejorRuta, mejorRutaBase.getCamino());
        return mejorRuta;
    }

    /*
        Nombre: agregarCriteriosDestacados
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las posibles rutas que existen.
            (RutaPosible) mejorRuta: Representa la mejor ruta.
            (LinkedList<Ruta>) caminoBase: Representa el camino que se va a buscar.
        Objetivo: Agregar los criterios destacados de la ruta si tienen el mismo camino.
        Retorno: -
     */
    private void agregarCriteriosDestacados(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta, LinkedList<Ruta> caminoBase) {
        for (RutaPosible posible : posiblesRutas) {
            if (posible != null && posible.sonIguales(caminoBase)) {
                if (!posible.getCriteriosDestacados().isEmpty()) {
                    mejorRuta.agregarCriterioDestacado(posible.getCriteriosDestacados().getFirst());
                }
            }
        }
    }

    /*
        Nombre: cantCritiriosPorCamino
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa la lista de rutas destacadas.
            (LinkedList<Ruta>) camino: Representa el camino a evaluar.
        Objetivo: Determinar cuantas de las rutas destacadas tienen el mismo camino.
        Retorno: (int) Retorna la cantidad de rutas que tienen el mismo camino en las rutas destacadas.
     */
    private int cantCritiriosPorCamino(Stack<RutaPosible> posiblesRutas, LinkedList<Ruta> camino) {
        int cantidadCriterios = 0;
        for (RutaPosible posible : posiblesRutas) {
            if (posible != null && posible.sonIguales(camino)) {
                cantidadCriterios++;
            }
        }
        return cantidadCriterios;
    }

    /*
        Nombre: obtenerRutasExceptoMejor
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa la lista de todas las rutas destacadas.
            (RutaPosible) mejorRuta: Representa la ruta que fue destacada como la mejor.
        Objetivo: Obtener las rutas que son distintas a la mejor ruta para evitar duplicados.
        Retorno: (ArrayList<RutaPosible>) Retorna una coleccion de todas las rutas que son distintas a la mejor ruta.
     */
    public ArrayList<RutaPosible> obtenerRutasExceptoMejor(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta) {
        ArrayList<RutaPosible> rutasUnicas = new ArrayList<>();

        if (posiblesRutas == null || posiblesRutas.isEmpty()) {
            return rutasUnicas;
        }


        verificarDuplicacion(posiblesRutas,mejorRuta,rutasUnicas);

        return rutasUnicas;
    }

    /*
        Nombre: verificarDuplicacion
        Argumentos:
            (Stack<RutaPosible>) posiblesRutas: Representa las rutas posibles.
            (RutaPosible) mejorRuta: Representa la mejor ruta.
            (ArrayList<RutaPosible>) rutasUnicas: Representa las rutas unicas.
        Objetivo: Verificar si existen duplicados entre las rutas posibles y las rutas unicas.
        Retorno: -
     */
    public void verificarDuplicacion(Stack<RutaPosible> posiblesRutas, RutaPosible mejorRuta, ArrayList<RutaPosible> rutasUnicas) {
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
    }
}