package Tiquetes;

import java.util.ArrayList;
import Usuarios.Cliente;

/**
 * Representa un paquete Deluxe dentro del sistema BoletaMaster.
 * Este tipo de paquete ofrece beneficios adicionales y, por política del sistema,
 * no es transferible a otros clientes.
 */
public class Deluxe extends PaqueteTiquetes {

    // Atributos
    private String beneficios;

    // ==========================
    // Constructor
    // ==========================

    /**
     * Crea un paquete deluxe.
     *
     * @param id              Identificador del paquete.
     * @param propietario     Cliente propietario.
     * @param precioPaquete   Precio base del paquete.
     * @param cargoServicio   Cargo de servicio del paquete.
     * @param cuotaImpresion  Valor de emisión o impresión del paquete.
     * @param beneficios      Descripción de beneficios adicionales.
     * @param tiquetes        Lista de tiquetes que componen el paquete.
     * @pre Parámetros válidos análogos a los del paquete base;
     *      la descripción de beneficios es un texto no vacío.
     * @post El paquete deluxe queda creado y, por política, no es transferible.
     */
    public Deluxe(int id, Cliente dueno, double precioPaquete, String beneficios, ArrayList<Tiquete> tiquetes) throws IllegalArgumentException {
        super(id, precioPaquete, dueno, tiquetes);

        if (beneficios == null || beneficios.isBlank())
            throw new IllegalArgumentException("La descripción de beneficios no puede estar vacía.");

        this.beneficios = beneficios;
        this.tiquetesIncluidos = (tiquetes != null) ? new ArrayList<>(tiquetes) : new ArrayList<>();
    }

    // Métodos

    /**
     * Obtiene la descripción de beneficios.
     *
     * @return Texto con los beneficios del paquete.
     * @pre Ninguna.
     * @post No cambia el estado del objeto.
     */
    public String getBeneficios() {
        return beneficios;
    }

    /**
     * Actualiza la descripción de beneficios.
     *
     * @param beneficios Nuevo texto de beneficios.
     * @pre beneficios es un texto no vacío.
     * @post El paquete queda con la nueva descripción de beneficios.
     */
    public void setBeneficios(String beneficios) {
        if (beneficios == null || beneficios.isBlank())
            throw new IllegalArgumentException("El texto de beneficios no puede estar vacío.");
        this.beneficios = beneficios;
    }

    /**
     * Indica si el paquete deluxe es transferible.
     *
     * @return Siempre {@code false} por política del sistema.
     * @pre Ninguna.
     * @post No cambia el estado del objeto.
     */
    public boolean esTransferible() {
        return false;
    }
}