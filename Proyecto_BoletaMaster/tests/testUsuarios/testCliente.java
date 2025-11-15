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


import Tiquetes.Compra;
import Tiquetes.Deluxe;
import Tiquetes.Individual;
import Tiquetes.Multiple;
import Tiquetes.PaseTemporada;
import Tiquetes.Tiquete;

public class testCliente{

    private Cliente cliente;
    private Venue v;
    private Organizador o;
    private Administrador a;
    private Evento eventoFuturoActivo;
    private Evento eventoPasado;
    private Localidad loc;
    

    @BeforeEach
    void setUp() {
        cliente = new Cliente("login", "pwd", "Cliente Prueba", 101);
    	v = new Venue(0, "venue", "Cll 1#1", 1000, "");
    	o= new Organizador("org", "123", "ORG", 0, 0, "");
    	a =new Administrador("adm", "123", "ADM",0);
    	
    	
    	eventoFuturoActivo = new Evento("a", 0, v, LocalDateTime.of(2026, 1, 1, 9, 0), o, "Religioso", a);
    	loc = new Localidad("A", true, 1000, 5, eventoFuturoActivo, 5, 5, 5);
    	
    	eventoPasado = new Evento("a", 0, v, LocalDateTime.of(2024, 1, 1, 9, 0), o, "Religioso", a);
    	loc = new Localidad("A", true, 1000, 5, eventoFuturoActivo, 5, 5, 5);

    }

    /**
     * Given: Un cliente recién creado con identificador válido y sin historial de compras.
     * When:  Se consultan el saldo, la lista de compras y el id inmediatamente después
     *        de la construcción.
     * Then:  El saldo debe iniciar en 0.0, la lista de compras debe estar inicializada
     *        y vacía, y el id debe coincidir con el valor pasado al constructor (101).
     */
    @Test
    @DisplayName("Constructor: saldo inicia en 0 y compras vacías")
    void constructorInicializa() {
        assertEquals(0.0, cliente.getSaldo(), 0.0001);
        assertNotNull(cliente.getCompras());
        assertTrue(cliente.getCompras().isEmpty());
        assertEquals(101, cliente.getId());
    }

    /**
     * Given: Un cliente existente con saldo actual (cualquiera).
     * When:  Se intenta establecer un nuevo saldo negativo mediante setSaldo().
     * Then:  El método debe lanzar IllegalArgumentException, ya que no se permiten
     *        saldos negativos para el cliente.
     */
    @Test
    @DisplayName("setSaldo: lanza excepción si es negativo")
    void setSaldoNegativoLanza() {
        assertThrows(IllegalArgumentException.class, () -> cliente.setSaldo(-1.0));
    }

    /**
     * Given: Un cliente con un saldo de reembolso positivo suficiente para cubrir
     *        el monto a usar.
     * When:  Se invoca usarSaldoDeReembolso(monto) con un valor menor al saldo actual.
     * Then:  El saldo del cliente debe disminuir exactamente en ese monto, reflejando
     *        el nuevo saldo restante.
     */
    @Test
    @DisplayName("usarSaldoDeReembolso: descuenta correctamente")
    void usarSaldoDeReembolsoOk() {
        cliente.setSaldo(200.0);
        cliente.usarSaldoDeReembolso(150.0);
        assertEquals(50.0, cliente.getSaldo(), 0.0001);
    }

    /**
     * Given: Un cliente con un saldo positivo (por ejemplo, 100.0).
     * When:  Se intenta usarSaldoDeReembolso con un monto igual a 0 o superior al
     *        saldo disponible.
     * Then:  En ambos casos, el método debe lanzar IllegalArgumentException, ya que
     *        el monto debe ser mayor que 0 y no puede superar el saldo.
     */
    @Test
    @DisplayName("usarSaldoDeReembolso: monto 0 o mayor al saldo lanza excepción")
    void usarSaldoDeReembolsoInvalido() {
        cliente.setSaldo(100.0);
        assertThrows(IllegalArgumentException.class, () -> cliente.usarSaldoDeReembolso(0));
        assertThrows(IllegalArgumentException.class, () -> cliente.usarSaldoDeReembolso(200));
    }

    /**
     * Given: Un evento futuro en estado activo, una localidad numerada válida y un
     *        cliente que desea precomprar un tiquete.
     * When:  Se llama a precompraIndividualNumerada(...) con código, evento, localidad
     *        y número de asiento válidos.
     * Then:  Debe crearse un tiquete Individual no nulo, con precio igual al de la
     *        localidad, y el dueño del tiquete debe ser el cliente.
     */
    @Test
    @DisplayName("precompraIndividualNumerada: crea tiquete cuando evento 'Activo' y futuro")
    void precompraIndividualNumeradaOk() {

        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 10);

        assertNotNull(t);
        assertEquals(5.0, t.getPrecio(), 0.0001);
        assertEquals(cliente, t.getDueno());
    }

    /**
     * Given: Un evento cuya fecha ya pasó (evento pasado) y una localidad asociada.
     * When:  El cliente intenta precomprar un tiquete numerado para dicho evento
     *        usando precompraIndividualNumerada(...).
     * Then:  El método debe lanzar IllegalArgumentException, ya que no se pueden
     *        precomprar tiquetes para eventos ya ocurridos.
     */
    @Test
    @DisplayName("precompraIndividualNumerada: falla si evento pasado")
    void precompraIndividualNumeradaEventoPasado() {
        assertThrows(IllegalArgumentException.class, () ->
            cliente.precompraIndividualNumerada(1, eventoPasado, loc, 5));
    }

    /**
     * Given: Un evento futuro activo, una localidad válidamente configurada y una
     *        lista de asientos válidos dentro de la capacidad de la localidad.
     * When:  El cliente invoca precompraPaqueteMultipleMismoEvento(...) indicando
     *        la cantidad y los asientos que desea.
     * Then:  Debe crearse un paquete Multiple no nulo, cuyo dueño sea el cliente
     *        y que contenga exactamente tantos tiquetes como asientos indicados.
     */
    @Test
    @DisplayName("precompraPaqueteMultipleMismoEvento: crea paquete cuando asientos válidos")
    void precompraPaqueteMultipleOk() {

        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );

        assertNotNull(p);
        assertEquals(cliente, p.getDueno());
        assertEquals(2, p.getTiquetesIncluidos().size());
    }

    /**
     * Given: Una lista de eventos futuros activos, una lista de localidades
     *        correspondientes y una lista de asientos válidos.
     * When:  El cliente llama a precompraPaseTemporada(...) para adquirir un pase
     *        que cubre todos esos eventos.
     * Then:  Debe crearse un PaseTemporada no nulo, cuyo dueño sea el cliente y que
     *        contenga los tiquetes generados para los eventos/localidades dados,
     *        con la cantidad de tiquetes esperada.
     */
    @Test
    @DisplayName("precompraPaseTemporada: crea cuando todos los eventos están activos y futuros")
    void precompraPaseTemporadaOk() {

        PaseTemporada pase = cliente.precompraPaseTemporada(
                20,
                new ArrayList<>(Arrays.asList(
                		eventoFuturoActivo,
                    new Evento("b", 0, v, LocalDateTime.of(2027, 1, 1, 9, 0), o, "Religioso", a)
                )),
                new ArrayList<>(Arrays.asList(loc, loc)),
                false,
                new ArrayList<>(Arrays.asList(7, 8))
        );

        assertNotNull(pase);
        assertEquals(cliente, pase.getDueno());
        assertEquals(2, pase.getTiquetesIncluidos().size());
    }

    /**
     * Given: Una lista de eventos futuros activos, localidades asociadas y asientos
     *        válidos, más una descripción de beneficios Deluxe.
     * When:  El cliente invoca precompraDeluxe(...) para adquirir un paquete deluxe
     *        asociado a los eventos dados.
     * Then:  Debe crearse un Deluxe no nulo, con el cliente como dueño y que incluya
     *        los tiquetes correspondientes, manteniendo la cantidad esperada.
     */
    @Test
    @DisplayName("precompraDeluxe: crea cuando todos los eventos están activos y futuros")
    void precompraDeluxeOk() {

        Deluxe d = cliente.precompraDeluxe(
                30,
                new ArrayList<>(Arrays.asList(
                		eventoFuturoActivo,
                    new Evento("b", 0, v, LocalDateTime.of(2027, 1, 1, 9, 0), o, "Religioso", a)
                )),
                new ArrayList<>(Arrays.asList(loc, loc)),
                "merch",
                null,
                new ArrayList<>(Arrays.asList(7, 8))
        );

        assertNotNull(d);
        assertEquals(cliente, d.getDueno());
        assertEquals(2, d.getTiquetesIncluidos().size());
    }

    /**
     * Given: Un cliente con saldo suficiente, un tiquete y un paquete precomprados
     *        para un evento futuro activo.
     * When:  Se llama a hacerCompra(...) indicando que se debe usar el saldo de
     *        reembolso para pagar la compra.
     * Then:  Debe crearse una Compra no nula, el saldo del cliente debe disminuir
     *        según el costo total, la compra debe registrarse en la lista de compras
     *        y los tiquetes/paquetes deben quedar asociados a la compra del cliente.
     */
    @Test
    @DisplayName("hacerCompra: descuenta saldo cuando alcanza el total y registra la compra")
    void hacerCompraUsandoSaldo() {
        // 1 tiquete de 100 + cargos 10 + 5 => total 115
        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 10);

        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );

        cliente.setSaldo(500.0);
        Compra c = cliente.hacerCompra(
                new ArrayList<>(Arrays.asList(t)),
                new ArrayList<>(Arrays.asList(p)),
                true
        );

        assertNotNull(c);
        assertEquals(420.0, cliente.getSaldo(), 0.0001);
        assertEquals(1, cliente.getCompras().size());
        assertEquals(1, cliente.obtenerTiquetesComprados().size());
        assertEquals(1, cliente.obtenerPaquetesComprados().size());
    }

    /**
     * Given: Un cliente origen con un tiquete transferible válido y un cliente destino
     *        con credenciales válidas para recibirlo.
     * When:  Se invoca transferirTiquete(...) con el tiquete, el cliente destino y
     *        la credencial de transferencia en true.
     * Then:  La transferencia debe tener éxito (true) y el dueño del tiquete debe
     *        cambiar al cliente destino.
     */
    @Test
    @DisplayName("transferirTiquete: éxito con credencial y tiquete transferible")
    void transferirTiqueteOk() {
        Cliente destino = new Cliente("dest", "pwd", "Dest", 202);
        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 10);
        boolean r = cliente.transferirTiquete(t, destino, true);

        assertTrue(r);
        assertEquals(destino, t.getDueno());
    }

    /**
     * Given: Un cliente con un tiquete no transferible y otro tiquete transferible,
     *        y un posible cliente destino.
     * When:  Se intenta transferir el tiquete no transferible a un destino válido y
     *        luego se intenta transferir un tiquete válido a un destino null.
     * Then:  En ambos casos, transferirTiquete(...) debe lanzar IllegalArgumentException,
     *        ya sea por la política de no transferibilidad o por destino inválido.
     */
    @Test
    @DisplayName("transferirTiquete: lanza si no transferible o destino null")
    void transferirTiqueteErrores() {
        Cliente destino = new Cliente("dest", "pwd", "Dest", 202);
        Individual noTransf = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 10);
        noTransf.setTransferible(false);

        assertThrows(IllegalArgumentException.class, () ->
            cliente.transferirTiquete(noTransf, destino, true));
        
        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 10);

        assertThrows(IllegalArgumentException.class, () ->
            cliente.transferirTiquete(t, null, true));
    }

    /**
     * Given: Un cliente dueño de un paquete Multiple transferible y un cliente destino
     *        con credenciales correctas.
     * When:  Se llama a transferirPaqueteCompleto(...) con el paquete, el destino y
     *        la credencial en true.
     * Then:  La operación debe retornar true y el dueño del paquete debe cambiar al
     *        cliente destino.
     */
    @Test
    @DisplayName("transferirPaqueteCompleto: éxito con credencial y paquete transferible")
    void transferirPaqueteCompletoOk() {
        Cliente destino = new Cliente("dest", "pwd", "Dest", 202);
        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );
        boolean r = cliente.transferirPaqueteCompleto(p, destino, true);

        assertTrue(r);
        assertEquals(destino, p.getDueno());
    }

    /**
     * Given: Un cliente dueño de un paquete Multiple que contiene varios tiquetes,
     *        y un cliente destino válido.
     * When:  Se invoca transferirParteDePaquete(...) indicando solo un subconjunto
     *        de tiquetes a transferir.
     * Then:  La operación debe retornar true, los tiquetes transferidos deben pasar
     *        a ser propiedad del destino, y los restantes deben seguir perteneciendo
     *        al cliente original.
     */
    @Test
    @DisplayName("transferirParteDePaquete: transfiere solo algunos tiquetes contenidos")
    void transferirParteDePaqueteOk() {
        Cliente destino = new Cliente("dest", "pwd", "Dest", 202);

        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );
        Tiquete t1 = p.getTiquetesIncluidos().getFirst();
        Tiquete t2 = p.getTiquetesIncluidos().get(1);
        boolean r = cliente.transferirParteDePaquete(p, new ArrayList<>(Arrays.asList(t1)), destino, true);

        assertTrue(r);
        assertEquals(destino, t1.getDueno());
        assertEquals(cliente, t2.getDueno());
    }

    /**
     * Given: Un paquete Multiple de un cliente y un tiquete que no pertenece a dicho
     *        paquete.
     * When:  Se intenta transferir parte del paquete pasando una lista que contiene
     *        ese tiquete ajeno.
     * Then:  transferirParteDePaquete(...) debe lanzar IllegalArgumentException,
     *        ya que solo se pueden transferir tiquetes que formen parte del paquete.
     */
    @Test
    @DisplayName("transferirParteDePaquete: lanza si el tiquete no pertenece al paquete")
    void transferirParteDePaqueteTiqueteFuera() {
        Cliente destino = new Cliente("dest", "pwd", "Dest", 202);
        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 20);
        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );       
        assertThrows(IllegalArgumentException.class, () ->
            cliente.transferirParteDePaquete(p, new ArrayList<>(Arrays.asList(t)), destino, true));
    }

    /**
     * Given: Un cliente con un tiquete individual y un paquete Multiple válidos para
     *        un evento futuro activo, y un administrador que gestiona solicitudes.
     * When:  El cliente llama a solicitarReembolsoPorCalamidadTiquete(...) y
     *        solicitarReembolsoPorCalamidadPaquete(...) para registrar dichas
     *        solicitudes ante el administrador.
     * Then:  El administrador debe acumular una solicitud en la lista de reembolsos
     *        de tiquetes y una en la lista de reembolsos de paquetes.
     */
    @Test
    @DisplayName("Solicitudes de reembolso llaman a Administrador")
    void solicitudesReembolso() {
        Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 20);
        Multiple p = cliente.precompraPaqueteMultipleMismoEvento(
                10, eventoFuturoActivo, loc, 2, false,
                new ArrayList<>(Arrays.asList(1, 2))
        );    
        cliente.solicitarReembolsoPorCalamidadTiquete(t, a);
        cliente.solicitarReembolsoPorCalamidadPaquete(p, "motivo", a);

        assertEquals(1, a.getSolicitudesReembolsoPaquetes().size());
        assertEquals(1, a.getSolicitudesReembolsoTiquete().size());
    }

}

