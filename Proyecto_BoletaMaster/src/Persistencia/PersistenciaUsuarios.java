package Persistencia;

import java.util.List;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;
import Usuarios.ReporteFinanciero;

/**
 * Maneja la persistencia de todos los usuarios del sistema BoletaMaster:
 * Administradores, Organizadores, Clientes y sus reportes financieros.
 */
public class PersistenciaUsuarios extends PersistenciaJSON {

    private static final String ARCHIVO_USUARIOS = "data/usuarios.json";

    /**
     * @pre clientes != null ; organizadores != null ; administradores != null ; reportes != null
     * @post Se guarda el estado completo de usuarios en el archivo JSON correspondiente
     * @param clientes lista de clientes
     * @param organizadores lista de organizadores
     * @param administradores lista de administradores
     * @param reportes lista de reportes financieros
     */
    public static void guardarUsuarios(List<Cliente> clientes,
                                       List<Organizador> organizadores,
                                       List<Administrador> administradores,
                                       List<ReporteFinanciero> reportes) {

        StringBuilder json = new StringBuilder("{\n");

        // Clientes
        json.append("  \"clientes\": [\n");
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            json.append("    {\"id\": \"").append(c.getId())
                .append("\", \"nombre\": \"").append(c.getNombre())
                .append("\", \"correo\": \"").append(c.getLogin()).append("\"}");
            if (i < clientes.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ],\n");

        // Organizadores
        json.append("  \"organizadores\": [\n");
        for (int i = 0; i < organizadores.size(); i++) {
            Organizador o = organizadores.get(i);
            json.append("    {\"id\": \"").append(o.getId())
                .append("\", \"nombre\": \"").append(o.getNombre())
                .append("\", \"correo\": \"").append(o.getLogin()).append("\"}");
            if (i < organizadores.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ],\n");

        // Administradores
        json.append("  \"administradores\": [\n");
        for (int i = 0; i < administradores.size(); i++) {
            Administrador a = administradores.get(i);
            json.append("    {\"id\": \"").append(a.getId())
                .append("\", \"nombre\": \"").append(a.getNombre())
                .append("\", \"correo\": \"").append(a.getLogin()).append("\"}");
            if (i < administradores.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ],\n");

        // Reportes Financieros
        json.append("  \"reportesFinancieros\": [\n");
        for (int i = 0; i < reportes.size(); i++) {
            ReporteFinanciero r = reportes.get(i);
            json.append("    {\"id\": \"").append(r.getId())
                .append("\", \"ingresos\": ").append(r.getVentasEvento())
                .append(", \"gastos\": ").append(r.getPorcentajeOcupacion()) 
                .append(", \"fechaInicio\": \"").append(r.getFechaInicio()).append("\"")
                .append(", \"fechaFin\": \"").append(r.getFechaFin()).append("\"")
                .append("}");
            if (i < reportes.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("  ]\n}");

        guardarArchivo(ARCHIVO_USUARIOS, json.toString());
    }
}
