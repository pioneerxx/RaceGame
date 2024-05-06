package edu.school21.raceserver.StaticFilesHandler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;

public class StaticFilesHandler implements HttpHandler {
    String filesPath;
    public StaticFilesHandler() throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(getClass().getResourceAsStream("/server.properties")));
        filesPath = properties.getProperty("static.files.path");
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        String requestContent = getContentType(uri);
        if (requestContent != null) {
            exchange.getResponseHeaders().set("Content-type", requestContent);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Access-Control-Allow-Origin", "*"); // Allow requests from any origin
            headers.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); // Allow GET, POST, OPTIONS methods
            headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Allow specified headers
        }
        File file = new File(filesPath + uri);
        if (file.exists()) {
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(Files.readAllBytes(file.toPath()));
            }
        } else {
            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private String getContentType(String uri) {
        String answer;
        if (uri.endsWith(".html")) {
            answer = "text/html";
        } else if (uri.endsWith(".js")) {
            answer = "text/javascript";
        } else if (uri.endsWith(".css")) {
            answer = "text/css";
        } else {
            answer = null;
        }
        return answer;
    }
}
