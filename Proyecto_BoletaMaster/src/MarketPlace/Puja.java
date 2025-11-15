package MarketPlace;

import Usuarios.Cliente;
import java.time.LocalDateTime;

/**
 * Representa una puja de dinero hecha por un comprador sobre una oferta del Marketplace.
 */
public class Puja {

    // Atributos
    private int idPuja;
    private OfertaMP oferta;
    private Cliente comprador;
    private double monto;
    private String estado; // Pendiente, Aceptada, Rechazada, Superada
    private LocalDateTime fechaHora;

    /**
     * @pre oferta != null ; comprador != null ; monto > 0 ; estado != null ; fechaHora != null
     * @post La puja queda creada con todos sus atributos asignados
     * @param idPuja identificador de la puja
     * @param oferta oferta asociada
     * @param comprador cliente que realiza la puja
     * @param monto valor ofrecido
     * @param estado estado inicial de la puja
     * @param fechaHora fecha y hora de creación
     */
    public Puja(int idPuja, OfertaMP oferta, Cliente comprador, double monto, 
                String estado, LocalDateTime fechaHora) {
        this.idPuja = idPuja;
        this.oferta = oferta;
        this.comprador = comprador;
        this.monto = monto;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    /**
     * @pre true
     * @post retorna el id de la puja
     * @return idPuja
     */
    public int getIdPuja() {
        return idPuja;
    }

    /**
     * @pre idPuja > 0
     * @post this.idPuja = idPuja
     * @param idPuja nuevo id
     */
    public void setIdPuja(int idPuja) {
        this.idPuja = idPuja;
    }

    /**
     * @pre true
     * @post retorna la oferta asociada
     * @return oferta
     */
    public OfertaMP getOferta() {
        return oferta;
    }

    /**
     * @pre oferta != null
     * @post this.oferta = oferta
     * @param oferta nueva oferta asociada
     */
    public void setOferta(OfertaMP oferta) {
        this.oferta = oferta;
    }

    /**
     * @pre true
     * @post retorna el comprador
     * @return comprador
     */
    public Cliente getComprador() {
        return comprador;
    }

    /**
     * @pre comprador != null
     * @post this.comprador = comprador
     * @param comprador nuevo comprador
     */
    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    /**
     * @pre true
     * @post retorna el monto de la puja
     * @return monto
     */
    public double getMonto() {
        return monto;
    }

    /**
     * @pre monto > 0
     * @post this.monto = monto
     * @param monto nuevo valor ofrecido
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * @pre true
     * @post retorna el estado actual de la puja
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
     * @post retorna fecha y hora de creación
     * @return fechaHora
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * @pre fechaHora != null
     * @post this.fechaHora = fechaHora
     * @param fechaHora nueva fecha
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
