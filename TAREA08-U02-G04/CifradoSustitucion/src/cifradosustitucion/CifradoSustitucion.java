
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
            // Leer archivo con el texto a cifrar T1
            long startTime = System.nanoTime();
            String textoPlano = new String(Files.readAllBytes(Paths.get(Entrada)));
            long endTime = System.nanoTime();

 

            // Generar y/o imprimir la(s) clave(s) de cifrado T2
            long startTime0 = System.nanoTime();
            int clave = generarClave();
            long endTime0 = System.nanoTime();
            System.out.println("Clave de cifrado generada: " + clave);

 

            // Cifrar e imprimir el texto cifrado T3
            long startTime1 = System.nanoTime();
            String textoCifrado = cifrarSustitucion(textoPlano, clave);
            long endTime1 = System.nanoTime();
            System.out.println("Texto cifrado: " + textoCifrado);
            System.out.println("----------------------------------------------------------------------------------");

 

            // Descifrar e imprimir el texto T3
            long startTime2 = System.nanoTime();
            String textoDescifrado = descifrarSustitucion(textoCifrado, clave);
            long endTime2 = System.nanoTime();
            System.out.println("Texto descifrado: " + textoDescifrado);
            System.out.println("----------------------------------------------------------------------------------");

 

            // Guardar el texto cifrado en un archivo de salida
            FileWriter archivoSalidaWriter = new FileWriter(new File(Salida));
            archivoSalidaWriter.write(textoCifrado);
            archivoSalidaWriter.close();
            System.out.println("----------------------------RESULTADOS--------------------------------------------");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Tiempo leer el archivo (T-E1): " + (endTime - startTime) + " nanosegundos");
            System.out.println("Tiempo generar clave (T-E2): " + (endTime0 - startTime0) + " nanosegundos");
            System.out.println("Tiempo para el cifrado (T-E3): " + (endTime1 - startTime1) + " nanosegundos");
            System.out.println("Tiempo para el descifrado (T-E4): " + (endTime2 - startTime2) + " nanosegundos");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("ÉXITO EL ARCHIVO SE CIFRÓ CORRECTAMENTE ");

 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}