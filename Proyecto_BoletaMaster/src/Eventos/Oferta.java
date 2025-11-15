package Eventos;
import java.time.*;

/**
 * Representa una oferta o promoción asociada a una localidad en el sistema BoletaMaster.
 * Cada oferta tiene un porcentaje de descuento, un periodo de vigencia y una descripción.
 */
public class Oferta{
	
	// Atributos
	
	private double porcentajeDescuento;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Localidad localidad;
	
	//Ctor
	
    /**
     * Crea una nueva oferta para una localidad.
     *
     * @param porcentajeDescuento Porcentaje de descuento (0–100).
     * @param fechaInicio Fecha de inicio de la oferta.
     * @param fechaFin Fecha de finalización de la oferta.
     * @param localidad Localidad asociada a la oferta.
     * @pre descripcion no vacía; 0 ≤ porcentajeDescuento ≤ 100; fechaInicio < fechaFin; localidad definida.
     * @post Se crea una oferta activa dentro del periodo especificado.
     */
	public Oferta(double porcentajeDescuento, LocalDateTime fechaInicio, LocalDateTime fechaFin, Localidad localidad) {
		this.porcentajeDescuento = porcentajeDescuento;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.localidad = localidad;
	}

	// Getters y Setters
	/**
     * Obtiene el porcentaje de descuento de la oferta.
     *
     * @return Porcentaje de descuento aplicado (entre 0 y 100).
     */
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    /**
     * Actualiza el porcentaje de descuento de la oferta.
     *
     * @param porcentajeDescuento Nuevo porcentaje de descuento.
     * @pre 0 ≤ porcentajeDescuento ≤ 100.
     * @post Se actualiza el valor del descuento de la oferta.
     */
    public void setPorcentajeDescuento(double porcentajeDescuento) throws IllegalArgumentException{
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100.");
        }
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Obtiene la fecha de inicio de la oferta.
     *
     * @return Fecha y hora en que la oferta comienza a ser válida.
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio de la oferta.
     *
     * @param fechaInicio Nueva fecha de inicio.
     * @pre fechaInicio < fechaFin.
     * @post Se actualiza la fecha de inicio de la oferta.
     */
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha de finalización de la oferta.
     *
     * @return Fecha y hora en que la oferta deja de ser válida.
     */
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha de finalización de la oferta.
     *
     * @param fechaFin Nueva fecha de fin.
     * @pre fechaInicio < fechaFin.
     * @post Se actualiza la fecha final de la oferta.
     */
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene la localidad asociada a la oferta.
     *
     * @return Localidad a la cual pertenece esta oferta.
     */
    public Localidad getLocalidad() {
        return localidad;
    }

    /**
     * Asocia la oferta a una localidad.
     *
     * @param localidad Localidad destino.
     * @pre localidad definida.
     * @post La oferta queda asociada a la localidad indicada.
     */
    public void setEvento(Localidad localidad) {
        this.localidad = localidad;
    }

    // Métodos funcionales

    /**
     * Verifica si la oferta está activa en una fecha específica.
     *
     * @param fecha Fecha a verificar.
     * @return {@code true} si la fecha está dentro del rango de validez; {@code false} en caso contrario.
     * @pre fecha definida.
     * @post No modifica el estado de la oferta.
     */
    public boolean estaActiva(LocalDateTime fecha) {
        return (fecha.isAfter(fechaInicio) || fecha.isEqual(fechaInicio)) &&
               (fecha.isBefore(fechaFin) || fecha.isEqual(fechaFin));
    }

    /**
     * Aplica el descuento de la oferta sobre un precio base.
     *
     * @param precioBase Precio original sin descuento.
     * @return Precio final con el descuento aplicado.
     * @pre precioBase >= 0.
     * @post No modifica el estado del objeto; devuelve el valor calculado.
     */
    public double aplicarDescuento(double precioBase) throws IllegalArgumentException{
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo.");
        }
        double descuento = precioBase * (porcentajeDescuento / 100);
        return precioBase - descuento;
    }

}