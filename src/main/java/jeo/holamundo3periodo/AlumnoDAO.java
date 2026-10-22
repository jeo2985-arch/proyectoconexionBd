package jeo.holamundo3periodo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlumnoDAO {
    public int insertar(String nombre, String correo) throws SQLException {
        String sql = "INSERT INTO dbo.Alumnos (nombre, correo) VALUES (?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("No fue posible recuperar el ID insertado.");
    }

    public List<Alumno> listar() throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT id, nombre, correo FROM dbo.Alumnos ORDER BY id";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) alumnos.add(leer(rs));
        }
        return alumnos;
    }

    public Optional<Alumno> buscar(int id) throws SQLException {
        String sql = "SELECT id, nombre, correo FROM dbo.Alumnos WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(leer(rs)) : Optional.empty();
            }
        }
    }

    public boolean actualizar(int id, String nombre, String correo) throws SQLException {
        String sql = "UPDATE dbo.Alumnos SET nombre = ?, correo = ? WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM dbo.Alumnos WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Alumno leer(ResultSet rs) throws SQLException {
        return new Alumno(rs.getInt("id"), rs.getString("nombre"), rs.getString("correo"));
    }
}
