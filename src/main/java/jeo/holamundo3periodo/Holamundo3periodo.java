package jeo.holamundo3periodo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Holamundo3periodo {

    public static void main(String[] args) {


        System.out.println("=== Ejecutando operaciones en tabla Alumnos ===");

        Connection cn = conexionDB.getConexion();

        if (cn == null) {
            System.out.println("No se puede establecer la conexion");
            return;
        }

        PreparedStatement psBuscar = null;
        PreparedStatement psInsertar = null;
        PreparedStatement psActualizar = null;
        PreparedStatement psEliminar = null;
        PreparedStatement psSeleccionar = null;

        ResultSet rsBuscar = null;
        ResultSet rs = null;

        try {

            // =====================================================
            // PARTE A: VERIFICAR E INSERTAR ALUMNO
            // =====================================================

            String nombreNuevo = "Pedro Contreras";
            String correoNuevo = "Pcontreras@uth.hn";

            System.out.println("\n1. Verificando si el alumno ya existe...");

            String sqlBuscar = "SELECT COUNT(*) FROM Alumnos WHERE correo = ?";

            psBuscar = cn.prepareStatement(sqlBuscar);
            psBuscar.setString(1, correoNuevo);

            rsBuscar = psBuscar.executeQuery();

            if (rsBuscar.next()) {

                int cantidad = rsBuscar.getInt(1);

                if (cantidad > 0) {

                    System.out.println( "El alumno con correo " + correoNuevo + " ya existe." );

                } else {

                    System.out.println( "\n2. Insertando nuevo alumno..." );

                    String sqlInsert = "INSERT INTO Alumnos(nombre, correo) VALUES(?, ?)";

                    psInsertar = cn.prepareStatement(sqlInsert);

                    psInsertar.setString(1, nombreNuevo);
                    psInsertar.setString(2, correoNuevo);

                    int filasAfectadas =
                            psInsertar.executeUpdate();

                    if (filasAfectadas > 0) {
                        System.out.println( "Alumno insertado con exito." );
                    }
                }
            }


            // =====================================================
            // PARTE B: ACTUALIZAR ALUMNO
            // =====================================================

            System.out.println("\n3. Actualizando alumno...");

            int idActualizar = 3;

            String nuevoNombre = "Jose Contreras";

            String nuevoCorreo = "Jcontreras@uth.hn";

            String sqlActualizar = "UPDATE Alumnos " + "SET nombre = ?, correo = ? " + "WHERE id = ?";

            psActualizar = cn.prepareStatement(sqlActualizar);
            psActualizar.setString(1, nuevoNombre);
            psActualizar.setString(2, nuevoCorreo);
            psActualizar.setInt(3, idActualizar);

            int filasActualizadas =psActualizar.executeUpdate();

            if (filasActualizadas > 0) {System.out.println( "Alumno actualizado correctamente.");

            } else {

                System.out.println("No se encontro un alumno con ID: " + idActualizar );
            }


            // =====================================================
            // PARTE C: ELIMINAR ALUMNO
            // =====================================================

            System.out.println("\n4. Eliminando alumno...");

            int idEliminar = 10;

            String sqlEliminar = "DELETE FROM Alumnos WHERE id = ?";

            psEliminar = cn.prepareStatement(sqlEliminar);

            psEliminar.setInt(1, idEliminar);

            int filasEliminadas = psEliminar.executeUpdate();

            if (filasEliminadas > 0) {

                System.out.println( "Alumno eliminado correctamente.");

            } else { 
                System.out.println( "No se encontro un alumno con ID: " + idEliminar );
            }


            // =====================================================
            // PARTE D: MOSTRAR TODOS LOS ALUMNOS
            // =====================================================

            System.out.println("\n5. Lista de alumnos registrados" );

            String sqlSelect = "SELECT id, nombre, correo FROM Alumnos";

            psSeleccionar = cn.prepareStatement(sqlSelect);

            rs = psSeleccionar.executeQuery();

            System.out.println("\n==========================================================" );

            System.out.println("ID  | Nombre               | Correo" );

            System.out.println("==========================================================");

            while (rs.next()) {
                int id =
                        rs.getInt("id");

                String nombre =rs.getString("nombre");

                String correo =rs.getString("correo");

                System.out.printf("%-3d | %-20s | %s%n",id,nombre,correo);
            }

            System.out.println("==========================================================");


        } catch (SQLException e) {

            System.err.println("Ocurrio un error con la base de datos: " + e.getMessage());

        } finally {

            try {

                if (rsBuscar != null) {
                    rsBuscar.close();
                }

                if (rs != null) {
                    rs.close();
                }

                if (psBuscar != null) {
                    psBuscar.close();
                }

                if (psInsertar != null) {
                    psInsertar.close();
                }

                if (psActualizar != null) {
                    psActualizar.close();
                }

                if (psEliminar != null) {
                    psEliminar.close();
                }

                if (psSeleccionar != null) {
                    psSeleccionar.close();
                }

                conexionDB.cerrarConexion();

            } catch (SQLException e) {

                System.err.println("Error al cerrar los recursos: "+ e.getMessage());
            }
        }
    }
}
