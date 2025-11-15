package Persistencia;

import java.util.List;

import MarketPlace.OfertaMP;
import MarketPlace.Puja;
import MarketPlace.marketPlace;
import Tiquetes.Tiquete;

/**
 * Gestiona la persistencia del Marketplace de reventa:
 * ofertas activas y log histórico.
 */
public class PersistenciaMarketplace extends PersistenciaJSON {

    private static final String ARCHIVO_MARKETPLACE = "data/marketplace.json";

    /**
     * Guarda el estado completo del Marketplace:
     * ofertas activas y log histórico.
     *
     * @param mp Instancia del Marketplace.
     * @pre mp definido.
     * @post Se guarda el estado del Marketplace en JSON.
     */
    public static void guardarMarketplace(marketPlace mp) {
        StringBuilder json = new StringBuilder("{\n");

        List<OfertaMP> activas = mp.getActivas();
        List<OfertaMP> log = mp.getLog();

        json.append("  \"ofertasActivas\": [\n");
        for (int i = 0; i < activas.size(); i++) {
            OfertaMP o = activas.get(i);
            agregarOferta(json, o);
            if (i < activas.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ],\n");

        json.append("  \"log\": [\n");
        for (int i = 0; i < log.size(); i++) {
            OfertaMP o = log.get(i);
            agregarOferta(json, o);
            if (i < log.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");

        json.append("}\n");

        guardarArchivo(ARCHIVO_MARKETPLACE, json.toString());
    }

    /**
     * Agrega la representación JSON de una oferta del Marketplace.
     *
     * @param JSON.
     * @param o    Oferta a serializar.
     */
    private static void agregarOferta(StringBuilder json, OfertaMP o) {
        json.append("    {\n");
        json.append("      \"idOferta\": ").append(o.getIdOferta()).append(",\n");
        json.append("      \"vendedor\": \"").append(o.getVendedor().getLogin()).append("\",\n");
        json.append("      \"precioBase\": ").append(o.getPrecioBase()).append(",\n");
        json.append("      \"estado\": \"").append(o.getEstado()).append("\",\n");
        json.append("      \"fechaPublicacion\": \"").append(o.getFechaPublicacion()).append("\",\n");
        json.append("      \"fechaCierre\": ");
        if (o.getFechaCierre() != null) {
            json.append("\"").append(o.getFechaCierre()).append("\"");
        } else {
            json.append("null");
        }
        json.append(",\n");

        List<Tiquete> boletas = o.getBoletas();
        json.append("      \"boletas\": [");
        for (int j = 0; j < boletas.size(); j++) {
            Tiquete t = boletas.get(j);
            json.append("{");
            json.append("\"eventoId\": ").append(t.getEvento().getId());
            json.append(", \"eventoNombre\": \"").append(t.getEvento().getNombre()).append("\"");
            json.append(", \"localidad\": \"").append(t.getLocalidad().getNombre()).append("\"");
            json.append(", \"dueno\": \"").append(t.getDueno().getLogin()).append("\"");
            json.append(", \"precio\": ").append(t.getPrecio());
            json.append(", \"transferible\": ").append(t.isTransferible());
            json.append(", \"usado\": ").append(t.isUsado());
            json.append(", \"reembolsado\": ").append(t.isReembolsado());
            json.append("}");
            if (j < boletas.size() - 1) {
                json.append(",");
            }
        }
        json.append("],\n");

        List<Puja> pujas = o.getPujas();
        json.append("      \"pujas\": [\n");
        for (int k = 0; k < pujas.size(); k++) {
            Puja p = pujas.get(k);
            json.append("        {");
            json.append("\"idPuja\": ").append(p.getIdPuja());
            json.append(", \"comprador\": \"").append(p.getComprador().getLogin()).append("\"");
            json.append(", \"monto\": ").append(p.getMonto());
            json.append(", \"estado\": \"").append(p.getEstado()).append("\"");
            json.append(", \"fechaHora\": \"").append(p.getFechaHora()).append("\"");
            json.append("}");
            if (k < pujas.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("      ]\n");
        json.append("    }");
    }
}