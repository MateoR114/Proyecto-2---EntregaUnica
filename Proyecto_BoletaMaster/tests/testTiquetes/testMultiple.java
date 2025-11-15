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
import java.util.Arrays;

import Tiquetes.Individual;
import Tiquetes.Multiple;
import Tiquetes.Tiquete;



public class testMultiple {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento;
    private Localidad localidad;
    private Cliente cliente;


    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Administrador", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.1);
        admin.setCuotaEmisionGlobal(2.5);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(10, "Teatro Nacional", "Calle 123", 1000, "Sin pirotecnia");
        venue.setAprobado(true);

        evento = new Evento("Rock Fest", 1001, venue, LocalDateTime.now().plusDays(5), organizador, "Concierto", admin);
        localidad = evento.crearLocalidad("VIP", true, 100, 200.0, 150.0, 120.0, 250.0);

        cliente = new Cliente("user1", "pass", "Cliente Uno", 100);
    }

    private ArrayList<Tiquete> crearTiquetes(Cliente dueno, int cantidad) {
        ArrayList<Tiquete> tiquetes = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            tiquetes.add(new Individual(i, 100.0, evento, localidad, dueno, true, i));
        }
        return tiquetes;
    }

    /**
     * Given: Un cliente válido, un evento, una localidad y una lista de tiquetes
     *        pertenecientes a ese cliente con tamaño conocido.
     * When:  Se construye un paquete Multiple con código, precio total y la lista
     *        de tiquetes creada.
     * Then:  El paquete debe inicializarse correctamente: precio igual al suministrado,
     *        cantidad y lista de tiquetes coherentes, dueño asociado, estado "Activo"
     *        y la propiedad de transferible en true.
     */
    @Test
    @DisplayName("Constructor: inicializa correctamente el paquete múltiple")
    void constructorInicializaCorrectamente() {
        ArrayList<Tiquete> tiquetes = crearTiquetes(cliente, 3);

        Multiple paquete = new Multiple(1, cliente, 300.0, tiquetes);

        assertEquals(0, paquete.getCodigo());
        assertEquals(300.0, paquete.getPrecio(), 1e-6);
        assertEquals(3, paquete.getCantidad());
        assertEquals(3, paquete.getTiquetesIncluidos().size());
        assertSame(cliente, paquete.getDueno());
        assertEquals("Activo", paquete.getEstado());
        assertTrue(paquete.isTransferible());
    }

    /**
     * Given: Un paquete Multiple creado con una lista de tiquetes de tamaño conocido.
     * When:  Se llama al método getCantidad() sobre ese paquete.
     * Then:  El valor retornado debe coincidir con el número de tiquetes incluidos
     *        en el paquete.
     */
    @Test
    @DisplayName("getCantidad devuelve el número correcto de tiquetes incluidos")
    void getCantidadDevuelveCorrectamente() {
        ArrayList<Tiquete> tiquetes = crearTiquetes(cliente, 5);
        Multiple paquete = new Multiple(2, cliente, 500.0, tiquetes);

        assertEquals(5, paquete.getCantidad());
    }

    /**
     * Given: Un paquete Multiple con un precio base definido y cargos adicionales
     *        de servicio e impresión configurados.
     * When:  Se invoca calcularCostoTotal() sobre el paquete.
     * Then:  El costo total debe corresponder a la suma del precio base más el
     *        cargo de servicio y la cuota de impresión.
     */
    @Test
    @DisplayName("calcularCostoTotal suma correctamente precio + cargos + cuotas de impresión")
    void calcularCostoTotalCorrectamente() {
        ArrayList<Tiquete> tiquetes = crearTiquetes(cliente, 2);
        Multiple paquete = new Multiple(3, cliente, 200.0, tiquetes);

        paquete.setCargoServicio(50.0);
        paquete.setCuotaImpresion(20.0);

        assertEquals(270.0, paquete.calcularCostoTotal(), 1e-6);
    }

    /**
     * Given: Un paquete Multiple inicializado con una lista de tiquetes.
     * When:  Se agrega un nuevo tiquete, y luego se elimina el mismo tiquete del paquete.
     * Then:  El tamaño de la lista de tiquetes y el conteo reportado deben aumentar
     *        al agregar y disminuir al eliminar, manteniendo la consistencia entre
     *        la lista interna y el método contarTiquetes().
     */
    @Test
    @DisplayName("Agregar y eliminar tiquetes mantienen consistencia en la lista")
    void agregarYEliminarTiquetes() {
        ArrayList<Tiquete> tiquetes = crearTiquetes(cliente, 2);
        Multiple paquete = new Multiple(4, cliente, 200.0, tiquetes);

        Tiquete nuevo = new Individual(3, 150.0, evento, localidad, cliente, true, 3);
        paquete.agregarTiquete(nuevo);

        assertEquals(3, paquete.getTiquetesIncluidos().size());
        assertEquals(3, paquete.contarTiquetes());

        paquete.eliminarTiquete(nuevo);
        assertEquals(2, paquete.getTiquetesIncluidos().size());
    }

    /**
     * Given: Un paquete Multiple válido con un dueño específico y al menos un tiquete.
     * When:  Se intenta agregar un tiquete nulo y un tiquete cuyo dueño es diferente
     *        al dueño del paquete.
     * Then:  En ambos casos, agregarTiquete debe lanzar IllegalArgumentException,
     *        ya que no se permiten tiquetes nulos ni de otro dueño dentro del mismo paquete.
     */
    @Test
    @DisplayName("agregarTiquete lanza excepciones por tiquete nulo o dueño diferente")
    void agregarTiqueteInvalido() {
        ArrayList<Tiquete> tiquetes = crearTiquetes(cliente, 1);
        Multiple paquete = new Multiple(5, cliente, 100.0, tiquetes);

        assertThrows(IllegalArgumentException.class, () -> paquete.agregarTiquete(null));

        Cliente otro = new Cliente("otro", "pwd", "Otro Cliente", 200);
        Tiquete distinto = new Individual(2, 80.0, evento, localidad, otro, true, 4);
        assertThrows(IllegalArgumentException.class, () -> paquete.agregarTiquete(distinto));
    }

    /**
     * Given: Un paquete Multiple con una lista de tiquetes cuyos precios pagados
     *        son conocidos y distintos.
     * When:  Se llama a calcularValorPromedio() para obtener el valor promedio
     *        de los precios de los tiquetes incluidos.
     * Then:  El valor retornado debe ser igual al promedio aritmético de dichos
     *        precios.
     */
    @Test
    @DisplayName("calcularValorPromedio retorna el promedio del precio de los tiquetes")
    void calcularValorPromedio() {
        ArrayList<Tiquete> tiquetes = new ArrayList<>();
        tiquetes.add(new Individual(1, 100.0, evento, localidad, cliente, true, 1));
        tiquetes.add(new Individual(2, 200.0, evento, localidad, cliente, true, 2));
        tiquetes.add(new Individual(3, 300.0, evento, localidad, cliente, true, 3));

        Multiple paquete = new Multiple(6, cliente, 600.0, tiquetes);

        assertEquals(200.0, paquete.calcularValorPromedio(), 1e-6);
    }

    /**
     * Given: Un paquete Multiple creado a partir de una precompra, al cual se le
     *        puede ajustar la lista interna de tiquetes.
     * When:  Se reemplaza la lista de tiquetes por una lista vacía y luego se agrega
     *        un tiquete, consultando estaVacio() y contarTiquetes() en cada estado.
     * Then:  Con la lista vacía, el paquete debe reportarse como vacío y con conteo
     *        cero; tras agregar un tiquete, ya no debe estar vacío y el conteo debe
     *        reflejar el número de tiquetes incluidos (1).
     */
    @Test
    @DisplayName("estaVacio y contarTiquetes reflejan el estado del paquete")
    void estaVacioYContarTiquetes() {
        ArrayList<Tiquete> tiquetes = new ArrayList<>();
        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, evento, localidad, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );
        p.setTiquetesIncluidos(tiquetes);

        assertTrue(p.estaVacio());
        assertEquals(0, p.contarTiquetes());

        p.agregarTiquete(new Individual(1, 50.0, evento, localidad, cliente, true, 1));
        assertFalse(p.estaVacio());
        assertEquals(1, p.contarTiquetes());
    }
}
