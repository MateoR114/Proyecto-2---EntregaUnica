package Persistencia;

import java.util.List;
import Eventos.Evento;

/**
 * Gestiona la persistencia de los eventos, incluyendo sus localidades,
 * ofertas y sedes (venues).
 */
public class PersistenciaEventos extends PersistenciaJSON {

    private static final String ARCHIVO_EVENTOS = "data/eventos.json";

    /**
     * Guarda la lista de eventos y sus componentes.
     *
     * @param eventos Lista de eventos.
     * @pre eventos definida.
     * @post Se guarda el estado de los eventos en JSON.
     */
    public static void guardarEventos(List<Evento> eventos) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            json.append("  {\n")
                .append("    \"id\": ").append(e.getId()).append(",\n")
                .append("    \"nombre\": \"").append(e.getNombre()).append("\",\n")
                .append("    \"fecha\": \"").append(e.getFecha()).append("\",\n")
                .append("    \"organizadorLogin\": \"").append(e.getOrganizador().getLogin()).append("\",\n")
                .append("    \"adminLogin\": \"").append(e.getAdmin().getLogin()).append("\",\n")
                .append("    \"venue\": \"").append(e.getVenue().getNombre()).append("\",\n")
                .append("    \"localidades\": [");
            // Serializa las localidades
            var locs = e.getLocalidades();
            for (int j = 0; j < locs.size(); j++) {
                var l = locs.get(j);
                json.append("{\"nombre\": \"").append(l.getNombre())
                    .append("\", \"capacidad\": ").append(l.getCapacidad()).append("}");
                if (j < locs.size() - 1) json.append(",");
            }
            json.append("],\n");

            // Ofertas
            json.append("    \"ofertas\": [");
            var ofertas = e.getOfertas();
            for (int k = 0; k < ofertas.size(); k++) {
                var o = ofertas.get(k);
                json.append("{\"porcentajeDescuento\": ").append(o.getPorcentajeDescuento())
                    .append(", \"fechaInicio\": \"").append(o.getFechaInicio()).append("\"")
                    .append(", \"fechaFin\": \"").append(o.getFechaFin()).append("\"")
                    .append(", \"localidad\": \"").append(o.getLocalidad().getNombre()).append("\"")
                    .append("}");
                if (k < ofertas.size() - 1) json.append(",");
            }
            json.append("]\n  }");
            if (i < eventos.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        guardarArchivo(ARCHIVO_EVENTOS, json.toString());
    }
}
