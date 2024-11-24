package tpFinal;

import java.util.Date;
import java.util.List;

public class VueloInternacional extends Vuelo {
    private String[] escalas;
    private double valorRefrigerio;
    private int cantRefrigerios;

    public VueloInternacional(String codigo, String origen, String destino, Date fecha, int tripulantes, double[] precios, int[] cantAsientos, 
                              String[] escalas, double valorRefrigerio, int cantRefrigerios) {
        super(codigo, origen, destino, fecha, tripulantes, precios, cantAsientos);
        this.escalas = escalas;
        this.valorRefrigerio = valorRefrigerio;
        this.cantRefrigerios = cantRefrigerios;
        this.tipo = "Internacional";
    }

    @Override
    public String getDetalle() {
        StringBuilder detalle = new StringBuilder();
        detalle.append("Vuelo Internacional\n");
        detalle.append("Código: ").append(codigo).append("\n");
        detalle.append("Origen: ").append(origen).append("\n");
        detalle.append("Destino: ").append(destino).append("\n");
        detalle.append("Fecha de salida: ").append(fecha).append("\n");
        detalle.append("Tripulantes: ").append(tripulantes).append("\n");
        detalle.append("Escalas: ");
        if (escalas.length > 0) {
            for (String escala : escalas) {
                detalle.append(escala).append(" ");
            }
        } else {
            detalle.append("Sin escalas");
        }
        detalle.append("\nValor del refrigerio: ").append(valorRefrigerio).append("\n");
        detalle.append("Cantidad de refrigerios: ").append(cantRefrigerios).append("\n");

        return detalle.toString();
    }

    @Override
    public double precioPorAsiento(int nroAsiento) {
        int asientosTurista = cantAsientos[0];
        int asientosEjecutiva = cantAsientos[1];

        if (nroAsiento <= asientosTurista) {
            return precios[0]; // Turista
        } else if (nroAsiento <= asientosTurista + asientosEjecutiva) {
            return precios[1]; // Ejecutiva
        } else {
            return precios[2]; // Primera clase
        }
    }
    
    public int getCantRefrigerios() {
        return cantRefrigerios;
    }
    
    @Override
    public double costoRefrigerio() {
        return valorRefrigerio;
    }
}
