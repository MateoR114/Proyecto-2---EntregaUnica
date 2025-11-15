package Interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Eventos.Evento;
import Eventos.Venue;
import Tiquetes.PaqueteTiquetes;
import Tiquetes.Tiquete;
import Usuarios.Cliente;
import Usuarios.Usuario;

public interface InterfazAdministrador {

    //Métodos planteados inicialmente del documento de diseño
    public void aprobarVenue(Venue venue);

    public void rechazarVenue(Venue venue);

    public void cancelarEvento(Evento evento);

    public void establecerCuotaEmisionGlobal(double cuota);

    public void establecerCargoServicio(String tipoEvento, double porcentaje);

    public void crearSolicitudReembolsoPaquete(PaqueteTiquetes paquete, Cliente cliente);

    public void crearSolicitudReembolsoTiquete(Tiquete tiquete, Cliente cliente);

    public void aprobarReembolsoTiquete(Cliente cliente, Tiquete tiquete, String motivo, String credencialAdmin);

    public void rechazarReembolsoTiquete(Cliente cliente, Tiquete tiquete, String motivo, String credencialAdmin);

    public void aprobarReembolsoPaquete(Cliente cliente, PaqueteTiquetes paquete, String motivo, String credencialAdmin);

    public void rechazarReembolsoPaquete(Cliente cliente, PaqueteTiquetes paquete, String motivo, String credencialAdmin);

    //Métodos adicionales para inicio de aplicación y el Marketplace
    public void iniciar();

    /*
     * El administrador consulta el log completo de actividades del Marketplace de reventa, con filtros por fecha, tipo de operación y usuario.
     * @param idAdmin Id del usuario (administrador) que consulta el log
     * @param fechaInicial Fecha inicio desde la cual se desea empezar a buscar los registros del log 
     * @param fechaFinal Fecha final desde la cual se desea terminar la búsqueda de los registros del log 
     * @param tipoRegistro Tipo de evento o accion registrada en el log (oferta,contraoferta, rechazo, venta,borrado etc)
     * @param idUsuario Id del usuario involucrado en el registro realizado
     */
    public void consultarLog(int idAdmin, LocalDateTime fechaInicial, LocalDateTime fechaFinal, String tipoRegistro, int idUsuario);

    /*
     * El administrador elimina una oferta de reventa que considere indebida, dejando traza de la acción en en log
     * @param credencialAdmin Credencial del administrador
     * @param idOferta Id de la oferta a eliminar
     * @param motivoEliminacion Motivo por el cual se está eliminando la oferta del Marketplace
     */
    public void eliminarOferta(String credencialAdmin, int idOferta, String motivoEliminacion);

    /*
     * El sistema registra de forma automática en un log cada oferta, contraoferta, rechazo, compra o borrado que ocurra en el Marketplace.
     * @param tipoEvento Tipo de evento que se desea registrar en el Marketplace
     * @param usuarios Lista de usuarios de los cuales se va a realizar registro
     * @param fechaHora Fecha y hora en la cual se realizó el registro en el Markketplace
     * @param precios Lista de precios que se están manejando en el momento en el Marketplace
     */
    public void registrarEventosMarketplace(String tipoEvento, ArrayList<Usuario> usuarios, LocalDateTime fechaHora, ArrayList<Double> precios);

    /*
     * Cada vez que se concreta una venta en el Marketplace, el sistema acredita el valor al saldo virtual del vendedor, permitiendo su uso en futuras compras
     * @param idVendedor Id del vendedor que está ejecutando una venta en el Marketplace 
     * @param tiquetesVendidos Tiquetes que se están vendiendo, presentes en el Marketplace
     * @param precioTotal Precio final de toda la oferta completa
     * @param fechaHora Fecha y hora en la cual se está ejecutando la ventas
     */
    public void acreditarSaldoPorVentas(int idVendedor, ArrayList<Tiquete> tiquetesVendidos, double precioTotal, LocalDateTime fechaHora);



    public void salir();
}