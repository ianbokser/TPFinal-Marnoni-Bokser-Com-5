package tpFinal;

import java.util.Date;

public class VueloPrivado extends Vuelo {
    private int jetsNecesarios;
    private double precio;
    private final int capacidadJet = 15;

    public VueloPrivado(String codigo, String origen, String destino, Date fecha, int tripulantes, double precio, int[] acompaniantes) {
        super(codigo, origen, destino, fecha, tripulantes, new double[]{precio}, new int[]{acompaniantes.length});
        this.tipo = "PRIVADO";
        this.precio = precio;
        this.jetsNecesarios = calcularJetsNecesarios(acompaniantes.length);
    }

    @Override
    public String getDetalle() {
        return "Vuelo Privado [Código: " + codigo + ", Origen: " + origen + ", Destino: " + destino +
               ", Fecha: " + fecha + ", Tripulantes: " + tripulantes + ", Jets Necesarios: " + jetsNecesarios +
               ", Precio Base: $" + precio + ", Costo Total: $" + calcularCostoTotal() + "]";
    }

    public double calcularCostoTotal() {
        double costoBase = precio * jetsNecesarios;
        return costoBase * 1.30;
    }

    public int calcularJetsNecesarios(int pasajeros) {
        return (int) Math.ceil((double) pasajeros / capacidadJet);
    }
}
