package persistence;

import model.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static model.PipeType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {
    // Code templated from: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Level> levels = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    // Reminder that it is not possible to have an empty game due to default levels. No need to test empty case.

    @Test
    // testReaderGameWithLevels data file has level 0 as default and level 1 as edited with:
    // (pos)  (type) (correctRot)
    //   00  L_SHAPE     0
    //   01  T_SHAPE     3
    //   43  CROSS       1
    // and remaining as default. best time is 13s.
    // Also has additional level with name "Another Default Level",
    // connecting at X=9, Y=9, default best time.
    void testReaderGameWithLevels() {
        JsonReader reader = new JsonReader("./data/testReaderGameWithLevels.json");
        try {
            ArrayList<Level> levels = reader.read();
            assertEquals(3, levels.size());
            Level level1 = levels.get(1);
            Level level2 = levels.get(2);
            checkLevel(level2, "Another Default Level", 9, 9, 1200);
            checkLevel(level1, "Level 1", 5, 0, 13);
            checkPipe(level2.getPipe(78), NULL, 0);
            checkPipe(level1.getPipe(0), L_SHAPE, 0); // represents pipe of pos 00
            checkPipe(level1.getPipe(1), T_SHAPE, 3); // represents pipe of pos 01
            checkPipe(level1.getPipe(43), CROSS, 1); // represents pipe of pos 43
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    // Confirms which variables should be constant or not based on previous test.
    void testReaderDefaultLevels() {
        JsonReader reader = new JsonReader("./data/testReaderDefaultLevels.json");
        try {
            ArrayList<Level> levels = reader.read();
            assertEquals(2, levels.size());
            Level level1 = levels.get(1);
            checkLevel(level1, "Level 1", 5, 0, 1200);
            checkPipe(level1.getPipe(43), NULL, 0); // represents pipe of pos 43 default
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
