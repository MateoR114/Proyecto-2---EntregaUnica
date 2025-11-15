package Interfaces;

import java.time.LocalDateTime;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;
import Usuarios.Administrador;
import Usuarios.Cliente;

public interface InterfazOrganizador {

    /**
     * @post Se muestran los eventos creados por el organizador
     */
    public void verEventosCreados();

    /**
     * @pre nombre != "" ; venue != null ; fecha != null ; tipoEvento != "" ; admin != null ; id > 0
     * @post Se crea un nuevo evento asociado al organizador
     * @param nombre nombre del evento
     * @param id identificador único
     * @param venue venue donde se realizará
     * @param fecha fecha del evento
     * @param tipoEvento tipo del evento
     * @param admin administrador asociado
     */
    public void crearEvento(String nombre, int id, Venue venue, LocalDateTime fecha, String tipoEvento, Administrador admin);

    /**
     * @post Se muestran los reportes financieros del organizador
     */
    public void verReportesFinancieros();

    /**
     * @pre evento != null
     * @post Se genera un reporte financiero asociado al evento
     * @param evento evento objetivo
     */
    public void crearReporteFinanciero(Evento evento);

    /**
     * @pre evento != null ; motivo != ""
     * @post El evento queda cancelado con registro del motivo
     * @param evento evento a cancelar
     * @param motivo motivo de cancelación
     */
    public void cancelarEvento(Evento evento, String motivo);

    /**
     * @pre evento != null ; nuevoNombre != "" ; id > 0 ; nuevaFecha != null
     * @post El evento queda modificado con los nuevos datos
     * @param evento evento original
     * @param nuevoNombre nuevo nombre
     * @param id nuevo identificador
     * @param nuevaFecha nueva fecha
     */
    public void modificarEvento(Evento evento, String nuevoNombre, int id, LocalDateTime nuevaFecha);

    /**
     * @pre evento != null ; nombre != "" ; capacidad > 0 ;
     *      precioBase >= 0 ; precioBasePaquetesxUnidad >= 0 ;
     *      precioPaseTemporadaxUnidad >= 0 ; precioPaseDeluxexUnidad >= 0
     * @post Se crea una nueva localidad asociada al evento
     */
    public void crearLocalidad(Evento evento, String nombre, boolean numerada, int capacidad, double precioBase,
                               double precioBasePaquetesxUnidad, double precioPaseTemporadaxUnidad, double precioPaseDeluxexUnidad);

    /**
     * @pre evento != null ; localidad != null ; nuevoPrecio >= 0
     * @post El precio de la localidad queda actualizado
     */
    public void modificarLocalidad(Evento evento, Localidad localidad, double nuevoPrecio);

    /**
     * @pre evento != null ; porcentajeDescuento >= 0 ; fechaInicio != null ;
     *      fechaFin != null ; localidad != null
     * @post Se registra una oferta de descuento en el evento
     */
    public void crearOferta(Evento evento, double porcentajeDescuento, LocalDateTime fechaInicio,
                            LocalDateTime fechaFin, Localidad localidad);

    /**
     * @pre evento != null ; cliente != null ; localidad != null
     * @post Se registra la cortesía para el cliente en la localidad indicada
     */
    public void registrarCortesia(Evento evento, Cliente cliente, Localidad localidad);

    /**
     * @post La interfaz del organizador inicia su ejecución
     */
    public void iniciar();

    /**
     * @post La interfaz se cierra correctamente
     */
    public void salir();    
}
