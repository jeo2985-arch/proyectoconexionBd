package jeo.holamundo3periodo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class conexionDB {
     private static final String URL =
            "jdbc:sqlserver://ORTEGA\\MSSQLSERVER01;"
            + "databaseName=AvanzadaII;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "Snow2010@";

    private static Connection conexion = null;
    private conexionDB(){}
    public static Connection getConexion() {

        try {

            if (conexion == null || conexion.isClosed()) {

                Class.forName(
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                );

                conexion = DriverManager.getConnection( URL,USER,PASSWORD
                );

                System.out.println(">> Conexion exitosa a SQL Server");
            }

        } catch (ClassNotFoundException e) {

            System.err.println(
                "Error: No se encontro el Driver de SQL Server JDBC: "
                + e.getMessage()
            );

        } catch (SQLException e) {

            System.err.println(
                "Error al conectar con SQL Server: "
                + e.getMessage()
            );
        }

        return conexion;
    }
    
    public static void cerrarConexion() {
    try {
        if (conexion != null && !conexion.isClosed()) {

            conexion.close();

            System.out.println("--> La conexion con SQL fue cerrada");
        }

    } catch (SQLException e) {

        System.err.println(
            "Error al cerrar la conexión: " + e.getMessage()
        );
    }
}
}



