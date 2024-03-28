package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static model.PipeType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Code templated from: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonWriterTest extends JsonTest {

    private Level levelDefault;
    private Level levelEdited;

    @BeforeEach
    // setting levelDefault to have default NULL pipes, whereas levelEdited will have the following pipes:
    // (pos)  (type) (correctRot) (userRot is not involved with this function)
    //   33  STRAIGHT     1
    //   41  T_SHAPE      2
    // and remaining as default. best time will be 20s.
    void runBefore() {
        levelDefault = new Level("Default Level", 5, 6);
        levelEdited = new Level("Edited Level", 0, 0);
        levelEdited.checkBestTime(20);
        Pipe pipe33 = levelEdited.getPipe(33);
        Pipe pipe41 = levelEdited.getPipe(41);
        pipe33.setType(STRAIGHT);
        pipe33.setCorrectRotation(1);
        pipe41.setType(T_SHAPE);
        pipe41.setCorrectRotation(2);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEditedLevels() {
        try {
            ArrayList<Level> levels = new ArrayList<>();
            levels.add(levelEdited);
            levels.add(levelDefault);
            levels.add(levelDefault);
            JsonWriter writer = new JsonWriter("./data/testWriterEditedLevels.json");
            writer.open();
            writer.write(levels);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEditedLevels.json");
            levels = reader.read();
            assertEquals(3, levels.size());
            Level level0 = levels.get(0);
            Level level1 = levels.get(1);
            checkLevel(level0, "Edited Level", 0, 0, 20);
            checkLevel(level1, "Default Level", 5, 6, 1200);
            checkPipe(level0.getPipe(33), STRAIGHT, 1);
            checkPipe(level0.getPipe(41), T_SHAPE, 2);
            checkPipe(level1.getPipe(33), NULL, 0); // represents any random default NULL pipe
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    // Catches any variables that can/can't change that was not checked in the last test.
    void testWriterDefaultLevels() {
        try {
            ArrayList<Level> levels = new ArrayList<>();
            levels.add(levelDefault);
            JsonWriter writer = new JsonWriter("./data/testWriterDefaultLevels.json");
            writer.open();
            writer.write(levels);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterDefaultLevels.json");
            levels = reader.read();
            assertEquals(1, levels.size());
            Level level0 = levels.get(0);
            checkLevel(level0, "Default Level", 5, 6, 1200);
            checkPipe(level0.getPipe(33), NULL, 0);
            checkPipe(level0.getPipe(0), NULL, 0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
