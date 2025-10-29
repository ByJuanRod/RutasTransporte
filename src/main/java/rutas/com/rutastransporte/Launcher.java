package rutas.com.rutastransporte;

import javafx.application.Application;
import rutas.com.rutastransporte.Modelos.Parada;
import rutas.com.rutastransporte.Modelos.Ruta;
import rutas.com.rutastransporte.Servicios.GrafoTransporte;
import rutas.com.rutastransporte.Servicios.RepositorioDatos;

import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        Application.launch(MenuPrincipal.class, args);
    }
}
