package modelo;

public class Mesa {
    private int idMesa;
    private int numero;
    private int capacidad;
    private boolean disponible;

    public Mesa(int idMesa, int numero, int capacidad) {
        this.idMesa = idMesa;
        this.numero = numero;
        this.capacidad = capacidad;
        this.disponible = true;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void reservar() {
        if (disponible) {
            disponible = false;
            System.out.println("✅ Mesa " + numero + " reservada.");
        } else {
            System.out.println("⚠️ La mesa " + numero + " ya está ocupada.");
        }
    }

    public void liberar() {
        disponible = true;
        System.out.println("Mesa " + numero + " liberada.");
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "Mesa " + numero + " (capacidad: " + capacidad + ", disponible: " + disponible + ")";
    }
}