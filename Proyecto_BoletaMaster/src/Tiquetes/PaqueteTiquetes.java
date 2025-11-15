package Tiquetes;
import java.util.*;
import Usuarios.*;

/**
 * Clase abstracta que representa un paquete de tiquetes en el sistema BoletaMaster.
 * Agrupa múltiples tiquetes individuales que comparten un mismo comprador y condiciones comunes.
 * Sirve como clase base para {@link PaqueteMultiple}, {@link PaseTemporada} y {@link PaqueteDeluxe}.
 */
public abstract class PaqueteTiquetes{
	// Atributos 
    protected int id;
	protected double precio;
	protected double cargoServicio;
	protected double cuotaImpresion;
	protected Cliente dueno;
	protected ArrayList<Tiquete> tiquetesIncluidos;	
	protected boolean isTransferible;
	protected String estado;
	//Ctor
	
    /**
     * Crea un nuevo paquete de tiquetes.
     *
     * @param id              Identificador único del paquete.
     * @param precio          Precio total pagado por el conjunto de tiquetes.
     * @param cargoServicio   Cargo adicional por servicio aplicado al paquete.
     * @param cuotaImpresion  Cuota de impresión de los tiquetes.
     * @param dueno           Cliente propietario del paquete.
     * @pre precio ≥ 0; dueno definido.
     * @post Se crea un paquete vacío, sin tiquetes individuales asignados.
     */
	public PaqueteTiquetes(int id,double precio, Cliente dueno, ArrayList<Tiquete> tiquetesIncluidos) {
		this.precio = precio;
		this.cargoServicio = precio*(1 + tiquetesIncluidos.getFirst().getCargoServicio());
		this.cuotaImpresion = (tiquetesIncluidos.getFirst().evento.getAdmin().getCuotaEmisionGlobal()) * tiquetesIncluidos.size();
		this.dueno = dueno;
		this.tiquetesIncluidos = tiquetesIncluidos;
		estado = "Activo";
		this.isTransferible = true;
	}
	
	// Setters y Getters
	
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
     * Obtiene el precio total del paquete.
     *
     * @return Precio actual del paquete.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio total del paquete.
     *
     * @param precio Nuevo precio total.
     * @pre precio ≥ 0.
     * @post Se actualiza el precio del paquete.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el cargo por servicio aplicado al paquete.
     *
     * @return Cargo de servicio actual.
     */
    public double getCargoServicio() {
        return cargoServicio;
    }

    /**
     * Establece el cargo por servicio del paquete.
     *
     * @param cargoServicio Nuevo valor del cargo de servicio.
     * @pre cargoServicio ≥ 0.
     * @post Se actualiza el cargo de servicio.
     */
    public void setCargoServicio(double cargoServicio) {
        this.cargoServicio = cargoServicio;
    }

    /**
     * Obtiene la cuota de impresión del paquete.
     *
     * @return Cuota de impresión actual.
     */
    public double getCuotaImpresion() {
        return cuotaImpresion;
    }

    /**
     * Establece la cuota de impresión del paquete.
     *
     * @param cuotaImpresion Nuevo valor de la cuota de impresión.
     * @pre cuotaImpresion ≥ 0.
     * @post Se actualiza la cuota de impresión del paquete.
     */
    public void setCuotaImpresion(double cuotaImpresion) {
        this.cuotaImpresion = cuotaImpresion;
    }

    /**
     * Obtiene el cliente propietario del paquete.
     *
     * @return Cliente dueño del paquete.
     */
    public Cliente getDueno() {
        return dueno;
    }

    /**
     * Establece el propietario del paquete.
     *
     * @param dueno Nuevo cliente propietario.
     * @pre dueno definido.
     * @post Se actualiza el dueño del paquete.
     */
    public void setDueno(Cliente dueno) {
        this.dueno = dueno;
    }
    
    /**
     * Obtiene el estado del paquete.
     *
     * @return Estado del paquete.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el propietario del paquete.
     *
     * @param dueno Nuevo cliente propietario.
     * @pre dueno definido.
     * @post Se actualiza el dueño del paquete.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la lista de tiquetes incluidos en el paquete.
     *
     * @return Lista de tiquetes asociados.
     */
    public ArrayList<Tiquete> getTiquetesIncluidos() {
        return tiquetesIncluidos;
    }

    /**
     * Reemplaza la lista de tiquetes incluidos en el paquete.
     *
     * @param tiquetesIncluidos Nueva lista de tiquetes.
     * @pre Lista no nula y todos los tiquetes pertenecen al mismo dueño.
     * @post Se actualiza la lista de tiquetes contenidos en el paquete.
     */
    public void setTiquetesIncluidos(ArrayList<Tiquete> tiquetesIncluidos) {
        this.tiquetesIncluidos = tiquetesIncluidos;
    }
    
    public boolean isTransferible() {
    	return this.isTransferible;
    }
    
    public void setTransferible(boolean transferible) {
    	this.isTransferible = transferible;
    }

    // Métodos funcionales

    /**
     * Agrega un tiquete individual al paquete.
     *
     * @param tiquete Tiquete a agregar.
     * @pre tiquete no nulo; pertenece al mismo dueño del paquete.
     * @post El tiquete se agrega a la lista de tiquetes del paquete.
     * @throws IllegalArgumentException si el tiquete es nulo o de otro dueño.
     */
    public void agregarTiquete(Tiquete tiquete) {
        if (tiquete == null)
            throw new IllegalArgumentException("El tiquete no puede ser nulo.");
        if (tiquete.getDueno() != dueno)
            throw new IllegalArgumentException("El tiquete debe pertenecer al mismo dueño del paquete.");
        tiquetesIncluidos.add(tiquete);
    }

    /**
     * Elimina un tiquete del paquete.
     *
     * @param tiquete Tiquete a eliminar.
     * @pre El tiquete está contenido en el paquete.
     * @post El tiquete se elimina de la lista de tiquetes incluidos.
     */
    public void eliminarTiquete(Tiquete tiquete) {
        tiquetesIncluidos.remove(tiquete);
    }

    /**
     * Calcula el costo total del paquete incluyendo cargos adicionales.
     *
     * @return Valor total resultante de la suma de precio, cargo de servicio y cuota de impresión.
     * @post No modifica el estado del paquete.
     */
    public double calcularCostoTotal() {
        return precio + cargoServicio + cuotaImpresion;
    }

    /**
     * Calcula la cantidad de tiquetes contenidos en el paquete.
     *
     * @return Número total de tiquetes incluidos.
     * @post No modifica el estado del paquete.
     */
    public int contarTiquetes() {
        return tiquetesIncluidos.size();
    }

    /**
     * Verifica si el paquete está vacío (sin tiquetes).
     *
     * @return {@code true} si no contiene tiquetes; {@code false} en caso contrario.
     * @post No modifica el estado del paquete.
     */
    public boolean estaVacio() {
        return tiquetesIncluidos.isEmpty();
    }

    /**
     * Calcula el valor promedio de los tiquetes incluidos.
     *
     * @return Valor promedio de los tiquetes; 0 si el paquete está vacío.
     * @post No modifica el estado del paquete.
     */
    public double calcularValorPromedio() {
        if (tiquetesIncluidos.isEmpty())
            return 0;
        int total = 0;
        for (Tiquete t : tiquetesIncluidos) {
            total += t.getPrecio();
        }
        return (double) total / tiquetesIncluidos.size();
    }

	
	
}