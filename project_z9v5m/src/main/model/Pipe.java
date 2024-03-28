package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Random; // only used the random integer function

import static model.PipeType.*;
// Random Source: https://www.educative.io/answers/how-to-generate-random-numbers-in-java

// Represents each pipe or each grid spot where a pipe can go (null). Can be rotated
// and can represent different shapes of pipes (does not represent start and end pipe)
public class Pipe implements Writable {

    private PipeType type;
    private static final EventLog log = EventLog.getInstance();
    private int userRotation; // Can be 0 - 3 if type L_SHAPE, T_SHAPE,
    // Can be 0 - 1 if type STRAIGHT
    private int correctRotation; // Same conditions as userRotation

    // EFFECTS: Sets rotation values to default 0 and pipe types as NULL.
    public Pipe() {
        this.type = NULL;
        this.userRotation = 0;
        this.correctRotation = 0;
    }

    // EFFECTS: returns pipe image link directory based on user rotation and type.
    public String getPipeSymbol(char boardType) {
        int rotationVal;
        if (boardType == 'u') {
            rotationVal = this.userRotation;
        } else {
            rotationVal = this.correctRotation;
        }
        return determinePipeSymbol(rotationVal);
    }

    // EFFECTS: Helper function for getPipeSymbol to determine the specific image link to be returned.
    public String determinePipeSymbol(int rotationVal) {
        switch (this.type) {
            case STRAIGHT:
                return rotationVal + "STRAIGHT";
            case L_SHAPE:
                return rotationVal + "L_SHAPE";
            case T_SHAPE:
                return rotationVal + "T_SHAPE";
            case CROSS:
                return "CROSS";
            default:
                return "NULL";
        }
    }

    // Effects: checks if pipe can be rotated (nested within level boolean functions)
    public boolean isChangeable() {
        return (!this.type.equals(NULL) && !this.type.equals(CROSS));
    }

    // EFFECTS: if pipe is oriented correctly or can only be oriented properly
    public boolean isEqualToSolution() {
        return (userRotation == correctRotation) || !isChangeable();
    }

    // MODIFIES: this
    // EFFECTS: randomizes rotation based on rotation value restrictions
    public void randomizeUserRotation() {
        Random r = new Random();
        int upperBound = 4; // Not including 4
        if (this.type.equals(STRAIGHT)) {
            upperBound = 2;
        }
        this.userRotation = r.nextInt(upperBound);
    }

    // MODIFIES: this
    // EFFECTS: Sets rotation boundaries for pipe types that need one, increments rotation value unless reached
    //          boundary, then returning to 0. . if boardType is 'u', userRotation is changed,
    //          otherwise ('s') correctRotation is changed
    public void rotate(char boardType) {
        int rotationBoundary = 3;
        if (type.equals(STRAIGHT)) {
            rotationBoundary = 1;
        }
        if (boardType == 'u' && userRotation >= rotationBoundary) {
            userRotation = 0;
        } else if (boardType == 'u') {
            userRotation++;
        } else if (correctRotation >= rotationBoundary) {
            correctRotation = 0;
        } else {
            correctRotation++;
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets type, but also returns rotations to 0 in case other value exceeds bounds
    public void setType(PipeType type) {
        this.type = type;
        this.correctRotation = 0;
        this.userRotation = 0;
    }

    // MODIFIES: this
    // EFFECTS: Sets correctRotation value
    public void setCorrectRotation(int r) {
        this.correctRotation = r;
    }

    // EFFECTS: produces JSONObject representation of a pipe
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("correctRotation", correctRotation);
        return json;
    }

    // MODIFIES: EventLog, Event
    // EFFECTS: adds edit choice to the log
    public static void logEditChoice(String end) {
        Event pipeTypeChange = new Event("Pipe was " + end);
        log.logEvent(pipeTypeChange);
    }

    // For tests:
    public int getUserRotation() {
        return this.userRotation;
    }

    public int getCorrectRotation() {
        return this.correctRotation;
    }

    public PipeType getType() {
        return this.type;
    }
}
