package Tiquetes;
import Eventos.*;
import Usuarios.*;

/**
 * Clase abstracta que representa un tiquete en el sistema BoletaMaster.
 * Define la información y el comportamiento común a todos los tipos de tiquetes.
 */
public abstract class Tiquete{
	
	
	protected double precio;
	protected double cargoServicio;
	protected double cuotaImpresion;
	protected int id;
	protected boolean transferible;
	protected boolean reembolsado;
	protected Evento evento;
	protected Localidad localidad;
	protected Cliente dueno;
	protected boolean usado;
	protected boolean impreso;  
	
	//Ctor
	
	/**
     * Crea un tiquete asociado a un evento y localidad.
     *
     * @param id Identificador del tiquete.
     * @param precio  Precio base del Tiquete.
     * @param cargoServicio  cargo del servicio en el Tiquete.
     * @param cuotaImpresión  cuota de impresión del Tiquete.
     * @param evento Evento al que pertenece el tiquete.
     * @param localidad Localidad del evento donde aplica el tiquete.
     * @param dueno Usuario que adquiere el tiquete.
     * @param transferible Indica si el tiquete es o no Transferible.
     * @pre id no vacío; precio ≥ 0; evento, localidad y dueño definidos.
     * @post Se crea un tiquete sin usar ni reembolsar.
     */
	public Tiquete(int id, double precio, Evento evento, Localidad localidad, Cliente dueno, boolean transferible) {
		this.precio = precio;
		this.cargoServicio = precio*(1 + evento.getCuotaEvento());
		this.cuotaImpresion = evento.getAdmin().getCuotaEmisionGlobal();
		this.id = id;
		this.evento = evento;
		this.localidad = localidad;
		this.transferible = transferible;
		this.reembolsado = false;
		this.usado = false;
		this.dueno = dueno;
		this.impreso = false;  
	}

	// Getters y Setters
	

    /**
     * Obtiene el precio pagado por el tiquete.
     *
     * @return Precio final pagado.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Actualiza el precio pagado por el tiquete.
     *
     * @param precioPagado Nuevo precio.
     * @pre precioPagado ≥ 0.
     * @post Se actualiza el valor del precio pagado.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el cargo del servicio.
     *
     * @return Valor cargo del servicio.
     */
    public double getCargoServicio() {
        return this.cargoServicio;
    }

    /**
     * Actualiza el cargo de servicio.
     *
     * @param cargoServicio Nuevo cargo.
     * @pre cargoServicio ≥ 0.
     * @post Se actualiza el valor del cargo de servicio.
     */
    public void setCargoServicio(double cargoServicio) {
        this.cargoServicio = cargoServicio;
    }
    
    /**
     * Obtiene la cuota de impresión por el tiquete.
     *
     * @return Precio de la cuota de impresión.
     */
    public double getCuotaImpresion() {
        return this.cuotaImpresion;
    }

    /**
     * Actualiza la cuota de impresión por el tiquete.
     *
     * @param cuotaImpresion Nueva cuota de impresion.
     * @pre cuotaImpresion ≥ 0.
     * @post Se actualiza el valor de la cuota de impresion.
     */
    public void setCuotaImpresion(double cuotaImpresion) {
        this.cuotaImpresion = cuotaImpresion;
    }
    
    /**
     * Verifica si el tiquete ya fue utilizado.
     *
     * @return {@code true} si fue usado, {@code false} si no.
     */
    public boolean isUsado() {
        return usado;
    }

    /**
     * Marca el tiquete como usado o no usado.
     *
     * @param usado Valor booleano que indica si el tiquete fue usado.
     * @post Actualiza el estado de uso del tiquete.
     */
    public void setUsado(boolean usado) {
        this.usado = usado;
    }
    
    /**
     * Verifica si el tiquete es transferible.
     *
     * @return {@code true} si es, {@code false} si no.
     */
    public boolean isTransferible() {
        return transferible;
    }

    /**
     * Marca el tiquete como transferible o no.
     *
     * @param transferible Valor booleano que indica si el tiquete es transferible.
     * @post Actualiza el estado de transferencia del tiquete.
     */
    public void setTransferible(boolean transferible) {
        this.transferible = transferible;
    }

    /**
     * Verifica si el tiquete fue reembolsado.
     *
     * @return {@code true} si el tiquete fue reembolsado.
     */
    public boolean isReembolsado() {
        return reembolsado;
    }

    /**
     * Marca el tiquete como reembolsado o no.
     *
     * @param reembolsado Valor booleano que indica si el tiquete fue reembolsado.
     * @post Actualiza el estado de reembolso del tiquete.
     */
    public void setReembolsado(boolean reembolsado) {
        this.reembolsado = reembolsado;
    }

    /**
     * Indica si el tiquete ya fue impreso al menos una vez.
     *
     * @return {@code true} si ya fue impreso; {@code false} en caso contrario.
     */
    public boolean isImpreso() {
        return impreso;
    }

    /**
     * Marca el tiquete como impreso. 
     * Si ya estaba impreso, no permite volver a imprimirlo.
     *
     * @pre El tiquete no ha sido impreso previamente.
     * @post El tiquete queda marcado como impreso.
     * @throws IllegalStateException si el tiquete ya estaba impreso.
     */
    public void registrarImpresion() throws IllegalStateException {
        if (impreso) {
            throw new IllegalStateException("El tiquete ya fue impreso y no puede volver a imprimirse.");
        }
        this.impreso = true;
    }

    /**
     * Permite actualizar directamente el estado de impresión.
     * Se recomienda usar registrarImpresion() para aplicar la regla de negocio.
     */
    public void setImpreso(boolean impreso) {
        this.impreso = impreso;
    }

    /**
     * Obtiene la localidad asociada al tiquete.
     *
     * @return Localidad del evento.
     */
    public Localidad getLocalidad() {
        return localidad;
    }

    /**
     * Asigna la localidad del tiquete.
     *
     * @param localidad Nueva localidad asociada.
     * @pre localidad definida.
     * @post Se actualiza la localidad asociada al tiquete.
     */
    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene el evento al que pertenece el tiquete.
     *
     * @return Evento asociado.
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Asigna un nuevo evento al tiquete.
     *
     * @param evento Evento a asociar.
     * @pre evento definido.
     * @post Se actualiza la asociación del evento al tiquete.
     */
    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    /**
     * Obtiene el dueño del tiquete.
     *
     * @return Cliente que es dueño del tiquete.
     */
    public Cliente getDueno() {
        return dueno;
    }
    
    /**
     * Calcula el costo total del tiquete incluyendo cargos adicionales.
     *
     * @return Valor total resultante de la suma de precio, cargo de servicio y cuota de impresión.
     * @post No modifica el estado del tiquete.
     */
    public double calcularCostoTotal() {
        return precio + cargoServicio + cuotaImpresion;
    }
    /**
     * Asigna el dueño del tiquete.
     *
     * @param dueno Cliente que es dueño del tiquete.
     * @pre Cliente definido.
     * @post Se actualiza el dueño asociado.
     */
    public void setDueno(Cliente dueno) {
        this.dueno = dueno;
    }
	
}
