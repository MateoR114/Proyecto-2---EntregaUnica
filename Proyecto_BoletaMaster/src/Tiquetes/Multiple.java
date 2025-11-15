package Tiquetes;

import java.util.*;
import Usuarios.Cliente;

/**
 * Representa un paquete múltiple dentro del sistema BoletaMaster.
 * Un paquete múltiple agrupa varios tiquetes adquiridos por un mismo cliente,
 * pudiendo corresponder a distintos eventos o localidades.
 */
public class Multiple extends PaqueteTiquetes {

    // Atributos
    // ==========================
    private int cantidad;

    // ==========================
    // Constructor
    // ==========================

    /**
     * Crea un paquete múltiple.
     *
     * @param id              Identificador del paquete.
     * @param propietario     Cliente propietario del paquete.
     * @param precioPaquete   Precio base total del paquete.
     * @param cargoServicio   Cargo de servicio aplicado al paquete.
     * @param cuotaImpresion  Valor de emisión o impresión del paquete.
     * @param tiquetes        Lista de tiquetes que componen el paquete.
     * @pre Parámetros válidos análogos a los del paquete base:
     *      id > 0, propietario definido, precioPaquete ≥ 0, cargoServicio ≥ 0,
     *      cuotaImpresion ≥ 0, lista de tiquetes no nula.
     * @post El paquete múltiple queda creado con la lista de tiquetes asociada.
     */
    public Multiple(int id, Cliente dueno, double precioPaquete, ArrayList<Tiquete> tiquetes) {
        super(id, precioPaquete, dueno, tiquetes);
        this.cantidad = tiquetes.size();
    }

    // Métodos funcionales

    /**
     * Obtiene la cantidad total de tiquetes del paquete.
     *
     * @return Cantidad de tiquetes contenidos en el paquete.
     * @pre Ninguna.
     * @post No cambia el estado del objeto.
     */
    public int getCantidad() {
        return cantidad;
    }
    
}
