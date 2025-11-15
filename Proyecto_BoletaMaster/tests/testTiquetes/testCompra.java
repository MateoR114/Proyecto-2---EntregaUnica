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

import Tiquetes.Compra;
import Tiquetes.Individual;
import Tiquetes.Multiple;
import Tiquetes.PaqueteTiquetes;
import Tiquetes.Tiquete;


public class testCompra {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento;
    private Localidad localidad;
    private Cliente cliente;

    private ArrayList<Tiquete> tiquetesBase;
    private ArrayList<PaqueteTiquetes> paquetesBase;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Administrador", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.10);
        admin.setCuotaEmisionGlobal(2.0);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(10, "Teatro Central", "Calle 123", 1500, "Sin pirotecnia");
        venue.setAprobado(true);

        evento = new Evento("Rock Fest", 1001, venue, LocalDateTime.now().plusDays(7), organizador, "Concierto", admin);
        localidad = evento.crearLocalidad("General", false, 500, 80.0, 60.0, 50.0, 120.0);

        cliente = new Cliente("user1", "pwd", "Cliente Uno", 101);

        tiquetesBase = new ArrayList<>();
        tiquetesBase.add(new Individual(1, 100.0, evento, localidad, cliente, true, -1));
        tiquetesBase.add(new Individual(2, 120.0, evento, localidad, cliente, true, -1));

        ArrayList<Tiquete> tiqsPaquete = new ArrayList<>();
        tiqsPaquete.add(new Individual(3, 90.0, evento, localidad, cliente, true, -1));
        tiqsPaquete.add(new Individual(4, 110.0, evento, localidad, cliente, true, -1));

        paquetesBase = new ArrayList<>();
        paquetesBase.add(new Multiple(500, cliente, 180.0, tiqsPaquete));
    }


    /**
     * Given: Una compra con un cliente existente, listas base de tiquetes y paquetes,
     *         y una fecha específica de compra.
     * When:  Se construye una nueva instancia de Compra con esos datos.
     * Then:  La compra queda asociada al cliente dado, con un idCompra > 0, la fecha
     *         de compra establecida correctamente, el valor total igual a 300.0 y las
     *         listas de tiquetes y paquetes contienen los elementos esperados (2 tiquetes y 1 paquete).
     */
    @Test
    @DisplayName("Constructor: inicializa dueño, id (>0), fecha, valor y listas")
    void constructorInicializa() {
        LocalDateTime ahora = LocalDateTime.now();
        Compra c = new Compra(cliente, new ArrayList<>(tiquetesBase), new ArrayList<>(paquetesBase), 300.0, ahora);

        assertSame(cliente, c.getDueno());
        assertTrue(c.getIdCompra() > 0);
        assertEquals(ahora, c.getFechaCompra());
        assertEquals(300.0, c.getvalorCompra(), 1e-6);
        assertEquals(2, c.getTiquetesComprados().size());
        assertEquals(1, c.getPaquetes().size());
    }

    /**
     * Given: Una compra ya creada con cliente, fecha, valor y listas de tiquetes
     *         y paquetes iniciales.
     * When:  Se actualizan el id de compra, la fecha, el valor, el dueño, la lista
     *         de tiquetes y la lista de paquetes mediante sus setters.
     * Then:  Los getters reflejan los nuevos valores asignados (id=999, fecha actualizada,
     *         valor 777.77, nuevo dueño) y las listas de tiquetes y paquetes contienen
     *         exactamente los nuevos elementos configurados (1 tiquete y 1 paquete).
     */
    @Test
    @DisplayName("Setters: idCompra, fecha, valor, dueño, listas de tiquetes y paquetes")
    void settersBasicos() {
        Compra c = new Compra(cliente, new ArrayList<>(tiquetesBase), new ArrayList<>(paquetesBase),250.0, LocalDateTime.now());

        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        c.setIdCompra(999);
        c.setFechaCompra(nuevaFecha);
        c.setvalorCompral(777.77);
        Cliente nuevo = new Cliente("user2", "pwd", "Cliente Dos", 102);
        c.setDueno(nuevo);

        ArrayList<Tiquete> nuevosTiqs = new ArrayList<>();
        nuevosTiqs.add(new Individual(10, 50.0, evento, localidad, nuevo, true, -1));
        c.setTiquetesComprados(nuevosTiqs);

        ArrayList<Tiquete> tiqsPaquete = new ArrayList<>();
        tiqsPaquete.add(new Individual(11, 60.0, evento, localidad, nuevo, true, -1));
        ArrayList<PaqueteTiquetes> nuevosPaqs = new ArrayList<>();
        nuevosPaqs.add(new Multiple(600, nuevo, 60.0, tiqsPaquete));
        c.setPaquetesComprados(nuevosPaqs);

        assertEquals(999, c.getIdCompra());
        assertEquals(nuevaFecha, c.getFechaCompra());
        assertEquals(777.77, c.getvalorCompra(), 1e-6);
        assertSame(nuevo, c.getDueno());
        assertEquals(1, c.getTiquetesComprados().size());
        assertEquals(1, c.getPaquetes().size());
    }

    /**
     * Given: Una compra inicializada con una lista de tiquetes y paquetes ya existentes.
     * When:  Se agrega un nuevo tiquete no duplicado a la compra, luego se intenta agregar
     *         el mismo tiquete nuevamente y finalmente se intenta agregar un tiquete nulo.
     * Then:  El nuevo tiquete se añade correctamente a la lista (la lista crece y contiene
     *         el tiquete), el intento de agregar el mismo tiquete no genera duplicados
     *         (el tamaño de la lista no cambia) y al intentar agregar un tiquete nulo
     *         se lanza una IllegalArgumentException.
     */
    @Test
    @DisplayName("agregarTiquete: agrega no duplicados; lanza si es nulo")
    void agregarTiqueteComportamiento() {
        Compra c = new Compra(cliente, new ArrayList<>(tiquetesBase), new ArrayList<>(paquetesBase),0.0, LocalDateTime.now());

        Tiquete nuevo = new Individual(99, 70.0, evento, localidad, cliente, true, -1);
        c.agregarTiquete(nuevo);
        assertTrue(c.getTiquetesComprados().contains(nuevo));
        int sizeDespues = c.getTiquetesComprados().size();

        c.agregarTiquete(nuevo);
        assertEquals(sizeDespues, c.getTiquetesComprados().size());

        assertThrows(IllegalArgumentException.class, () -> c.agregarTiquete(null));
    }

    /**
     * Given: Una compra inicializada con una lista de tiquetes y paquetes ya existentes.
     * When:  Se agrega un nuevo paquete de tiquetes no duplicado a la compra, luego se
     *         intenta agregar el mismo paquete nuevamente y finalmente se intenta agregar
     *         un paquete nulo.
     * Then:  El nuevo paquete se añade correctamente a la lista (la lista crece y contiene
     *         el paquete), el intento de agregar el mismo paquete no genera duplicados
     *         (el tamaño de la lista no cambia) y al intentar agregar un paquete nulo
     *         se lanza una IllegalArgumentException.
     */
    @Test
    @DisplayName("agregarPaquete: agrega no duplicados; lanza si es nulo")
    void agregarPaqueteComportamiento() {
        Compra c = new Compra(cliente, new ArrayList<>(tiquetesBase), new ArrayList<>(paquetesBase),0.0, LocalDateTime.now());

        ArrayList<Tiquete> tiqs = new ArrayList<>();
        tiqs.add(new Individual(77, 80.0, evento, localidad, cliente, true, -1));
        PaqueteTiquetes paqueteNuevo = new Multiple(700, cliente, 80.0, tiqs);

        c.agregarPaquete(paqueteNuevo);
        assertTrue(c.getPaquetes().contains(paqueteNuevo));
        int sizeDespues = c.getPaquetes().size();

        c.agregarPaquete(paqueteNuevo);
        assertEquals(sizeDespues, c.getPaquetes().size());

        assertThrows(IllegalArgumentException.class, () -> c.agregarPaquete(null));
    }

    /**
     * Given: Una compra inicializada con al menos un tiquete y un paquete asociados.
     * When:  Se elimina primero un tiquete existente y luego se intenta eliminarlo de nuevo,
     *         y de forma análoga se elimina un paquete existente y se intenta eliminarlo de nuevo.
     * Then:  Los elementos existentes se remueven correctamente de sus listas (ya no están
     *         contenidos en ellas) y los intentos posteriores de eliminar los mismos elementos
     *         no producen errores ni modifican el estado (las listas permanecen sin dichos elementos).
     */
    @Test
    @DisplayName("Eliminar tiquete y paquete: remueven si existen y no fallan si no están")
    void eliminarTiqueteYPaquete() {
        Compra c = new Compra(cliente, new ArrayList<>(tiquetesBase), new ArrayList<>(paquetesBase),0.0, LocalDateTime.now());

        Tiquete t = c.getTiquetesComprados().get(0);
        c.eliminarTiquete(t);
        assertFalse(c.getTiquetesComprados().contains(t));

        c.eliminarTiquete(t);
        assertFalse(c.getTiquetesComprados().contains(t));

        PaqueteTiquetes p = c.getPaquetes().get(0);
        c.eliminarPaquete(p);
        assertFalse(c.getPaquetes().contains(p));

        c.eliminarPaquete(p);
        assertFalse(c.getPaquetes().contains(p));
    }
}
