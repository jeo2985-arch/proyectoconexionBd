package jeo.holamundo3periodo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
    private Conexion() {}

    public static Connection conectar() throws SQLException {
        String host = requerido("DB_HOST");
        String db = System.getenv().getOrDefault("DB_NAME", "AvanzadaII");
        String user = requerido("DB_USER");
        String pass = requerido("DB_PASSWORD");
        if (!host.matches("[a-zA-Z0-9.-]+")) {
            throw new IllegalArgumentException("DB_HOST debe ser un nombre DNS válido, sin puerto ni protocolo.");
        }
        if (!db.matches("[a-zA-Z0-9_-]+")) {
            throw new IllegalArgumentException("DB_NAME contiene caracteres no permitidos.");
        }
        String url = "jdbc:sqlserver://" + host + ":1433;databaseName=" + db
                + ";encrypt=true;trustServerCertificate=false;loginTimeout=15;";
        return DriverManager.getConnection(url, user, pass);
    }

    private static String requerido(String clave) {
        String valor = System.getenv(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta configurar el secreto " + clave + " en GitHub Codespaces.");
        }
        return valor;
    }
}
