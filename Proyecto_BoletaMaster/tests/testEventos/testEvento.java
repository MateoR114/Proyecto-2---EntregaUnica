package testEventos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Oferta;
import Eventos.Venue;

import static org.junit.jupiter.api.Assertions.*;

import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;

import java.time.LocalDateTime;
import java.util.List;


public class testEvento {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private LocalDateTime fechaFutura;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Admin", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.15);

        organizador = new Organizador("org", "pwd", "Org", 10, 0.0, "Org S.A.");
        venue = new Venue(101, "Coliseo", "Calle 1 #2-3", 5000, "No pirotecnia");
        venue.setAprobado(true);

        fechaFutura = LocalDateTime.now().plusDays(7);
    }

    /**
     * Given: Un administrador con una cuota de servicio definida para el tipo "Concierto",
     *        un organizador, un venue aprobado y una fecha futura.
     * When:  Se crea un Evento con nombre, id, venue, fecha futura, organizador, tipo
     *        de evento y el administrador como referencia.
     * Then:  El evento queda inicializado con los campos básicos (nombre, id, venue,
     *        fecha, organizador, tipo), estado "Activo", listas de localidades y
     *        ofertas vacías pero no nulas, y la cuota del evento igual a la definida
     *        por el administrador (0.15).
     */
    @Test
    @DisplayName("Constructor: inicializa campos, estado Activo y cuota por tipo desde Administrador")
    void constructorInicializa() {
        Evento e = new Evento("Rock Fest", 1001, venue, fechaFutura, organizador, "Concierto", admin);

        assertEquals("Rock Fest", e.getNombre());
        assertEquals(1001, e.getId());
        assertEquals(venue, e.getVenue());
        assertEquals(fechaFutura, e.getFecha());
        assertEquals(organizador, e.getOrganizador());
        assertEquals("Concierto", e.getTipoEvento());
        assertEquals("Activo", e.getEstado());
        assertNotNull(e.getLocalidades());
        assertTrue(e.getLocalidades().isEmpty());
        assertNotNull(e.getOfertas());
        assertTrue(e.getOfertas().isEmpty());
        assertEquals(0.15, e.getCuotaEvento(), 1e-6);
    }

    /**
     * Given: Un evento creado en estado "Activo" cuyo estado puede cambiar a
     *        "finalizado", "cancelado por administrador" o "cancelado".
     * When:  Se intenta cancelar el evento por organizador y administrador cuando
     *        el estado es "finalizado", y luego se cancela desde el administrador
     *        y desde el organizador cuando el estado es "Activo".
     * Then:  En estado "finalizado", ambos intentos de cancelación deben lanzar
     *        IllegalStateException y conservar el estado "finalizado"; en estado
     *        "Activo", cancelarPorAdministrador() debe dejar el estado en
     *        "cancelado por administrador" y cancelarPorOrganizador(...) debe dejar
     *        el estado en "cancelado".
     */
    @Test
    @DisplayName("cancelarPorOrganizador / cancelarPorAdministrador: solo permiten cancelar cuando estado = 'finalizado'")
    void cancelarSegunEstadoActual() {
        Evento e = new Evento("Show", 1002, venue, fechaFutura, organizador, "Concierto", admin);

        e.setEstado("finalizado");
        assertThrows(IllegalStateException.class, () -> e.cancelarPorOrganizador("Motivo X"), "a");
        assertThrows(IllegalStateException.class, e::cancelarPorAdministrador, "b");
        assertEquals("finalizado", e.getEstado());

        e.setEstado("Activo");
        e.cancelarPorAdministrador();
        assertEquals("cancelado por administrador", e.getEstado());
        
        e.setEstado("Activo");
        e.cancelarPorOrganizador("Motivo");
        assertEquals("cancelado", e.getEstado());
    }

    /**
     * Given: Un evento creado con un nombre, id y fecha iniciales.
     * When:  Se invoca modificarDatos(...) con un nuevo nombre, nuevo id y una nueva
     *        fecha para el evento.
     * Then:  El evento debe reflejar los nuevos valores en sus getters de nombre, id
     *        y fecha, sustituyendo completamente los anteriores.
     */
    @Test
    @DisplayName("modificarDatos: actualiza nombre, id y fecha")
    void modificarDatos() {
        Evento e = new Evento("Original", 2001, venue, fechaFutura, organizador, "Concierto", admin);

        LocalDateTime nuevaFecha = fechaFutura.plusDays(3);
        e.modificarDatos("Nuevo Nombre", 2002, nuevaFecha);

        assertEquals("Nuevo Nombre", e.getNombre());
        assertEquals(2002, e.getId());
        assertEquals(nuevaFecha, e.getFecha());
    }

    /**
     * Given: Un evento sin localidades iniciales.
     * When:  Se crean dos localidades (VIP y General) mediante crearLocalidad y luego
     *        se buscan por nombre usando buscarLocalidad con diferentes mayúsculas
     *        y minúsculas, e incluso un nombre inexistente.
     * Then:  Las localidades creadas deben agregarse a la lista interna del evento,
     *        pueden ser encontradas por nombre de forma case-insensitive y una
     *        búsqueda con un nombre inexistente debe devolver null.
     */
    @Test
    @DisplayName("crearLocalidad y buscarLocalidad: agrega a la lista y se puede encontrar por nombre (case-insensitive)")
    void crearYBuscarLocalidad() {
        Evento e = new Evento("Teatro", 3001, venue, fechaFutura, organizador, "Concierto", admin);

        Localidad vip = e.crearLocalidad("VIP", true, 100, 200.0, 150.0, 120.0, 250.0);
        Localidad general = e.crearLocalidad("General", false, 500, 80.0, 60.0, 50.0, 120.0);

        assertEquals(2, e.getLocalidades().size());
        assertTrue(e.getLocalidades().contains(vip));
        assertTrue(e.getLocalidades().contains(general));

        assertEquals(vip, e.buscarLocalidad("vip"));
        assertEquals(general, e.buscarLocalidad("GENERAL"));
        assertNull(e.buscarLocalidad("NoExiste"));
    }

    /**
     * Given: Un evento con una localidad asociada y sin ofertas iniciales.
     * When:  Se crean varias ofertas con distintos rangos de vigencia (una que
     *        incluye la fecha de "ahora", otra futura y otra ya vencida) y luego
     *        se consulta obtenerOfertasActivas(ahora).
     * Then:  La lista de ofertas activas debe contener únicamente la oferta cuyo
     *        rango de fechas incluye el instante proporcionado como parámetro.
     */
    @Test
    @DisplayName("crearOferta y obtenerOfertasActivas: filtra correctamente por fecha")
    void ofertasActivas() {
        Evento e = new Evento("Promo", 4001, venue, fechaFutura, organizador, "Concierto", admin);
        Localidad loc = e.crearLocalidad("General", false, 1000, 50.0, 40.0, 35.0, 90.0);

        LocalDateTime ahora = LocalDateTime.now();
        e.crearOferta(10.0, ahora.minusDays(1), ahora.plusDays(1), loc);
        e.crearOferta(20.0, ahora.plusDays(2), ahora.plusDays(5), loc);
        e.crearOferta(5.0, ahora.minusDays(10), ahora.minusDays(5), loc);

        List<Oferta> activasHoy = e.obtenerOfertasActivas(ahora);
        assertEquals(1, activasHoy.size());
    }

    /**
     * Given: Un evento sin tiquetes vendidos y una localidad con capacidad para
     *        entregar cortesías.
     * When:  Se entregan cortesías a dos clientes distintos mediante entregarCortesia,
     *        lo que debería reservar tiquetes en la localidad para cada cliente.
     * Then:  El total de tiquetes vendidos del evento debe incrementarse en la misma
     *        cantidad de cortesías entregadas (en este caso, 2).
     */
    @Test
    @DisplayName("entregarCortesia: reserva asiento en la localidad y afecta el total de vendidos del evento")
    void entregarCortesiaAfectaVendidos() {
        Evento e = new Evento("Cortesias", 5001, venue, fechaFutura, organizador, "Concierto", admin);
        Localidad loc = e.crearLocalidad("VIP", false, 5, 200.0, 150.0, 120.0, 250.0);

        Cliente c1 = new Cliente("c1", "pwd", "Cliente 1", 1);
        Cliente c2 = new Cliente("c2", "pwd", "Cliente 2", 2);

        assertEquals(0, e.calcularTotalTiquetesVendidos());

        e.entregarCortesia(c1, loc);
        e.entregarCortesia(c2, loc);

        assertEquals(2, e.calcularTotalTiquetesVendidos());
    }

    /**
     * Given: Un evento con dos localidades, donde cada localidad tiene precios y
     *        reglas de reserva definidos.
     * When:  Se reservan asientos en las localidades (dos reservas en la localidad A
     *        y una en la localidad B) y luego se invoca calcularIngresosTotales().
     * Then:  El valor retornado debe ser mayor que 0 y coincidir con la suma de los
     *        ingresos generados por las localidades según sus reservas (en este caso,
     *        exactamente 300.0).
     */
    @Test
    @DisplayName("calcularIngresosTotales: suma los ingresos de todas las localidades")
    void calcularIngresosTotalesSumaLocalidades() {
        Evento e = new Evento("Ingresos", 6001, venue, fechaFutura, organizador, "Concierto", admin);
        Localidad locA = e.crearLocalidad("A", false, 100, 50.0, 40.0, 35.0, 90.0);
        Localidad locB = e.crearLocalidad("B", true,  50, 200.0, 150.0, 120.0, 250.0);

        locA.reservarAsiento(); 
        locA.reservarAsiento(); 
        locB.reservarAsiento(1); 

        double ingresos = e.calcularIngresosTotales();

        assertTrue(ingresos > 0);
        assertEquals(300.0, ingresos, 1e-6);
    }
}
