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
import java.util.ArrayList;


public class testVenue {

    private Venue venue;
    private Organizador o;
    private Administrador a;
    private Evento eventoFuturoActivo;

    @BeforeEach
    void setUp() {
        venue = new Venue(1, "Coliseo Mayor", "Calle 123 #45-67", 1000, "No pirotecnia");
    	o= new Organizador("org", "123", "ORG", 0, 0, "");
    	a =new Administrador("adm", "123", "ADM",0);
    	
    	eventoFuturoActivo = new Evento("a", 0, venue, LocalDateTime.of(2026, 1, 1, 9, 0), o, "Religioso", a);
    }


    /**
     * Given: Un venue recién creado con id, nombre, dirección, capacidad y
     *        restricciones definidos, pero sin localidades asociadas.
     * When:  Se consultan sus getters inmediatamente después de la construcción.
     * Then:  Debe conservar los valores pasados al constructor, estar marcado como
     *        no aprobado, tener una lista de localidades no nula y dicha lista debe
     *        estar vacía.
     */
    @Test
    @DisplayName("Constructor: inicia sin localidades y no aprobado")
    void constructorInicializa() {
        assertEquals(1, venue.getId());
        assertEquals("Coliseo Mayor", venue.getNombre());
        assertEquals("Calle 123 #45-67", venue.getUbicación());
        assertEquals(1000, venue.getCapacidad());
        assertEquals("No pirotecnia", venue.getRestricciones());
        assertFalse(venue.estaAprobado());
        assertNotNull(venue.getLocalidades());
        assertTrue(venue.getLocalidades().isEmpty());
    }

    /**
     * Given: Un venue ya inicializado con valores por defecto.
     * When:  Se actualizan id, nombre, dirección, capacidad, restricciones y estado
     *        de aprobación mediante sus setters.
     * Then:  Los getters deben reflejar exactamente los nuevos valores asignados y
     *        el venue debe quedar marcado como aprobado.
     */
    @Test
    @DisplayName("Setters: actualizan id, nombre, dirección, capacidad, restricciones y aprobado")
    void settersActualizan() {
        venue.setId(99);
        venue.setNombre("Arena Central");
        venue.setDireccion("Av. Siempre Viva 742");
        venue.setCapacidad(2000);
        venue.setRestricciones("Sin restricciones");
        venue.setAprobado(true);

        assertEquals(99, venue.getId());
        assertEquals("Arena Central", venue.getNombre());
        assertEquals("Av. Siempre Viva 742", venue.getUbicación());
        assertEquals(2000, venue.getCapacidad());
        assertEquals("Sin restricciones", venue.getRestricciones());
        assertTrue(venue.estaAprobado());
    }

    /**
     * Given: Un venue sin localidades o con localidades previas, y una nueva lista
     *        de localidades con capacidades conocidas.
     * When:  Se llama a setLocalidades() pasando esta nueva lista.
     * Then:  La lista interna de localidades del venue debe ser reemplazada por la
     *        nueva lista, y la capacidad total calculada debe equivaler a la suma
     *        de las capacidades de las localidades nuevas (en este caso, 400).
     */
    @Test
    @DisplayName("setLocalidades: reemplaza la lista completa")
    void setLocalidadesReemplaza() {
    	//( String nombre, boolean numerada, int capacidad, double precioBase, Evento evento, double precioBasePaquetesxUnidad, double precioPaseTemporadaxUnidad, double precioPaseDeluxexUnidad)
        ArrayList<Localidad> nuevas = new ArrayList<>();
        nuevas.add(new Localidad("VIP", true, 100, 150.0,eventoFuturoActivo, 120.0, 110.0, 200.0));
        nuevas.add(new Localidad("General", false, 300, 50.0,eventoFuturoActivo, 40.0, 35.0, 90.0));

        venue.setLocalidades(nuevas);

        assertEquals(2, venue.getLocalidades().size());
        assertEquals(400, venue.calcularCapacidadTotal());
    }


    /**
     * Given: Un venue sin localidades y dos localidades con capacidades que, sumadas,
     *        no superan la capacidad total del venue ni están duplicadas.
     * When:  Se agregan ambas localidades usando agregarLocalidad().
     * Then:  Ambas deben quedar registradas en la lista de localidades, la capacidad
     *        total calculada debe ser la suma de sus capacidades y el venue debe
     *        reportar que tiene localidades.
     */
    @Test
    @DisplayName("agregarLocalidad: agrega cuando no existe y no supera la capacidad")
    void agregarLocalidadOk() {
        Localidad vip = new Localidad("VIP", true, 200, 200.0,eventoFuturoActivo, 150.0, 120.0, 250.0);
        Localidad general = new Localidad("General", false, 500, 80.0, eventoFuturoActivo,60.0, 50.0, 120.0);

        venue.agregarLocalidad(vip);
        venue.agregarLocalidad(general);

        assertEquals(2, venue.getLocalidades().size());
        assertEquals(700, venue.calcularCapacidadTotal());
        assertTrue(venue.tieneLocalidades());
    }

    /**
     * Given: Un venue disponible para agregar localidades.
     * When:  Se intenta agregar una localidad nula mediante agregarLocalidad(null).
     * Then:  El método debe lanzar IllegalArgumentException indicando que la
     *        localidad no puede ser nula.
     */
    @Test
    @DisplayName("agregarLocalidad: lanza si la localidad es nula")
    void agregarLocalidadNulaLanza() {
        assertThrows(IllegalArgumentException.class, () -> venue.agregarLocalidad(null));
    }

    /**
     * Given: Un venue y una localidad construida que ya ha sido agregada al venue.
     * When:  Se intenta agregar la misma instancia de localidad una segunda vez.
     * Then:  agregarLocalidad() debe lanzar IllegalArgumentException indicando que
     *        la localidad ya existe en el venue y no puede duplicarse.
     */
    @Test
    @DisplayName("agregarLocalidad: lanza si la localidad ya existe en el venue")
    void agregarLocalidadDuplicadaLanza() {
        Localidad platea = new Localidad("Platea", true, 300, 120.0,eventoFuturoActivo, 100.0, 90.0, 180.0);

        venue.agregarLocalidad(platea);
        assertThrows(IllegalArgumentException.class, () -> venue.agregarLocalidad(platea));
    }

    /**
     * Given: Un venue con capacidad máxima fija y una localidad ya agregada que
     *        consume parte de esa capacidad.
     * When:  Se intenta agregar una nueva localidad cuya capacidad, sumada a la
     *        ya existente, excede la capacidad máxima del venue.
     * Then:  agregarLocalidad() debe lanzar IllegalArgumentException con un mensaje
     *        relacionado con la capacidad, y la capacidad total del venue no debe
     *        cambiar tras el intento fallido.
     */
    @Test
    @DisplayName("agregarLocalidad: lanza si excede la capacidad total del venue")
    void agregarLocalidadExcedeCapacidad() {
        venue.agregarLocalidad(new Localidad("A", false, 800, 70.0, eventoFuturoActivo,55.0, 45.0, 100.0));
        // Intentar agregar una de 300 superaría 1000
        Localidad b = new Localidad("B", true, 300, 200.0,eventoFuturoActivo, 150.0, 120.0, 250.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> venue.agregarLocalidad(b));
        assertTrue(ex.getMessage().toLowerCase().contains("capacidad"));
        assertEquals(800, venue.calcularCapacidadTotal());
    }


    /**
     * Given: Un venue al que se le han agregado dos localidades específicas.
     * When:  Se elimina una de las localidades existentes y luego se intenta eliminar
     *        una localidad distinta que nunca fue agregada.
     * Then:  La localidad existente debe eliminarse, reduciendo el número de
     *        localidades y su capacidad total; al intentar eliminar una localidad
     *        inexistente, eliminarLocalidad() debe lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("eliminarLocalidad: elimina existente; lanzar si no existe")
    void eliminarLocalidadOkYError() {
        Localidad l1 = new Localidad("L1", false, 100, 50.0,eventoFuturoActivo, 40.0, 35.0, 90.0);
        Localidad l2 = new Localidad("L2", true, 200, 120.0,eventoFuturoActivo, 100.0, 90.0, 180.0);
        venue.agregarLocalidad(l1);
        venue.agregarLocalidad(l2);

        venue.eliminarLocalidad(l1);
        assertEquals(1, venue.getLocalidades().size());
        assertEquals(200, venue.calcularCapacidadTotal());

        Localidad inexistente = new Localidad("X", false, 50, 30.0,eventoFuturoActivo, 25.0, 20.0, 60.0);
        assertThrows(IllegalArgumentException.class, () -> venue.eliminarLocalidad(inexistente));
    }


    /**
     * Given: Un venue sin localidades iniciales.
     * When:  Se consulta calcularCapacidadTotal() antes de agregar localidades y
     *        después de agregar varias localidades con capacidades conocidas.
     * Then:  Sin localidades, la capacidad total debe ser 0; tras agregar varias
     *        localidades, la capacidad total debe ser la suma de las capacidades de
     *        todas ellas.
     */
    @Test
    @DisplayName("calcularCapacidadTotal: suma capacidades; sin localidades es 0")
    void calcularCapacidadTotalVarios() {
        assertEquals(0, venue.calcularCapacidadTotal());

        venue.agregarLocalidad(new Localidad("A", false, 123, 10,eventoFuturoActivo, 8, 7, 20));
        venue.agregarLocalidad(new Localidad("B", true, 77, 10,eventoFuturoActivo, 8, 7, 20));
        venue.agregarLocalidad(new Localidad("C", false, 300, 10, eventoFuturoActivo, 8, 7, 20));

        assertEquals(123 + 77 + 300, venue.calcularCapacidadTotal());
    }

    /**
     * Given: Un venue recién creado sin localidades asociadas.
     * When:  Se consulta tieneLocalidades() antes y después de agregar una localidad.
     * Then:  Inicialmente debe retornar false; tras agregar al menos una localidad
     *        debe retornar true.
     */
    @Test
    @DisplayName("tieneLocalidades: true si hay al menos una; false en vacío")
    void tieneLocalidadesOk() {
        assertFalse(venue.tieneLocalidades());
        venue.agregarLocalidad(new Localidad("SoloUna", false, 10, 10,eventoFuturoActivo, 8, 7, 20));
        assertTrue(venue.tieneLocalidades());
    }
}
