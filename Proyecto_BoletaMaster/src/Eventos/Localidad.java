package Eventos;
import java.util.*;
/**
 * Representa una localidad dentro de un evento.
 * Puede ser numerada o no numerada, y tiene su propio precio base, capacidad y control de ventas.
 */
public class Localidad{
	
	//private int id;
	private String nombre;
	private double precioBase;
	private double precioBasePaquetesxUnidad;
	private double precioPaseTemporadaxUnidad;
	private double precioPaseDeluxexUnidad;
	private int capacidad;
	private boolean numerada;
    private int tiquetesVendidos;
    private Evento evento;
    private ArrayList<Integer> asientosOcupados;
	
    /**
     * Crea una localidad asociada a un evento.
     * @param id Identificador de Localidad.
     * @param nombre Nombre de la localidad.
     * @param numerada Indica si la localidad tiene asientos numerados.
     * @param capacidad Capacidad total de la localidad.
     * @param precioBase Precio base por tiquete.
     * @param evento Evento al que pertenece.
     * @pre nombre no vacío; capacidad > 0; precioBase >= 0; evento definido.
     * @post La localidad queda asociada al evento con tiquetes disponibles.
     */
	public Localidad( String nombre, boolean numerada, int capacidad, double precioBase, Evento evento, double precioBasePaquetesxUnidad, double precioPaseTemporadaxUnidad, double precioPaseDeluxexUnidad) {

		//this.id = id;
        this.nombre = nombre;
        this.numerada = numerada;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.precioBasePaquetesxUnidad = precioBasePaquetesxUnidad;
        this.precioPaseTemporadaxUnidad = precioPaseTemporadaxUnidad;
        this.precioPaseDeluxexUnidad = precioPaseDeluxexUnidad;
        this.evento = evento;
        this.tiquetesVendidos = 0;
        this.asientosOcupados = new ArrayList<Integer>();
	}

	
	//Getters y Setters
	
	public double getPrecioBasePaquetesxUnidad() {
		return this.precioBasePaquetesxUnidad;
	}
	public double getPrecioPaseTemporadaxUnidad() {
		return this.precioPaseTemporadaxUnidad;
	}
	public double getPrecioPaseDeluxexUnidad() {
		return this.precioPaseDeluxexUnidad;
	}

    /**
     * Obtiene el nombre de la localidad.
     *
     * @return Nombre actual de la localidad.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre de la localidad.
     *
     * @param nombre Nuevo nombre a asignar.
     * @pre nombre no vacío.
     * @post Se actualiza el nombre de la localidad.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Indica si la localidad es numerada.
     *
     * @return Verdadero si la localidad tiene asientos numerados.
     */
    public boolean isNumerada() {
        return numerada;
    }

    /**
     * Establece si la localidad debe ser numerada o no.
     *
     * @param numerada Valor booleano que indica si los asientos son numerados.
     * @post Se actualiza el atributo numerada.
     */
    public void setNumerada(boolean numerada) {
        this.numerada = numerada;
    }

    /**
     * Obtiene la capacidad total de la localidad.
     *
     * @return Número de asientos o cupos disponibles originalmente.
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Establece la capacidad total de la localidad.
     *
     * @param capacidad Nueva capacidad total.
     * @pre capacidad > 0.
     * @post Se actualiza la capacidad de la localidad.
     */
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Obtiene el precio base por tiquete en esta localidad.
     *
     * @return Precio base actual.
     */
    public double getPrecioBase() {
        return precioBase;
    }

    /**
     * Actualiza el precio base de los tiquetes en esta localidad.
     *
     * @param precioBase Nuevo precio base.
     * @pre precioBase >= 0.
     * @post El precio base de la localidad se actualiza.
     */
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    /**
     * Obtiene el número de tiquetes vendidos hasta el momento.
     *
     * @return Número total de tiquetes vendidos.
     */
    public int getTiquetesVendidos() {
        return tiquetesVendidos;
    }

    /**
     * Obtiene el evento al cual pertenece la localidad.
     *
     * @return Evento asociado.
     */
    public Evento getEvento() {
        return evento;
    }
	

    // Métodos funcionales
    /**
     * Verifica si hay disponibilidad de tiquetes.
     *
     * @return {@code true} si existen cupos o asientos disponibles, {@code false} en caso contrario.
     * @post No modifica el estado de la localidad.
     */
    public boolean hayDisponibilidad() {
        return tiquetesVendidos < capacidad;
    }

    /**
     * Verifica si un asiento específico está disponible.
     *
     * @param numeroAsiento Número del asiento a verificar.
     * @return {@code true} si el asiento está libre.
     * @pre La localidad debe ser numerada.
     * @post No modifica el estado de la localidad.
     */
    public boolean asientoDisponible(int numeroAsiento) throws IllegalStateException{
        if (!numerada) {
            throw new IllegalStateException("La localidad no es numerada.");
        }
        return !asientosOcupados.contains(numeroAsiento);
    }

    /**
     * Reserva un asiento específico en una localidad numerada.
     *
     * @param numeroAsiento Número del asiento a reservar.
     * @pre La localidad es numerada y el asiento está disponible.
     * @post El asiento queda marcado como ocupado y aumenta el contador de tiquetes vendidos.
     */
    public void reservarAsiento(int numeroAsiento) throws IllegalStateException{
        if (!numerada) {
            throw new IllegalStateException("No aplica para localidades no numeradas.");
        }
        if (!asientoDisponible(numeroAsiento)) {
            throw new IllegalStateException("El asiento ya está ocupado.");
        }
        asientosOcupados.add(numeroAsiento);
        tiquetesVendidos++;
    }

    /**
     * Reserva un cupo en una localidad no numerada.
     *
     * @pre La localidad no es numerada y hay disponibilidad.
     * @post Incrementa el número de tiquetes vendidos.
     */
    public void reservarAsiento()throws IllegalStateException {
        if (numerada) {
            throw new IllegalStateException("Debe indicar número de asiento para localidad numerada.");
        }
        if (!hayDisponibilidad()) {
            throw new IllegalStateException("No hay cupos disponibles.");
        }
        tiquetesVendidos++;
    }

    /**
     * Libera un asiento previamente reservado.
     *
     * @param numeroAsiento Número del asiento a liberar.
     * @pre El asiento está ocupado.
     * @post El asiento vuelve a estar disponible y se reduce el contador de tiquetes vendidos.
     */
    public void liberarAsiento(int numeroAsiento) throws IllegalStateException{
        if (!numerada) {
            throw new IllegalStateException("Solo aplica a localidades numeradas.");
        }
        asientosOcupados.remove(Integer.valueOf(numeroAsiento));
        tiquetesVendidos--;
    }

    /**
     * Cancela una reserva general en una localidad no numerada.
     *
     * @pre Hay al menos un tiquete vendido.
     * @post Se reduce el contador de tiquetes vendidos en uno.
     */
    public void cancelarReservaGeneral() throws IllegalStateException{
        if (numerada) {
            throw new IllegalStateException("No aplica a localidades numeradas.");
        }
        if (tiquetesVendidos <= 0) {
            throw new IllegalStateException("No hay reservas que cancelar.");
        }
        tiquetesVendidos--;
    }

    /**
     * Calcula el ingreso total generado por la localidad.
     *
     * @return Total de ingresos calculados como (tiquetesVendidos * precioBase).
     * @post No modifica el estado de la localidad.
     */
    public double calcularIngresos() {
        return tiquetesVendidos * precioBase;
    }

    /**
     * Calcula el porcentaje de ocupación actual de la localidad.
     *
     * @return Porcentaje de ocupación entre 0 y 100.
     * @post No modifica el estado de la localidad.
     */
    public double calcularOcupacion() {
        if (capacidad == 0) { 
        	return 0;
        }
        return (tiquetesVendidos * 100.0) / capacidad;
    }

}