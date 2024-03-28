package persistence;

import static org.junit.jupiter.api.Assertions.*;
import model.*;

public class JsonTest {

    protected void checkLevel(Level level, String name, int connectorX, int connectorY, long bestTime) {
        assertEquals(name, level.getName());
        assertEquals(connectorX, level.getConnectorX());
        assertEquals(connectorY, level.getConnectorY());
        assertEquals(bestTime, level.getBestTime());
    }

    protected void checkPipe(Pipe pipe, PipeType type, int correctRot) {
        assertEquals(type, pipe.getType());
        assertEquals(0, pipe.getUserRotation());
        assertEquals(correctRot, pipe.getCorrectRotation());
    }
}
