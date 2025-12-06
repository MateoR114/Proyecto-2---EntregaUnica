package qr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Utilidad para generar códigos QR a partir de un texto.
 * Se apoya en la librería ZXing (core + javase).
 */
public final class QR {

    private QR() {
        
    }

    /**
     * Genera un QR como imagen en memoria.
     *
     * @param contenido Texto a codificar en el QR.
     * @param ancho     Ancho del código QR en píxeles.
     * @param alto      Alto del código QR en píxeles.
     * @return Imagen BufferedImage con el QR.
     * @throws RuntimeException si ocurre un error al generar el QR.
     */
    public static BufferedImage generarQRComoImagen(String contenido, int ancho, int alto) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(contenido, BarcodeFormat.QR_CODE, ancho, alto);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            throw new RuntimeException("Error al generar el código QR", e);
        }
    }

    /**
     * Genera y guarda un código QR como archivo PNG.
     *
     * @param contenido Texto a codificar.
     * @param ancho     Ancho en píxeles.
     * @param alto      Alto en píxeles.
     * @param destino   Ruta del archivo PNG a crear.
     * @throws IOException si falla la escritura del archivo.
     */
    public static void guardarQRComoPNG(String contenido, int ancho, int alto, Path destino) throws IOException {
        BufferedImage imagen = generarQRComoImagen(contenido, ancho, alto);
        if (destino.getParent() != null) {
            Files.createDirectories(destino.getParent());
        }
        ImageIO.write(imagen, "png", destino.toFile());
    }
}
