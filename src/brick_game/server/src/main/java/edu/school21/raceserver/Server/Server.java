package edu.school21.raceserver.Server;

import edu.school21.raceserver.ApiHandler.ApiHandler;
import edu.school21.raceserver.StaticFilesHandler.StaticFilesHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            StaticFilesHandler staticFilesHandler = new StaticFilesHandler();
            ApiHandler apiHandler = new ApiHandler();
            server.createContext("/", staticFilesHandler);
            server.createContext("/api", apiHandler);
            server.start();
            System.out.println("Server is running on port 8000");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
