package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import data.JavaParser;
import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Server {
    private static final String ASSETSPATH= "assets/";
    private static final String PROJECT_TO_PARSE = "project_to_parse/";

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
            String downloadUrlString = textBuilder.toString();

            List<String> downloadUrls = new ArrayList<>();
            downloadUrls = parseListFromServer(downloadUrlString);

            try {
                for (String url: downloadUrls) {
                    download(url);
                }
            } catch (Throwable e) {
                System.out.println("Error while downloading");
            }

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

        private static boolean isRedirected( Map<String, List<String>> header ) {
            for( String hv : header.get( null )) {
                if(   hv.contains( " 301 " )
                        || hv.contains( " 302 " )) return true;
            }
            return false;
        }

        private static void download(String link) throws Throwable {
            String            fileName = getFileName(link);
            URL               url  = new URL( link );
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            Map< String, List< String >> header = http.getHeaderFields();
            while( isRedirected( header )) {
                link = header.get( "Location" ).get( 0 );
                url    = new URL( link );
                http   = (HttpURLConnection)url.openConnection();
                header = http.getHeaderFields();
            }
            InputStream  input  = http.getInputStream();
            byte[]       buffer = new byte[4096];
            int          n      = -1;

            String filePath = ASSETSPATH + PROJECT_TO_PARSE;
            OutputStream output = new FileOutputStream( new File( filePath + fileName ));
            while ((n = input.read(buffer)) != -1) {
                output.write( buffer, 0, n );
            }
            output.close();
        }

        private List<String> parseListFromServer(String str) {
            List<String> returnedList = Arrays.asList(str.split(","));
            return returnedList;
        }
        private static String getFileName(String link) {
            List<String> splitedUrl = Arrays.asList(link.split("/"));
            String filename = splitedUrl.get(splitedUrl.size() - 1);

            return filename;

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
