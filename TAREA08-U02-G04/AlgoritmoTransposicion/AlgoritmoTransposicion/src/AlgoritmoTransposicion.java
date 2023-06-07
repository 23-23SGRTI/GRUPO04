import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class AlgoritmoTransposicion {
    private static final int[] NUM_PALABRAS = { 10, 100, 1000, 10000, 100000, 1000000 };

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        while (true) {
            System.out.println("***************MENU***************");
            System.out.println("1. Procesar archivo con 10 palabras.");
            System.out.println("2. Procesar archivo con 100 palabras.");
            System.out.println("3. Procesar archivo con 1000 palabras.");
            System.out.println("4. Procesar archivo con 10000 palabras.");
            System.out.println("5. Procesar archivo con 100000 palabras.");
            System.out.println("6. Procesar archivo con 1000000 palabras.");
            System.out.println("0. Salir.");
            System.out.println("***********************************");

            int opcion = obtenerEntero("Seleccione una opción: ");

            if (opcion == 0) {
                break;
            } else if (opcion >= 1 && opcion <= NUM_PALABRAS.length) {
                int numPalabras = NUM_PALABRAS[opcion - 1];
                String nombreArchivo = "texto_" + numPalabras + ".txt";
                procesarArchivo(nombreArchivo);
            } else {
                System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
            }
        }
    }

    public static void procesarArchivo(String nombreArchivo) {
        System.out.println("Procesando archivo: " + nombreArchivo);

        // Leer el archivo
        long inicioLectura = System.nanoTime();
        String textoPlano = leerArchivo(nombreArchivo);
        long finLectura = System.nanoTime();
        double tiempoLectura = (finLectura - inicioLectura) / 1e9;

        // Generar clave de cifrado
        long inicioClave = System.nanoTime();
        int[] clave = generarClave(textoPlano.length());
        long finClave = System.nanoTime();
        double tiempoClave = (finClave - inicioClave) / 1e9;

        System.out.println("Clave de cifrado: " + Arrays.toString(clave));

        // Cifrar el texto
        long inicioCifrado = System.nanoTime();
        String textoCifrado = cifrarTransposicion(textoPlano, clave, true);
        long finCifrado = System.nanoTime();
        double tiempoCifrado = (finCifrado - inicioCifrado) / 1e9;

        System.out.println("Texto cifrado: " + textoCifrado);

        // Descifrar el texto
        long inicioDescifrado = System.nanoTime();
        String textoDescifrado = descifrarTransposicion(textoCifrado, clave, true);
        long finDescifrado = System.nanoTime();
        double tiempoDescifrado = (finDescifrado - inicioDescifrado) / 1e9;

        System.out.println("Texto descifrado: " + textoDescifrado);

        System.out.println("Tiempo de lectura: " + tiempoLectura + " ns");
        System.out.println("Tiempo de generación de clave: " + tiempoClave + " ns");
        System.out.println("Tiempo de cifrado: " + tiempoCifrado + " ns");
        System.out.println("Tiempo de descifrado: " + tiempoDescifrado + " ns");

        System.out.println("----------------------------------------");
    }

    public static String leerArchivo(String nombreArchivo) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int[] generarClave(int longitud) {
        int[] clave = new int[longitud];
        for (int i = 0; i < longitud; i++) {
            clave[i] = i;
        }
        Random random = new Random();
        for (int i = longitud - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = clave[i];
            clave[i] = clave[j];
            clave[j] = temp;
        }
        return clave;
    }
    

    public static String cifrarTransposicion(String textoPlano, int[] clave, boolean porFilas) {
        int longitud = clave.length;
        StringBuilder textoCifrado = new StringBuilder();

        if (porFilas) {
            int filas = (textoPlano.length() + longitud - 1) / longitud;
            char[][] matriz = new char[filas][longitud];

            int k = 0;
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < longitud; j++) {
                    if (k < textoPlano.length()) {
                        matriz[i][j] = textoPlano.charAt(k++);
                    } else {
                        matriz[i][j] = ' ';
                    }
                }
            }

            for (int i : clave) {
                for (int j = 0; j < filas; j++) {
                    textoCifrado.append(matriz[j][i]);
                }
            }
        } else {
            int columnas = (textoPlano.length() + longitud - 1) / longitud;
            char[][] matriz = new char[longitud][columnas];

            int k = 0;
            for (int i = 0; i < longitud; i++) {
                for (int j = 0; j < columnas; j++) {
                    if (k < textoPlano.length()) {
                        matriz[i][j] = textoPlano.charAt(k++);
                    } else {
                        matriz[i][j] = ' ';
                    }
                }
            }

            for (int i = 0; i < columnas; i++) {
                for (int j : clave) {
                    textoCifrado.append(matriz[j][i]);
                }
            }
        }

        return textoCifrado.toString();
    }

    public static String descifrarTransposicion(String textoCifrado, int[] clave, boolean porFilas) {
        int longitud = clave.length;
        StringBuilder textoDescifrado = new StringBuilder();

        if (porFilas) {
            int filas = (textoCifrado.length() + longitud - 1) / longitud;
            char[][] matriz = new char[filas][longitud];

            int k = 0;
            for (int i : clave) {
                for (int j = 0; j < filas; j++) {
                    matriz[j][i] = textoCifrado.charAt(k++);
                }
            }

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < longitud; j++) {
                    textoDescifrado.append(matriz[i][j]);
                }
            }
        } else {
            int columnas = (textoCifrado.length() + longitud - 1) / longitud;
            char[][] matriz = new char[longitud][columnas];

            int k = 0;
            for (int i = 0; i < columnas; i++) {
                for (int j : clave) {
                    matriz[j][i] = textoCifrado.charAt(k++);
                }
            }

            for (int i = 0; i < longitud; i++) {
                for (int j = 0; j < columnas; j++) {
                    textoDescifrado.append(matriz[i][j]);
                }
            }
        }

        return textoDescifrado.toString().trim();
    }

    public static int obtenerEntero(String mensaje) {
        int valor = 0;
        boolean entradaValida = false;
        do {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print(mensaje);
                valor = Integer.parseInt(br.readLine());
                entradaValida = true;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Entrada inválida. Intente nuevamente.");
            }
        } while (!entradaValida);
        return valor;
    }
}
