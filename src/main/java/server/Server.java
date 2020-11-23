package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server {

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        // Endpoint GET /data
        server.createContext("/data", new MyHandler());
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
                download();
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

        private static void download() throws Throwable {
            String link =
                    "https://raw.githubusercontent.com/dwmkerr/spaceinvaders/master/js/spaceinvaders.js";
            String            fileName = "spaceinvaders.js";
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
            OutputStream output = new FileOutputStream( new File( fileName ));
            while ((n = input.read(buffer)) != -1) {
                output.write( buffer, 0, n );
            }
            output.close();
        }

        private List<String> parseListFromServer(String str) {
            List<String> returnedList = Arrays.asList(str.split(","));
            return returnedList;
        }
        }
    }
