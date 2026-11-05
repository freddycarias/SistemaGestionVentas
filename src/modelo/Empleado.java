package modelo;

public class Empleado {

    private int idEmpleado;
    private String nombre;
    private String apellido;

    public Empleado(int idEmpleado, String nombre, String apellido) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}
