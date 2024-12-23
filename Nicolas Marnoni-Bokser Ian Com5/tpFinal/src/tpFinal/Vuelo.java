package tpFinal;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public abstract class Vuelo {
    protected String codigo;
    protected String origen;
    protected String destino;
    protected Date fecha;            
    protected int tripulantes;      
    protected double[] precios;      
    protected int[] cantAsientos;     
    protected List<Integer> asientosDisponibles; 
    protected String tipo;            

    public Vuelo(String codigo, String origen, String destino, Date fecha, int tripulantes, double[] precios, int[] cantAsientos) {
    	this.codigo = codigo;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.tripulantes = tripulantes;
        this.precios = precios;
        this.cantAsientos = cantAsientos;
        this.asientosDisponibles = new ArrayList<>();
        generarAsientosDisponibles();
    }

    private void generarAsientosDisponibles() {
        int asientoNumero = 1;
        for (int i = 0; i < cantAsientos.length; i++) {
            for (int j = 0; j < cantAsientos[i]; j++) {
                asientosDisponibles.add(asientoNumero++);
            }
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public abstract String getDetalle();

    public List<Integer> consultarAsientosDisponibles() {
        return asientosDisponibles;
    }

    public boolean reservarAsiento(int numeroAsiento) {
        return asientosDisponibles.remove(Integer.valueOf(numeroAsiento));
    }

    public Date getFecha() {
        return fecha;
    }

	public double costoRefrigerio() {
		return 0;
	}

	public double precioPorAsiento(int nroAsiento) {
		return 0;
	}
}
