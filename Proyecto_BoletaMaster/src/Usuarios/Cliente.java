package Usuarios;

import java.time.*;
import java.util.*;
import Tiquetes.*;
import Eventos.*;
import MarketPlace.OfertaMP;
import MarketPlace.Puja;
import MarketPlace.marketPlace;

/**
 * Representa a un cliente del sistema BoletaMaster.
 * Un cliente puede comprar tiquetes, solicitar reembolsos y transferir entradas.
 */
public class Cliente extends Usuario{
	
	// Atributos 
	private double saldo;
	private ArrayList<Compra> compras; 
    protected int id;
	
	// Constructor
	
    /**
     * @pre login != "" ; password != "" ; nombre != ""
     * @post Cliente creado con saldo 0 y lista de compras vacía
     * @param login Login único
     * @param password Contraseña
     * @param nombre Nombre del cliente
     * @param id Identificador
     */
	public Cliente(String login, String password, String nombre, int id) {
		super(login, password, nombre);
		this.setSaldo(0.0);
		this.compras = new ArrayList<Compra>();
		this.id = id;
	}

	// Getters y Setters

    /**
     * @pre true
     * @post Se retorna el id del cliente
     * @return id único
     */
    public int getId(){
        return id;
    }

    /**
     * @pre codigo > 0
     * @post Se actualiza el id del cliente
     * @param codigo Nuevo id
     */
    public void setId(int codigo) {
        this.id=codigo;
    }

    /**
     * @pre true
     * @post Se retorna el saldo actual
     * @return saldo
     */
	public double getSaldo() {
		return saldo;
	}

    /**
     * @pre saldo >= 0
     * @post El saldo queda actualizado
     * @param saldo Nuevo saldo
     */
	public void setSaldo(double saldo) throws IllegalArgumentException  {
        if (saldo < 0) {
        	throw new IllegalArgumentException("El saldo no puede ser negativo.");
        }
		this.saldo = saldo;
	}

    /**
     * @pre true
     * @post Retorna la lista actual de compras
     * @return lista de compras
     */
    public ArrayList<Compra> getCompras() {
        return compras;
    }

    /**
     * @pre compras != null
     * @post Se reemplaza la lista de compras
     * @param compras nueva lista
     */
    public void setCompras(ArrayList<Compra> compras) {
        this.compras = compras;
    }

    /**
     * @pre compra != null
     * @post compra agregada a la lista del cliente
     * @param compra objeto de compra
     */
    public void registrarCompra(Compra compra) {
        if (compra != null) {
        	compras.add(compra);
        }
    }

    /**
     * @pre monto > 0 && monto <= saldo
     * @post saldo = saldo - monto
     * @param monto cantidad a descontar
     */
    public void usarSaldoDeReembolso(double monto)throws IllegalArgumentException  {
        if (monto <= 0 || monto > saldo) {
            throw new IllegalArgumentException("Monto inválido o saldo insuficiente.");
        }
        saldo -= monto;
    }
    
    
    // MÉTODOS DE COMPRA

    /**
     * @pre evento != null ; localidad != null ; localidad.isNumerada()==true ;
     *      evento.getEstado()=="Activo" ; evento.getFecha().isAfter(now)
     * @post Se crea un tiquete numerado sin registrar compra
     * @param codigo identificador del tiquete
     * @param evento evento asociado
     * @param localidad localidad numerada
     * @param numeroAsiento asiento solicitado
     * @return Tiquete Individual numerado
     * @throws IllegalArgumentException si evento/localidad inválidos o fuera de fecha
     */
    public Individual precompraIndividualNumerada(int codigo, Evento evento, Localidad localidad, int numeroAsiento)
            throws IllegalArgumentException {

        if (evento == null || localidad == null || !localidad.isNumerada())
            throw new IllegalArgumentException("Evento o localidad inválidos.");

        if (evento.getEstado() != "Activo" || evento.getFecha().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("El evento no está disponible para compras.");

        Individual tiquete = new Individual(codigo, localidad.getPrecioBase(), evento, localidad, this, true, numeroAsiento);
        
        return tiquete;
    }

    /**
     * @pre evento != null ; localidad != null ; localidad.isNumerada()==false ;
     *      evento.getEstado()=="Activo"
     * @post Se crea un tiquete no numerado
     * @param codigo id
     * @param evento evento objetivo
     * @param localidad localidad no numerada
     * @param usarSaldoReembolso indicador
     * @return Individual no numerado
     * @throws IllegalArgumentException si evento/localidad inválidos
     */
    public Individual precompraIndividualNoNumerada(int codigo, Evento evento, Localidad localidad, boolean usarSaldoReembolso)
            throws IllegalArgumentException {

        if (evento == null || localidad == null || localidad.isNumerada())
            throw new IllegalArgumentException("Evento o localidad inválidos.");

        if (evento.getEstado() != "Activo" || evento.getFecha().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("El evento no está disponible para compras.");

        Individual tiquete = new Individual(codigo, localidad.getPrecioBase(), evento, localidad, this, true, -1);

        return tiquete;
    }

    /**
     * @pre evento != null ; localidad != null ; cantidad > 0 ; numerosAsientos.size()==cantidad
     * @post Se genera un paquete Multiple sin registrar compra
     * @param codigo id
     * @param evento evento asociado
     * @param localidad localidad
     * @param cantidad cantidad de tiquetes
     * @param usarSaldoReembolso indicador
     * @param numerosAsientos lista de asientos
     * @return paquete Multiple
     * @throws IllegalArgumentException si un asiento no está disponible
     */
    public Multiple precompraPaqueteMultipleMismoEvento(int codigo, Evento evento, Localidad localidad, int cantidad, boolean usarSaldoReembolso, ArrayList<Integer> numerosAsientos)
            throws IllegalArgumentException {

        if (evento == null || localidad == null)
            throw new IllegalArgumentException("Evento o localidad inválidos.");

        if (evento.getEstado() != "Activo" || evento.getFecha().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("El evento no está disponible para compras.");

        ArrayList<Tiquete> tiquetesPaquete = new ArrayList<>();
        
        for(int i = 0;i<cantidad;i++) {
        	if((localidad.asientoDisponible(numerosAsientos.get(i)) && localidad.isNumerada())||(!localidad.isNumerada() && localidad.hayDisponibilidad())) {
                Individual tiquete = new Individual(codigo, localidad.getPrecioBase(), evento, localidad, this, true, numerosAsientos.get(i));
        		tiquetesPaquete.add(tiquete);
        	}
        	else throw new IllegalArgumentException("Numeros de asiento no disponibles");
        }
        
        Multiple paquete = new Multiple(codigo, this, localidad.getPrecioBasePaquetesxUnidad()*cantidad, tiquetesPaquete);

        return paquete;
    }

    /**
     * @pre eventos.size()==localidades.size()==numerosAsientos.size()
     *      todos los eventos activos
     * @post Se genera un PaseTemporada sin registrar compra
     * @param codigo id
     * @param eventos lista eventos
     * @param localidades lista localidades
     * @param usarSaldoReembolso indicador
     * @param numerosAsientos lista asientos
     * @return PaseTemporada
     * @throws IllegalArgumentException si un evento no está disponible
     */
    public PaseTemporada precompraPaseTemporada(int codigo, ArrayList<Evento> eventos, ArrayList<Localidad> localidades, boolean usarSaldoReembolso, ArrayList<Integer> numerosAsientos)
            throws IllegalArgumentException {
    	for(Evento e: eventos) {
    		if (e == null) {
    			throw new IllegalArgumentException("Evento o localidad inválidos.");
    		}
    		if (e.getEstado() != "Activo" || e.getFecha().isBefore(LocalDateTime.now())) {
    			throw new IllegalArgumentException("El evento no está disponible para compras.");
    		}
    	}
    	
    	double precioTotal = 0.0;
    	for(Localidad l: localidades) {
            if (l == null) {
                throw new IllegalArgumentException("Evento o localidad inválidos.");
            }
            precioTotal = l.getPrecioPaseTemporadaxUnidad();
    	}
    	
        ArrayList<Tiquete> tiquetesPaquete = new ArrayList<>();
        
        for(int i = 0;i<eventos.size();i++) {
        	if((localidades.get(i).asientoDisponible(numerosAsientos.get(i)) && localidades.get(i).isNumerada())||(!localidades.get(i).isNumerada() && localidades.get(i).hayDisponibilidad())) {
                Individual tiquete = new Individual(codigo, localidades.get(i).getPrecioBase(), eventos.get(i), localidades.get(i), this, true, numerosAsientos.get(i));
        		tiquetesPaquete.add(tiquete);
        	}
        	else throw new IllegalArgumentException("Numeros de asiento no disponibles");
        }

        PaseTemporada paseTemporada = new PaseTemporada(codigo, this, precioTotal, eventos, tiquetesPaquete);
        return paseTemporada;
    }

    /**
     * @pre eventos.size()==localidades.size()==numerosAsientos.size()
     * @post Se crea un paquete Deluxe sin registrar compra
     * @param codigo id
     * @param eventos eventos incluidos
     * @param localidades localidades
     * @param beneficios beneficios extra
     * @param tiquetes lista adicional
     * @param numerosAsientos asientos
     * @return Deluxe
     * @throws IllegalArgumentException si un evento/asiento es inválido
     */
    public Deluxe precompraDeluxe(int codigo, ArrayList<Evento> eventos, ArrayList<Localidad> localidades, String beneficios, ArrayList<Tiquete> tiquetes, ArrayList<Integer> numerosAsientos)
            throws IllegalArgumentException {

    	for(Evento e: eventos) {
    		if (e == null) {
    			throw new IllegalArgumentException("Evento o localidad inválidos.");
    		}
    		if (e.getEstado() != "Activo" || e.getFecha().isBefore(LocalDateTime.now())) {
    			throw new IllegalArgumentException("El evento no está disponible para compras.");
    		}
    	}
    	
    	double precioTotal = 0.0;
    	for(Localidad l: localidades) {
            if (l == null) {
                throw new IllegalArgumentException("Evento o localidad inválidos.");
            }
            precioTotal = l.getPrecioPaseDeluxexUnidad();
    	}
    	
        ArrayList<Tiquete> tiquetesPaquete = new ArrayList<>();
        
        for(int i = 0;i<eventos.size();i++) {
        	if((localidades.get(i).asientoDisponible(numerosAsientos.get(i)) && localidades.get(i).isNumerada())||(!localidades.get(i).isNumerada() && localidades.get(i).hayDisponibilidad())) {
                Individual tiquete = new Individual(codigo, localidades.get(i).getPrecioBase(), eventos.get(i), localidades.get(i), this, true, numerosAsientos.get(i));
        		tiquetesPaquete.add(tiquete);
        	}
        	else throw new IllegalArgumentException("Numeros de asiento no disponibles");
        }

        Deluxe deluxe = new Deluxe(codigo, this, precioTotal,beneficios,  tiquetesPaquete);
        return deluxe;
    }

    /**
     * @pre true
     * @post Se retorna la lista de tiquetes comprados
     * @return lista de tiquetes
     */
    public ArrayList<Tiquete> obtenerTiquetesComprados() {
        ArrayList<Tiquete> resultado = new ArrayList<Tiquete>();
        for (Compra c : compras) {
            resultado.addAll(c.getTiquetesComprados());
        }
        return resultado;
    }

    /**
     * @pre true
     * @post Se retorna lista de paquetes comprados
     * @return lista de paquetes
     */
    public ArrayList<PaqueteTiquetes> obtenerPaquetesComprados() {
        ArrayList<PaqueteTiquetes> resultado = new ArrayList<PaqueteTiquetes>();
        for (Compra c : compras) {
            resultado.addAll(c.getPaquetes());
        }
        return resultado;
    }

    /**
     * @pre tiquetes != null ; paquetes != null ; si usarSaldoReembolso entonces saldo>=total
     * @post compra registrada y saldo actualizado si aplica
     * @param tiquetes lista de tiquetes
     * @param paquetes lista de paquetes
     * @param usarSaldoReembolso indicador
     * @return nueva Compra
     */
    public Compra hacerCompra(ArrayList<Tiquete> tiquetes, ArrayList<PaqueteTiquetes> paquetes, boolean usarSaldoReembolso) {
    	double totalCompra = 0.0;
    	
    	for(Tiquete t: tiquetes) {
            totalCompra += t.getPrecio() + t.getCargoServicio() + t.getCuotaImpresion();
    	}
    	
    	for(PaqueteTiquetes p: paquetes) {
            totalCompra += p.getPrecio() + p.getCargoServicio() + p.getCuotaImpresion();
    	}
    	
    	if (usarSaldoReembolso && saldo >= totalCompra) {
            this.setSaldo(saldo-totalCompra);
        } else {
            // pasarela externa
        }
        Compra compra = new Compra(this, tiquetes, paquetes, totalCompra, LocalDateTime.now());
        this.registrarCompra(compra);
        return compra;

    }

    /**
     * @pre tiquete != null ; destino != null ; tiquete.isTransferible()==true
     * @post Si credencial==true se transfiere el tiquete
     * @param tiquete tiquete a transferir
     * @param destino receptor
     * @param credencial autorización
     * @return true si transferido
     * @throws IllegalArgumentException si no es transferible
     */
    public boolean transferirTiquete(Tiquete tiquete, Cliente destino, boolean credencial) throws IllegalArgumentException{
    	if(!tiquete.isTransferible()){
    		throw new IllegalArgumentException("El tiquete no es transferible");
    	}
    	if(destino == null){
    		throw new IllegalArgumentException("Cliente destino no existe");
    	}
    	
    	if(credencial) {
    		tiquete.setDueno(destino);
    		return true;
    	}
    	else return false;
    	
    }

    /**
     * @pre paquete != null ; destino != null ; paquete.isTransferible()==true
     * @post Si credencial true, el paquete cambia de dueño
     * @param paquete paquete
     * @param destino receptor
     * @param credencial autorización
     * @return true si transferido
     * @throws IllegalArgumentException si no transferible
     */
    public boolean transferirPaqueteCompleto(PaqueteTiquetes paquete, Cliente destino, boolean credencial) {
    	if(!paquete.isTransferible()){
    		throw new IllegalArgumentException("El paquete no es transferible");
    	}
    	if(destino == null){
    		throw new IllegalArgumentException("Cliente destino no existe");
    	}
    	
    	if(credencial) {
    		paquete.setDueno(destino);
    		return true;
    	}
    	else return false;
    }

    /**
     * @pre paquete != null ; destino != null ; paquete.isTransferible()==true
     * @post tiquetes indicados cambian de dueño si pertenecen al paquete
     * @param paquete paquete origen
     * @param tiquetes lista parcial
     * @param destino receptor
     * @param credencial autorización
     * @return true si éxito
     * @throws IllegalArgumentException si un tiquete no pertenece
     */
    public boolean transferirParteDePaquete(PaqueteTiquetes paquete, ArrayList<Tiquete> tiquetes,
                                         Cliente destino, boolean credencial) {

        	if(!paquete.isTransferible()){
        		throw new IllegalArgumentException("El paquete no es transferible");
        	}
        	if(destino == null){
        		throw new IllegalArgumentException("Cliente destino no existe");
        	}
        	
        	if(credencial) {
        		for (Tiquete t: tiquetes) {
        			if(paquete.getTiquetesIncluidos().contains(t)) {
        			t.setDueno(destino);}
        			else throw new IllegalArgumentException("El tiquete no está en el paquete");
        		}
        		return true;
        	}
        	else return false;
        }

    /**
     * @pre tiquete != null ; admin != null
     * @post Se crea la solicitud de reembolso
     * @param tiquete tiquete
     * @param admin admin
     */
    public void solicitarReembolsoPorCalamidadTiquete(Tiquete tiquete, Administrador admin) {
    	admin.crearSolicitudReembolsoTiquete(tiquete, this);
    }

    /**
     * @pre paquete != null ; admin != null
     * @post Se registra solicitud
     * @param paquete paquete
     * @param motivo motivo
     * @param admin admin
     */
    public void solicitarReembolsoPorCalamidadPaquete(PaqueteTiquetes paquete, String motivo, Administrador admin) {
    	admin.crearSolicitudReembolsoPaquete( paquete, this);
    }

    /**
     * @pre omp != null ; monto > 0 ; monto <= saldo
     * @post puja creada y saldo descontado
     * @param id id puja
     * @param omp oferta
     * @param monto valor
     */
    public void crearPuja(int id, OfertaMP omp, double monto) {

        if (omp == null)
            throw new IllegalArgumentException("La oferta no puede ser nula.");

        if (monto <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");

        if (monto > this.saldo)
            throw new IllegalArgumentException("Saldo insuficiente.");

        if (!omp.getPujas().isEmpty() && monto <= omp.getPujas().getFirst().getMonto()) {
            throw new IllegalArgumentException("El monto debe superar la puja más reciente.");
        }

        Puja p = new Puja(id, omp, this, monto, "Pendiente", LocalDateTime.now());

        this.setSaldo(this.saldo - monto);

        ArrayList<Puja> pjs = omp.getPujas();
        pjs.addFirst(p);
        omp.setPujas(pjs);
    }

    /**
     * @pre p != null ; omp != null ; omp.getVendedor()==this
     * @post puja aceptada, oferta cerrada, tiquetes cambian de dueño
     * @param p puja
     * @param omp oferta
     * @throws IllegalArgumentException si this no es vendedor
     */
    public void aceptarPuja(Puja p, OfertaMP omp) throws IllegalArgumentException{
    	if (omp.getVendedor()!= this) {
    		throw new IllegalArgumentException();
    	}
    	p.setEstado("Aceptada");
    	omp.setEstado("Cerrada");
    	for (Tiquete t: omp.getBoletas()) {
    		t.setDueno(p.getComprador());
    	}
    	this.setSaldo(saldo+p.getMonto());
    	
    	ArrayList<OfertaMP> ofs= marketPlace.getInstance().getActivas();
    	ofs.remove(omp);
    	marketPlace.getInstance().setActivas(ofs);
    }

    /**
     * @pre p != null ; omp != null ; p.getComprador()==this
     * @post puja cancelada y saldo devuelto
     * @param p puja
     * @param omp oferta
     * @throws IllegalArgumentException si this no es comprador
     */
    public void cancelarPuja(Puja p, OfertaMP omp) throws IllegalArgumentException{
    	if (p.getComprador()!= this) {
    		throw new IllegalArgumentException();
    	}
    	p.setEstado("Cancelada");
    	p.getComprador().setSaldo(saldo+p.getMonto());
    	ArrayList<Puja> pjs = omp.getPujas();
    	pjs.remove(p);
    	omp.setPujas(pjs);
    }

    /**
     * @pre boletas != null ; precioBase > 0 ; fechaCierre != null
     * @post oferta creada y añadida a activas y log
     * @param idOferta id
     * @param boletas lista
     * @param precioBase precio
     * @param fechaCierre cierre
     * @throws IllegalArgumentException si boletas es null
     */
    public void crearOfertaMP(int idOferta, ArrayList<Tiquete> boletas, double precioBase, LocalDateTime fechaCierre) throws IllegalArgumentException{
    	if (boletas == null){
    		throw new IllegalArgumentException();
    	}
    	
    	ArrayList<Puja> pjs = new ArrayList<Puja>();
    	OfertaMP omp= new OfertaMP(idOferta, this, boletas,  precioBase, "Activa",
    			LocalDateTime.now(), fechaCierre, pjs);
    	
    	ArrayList<OfertaMP> ofs= marketPlace.getInstance().getActivas();
    	ofs.add(omp);
    	marketPlace.getInstance().setActivas(ofs);
    	
    	ArrayList<OfertaMP> log= marketPlace.getInstance().getLog();
    	log.add(omp);
    	marketPlace.getInstance().setLog(log);
    }

    /**
     * @pre omp != null ; omp.getVendedor()==this
     * @post oferta marcada cancelada y removida de activas
     * @param omp oferta
     * @throws IllegalArgumentException si this no es vendedor
     */
    public void cancelarOfertaMP(OfertaMP omp) throws IllegalArgumentException{
    	if (omp.getVendedor()!= this) {
    		throw new IllegalArgumentException();
    	}
    	omp.setEstado("Cancelado por vendedor");
    	
    	ArrayList<OfertaMP> ofs= marketPlace.getInstance().getActivas();
    	ofs.remove(omp);
    	marketPlace.getInstance().setActivas(ofs);
    }

    /**
     * @pre true
     * @post retorna ofertas activas
     * @return lista ofertas
     */
    public ArrayList<OfertaMP> consultarOfertasActivas() {
    	return marketPlace.getInstance().getActivas();
    }

    /**
     * @pre omp != null
     * @post retorna la lista de pujas
     * @param omp oferta
     * @return lista de pujas
     */
    public ArrayList<Puja> consultarPujasOfertaMP(OfertaMP omp){
    	return omp.getPujas();
    }
    
}
