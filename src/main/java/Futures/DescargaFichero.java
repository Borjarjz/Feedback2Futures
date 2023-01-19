package Futures;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.*;
import java.awt.Desktop;

public class DescargaFichero {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        URL website = new URL("https://raw.githubusercontent.com/Borjarjz/Feedback2Futures/master/textoleer.txt");

        Future<File> future = executor.submit(() -> {
            try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                 FileOutputStream fos = new FileOutputStream("src/main/java/Futures/directoriosincomprimir/file.txt")) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            return new File("src/main/java/Futures/directoriosincomprimir/file.txt");
        });
        File file = future.get();
        Desktop.getDesktop().open(file);
    }
}