
package cifradosustitucion;

/**
 *
 * @author karol
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class CifradoSustitucion {

   private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyz";

    public static String cifrarSustitucion(String palabra, int clave) {
    StringBuilder textoCifrado = new StringBuilder();
    palabra = palabra.toLowerCase();

    for (char caracter : palabra.toCharArray()) {
        if (Character.isLetter(caracter)) {
            int indice = (ALFABETO.indexOf(caracter) + clave) % ALFABETO.length();
            if (indice < 0) {
                indice += ALFABETO.length();
            }
            textoCifrado.append(ALFABETO.charAt(indice));
        } else {
            textoCifrado.append(caracter);
        }
    }

    return textoCifrado.toString();
}

    public static String descifrarSustitucion(String textoCifrado, int clave) {
        return cifrarSustitucion(textoCifrado, -clave);
    }

    public static int generarClave() {
        Random random = new Random();
        return random.nextInt(26) + 1;
    }

    public static void main(String[] args) {
        String Entrada = "C:\\Users\\Lenovo\\Documents\\Universidad\\Sexto\\Seguridad\\TAREA08-U02-G04\\CifradoSustitucion\\archivo1000000.txt";
        String Salida = "C:\\Users\\Lenovo\\Documents\\Universidad\\Sexto\\Seguridad\\TAREA08-U02-G04\\CifradoSustitucion\\\\archivo_cifrado.txt";

        try {
            // Leer archivo con el texto a cifrar
            String textoPlano = new String(Files.readAllBytes(Paths.get(Entrada)));

            // Generar y/o imprimir la(s) clave(s) de cifrado
            int clave = generarClave();
            System.out.println("Clave de cifrado generada: " + clave);

            // Cifrar e imprimir el texto cifrado
            long startTime = System.nanoTime();
            String textoCifrado = cifrarSustitucion(textoPlano, clave);
            long endTime = System.nanoTime();
            System.out.println("Texto cifrado: " + textoCifrado);
            System.out.println("----------------------------------------------------------------------------------");

            // Descifrar e imprimir el texto claro
            startTime = System.nanoTime();
            String textoDescifrado = descifrarSustitucion(textoCifrado, clave);
            endTime = System.nanoTime();
            System.out.println("Texto descifrado: " + textoDescifrado);
            System.out.println("----------------------------------------------------------------------------------");

            try ( // Guardar el texto cifrado en un archivo de salida
                    FileWriter archivoSalidaWriter = new FileWriter(new File(Salida))) {
                archivoSalidaWriter.write(textoCifrado);
            }
            
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Tiempo para el cifrado: " + (endTime - startTime) + " nanosegundos");
            System.out.println("Tiempo para el descifrado: " + (endTime - startTime) + " nanosegundos");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("El archivo se ha cifrado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}