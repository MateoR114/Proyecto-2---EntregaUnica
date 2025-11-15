package Interfaces;

import java.time.LocalDateTime;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;

public class ConsolaOrganizador implements InterfazOrganizador {

    private Organizador organizador;

    public ConsolaOrganizador(Organizador organizador) {
        this.organizador = organizador;
    }

    @Override
    public void verEventosCreados() {
        System.out.println("Eventos creados por el organizador:");
        for (Evento e : organizador.getEventos()) {
            System.out.println("- " + e.getNombre() + " (ID " + e.getId() + ")");
        }
    }

    @Override
    public void crearEvento(String nombre, int id, Venue venue, LocalDateTime fecha, String tipoEvento, Administrador admin) {
        organizador.crearEvento(nombre, id, venue, fecha, tipoEvento, admin);
        System.out.println("Evento creado por el organizador.");
    }

    @Override
    public void verReportesFinancieros() {
        System.out.println("Reportes financieros generados:");
        organizador.getReportesGenerados().forEach(
                r -> System.out.println("- Reporte ID " + r.getId())
        );
    }

    @Override
    public void crearReporteFinanciero(Evento evento) {
        organizador.generarReporteFinanciero(evento);
        System.out.println("Reporte financiero generado.");
    }

    @Override
    public void cancelarEvento(Evento evento, String motivo) {
        organizador.cancelarEvento(evento, motivo);
        System.out.println("Evento cancelado.");
    }

    @Override
    public void modificarEvento(Evento evento, String nuevoNombre, int id, LocalDateTime nuevaFecha) {
        organizador.modificarEvento(evento, nuevoNombre, id, nuevaFecha);
        System.out.println("Evento modificado.");
    }

    @Override
    public void crearLocalidad(Evento evento, String nombre, boolean numerada, int capacidad, double precioBase,
                               double precioBasePaquetesxUnidad, double precioPaseTemporadaxUnidad, double precioPaseDeluxexUnidad) {

        organizador.crearLocalidad(evento, nombre, numerada, capacidad,
                precioBase, precioBasePaquetesxUnidad, precioPaseTemporadaxUnidad, precioPaseDeluxexUnidad);

        System.out.println("Localidad creada.");
    }

    @Override
    public void modificarLocalidad(Evento evento, Localidad localidad, double nuevoPrecio) {
        organizador.modificarLocalidad(evento, localidad, nuevoPrecio);
        System.out.println("Localidad modificada.");
    }

    @Override
    public void crearOferta(Evento evento, double porcentajeDescuento, LocalDateTime fechaInicio,
                            LocalDateTime fechaFin, Localidad localidad) {
        organizador.crearOferta(evento, porcentajeDescuento, fechaInicio, fechaFin, localidad);
        System.out.println("Oferta creada.");
    }

    @Override
    public void registrarCortesia(Evento evento, Cliente cliente, Localidad localidad) {

        
        if (localidad.isNumerada()) {
            for (int asiento = 1; asiento <= localidad.getCapacidad(); asiento++) {
                if (localidad.asientoDisponible(asiento)) {
                    localidad.reservarAsiento(asiento);
                    break;
                }
            }
        } else {
            if (!localidad.hayDisponibilidad()) {
                throw new IllegalStateException("No hay cupos para cortesía en esta localidad.");
            }
        }

        organizador.registrarCortesia(evento, cliente, localidad);
        System.out.println("Cortesía registrada.");
    }

    @Override
    public void iniciar() {
        System.out.println("Interfaz organizador iniciada.");
    }

    @Override
    public void salir() {
        System.out.println("Interfaz organizador cerrada.");
    }

    // =========================================================================
    // MAIN PARA PRUEBAS
    // =========================================================================
    public static void main(String[] args) {

        System.out.println("=== Consola Organizador ===");

        Administrador admin = new Administrador("admin", "1234", "Administrador", 1);
        Organizador org = new Organizador("org", "pass", "Organizador", 2, 0.0, "OrgLtda");
        ConsolaOrganizador consola = new ConsolaOrganizador(org);

        Venue venue = new Venue(10, "Teatro Nacional", "Bogotá", 5000, "");
        venue.setAprobado(true);

        consola.crearEvento("Festival", 100, venue, LocalDateTime.now().plusDays(3), "Musica", admin);

        Evento e = org.getEventos().get(0);

        consola.crearLocalidad(e, "VIP", true, 100, 200000, 150000, 300000, 500000);

        Cliente cli = new Cliente("mateo", "pass", "Cliente", 3);
        Localidad loca = e.getLocalidades().get(0);

        consola.registrarCortesia(e, cli, loca);

        consola.crearReporteFinanciero(e);
        consola.verEventosCreados();

        consola.salir();
    }
}

