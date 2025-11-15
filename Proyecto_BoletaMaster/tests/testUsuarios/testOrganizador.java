package testUsuarios;
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
import Usuarios.ReporteFinanciero;

import java.time.LocalDateTime;
import java.util.List;

class testOrganizador {

    private Venue v;
    private Organizador org;
    private Administrador a;


    @BeforeEach
    void setUp() {
       	v = new Venue(0, "venue", "Cll 1#1", 1000, "");
       	org= new Organizador("org", "123", "ORG", 0, 0, "Org SA");
       	a =new Administrador("adm", "123", "ADM",0);

    }

    /**
     * Given: Un organizador recién creado con saldo, ventas y ganancia iniciales en cero,
     *        sin eventos ni reportes asociados y con id y nombre de organización definidos.
     * When:  Se consultan sus getters inmediatamente después de la construcción.
     * Then:  El saldo, las ventas de localidades y la ganancia sin recargos deben ser 0.0,
     *        las listas de eventos y reportes deben estar inicializadas y vacías, y los
     *        datos básicos (id y nombre de la organización) deben coincidir con lo dado.
     */
    @Test
    @DisplayName("Constructor: listas vacías y acumuladores en cero (saldo, ventas, ganancia)")
    void constructorInicializa() {
        assertEquals(0.0, org.getSaldo());
        assertEquals(0.0, org.getVentasLocalidad());
        assertEquals(0.0, org.getGananciaSinRecargos());

        assertNotNull(org.getEventos());
        assertTrue(org.getEventos().isEmpty());

        assertNotNull(org.getReportesGenerados());
        assertTrue(org.getReportesGenerados().isEmpty());

        assertEquals(0, org.getId());
        assertEquals("Org SA", org.getNombreOrganización());
    }

    /**
     * Given: Un venue aprobado y una fecha futura válida, además de un organizador
     *        asociado y un administrador con la configuración requerida.
     * When:  El organizador invoca crearEvento(...) con esos parámetros.
     * Then:  Se debe crear un evento no nulo, agregarse a la lista de eventos del
     *        organizador y mantener la asociación correcta de nombre, organizador y venue.
     */
    @Test
    @DisplayName("crearEvento: con venue aprobado y fecha futura agrega el evento")
    void crearEventoOk() {
        LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);

        assertNotNull(e);
        assertEquals(1, org.getEventos().size());
        assertSame(e, org.getEventos().get(0));
        assertEquals("Concierto", e.getNombre());
        assertEquals(org, e.getOrganizador());
        assertEquals(v, e.getVenue());
    }

    /**
     * Given: Un venue no aprobado y una fecha futura.
     * When:  El organizador intenta crear un evento en ese venue mediante crearEvento().
     * Then:  El método debe lanzar IllegalArgumentException, impidiendo la creación
     *        del evento al no cumplir la precondición de venue aprobado.
     */
    @Test
    @DisplayName("crearEvento: lanza si venue no aprobado")
    void crearEventoErrores() {
        LocalDateTime futura = LocalDateTime.now().plusDays(1);
        
        v.setAprobado(false);
        assertThrows(IllegalArgumentException.class, () ->
                org.crearEvento("X", 1, v, futura, "Tipo", a)
        );
    }

    /**
     * Given: Un evento creado por el organizador actual y registrado en su lista de eventos.
     * When:  El organizador llama a cancelarEvento(e, motivo) sobre ese mismo evento.
     * Then:  El estado del evento debe cambiar a "cancelado" y continuar asociado al
     *        organizador.
     */
    @Test
    @DisplayName("cancelarEvento: solo si pertenece al organizador; marca cancelado")
    void cancelarEventoOk() {

    	LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);
        org.cancelarEvento(e, "Motivo X");

        assertEquals(e.getEstado(), "cancelado");
    }

    /**
     * Given: Un organizador sin relación con cierto evento (evento ajeno).
     * When:  El organizador intenta cancelar ese evento usando cancelarEvento(...).
     * Then:  El método debe lanzar IllegalArgumentException, ya que solo se pueden
     *        cancelar eventos que pertenezcan al organizador.
     */
    @Test
    @DisplayName("cancelarEvento: lanza si el evento no pertenece al organizador")
    void cancelarEventoAjenoLanza() {
        Evento ajeno = new Evento("a", 0, v, LocalDateTime.of(2026, 1, 1, 9, 0), null, "Religioso", a);

        assertThrows(IllegalArgumentException.class, () -> org.cancelarEvento(ajeno, "no es mío"));
    }

    // ===== modificarEvento =====

    /**
     * Given: Un evento creado por el organizador con un nombre, id y fecha iniciales.
     * When:  El organizador invoca modificarEvento(...) cambiando el nombre, el id
     *        y la fecha del evento.
     * Then:  Los campos del evento deben actualizarse correctamente y los getters
     *        deben reflejar los nuevos valores.
     */
    @Test
    @DisplayName("modificarEvento: actualiza nombre e id/fecha si pertenece")
    void modificarEventoOk() {
        LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(10);
        org.modificarEvento(e, "NuevoNombre", 301, nuevaFecha);

        assertEquals("NuevoNombre", e.getNombre());
        assertEquals(301, e.getId());
        assertEquals(nuevaFecha, e.getFecha());
    }

    /**
     * Given: Un evento que no fue creado por el organizador actual (evento ajeno).
     * When:  El organizador intenta modificarlo usando modificarEvento(...).
     * Then:  Debe lanzarse IllegalArgumentException porque el organizador solo
     *        puede modificar eventos que le pertenezcan.
     */
    @Test
    @DisplayName("modificarEvento: lanza si evento no pertenece")
    void modificarEventoAjenoLanza() {
    	Evento ajeno = new Evento("a", 0, v, LocalDateTime.of(2026, 1, 1, 9, 0), null, "Religioso", a);

        assertThrows(IllegalArgumentException.class, () ->
                org.modificarEvento(ajeno, "NN", 401, LocalDateTime.now().plusDays(12)));
    }

    /**
     * Given: Un evento creado por el organizador y sin localidades iniciales.
     * When:  El organizador llama a crearLocalidad(...) sobre ese evento con parámetros
     *        válidos de nombre, numeración, capacidad y precios.
     * Then:  Se debe crear una localidad no nula, con los campos configurados de
     *        acuerdo a los parámetros, y la localidad debe agregarse a la lista de
     *        localidades del evento.
     */
    @Test
    @DisplayName("crearLocalidad: válida si el evento es del organizador")
    void crearLocalidadOk() {
    	LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);
        Localidad loc = org.crearLocalidad(e, "VIP", true, 100, 150.0, 120.0, 110.0, 200.0);

        assertNotNull(loc);
        assertEquals("VIP", loc.getNombre());
        assertEquals(150.0, loc.getPrecioBase(), 1e-6);
        assertTrue(e.getLocalidades().contains(loc));
    }

    /**
     * Given: Un evento que no pertenece al organizador actual.
     * When:  El organizador intenta crear una localidad sobre ese evento usando
     *        crearLocalidad(...).
     * Then:  Debe lanzarse IllegalArgumentException, ya que solo se pueden crear
     *        localidades en eventos que pertenezcan al organizador.
     */
    @Test
    @DisplayName("crearLocalidad: lanza si el evento no pertenece")
    void crearLocalidadEventoAjenoLanza() {
        Evento ajeno = new Evento("Ajeno", 600, v,
                LocalDateTime.now().plusDays(8), org, "X", a);

        assertThrows(IllegalArgumentException.class, () ->
                org.crearLocalidad(ajeno, "General", false, 500, 50.0, 35.0, 30.0, 90.0));
    }

    /**
     * Given: Un evento creado por el organizador y una localidad asociada a ese evento
     *        con un precio base inicial.
     * When:  El organizador invoca modificarLocalidad(...) cambiando el precio base.
     * Then:  El precio base de la localidad debe actualizarse correctamente y el
     *        getter correspondiente debe reflejar el nuevo valor.
     */
    @Test
    @DisplayName("modificarLocalidad: actualiza precio base")
    void modificarLocalidadOk() {
    	LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);
        Localidad loc = org.crearLocalidad(e, "Platea", false, 200, 80.0, 60.0, 50.0, 120.0);

        org.modificarLocalidad(e, loc, 99.9);

        assertEquals(99.9, loc.getPrecioBase(), 1e-6);
    }

    /**
     * Given: Un evento del organizador con una localidad válida y sin ofertas iniciales.
     * When:  El organizador crea una oferta con porcentaje de descuento en rango,
     *        fechas de vigencia válidas y asociada a la localidad usando crearOferta(...).
     * Then:  La oferta resultante no debe ser nula, su porcentaje debe coincidir con
     *        el indicado y debe quedar registrada en la lista de ofertas del evento.
     */
    @Test
    @DisplayName("crearOferta: valida pertenencia y porcentaje en rango [0,100]")
    void crearOfertaOk() {
    	LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);
        Localidad loc = org.crearLocalidad(e, "General", false, 1000, 40.0, 30.0, 25.0, 70.0);

        Oferta of = org.crearOferta(e, 15.0, LocalDateTime.now(), LocalDateTime.now().plusDays(10), loc);

        assertNotNull(of);
        assertEquals(15.0, of.getPorcentajeDescuento());
        assertTrue(e.getOfertas().contains(of));
    }

    /**
     * Given: Un evento del organizador con una localidad válida.
     * When:  Se intenta crear una oferta con porcentaje de descuento fuera del rango
     *        [0, 100] y luego se intenta crear una oferta sobre un evento ajeno.
     * Then:  En los casos de porcentaje inválido, crearOferta() debe lanzar
     *        IllegalArgumentException; igualmente, al intentar crear una oferta sobre
     *        un evento que no pertenece al organizador, también debe lanzarse
     *        IllegalArgumentException.
     */
    @Test
    @DisplayName("crearOferta: lanza si porcentaje inválido o evento ajeno")
    void crearOfertaLanza() {
    	LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);
        Localidad loc = org.crearLocalidad(e, "General", false, 1000, 40.0, 30.0, 25.0, 70.0);

        assertThrows(IllegalArgumentException.class, () ->
                org.crearOferta(e, -1.0, LocalDateTime.now(), LocalDateTime.now().plusDays(1), loc));
        assertThrows(IllegalArgumentException.class, () ->
                org.crearOferta(e, 101.0, LocalDateTime.now(), LocalDateTime.now().plusDays(1), loc));

        Evento ajeno = new Evento("Ajeno", 900, v,
                LocalDateTime.now().plusDays(3), org, "X", a);
        assertThrows(IllegalArgumentException.class, () ->
                org.crearOferta(ajeno, 10.0, LocalDateTime.now(), LocalDateTime.now().plusDays(1), loc));
    }

    /**
     * Given: Un evento creado por el organizador, inicialmente sin reportes financieros
     *        asociados al organizador.
     * When:  El organizador invoca generarReporteFinanciero(e) y luego obtiene la lista
     *        de reportes tanto internamente como mediante listarReportesFinancieros().
     * Then:  Debe crearse un reporte no nulo, agregarse a la lista interna de reportes
     *        del organizador, y listarReportesFinancieros() debe devolver una copia de
     *        esa lista (modificar la copia no afecta la lista original).
     */
    @Test
    @DisplayName("generarReporteFinanciero: agrega a la lista y listar devuelve copia")
    void generarReporteFinancieroOk() {
    	LocalDateTime futura = LocalDateTime.now().plusDays(3);
        v.setAprobado(true);
        Evento e = org.crearEvento("Concierto", 101, v, futura, "Concierto", a);

        ReporteFinanciero r = org.generarReporteFinanciero(e);

        assertNotNull(r);
        assertEquals(1, org.getReportesGenerados().size());

        List<ReporteFinanciero> copia = org.listarReportesFinancieros();
        assertEquals(1, copia.size());
        copia.clear();
        assertEquals(1, org.getReportesGenerados().size());
    }
}
