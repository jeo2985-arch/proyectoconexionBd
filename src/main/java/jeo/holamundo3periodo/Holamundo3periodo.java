package jeo.holamundo3periodo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Holamundo3periodo {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final AlumnoDAO DAO = new AlumnoDAO();

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTIÓN DE ALUMNOS ===");
        try (var prueba = Conexion.conectar()) {
            System.out.println("Conexión segura a Azure SQL establecida.\n");
        } catch (SQLException | IllegalStateException | IllegalArgumentException ex) {
            System.err.println("No se pudo conectar: " + ex.getMessage());
            System.err.println("Revisa DB_HOST, DB_NAME, DB_USER, DB_PASSWORD y el firewall de Azure.");
            return;
        }
        while (true) {
            System.out.println("\n1. Registrar alumno\n2. Listar alumnos\n3. Buscar por ID\n4. Actualizar alumno\n5. Eliminar alumno\n0. Salir");
            int opcion = entero("Selecciona una opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> actualizar();
                    case 5 -> eliminar();
                    case 0 -> { System.out.println("Hasta pronto."); return; }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (SQLException ex) {
                System.err.println("Error de base de datos (código " + ex.getErrorCode() + "): " + ex.getMessage());
            }
        }
    }

    private static void registrar() throws SQLException {
        String nombre = texto("Nombre completo", 100);
        String correo = correo();
        int id = DAO.insertar(nombre, correo);
        System.out.println("Alumno registrado con ID: " + id);
    }

    private static void listar() throws SQLException {
        List<Alumno> alumnos = DAO.listar();
        if (alumnos.isEmpty()) { System.out.println("No hay alumnos registrados."); return; }
        System.out.printf("%-6s %-35s %-40s%n", "ID", "NOMBRE", "CORREO");
        for (Alumno a : alumnos) mostrar(a);
    }

    private static void buscar() throws SQLException {
        int id = enteroPositivo("ID del alumno: ");
        Optional<Alumno> alumno = DAO.buscar(id);
        if (alumno.isPresent()) mostrar(alumno.get());
        else System.out.println("No existe ese alumno.");
    }

    private static void actualizar() throws SQLException {
        int id = enteroPositivo("ID a actualizar: ");
        Optional<Alumno> actual = DAO.buscar(id);
        if (actual.isEmpty()) { System.out.println("No existe ese alumno."); return; }
        mostrar(actual.get());
        String nombre = texto("Nuevo nombre", 100);
        String correo = correo();
        System.out.println(DAO.actualizar(id, nombre, correo) ? "Alumno actualizado." : "No se actualizó el alumno.");
    }

    private static void eliminar() throws SQLException {
        int id = enteroPositivo("ID a eliminar: ");
        Optional<Alumno> actual = DAO.buscar(id);
        if (actual.isEmpty()) { System.out.println("No existe ese alumno."); return; }
        mostrar(actual.get());
        System.out.print("Confirma eliminación escribiendo SI: ");
        if (!ENTRADA.nextLine().trim().equalsIgnoreCase("SI")) {
            System.out.println("Operación cancelada."); return;
        }
        System.out.println(DAO.eliminar(id) ? "Alumno eliminado." : "No se eliminó el alumno.");
    }

    private static String texto(String etiqueta, int longitudMaxima) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String valor = ENTRADA.nextLine().trim();
            if (!valor.isEmpty() && valor.length() <= longitudMaxima) return valor;
            System.out.println("Debes ingresar entre 1 y " + longitudMaxima + " caracteres.");
        }
    }

    private static String correo() {
        while (true) {
            String valor = texto("Correo electrónico", 100);
            if (valor.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) return valor;
            System.out.println("Introduce un correo electrónico válido.");
        }
    }

    private static int entero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try { return Integer.parseInt(ENTRADA.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Ingresa un número entero válido."); }
        }
    }

    private static int enteroPositivo(String mensaje) {
        while (true) {
            int id = entero(mensaje);
            if (id > 0) return id;
            System.out.println("El ID debe ser positivo.");
        }
    }

    private static void mostrar(Alumno a) {
        System.out.printf("%-6d %-35s %-40s%n", a.getId(), a.getNombre(), a.getCorreo());
    }
}
