package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        // Endpoint GET /data
        server.createContext("/data", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Test response in JSON
            String response = "{ \"a\" : 1 }";

            Headers responseHeaders = exchange.getResponseHeaders();
            // reply with JSON
            responseHeaders.set("Content-Type", "application/json");
            // allow CORS from local browser; must use same port as front end hosted port
            responseHeaders.set("Access-Control-Allow-Origin", "http://localhost:8080");

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}