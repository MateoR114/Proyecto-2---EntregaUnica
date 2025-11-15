package Persistencia;

import java.util.List;
import Tiquetes.*;

/**
 * Gestiona la persistencia de todos los tiquetes del sistema BoletaMaster:
 * Individuales y Paquetes (Deluxe, Multiple, PaseTemporada).
 */
public class PersistenciaTiquetes extends PersistenciaJSON {

    private static final String ARCHIVO_TIQUETES = "data/tiquetes.json";

    /**
     * Guarda todos los tiquetes (individuales y paquetes) en JSON.
     *
     * @param individuales Lista de tiquetes individuales.
     * @param paquetes Lista de paquetes (Deluxe, Multiple, Temporada).
     * @pre Listas definidas.
     * @post Se guarda el estado de todos los tiquetes.
     */
    public static void guardarTiquetes(List<Individual> individuales,
                                       List<PaqueteTiquetes> paquetes) {

        StringBuilder json = new StringBuilder("{\n");

        // Tiquetes individuales
        json.append("  \"individuales\": [\n");
        for (int i = 0; i < individuales.size(); i++) {
            Individual t = individuales.get(i);
            json.append("    {\"codigo\": \"").append(t.getCodigo())
                .append("\", \"precio\": ").append(t.getPrecioPagado())
                .append(", \"evento\": ").append(t.getEvento().getId())
                .append(", \"cliente\": \"").append(t.getComprador().getLogin())
                .append("\", \"usado\": ").append(t.isUsado())
                .append(", \"reembolsado\": ").append(t.isReembolsado()).append("}");
            if (i < individuales.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ],\n");

        // Paquetes
        json.append("  \"paquetes\": [\n");
        for (int i = 0; i < paquetes.size(); i++) {
            PaqueteTiquetes p = paquetes.get(i);
            json.append("    {\"id\": ").append(p.getCodigo())
                .append(", \"tipo\": \"").append(p.getClass().getSimpleName())
                .append("\", \"precio\": ").append(p.getPrecio())
                .append(", \"dueno\": \"").append(p.getDueno().getLogin())
                .append("\", \"cantidad\": ").append(p.getTiquetesIncluidos().size())
                .append("}");
            if (i < paquetes.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ]\n}");

        guardarArchivo(ARCHIVO_TIQUETES, json.toString());
    }
}
