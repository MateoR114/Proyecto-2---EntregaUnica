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
import Usuarios.Organizador;

import java.time.LocalDateTime;


public class testOferta {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento;
    private Localidad localidad;

    private LocalDateTime t0, t1, t2;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Admin", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.10);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(100, "Coliseo", "Calle 1 #2-3", 1000, "No pirotecnia");
        venue.setAprobado(true);

        evento = new Evento("Rock", 500, venue, LocalDateTime.now().plusDays(7), organizador, "Concierto", admin);
        localidad = new Localidad("VIP", true, 50, 200.0, evento, 150.0, 120.0, 250.0);

        t0 = LocalDateTime.now();
        t1 = t0.plusDays(1);
        t2 = t0.plusDays(2);
    }

    /**
     * Given: Una localidad válida, un evento asociado y un rango de fechas definido
     *        (inicio y fin), junto con un porcentaje de descuento.
     * When:  Se construye una Oferta con ese porcentaje, fecha de inicio, fecha de fin
     *        y la localidad objetivo.
     * Then:  La oferta debe inicializar correctamente sus campos: porcentaje de
     *        descuento, fechas de inicio y fin, y la referencia a la localidad
     *        asociada.
     */
    @Test
    @DisplayName("Constructor: inicializa porcentaje, fechas y localidad")
    void constructorInicializa() {
        Oferta o = new Oferta(15.0, t0, t2, localidad);
        assertEquals(15.0, o.getPorcentajeDescuento(), 1e-6);
        assertEquals(t0, o.getFechaInicio());
        assertEquals(t2, o.getFechaFin());
        assertSame(localidad, o.getLocalidad());
    }

    /**
     * Given: Una oferta creada con un porcentaje de descuento inicial dentro del
     *        rango permitido [0, 100].
     * When:  Se establecen distintos valores de porcentaje dentro del rango (0 y 100)
     *        y luego se intenta asignar valores fuera de ese rango.
     * Then:  Los valores dentro de [0, 100] deben ser aceptados y reflejados por el
     *        getter; los valores menores a 0 o mayores a 100 deben provocar una
     *        IllegalArgumentException.
     */
    @Test
    @DisplayName("setPorcentajeDescuento: acepta [0,100] y rechaza fuera de rango")
    void setPorcentajeDescuentoValidaciones() {
        Oferta o = new Oferta(0.0, t0, t2, localidad);

        o.setPorcentajeDescuento(0.0);
        assertEquals(0.0, o.getPorcentajeDescuento(), 1e-6);

        o.setPorcentajeDescuento(100.0);
        assertEquals(100.0, o.getPorcentajeDescuento(), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> o.setPorcentajeDescuento(-0.01));
        assertThrows(IllegalArgumentException.class, () -> o.setPorcentajeDescuento(100.01));
    }

    /**
     * Given: Una oferta con un rango de vigencia que va de t0 a t2 (inclusive).
     * When:  Se consulta estaActiva() usando la fecha de inicio, una fecha intermedia,
     *        la fecha de fin y fechas justo antes y justo después del rango.
     * Then:  estaActiva() debe retornar true para cualquier instante dentro del rango
     *        [t0, t2], incluyendo los bordes, y false para instantes fuera de dicho
     *        rango.
     */
    @Test
    @DisplayName("estaActiva: true en el inicio y fin (bordes inclusivos) y dentro del rango; false fuera")
    void estaActivaBordesEInteriores() {
        Oferta o = new Oferta(20.0, t0, t2, localidad);

        assertTrue(o.estaActiva(t0));
        assertTrue(o.estaActiva(t1));
        assertTrue(o.estaActiva(t2));

        assertFalse(o.estaActiva(t0.minusNanos(1)));
        assertFalse(o.estaActiva(t2.plusNanos(1)));
    }

    /**
     * Given: Una oferta con un porcentaje de descuento válido y precios base
     *        no negativos a los que se les aplicará el descuento.
     * When:  Se llama a aplicarDescuento() con distintos precios (incluyendo 0) y se
     *        cambia el porcentaje de descuento a 100%; además, se intenta aplicar el
     *        descuento sobre un precio negativo.
     * Then:  El precio final debe corresponder al precio original menos el porcentaje
     *        de descuento; con 100% de descuento, el resultado debe ser 0.0; y al
     *        pasar un precio negativo, el método debe lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("aplicarDescuento: calcula precio final y valida precio negativo")
    void aplicarDescuento() {
        Oferta o = new Oferta(25.0, t0, t2, localidad);

        assertEquals(75.0, o.aplicarDescuento(100.0), 1e-6);
        assertEquals(0.0, o.aplicarDescuento(0.0), 1e-6);

        o.setPorcentajeDescuento(100.0);
        assertEquals(0.0, o.aplicarDescuento(50.0), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> o.aplicarDescuento(-1.0));
    }

    /**
     * Given: Una oferta con fechas de inicio y fin definidas y una localidad asociada.
     * When:  Se cambian las fechas de inicio y fin, y se asigna una nueva localidad
     *        mediante los setters correspondientes.
     * Then:  Los getters de fechaInicio, fechaFin y localidad deben reflejar los
     *        nuevos valores, reemplazando por completo los anteriores, sin aplicar
     *        validaciones adicionales en este contexto de prueba.
     */
    @Test
    @DisplayName("Setters de fecha y localidad: actualizan correctamente (sin validación adicional)")
    void settersBasicos() {
        Oferta o = new Oferta(10.0, t0, t2, localidad);

        LocalDateTime nuevoInicio = t0.plusHours(2);
        LocalDateTime nuevoFin = t2.plusDays(1);
        Localidad otra = new Localidad("General", false, 500, 50.0, evento, 40.0, 35.0, 90.0);

        o.setFechaInicio(nuevoInicio);
        o.setFechaFin(nuevoFin);
        o.setEvento(otra);

        assertEquals(nuevoInicio, o.getFechaInicio());
        assertEquals(nuevoFin, o.getFechaFin());
        assertSame(otra, o.getLocalidad());
    }
}
