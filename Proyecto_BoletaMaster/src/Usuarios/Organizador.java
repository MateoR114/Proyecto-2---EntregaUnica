package Usuarios;
import java.util.*;
import Eventos.*;
import java.time.*;

/**
 * Representa a un organizador de eventos dentro del sistema BoletaMaster.
 * Puede crear eventos, definir sus localidades, registrar ofertas, 
 * y generar reportes financieros asociados a sus ventas.
 */
public class Organizador extends Usuario{
	// Atributos 
	private int id;
	private String nombreOrganización;
	private double ventasLocalidad;
	private double gananciaSinRecargos;
	private double saldo;
    private ArrayList<Evento> eventosOrganizados;
    private ArrayList<ReporteFinanciero> reportesGenerados;
	
	// Ctor

    /**
     * Crea un nuevo organizador del sistema.
     * @param login Login único del organizador.
     * @param password Contraseña.
     * @param nombre Nombre visible del organizador.
     * @param id Identificador interno.
     * @param saldo Saldo inicial usuario.
     * @param nombreOrganización Nombre de la organización.
     * @pre login, password y nombre no vacíos; id > 0.
     * @post El organizador queda creado con listas vacías de eventos y reportes.
     */
	public Organizador(String login, String password, String nombre, int id, double saldo, String nombreOrganización) {
		super(login, password, nombre);
		this.setSaldo(0.0);
		this.setGananciasinRecargos(0.0);
		this.setId(id);
		this.setNombreOrganización(nombreOrganización);
		this.setVentasLocalidad(0.0);
        this.eventosOrganizados = new ArrayList<Evento>();
        this.reportesGenerados = new ArrayList<ReporteFinanciero>();
		
	}
	
	// Getters y Setters
	
    /** Devuelve el ID del organizador. */
	public int getId() {
		return id;
	}
	
    /** Establece un nuevo ID. */
	public void setId(int id) {
		this.id = id;
	}

	public String getNombreOrganización() {
		return nombreOrganización;
	}

	public void setNombreOrganización(String nombreOrganización) {
		this.nombreOrganización = nombreOrganización;
	}

	public double getVentasLocalidad() {
		return ventasLocalidad;
	}

	public void setVentasLocalidad(double d) {
		this.ventasLocalidad = d;
	}

	public double getGananciaSinRecargos() {
		return gananciaSinRecargos;
	}

	public void setGananciasinRecargos(double d) {
		this.gananciaSinRecargos = d;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
    /** Devuelve los eventos organizados por este usuario. */
	public ArrayList<Evento> getEventos(){
		return this.eventosOrganizados;
	}
	
	/** Establece la lista de eventos organizados. */
    public void setEventosOrganizados(ArrayList<Evento> eventosOrganizados) { 
    	this.eventosOrganizados = eventosOrganizados; 
    	}

    /** Devuelve los reportes financieros generados por el organizador. */
    public ArrayList<ReporteFinanciero> getReportesGenerados() { return reportesGenerados; }
    
    /** Reemplaza la lista de reportes financieros. */
    public void setReportesGenerados(ArrayList<ReporteFinanciero> reportesGenerados) {
        this.reportesGenerados = reportesGenerados;
    }
    
    // Métodos funcionales de eventos
    
    /**
     * Crea un nuevo evento organizado por este usuario.
     * @param nombre Nombre del evento.
     * @param id Identificación del Evento
     * @param venue Lugar donde se realizará.
     * @param fecha Fecha y hora de inicio.
     * @param tipoEvento Categoría del evento.
     * @return El evento recién creado.
     * @pre venue está aprobado y disponible; fecha es futura.
     * @post El evento queda creado con estado “pendiente de aprobación”.
     */
    public Evento crearEvento(String nombre, int id, Venue venue, LocalDateTime fecha, String tipoEvento, Administrador admin) throws IllegalArgumentException {
        if (venue == null || !venue.estaAprobado()) {
            throw new IllegalArgumentException("El venue no está aprobado o es inválido.");
        }
        if (fecha.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento debe ser futura.");
        }

        Evento nuevoEvento = new Evento(nombre, id, venue, LocalDateTime.now(), this, tipoEvento, admin);
        eventosOrganizados.add(nuevoEvento);
        return nuevoEvento;
    }
    
    /**
     * Cancela un evento antes de su realización.
     * @param evento Evento a cancelar.
     * @param motivo Texto explicando la causa de la cancelación.
     * @pre El evento pertenece al organizador y no ha iniciado.
     * @post El evento queda marcado como cancelado y se notifican los reembolsos.
     */
    public void cancelarEvento(Evento evento, String motivo) {
        if (evento == null || !eventosOrganizados.contains(evento))
            throw new IllegalArgumentException("Evento inválido o no pertenece al organizador.");
        evento.cancelarPorOrganizador(motivo);
        // TODO: Registrar notificación y coordinar reembolsos con el administrador.
    }
    
    /**
     * Modifica los datos básicos de un evento.
     * @param evento Evento a modificar.
     * @param nuevoNombre Nuevo nombre.
     * @param nuevaFecha Nueva fecha y hora.
     * @pre El evento pertenece al organizador y no tiene ventas activas.
     * @post Los atributos del evento son actualizados.
     */
    public void modificarEvento(Evento evento, String nuevoNombre, int id, LocalDateTime nuevaFecha) {
        if (evento == null || !eventosOrganizados.contains(evento))
            throw new IllegalArgumentException("Evento inválido.");
        evento.modificarDatos(nuevoNombre, id, nuevaFecha);
    }

    
    // Métodos de Localidades
    
    /**
     * Crea una nueva localidad dentro de un evento.
     * @param evento Evento en el cual se define la localidad.
     * @param nombre Nombre de la localidad.
     * @param numerada Indica si los asientos son numerados.
     * @param capacidad Número de asientos disponibles.
     * @param precioBase Precio base por tiquete.
     * @return Localidad creada.
     * @pre El evento pertenece al organizador.
     * @post La localidad queda registrada en el evento.
     */
    public Localidad crearLocalidad(Evento evento, String nombre, boolean numerada,
                                    int capacidad, double precioBase, double precioBasePaquetesxUnidad, double precioPaseTemporadaxUnidad, double precioPaseDeluxexUnidad) {
        if (evento == null || !eventosOrganizados.contains(evento))
            throw new IllegalArgumentException("Evento inválido o no pertenece al organizador.");
        return evento.crearLocalidad(nombre, numerada, capacidad,  precioBase,  precioBasePaquetesxUnidad,  precioPaseTemporadaxUnidad,  precioPaseDeluxexUnidad);
    }
    
    
    /**
     * Modifica una localidad existente.
     * @param evento Evento que contiene la localidad.
     * @param localidad Localidad a modificar.
     * @param nuevoPrecio Nuevo precio base.
     * @pre La localidad pertenece al evento.
     * @post Se actualiza el precio base.
     */
    public void modificarLocalidad(Evento evento, Localidad localidad, double nuevoPrecio) {
        if (evento == null || localidad == null)
            throw new IllegalArgumentException("Datos inválidos.");
        localidad.setPrecioBase(nuevoPrecio);
    }
    
    // Métodos de Ofertas y Cortesías


    /**
     * Crea una oferta asociada a un evento.
     * @param evento Evento destino.
     * @param descripcion Descripción de la oferta.
     * @param porcentajeDescuento Porcentaje de descuento (0–100).
     * @param fechaInicio Fecha de inicio de la oferta.
     * @param fechaFin Fecha de finalización.
     * @return La oferta creada.
     * @pre El evento pertenece al organizador y las fechas son válidas.
     * @post La oferta queda activa en el evento.
     */
    public Oferta crearOferta(Evento evento, double porcentajeDescuento,
                              LocalDateTime fechaInicio, LocalDateTime fechaFin, Localidad localidad) {
        if (evento == null || !eventosOrganizados.contains(evento))
            throw new IllegalArgumentException("Evento inválido.");
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100)
            throw new IllegalArgumentException("Descuento inválido.");
        return evento.crearOferta(porcentajeDescuento, fechaInicio, fechaFin, localidad);
    }
    
    /**
     * Registra una cortesía (tiquete gratuito) en un evento.
     * @param evento Evento asociado.
     * @param cliente Cliente que recibe la cortesía.
     * @param localidad Localidad de la cortesía.
     * @pre El evento pertenece al organizador y la localidad tiene disponibilidad.
     * @post El cliente recibe un tiquete con valor $0.
     */
    public void registrarCortesia(Evento evento, Cliente cliente, Localidad localidad) {
        if (evento == null || cliente == null || localidad == null)
            throw new IllegalArgumentException("Datos inválidos para cortesía.");
        evento.entregarCortesia(cliente, localidad);
    }
    
    // Métodos de Reportes
    
    /**
     * Genera un reporte financiero de un evento organizado.
     * @param id Id del Reporte Financiero
     * @param evento Evento objetivo.
     * @return Reporte financiero generado.
     * @pre El evento pertenece al organizador.
     * @post Se crea y almacena un reporte con los ingresos totales y número de tiquetes vendidos.
     */
    public ReporteFinanciero generarReporteFinanciero(Evento evento) {
        if (evento == null || !eventosOrganizados.contains(evento))
            throw new IllegalArgumentException("Evento inválido o no pertenece al organizador.");

        ReporteFinanciero reporte = new ReporteFinanciero(id, LocalDateTime.now(), LocalDateTime.now());
        reportesGenerados.add(reporte);
        return reporte;
    }

    /**
     * Lista todos los reportes financieros generados por el organizador.
     * @return Lista de reportes.
     */
    public List<ReporteFinanciero> listarReportesFinancieros() {
        return new ArrayList<>(reportesGenerados);
    }
}