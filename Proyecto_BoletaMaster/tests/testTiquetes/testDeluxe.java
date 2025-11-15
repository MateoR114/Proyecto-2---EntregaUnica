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

import Tiquetes.Deluxe;
import Tiquetes.Individual;
import Tiquetes.Tiquete;


public class testDeluxe {

    private Administrador admin;
    private Organizador organizador;
    private Venue venue;
    private Evento evento;
    private Localidad localidad;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Administrador", 1);
        admin.establecerCargoServicioPorTipo("Concierto", 0.15);
        admin.setCuotaEmisionGlobal(3.0);

        organizador = new Organizador("org", "pwd", "Organizador", 10, 0.0, "Org S.A.");
        venue = new Venue(10, "Auditorio Mayor", "Carrera 7 #45", 1500, "Sin fuegos");
        venue.setAprobado(true);

        evento = new Evento("Opera Gala", 5001, venue, LocalDateTime.now().plusDays(8), organizador, "Concierto", admin);
        localidad = evento.crearLocalidad("Platea", true, 200, 300.0, 250.0, 200.0, 400.0);

        cliente = new Cliente("cli", "pass", "Cliente Deluxe", 100);
    }

    private Individual crearTiquete(int codigo, double precio, int asiento) {
        return new Individual(codigo, precio, evento, localidad, cliente, true, asiento);
    }

    /**
     * Given: Una lista de tiquetes válida, un cliente existente y valores correctos
     *        para precio y beneficios del paquete Deluxe.
     * When:  Se crea una instancia de Deluxe con esos datos válidos.
     * Then:  El objeto queda correctamente inicializado: precio asignado, beneficios
     *        iguales al texto dado, estado "Activo", dueño establecido, cargo de
     *        servicio calculado, cuota de impresión según número de tiquetes,
     *        y la lista contiene los tiquetes suministrados.
     */
    @Test
    @DisplayName("Constructor: crea un paquete Deluxe válido con beneficios y tiquetes")
    void constructorValido() {
        ArrayList<Tiquete> tiquetes = new ArrayList<>();
        tiquetes.add(crearTiquete(1, 100.0, 1));
        tiquetes.add(crearTiquete(2, 200.0, 2));

        Deluxe deluxe = new Deluxe(1, cliente, 500.0, "Acceso VIP y cena exclusiva", tiquetes);

        assertEquals(0, deluxe.getCodigo());
        assertEquals(500.0, deluxe.getPrecio(), 1e-6);
        assertEquals("Acceso VIP y cena exclusiva", deluxe.getBeneficios());
        assertEquals(2, deluxe.getTiquetesIncluidos().size());
        assertSame(cliente, deluxe.getDueno());
        assertEquals("Activo", deluxe.getEstado());
        assertTrue(deluxe.getCargoServicio() >= 0);
        assertEquals(3.0 * 2, deluxe.getCuotaImpresion(), 1e-6);
    }

    /**
     * Given: Una lista de tiquetes válida y un cliente existente, pero descripciones
     *        de beneficios vacías, compuestas solo por espacios en blanco o nulas.
     * When:  Se intenta construir un objeto Deluxe con dichas descripciones inválidas.
     * Then:  El constructor debe lanzar IllegalArgumentException en todos los casos,
     *        ya que los beneficios no pueden ser vacíos ni nulos.
     */
    @Test
    @DisplayName("Constructor: lanza excepción si la descripción de beneficios está vacía o nula")
    void constructorInvalido() {
        ArrayList<Tiquete> lista = new ArrayList<>();
        lista.add(crearTiquete(1, 100.0, 1));

        assertThrows(IllegalArgumentException.class,
                () -> new Deluxe(2, cliente, 300.0, "", lista));

        assertThrows(IllegalArgumentException.class,
                () -> new Deluxe(3, cliente, 300.0, "   ", lista));

        assertThrows(IllegalArgumentException.class,
                () -> new Deluxe(4, cliente, 300.0, null, lista));
    }

    /**
     * Given: Un objeto Deluxe válido con beneficios iniciales establecidos.
     * When:  Se actualiza el texto de beneficios con un valor válido y luego
     *        se intenta actualizar con textos vacíos, con solo espacios o nulos.
     * Then:  La actualización válida debe reflejarse en el getter, mientras que
     *        las actualizaciones inválidas deben lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("setBeneficios actualiza correctamente y lanza excepción por texto vacío o nulo")
    void setBeneficiosValidoYInvalido() {
        ArrayList<Tiquete> lista = new ArrayList<>();
        lista.add(crearTiquete(1, 100.0, 1));

        Deluxe deluxe = new Deluxe(5, cliente, 400.0, "Incluye cóctel de bienvenida", lista);
        assertEquals("Incluye cóctel de bienvenida", deluxe.getBeneficios());

        deluxe.setBeneficios("Cena gourmet incluida");
        assertEquals("Cena gourmet incluida", deluxe.getBeneficios());

        assertThrows(IllegalArgumentException.class, () -> deluxe.setBeneficios(""));
        assertThrows(IllegalArgumentException.class, () -> deluxe.setBeneficios("   "));
        assertThrows(IllegalArgumentException.class, () -> deluxe.setBeneficios(null));
    }

    /**
     * Given: Un paquete Deluxe válido asociado a un cliente y con una lista de tiquetes.
     * When:  Se consulta si es transferible y luego se intenta activar la transferibilidad.
     * Then:  El método esTransferible() siempre debe retornar false — incluso si se llama
     *        a setTransferible(true) — ya que por política los paquetes Deluxe no pueden
     *        ser transferidos.
     */
    @Test
    @DisplayName("esTransferible siempre retorna false por política del sistema")
    void esTransferibleSiempreFalse() {
        ArrayList<Tiquete> lista = new ArrayList<>();
        lista.add(crearTiquete(1, 120.0, 5));

        Deluxe deluxe = new Deluxe(6, cliente, 500.0, "Acceso al lounge privado", lista);

        assertFalse(deluxe.esTransferible());
        deluxe.setTransferible(true);
        assertFalse(deluxe.esTransferible());
    }

    /**
     * Given: Un objeto Deluxe correctamente creado con una lista inicial de tiquetes.
     * When:  Se agrega un tiquete, luego se elimina, y posteriormente se modifican
     *        cargo de servicio, cuota de impresión y precio base para calcular el costo.
     * Then:  agregarTiquete aumenta el conteo; eliminarTiquete lo reduce; y el cálculo
     *        del costo total debe ser consistente con la suma de precio + cargo +
     *        cuota de impresión.
     */
    @Test
    @DisplayName("Métodos heredados: agregar, eliminar, contar y costo total")
    void metodosHeredados() {
        ArrayList<Tiquete> lista = new ArrayList<>();
        lista.add(crearTiquete(1, 100.0, 1));
        lista.add(crearTiquete(2, 150.0, 2));

        Deluxe deluxe = new Deluxe(7, cliente, 300.0, "Incluye parqueadero y refrigerio", lista);

        Tiquete nuevo = crearTiquete(3, 200.0, 3);
        deluxe.agregarTiquete(nuevo);
        assertEquals(3, deluxe.contarTiquetes());

        deluxe.eliminarTiquete(nuevo);
        assertEquals(2, deluxe.contarTiquetes());

        deluxe.setCargoServicio(50.0);
        deluxe.setCuotaImpresion(10.0);
        deluxe.setPrecio(300.0);
        assertEquals(360.0, deluxe.calcularCostoTotal(), 1e-6);
    }
}
