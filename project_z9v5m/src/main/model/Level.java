package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a level that can be played by a user. Uses a list of pipes to represent each unit in the grid.
// The amount of pipes in the list cannot change but the pipes themselves can change. Contains a best time and a
// name for each level.
public class Level implements Writable {

    static final int BOARD_LENGTH = 10;
    static final int BOARD_AREA = BOARD_LENGTH * BOARD_LENGTH;
    static final int INITIAL_BEST_TIME = 1200; // I am hoping everyone can finish a game in 20 minutes :)

    private static final EventLog log = EventLog.getInstance();
    private final ArrayList<Pipe> pipes;
    private final String name;
    private final int connectorY; // Starting pipe on y-axis, I may remove final later on.
    private final int connectorX; // end pipe on x-axis
    private long bestTime;

    // EFFECTS: Initializes a level with given level information with 100 Null pipes. Event is logged.
    public Level(String name, int x, int y) {
        this.bestTime = INITIAL_BEST_TIME;
        this.name = name;
        this.connectorX = x;
        this.connectorY = y;
        pipes = new ArrayList<>(BOARD_AREA);

        for (int i = 0; i < BOARD_AREA; i++) {
            pipes.add(i, new Pipe());
        }
        Event createdLevel = new Event(this.name + " was created and added to list of levels");
        log.logEvent(createdLevel);
    }

    // REQUIRES: Level cannot be automatic win or else will endlessly loop
    // MODIFIES: this, Pipe
    // EFFECTS: Randomizes the rotation values of all pipes in the pipes list
    public void shuffle() {
        for (Pipe p : pipes) {
            p.randomizeUserRotation();
        }
    }

    // EFFECTS: checks if the pipe rotation matches the solution
    public boolean isSolution() {
        boolean solution = true;
        for (int i = 0; i < BOARD_AREA; i++) {
            Pipe currentPipe = pipes.get(i);
            if (!currentPipe.isEqualToSolution()) {
                solution = false;
            }
        }
        return solution;
    }

    // EFFECTS: checks if there are no possible rotations
    public boolean isAutomaticWin() {
        boolean isSoFarWin = true;
        for (Pipe p : pipes) {
            if (p.isChangeable()) {
                isSoFarWin = false;
            }
        }
        return isSoFarWin;
    }

    // MODIFIES: this
    // EFFECTS: replaces the best time variable if the new time is lower
    // and returns the true best time.
    public void checkBestTime(long newTime) {
        if (this.bestTime > newTime) {
            this.bestTime = newTime;
        }
    }

    // REQUIRES: pipeIndex < pipes.Size()
    // EFFECTS: retrieves pipe at given index in pipes
    public Pipe getPipe(int pipeIndex) {
        return pipes.get(pipeIndex);
    }

    // EFFECTS: returns JSONObject representation of a level
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("bestTime", bestTime);
        json.put("connectorY", connectorY);
        json.put("connectorX", connectorX);
        json.put("pipes", pipesToJson());
        return json;
    }

    // EFFECTS: produces JSONArray representation of the pipes field
    public JSONArray pipesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Pipe pipe : pipes) {
            jsonArray.put(pipe.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: EventLog, Event
    // EFFECTS: logs the displaying of levels
    public static void logDisplay() {
        Event displayedLevels = new Event("Levels were displayed in a JComboBox");
        log.logEvent(displayedLevels);
    }

    public String getName() {
        return this.name;
    }

    public long getBestTime() {
        return this.bestTime;
    }

    public int getConnectorY() {
        return this.connectorY;
    }

    public int getConnectorX() {
        return this.connectorX;
    }
}
