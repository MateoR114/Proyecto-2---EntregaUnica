package Usuarios;
import java.time.*;
import Eventos.*;

/**
 * Representa un reporte financiero asociado a un evento del sistema BoletaMaster.
 * Permite calcular los ingresos, costos, comisiones y beneficios totales
 * obtenidos en las ventas de tiquetes de un evento.
 */
public class ReporteFinanciero {
     
    private int id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int ventasEvento;
    private double porcentajeOcupacion;

    /**
     * Crea un reporte financiero para un rango de fechas.
     * @param id Identificador único del reporte.
     * @param fechaInicio Fecha y hora inicial del reporte.
     * @param fechaFin Fecha y hora final del reporte.
     * @pre fechaInicio != null
     * @pre fechaFin != null
     * @pre !fechaFin.isBefore(fechaInicio)
     * @post this.id == id
     * @post this.fechaInicio.equals(fechaInicio)
     * @post this.fechaFin.equals(fechaFin)
     */
    public ReporteFinanciero(int id, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeOcupacion = 0;
    }


    public int getId() {
        return id;
    }

    public void setId(int codigo) {
        this.id = codigo;
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

    /**
     * Actualiza el total de tiquetes vendidos a partir de un evento.
     * @param evento Evento del cual se obtendrán las ventas.
     * @pre evento != null
     * @post this.ventasEvento == evento.calcularTotalTiquetesVendidos()
     */
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
