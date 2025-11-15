package Usuarios;

import Eventos.*;
import MarketPlace.OfertaMP;
import MarketPlace.marketPlace;
import Tiquetes.*;

import java.util.*;

/**
 * Representa a un administrador del sistema BoletaMaster.
 * Se encarga de aprobar venues, abrir o cerrar eventos, establecer cuotas
 * y resolver solicitudes de reembolso.
 */
public class Administrador extends Usuario{
	
    // Atributos 
    protected int id;
	private double gananciaSobreCargos;
	private double ventasDia;
	private double ventasOrganizador;
	private double cuotaEmisionGlobal;
	private Map<String, Double> cargosPorTipoEvento;
    private Map<Cliente, Tiquete> solicitudesReembolsoTiquete;
    private Map<Cliente, PaqueteTiquetes> solicitudesReembolsoPaquetes;
	
	// Constructor

    /**
     * @pre login != "" ; password != "" ; nombre != "" ; id > 0
     * @post Admin creado con mapas vacíos y valores numéricos en 0
     * @param login login
     * @param password contraseña
     * @param nombre nombre
     * @param id identificador
     */
	public Administrador(String login, String password, String nombre, int id) {
		super(login, password, nombre);
		this.setGananciaSobreCargos(0.0);
		this.setVentasDia(0.0);
		this.setVentasOrganizador(0.0);
		this.cuotaEmisionGlobal = 0.0;
        this.cargosPorTipoEvento = new HashMap<>();
        this.solicitudesReembolsoTiquete = new HashMap<>();
        this.solicitudesReembolsoPaquetes = new HashMap<>();
	}

	// Getters y Setters

    /**
     * @pre true
     * @post retorna id
     * @return id
     */
    public int getId(){
        return id;
    }

    /**
     * @pre codigo > 0
     * @post id actualizado
     * @param codigo nuevo id
     */
    public void setId(int codigo) {
        this.id=codigo;
    }
	
    /**
     * @pre true
     * @post retorna ventas organizador
     * @return doble
     */
	public double getVentasOrganizador() {
		return ventasOrganizador;
	}

    /**
     * @pre ventasOrganizador >= 0
     * @post valor actualizado
     * @param ventasOrganizador valor
     */
	public void setVentasOrganizador(double ventasOrganizador) throws IllegalArgumentException {
        if (ventasOrganizador < 0) {
        	throw new IllegalArgumentException("Valor inválido.");
        }
		this.ventasOrganizador = ventasOrganizador;
	}

    /**
     * @pre true
     * @post retorna ventas del día
     * @return valor
     */
	public double getVentasDia() {
		return ventasDia;
	}

    /**
     * @pre ventasDia >= 0
     * @post valor actualizado
     * @param ventasDia valor
     */
	public void setVentasDia(double ventasDia) throws IllegalArgumentException{
        if (ventasDia < 0) {
        	throw new IllegalArgumentException("Valor inválido.");
        }
		this.ventasDia = ventasDia;
	}

    /**
     * @pre true
     * @post retorna ganancia sobre cargos
     * @return valor
     */
	public double getGananciaSobreCargos() {
		return gananciaSobreCargos;
	}

    /**
     * @pre gananciaSobreCargos >=0
     * @post valor actualizado
     * @param gananciaSobreCargos valor nuevo
     */
	public void setGananciaSobreCargos(double gananciaSobreCargos) throws IllegalArgumentException {
        if (gananciaSobreCargos < 0) {
        	throw new IllegalArgumentException("Valor inválido.");
        }
		this.gananciaSobreCargos = gananciaSobreCargos;
	}

    /**
     * @pre true
     * @post retorna cuota global
     * @return cuota
     */
    public double getCuotaEmisionGlobal() { 
    	return cuotaEmisionGlobal; 
    }

    /**
     * @pre c >= 0
     * @post cuotaEmisionGlobal actualizada
     * @param c nueva cuota
     */
    public void setCuotaEmisionGlobal(double c)throws IllegalArgumentException {
        if (c < 0) {
        	throw new IllegalArgumentException("La cuota no puede ser negativa.");
        }
        this.cuotaEmisionGlobal = c;
    }

    /**
     * @pre true
     * @post retorna mapa de cargos
     * @return mapa
     */
    public Map<String, Double> getCargosPorTipoEvento() {
    	return cargosPorTipoEvento; 
    }

    /**
     * @pre m != null
     * @post mapa reemplazado
     * @param m mapa nuevo
     */
    public void setCargosPorTipoEvento(Map<String, Double> m)throws IllegalArgumentException {
        if (m == null) {
        	throw new IllegalArgumentException("Mapa inválido.");
        }
        this.cargosPorTipoEvento = m;
    }

    /**
     * @pre true
     * @post retorna solicitudes de reembolso tiquete
     * @return mapa
     */
    public Map<Cliente, Tiquete> getSolicitudesReembolsoTiquete() {
    	return solicitudesReembolsoTiquete; 
    }

    /**
     * @pre true
     * @post retorna solicitudes de paquetes
     * @return mapa
     */
    public Map<Cliente, PaqueteTiquetes> getSolicitudesReembolsoPaquetes() {
    	return solicitudesReembolsoPaquetes; 
    }

    // MÉTODOS FUNCIONALES

    /**
     * @pre venue != null
     * @post venue.aprobado = true
     * @param venue venue a aprobar
     */
    public void aprobarVenue(Venue venue) throws IllegalArgumentException{
        if (venue == null) {
        	throw new IllegalArgumentException("Venue inválido.");
        }
        venue.setAprobado(true);
    }

    /**
     * @pre venue != null
     * @post venue.aprobado = false
     * @param venue venue a desaprobar
     */
    public void desaprobarVenue(Venue venue) throws IllegalArgumentException{
        if (venue == null) {
        	throw new IllegalArgumentException("Venue inválido.");
        }
        venue.setAprobado(false);
    }

    /**
     * @pre evento != null
     * @post evento cancelado
     * @param evento evento a cancelar
     */
    public void cancelarEvento(Evento evento)throws IllegalArgumentException {
        if (evento == null) {
        	throw new IllegalArgumentException("Evento inválido.");
        }
        evento.cancelarPorAdministrador();
    }

    /**
     * @pre cuota >= 0
     * @post cuotaEmisionGlobal = cuota
     * @param cuota valor
     */
    public void establecerCuotaEmisionGlobal(double cuota) throws IllegalArgumentException{
        if (cuota < 0) {
        	throw new IllegalArgumentException("La cuota debe ser positiva.");
        }
        this.cuotaEmisionGlobal = cuota;
    }

    /**
     * @pre tipoEvento != "" ; porcentaje >= 0
     * @post cargosPorTipoEvento.put(tipoEvento, porcentaje)
     * @param tipoEvento etiqueta
     * @param porcentaje porcentaje
     */
    public void establecerCargoServicioPorTipo(String tipoEvento, double porcentaje)throws IllegalArgumentException {
        if (tipoEvento == null || tipoEvento.isEmpty()) {
            throw new IllegalArgumentException("Tipo de evento inválido.");
        }
        if (porcentaje < 0) {
            throw new IllegalArgumentException("Porcentaje inválido.");
        }
        cargosPorTipoEvento.put(tipoEvento, porcentaje);
    }

    /**
     * @pre true
     * @post retorna porcentaje o 0
     * @param tipoEvento tipo
     * @return porcentaje
     */
    public double getCargoServicioParaEvento(String tipoEvento) {
        return cargosPorTipoEvento.getOrDefault(tipoEvento, 0.0);
    }

    /**
     * @pre true
     * @post siempre retorna false
     * @return false
     */
    public boolean puedeComprar() {
        return false;
    }

    // REEMBOLSOS

    /**
     * @pre cliente != null ; tiquete != null
     * @post tiquete marcado reembolsado ; saldo del cliente incrementa
     * @param cliente cliente
     * @param tiquete tiquete
     */
    public void aprobarReembolsoPorCalamidadTiquete(Cliente cliente, Tiquete tiquete) {
        
        tiquete.setReembolsado(true);
        cliente.setSaldo(cliente.getSaldo() + tiquete.calcularCostoTotal());;
        solicitudesReembolsoTiquete.remove(cliente);
    }

    /**
     * @pre cliente != null
     * @post solicitud eliminada
     * @param cliente cliente
     */
    public void rechazarReembolsoPorCalamidadTiquete(Cliente cliente) {
        solicitudesReembolsoTiquete.remove(cliente);
    }

    /**
     * @pre cliente != null ; paquete != null
     * @post todos los tiquetes del paquete reembolsados y saldo incrementado
     * @param cliente cliente
     * @param paquete paquete
     */
    public void aprobarReembolsoPorCalamidadPaquete(Cliente cliente, PaqueteTiquetes paquete) {
    	double precioAReembolsar = 0.0;
    	for(Tiquete t: paquete.getTiquetesIncluidos()) {
    		t.setReembolsado(true);
    		precioAReembolsar += t.calcularCostoTotal();
        }
        cliente.setSaldo(cliente.getSaldo() + precioAReembolsar);;
        solicitudesReembolsoTiquete.remove(cliente);
    }

    /**
     * @pre cliente != null
     * @post solicitud eliminada
     * @param cliente cliente
     */
    public void rechazarReembolsoPorCalamidadPaquete(Cliente cliente) {
        solicitudesReembolsoPaquetes.remove(cliente);
    }

    /**
     * @pre tiquete != null ; cliente != null
     * @post solicitud agregada
     * @param tiquete tiquete
     * @param cliente cliente
     */
    public void crearSolicitudReembolsoTiquete(Tiquete tiquete, Cliente cliente) {
    	solicitudesReembolsoTiquete.put(cliente, tiquete);
    }

    /**
     * @pre paquete != null ; cliente != null
     * @post solicitud agregada
     * @param paquete paquete
     * @param cliente cliente
     */
    public void crearSolicitudReembolsoPaquete(PaqueteTiquetes paquete, Cliente cliente) {
    	solicitudesReembolsoPaquetes.put(cliente, paquete);
    }

    /**
     * @pre omp != null
     * @post oferta removida de activas y marcada como cancelada
     * @param omp oferta
     * @throws IllegalArgumentException si omp == null
     */
    public void cancelarOfertaMP(OfertaMP omp) throws IllegalArgumentException{
    	omp.setEstado("Cancelado por admin");
    	
    	ArrayList<OfertaMP> ofs= marketPlace.getInstance().getActivas();
    	ofs.remove(omp);
    	marketPlace.getInstance().setActivas(ofs);
    }

    /**
     * @pre true
     * @post retorna el log completo
     * @return lista log
     */
    public ArrayList<OfertaMP> consultarLogOfertas(){
    	return marketPlace.getInstance().getLog();
    }
}
