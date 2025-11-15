package testUsuarios;

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
import Tiquetes.Tiquete;
import MarketPlace.OfertaMP;
import MarketPlace.Puja;

class testClienteMarketPlace {

    private Cliente comprador;
    private Cliente vendedor;
    private Venue v;
    private Organizador o;
    private Administrador a;
    private Evento eventoFuturo;
    private Localidad loc;
    private ArrayList<Tiquete> t;

    @BeforeEach
    void setUp() {
        comprador = new Cliente("cli", "pwd", "Cliente", 100);
        vendedor = new Cliente("vend", "pwd", "Vendedor", 200);

        v = new Venue(1, "venue", "Cll 1#1", 1000, "");
        o = new Organizador("org", "123", "ORG", 0, 0, "");
        a = new Administrador("adm", "123", "ADM", 0);

        eventoFuturo = new Evento("Evento MP",10,v, LocalDateTime.now().plusDays(10),o, "Religioso", a);
        loc = new Localidad("A", true, 100, 50.0, eventoFuturo, 40.0, 30.0, 80.0);
        Individual tiq = vendedor.precompraIndividualNumerada(1, eventoFuturo, loc, 10);
        t = new ArrayList<Tiquete>();
        t.add(tiq);
    }


    /**
     * Given: Un cliente comprador con saldo suficiente y una oferta de marketplace
     *        sin pujas previas (lista de pujas vacía).
     * When:  El comprador llama a crearPuja() con una OfertaMP válida y un monto
     *        positivo mayor que el precio base implícito.
     * Then:  Debe crearse una nueva Puja asociada al comprador, el saldo del cliente
     *        debe disminuir exactamente en el valor de la puja, y la puja debe
     *        agregarse a la lista de pujas de la oferta.
     */
    @Test
    @DisplayName("crearPuja: crea puja, descuenta saldo y la agrega a la oferta")
    void crearPujaValidaActualizaSaldoYLista() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        assertTrue(oferta.getPujas().isEmpty());

        comprador.setSaldo(200.0);
        double monto = 80.0;

        comprador.crearPuja(1, oferta, monto);

        assertEquals(120.0, comprador.getSaldo(), 1e-6);
        assertEquals(1, oferta.getPujas().size());
        Puja creada = oferta.getPujas().get(0);
        assertEquals(monto, creada.getMonto(), 1e-6);
        assertEquals(comprador, creada.getComprador());
    }

    /**
     * Given: Un cliente que desea crear una puja.
     * When:  Llama a crearPuja(), pasando una oferta nula como parámetro.
     * Then:  El método debe lanzar IllegalArgumentException porque la OfertaMP
     *        no puede ser null.
     */
    @Test
    @DisplayName("crearPuja: lanza si la OfertaMP es null")
    void crearPujaOfertaNullLanza() {
        comprador.setSaldo(100.0);
        assertThrows(IllegalArgumentException.class,
                () -> comprador.crearPuja(1, null, 50.0));
    }

    /**
     * Given: Una oferta de marketplace con al menos una puja existente y un comprador
     *        que intenta pujar de nuevo.
     * When:  El comprador llama a crearPuja() con un monto menor o igual al monto
     *        de la puja más reciente registrada en la oferta.
     * Then:  El método debe lanzar IllegalArgumentException, ya que el nuevo monto
     *        debe ser estrictamente mayor al de la última puja registrada.
     */
    @Test
    @DisplayName("crearPuja: lanza si el monto no supera a la puja más reciente")
    void crearPujaMontoNoMayorALaUltimaLanza() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        Puja existente = new Puja(1, oferta, comprador, 100.0, "Pendiente", LocalDateTime.now());
        ArrayList<Puja> pujas = new ArrayList<>();
        pujas.add(existente);
        oferta.setPujas(pujas);

        comprador.setSaldo(200.0);

        assertThrows(IllegalArgumentException.class,
                () -> comprador.crearPuja(2, oferta, 100.0));
        assertThrows(IllegalArgumentException.class,
                () -> comprador.crearPuja(3, oferta, 80.0));
    }

    /**
     * Given: Una oferta con boletas cuyo dueño actual es el vendedor, y una puja
     *        válida realizada por un comprador con un monto positivo.
     * When:  El vendedor llama a aceptarPuja(p, oferta) sobre esa puja y oferta.
     * Then:  La puja debe cambiar su estado a "Aceptada", la oferta debe pasar a
     *        estado "Cerrada", las boletas deben transferir la propiedad al comprador
     *        de la puja y el saldo del vendedor debe incrementarse en el monto de la
     *        puja.
     */
    @Test
    @DisplayName("aceptarPuja: actualiza estados, transfiere boletas y suma saldo al vendedor")
    void aceptarPujaValidaCambiaEstadosTransfiereYActualizaSaldo() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        comprador.setSaldo(200.0);
        double monto = 90.0;
        comprador.crearPuja(1, oferta, monto);
        Puja p = oferta.getPujas().get(0);

        assertEquals(0.0, vendedor.getSaldo(), 1e-6);
        vendedor.aceptarPuja(p, oferta);

        assertEquals("Aceptada", p.getEstado());
        assertEquals("Cerrada", oferta.getEstado());
        for (Tiquete t : oferta.getBoletas()) {
            assertEquals(p.getComprador(), t.getDueno());
        }
        assertEquals(monto, vendedor.getSaldo(), 1e-6);
    }

    /**
     * Given: Una oferta de marketplace asociada a un vendedor específico y una puja
     *        válida realizada por un comprador.
     * When:  Un cliente distinto al vendedor intenta aceptar la puja llamando a
     *        aceptarPuja() con esa oferta.
     * Then:  El método debe lanzar IllegalArgumentException, ya que solo el vendedor
     *        de la oferta puede aceptar una puja sobre ella.
     */
    @Test
    @DisplayName("aceptarPuja: lanza si el cliente no es el vendedor de la oferta")
    void aceptarPujaVendedorDistintoLanza() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        comprador.setSaldo(200.0);
        comprador.crearPuja(1, oferta, 80.0);
        Puja p = oferta.getPujas().get(0);

        Cliente otro = new Cliente("otro", "pwd", "Otro", 300);
        assertThrows(IllegalArgumentException.class,
                () -> otro.aceptarPuja(p, oferta));
    }

    /**
     * Given: Una oferta de marketplace y una puja válida registrada en dicha oferta,
     *        cuyo comprador es el cliente actual.
     * When:  El comprador llama a cancelarPuja(p, oferta) sobre esa puja.
     * Then:  El estado de la puja debe cambiar a "Cancelada", la puja debe ser
     *        removida de la lista de pujas de la oferta, y el saldo del comprador
     *        debe aumentar en el monto de la puja, devolviéndolo al valor que tenía
     *        antes de crear la puja.
     */
    @Test
    @DisplayName("cancelarPuja: cambia estado, devuelve saldo y remueve la puja de la oferta")
    void cancelarPujaValidaActualizaEstadoSaldoYLista() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        comprador.setSaldo(200.0);
        double monto = 70.0;
        double saldoInicial = comprador.getSaldo();

        comprador.crearPuja(1, oferta, monto);
        Puja p = oferta.getPujas().get(0);

        assertEquals(saldoInicial - monto, comprador.getSaldo(), 1e-6);

        comprador.cancelarPuja(p, oferta);

        assertEquals("Cancelada", p.getEstado());
        assertFalse(oferta.getPujas().contains(p));
        assertEquals(saldoInicial, comprador.getSaldo(), 1e-6);
    }

    /**
     * Given: Una puja asociada a un comprador específico sobre una oferta de
     *        marketplace.
     * When:  Un cliente distinto al comprador original intenta cancelar la puja
     *        llamando a cancelarPuja(p, oferta).
     * Then:  El método debe lanzar IllegalArgumentException, ya que solo el comprador
     *        que realizó la puja puede cancelarla.
     */
    @Test
    @DisplayName("cancelarPuja: lanza si el cliente no es el comprador de la puja")
    void cancelarPujaCompradorDistintoLanza() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        comprador.setSaldo(200.0);
        comprador.crearPuja(1, oferta, 60.0);
        Puja p = oferta.getPujas().get(0);

        Cliente otroComprador = new Cliente("otro", "pwd", "Otro", 300);
        assertThrows(IllegalArgumentException.class,
                () -> otroComprador.cancelarPuja(p, oferta));
    }

    /**
     * Given: Un cliente vendedor y una lista no nula de tiquetes que desea ofrecer
     *        en el marketplace, junto con un precio base y una fecha de cierre.
     * When:  El vendedor llama a crearOfertaMP(...) con esos parámetros válidos.
     * Then:  Debe crearse una nueva OfertaMP en estado "Activa", añadirse al listado
     *        de ofertas activas del marketplace, y la lista de ofertas activas
     *        devuelta por consultarOfertasActivas() debe incrementarse en uno.
     */
    @Test
    @DisplayName("crearOfertaMP: registra una nueva oferta activa en el marketplace")
    void crearOfertaMPValidaRegistraOferta() {
        ArrayList<Tiquete> boletas = new ArrayList<>();
        boletas.add(new Individual(1, 50.0, eventoFuturo, loc, vendedor, true, 1));

        int antes = vendedor.consultarOfertasActivas().size();

        vendedor.crearOfertaMP(1, boletas, 50.0, LocalDateTime.now().plusDays(5));

        ArrayList<OfertaMP> activas = vendedor.consultarOfertasActivas();
        assertEquals(antes + 1, activas.size());
    }

    /**
     * Given: Un cliente vendedor que intenta crear una oferta de marketplace.
     * When:  Llama a crearOfertaMP(...) pasando la lista de boletas como null.
     * Then:  El método debe lanzar IllegalArgumentException, ya que la colección
     *        de tiquetes ofrecidos no puede ser nula.
     */
    @Test
    @DisplayName("crearOfertaMP: lanza si la lista de boletas es null")
    void crearOfertaMPBoletasNullLanza() {
        assertThrows(IllegalArgumentException.class,
                () -> vendedor.crearOfertaMP(1, null, 50.0, LocalDateTime.now().plusDays(5)));
    }

    /**
     * Given: Una oferta de marketplace creada por un vendedor y presente en el
     *        listado de ofertas activas.
     * When:  Ese mismo vendedor llama a cancelarOfertaMP(oferta).
     * Then:  El estado de la oferta debe cambiar a "Cancelado por vendedor" y la
     *        oferta debe ser removida de la lista de ofertas activas devuelta por
     *        consultarOfertasActivas().
     */
    @Test
    @DisplayName("cancelarOfertaMP: cambia estado y remueve la oferta de las activas")
    void cancelarOfertaMPVendedorCorrectoElimina() {
        ArrayList<Tiquete> boletas = new ArrayList<>();
        boletas.add(new Individual(1, 50.0, eventoFuturo, loc, vendedor, true, 1));

        vendedor.crearOfertaMP(1, boletas, 50.0, LocalDateTime.now().plusDays(5));
        ArrayList<OfertaMP> activas = vendedor.consultarOfertasActivas();
        OfertaMP ofertamp = activas.get(activas.size() - 1);

        vendedor.cancelarOfertaMP(ofertamp);

        assertEquals("Cancelado por vendedor", ofertamp.getEstado());
        assertFalse(vendedor.consultarOfertasActivas().contains(ofertamp));
    }

    /**
     * Given: Una oferta de marketplace perteneciente a un vendedor específico.
     * When:  Un cliente distinto intenta cancelar esa oferta llamando a
     *        cancelarOfertaMP(oferta).
     * Then:  El método debe lanzar IllegalArgumentException, ya que solo el vendedor
     *        asociado a la oferta puede cancelarla.
     */
    @Test
    @DisplayName("cancelarOfertaMP: lanza si el cliente no es el vendedor")
    void cancelarOfertaMPVendedorDistintoLanza() {
        ArrayList<Tiquete> boletas = new ArrayList<>();
        boletas.add(new Individual(1, 50.0, eventoFuturo, loc, vendedor, true, 1));

        vendedor.crearOfertaMP(1, boletas, 50.0, LocalDateTime.now().plusDays(5));
        ArrayList<OfertaMP> activas = vendedor.consultarOfertasActivas();
        OfertaMP ofertamp = activas.get(activas.size() - 1);

        assertThrows(IllegalArgumentException.class,
                () -> comprador.cancelarOfertaMP(ofertamp));
    }

    /**
     * Given: Un marketplace con cierto conjunto de ofertas activas gestionadas a
     *        través de crearOfertaMP(...).
     * When:  Un cliente invoca consultarOfertasActivas().
     * Then:  El método debe retornar la misma colección de ofertas activas que
     *        mantiene el marketplace, permitiendo observar los cambios después de
     *        crear o cancelar ofertas.
     */
    @Test
    @DisplayName("consultarOfertasActivas: devuelve la lista de ofertas activas del marketplace")
    void consultarOfertasActivasDevuelveActivas() {
        int antes = vendedor.consultarOfertasActivas().size();

        ArrayList<Tiquete> boletas = new ArrayList<>();
        boletas.add(new Individual(1, 50.0, eventoFuturo, loc, vendedor, true, 1));
        vendedor.crearOfertaMP(1, boletas, 50.0, LocalDateTime.now().plusDays(5));

        ArrayList<OfertaMP> activas = vendedor.consultarOfertasActivas();
        assertEquals(antes + 1, activas.size());
    }

    /**
     * Given: Una OfertaMP con una lista interna de pujas ya configurada.
     * When:  Un cliente llama a consultarPujasOfertaMP(oferta) sobre dicha oferta.
     * Then:  El método debe retornar la misma colección de pujas interna de la
     *        oferta, permitiendo inspeccionar el número y contenido de pujas
     *        asociadas a esa OfertaMP.
     */
    @Test
    @DisplayName("consultarPujasOfertaMP: retorna la lista interna de pujas de la oferta")
    void consultarPujasOfertaMPDevuelveListaPujas() {
        OfertaMP oferta = new OfertaMP(0, vendedor, t, 100.0, "Activa", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new ArrayList<Puja>());
        Puja p1 = new Puja(1, oferta, comprador, 60.0, "Pendiente", LocalDateTime.now());
        Puja p2 = new Puja(2, oferta, comprador, 80.0, "Pendiente", LocalDateTime.now());

        ArrayList<Puja> pujas = new ArrayList<>(Arrays.asList(p1, p2));
        oferta.setPujas(pujas);

        ArrayList<Puja> resultado = comprador.consultarPujasOfertaMP(oferta);

        assertEquals(2, resultado.size());
        assertSame(p1, resultado.get(0));
        assertSame(p2, resultado.get(1));
    }
}
