package Eventos;
import java.time.*;
import java.util.*;
import Usuarios.*;

/**
 * Representa un evento disponible en la plataforma BoletaMaster.
 * Cada evento pertenece a un organizador, tiene un venue asignado,
 * y cuenta con un conjunto de localidades, ofertas y un estado de ventas.
 */
public class Evento{
	// Atributos 
	private String nombre;
	private int id;
	private Venue venue;
	private LocalDateTime fecha;
	private String estado; // abierto, cancelado o finalizado.
    private Organizador organizador;
	private String tipoEvento;
	private Administrador admin;
	private double cuotaEvento;
	
    private List<Localidad> localidades;
    private List<Oferta> ofertas;
	
	// Ctor

    /**
     * Crea un nuevo evento.
     * @param nombre Nombre del evento.
     * @param id Identificador de Evento.
     * @param venue Lugar donde se realizará.
     * @param fecha Fecha y hora del evento.
     * @param organizador Organizador responsable.
     * @param tipoEvento Tipo o categoría.
     * @pre venue aprobado, organizador válido, fecha futura.
     * @post El evento queda en estado “pendiente”.
     */
	public Evento(String nombre, int id, Venue venue, LocalDateTime fecha, Organizador organizador, String tipoEvento, Administrador admin) {
        this.nombre = nombre;
        this.id = id;
        this.venue = venue;
        this.fecha = fecha;
        this.organizador = organizador;
        this.tipoEvento = tipoEvento;
        this.estado = "Activo";
        this.localidades = new ArrayList<>();
        this.ofertas = new ArrayList<>();
        this.admin = admin;
        this.cuotaEvento = admin.getCargoServicioParaEvento(tipoEvento);
	}

	// Getters y Setters
	
	public Administrador getAdmin() {
		return admin;
	}
	
	
    public String getNombre() {
    	return nombre; 
    	}
    
	
    public void setNombre(String nombre) { this.nombre = nombre; }

	
    public int getId() {
    	return this.id; 
    	}
    
	
    public void setId(int id) { 
    	this.id = id; 
    	}
	
    public Venue getVenue() { 
    	return venue; 
    	}
    
	
    public void setVenue(Venue venue) {
    	this.venue = venue; 
    	}

	
    public LocalDateTime getFecha() { 
    	return fecha; 
    	}
    
	
    public void setFecha(LocalDateTime fecha) { 
    	this.fecha = fecha; 
    	}
    
	
    public Organizador getOrganizador() { 
    	return organizador; 
    	}
    
	
    public void setOrganizador(Organizador organizador) {
    	this.organizador = organizador;
    	}

	
    public String getTipoEvento() { 
    	return tipoEvento; 
    	}
    
	
    public void setTipoEvento(String tipoEvento) { 
    	this.tipoEvento = tipoEvento; 
    	}

	
    public String getEstado() { 
    	return estado; 
    	}
    
	
    public void setEstado(String estado) { this.estado = estado; }
    
	
    public List<Localidad> getLocalidades() { 
    	return localidades; 
    	}
    
	
    public List<Oferta> getOfertas() { 
    	return ofertas; 
    	}
	
    public double getCuotaEvento() { 
    	return cuotaEvento; 
    	}
    
    
  
    
    /**
     * Cancela el evento por decisión del organizador.
     * @param motivo Descripción de la causa de cancelación.
     * @pre El evento está en estado activo (publicado o ventas abiertas).
     * @post El evento pasa a estado “cancelado”, y se programan los reembolsos.
     */
    public void cancelarPorOrganizador(String motivo) throws IllegalStateException {
        if (estado.equals("finalizado")){
            throw new IllegalStateException("No se puede cancelar un evento en estado actual.");
        }
        this.estado = "cancelado";
        
    }
    
    /**
     * Cancela el evento por decisión del administrador.
     * @pre El evento está activo.
     * @post El evento pasa a estado “cancelado por administrador”.
     */
    public void cancelarPorAdministrador()throws IllegalStateException {
    	if (estado.equals("finalizado")){
            throw new IllegalStateException("No se puede cancelar un evento en estado actual.");
        }
        this.estado = "cancelado por administrador";
        
    }
    
    /**
     * Modifica la información básica del evento.
     * @param nuevoNombre Nuevo nombre del evento.
     * @param nuevaDescripcion Nueva descripción.
     * @param nuevaFecha Nueva fecha del evento.
     * @pre El evento no tiene ventas abiertas.
     * @post Los datos del evento son actualizados.
     */
    public void modificarDatos(String nuevoNombre, int nuevoId, LocalDateTime nuevaFecha) {
        this.nombre = nuevoNombre;
        this.id = nuevoId;
        this.fecha = nuevaFecha;
    }
    
   


    /**
     * Crea una nueva localidad para el evento.
     * @param nombre Nombre de la localidad.
     * @param numerada Si los asientos son numerados.
     * @param capacidad Número de asientos o cupos.
     * @param precioBase Precio base por tiquete.
     * @return La localidad creada.
     * @pre nombre no vacío, capacidad > 0, precioBase >= 0.
     * @post La localidad queda asociada al evento.
     */
    public Localidad crearLocalidad(String nombre, boolean numerada, int capacidad, double precioBase, double precioBasePaquetesxUnidad, double precioPaseTemporadaxUnidad, double precioPaseDeluxexUnidad) {
        Localidad nueva = new Localidad(nombre, numerada, capacidad, precioBase, this,  precioBasePaquetesxUnidad,  precioPaseTemporadaxUnidad,  precioPaseDeluxexUnidad);
        localidades.add(nueva);
        return nueva;
    }
    
    /**
     * Busca una localidad por nombre.
     * @param nombre Nombre buscado.
     * @return Localidad si existe, null si no.
     */
    public Localidad buscarLocalidad(String nombre) {
        for (Localidad l : localidades) {
            if (l.getNombre().equalsIgnoreCase(nombre)) return l;
        }
        return null;
    }
    

   
    /**
     * Crea una oferta de descuento para el evento.
     * @param descripcion Descripción de la oferta.
     * @param porcentajeDescuento Porcentaje de descuento (0–100).
     * @param fechaInicio Fecha de inicio de la oferta.
     * @param fechaFin Fecha de fin de la oferta.
     * @return La oferta creada.
     * @pre porcentaje entre 0 y 100, fechas válidas.
     * @post La oferta queda registrada en el evento.
     */
    public Oferta crearOferta(double porcentajeDescuento,
                              LocalDateTime fechaInicio, LocalDateTime fechaFin, Localidad localidad) {
        Oferta nueva = new Oferta(porcentajeDescuento, fechaInicio, fechaFin, localidad);
        ofertas.add(nueva);
        return nueva;
    }
    

    /**
     * Obtiene las ofertas activas para una fecha dada.
     * @param fecha Fecha de referencia.
     * @return Lista de ofertas activas.
     */
    public List<Oferta> obtenerOfertasActivas(LocalDateTime fecha) {
        List<Oferta> activas = new ArrayList<>();
        for (Oferta o : ofertas) {
            if (o.estaActiva(fecha)) activas.add(o);
        }
        return activas;
    }

    

    /**
     * Entrega una cortesía a un cliente.
     * @param cliente Cliente que recibe el beneficio.
     * @param localidad Localidad correspondiente.
     * @pre La localidad tiene cupos disponibles.
     * @post Se genera un tiquete gratuito para el cliente.
     */
    public void entregarCortesia(Cliente cliente, Localidad localidad) {
        if (cliente == null || localidad == null)
            throw new IllegalArgumentException("Datos inválidos.");
        localidad.reservarAsiento();
        // TODO: Generar tiquete gratuito (precio = 0).
    }

    
    /**
     * Calcula el total de tiquetes vendidos en el evento.
     * @return Número de tiquetes vendidos.
     */
    public int calcularTotalTiquetesVendidos() {
        int total = 0;
        for (Localidad l : localidades) {
            total += l.getTiquetesVendidos();
        }
        return total;
    }

    /**
     * Calcula el ingreso total del evento.
     * @return Suma de todos los ingresos generados por ventas.
     */
    public double calcularIngresosTotales() {
        double total = 0;
        for (Localidad l : localidades) {
            total += l.calcularIngresos();
        }
        return total;
    }
}