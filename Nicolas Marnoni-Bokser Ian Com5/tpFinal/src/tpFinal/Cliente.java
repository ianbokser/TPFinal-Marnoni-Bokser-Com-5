package tpFinal;

public class Cliente {
    private int dni;
    private String nombre;
    private String telefono;
    private boolean esPasajero;
    private String codigoVuelo;

    public Cliente(int dni, String nombre, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.esPasajero = false;
        this.codigoVuelo = null; // Inicialmente sin código de vuelo
    }

    public void convertirEnPasajero() {
        this.esPasajero = true;
    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean esPasajero() {
        return esPasajero;
    }

    public String getCodigoVuelo() {
        return codigoVuelo;
    }

    public void setCodigoVuelo(String codigoVuelo) {
        this.codigoVuelo = codigoVuelo;
    }
}
