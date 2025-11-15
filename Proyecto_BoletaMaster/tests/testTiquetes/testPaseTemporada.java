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
import java.util.ArrayList;

import Tiquetes.Individual;
import Tiquetes.PaseTemporada;
import Tiquetes.Tiquete;


public class testPaseTemporada {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento1, evento2;
    private Localidad loc1, loc2;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Administrador", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.10);
        admin.setCuotaEmisionGlobal(2.0);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(10, "Teatro Nacional", "Calle 123", 2000, "Sin pirotecnia");
        venue.setAprobado(true);

        evento1 = new Evento("Rock A", 1001, venue, LocalDateTime.now().plusDays(5), organizador, "Concierto", admin);
        evento2 = new Evento("Rock B", 1002, venue, LocalDateTime.now().plusDays(10), organizador, "Concierto", admin);

        loc1 = evento1.crearLocalidad("VIP-A", true, 100, 200.0, 150.0, 120.0, 250.0);
        loc2 = evento2.crearLocalidad("VIP-B", true, 100, 220.0, 160.0, 130.0, 260.0);

        cliente = new Cliente("user1", "pass", "Cliente Uno", 100);
    }

    /**
     * Given: Una lista de eventos válidos de la temporada, una lista de tiquetes
     *        precomprados para esos eventos y un cliente dueño del pase.
     * When:  Se construye un PaseTemporada con código, precio total, lista de eventos
     *        válidos y lista de tiquetes.
     * Then:  El pase queda correctamente inicializado: precio y dueño asignados,
     *        estado "Activo", transferible, tiquetes incluidos y eventos válidos
     *        registrados, cuota de impresión acorde al número de tiquetes, y cargo
     *        de servicio calculado de forma no negativa.
     */
    @Test
    @DisplayName("Constructor: inicializa eventos válidos, tiquetes, estado y campos heredados")
    void constructorInicializa() {
        ArrayList<Evento> eventosValidos = new ArrayList<>();
        eventosValidos.add(evento1);
        eventosValidos.add(evento2);

        ArrayList<Tiquete> tiqs = new ArrayList<>();
        tiqs.add(cliente.precompraIndividualNumerada(1, this.evento1, loc1, 20));
        tiqs.add(cliente.precompraIndividualNumerada(1, this.evento2, loc1, 20));
        tiqs.add(cliente.precompraIndividualNumerada(1, this.evento2, loc2, 20));

        PaseTemporada pase = new PaseTemporada(7, cliente, 999.0, eventosValidos, tiqs);

        assertEquals(0, pase.getCodigo());
        assertEquals(999.0, pase.getPrecio());
        assertSame(cliente, pase.getDueno());
        assertEquals("Activo", pase.getEstado());
        assertTrue(pase.isTransferible());

        assertEquals(3, pase.getTiquetesIncluidos().size());
        assertEquals(2, pase.getEventosValidos().size());
        assertTrue(pase.getEventosValidos().contains(evento1));
        assertTrue(pase.getEventosValidos().contains(evento2));

        assertEquals(2.0 * 3, pase.getCuotaImpresion(), 1e-6);
        assertTrue(pase.getCargoServicio() >= 0.0);
    }

    /**
     * Given: Un PaseTemporada creado con una lista inicial de eventos válidos.
     * When:  Se reemplaza la lista de eventos válidos por una nueva lista usando
     *        el setter setEventosValidos().
     * Then:  La lista interna de eventos válidos del pase debe coincidir con la nueva
     *        lista suministrada y su tamaño y contenido deben actualizarse correctamente.
     */
    @Test
    @DisplayName("get/set EventosVálidos: reemplaza correctamente la lista")
    void setEventosValidos() {
        ArrayList<Evento> evs = new ArrayList<>();
        evs.add(evento1);
        ArrayList<Tiquete> ts = new ArrayList<>();
        Individual t = cliente.precompraIndividualNumerada(1, this.evento1, loc1, 20);
        ts.add(t);
        PaseTemporada pase = new PaseTemporada(8, cliente, 500.0, evs, ts);
        assertEquals(1, pase.getEventosValidos().size());

        ArrayList<Evento> nuevos = new ArrayList<>();
        nuevos.add(evento2);
        pase.setEventosValidos(nuevos);

        assertEquals(1, pase.getEventosValidos().size());
        assertSame(evento2, pase.getEventosValidos().get(0));
    }

    /**
     * Given: Un PaseTemporada con eventos válidos y tiquetes iniciales asociados
     *        al mismo cliente.
     * When:  Se consultan contarTiquetes() y estaVacio(), se agrega un nuevo tiquete
     *        válido y luego se elimina, y finalmente se configuran precio, cargo
     *        de servicio y cuota de impresión para calcular el costo total y el
     *        valor promedio de los tiquetes.
     * Then:  El conteo de tiquetes y el estado vacío/no vacío deben ser coherentes
     *        con las operaciones de agregar y eliminar; el costo total debe coincidir
     *        con precio + cargo + cuota de impresión; y el valor promedio debe ser
     *        el esperado a partir de los precios pagados de los tiquetes.
     */
    @Test
    @DisplayName("Herencia: agregar/eliminar tiquetes, contar, vacío, costo total y promedio")
    void herenciaComportamientosBase() {
        ArrayList<Evento> evs = new ArrayList<>();
        evs.add(evento1);
        evs.add(evento2);

        ArrayList<Tiquete> tiqs = new ArrayList<>();
        tiqs.add(cliente.precompraIndividualNumerada(1, this.evento1, loc1, 20));
        tiqs.add(cliente.precompraIndividualNumerada(1, this.evento2, loc2, 20));

        PaseTemporada pase = new PaseTemporada(9, cliente, 300.0, evs, tiqs);

        assertEquals(2, pase.contarTiquetes());
        assertFalse(pase.estaVacio());

        Tiquete nuevo = cliente.precompraIndividualNumerada(1, this.evento1, loc1, 10);
        pase.agregarTiquete(nuevo);
        assertEquals(3, pase.contarTiquetes());

        pase.eliminarTiquete(nuevo);
        assertEquals(2, pase.contarTiquetes());

        pase.setCargoServicio(50.0);
        pase.setCuotaImpresion(10.0);
        pase.setPrecio(300.0);
        assertEquals(360.0, pase.calcularCostoTotal(), 1e-6);

        assertEquals(210, pase.calcularValorPromedio());
    }

    /**
     * Given: Un PaseTemporada con al menos un tiquete válido perteneciente al dueño
     *        del pase.
     * When:  Se intenta agregar un tiquete nulo y luego un tiquete cuyo dueño es
     *        distinto al dueño del pase.
     * Then:  En ambos casos, agregarTiquete debe lanzar IllegalArgumentException,
     *        ya que no se permiten tiquetes nulos ni tiquetes de otro dueño dentro
     *        del mismo pase de temporada.
     */
    @Test
    @DisplayName("agregarTiquete: lanza por nulo o por dueño distinto")
    void agregarTiqueteValidaciones() {
        ArrayList<Tiquete> base = new ArrayList<>();
        base.add(cliente.precompraIndividualNumerada(1, this.evento1, loc1, 20));
        PaseTemporada pase = new PaseTemporada(10, cliente, 100.0, new ArrayList<>(), base);

        assertThrows(IllegalArgumentException.class, () -> pase.agregarTiquete(null));

        Cliente otro = new Cliente("otro", "pwd", "Otro", 200);
        Tiquete ajeno = new Individual(2, 80.0, evento1, loc1, otro, true, 2);
        assertThrows(IllegalArgumentException.class, () -> pase.agregarTiquete(ajeno));
    }
}

