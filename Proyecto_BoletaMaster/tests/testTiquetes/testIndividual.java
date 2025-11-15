package testTiquetes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;

import static org.junit.jupiter.api.Assertions.*;

import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;

import java.time.LocalDateTime;

import Tiquetes.Individual;


public class testIndividual {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento;
    private Localidad locNumerada;
    private Localidad locNoNumerada;
    private Cliente comprador;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Admin", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.10);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(101, "Coliseo", "Calle 1 #2-3", 5000, "No pirotecnia");
        venue.setAprobado(true);

        evento = new Evento("Rock Fest", 1001, venue, LocalDateTime.now().plusDays(7), organizador, "Concierto", admin);

        locNumerada = evento.crearLocalidad("VIP", true, 100, 200.0, 150.0, 120.0, 250.0);
        locNoNumerada = evento.crearLocalidad("General", false, 500, 50.0, 40.0, 35.0, 90.0);

        comprador = new Cliente("cli", "pwd", "Cliente Prueba", 777);
    }

    /**
     * Given: Una localidad numerada, un comprador válido, un evento y valores iniciales
     *        correctos (código, precio y número de asiento).
     * When:  Se construye un objeto Individual con estos parámetros.
     * Then:  El tiquete debe quedar inicializado con el código, precio pagado, comprador
     *        y número de asiento correctos, accesibles mediante sus getters.
     */
    @Test
    @DisplayName("Constructor: inicializa código, precio, evento/localidad, dueño y número de asiento")
    void constructorInicializa() {
        Individual t = new Individual(123, 200.0, evento, locNumerada, comprador, true, 10);

        assertEquals(123, t.getCodigo());
        assertEquals(200.0, t.getPrecioPagado(), 1e-6);
        assertEquals(10, t.getNumeroAsiento());
        assertSame(comprador, t.getComprador());
    }


    /**
     * Given: Un tiquete Individual inicializado correctamente.
     * When:  Se actualizan código, precio pagado, número de asiento y comprador mediante
     *        sus setters correspondientes.
     * Then:  Los valores deben actualizarse correctamente y los getters deben reflejar
     *        la nueva información sin inconsistencias.
     */
    @Test
    @DisplayName("setCodigo, setNumeroAsiento, setPrecioPagado, setComprador actualizan valores")
    void settersActualizan() {
        Individual t = new Individual(1, 100.0, evento, locNoNumerada, comprador, true, -1);

        t.setCodigo(999);
        t.setNumeroAsiento(42);
        t.setPrecioPagado(123.45);
        Cliente nuevo = new Cliente("otro", "pwd", "Otro Cliente", 778);
        t.setComprador(nuevo);

        assertEquals(999, t.getCodigo());
        assertEquals(42, t.getNumeroAsiento());
        assertEquals(123.45, t.getPrecioPagado(), 1e-6);
        assertSame(nuevo, t.getComprador());
    }


    /**
     * Given: Un tiquete Individual válido que aún no ha sido usado ni reembolsado.
     * When:  Se invoca el método usar() para marcarlo como utilizado.
     * Then:  El tiquete queda marcado como usado y cualquier intento adicional de usarlo
     *        debe lanzar IllegalStateException.
     */
    @Test
    @DisplayName("usar(): marca el tiquete como usado si no está usado ni reembolsado")
    void usarOk() {
        Individual t = new Individual(10, 50.0, evento, locNoNumerada, comprador, true, -1);

        t.usar();

        assertThrows(IllegalStateException.class, t::usar);
    }

    /**
     * Given: Un tiquete Individual recién creado.
     * When:  Se marca como reembolsado antes de intentar usarlo.
     * Then:  Invocar usar() debe lanzar IllegalStateException ya que un tiquete
     *        reembolsado no puede ser utilizado.
     */
    @Test
    @DisplayName("usar(): lanza si ya fue reembolsado")
    void usarLanzaSiReembolsado() {
        Individual t = new Individual(11, 60.0, evento, locNumerada, comprador, true, 5);
        t.reembolsar(); 
        assertThrows(IllegalStateException.class, t::usar);
    }

    /**
     * Given: Un tiquete Individual válido que no ha sido usado ni reembolsado.
     * When:  Se llama al método reembolsar() para marcarlo como tal.
     * Then:  El tiquete queda en estado reembolsado y un segundo intento de reembolsar
     *        debe lanzar IllegalStateException.
     */
    @Test
    @DisplayName("reembolsar(): marca reembolsado si no ha sido usado ni reembolsado")
    void reembolsarOk() {
        Individual t = new Individual(20, 80.0, evento, locNumerada, comprador, true, 8);

        t.reembolsar();

        assertThrows(IllegalStateException.class, t::reembolsar);
    }

    /**
     * Given: Un tiquete Individual válido.
     * When:  Se llama usar() primero, marcándolo como utilizado, y luego se intenta
     *        reembolsarlo.
     * Then:  reembolsar() debe lanzar IllegalStateException ya que un tiquete usado
     *        no puede ser reembolsado.
     */
    @Test
    @DisplayName("reembolsar(): lanza si ya fue usado")
    void reembolsarLanzaSiUsado() {
        Individual t = new Individual(21, 90.0, evento, locNoNumerada, comprador, true, -1);
        t.usar();
        assertThrows(IllegalStateException.class, t::reembolsar);
    }


    /**
     * Given: Dos tipos de localidad (numerada y no numerada) disponibles en el evento.
     * When:  Se crean tiquetes con asiento explícito en la numerada y con -1 en la no numerada,
     *        y posteriormente se ajustan los números de asiento mediante el setter.
     * Then:  En localidades numeradas, el número de asiento debe almacenarse y actualizarse.
     *        En localidades no numeradas, siempre debe mantenerse -1 según convención del sistema.
     */
    @Test
    @DisplayName("En localidad numerada, el número de asiento puede fijarse; en no numerada se usa -1 por convención")
    void asientoSegunTipoLocalidad() {
        Individual numerado = new Individual(30, 200.0, evento, locNumerada, comprador, true, 12);
        assertEquals(12, numerado.getNumeroAsiento());

        Individual noNumerado = new Individual(31, 50.0, evento, locNoNumerada, comprador, true, -1);
        assertEquals(-1, noNumerado.getNumeroAsiento());

        numerado.setNumeroAsiento(99);
        assertEquals(99, numerado.getNumeroAsiento());
        noNumerado.setNumeroAsiento(-1);
        assertEquals(-1, noNumerado.getNumeroAsiento());
    }
}
