package Futures;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



//ESTA CLASE OBTENDRA LOS ARCHIVOS QUE HAY EN LA CARPETA "src/main/java/Futures/directoriosincomprimir" Y LOS COMPRIMIRA EN UN ARCHIVO ZIP LLAMADO "src/main/java/Futures/directorioscomprimidos/directorio.zip"
public class
ZipDirectorio {
    public static void main(String[] args) {
        // Directorio original
        String sourceDir = "src/main/java/Futures/directoriosincomprimir";
        // Archivo zip
        String zipFile = "archivocomprimido.zip";
        // Carpeta de destino
        String destinationDir = "src/main/java/Futures";

        // Crear una tarea asíncrona para comprimir el directorio
        CompletableFuture<Void> compressTask = CompletableFuture.runAsync(() -> {
            try {
                // Crear el archivo zip
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos);

                // Obtener una lista de todos los archivos en el directorio original
                File[] files = new File(sourceDir).listFiles();

                for (File file : files) {
                    if (file.isFile()) {
                        // Leer el contenido del archivo y escribirlo en el archivo zip
                        FileInputStream fis = new FileInputStream(file);
                        zos.putNextEntry(new ZipEntry(file.getName()));

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        zos.closeEntry();
                        fis.close();
                    }
                }

                zos.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Crear una tarea asíncrona para copiar el archivo zip a la carpeta de destino
        CompletableFuture<Void> copyTask = compressTask.thenRunAsync(() -> {
            try {
                Path source = Paths.get(zipFile);
                Path target = Paths.get(destinationDir + "/" + zipFile);
                if(!target.toFile().exists()){
                    Files.copy(source, target);
                    System.out.println("Archivo comprimido con exito!!!!!");
                }else {
                    System.out.println("Ya existe un archivo zip "+zipFile+" con el mismo nombre en la ruta "+destinationDir+".  Borrelo y ejecute de nuevo el programa!!!!!");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            // esperar a que las tareas terminen
            copyTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}