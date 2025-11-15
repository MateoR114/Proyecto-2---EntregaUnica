package testUsuarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import Eventos.Evento;
import Eventos.Localidad;
import Eventos.Venue;
import Tiquetes.Individual;
import Tiquetes.Tiquete;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;
import MarketPlace.OfertaMP;
import MarketPlace.marketPlace;

public class testAdminMarketPlace {

    private Administrador admin;
    private Cliente vendedor;
    private Venue v;
    private Organizador o;
    private Evento eventoFuturo;
    private Localidad loc;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Administrador", 1);
        vendedor = new Cliente("vend", "pwd", "Vendedor", 200);

        v = new Venue(1, "Venue MP", "Cll 1 #1", 1000, "");
        o = new Organizador("org", "123", "ORG", 0, 0, "");
        Administrador aAux = new Administrador("aux", "123", "AUX", 2);

        eventoFuturo = new Evento( "Evento MP", 10, v, LocalDateTime.now().plusDays(5),o, "Concierto", aAux);
        loc = new Localidad("A", true, 100, 50.0, eventoFuturo, 40.0, 30.0, 80.0);

        marketPlace mp = marketPlace.getInstance();
        mp.setActivas(new ArrayList<>());
        mp.setLog(new ArrayList<>());
    }

    /**
     * Given: Una oferta de marketplace en estado "Activa", registrada dentro de la lista
     *        de ofertas activas del singleton marketPlace.
     * When:  El administrador invoca cancelarOfertaMP(omp) sobre esa oferta.
     * Then:  El estado de la oferta debe cambiar a "Cancelado por admin" y la oferta
     *        debe ser removida de la lista de ofertas activas del marketplace.
     */
    @Test
    @DisplayName("cancelarOfertaMP: cambia estado a 'Cancelado por admin' y remueve de activas")
    void cancelarOfertaMP_ActualizaEstadoYRemueveDeActivas() {
        ArrayList<Tiquete> boletas = new ArrayList<>();
        boletas.add(new Individual(1, 50.0, eventoFuturo, loc, vendedor, true, 1));

        OfertaMP omp = new OfertaMP(1,vendedor,boletas,50.0,"Activa",LocalDateTime.now(),LocalDateTime.now().plusDays(2),new ArrayList<>());

        marketPlace mp = marketPlace.getInstance();
        ArrayList<OfertaMP> activas = mp.getActivas();
        activas.add(omp);
        mp.setActivas(activas);

        assertTrue(mp.getActivas().contains(omp));
        assertEquals("Activa", omp.getEstado());

        admin.cancelarOfertaMP(omp);

        assertEquals("Cancelado por admin", omp.getEstado());
        assertFalse(mp.getActivas().contains(omp));
    }

    /**
     * Given: Un marketplace con un log de ofertas en el que se han registrado varias
     *        instancias de OfertaMP (independiente de su estado actual).
     * When:  El administrador invoca consultarLogOfertas().
     * Then:  El método debe retornar la misma colección de ofertas almacenada en el
     *        log del marketplace, conteniendo todas las ofertas previamente registradas.
     */
    @Test
    @DisplayName("consultarLogOfertas: retorna la lista de ofertas registrada en el log")
    void consultarLogOfertas_DevuelveLogDelMarketplace() {
        // Arrange
        marketPlace mp = marketPlace.getInstance();

        ArrayList<Tiquete> boletas1 = new ArrayList<>();
        boletas1.add(new Individual(1, 50.0, eventoFuturo, loc, vendedor, true, 1));
        OfertaMP o1 = new OfertaMP(101,vendedor,boletas1, 50.0,"Activa",LocalDateTime.now(),LocalDateTime.now().plusDays(3),new ArrayList<>());

        ArrayList<Tiquete> boletas2 = new ArrayList<>();
        boletas2.add(new Individual(2, 80.0, eventoFuturo, loc, vendedor, true, 2));
        OfertaMP o2 = new OfertaMP(102,vendedor,boletas2,80.0,"Cancelado por vendedor",LocalDateTime.now().minusDays(1),LocalDateTime.now().plusDays(1),new ArrayList<>());

        ArrayList<OfertaMP> log = new ArrayList<>();
        log.add(o1);
        log.add(o2);
        mp.setLog(log);

        ArrayList<OfertaMP> resultado = admin.consultarLogOfertas();

        assertEquals(2, resultado.size());
        assertSame(o1, resultado.get(0));
        assertSame(o2, resultado.get(1));
    }
}
