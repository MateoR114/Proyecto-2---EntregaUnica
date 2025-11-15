package Eventos;
import java.util.*;

/**
 * Representa un lugar físico (venue) donde se pueden realizar eventos en el sistema BoletaMaster.
 * Un venue debe ser aprobado por un administrador antes de ser usado por un organizador.
 */
public class Venue{
	// Atributos
	private int id;
	private String nombre;
	private String ubicacion;
	private int capacidad;
	private String restricciones;
	private boolean aprobado;
    private ArrayList<Localidad> localidades;
	
	//Ctor
	/**
     * Crea un nuevo venue.
     *
     * @param id Identificador del venue.
     * @param nombre Nombre del venue.
     * @param ubciación Dirección física del lugar.
     * @param capacidad Capacidad máxima total del venue.
     * @param restricciones Restricciones del venue.
     * @pre nombre y dirección no vacíos; capacidadTotal > 0.
     * @post Se crea un venue sin aprobar y sin localidades asignadas.
     */
	public Venue(int id, String nombre, String ubicacion, int capacidad, String restricciones) {
		this.id = id;
		this.nombre = nombre;
		this.ubicacion = ubicacion;
		this.capacidad = capacidad;
		this.restricciones = restricciones;
		this.localidades = new ArrayList<Localidad>();
	}

	// Getters y Setters
	
    /**
     * Obtiene el id del venue.
     *
     * @return id actual del venue.
     */
	public int getId() {
		return id;
	}

    /**
     * Actualiza el id del venue.
     *
     * @param id Nuevo id a asignar.
     */
	public void setId(int id) {
		this.id = id;
	}

    /**
     * Obtiene las restricciones del venue.
     *
     * @return restricciones actuales del venue.
     */
	public String getRestricciones() {
		return restricciones;
	}
	
    /**
     * Actualiza las restricciones del venue.
     *
     * @param restricciones Nuevas restricciones a asignar.
     */
	public void setRestricciones(String restricciones) {
		this.restricciones = restricciones;
	}

    /**
     * Obtiene el nombre del venue.
     *
     * @return Nombre actual del venue.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el nombre del venue.
     *
     * @param nombre Nuevo nombre a asignar.
     * @pre nombre no vacío.
     * @post Se actualiza el nombre del venue.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la dirección del venue.
     *
     * @return Dirección física actual.
     */
    public String getUbicación() {
        return this.ubicacion;
    }

    /**
     * Actualiza la dirección del venue.
     *
     * @param ubicacion Nueva dirección física.
     * @pre ubicacion no vacía.
     * @post Se actualiza la dirección del venue.
     */
    public void setDireccion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la capacidad máxima total del venue.
     *
     * @return Capacidad total.
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Establece una nueva capacidad total para el venue.
     *
     * @param capacidad Nueva capacidad máxima.
     * @pre capacidad > 0.
     * @post Se actualiza la capacidad total del venue.
     */
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Verifica si el venue está aprobado para uso en eventos.
     *
     * @return {@code true} si está aprobado; {@code false} en caso contrario.
     */
    public boolean estaAprobado() {
        return aprobado;
    }

    /**
     * Define si el venue está aprobado o no.
     *
     * @param aprobado Valor booleano que indica el estado de aprobación.
     * @post Se actualiza el estado de aprobación del venue.
     */
    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    /**
     * Obtiene la lista de localidades del venue.
     *
     * @return Lista actual de localidades.
     */
    public ArrayList<Localidad> getLocalidades() {
        return localidades;
    }

    /**
     * Reemplaza la lista de localidades del venue.
     *
     * @param localidades Nueva lista de localidades.
     * @pre La lista no es nula y cada localidad pertenece a este venue.
     * @post Se actualiza la lista completa de localidades asociadas al venue.
     */
    public void setLocalidades(ArrayList<Localidad> localidades) {
        this.localidades = localidades;
    }

    // Métodos funcionales

    /**
     * Agrega una localidad al venue.
     *
     * @param localidad Localidad a agregar.
     * @pre localidad no nula y no existente previamente en el venue.
     * @post La localidad queda registrada en la lista de localidades del venue.
     */
    public void agregarLocalidad(Localidad localidad) throws IllegalArgumentException{
        if (localidad == null) {
            throw new IllegalArgumentException("La localidad no puede ser nula.");
        }
        if (localidades.contains(localidad)) {
            throw new IllegalArgumentException("La localidad ya está registrada en este venue.");
        }
        if(localidad.getCapacidad() + this.calcularCapacidadTotal() > this.capacidad) {
        	throw new IllegalArgumentException("La capacidad del venue supera su valor máximo con la nueva localidad.");
        }
        localidades.add(localidad);
    }

    /**
     * Elimina una localidad del venue.
     *
     * @param localidad Localidad a eliminar.
     * @pre La localidad pertenece a este venue.
     * @post La localidad se elimina de la lista de localidades asociadas.
     */
    public void eliminarLocalidad(Localidad localidad)throws IllegalArgumentException {
    	if (!localidades.contains(localidad)) {
    		throw new IllegalArgumentException("La localidad indicada no existe.");
    	}
        localidades.remove(localidad);
    }

    /**
     * Calcula la capacidad total del venue sumando las capacidades de sus localidades.
     *
     * @return Suma de las capacidades de todas las localidades.
     * @post No modifica el estado del objeto.
     */
    public int calcularCapacidadTotal() {
        int total = 0;
        for (Localidad l : localidades) {
            total += l.getCapacidad();
        }
        return total;
    }

    /**
     * Verifica si el venue tiene al menos una localidad registrada.
     *
     * @return {@code true} si el venue tiene localidades; {@code false} en caso contrario.
     * @post No modifica el estado del venue.
     */
    public boolean tieneLocalidades() {
        return !localidades.isEmpty();
    }

}