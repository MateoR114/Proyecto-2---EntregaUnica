package MarketPlace;

import java.util.ArrayList; 

/**
 * Representa el sistema de reventa dentro de la aplicación.
 * Mantiene las ofertas activas y el log histórico.
 */
public class marketPlace {

    // Atributos
	private static marketPlace instance;
    private ArrayList<OfertaMP> activas;
    private ArrayList<OfertaMP> log;

    /**
     * @pre activas != null ; log != null
     * @post Instancia creada con listas internas asignadas
     * @param activas lista inicial de ofertas activas
     * @param log lista inicial de historial de ofertas
     */
	private marketPlace(ArrayList<OfertaMP> activas, ArrayList<OfertaMP> log) {
		this.activas = activas;
		this.log = log;
	}

    /**
     * @pre true
     * @post Retorna lista de ofertas activas
     * @return lista de ofertas activas
     */
	public ArrayList<OfertaMP> getActivas() {
		return activas;
	}

    /**
     * @pre activas != null
     * @post this.activas = activas
     * @param activas nueva lista de ofertas activas
     */
	public void setActivas(ArrayList<OfertaMP> activas) {
		this.activas = activas;
	}

    /**
     * @pre true
     * @post Retorna lista log
     * @return lista del log
     */
	public ArrayList<OfertaMP> getLog() {
		return log;
	}

    /**
     * @pre log != null
     * @post this.log = log
     * @param log nueva lista del log
     */
	public void setLog(ArrayList<OfertaMP> log) {
		this.log = log;
	}

    /**
     * @pre true
     * @post Retorna instancia única del marketplace. Si no existe, la crea.
     * @return instancia única marketPlace
     */
	public static marketPlace getInstance() {

		if(marketPlace.instance == null) {
			ArrayList<OfertaMP> ofsA = new ArrayList<OfertaMP>();
			ArrayList<OfertaMP> ofsB = new ArrayList<OfertaMP>();
			instance = new marketPlace(ofsA, ofsB);
		}

		return instance;
	}
}
