package rutas.com.rutastransporte.utilidades;

/*
    Nombre: Confiabilidad
    Clase: Enum
    Valores: ALTA, MEDIA, BAJA, CRITICA
    Estructura:
        (String) nombre: Representa el nombre de la confiabilidad.
        (String) imagen: Representa la imagen que se muestra asociada a la confiabilidad.
        (int) minimo: Representa el minimo valor que se necesita para cumplir con la condición de confiabilidad.
    Objetivo: Obtener el indice de confiabilidad que tiene una parada.
 */
public enum Confiabilidad {
    ALTA("Alta","alta.png",80),
    MEDIA("Media","media.png",50),
    BAJA("Baja","baja.png",25),
    CRITICA("Crítica","critica.png",0);

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

    /*
        Nombre: getConfiabilidad
        Argumentos:
            (int) valor: Representa el valor que sera tomado como referencia.
        Objetivo: Determinar que grado de confiabilidad se asocia a una parada.
        Retorno: (Confiabilidad) Retorna la confiabilidad asociada al valor ingresado.
     */
    public static Confiabilidad getConfiabilidad(int valor){
        for(Confiabilidad confiabilidad : Confiabilidad.values()){
            if(valor >= confiabilidad.minimo){
                return confiabilidad;
            }
        }
        return Confiabilidad.CRITICA;
    }
}
