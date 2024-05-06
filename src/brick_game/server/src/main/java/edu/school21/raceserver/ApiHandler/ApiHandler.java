package edu.school21.raceserver.ApiHandler;

import edu.school21.raceserver.GameResultRepository.GameResultRepository;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.school21.racelogic.MapMatrix.MapMatrix;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import edu.school21.raceserver.DataSource.HikariDS;

public class ApiHandler implements HttpHandler {
    private GameResultRepository resultRepository;
    private final MapMatrix matrix;
    public ApiHandler() {
        matrix = new MapMatrix();
        try {
            HikariDS ds = new HikariDS();
            resultRepository = new GameResultRepository(ds.getDataSource());
        } catch (Exception e) {
            System.err.println("Database unavailable");
            resultRepository = null;
        }
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        if (exchange.getRequestMethod().equalsIgnoreCase("GET") && resultRepository != null) {
            exchange.getResponseHeaders().add("Content-type", "text/plain");
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(Long.toString(resultRepository.getHighScore()).getBytes());
            }
            return;
        }
        StringBuilder requestBody = new StringBuilder();
        InputStreamReader is = new InputStreamReader(exchange.getRequestBody());
        BufferedReader reader = new BufferedReader(is);
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        JSONObject request = new JSONObject(requestBody.toString());
        switch (request.getString("Action")) {
            case "Start":
                if (resultRepository != null)
                    matrix.setHighScore(resultRepository.getHighScore());
                matrix.userInput(MapMatrix.Action.Start);
                break;
            case "Terminate":
                matrix.userInput(MapMatrix.Action.Terminate);
                if (resultRepository != null)
                    saveResult(matrix.isNewHighScore());
                break;
            case "Left":
                matrix.userInput(MapMatrix.Action.Left);
                break;
            case "Right":
                matrix.userInput(MapMatrix.Action.Right);
                break;
            case "Up":
                matrix.userInput(MapMatrix.Action.Up);
                break;
            case "Down":
                matrix.userInput(MapMatrix.Action.Down);
                break;
            case "Pause":
                matrix.userInput(MapMatrix.Action.Pause);
                break;
            case "Action":
                matrix.updateState();
                break;
        }
        exchange.getResponseHeaders().add("Content-type", "text/json");
        exchange.sendResponseHeaders(200, 0);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < matrix.getMap().length; i++) {
            JSONArray innerArray = new JSONArray();
            for (int j = 0; j < matrix.getMap()[i].length; j++)
                innerArray.put(matrix.getMap()[i][j]);
            array.put(innerArray);
        }
        json.put("map", array);
        json.put("score", matrix.getScore());
        json.put("level", matrix.getLevel());
        json.put("speed", matrix.getSpeed());
        json.put("score", matrix.getScore());
        json.put("topScore", matrix.getHighScore());
        json.put("state", matrix.getState());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.toString().getBytes());
        }
    }

    private void saveResult(boolean flag) {
        if (flag) {
            try {
                resultRepository.save(matrix.getScore());
            } catch (SQLException e) {}
        }
    }
}
