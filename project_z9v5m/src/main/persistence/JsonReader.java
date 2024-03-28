package persistence;

import model.Level;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.Pipe;
import model.PipeType;
import org.json.*;
// Code templated from: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads a levels list from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads levels from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<Level> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses levels from JSON object and returns it
    private ArrayList<Level> parseWorkRoom(JSONObject jsonObject) {
        ArrayList<Level> levels = new ArrayList<>();
        loadLevels(levels, jsonObject);
        return levels;
    }

    // MODIFIES: PipeGameApp, Level, Pipe
    // EFFECTS: parses levels from JSON object and adds them to levels list
    private void loadLevels(ArrayList<Level> levels, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("levels");
        for (Object json : jsonArray) {
            JSONObject nextlevel = (JSONObject) json;
            loadLevel(levels, nextlevel);
        }
    }

    // MODIFIES: PipeGameApp, Level, Pipe
    // EFFECTS: parses level from JSON object and adds it to the level jsonArray
    private void loadLevel(ArrayList<Level> levels, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        long bestTime = jsonObject.getLong("bestTime");
        int connectorY = jsonObject.getInt("connectorY");
        int connectorX = jsonObject.getInt("connectorX");

        Level l = new Level(name, connectorX, connectorY);
        l.checkBestTime(bestTime); // Will always replace the time (unless default best time)
        loadPipes(l, jsonObject);
        levels.add(l);
    }

    // MODIFIES: PipeGameApp, Level, Pipe
    // EFFECTS: parses pipes from JSON object and adds it to level
    private void loadPipes(Level l, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("pipes");
        int indexCounter = 0;
        for (Object json : jsonArray) { // will always be 100 elements
            JSONObject nextPipe = (JSONObject) json;
            loadPipe(l, nextPipe, indexCounter);
            indexCounter++;
        }
    }

    // MODIFIES: PipeGameApp, Level, Pipe
    // EFFECTS: parses pipe from JSON object and adds it to the pipe jsonArray
    public void loadPipe(Level l, JSONObject jsonObject, int pipeIndex) {
        PipeType type = PipeType.valueOf(jsonObject.getString("type"));
        int correctRotation = jsonObject.getInt("correctRotation");
        Pipe currentPipe = l.getPipe(pipeIndex);
        currentPipe.setType(type);
        currentPipe.setCorrectRotation(correctRotation);
    }
}
