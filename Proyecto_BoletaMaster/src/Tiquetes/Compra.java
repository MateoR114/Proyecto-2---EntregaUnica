package Tiquetes;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Usuarios.Cliente;

/**
 * Representa un registro de compra dentro del sistema BoletaMaster.
 * Cada compra puede incluir uno o varios tiquetes o paquetes de tiquetes.
 */
public class Compra {

    // Atributos
	private Cliente dueno;
    private int idCompra;
    private LocalDateTime fechaCompra;
    private double valorCompra;
    private ArrayList<Tiquete> tiquetesComprados;
    private ArrayList<PaqueteTiquetes> paquetesComprados;
    private static int idActual = 0;

    // Ctor
    /**
     * Crea un registro de compra de Tiquetes.
     * @param idCompra Identificador de la compra.
     * @param fechaCompra Fecha y hora en que se realiza la compra.
     * @pre idCompra > 0; fechaCompra está definida.
     * @post La compra queda creada en estado pendiente o confirmada según la implementación,
     *       con valor total en cero y sin ítems iniciales.
     */
    public Compra(Cliente dueno, ArrayList<Tiquete> tiquetesComprados, ArrayList<PaqueteTiquetes> paquetesComprados, double valorCompra, LocalDateTime fechaCompra) {
        this.dueno = dueno;
    	this.idCompra = Compra.idActual+1;
        this.fechaCompra = fechaCompra;
        this.valorCompra = valorCompra;
        this.tiquetesComprados = tiquetesComprados;
        this.paquetesComprados = paquetesComprados;
    }
    

    // Setters y Getters
    /**
     * Obtiene el identificador de la compra.
     * @return Identificador único de la compra.
     */
    public int getIdCompra() {
        return idCompra;
    }

    /**
     * Establece el identificador de la compra.
     * @param idCompra Nuevo identificador.
     */
    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    /**
     * Obtiene la fecha de la compra.
     * @return Fecha y hora de la compra.
     */
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Actualiza la fecha de la compra.
     * @param fechaCompra Nueva fecha y hora.
     */
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /**
     * Obtiene el valor total de la compra.
     * @return Valor total actual.
     */
    public double getvalorCompra() {
        return valorCompra;
    }

    /**
     * Establece el valor total de la compra.
     * @param valorTotal Nuevo valor total.
     */
    public void setvalorCompral(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    /**
     * Obtiene los tiquetes incluidos en la compra.
     * @return Lista de tiquetes.
     */
    public ArrayList<Tiquete> getTiquetesComprados() {
        return tiquetesComprados;
    }

    /**
     * Reemplaza la lista de tiquetes.
     * @param tiquetes Nueva lista de tiquetes.
     */
    public void setTiquetesComprados(ArrayList<Tiquete> tiquetes) {
        this.tiquetesComprados = tiquetes;
    }

    /**
     * Obtiene los paquetes incluidos en la compra.
     * @return Lista de paquetes.
     */
    public ArrayList<PaqueteTiquetes> getPaquetes() {
        return paquetesComprados;
    }

    /**
     * Reemplaza la lista de paquetes.
     * @param paquetes Nueva lista de paquetes.
     */
    public void setPaquetesComprados(ArrayList<PaqueteTiquetes> paquetes) {
        this.paquetesComprados = paquetes;
    }
    
    public Cliente getDueno() {
    	return dueno;
    }
    
    public void setDueno(Cliente dueno) {
    	this.dueno = dueno;
    }


    // Métodos funcionales

    /**
     * Agrega un tiquete a la compra.
     * @param tiquete Tiquete que se agrega.
     * @pre El tiquete está definido y no se encuentra ya entre los tiquetes de la compra.
     * @post El tiquete queda incluido en la compra y podrá considerarse para el cálculo del valor total.
     */
    public void agregarTiquete(Tiquete tiquete) throws IllegalArgumentException {
        if (tiquete == null) {
        	throw new IllegalArgumentException("El tiquete no puede ser nulo.");
        }
        if (!tiquetesComprados.contains(tiquete)) {
            tiquetesComprados.add(tiquete);
        }
    }

    /**
     * Agrega un paquete a la compra.
     * @param paquete Paquete que se agrega.
     * @pre El paquete está definido y no se encuentra ya entre los paquetes de la compra.
     * @post El paquete queda incluido en la compra.
     */
    public void agregarPaquete(PaqueteTiquetes paquete) throws IllegalArgumentException {
        if (paquete == null) {
        	throw new IllegalArgumentException("El paquete no puede ser nulo.");
        }
        if (!paquetesComprados.contains(paquete)) {
            paquetesComprados.add(paquete);
        }
    }

    /**
     * Elimina un tiquete de la compra.
     * @param tiquete Tiquete que se elimina.
     * @pre El tiquete está definido y forma parte de los tiquetes de la compra.
     * @post El tiquete deja de figurar entre los ítems de la compra.
     */
    public void eliminarTiquete(Tiquete tiquete) {
        tiquetesComprados.remove(tiquete);
    }

    /**
     * Elimina un paquete de la compra.
     * @param paquete Paquete que se elimina.
     * @pre El paquete está definido y forma parte de los paquetes de la compra.
     * @post El paquete deja de figurar entre los ítems de la compra.
     */
    public void eliminarPaquete(PaqueteTiquetes paquete) {
        paquetesComprados.remove(paquete);
    }

}
