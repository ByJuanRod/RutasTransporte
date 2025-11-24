package rutas.com.rutastransporte.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
    Nombre: ConexionDB
    Tipo: Clase
    Objetivo: Almacenar los datos de la conexión MySQL para su reutilización.
 */
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/rutas_transporte_db";
    private static final String USER = "root";
    private static final String PASSWORD = "claveruta";

    /*
        Nombre: getConexion
        Argumentos: -
        Objetivo: Procesar la conexión a la base de datos
        Retorno: (Connection) Retorna la conexión a la base de datos
     */
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL.");
            throw new SQLException("Driver no encontrado", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}