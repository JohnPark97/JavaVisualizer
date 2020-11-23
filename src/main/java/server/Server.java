package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import data.JavaParser;
import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Endpoint GET /data
        server.createContext("/data", new MyHandler());
        // Endpoint POST /dataFromZip
        server.createContext("/dataFromZip", new MyZipHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server running on port: " + port);
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }
            String gitUrl = textBuilder.toString();
            System.out.println(gitUrl);

            // Test response in JSON
            String response = "{ \"a\" : 1 }";

            Headers responseHeaders = exchange.getResponseHeaders();
            // reply with JSON
            responseHeaders.set("Content-Type", "application/json");
            // allow CORS from local browser; must use same port as front end hosted port
            responseHeaders.set("Access-Control-Allow-Origin", "*");

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class MyZipHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            byte[] data = inputStream.readAllBytes();

            // Load user input to assets/user_input.zip
            String zipPath = "assets/user_input.zip";
            Path path = Paths.get(zipPath);
            Files.write(path, data);

            // Unzip to filePath
            String filePath = "assets/project_to_parse";
            new ZipFile(Paths.get(zipPath).toFile())
                    .extractAll(Paths.get(filePath).toString());

            // Run it through the parser
            JavaParser jp = new JavaParser();
            jp.parse();

            // Test response in JSON
            String jsonFilePath = "assets/project.json";
            String response = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            Headers responseHeaders = exchange.getResponseHeaders();
            // reply with JSON
            responseHeaders.set("Content-Type", "application/json");
            // allow CORS from local browser; must use same port as front end hosted port
            responseHeaders.set("Access-Control-Allow-Origin", "*");

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}