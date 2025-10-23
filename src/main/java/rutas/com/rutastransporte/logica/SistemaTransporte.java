package rutas.com.rutastransporte.logica;

import java.util.ArrayList;

public class SistemaTransporte {
    public static int genCodigoParada = 0;

    public static int genCodigoRuta = 0;

    public ArrayList<Parada> paradas;

    public ArrayList<Ruta> rutas;

    private SistemaTransporte instancia = null;

    private SistemaTransporte() {
        paradas = new ArrayList<>();
        rutas = new ArrayList<>();
    }

    public SistemaTransporte getSistemaTransporte(){
        if(instancia == null){
            instancia = new SistemaTransporte();
        }
        return instancia;
    }

    public void RegistrarParada(Parada parada){
        paradas.add(parada);
        genCodigoParada++;
    }



}
