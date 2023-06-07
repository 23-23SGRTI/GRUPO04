
package encriptacionasimetrica;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncriptacionAsimetrica {


    public static void main(String[] args) {
                System.out.println("--------------------------------------------");
        System.out.println("Eliga el numero de palabras que desea cifrar");
        System.out.println("--------------------------------------------");
        System.out.println("1. Archivo de 10 palabras.");
        System.out.println("2. Archivo de 100 palabras.");
        System.out.println("3. Archivo de 1000 palabras.");
        System.out.println("4. Archivo de 10000 palabras.");
        System.out.println("5. Archivo de 100000 palabras.");
        System.out.println("6. Archivo de 1000000 palabras.");
        System.out.println("7. Salir\n");
        int i = 0;
        Scanner leer =new Scanner(System.in);
        i=leer.nextInt();

        switch (i) {
            case 1:
                System.out.println("\nHa seleccionado un Archivo de 10 palabras."); 
                Proceso("archivo10.txt");
                break;
            case 2:
                System.out.println("\nHa seleccionado un Archivo de 100 palabras.");
                Proceso("archivo100.txt");
                break;
            case 3:
                System.out.println("\nHa seleccionado un Archivo de 1000 palabras.");
                Proceso("archivo1000.txt");
                break;
            case 4:
                System.out.println("\nHa seleccionado un Archivo de 10000 palabras.");
                Proceso("archivo10000.txt");
                break;
            case 5:
                System.out.println("\nHa seleccionado un Archivo de 100000 palabras.");
                Proceso("archivo100000.txt");
                break;
            case 6:
                System.out.println("\nHa seleccionado un Archivo de 1000000 palabras.");
                Proceso("archivo1000000.txt");
                break;
            default:
                System.out.println("\nUsted a salido del programa");
        }
    }
    
    public static void Proceso(String cifrarArchivo) {
        try {

            System.out.println("\nLeyendo archivo...");
            System.out.println("Generando claves...");
            long startLeerArchivo = System.nanoTime();
            byte[] Archivos = leerArchivo(cifrarArchivo);
            long endLeerArchivo = System.nanoTime();
            
            // Generar las claves pública y privada para el receptor
            long startProcesoClaves=System.nanoTime();
            KeyPairGenerator clavesReceptor = KeyPairGenerator.getInstance("DiffieHellman");
            KeyPair parClavesReceptor = clavesReceptor.generateKeyPair();
            PrivateKey clavePrivadaReceptor = parClavesReceptor.getPrivate();
            PublicKey clavePublicaReceptor = parClavesReceptor.getPublic();

            // Guardar la clave pública del receptor en un archivo
            guardarClavePublica(clavePublicaReceptor, "clavePublicaReceptor.key");

            // Generar las claves pública y privada para el emisor
            KeyPairGenerator clavesEmidor = KeyPairGenerator.getInstance("DiffieHellman");
            KeyPair parClavesEmisor = clavesEmidor.generateKeyPair();
            PrivateKey clavePrivadaEmisor = parClavesEmisor.getPrivate();
            PublicKey clavePublicaEmisor = parClavesEmisor.getPublic();

            // Mostrar la clave privada del emisor
            String clavePrivadaEmisorString = Base64.encode(clavePrivadaEmisor.getEncoded());
            System.out.println("Clave privada del emisor: " + clavePrivadaEmisorString);

            // Mostrar la clave pública del emisor            
            String clavePublicaEmisorString = Base64.encode(clavePublicaEmisor.getEncoded());
            System.out.println("Clave pública del emisor: " + clavePublicaEmisorString);
            
            // Cargar la clave pública del receptor desde el archivo
            PublicKey cargarClavePublicaReceptor = cargarClavePublica("receiverPublicKey.key");

            // Crear el objeto KeyAgreement para el emisor y establecer su clave privada
            KeyAgreement senderKeyAgreement = KeyAgreement.getInstance("DiffieHellman");
            senderKeyAgreement.init(clavePrivadaEmisor);

            // Generar el secreto compartido utilizando la clave pública del receptor
            senderKeyAgreement.doPhase(cargarClavePublicaReceptor, true);
            byte[] sharedSecret = senderKeyAgreement.generateSecret();

            // Crear una clave secreta a partir del secreto compartido
            SecretKey secretKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");

            // Mostrar la clave privada del receptor
            String clavePrivadaReceptorString = Base64.encode(clavePrivadaReceptor.getEncoded());
            System.out.println("Clave privada del receptor: " + clavePrivadaReceptorString);

            // Mostrar la clave pública del receptor
            String clavePublicaReceptorString = Base64.encode(clavePublicaReceptor.getEncoded());
            System.out.println("Clave pública del receptor: " + clavePublicaReceptorString);
            long endProcesoClaves = System.nanoTime();

            // Encriptar el archivo de ejemplo utilizando la clave secreta
            long startEncriptarArchivo=System.nanoTime();
            encriptarArchivo(cifrarArchivo, "archivoEncriptado.txt", secretKey);
            long endEncriptarArchivo=System.nanoTime();
            long tiempoCifrado=endEncriptarArchivo-startEncriptarArchivo;

            // Desencriptar el archivo cifrado utilizando la clave secreta
            long startDesencriptarArchivo=System.nanoTime();
            desencriptarArchivo("archivoEncriptado.txt", "archivoDesencriptado.txt", secretKey);
            long endDesencriptarArchivo=System.nanoTime();
            long tiempoDescifrado=endDesencriptarArchivo-startDesencriptarArchivo;
            
            // Mostrar el contenido del archivo encriptado
            System.out.println("\nARCHIVO ENCRIPTADO\n");
            String contenidoArchivoEncriptado = mostrarArchivoEncriptado("archivoEncriptado.txt");
            System.out.println(contenidoArchivoEncriptado);

            // Mostrar el contenido del archivo desencriptado
            System.out.println("\nARCHIVO DESENCRIPTADO\n");
            mostrarArchivo("archivoDesencriptado.txt");

            System.out.println("\nEncriptación y desencriptación completadas correctamente.");
            
            // Mostrar el tiempo de lectura del archivo
            System.out.println("1. Tiempo en leer un archivo con el texto a cifrar (T-E1): "+ (endLeerArchivo - startLeerArchivo) + " ns");
            
            //mostrar el tiempo de generar las claves de cifrado
            System.out.println("2. Tiempo en Generar y/o imprimir las claves de cifrado (T-E2): "+(endProcesoClaves-startProcesoClaves)+" ns");
            
            //mostrar el tiempo cifrar e imprimir el texto cifrado.
            System.out.println("3. Tiempo en Cifrar e imprimir el texto cifrado (T-E3): "+tiempoCifrado+" ns");
            
            //Mostrar el tiempo descifrar e imprimir el texto claro
            System.out.println("4. Tiempo en Descifrar e imprimir el texto claro (T-E4): "+tiempoDescifrado+" ns");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void guardarClavePublica(PublicKey publicKey, String fileName) throws IOException {
        byte[] encodedKey = publicKey.getEncoded();
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(encodedKey);
        fos.close();
    }

    private static PublicKey cargarClavePublica(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] encodedKey = new byte[fis.available()];
        fis.read(encodedKey);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private static void encriptarArchivo(String inputFile, String outputFile, SecretKey secretKey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] buffer = new byte[8192];
        int bytesRead;
        
        while ((bytesRead = fis.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] output = cipher.doFinal();
        if (output != null) {
            fos.write(output);
        }

        fis.close();
        fos.flush();
        fos.close();
    }
    
     private static void desencriptarArchivo(String inputFile, String outputFile, SecretKey secretKey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] output = cipher.doFinal();
        if (output != null) {
            fos.write(output);
        }

        fis.close();
        fos.flush();
        fos.close();
    }
     
    public static void mostrarArchivo(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static byte[] leerArchivo(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        byte[] fileData = new byte[(int) file.length()];
        fis.read(fileData);
        fis.close();
        return fileData;
    }
    
    private static String mostrarArchivoEncriptado(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] encryptedBytes = new byte[fis.available()];
        fis.read(encryptedBytes);
        fis.close();

        return Base64.encode(encryptedBytes);
    }
}

