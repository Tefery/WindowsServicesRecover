package com.mascas.windowsServicesRecover;

import org.mozilla.universalchardet.ReaderFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final boolean DEV_MODE = false;

    private static final String REG_HEADER_REGEX = "(^Windows Registry Editor Version (\\d)+\\.(\\d)+$)";
    private static final String RAIZ = "[HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\";
    private static final String RAIZ_COMPLETA = "[HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services]";
    private static final String REG_EXT = ".reg";
    private static final String SALTO = "\r\n";
    private static final String RE = "regedit";
    private static final String UNDERSCORE = "_";
    private static final String REGEDIT_SILENT_MODE = "/s";
    private static final String SOLO_DIVIDIR_MODE = "/d";
    private static final Set<String> NOMBRES_USADOS = new HashSet<>();

    private static FileOutputStream fileOutW;
    private static Writer writer;
    private static File fileOut;
    private static boolean soloDividir = false;
    private static int fileIndex = 0;
    private static Scanner scann;

    private static Scanner getScanner() {
        if (scann == null) {
            scann = new Scanner(System.in);
        }
        return scann;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println();

        if (args.length < 1) {
            System.out.println("Si no me dices que archivo es lo tenemos jodido...");
            return;
        }

        if (args.length > 1) {
            if (args.length == 2 && (args[fileIndex + 1].equalsIgnoreCase(SOLO_DIVIDIR_MODE) || args[fileIndex++].equalsIgnoreCase(SOLO_DIVIDIR_MODE))) {
                soloDividir = true;
            } else {
                StringBuilder errorMsg = new StringBuilder("A ver tont@polla, con que fichero trabajo entonces, con " + args[0]);
                for (int i = 1; i < args.length; i++) {
                    errorMsg.append(", o con ").append(args[i]);
                }
                errorMsg.append("?");
                System.out.println(errorMsg);
                return;
            }
        }

        String ruta = args[fileIndex];
        if (!ruta.toLowerCase().endsWith(REG_EXT)) {
            System.out.println("Seleccione un archivo de tipo \"reg\"");
            return;
        }

        File asd = new File(ruta);
        if (!asd.exists() || !asd.isFile()) {
            System.out.println("El archivo " + ruta + " no existe");
            return;
        }

        if (soloDividir) {
            System.out.println("Solo division activado, dividiendo el archivo " + asd.getName() + "...\n");
        } else {
            Scanner scan = getScanner();
            System.out.println("Se recomienda ejecutar la utilidad en modo a prueba de errores con simbolo del sistema para " +
                    "una restauración más completa, si no es el caso, te recomiendo que cierres ahora esta ventana y lo hagas." +
                    "\nSi pasas del tema y quieres continuar, pulsa Enter...");
            scan.nextLine();
        }


        String salida = asd.getAbsolutePath();
        salida = salida.substring(0, salida.lastIndexOf(File.separator)) + File.separator + "salida" + File.separator;
        File qwe = new File(salida);

        if (qwe.exists() && qwe.isDirectory()) {
            if (qwe.list().length > 0) {
                System.out.println("La carpeta de salida para preparar la division \"" + salida + "\" ya existe, si continuas se procedera al borrado...");
                String respuesta;
                int tontoCounter = 0;
                do {
                    Scanner scan = getScanner();
                    System.out.print("Estas de acuerdo? S/N: ");
                    respuesta = scan.nextLine();
                } while (respuesta.length() == 0 && ++tontoCounter < 5);
                switch (respuesta.toUpperCase()) {
                    case "N", "NO":
                        System.out.println("Haz lo que te salga del nardo/conio con la carpeta y vuelve a intentarlo...");
                        return;
                    case "S", "SI", "Y", "YES":
                        System.out.println("Haciendo como que esto nunca ha ocurrido...\n");
                        break;
                    case "":
                        System.out.println("Ya hemos tocado con el tonto, pues te quedas sin arreglo...");
                        return;
                    default:
                        System.out.println(respuesta + " no es una respuesta valida, dare por hecho que era un no...");
                        return;
                }
            }
            deleteDirectoryStream(qwe.toPath());
        }
        qwe.mkdir();

        String newService;
        String newServiceCache;
        String line;
        int findCounter;

        BufferedReader zxc = ReaderFactory.createBufferedReader(asd);
        fileOutW = null;
        writer = null;
        line = zxc.readLine();
        if (!line.matches(REG_HEADER_REGEX)) {
            System.out.println("Fichero incompleto, vete a cagar a la via...");
            return;
        }
        final String originalRegHeader = line;
        zxc.readLine();
        line = zxc.readLine();
        if (!line.equals(RAIZ_COMPLETA)) {
            System.out.println("El fichero tiene que partir de \"" + RAIZ_COMPLETA + "\", exporta del lugar correcto y vuelve a intentarlo...");
            return;
        }
        zxc.readLine();

        if (!soloDividir) {
            System.out.println("Comenzando escritura en el registro, sobra decir que no toques nada hasta que termine...\n");
        }

        while ((line = zxc.readLine()) != null) {
            if (line.startsWith(RAIZ)) {
                writeAndEjecute();
                findCounter = 2;
                newService = line.substring(RAIZ.length(), line.length() - 1).replace("\\", UNDERSCORE).replace("/", UNDERSCORE).replace(File.separator, UNDERSCORE);
                newServiceCache = newService;
                while (!NOMBRES_USADOS.add(newServiceCache)) {
                    newServiceCache = newService + UNDERSCORE + findCounter++;
                }
                newService = newServiceCache;
                fileOut = new File(salida + newService + REG_EXT);
                fileOutW = new FileOutputStream(fileOut);
                writer = new BufferedWriter(new OutputStreamWriter(fileOutW, StandardCharsets.US_ASCII));
                writer.write(originalRegHeader + SALTO);
                writer.write(SALTO);
            }
            writer.write(line + SALTO);
        }
        zxc.close();
        writeAndEjecute();

        System.out.println("HABEMUS TERMINADO...\n");
        System.out.print("Los archivos usados no se han borrado, si no los querias te jodes ");
        System.out.println("¯\\_(ツ)_/¯");
    }

    private static void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private static void writeAndEjecute() throws IOException, InterruptedException {
        if (fileOutW != null) {
            writer.close();
            fileOutW.close();
            if (!soloDividir && !DEV_MODE) {
                ProcessBuilder processBuilder = new ProcessBuilder(RE, REGEDIT_SILENT_MODE, fileOut.getAbsolutePath());
                Process proceso = processBuilder.start();
                if (proceso.waitFor() != 0) {
                    System.out.println("algo no ha ido bien con el fichero " + fileOut.getAbsolutePath());
                }
            }
        }
    }

}
