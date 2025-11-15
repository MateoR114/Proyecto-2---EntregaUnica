package Interfaces;

import java.util.ArrayList;

import Eventos.Evento;
import Eventos.Localidad;
import Tiquetes.Compra;
import Tiquetes.PaqueteTiquetes;
import Tiquetes.Tiquete;
import Usuarios.Administrador;
import Usuarios.Cliente;


public interface InterfazCliente {

    //Métodos planteados inicialmente del documento de diseño
    public void usarSaldoReembolso(double monto);

    public void hacerCompra(ArrayList<Tiquete> tiquetes, ArrayList<PaqueteTiquetes> paquetes, boolean usarSaldoReembolso);

    public void registrarCompra(Compra compra);

    public void verTiquetesComprados();

    public void verPaquetesComprados();

    public void transferirTiquete(Tiquete tiquete, Cliente destino, boolean credencial);

    public void transferirPaquete(PaqueteTiquetes paquete, Cliente destino, boolean credencial);

    public void solicitarReembolsoTiquete(Tiquete tiquete, Administrador admin);

    public void solicitarReembolsoPaquete(PaqueteTiquetes paquete, Administrador admin);

    public void precompraDeluxe(int codigo, ArrayList<Evento> eventos, ArrayList<Localidad> localidades, String beneficios, ArrayList<Tiquete> tiquetes, ArrayList<Integer> numerosAsientos);

    public void precompraIndividualNoNumerada(int codigo, Evento evento, Localidad localidad, boolean usarSaldoReembolso);

    public void precompraIndividualNumerada(int codigo, Evento evento, Localidad localidad, int numeroAsiento);

    public void precompraPaqueteMultiple(int codigo, Evento evento, Localidad localidad, int cantidad, boolean usarSaldoReembolso, ArrayList<Integer> numerosAsientos);

    public void precompraPaseTemporada(int codigo, ArrayList<Evento> eventos, ArrayList<Localidad> localidades, boolean usarSaldoReembolso, ArrayList<Integer> numerosAsientos);

    //Métodos adicionales para inicio de aplicación y el Marketplace
    public void iniciar();

    /*
     * Un cliente publica una oferta de reventa para uno o varios tiquetes vigentes que posee, indicando el precio al que desea venderlos.
     * @param id Id del usuario que publica la oferta
     * @param tiquetes Lista de tiquetes a revender
     * @param precio Precio de venta del / los tiquetes y/o paquete a vender
     */
    public void publicarOfertaReventa(int id, ArrayList<Tiquete> tiquetes, double precio);


    /*
     * Un cliente elimina una oferta de reventa que había publicado, de modo que ya no sea visible ni esté disponible para compra
     * @param idUsuario Id del usuario que elimina la oferta
     * @param idOferta Id de la oferta que se va a eliminar
     */
    public void eliminarOferta(int idUsuario, int idOferta);

    /*
     * Un cliente compra tiquetes asociados a una oferta activa del Marketplace, usando saldo virtual o pasarela de pago, y los tiquetes pasan a ser de su propiedad
     * @param idComprador Id del usuario comprador
     * @param idOferta Id de la oferta publicada en el Marketplace
     * @param cantidad Cantidad de tiquetes o pauetes a comprar (depende el caso)
     * @param medioPago Método de pago que puede ser por el saldo del usuario o la pasarela de pago tercerizada
     * Tiquetes comprados no pueden ser parte de un paquete Deluxe y no pueden pertenecer al usuario que los está vendiendo
     */
    public void comprarTiquetesMarketplace(int idComprador, int idOferta, int cantidad, String medioPago);

    /*
     * Un cliente propone una contraoferta con un nuevo precio sobre una oferta de reventa existente, quedando en espera de respuesta del vendedor
     * @param idComprador Id del usuario comprador
     * @param int idOfertaOriginal Id de la oferta original publicada en el Marketplace
     * @param precioPropuesto Precio propuesto por el usuario comprador 
     * @param cantidad Cantidad de tiquetes o pauetes a comprar (depende el caso)
     * Precio de la puja debe ser mayor al precio de la oferta o al precio de la última puja publicada.
     * 
     */
    public void contraofertaMarketplace(int idComprador, int idOfertaOriginal, double precioPropuesto, int cantidad);

    /*
     * El vendedor de la oferta responde a una contraoferta, pudiendo aceptarla para concretar la venta o rechazarla para mantener su oferta original.
     * @param idVendedor Id del usuario vendedor (quien publica la oferta)
     * @param idContraoferta Id de la contraoferta propuesta por un usuario interesado (Usuario comprador)
     * @param decision Acptación o negación de la contraoferta por parte del usuario vendedor
     */
    public void gestionarContraoferta(int idVendedor, int idContraoferta, boolean decision);

    /*
     * El cliente consulta un resumen de sus ofertas de reventa y contraofertas relacionadas, ya sea como comprador o como vendedor
     * @param idUsuario Id del usuarioq ue desea consultar las ofertas y contraofertas presentes en el Marketplace
     */
    public void consultarContraofertas(int idUsuario);

    public void salir();
}