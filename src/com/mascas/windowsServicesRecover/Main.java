package com.mascas.windowsServicesRecover;

import org.mozilla.universalchardet.ReaderFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class Main {

    private static final String REG_HEADER_REGEX = "(^Windows Registry Editor Version (\\d)+\\.(\\d)+$)";
    private static final String RAIZ = "[HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\";
    private static final String REG_EXT = ".reg";
    private static final String SALTO = "\r\n";
    private static final String RE = "regedit";
    private static final String UNDERLINE = "_";
    private static final String SILENT_MODE = "/s";

    private static FileOutputStream fileOutW;
    private static Writer writer;
    private static File fileOut;

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 1) {
            System.out.println("Si no me dices que archivo es lo tenemos jodido...");
            return;
        }

        if(args.length > 1) {
            String errorMsg = "A ver tontopolla, con que fichero trabajo entonces, con " + args[0];
            for (int i = 1; i < args.length; i++) {
                errorMsg += ", o con " + args[i];
            }
            System.out.println(errorMsg + "?");
            return;
        }

        String ruta = args[0];
        if (!ruta.toLowerCase().endsWith(REG_EXT)) {
            System.out.println("Seleccione un archivo de tipo \"reg\"");
            return;
        }

        File asd = new File(ruta);
        if (!asd.exists() || !asd.isFile()) {
            System.out.println("El archivo " + ruta + " no existe");
            return;
        }

        String salida = asd.getAbsolutePath();
        salida = salida.substring(0, salida.lastIndexOf(File.separator)) + File.separator + "salida" + File.separator;
        File qwe = new File(salida);

        if (qwe.exists() && qwe.isDirectory()) {
            deleteDirectoryStream(qwe.toPath());
        }
        qwe.mkdir();

        String newService;
        String line;

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
        if (!line.equals("[HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services]")) {
            System.out.println("El fichero tiene que partir de \"" + RAIZ + "\", exporta del lugar correcto y vuelve a intentarlo...");
        }
        zxc.readLine();

        while ((line = zxc.readLine()) != null) {
            if (line.startsWith(RAIZ)) {
                writeAndEjecute();
                newService = line.substring(RAIZ.length(), line.length() - 1).replace("\\", UNDERLINE).replace("/", UNDERLINE).replace(File.separator, UNDERLINE) + " - " + ((int)(1500 * Math.random()));
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

        System.out.println("HABEMUS TERMINADO...");
        System.out.println("Los archivos usados no se han borrado, si no los querias te jodes ¯\\_(ツ)_/¯");
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
            ProcessBuilder processBuilder = new ProcessBuilder(RE, SILENT_MODE, fileOut.getAbsolutePath());
            Process proceso = processBuilder.start();
            if(proceso.waitFor() != 0) {
                System.out.println("algo no ha ido bien con el fichero " + fileOut.getAbsolutePath());
            }
        }
    }

}
