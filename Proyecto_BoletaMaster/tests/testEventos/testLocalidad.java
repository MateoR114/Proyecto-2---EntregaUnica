package testEventos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;

import static org.junit.jupiter.api.Assertions.*;

import Usuarios.Administrador;
import Usuarios.Organizador;

import java.time.LocalDateTime;

public class testLocalidad {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento;
    private LocalDateTime fechaFutura;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Admin", 1);
        // Para que Evento calcule cuota por tipo sin fallar
        admin.establecerCargoServicioPorTipo("Concierto", 0.10);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(100, "Coliseo", "Calle 1 #2-3", 1000, "No pirotecnia");
        venue.setAprobado(true);

        fechaFutura = LocalDateTime.now().plusDays(7);
        evento = new Evento("Rock", 500, venue, fechaFutura, organizador, "Concierto", admin);
    }

    /**
     * Given: Un evento válido y los parámetros necesarios para crear una localidad
     *        (nombre, si es numerada, capacidad, precios y referencia al evento).
     * When:  Se construye una nueva instancia de Localidad con esos valores.
     * Then:  La localidad debe inicializar sus campos básicos (nombre, numerada,
     *        capacidad, precio base, contadores de tiquetes vendidos) y los precios
     *        unitarios para paquetes, pase de temporada y pase deluxe según los
     *        valores suministrados, manteniendo la referencia al evento.
     */
    @Test
    @DisplayName("Constructor: inicializa nombre, tipo, capacidad y contadores")
    void constructorInicializa() {
        Localidad loc = new Localidad("VIP", true, 50, 200.0, evento, 150.0, 120.0, 250.0);

        assertEquals("VIP", loc.getNombre());
        assertTrue(loc.isNumerada());
        assertEquals(50, loc.getCapacidad());
        assertEquals(200.0, loc.getPrecioBase(), 1e-6);
        assertEquals(0, loc.getTiquetesVendidos());
        assertSame(evento, loc.getEvento());

        assertEquals(150.0, loc.getPrecioBasePaquetesxUnidad(), 1e-6);
        assertEquals(120.0, loc.getPrecioPaseTemporadaxUnidad(), 1e-6);
        assertEquals(250.0, loc.getPrecioPaseDeluxexUnidad(), 1e-6);
    }

    /**
     * Given: Una localidad inicializada con nombre, numeración, capacidad y precio base.
     * When:  Se modifican estos atributos mediante los setters correspondientes.
     * Then:  Los getters deben reflejar los nuevos valores asignados, reemplazando
     *        completamente los valores originales.
     */
    @Test
    @DisplayName("Setters: nombre, numerada, capacidad y precioBase")
    void settersActualizan() {
        Localidad loc = new Localidad("General", false, 100, 50.0, evento, 40.0, 35.0, 90.0);

        loc.setNombre("Platea");
        loc.setNumerada(true);
        loc.setCapacidad(120);
        loc.setPrecioBase(99.9);

        assertEquals("Platea", loc.getNombre());
        assertTrue(loc.isNumerada());
        assertEquals(120, loc.getCapacidad());
        assertEquals(99.9, loc.getPrecioBase(), 1e-6);
    }

    /**
     * Given: Una localidad no numerada con una capacidad máxima y cero tiquetes vendidos.
     * When:  Se consultan hayDisponibilidad(), se reservan asientos generales hasta
     *        alcanzar la capacidad y se vuelve a consultar disponibilidad.
     * Then:  Mientras los tiquetes vendidos sean menores a la capacidad, hayDisponibilidad()
     *        debe retornar true; al alcanzar la capacidad, debe retornar false y el
     *        contador de tiquetes vendidos debe igualar la capacidad.
     */
    @Test
    @DisplayName("hayDisponibilidad: true mientras vendidos < capacidad (no numerada)")
    void hayDisponibilidadNoNumerada() {
        Localidad loc = new Localidad("General", false, 3, 10.0, evento, 8.0, 7.0, 20.0);
        assertTrue(loc.hayDisponibilidad());
        loc.reservarAsiento();
        loc.reservarAsiento();
        assertTrue(loc.hayDisponibilidad());
        loc.reservarAsiento();
        assertFalse(loc.hayDisponibilidad());
        assertEquals(3, loc.getTiquetesVendidos());
    }

    /**
     * Given: Una localidad numerada con capacidad definida y una localidad no numerada.
     * When:  En la localidad numerada, se consulta si un asiento está disponible antes
     *        y después de reservarlo. En la no numerada, se invoca asientoDisponible()
     *        pasando un número de asiento.
     * Then:  En la numerada, un asiento libre debe reportarse como disponible y luego
     *        como no disponible tras la reserva; en la no numerada, llamar
     *        asientoDisponible() debe lanzar IllegalStateException.
     */
    @Test
    @DisplayName("asientoDisponible: válido solo en numeradas; en no numeradas lanza")
    void asientoDisponibleComportamiento() {
        Localidad num = new Localidad("VIP", true, 2, 100.0, evento, 80.0, 70.0, 150.0);
        assertTrue(num.asientoDisponible(1));
        num.reservarAsiento(1);
        assertFalse(num.asientoDisponible(1));

        Localidad noNum = new Localidad("General", false, 2, 50.0, evento, 40.0, 35.0, 90.0);
        assertThrows(IllegalStateException.class, () -> noNum.asientoDisponible(1));
    }

    /**
     * Given: Una localidad numerada con capacidad y cero tiquetes vendidos.
     * When:  Se reserva un asiento numerado disponible, se verifica que aumenta el
     *        contador de tiquetes vendidos y que el asiento deja de estar disponible,
     *        y luego se intenta reservar el mismo asiento nuevamente.
     * Then:  La primera reserva incrementa los tiquetes vendidos y marca el asiento
     *        como ocupado; la segunda reserva sobre el mismo asiento debe lanzar
     *        IllegalStateException. Reservar otro asiento válido debe incrementar
     *        nuevamente el contador de tiquetes vendidos.
     */
    @Test
    @DisplayName("reservarAsiento(int): incrementa vendidos y evita duplicados en numeradas")
    void reservarAsientoNumerada() {
        Localidad loc = new Localidad("VIP", true, 2, 100.0, evento, 80.0, 70.0, 150.0);

        loc.reservarAsiento(1);
        assertEquals(1, loc.getTiquetesVendidos());
        assertFalse(loc.asientoDisponible(1));

        assertThrows(IllegalStateException.class, () -> loc.reservarAsiento(1));

        loc.reservarAsiento(2);
        assertEquals(2, loc.getTiquetesVendidos());
    }

    /**
     * Given: Una localidad numerada con algunos asientos ya reservados.
     * When:  Se reservan dos asientos, se verifica que uno está ocupado y luego
     *        se llama a liberarAsiento() sobre uno de esos asientos.
     * Then:  El contador de tiquetes vendidos debe decrementar en uno y el asiento
     *        liberado debe volver a reportarse como disponible.
     */
    @Test
    @DisplayName("liberarAsiento: libera asiento numerado y decrementa vendidos")
    void liberarAsientoNumerada() {
        Localidad loc = new Localidad("VIP", true, 3, 100.0, evento, 80.0, 70.0, 150.0);

        loc.reservarAsiento(10);
        loc.reservarAsiento(11);
        assertEquals(2, loc.getTiquetesVendidos());
        assertFalse(loc.asientoDisponible(10));

        loc.liberarAsiento(10);
        assertEquals(1, loc.getTiquetesVendidos());
        assertTrue(loc.asientoDisponible(10));
    }

    /**
     * Given: Una localidad no numerada con capacidad limitada y una localidad numerada
     *        distinta.
     * When:  En la no numerada, se reservan asientos generales hasta agotar la
     *        capacidad y se consulta el número de tiquetes vendidos y la disponibilidad.
     *        En la numerada, se invoca reservarAsiento() sin número de asiento.
     * Then:  En la no numerada, el contador de vendidos debe incrementarse con cada
     *        reserva y hayDisponibilidad() debe pasar a false al llegar a la capacidad;
     *        en la numerada, llamar reservarAsiento() sin parámetro debe lanzar
     *        IllegalStateException.
     */
    @Test
    @DisplayName("reservarAsiento() en no numerada incrementa vendidos; en numerada lanza")
    void reservarAsientoNoNumerada() {
        Localidad noNum = new Localidad("General", false, 2, 50.0, evento, 40.0, 35.0, 90.0);

        noNum.reservarAsiento();
        noNum.reservarAsiento();
        assertEquals(2, noNum.getTiquetesVendidos());
        assertFalse(noNum.hayDisponibilidad());

        Localidad num = new Localidad("VIP", true, 2, 100.0, evento, 80.0, 70.0, 150.0);
        assertThrows(IllegalStateException.class, num::reservarAsiento);
    }

    /**
     * Given: Una localidad no numerada con capacidad limitada y cero tiquetes
     *        vendidos, y otra localidad numerada independiente.
     * When:  En la no numerada, se intenta cancelar una reserva general sin haber
     *        vendido tiquetes (esperando error), luego se reservan asientos, se
     *        cancela una reserva y se verifica el conteo. En la numerada, se invoca
     *        cancelarReservaGeneral().
     * Then:  En la no numerada, cancelar sin ventas debe lanzar IllegalStateException,
     *        cancelar tras haber ventas debe decrementar el contador; en la numerada,
     *        llamar cancelarReservaGeneral() debe lanzar IllegalStateException.
     */
    @Test
    @DisplayName("cancelarReservaGeneral: decrementa en no numerada; valida errores")
    void cancelarReservaGeneralNoNumerada() {
        Localidad noNum = new Localidad("General", false, 2, 50.0, evento, 40.0, 35.0, 90.0);

        assertThrows(IllegalStateException.class, noNum::cancelarReservaGeneral);

        noNum.reservarAsiento();
        noNum.reservarAsiento();
        assertEquals(2, noNum.getTiquetesVendidos());

        noNum.cancelarReservaGeneral();
        assertEquals(1, noNum.getTiquetesVendidos());

        Localidad num = new Localidad("VIP", true, 2, 100.0, evento, 80.0, 70.0, 150.0);
        assertThrows(IllegalStateException.class, num::cancelarReservaGeneral);
    }

    /**
     * Given: Una localidad no numerada y otra numerada, cada una con un precio base
     *        definido y con un cierto número de tiquetes vendidos.
     * When:  Se reservan asientos en ambas localidades y se llama a calcularIngresos()
     *        antes y después de modificar el precio base en la localidad numerada.
     * Then:  En cada caso, los ingresos deben corresponder a tiquetesVendidos *
     *        precioBase; tras cambiar el precio base, los ingresos calculados deben
     *        actualizarse acorde al nuevo valor.
     */
    @Test
    @DisplayName("calcularIngresos: tiquetesVendidos * precioBase (ambos modos)")
    void calcularIngresos() {
        // No numerada
        Localidad gen = new Localidad("General", false, 3, 20.0, evento, 15.0, 12.0, 30.0);
        gen.reservarAsiento();
        gen.reservarAsiento();
        assertEquals(40.0, gen.calcularIngresos(), 1e-6);

        // Numerada
        Localidad vip = new Localidad("VIP", true, 3, 100.0, evento, 80.0, 70.0, 150.0);
        vip.reservarAsiento(1);
        vip.reservarAsiento(2);
        assertEquals(200.0, vip.calcularIngresos(), 1e-6);

        vip.setPrecioBase(50.0);
        assertEquals(100.0, vip.calcularIngresos(), 1e-6);
    }

    /**
     * Given: Tres localidades con distintas capacidades (incluyendo una de capacidad
     *        cero) y diferentes cantidades de tiquetes vendidos.
     * When:  Se consultan los valores de calcularOcupacion() en cada localidad tras
     *        realizar las reservas necesarias.
     * Then:  Para capacidad cero, la ocupación debe ser 0.0; para las demás, la
     *        ocupación debe ser el porcentaje (en 0–100) de tiquetesVendidos /
     *        capacidad, es decir 50% cuando hay 2/4 vendidos y 60% cuando hay 3/5.
     */
    @Test
    @DisplayName("calcularOcupacion: 0 cuando capacidad=0; porcentaje correcto en otros casos")
    void calcularOcupacion() {
        Localidad a = new Localidad("A", false, 0, 10.0, evento, 8.0, 7.0, 20.0);
        assertEquals(0.0, a.calcularOcupacion(), 1e-6);

        Localidad b = new Localidad("B", false, 4, 10.0, evento, 8.0, 7.0, 20.0);
        b.reservarAsiento(); // 1/4 = 25%
        b.reservarAsiento(); // 2/4 = 50%
        assertEquals(50.0, b.calcularOcupacion(), 1e-6);

        Localidad c = new Localidad("C", true, 5, 100.0, evento, 80.0, 70.0, 150.0);
        c.reservarAsiento(10);
        c.reservarAsiento(11);
        c.reservarAsiento(12); // 3/5 = 60%
        assertEquals(60.0, c.calcularOcupacion(), 1e-6);
    }
}

