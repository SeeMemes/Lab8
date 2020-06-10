package Server.Tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConvertToUTF {
    public static void Convert (String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileName);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String input = "";
        while (reader.ready()) input += reader.readLine() + "\n";
        byte[] bytes = ("\uFEFF" + input).getBytes(StandardCharsets.UTF_8);
        Path path = Paths.get(fileName);
        Files.write(path, bytes);
    }
}
