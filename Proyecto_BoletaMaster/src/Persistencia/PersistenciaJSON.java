package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Clase base abstracta para la persistencia de datos en formato JSON.
 * Proporciona métodos genéricos de lectura y escritura en archivos.
 */
public abstract class PersistenciaJSON {

    /**
     * Guarda un texto JSON en un archivo.
     *
     * @param rutaArchivo Ruta completa del archivo.
     * @param contenidoJSON Contenido JSON a guardar.
     * @pre rutaArchivo y contenidoJSON definidos.
     * @post El archivo se crea o sobrescribe con el JSON especificado.
     */
    protected static void guardarArchivo(String rutaArchivo, String contenidoJSON) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            writer.write(contenidoJSON);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo JSON: " + rutaArchivo, e);
        }
    }

    /**
     * Carga un archivo JSON como cadena de texto.
     *
     * @param rutaArchivo Ruta del archivo JSON.
     * @return Contenido del archivo o {@code null} si no existe.
     * @post No cambia el estado del sistema.
     */
    protected static String cargarArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return null;

        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + rutaArchivo, e);
        }
        return contenido.toString();
    }
}