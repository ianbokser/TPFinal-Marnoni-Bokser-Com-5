package tpFinal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aerolinea implements IAerolinea {
	private String nombre;
	private String CUIT;
	private Map<Integer, Cliente> clientes;
	private Map<String, Aeropuerto> aeropuertos;
	private Map<String, Vuelo> vuelos;
	private int vueloCount = 0;
	private int contadorVuelosPublicos = 0;
	private int contadorVuelosPrivados = 1;
	private Map<String, Double> recaudacionPorDestino = new HashMap<>();

	
	
	public Aerolinea(String nombre, String CUIT) {
		this.nombre=nombre;
		this.CUIT=CUIT;
		this.clientes = new HashMap<>();
		this.aeropuertos = new HashMap<>();
		this.vuelos = new HashMap<>();
	}

	@Override
	public void registrarCliente(int dni, String nombre, String telefono) {
		 if (clientes.containsKey(dni)) {
	            throw new IllegalArgumentException("El cliente con DNI " + dni + " ya está registrado.");
	        }
	     Cliente nuevoCliente = new Cliente(dni, nombre, telefono);
	     clientes.put(dni, nuevoCliente);
	}

	@Override
	public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) {
		if (aeropuertos.containsKey(nombre)) {
            throw new IllegalArgumentException("El aeropuerto con nombre " + nombre + " ya está registrado.");
        }
		Aeropuerto nuevoAeropuerto = new Aeropuerto(nombre, pais, provincia, direccion);
		aeropuertos.put(nombre, nuevoAeropuerto);		
	}

	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) {
		if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino) ||
	            !aeropuertos.get(origen).getPais().equals("Argentina") || !aeropuertos.get(destino).getPais().equals("Argentina")) {
	        throw new IllegalArgumentException("Origen y destino deben ser aeropuertos en Argentina.");
	    }
	    if (precios.length != 2 || cantAsientos.length != 2) {
	        throw new IllegalArgumentException("Deben especificarse dos precios y dos cantidades de asientos.");
	    }
	    Date fechaVuelo;
	    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
	    try {
	        fechaVuelo = formatoFecha.parse(fecha);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Formato de fecha inválido. Use 'dd/MM/yyyy'.");
	    }

	    String codigoVuelo = contadorVuelosPublicos++ + "-PUB";
	    Vuelo vuelo = new VueloNacional(codigoVuelo, origen, destino, fechaVuelo, tripulantes, valorRefrigerio, precios, cantAsientos);
	    vuelos.put(codigoVuelo, vuelo);

	    return codigoVuelo;
	}
	

	@Override
	public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) {
		 if (origen == null || destino == null || fecha == null || precios == null || cantAsientos == null || escalas == null) {
	            throw new RuntimeException("Los parámetros no pueden ser nulos.");
	        }
	        if (tripulantes <= 0) {
	            throw new RuntimeException("La cantidad de tripulantes debe ser mayor a cero.");
	        }
	        if (cantRefrigerios < 0) {
	            throw new RuntimeException("La cantidad de refrigerios no puede ser negativa.");
	        }
	        if (precios.length != 3 || cantAsientos.length != 3) {
	            throw new RuntimeException("Los arreglos de precios y asientos deben tener exactamente 3 elementos.");
	        }
	        for (int asientos : cantAsientos) {
	            if (asientos < 0) {
	                throw new RuntimeException("La cantidad de asientos no puede ser negativa.");
	            }
	        }
	        for (double precio : precios) {
	            if (precio < 0) {
	                throw new RuntimeException("El precio no puede ser negativo.");
	            }
	        }

	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Date fechaVuelo;
	        try {
	            fechaVuelo = sdf.parse(fecha);
	        } catch (ParseException e) {
	            throw new RuntimeException("La fecha debe estar en el formato dd/MM/yyyy.");
	        }

	        if (fechaVuelo.before(new Date())) {
	            throw new RuntimeException("La fecha de vuelo debe ser posterior a la actual.");
	        }

	        vueloCount++; 
	        String codigoVuelo = vueloCount + "-PUB";
	        VueloInternacional vueloInternacional = new VueloInternacional(codigoVuelo, origen, destino, fechaVuelo, tripulantes,
	                precios, cantAsientos, escalas, valorRefrigerio, cantRefrigerios);
	        vuelos.put(codigoVuelo, vueloInternacional);
	        return codigoVuelo;
	}


	@Override
	public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,
	                                 int dniComprador, int[] acompaniantes) {
	    if (!aeropuertos.containsKey(origen) || !aeropuertos.containsKey(destino)) {
	        throw new IllegalArgumentException("Origen o destino no válidos.");
	    }

	    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
	    Date fechaVuelo;
	    try {
	        fechaVuelo = formatoFecha.parse(fecha);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Formato de fecha inválido. Use 'dd/MM/yyyy'.");
	    }

	    Date fechaActual = new Date();
	    if (fechaVuelo.before(fechaActual)) {
	        throw new IllegalArgumentException("La fecha del vuelo debe ser posterior a la fecha actual.");
	    }
	    
	    int pasajerosTotales = 1 + acompaniantes.length;
	    int jetsNecesarios = (int) Math.ceil((double) pasajerosTotales / 15);

	    double totalPorVueloPrivado = precio * jetsNecesarios * 1.3;

	    recaudacionPorDestino.put(destino, totalPorVueloPrivado);

	    String codigoVuelo = (contadorVuelosPrivados++) + "-PRI";
	    VueloPrivado vueloPrivado = new VueloPrivado(codigoVuelo, origen, destino, fechaVuelo, tripulantes, precio, acompaniantes);
	    vuelos.put(codigoVuelo, vueloPrivado);

	    Cliente clienteComprador = clientes.get(dniComprador);
	    if (clienteComprador == null) {
	        throw new IllegalArgumentException("El cliente comprador no está registrado.");
	    }
	    clienteComprador.setCodigoVuelo(codigoVuelo);

	    return codigoVuelo;
	}

	@Override
	public Map<Integer, String> asientosDisponibles(String codVuelo) {
	    Vuelo vuelo = vuelos.get(codVuelo);
	    if (vuelo == null) {
	        throw new IllegalArgumentException("Código de vuelo no válido.");
	    }
	    
	    Map<Integer, String> disponibilidad = new HashMap<>();
	    List<Integer> asientos = vuelo.consultarAsientosDisponibles();
	    
	    for (int asiento : asientos) {
	        disponibilidad.put(asiento, "Disponible");
	    }
	    
	    return disponibilidad;
	}

	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
	    Vuelo vuelo = vuelos.get(codVuelo);
	    if (vuelo == null) {
	        throw new IllegalArgumentException("Código de vuelo no válido.");
	    }
	    
	    Cliente cliente = clientes.get(dni);
	    if (cliente == null) {
	        throw new IllegalArgumentException("El cliente no está registrado.");
	    }
	    
	    if (!vuelo.consultarAsientosDisponibles().contains(nroAsiento)) {
	        throw new IllegalArgumentException("El asiento " + nroAsiento + " no está disponible.");
	    }
	    
	    double precioAsiento = vuelo.precioPorAsiento(nroAsiento);
	    double valorRefrigerio = vuelo.costoRefrigerio();
	    double totalPorPasaje = (precioAsiento + (valorRefrigerio * ((vuelo instanceof VueloInternacional) ? ((VueloInternacional) vuelo).getCantRefrigerios() : 0))) * 1.2;

	    String destino = vuelo.getDestino();
	    recaudacionPorDestino.put(destino, recaudacionPorDestino.getOrDefault(destino, 0.0) + totalPorPasaje);

	    vuelo.reservarAsiento(nroAsiento);
	    if (!cliente.esPasajero()) {
	        cliente.convertirEnPasajero();
	    }
	    cliente.setCodigoVuelo(codVuelo);

	    return nroAsiento;
	}




	@Override
	public List<String> consultarVuelosSimilares(String origen, String destino, String fecha) {
	    List<String> vuelosSimilares = new ArrayList<>();
	    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
	    Date fechaConsulta;
	    try {
	        fechaConsulta = formatoFecha.parse(fecha);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException("Formato de fecha inválido. Use 'dd/MM/yyyy'.");
	    }

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(fechaConsulta);
	    cal.add(Calendar.DAY_OF_YEAR, 7);
	    Date fechaLimite = cal.getTime();

	    for (Vuelo vuelo : vuelos.values()) {
	        if (vuelo.getOrigen().equals(origen) && vuelo.getDestino().equals(destino)) {
	            Date fechaVuelo = vuelo.getFecha();
	            if (!fechaVuelo.before(fechaConsulta) && !fechaVuelo.after(fechaLimite)) {
	                vuelosSimilares.add(vuelo.getCodigo());
	            }
	        }
	    }

	    return vuelosSimilares;
	}



	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
	    Vuelo vuelo = vuelos.get(codVuelo);
	    if (vuelo == null) {
	        throw new IllegalArgumentException("Código de vuelo no válido.");
	    }
	    
	    Cliente cliente = clientes.get(dni);
	    if (cliente == null || !cliente.esPasajero()) {
	        throw new IllegalArgumentException("El cliente no es un pasajero o no está registrado.");
	    }
	    vuelo.consultarAsientosDisponibles().add(nroAsiento);
	    System.out.println("Pasaje cancelado exitosamente.");
	}

	@Override
	public void cancelarPasaje(int dni, int codPasaje) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> cancelarVuelo(String codVuelo) {
		
	    Vuelo vueloCancelado = vuelos.remove(codVuelo);
	    if (vueloCancelado == null) {
	        throw new IllegalArgumentException("Código de vuelo no válido.");
	    }
	    
	    List<String> resultado = new ArrayList<>();
	    for (Cliente cliente : clientes.values()) {
	        if (codVuelo.equals(cliente.getCodigoVuelo())) {
	            List<String> vuelosSimilares = consultarVuelosSimilares(
	                vueloCancelado.getOrigen(),
	                vueloCancelado.getDestino(),
	                new SimpleDateFormat("dd/MM/yyyy").format(vueloCancelado.getFecha())
	            );
	            
	            if (!vuelosSimilares.isEmpty()) {
	                String nuevoVuelo = vuelosSimilares.get(0);
	                cliente.setCodigoVuelo(nuevoVuelo);
	                String registro = String.format("%d - %s - %s - %s",
	                    cliente.getDni(),
	                    cliente.getNombre(),
	                    cliente.getTelefono(),
	                    nuevoVuelo
	                );
	                resultado.add(registro);
	            } else {
	                cliente.setCodigoVuelo(null);
	            }
	        }
	    }

	    return resultado;
	}


	@Override
	public double totalRecaudado(String destino) {
	    return recaudacionPorDestino.getOrDefault(destino, 0.0);
	}


	@Override
	public String detalleDeVuelo(String codVuelo) {
	    Vuelo vuelo = vuelos.get(codVuelo);
	    if (vuelo == null) {
	        throw new IllegalArgumentException("Código de vuelo no válido.");
	    }

	    // Crear un StringBuilder para construir el detalle
	    StringBuilder detalle = new StringBuilder();
	    detalle.append(codVuelo)
	           .append(" - ")
	           .append(vuelo.getOrigen())
	           .append(" - ")
	           .append(vuelo.getDestino())
	           .append(" - ")
	           .append(new SimpleDateFormat("dd/MM/yyyy").format(vuelo.getFecha()));

	    if (vuelo instanceof VueloNacional) {
	        detalle.append(" - NACIONAL");
	    } else if (vuelo instanceof VueloInternacional) {
	        detalle.append(" - INTERNACIONAL");
	    } else if (vuelo instanceof VueloPrivado) {
	        VueloPrivado vueloPrivado = (VueloPrivado) vuelo;
	        detalle.append(" - PRIVADO (")
	               .append(String.valueOf(vueloPrivado.calcularJetsNecesarios(vueloPrivado.tripulantes)))
	               .append(")");
	    }
	    return detalle.toString();
	}

}
