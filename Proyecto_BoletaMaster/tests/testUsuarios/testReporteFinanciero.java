package testUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Eventos.Evento;
import Eventos.Venue;

import static org.junit.jupiter.api.Assertions.*;

import Usuarios.Administrador;
import Usuarios.Organizador;
import Usuarios.ReporteFinanciero;

import java.time.LocalDateTime;


public class testReporteFinanciero {

    private ReporteFinanciero reporte;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    
    private Venue v;
    private Organizador o;
    private Administrador a;
    private Evento eventoFuturoActivo;


    @BeforeEach
    void setUp() {
        inicio = LocalDateTime.now().minusDays(7);
        fin = LocalDateTime.now();
        reporte = new ReporteFinanciero(123, inicio, fin);
        
    	v = new Venue(0, "venue", "Cll 1#1", 1000, "");
    	o= new Organizador("org", "123", "ORG", 0, 0, "");
    	a =new Administrador("adm", "123", "ADM",0);
    	
    	eventoFuturoActivo = new Evento("a", 0, v, LocalDateTime.of(2026, 1, 1, 9, 0), o, "Religioso", a);
    }

    /**
     * Given: Un ReporteFinanciero recién creado con id, fecha de inicio y fecha de fin
     *        específicas, y sin datos aún de ocupación ni ventas.
     * When:  Se consultan sus getters inmediatamente después de la construcción.
     * Then:  El id y las fechas deben coincidir con los valores entregados, el porcentaje
     *        de ocupación debe iniciar en 0.0 y las ventas del evento deben ser 0.
     */
    @Test
    @DisplayName("Constructor: inicializa id, fechas y porcentaje en 0")
    void constructorInicializa() {
        assertEquals(123, reporte.getId());
        assertEquals(inicio, reporte.getFechaInicio());
        assertEquals(fin, reporte.getFechaFin());
        assertEquals(0.0, reporte.getPorcentajeOcupacion(), 1e-6);
        // ventasEvento se calculará al llamar setVentasEvento(...)
        assertEquals(0, reporte.getVentasEvento());
    }

    /**
     * Given: Un ReporteFinanciero inicializado con un id y un rango de fechas.
     * When:  Se actualizan el id, la fecha de inicio y la fecha de fin mediante sus
     *        setters correspondientes.
     * Then:  Los getters deben devolver exactamente los nuevos valores asignados,
     *        reemplazando por completo los anteriores.
     */
    @Test
    @DisplayName("Setters básicos: id y fechas se actualizan correctamente")
    void settersBasicos() {
        var nuevoInicio = inicio.minusDays(1);
        var nuevoFin = fin.plusDays(2);

        reporte.setId(999);
        reporte.setFechaInicio(nuevoInicio);
        reporte.setFechaFin(nuevoFin);

        assertEquals(999, reporte.getId());
        assertEquals(nuevoInicio, reporte.getFechaInicio());
        assertEquals(nuevoFin, reporte.getFechaFin());
    }

    /**
     * Given: Un ReporteFinanciero con porcentaje de ocupación inicial igual a 0.0.
     * When:  Se establece un nuevo porcentaje de ocupación mediante setPorcentajeOcupacion().
     * Then:  El getter getPorcentajeOcupacion() debe devolver el mismo valor asignado,
     *        sin modificaciones.
     */
    @Test
    @DisplayName("setPorcentajeOcupacion: asigna y retorna el valor establecido")
    void porcentajeOcupacion() {
        reporte.setPorcentajeOcupacion(73.5);
        assertEquals(73.5, reporte.getPorcentajeOcupacion(), 1e-6);
    }

    /**
     * Given: Un ReporteFinanciero sin ventas registradas y un evento futuro que aún no
     *        tiene tiquetes vendidos (calcularTotalTiquetesVendidos() == 0).
     * When:  Se llama a setVentasEvento(eventoFuturoActivo) para tomar el valor de ventas
     *        directamente desde el evento.
     * Then:  El número de ventas en el reporte debe coincidir con el valor calculado
     *        por el evento, que en este caso es 0.
     */
    @Test
    @DisplayName("setVentasEvento: toma el valor desde Evento.calcularTotalTiquetesVendidos()")
    void ventasEventoDesdeCalculoDeEvento() {
        //Individual t = cliente.precompraIndividualNumerada(1, eventoFuturoActivo, loc, 10);
        reporte.setVentasEvento(eventoFuturoActivo);
        
        assertEquals(0, reporte.getVentasEvento());
    }
}
