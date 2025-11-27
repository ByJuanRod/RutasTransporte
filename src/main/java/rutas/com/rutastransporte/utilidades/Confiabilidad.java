package rutas.com.rutastransporte.utilidades;

public enum Confiabilidad {
    ALTA("Alta","alta.png",80),
    MEDIA("Media","media.png",50),
    BAJA("Baja","baja.png",0);

    private final String nombre;
    private final String imagen;
    private final int minimo;

    public String getNombre(){
        return nombre;
    }

    public String getImagen(){
        return imagen;
    }

    Confiabilidad(String nombre, String imagen, int minimo) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.minimo = minimo;
    }

    public static Confiabilidad getConfiabilidad(int valor){
        for(Confiabilidad confiabilidad : Confiabilidad.values()){
            if(valor >= confiabilidad.minimo){
                return confiabilidad;
            }
        }
        return Confiabilidad.BAJA;
    }
}
