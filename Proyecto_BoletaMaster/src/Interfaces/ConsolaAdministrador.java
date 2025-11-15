package Interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Eventos.Evento;
import Eventos.Venue;

import Tiquetes.PaqueteTiquetes;
import Tiquetes.Tiquete;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Usuario;

public class ConsolaAdministrador implements InterfazAdministrador {

    private Administrador admin;

    public ConsolaAdministrador(Administrador admin) {
        this.admin = admin;
    }

  
    @Override
    public void aprobarVenue(Venue venue) {
        admin.aprobarVenue(venue);
        System.out.println("Venue aprobado.");
    }

    @Override
    public void rechazarVenue(Venue venue) {
        admin.desaprobarVenue(venue);
        System.out.println("Venue rechazado.");
    }

    @Override
    public void cancelarEvento(Evento evento) {
        admin.cancelarEvento(evento);
        System.out.println("Evento cancelado.");
    }

    @Override
    public void establecerCuotaEmisionGlobal(double cuota) {
        admin.establecerCuotaEmisionGlobal(cuota);
        System.out.println("Cuota global establecida.");
    }

    @Override
    public void establecerCargoServicio(String tipoEvento, double porcentaje) {
        admin.establecerCargoServicioPorTipo(tipoEvento, porcentaje);
        System.out.println("Cargo de servicio establecido.");
    }

    @Override
    public void crearSolicitudReembolsoPaquete(PaqueteTiquetes paquete, Cliente cliente) {
        admin.crearSolicitudReembolsoPaquete(paquete, cliente);
        System.out.println("Solicitud de reembolso de paquete creada.");
    }

    @Override
    public void crearSolicitudReembolsoTiquete(Tiquete tiquete, Cliente cliente) {
        admin.crearSolicitudReembolsoTiquete(tiquete, cliente);
        System.out.println("Solicitud de reembolso de tiquete creada.");
    }

    @Override
    public void aprobarReembolsoTiquete(Cliente cliente, Tiquete tiquete, String motivo, String credencialAdmin) {
        admin.aprobarReembolsoPorCalamidadTiquete(cliente, tiquete);
        System.out.println("Reembolso de tiquete aprobado.");
    }

    @Override
    public void rechazarReembolsoTiquete(Cliente cliente, Tiquete tiquete, String motivo, String credencialAdmin) {
        admin.rechazarReembolsoPorCalamidadTiquete(cliente);
        System.out.println("Reembolso de tiquete rechazado.");
    }

    @Override
    public void aprobarReembolsoPaquete(Cliente cliente, PaqueteTiquetes paquete, String motivo, String credencialAdmin) {
        admin.aprobarReembolsoPorCalamidadPaquete(cliente, paquete);
        System.out.println("Reembolso de paquete aprobado.");
    }

    @Override
    public void rechazarReembolsoPaquete(Cliente cliente, PaqueteTiquetes paquete, String motivo, String credencialAdmin) {
        admin.rechazarReembolsoPorCalamidadPaquete(cliente);
        System.out.println("Reembolso de paquete rechazado.");
    }

    @Override
    public void iniciar() {
        System.out.println("Interfaz administrador iniciada.");
    }

    @Override
    public void consultarLog(int idAdmin, LocalDateTime fechaInicial, LocalDateTime fechaFinal, String tipoRegistro, int idUsuario) {
        var log = admin.consultarLogOfertas();
        System.out.println("Log consultado. Registros: " + log.size());
    }

    @Override
    public void eliminarOferta(String credencialAdmin, int idOferta, String motivoEliminacion) {
        System.out.println("La oferta " + idOferta + " se marca como eliminada por el administrador.");
        
    }

    @Override
    public void registrarEventosMarketplace(String tipoEvento, ArrayList<Usuario> usuarios, LocalDateTime fechaHora,
                                            ArrayList<Double> precios) {
        System.out.println("Registro en log: " + tipoEvento + " realizado con " + usuarios.size() + " usuarios.");
    }

    @Override
    public void acreditarSaldoPorVentas(int idVendedor, ArrayList<Tiquete> tiquetesVendidos, double precioTotal,
                                        LocalDateTime fechaHora) {
        System.out.println("Venta acreditada: vendedor " + idVendedor + " recibe $" + precioTotal);
    }

    @Override
    public void salir() {
        System.out.println("Interfaz administrador cerrada.");
    }

    // ==========================================================
    // MAIN PARA PRUEBAS
    // ==========================================================

    public static void main(String[] args) {

        Administrador admin = new Administrador("adminLogin", "1234", "Administrador", 1);
        ConsolaAdministrador consola = new ConsolaAdministrador(admin);

        // Crear y aprobar venue
        Venue v = new Venue(10, "Coliseo", "Bogotá", 5000, "");
        consola.aprobarVenue(v);

        // Crear organizador + evento de prueba
        Usuarios.Organizador org = new Usuarios.Organizador("org", "pass", "Organizador", 2, 0.0, "Org Ltda");
        Evento e = new Evento("Concierto", 20, v, LocalDateTime.now().plusDays(2), org, "Musica", admin);

        // Cancelar evento
        consola.cancelarEvento(e);

        // Solicitud de reembolso
        Cliente cli = new Cliente("cli", "1111", "Cliente", 3);
        Tiquete t = new Tiquetes.Individual(500, 100.0, e, new Eventos.Localidad("VIP", true, 120000, 200, e, 50, 0, 400), cli, true, 10);
        consola.crearSolicitudReembolsoTiquete(t, cli);
        consola.aprobarReembolsoTiquete(cli, t, "", "");
        System.out.println("Saldo cliente: " + cli.getSaldo());

        consola.salir();
    }
}
