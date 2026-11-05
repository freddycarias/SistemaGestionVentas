package modelo;

import java.math.BigDecimal;

public class Producto {

    private int idProducto;
    private String nombre;
    private BigDecimal precioActual;

    public Producto(int idProducto, String nombre,
            BigDecimal precioActual) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioActual = precioActual;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecioActual() {
        return precioActual;
    }

    @Override
    public String toString() {
        return nombre + " - Q" + precioActual;
    }
}
