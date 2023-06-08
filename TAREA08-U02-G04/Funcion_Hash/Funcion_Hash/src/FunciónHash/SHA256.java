package FunciónHash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static void main(String[] args) {
        
        // Ruta del archivo con el texto a cifrar
        String ruta = "C:\Users\karol\Documents\Sexto Semestre\Seguridad en TI\Funcion_Hash\Funcion_Hash\\archivo100.txt";

        try {
            
            long tiempoI1 = System.nanoTime();
            String content = readFile(ruta); // Leer el contenido del archivo
            long  tiempoF1 = System.nanoTime();
            long   T1 = tiempoF1 - tiempoI1; // Tiempo final de lectura del archivo
            
            
            long tiempoI2 = System.nanoTime(); // Obtener el tiempo actual en nanosegundos
            String hash = SHA256(content); // Calcular el valor hash SHA-256 del contenido
            long tiempoF2 = System.nanoTime(); // Obtener el tiempo actual en nanosegundos             
            long T2 = tiempoF2 - tiempoI2; // Calcular el tiempo transcurrido hash 
            System.out.println("------------------------------------------------");
            System.out.println("***************FUNCIÓN HASH*********************");
            System.out.println("------------------------------------------------");
            System.out.println("algoritmo: SHA-256");
            System.out.println("hash : " + hash); // Imprimir el valor hash
            System.out.println("Tiempo de lectura del archivo: "+ T1+ " nanosegundos" );
            System.out.println("Tiempo de cifrado: " + T2 + " nanosegundos"); // Imprimir el tiempo transcurrido
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //  //Leer el contenido del archivo y devolverlo como una cadena
    public static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line); // Agregar la línea al contenido
            content.append(System.lineSeparator()); 
        }

        reader.close();
        return content.toString();
    }

    // Calcular el valor hash SHA-256 del contenido y devolverlo como una cadena hexadecimal
    public static String SHA256(String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Obtener una instancia del algoritmo SHA-256
        byte[] encodedHash = digest.digest(content.getBytes()); // Calcular el hash del contenido
        StringBuilder hexString = new StringBuilder();

        // Convertir los bytes del hash en una cadena hexadecimal
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b); // Convertir el byte a una cadena hexadecimal
            if (hex.length() == 1) {
                hexString.append('0'); // Añadir un 0 inicial si la cadena hexadecimal tiene un solo dígito
            }
            hexString.append(hex); // Agregar la cadena hexadecimal al resultado final
        }

        
        return hexString.toString();
    }
}