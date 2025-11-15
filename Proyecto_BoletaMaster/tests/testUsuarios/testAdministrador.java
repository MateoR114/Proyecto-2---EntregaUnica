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
import Tiquetes.Multiple;
import Tiquetes.Tiquete;

class testAdministrador {

    private Administrador admin;
    private Cliente cliente;
    private Venue v;
    private Organizador o;
    private Administrador a;
    private Evento eventoFuturoActivo;
    private Localidad loc;

    @BeforeEach
    void setUp() {
        admin = new Administrador("admin", "pwd", "Administrador", 1);
        cliente = new Cliente("cli", "pwd", "Cliente", 100);
       	v = new Venue(0, "venue", "Cll 1#1", 1000, "");
    	o= new Organizador("org", "123", "ORG", 0, 0, "");
    	a =new Administrador("adm", "123", "ADM",0);
    	
    	
    	eventoFuturoActivo = new Evento("a", 0, v, LocalDateTime.of(2026, 1, 1, 9, 0), o, "Religioso", a);
    	loc = new Localidad("A", true, 1000, 5, eventoFuturoActivo, 5, 5, 5);
    }

    /**
     * Given: Un administrador recién creado sin ventas ni ganancias registradas y sin
     *        cargos configurados ni solicitudes de reembolso en curso.
     * When:  Se consultan sus acumuladores (ganancia, ventas del día, ventas por
     *        organizador, cuota de emisión global) y las colecciones internas.
     * Then:  Todos los acumuladores deben iniciar en 0.0, los mapas/listas de cargos
     *        y solicitudes deben estar inicializados y vacíos.
     */
    @Test
    @DisplayName("Constructor: inicializa acumuladores en 0 y mapas vacíos")
    void constructorInicializa() {
        assertEquals(0.0, admin.getGananciaSobreCargos(), 1e-6);
        assertEquals(0.0, admin.getVentasDia(), 1e-6);
        assertEquals(0.0, admin.getVentasOrganizador(), 1e-6);
        assertEquals(0.0, admin.getCuotaEmisionGlobal(), 1e-6);
        assertNotNull(admin.getCargosPorTipoEvento());
        assertTrue(admin.getCargosPorTipoEvento().isEmpty());
        assertNotNull(admin.getSolicitudesReembolsoTiquete());
        assertTrue(admin.getSolicitudesReembolsoTiquete().isEmpty());
        assertNotNull(admin.getSolicitudesReembolsoPaquetes());
        assertTrue(admin.getSolicitudesReembolsoPaquetes().isEmpty());
    }

    /**
     * Given: Un administrador con ventas por organizador inicialmente en cero.
     * When:  Se establece un valor de ventas no negativo y luego se intenta establecer
     *        un valor negativo mediante setVentasOrganizador().
     * Then:  El valor no negativo debe persistir y ser retornado por el getter, mientras
     *        que el intento de asignar un valor negativo debe lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("setVentasOrganizador: acepta valores >= 0 y rechaza negativos")
    void setVentasOrganizadorValidaciones() {
        admin.setVentasOrganizador(123.45);
        assertEquals(123.45, admin.getVentasOrganizador(), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> admin.setVentasOrganizador(-1.0));
    }

    /**
     * Given: Un administrador con ventas del día inicialmente en cero.
     * When:  Se asigna un valor de ventas del día no negativo y luego se intenta
     *        asignar un valor negativo mediante setVentasDia().
     * Then:  El valor no negativo debe guardarse correctamente, y el intento de
     *        asignar un valor negativo debe lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("setVentasDia: acepta valores >= 0 y rechaza negativos")
    void setVentasDiaValidaciones() {
        admin.setVentasDia(999.9);
        assertEquals(999.9, admin.getVentasDia(), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> admin.setVentasDia(-0.01));
    }

    /**
     * Given: Un administrador con ganancia sobre cargos inicialmente en cero.
     * When:  Se establece una ganancia no negativa y luego se intenta establecer un
     *        valor negativo mediante setGananciaSobreCargos().
     * Then:  El valor no negativo debe persistir correctamente, y el intento de
     *        asignar un valor negativo debe provocar IllegalArgumentException.
     */
    @Test
    @DisplayName("setGananciaSobreCargos: acepta >= 0 y rechaza negativos")
    void setGananciaSobreCargosValidaciones() {
        admin.setGananciaSobreCargos(10.0);
        assertEquals(10.0, admin.getGananciaSobreCargos(), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> admin.setGananciaSobreCargos(-1.0));
    }

    /**
     * Given: Un administrador con cuota de emisión global inicialmente en 0.0.
     * When:  Se configuran cuotas de emisión global válidas (no negativas) mediante
     *        setCuotaEmisionGlobal() y establecerCuotaEmisionGlobal(), y luego se
     *        intenta establecer valores negativos.
     * Then:  Las asignaciones no negativas deben reflejarse en el getter, mientras que
     *        las asignaciones negativas deben lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("setCuotaEmisionGlobal y establecerCuotaEmisionGlobal: validan no negativo")
    void cuotasEmisionGlobal() {
        admin.setCuotaEmisionGlobal(2.5);
        assertEquals(2.5, admin.getCuotaEmisionGlobal(), 1e-6);

        admin.establecerCuotaEmisionGlobal(3.5);
        assertEquals(3.5, admin.getCuotaEmisionGlobal(), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> admin.setCuotaEmisionGlobal(-1));
        assertThrows(IllegalArgumentException.class, () -> admin.establecerCuotaEmisionGlobal(-1));
    }

    /**
     * Given: Un administrador sin cargos configurados inicialmente.
     * When:  Se establece un cargo de servicio válido para un tipo de evento y luego
     *        se invocan establecerCargoServicioPorTipo() con nombre vacío o null, así
     *        como con porcentajes negativos; también se consulta un tipo inexistente.
     * Then:  El cargo válido debe persistir y ser retornado por el getter; las
     *        configuraciones con nombre inválido o porcentaje negativo deben lanzar
     *        IllegalArgumentException; para un tipo inexistente, el cargo retornado
     *        debe ser 0.0.
     */
    @Test
    @DisplayName("establecerCargoServicioPorTipo: valida entradas y persiste porcentaje")
    void establecerCargoServicioPorTipo() {
        admin.establecerCargoServicioPorTipo("Teatro", 0.10);
        assertEquals(0.10, admin.getCargoServicioParaEvento("Teatro"), 1e-6);

        assertThrows(IllegalArgumentException.class, () -> admin.establecerCargoServicioPorTipo("", 0.1));
        assertThrows(IllegalArgumentException.class, () -> admin.establecerCargoServicioPorTipo(null, 0.1));
        assertThrows(IllegalArgumentException.class, () -> admin.establecerCargoServicioPorTipo("Cine", -0.01));
        assertEquals(0.0, admin.getCargoServicioParaEvento("NoExiste"), 1e-6);
    }

    /**
     * Given: Un venue sin estado de aprobación definido y un administrador válido.
     * When:  El administrador aprueba y luego desaprueba el venue mediante
     *        aprobarVenue() y desaprobarVenue(), y además se intenta aprobar o
     *        desaprobar un venue null.
     * Then:  El venue debe cambiar correctamente su estado de aprobado/no aprobado
     *        según las llamadas; las operaciones con venue null deben lanzar
     *        IllegalArgumentException.
     */
    @Test
    @DisplayName("aprobarVenue/desaprobarVenue: cambia el estado y valida null")
    void aprobarDesaprobarVenue() {
        admin.aprobarVenue(v);
        assertTrue(v.estaAprobado());

        admin.desaprobarVenue(v);
        assertFalse(v.estaAprobado());

        assertThrows(IllegalArgumentException.class, () -> admin.aprobarVenue(null));
        assertThrows(IllegalArgumentException.class, () -> admin.desaprobarVenue(null));
    }

    /**
     * Given: Un evento futuro activo asociado a un administrador y un venue válidos.
     * When:  El administrador invoca cancelarEvento(eventoFuturoActivo) y luego se
     *        intenta cancelar un evento null.
     * Then:  El evento debe cambiar su estado a "cancelado por administrador" y
     *        cancelarEvento(null) debe lanzar IllegalArgumentException.
     */
    @Test
    @DisplayName("cancelarEvento: invoca cancelarPorAdministrador y valida null")
    void cancelarEvento() {
        assertEquals("Activo",eventoFuturoActivo.getEstado());
        admin.cancelarEvento(eventoFuturoActivo);
        assertEquals("cancelado por administrador",eventoFuturoActivo.getEstado());

        assertThrows(IllegalArgumentException.class, () -> admin.cancelarEvento(null));
    }

    /**
     * Given: Un cliente con un tiquete individual válido para un evento futuro activo,
     *        que solicita reembolso por calamidad y queda registrado en el mapa de
     *        solicitudes de tiquetes del administrador auxiliar 'a'.
     * When:  El administrador principal aprueba el reembolso mediante
     *        aprobarReembolsoPorCalamidadTiquete(cliente, tiquete).
     * Then:  El tiquete debe quedar marcado como reembolsado, el saldo del cliente debe
     *        incrementarse con el valor correspondiente y la solicitud debe eliminarse
     *        del mapa de solicitudes de reembolso de tiquetes del administrador.
     */
    @Test
    @DisplayName("aprobarReembolsoPorCalamidadTiquete: marca reembolsado, abona saldo y saca del mapa")
    void aprobarReembolsoTiquete() {
        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 20);
        cliente.solicitarReembolsoPorCalamidadTiquete(t, a);

        admin.aprobarReembolsoPorCalamidadTiquete(cliente, t);

        assertTrue(t.isReembolsado());
        assertEquals(10.0, cliente.getSaldo(), 1e-6);
        assertFalse(admin.getSolicitudesReembolsoTiquete().containsKey(cliente));
    }

    /**
     * Given: Un cliente con un tiquete y una solicitud de reembolso por calamidad ya
     *        registrada en el administrador auxiliar 'a'.
     * When:  El administrador principal llama a rechazarReembolsoPorCalamidadTiquete(cliente).
     * Then:  La solicitud correspondiente debe eliminarse del mapa de solicitudes de
     *        reembolso de tiquetes, el saldo del cliente debe permanecer en 0.0 y el
     *        tiquete no debe marcarse como reembolsado.
     */
    @Test
    @DisplayName("rechazarReembolsoPorCalamidadTiquete: elimina del mapa sin cambiar saldo")
    void rechazarReembolsoTiquete() {
    	Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 20);
        cliente.solicitarReembolsoPorCalamidadTiquete(t, a);

        admin.rechazarReembolsoPorCalamidadTiquete(cliente);

        assertFalse(admin.getSolicitudesReembolsoTiquete().containsKey(cliente));
        assertEquals(0.0, cliente.getSaldo(), 1e-6);
        assertFalse(t.isReembolsado());
    }

    /**
     * Given: Un cliente con un paquete Multiple válido, cuyos tiquetes pertenecen a
     *        un evento futuro activo, y una solicitud de reembolso por calamidad
     *        registrada en el administrador auxiliar 'a'.
     * When:  El administrador principal aprueba el reembolso del paquete mediante
     *        aprobarReembolsoPorCalamidadPaquete(cliente, paquete).
     * Then:  Cada tiquete dentro del paquete debe quedar marcado como reembolsado, el
     *        saldo del cliente debe incrementarse con el valor total correspondiente, y
     *        la entrada del cliente debe eliminarse del mapa de solicitudes de paquetes.
     */
    @Test
    @DisplayName("aprobarReembolsoPorCalamidadPaquete: marca cada tiquete, abona suma y elimina del mapa de paquetes")
    void aprobarReembolsoPaquete() {
    	Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        ); 


        cliente.solicitarReembolsoPorCalamidadPaquete(p, "motivo", a);
        admin.aprobarReembolsoPorCalamidadPaquete(cliente, p);
        for(Tiquete t: p.getTiquetesIncluidos()) {
        	assertTrue(t.isReembolsado());
        }
        assertEquals(20.0, cliente.getSaldo());
        assertFalse(admin.getSolicitudesReembolsoPaquetes().containsKey(cliente));
    }

    /**
     * Given: Un cliente con un paquete Multiple para el que se ha registrado una
     *        solicitud de reembolso por calamidad en el administrador auxiliar 'a'.
     * When:  El administrador principal llama a rechazarReembolsoPorCalamidadPaquete(cliente).
     * Then:  La solicitud del cliente debe eliminarse del mapa de solicitudes de
     *        paquetes, el saldo del cliente debe permanecer en 0.0 y ningún tiquete
     *        del paquete debe quedar marcado como reembolsado.
     */
    @Test
    @DisplayName("rechazarReembolsoPorCalamidadPaquete: elimina del mapa de paquetes")
    void rechazarReembolsoPaquete() {
    	Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        ); 


        cliente.solicitarReembolsoPorCalamidadPaquete(p, "motivo", a);

        admin.rechazarReembolsoPorCalamidadPaquete(cliente);
        for(Tiquete t: p.getTiquetesIncluidos()) {
        	assertFalse(t.isReembolsado());
        }
        assertEquals(0.0, cliente.getSaldo(), "no es el saldo esperado");
        assertFalse(admin.getSolicitudesReembolsoPaquetes().containsKey(cliente));
    }
}

