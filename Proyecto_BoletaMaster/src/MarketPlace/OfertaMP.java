package MarketPlace;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Tiquetes.Tiquete;
import Usuarios.Cliente;

/**
 * Representa una oferta individual dentro del Marketplace. 
 * Modela las boletas ofertadas, su precio base, las pujas hechas por los compradores 
 * y el estado actual de la oferta.
 */
public class OfertaMP {

    // Atributos
    private int idOferta;
    private Cliente vendedor;
    private ArrayList<Tiquete> boletas;
    private double precioBase;
    private String estado;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaCierre;
    private ArrayList<Puja> pujas;

    /**
     * @pre vendedor != null ; boletas != null ; pujas != null
     * @post La oferta queda creada con los atributos asignados
     * @param idOferta identificador único
     * @param vendedor dueño de las boletas
     * @param boletas lista de tiquetes a vender
     * @param precioBase precio base inicial
     * @param estado estado inicial de la oferta
     * @param fechaPublicacion momento de publicación
     * @param fechaCierre momento de cierre de pujas (puede ser null)
     * @param pujas lista inicial de pujas
     */
    public OfertaMP(int idOferta, Cliente vendedor, ArrayList<Tiquete> boletas, double precioBase, 
                    String estado, LocalDateTime fechaPublicacion, LocalDateTime fechaCierre, 
                    ArrayList<Puja> pujas) {
        this.idOferta = idOferta;
        this.vendedor = vendedor;
        this.boletas = boletas;
        this.precioBase = precioBase;
        this.estado = estado;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaCierre = fechaCierre;
        this.pujas = pujas;
    }

    /**
     * @pre true
     * @post retorna el id de la oferta
     * @return idOferta
     */
    public int getIdOferta() {
        return idOferta;
    }

    /**
     * @pre idOferta > 0
     * @post this.idOferta = idOferta
     * @param idOferta nuevo id
     */
    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    /**
     * @pre true
     * @post retorna el vendedor
     * @return vendedor
     */
    public Cliente getVendedor() {
        return vendedor;
    }

    /**
     * @pre vendedor != null
     * @post this.vendedor = vendedor
     * @param vendedor nuevo vendedor
     */
    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * @pre true
     * @post retorna lista de boletas
     * @return boletas
     */
    public ArrayList<Tiquete> getBoletas() {
        return boletas;
    }

    /**
     * @pre boletas != null
     * @post this.boletas = boletas
     * @param boletas nueva lista de boletas
     */
    public void setBoletas(ArrayList<Tiquete> boletas) {
        this.boletas = boletas;
    }

    /**
     * @pre true
     * @post retorna precio base
     * @return precioBase
     */
    public double getPrecioBase() {
        return precioBase;
    }

    /**
     * @pre precioBase >= 0
     * @post this.precioBase = precioBase
     * @param precioBase nuevo precio
     */
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    /**
     * @pre true
     * @post retorna estado actual
     * @return estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @pre estado != null
     * @post this.estado = estado
     * @param estado nuevo estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @pre true
     * @post retorna la fecha de publicación
     * @return fechaPublicacion
     */
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * @pre fechaPublicacion != null
     * @post this.fechaPublicacion = fechaPublicacion
     * @param fechaPublicacion nueva fecha
     */
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * @pre true
     * @post retorna la fecha de cierre (puede ser null)
     * @return fechaCierre
     */
    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    /**
     * @pre true
     * @post this.fechaCierre = fechaCierre
     * @param fechaCierre nueva fecha
     */
    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    /**
     * @pre true
     * @post retorna lista de pujas
     * @return pujas
     */
    public ArrayList<Puja> getPujas() {
        return pujas;
    }

    /**
     * @pre p != null
     * @post this.pujas = p
     * @param p nueva lista de pujas
     */
    public void setPujas(ArrayList<Puja> p) {
        this.pujas = p;
    }
}
