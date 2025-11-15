package Tiquetes;
import java.util.*;
import Eventos.Evento;
import Usuarios.Cliente;

/**
 * Representa un pase de temporada dentro del sistema BoletaMaster.
 * Un pase de temporada permite al cliente acceder a múltiples eventos válidos
 * dentro de un mismo período, agrupando varios tiquetes asociados.
 */
public class PaseTemporada extends PaqueteTiquetes{
	// Atributos
	private ArrayList<Evento> eventosValidos;

	// Ctor

    /**
     * Crea un pase de temporada.
     *
     * @param id              Identificador del pase.
     * @param dueno     Cliente propietario.
     * @param precioPaquete   Precio base del pase.
     * @param cargoServicio   Cargo de servicio del pase.
     * @param cuotaImpresion  Valor de emisión o impresión del pase.
     * @param eventosValidos  Lista de eventos a los que aplica el pase.
     * @param tiquetes        Lista de tiquetes que componen el pase.
     * @pre Parámetros válidos análogos a los del paquete base; la lista de eventos está definida
     *      (puede estar vacía según la política).
     * @post El pase de temporada queda creado con los eventos válidos asociados.
     */
    public PaseTemporada(int id, Cliente dueno, double precioPaquete, ArrayList<Evento> eventosValidos, ArrayList<Tiquete> tiquetes) {
        super(id, precioPaquete, dueno, tiquetes);
        this.eventosValidos = eventosValidos ;
        this.tiquetesIncluidos = tiquetes;
    }

	// Getters y Setters
	public ArrayList<Evento> getEventosValidos() {
		return eventosValidos;
	}
	
	public void setEventosValidos(ArrayList<Evento> eventosValidos) {
		this.eventosValidos = eventosValidos;
	}

}