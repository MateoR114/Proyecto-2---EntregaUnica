package Tiquetes;
import Eventos.*;
import Usuarios.Cliente;

/**
 * Representa un tiquete individual comprado para un evento específico.
 * Es una implementación concreta de la clase abstracta {@link Tiquete}.
 */
public class Individual extends Tiquete{
	// Atributos
	private int numeroAsiento; // -1 Es que no es numerado. Rango:(1 - capacidad es numero de asiento por localidad.)

	// Ctor

    /**
     * Crea un nuevo tiquete individual.
     *
     * @param codigo Código único del tiquete.
     * @param precio Precio pagado por el cliente.
     * @param evento Evento al que pertenece el tiquete.
     * @param localidad Localidad asignada dentro del evento.
     * @param dueno Cliente que es dueno del tiquete.
     * @pre id no vacío; precio ≥ 0; evento, localidad y Cliente definidos.
     * @post Se crea un tiquete individual sin usar ni reembolsar.
     */
	public Individual(int codigo, double precio, Evento evento, Localidad localidad, Cliente dueno, boolean transferible, int numeroAsiento) {
		super(codigo, precio, evento, localidad, dueno, transferible);
		this.numeroAsiento = numeroAsiento;
	}
	
	// Getters y Setters

    /**
     * Obtiene código único de cada tiquete
     * 
     * @return código del tiquete
     */
    public int getCodigo(){
        return id;
    }

    /**
     * Fija el codigo único del tiquete
     * @param codigo Identificador numérico único del tiquete emitido
     * @pre Identificador distinto de otro tiquete
     * @post Se actualiza el codigo de identificación del tiquete
     */
    public void setCodigo(int codigo) {
        this.id=codigo;
    }
	
	/**
     * Obtiene el número de asiento del tiquete.
     *
     * @return número de asiento.
     */
	public int getNumeroAsiento() {
		return numeroAsiento;
	}

	/**
     * Fija el número de asiento del tiquete.
     *
     * @param numeroAsiento Nuevo número de asiento a asignar.
     * @pre número de asiento mayor a -1 y no vacío.
     * @post Se actualiza el número de asiento del tiquete.
     */
	public void setNumeroAsiento(int numeroAsiento) {
		this.numeroAsiento = numeroAsiento;
	}

    /**
     * Obtiene el precio total del tiquete individual.
     * 
     * @return precio del tiquete
     */
    public double getPrecioPagado() {
        return precio;
    }

    /**
     * Fija el precio pagado por un tiquete individual
     * 
     * @param double Precio pagado por el tiquete individual
     * @pre Valor del tiquete es mayor que 0 y está compuesto por el precio base, cargo por servicio y cuota de emisión
     */
    public void setPrecioPagado(double precio) {
        this.precio = precio;
    }

    /** 
     * Obtiene el nombre del comprador del tiquete
     * 
     * @return Nombre del usuario comprador
     */
    public Cliente getComprador() {
        return dueno;
    }

    /**
     * Define el comprador del tiquete 
     * @param Cliente comprador del tiquete
     * @pre Cliente existe y tiene una cuenta en la plataforma
     * @post Se actualiza el estado del tiquete y el dueño
     */
    public void setComprador(Cliente cliente) {
        this.dueno = cliente;
    }
	
	// Métodos funcionales
	
	/**
     * Usa el tiquete para permitir el ingreso al evento.
     *
     * @pre El tiquete no ha sido usado ni reembolsado y el evento está activo.
     * @post El tiquete queda marcado como usado.
     * @throws IllegalStateException si el tiquete ya fue usado o reembolsado.
     */
    public void usar() throws IllegalStateException {
        if (usado) {
            throw new IllegalStateException("El tiquete ya fue utilizado.");
        }
        if (reembolsado) {
            throw new IllegalStateException("El tiquete fue reembolsado y no puede usarse.");
        }
        usado = true;
    }

    /**
     * Solicita el reembolso del tiquete individual.
     *
     * @return Monto devuelto al cliente.
     * @pre El tiquete no ha sido usado ni reembolsado y el evento permite reembolsos.
     * @post El tiquete queda marcado como reembolsado.
     * @throws IllegalStateException si el tiquete ya fue usado o reembolsado, o el evento no acepta reembolsos.
     */
    public void reembolsar() {
        if (usado)
            throw new IllegalStateException("No se puede reembolsar un tiquete ya usado.");
        if (reembolsado)
            throw new IllegalStateException("El tiquete ya fue reembolsado.");

        reembolsado = true;
    }




	
}