package persistence;

import model.Level;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
// Code templated from: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a writer that writes JSON representation of the game to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of levels to file
    public void write(ArrayList<Level> levels) {
        JSONObject json = new JSONObject();
        json.put("levels", levelsToJson(levels));
        saveToFile(json.toString(TAB));
    }

    // EFFECTS: writes JSON representation of levels to file
    public JSONArray levelsToJson(ArrayList<Level> levels) {
        JSONArray jsonArray = new JSONArray();
        for (Level level : levels) {
            jsonArray.put(level.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
