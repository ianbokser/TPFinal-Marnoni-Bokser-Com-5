package tpFinal;

import java.sql.Date;

public class VueloNacional extends Vuelo{
    private double valorRefrigerio;
    private String tipo;
    private String codigo;

    

    public VueloNacional(String codigo, String origen, String destino, java.util.Date fechaVuelo, int tripulantes, double valorRefrigerio,
            double[] precios, int[] cantAsientos) {
        super(codigo, origen, destino, fechaVuelo, tripulantes, precios, cantAsientos);
        this.codigo = codigo;
        this.valorRefrigerio = valorRefrigerio;
        this.tipo = "NACIONAL";
    }
    
    @Override
    public String getDetalle() {
        return codigo + " - " + origen + " - " + destino + " - " + fecha.toString() + " - " + tipo;
    }

    @Override
    public double precioPorAsiento(int nroAsiento) {
        if (nroAsiento <= asientosDisponibles.size()) {
            return precios[0];
        }
        throw new IllegalArgumentException("Número de asiento no válido: " + nroAsiento);
    }

    @Override
    public double costoRefrigerio() {
        return valorRefrigerio;
    }
    
    public void setTipo(String tipo) {
    	this.tipo = tipo;
    }
}
