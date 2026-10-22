package jeo.holamundo3periodo;

public class Alumno {
    private final int id;
    private final String nombre;
    private final String correo;

    public Alumno(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
