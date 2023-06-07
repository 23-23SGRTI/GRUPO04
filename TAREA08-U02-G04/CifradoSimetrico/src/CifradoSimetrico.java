import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class CifradoSimetrico {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("=== Menú ===");
            System.out.println("1. Procesar archivo con 10 palabras");
            System.out.println("2. Procesar archivo con 100 palabras");
            System.out.println("3. Procesar archivo con 1000 palabras");
            System.out.println("4. Procesar archivo con 10000 palabras");
            System.out.println("5. Procesar archivo con 100000 palabras");
            System.out.println("6. Procesar archivo con 1000000 palabras");
            System.out.println("Ingrese una opción: ");
            
            int option = Integer.parseInt(reader.readLine());
            
            String filename;
            
            switch (option) {
                case 1:
                    filename = "archivo10.txt";
                    break;
                case 2:
                    filename = "archivo100.txt";
                    break;
                case 3:
                    filename = "archivo1000.txt";
                    break;
                case 4:
                    filename = "archivo10000.txt";
                    break;
                case 5:
                    filename = "archivo100000.txt";
                    break;
                case 6:
                    filename = "archivo1000000.txt";
                    break;
                default:
                    System.out.println("Opción inválida. Saliendo del programa.");
                    return;
            }
            
            long startTime, endTime, elapsedTime;
            
            // Etapa 1: Leer archivo con el texto a cifrar
            startTime = System.nanoTime();
            String plainText = readFile(filename);
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            
            System.out.println("-> Etapa 1: Leer archivo.");
            System.out.println("Texto original: " + plainText);
            System.out.println("Tiempo transcurrido: " + elapsedTime + " nanosegundos.");
            
            // Etapa 2: Generar y/o imprimir la(s) claves de cifrado
            startTime = System.nanoTime();
            SecretKey secretKey = generateKey();
            byte[] encryptedKey = secretKey.getEncoded();
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            
            System.out.println("-> Etapa 2: Generar claves de cifrado.");
            System.out.println("Clave de cifrado: " + Arrays.toString(encryptedKey));
            System.out.println("Tiempo transcurrido: " + elapsedTime + " nanosegundos.");
            
            // Etapa 3: Cifrar e imprimir el texto cifrado
            startTime = System.nanoTime();
            byte[] encryptedText = encrypt(plainText, secretKey);
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            
            System.out.println("-> Etapa 3: Cifrar texto.");
            System.out.println("Texto cifrado: " + Arrays.toString(encryptedText));
            System.out.println("Tiempo transcurrido: " + elapsedTime + " nanosegundos.");
            
            // Etapa 4: Descifrar e imprimir el texto claro
            startTime = System.nanoTime();
            String decryptedText = decrypt(encryptedText, secretKey);
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            
            System.out.println("-> Etapa 4: Descifrar texto.");
            System.out.println("Texto descifrado: " + decryptedText);
            System.out.println("Tiempo transcurrido: " + elapsedTime + " nanosegundos.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String readFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        br.close();
        return sb.toString();
    }
    
    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }
    
    private static byte[] encrypt(String plainText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }
    
    private static String decrypt(byte[] cipherText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(cipherText);
        return new String(decryptedBytes);
    }
}
