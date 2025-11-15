package Usuarios;
import java.time.*;
import Eventos.*;


/**
 * Representa un reporte financiero asociado a un evento del sistema BoletaMaster.
 * Permite calcular los ingresos, costos, comisiones y beneficios totales
 * obtenidos en las ventas de tiquetes de un evento.
 */
public class ReporteFinanciero{
	// Atributos 
	private int id;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private int ventasEvento;
	private double porcentajeOcupacion;

	// Ctor
	public ReporteFinanciero(int id, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.porcentajeOcupacion = 0;
	}

	// Getters y Setters

    public int getId(){
        return id;
    }

    public void setId(int codigo) {
        this.id=codigo;
    }

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}


	public int getVentasEvento() {
		return ventasEvento;
	}

	public void setVentasEvento(Evento evento) {
		ventasEvento = evento.calcularTotalTiquetesVendidos();
	}

	public double getPorcentajeOcupacion() {
		return porcentajeOcupacion;
	}

	public void setPorcentajeOcupacion(double porcentajeOcupacion) {
		this.porcentajeOcupacion = porcentajeOcupacion;
	}

	
}