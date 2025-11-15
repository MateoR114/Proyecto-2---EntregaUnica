package Interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;

import MarketPlace.marketPlace;
import Tiquetes.Compra;
import Tiquetes.PaqueteTiquetes;
import Tiquetes.Tiquete;
import Tiquetes.Individual;

import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;

public class ConsolaCliente implements InterfazCliente {

    private Cliente cliente;

    public ConsolaCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void usarSaldoReembolso(double monto) {
        cliente.usarSaldoDeReembolso(monto);
    }

    @Override
    public void hacerCompra(ArrayList<Tiquete> tiquetes, ArrayList<PaqueteTiquetes> paquetes,
                            boolean usarSaldoReembolso) {
        cliente.hacerCompra(tiquetes, paquetes, usarSaldoReembolso);
    }

    @Override
    public void registrarCompra(Compra compra) {
        cliente.registrarCompra(compra);
    }

    @Override
    public void verTiquetesComprados() {
        System.out.println(cliente.obtenerTiquetesComprados());
    }

    @Override
    public void verPaquetesComprados() {
        System.out.println(cliente.obtenerPaquetesComprados());
    }

    @Override
    public void transferirTiquete(Tiquete tiquete, Cliente destino, boolean credencial) {
        cliente.transferirTiquete(tiquete, destino, credencial);
    }

    @Override
    public void transferirPaquete(PaqueteTiquetes paquete, Cliente destino, boolean credencial) {
        cliente.transferirPaqueteCompleto(paquete, destino, credencial);
    }

    @Override
    public void solicitarReembolsoTiquete(Tiquete tiquete, Administrador admin) {
        cliente.solicitarReembolsoPorCalamidadTiquete(tiquete, admin);
    }

    @Override
    public void solicitarReembolsoPaquete(PaqueteTiquetes paquete, Administrador admin) {
        cliente.solicitarReembolsoPorCalamidadPaquete(paquete, "", admin);
    }

    @Override
    public void precompraDeluxe(int codigo, ArrayList<Evento> eventos, ArrayList<Localidad> localidades,
                                String beneficios, ArrayList<Tiquete> tiquetes, ArrayList<Integer> numerosAsientos) {
        cliente.precompraDeluxe(codigo, eventos, localidades, beneficios, tiquetes, numerosAsientos);
    }

    @Override
    public void precompraIndividualNoNumerada(int codigo, Evento evento, Localidad localidad,
                                              boolean usarSaldoReembolso) {
        cliente.precompraIndividualNoNumerada(codigo, evento, localidad, usarSaldoReembolso);
    }

    @Override
    public void precompraIndividualNumerada(int codigo, Evento evento, Localidad localidad, int numeroAsiento) {
        cliente.precompraIndividualNumerada(codigo, evento, localidad, numeroAsiento);
    }

    @Override
    public void precompraPaqueteMultiple(int codigo, Evento evento, Localidad localidad, int cantidad,
                                         boolean usarSaldoReembolso, ArrayList<Integer> numerosAsientos) {
        cliente.precompraPaqueteMultipleMismoEvento(codigo, evento, localidad, cantidad, usarSaldoReembolso, numerosAsientos);
    }

    @Override
    public void precompraPaseTemporada(int codigo, ArrayList<Evento> eventos, ArrayList<Localidad> localidades,
                                       boolean usarSaldoReembolso, ArrayList<Integer> numerosAsientos) {
        cliente.precompraPaseTemporada(codigo, eventos, localidades, usarSaldoReembolso, numerosAsientos);
    }

    @Override
    public void iniciar() {
        System.out.println("Iniciando consola de cliente...");
    }

    @Override
    public void publicarOfertaReventa(int id, ArrayList<Tiquete> tiquetes, double precio) {
        cliente.crearOfertaMP(id, tiquetes, precio, null);
    }

    @Override
    public void eliminarOferta(int idUsuario, int idOferta) {
        marketPlace.getInstance().getActivas().removeIf(o -> o.getIdOferta() == idOferta);
    }

    @Override
    public void comprarTiquetesMarketplace(int idComprador, int idOferta, int cantidad, String medioPago) {
        System.out.println("Función no implementada en el modelo.");
    }

    @Override
    public void contraofertaMarketplace(int idComprador, int idOfertaOriginal, double precioPropuesto, int cantidad) {
        System.out.println("Función no implementada en el modelo.");
    }

    @Override
    public void gestionarContraoferta(int idVendedor, int idContraoferta, boolean decision) {
        System.out.println("Función no implementada en el modelo.");
    }

    @Override
    public void consultarContraofertas(int idUsuario) {
        System.out.println("Función no implementada.");
    }

    @Override
    public void salir() {
        System.out.println("Cerrando consola de cliente...");
    }

    // ============================================================
    //                       MAIN PARA PRUEBAS
    // ============================================================
    public static void main(String[] args) {

        // Crear administrador
        Administrador admin = new Administrador("admin@mail.com", "123", "Admin", 1);

        // Crear organizador
        Organizador org = new Organizador("org@mail.com", "abc", "Organizador", 10, 0.0, "Org");

        // Crear cliente
        Cliente cliente = new Cliente("cli@mail.com", "000", "Cliente", 3);

        ConsolaCliente c = new ConsolaCliente(cliente);
        c.iniciar();

        // Venue aprobado
        Venue venue = new Venue(1, "Estadio", "Bogotá", 30000, "");
        venue.setAprobado(true);

        // Evento
        Evento evento = new Evento("Evento Test", 100, venue, LocalDateTime.now().plusDays(2), org, "Concierto", admin);

        // Localidad
        Localidad loc = new Localidad("VIP", true, 100000, 120.5, evento, 100, 0, 500);

        // Precompras
        Individual t1 = cliente.precompraIndividualNumerada(1, evento, loc, 10);
        Individual t2 = cliente.precompraIndividualNumerada(2, evento, loc, 11);

        ArrayList<Tiquete> listaT = new ArrayList<>();
        listaT.add(t1);
        listaT.add(t2);

        ArrayList<PaqueteTiquetes> listaP = new ArrayList<>();

        // Compra
        Compra compra = cliente.hacerCompra(listaT, listaP, false);
        System.out.println("Compra total: " + compra.getvalorCompra());

        // Solicitar reembolso
        cliente.solicitarReembolsoPorCalamidadTiquete(t1, admin);

        // Publicar oferta MP
        c.publicarOfertaReventa(99, listaT, 50000);

        // Ver compras
        c.verTiquetesComprados();

        c.salir();
    }
}
