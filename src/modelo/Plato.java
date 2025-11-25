package modelo;

public class Plato {
private int idPlato;
private String nombre;
private double precio;
private String categoria;


public Plato(int idPlato, String nombre, double precio, String categoria) {
    this.idPlato = idPlato;
    this.nombre = nombre;
    this.precio = precio;
    this.categoria = categoria;
}

public int getIdPlato() {
    return idPlato;
}

public String getNombre() {
    return nombre;
}

public double getPrecio() {
    return precio;
}

public String getCategoria() {
    return categoria;
}

public void mostrarInfo() {
    System.out.println(
        "ID: " + idPlato +
        " | " + nombre +
        " | S/ " + precio +
        " | " + categoria
    );
}

}
