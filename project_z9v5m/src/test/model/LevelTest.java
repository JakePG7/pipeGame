package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static model.PipeType.*;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {
    private Level testLevel0;
    private Level testLevel1;
    private Pipe p00;
    private Pipe p43;

    @BeforeEach
    void runBefore() {
        testLevel0 = new Level("testLevel0", 0, 9);
        testLevel1 = new Level("testLevel1", 4, 1);
        p00 = testLevel1.getPipe(0);
        p43 = testLevel1.getPipe(43);
    }

    @Test
    public void testConstructor() {
        assertEquals("testLevel0", testLevel0.getName());
        assertEquals(0, testLevel0.getConnectorX());
        assertEquals(9, testLevel0.getConnectorY());
        assertEquals(1200, testLevel0.getBestTime()); // based on constant
        Pipe p0 = testLevel0.getPipe(0);
        Pipe p1 = testLevel0.getPipe(1);
        Pipe p2 = testLevel0.getPipe(2);
        assertNotEquals(p0, p1);
        assertNotEquals(p1, p2);

        EventLog log = EventLog.getInstance();
        Iterator<Event> logIterator = log.iterator();
        Event e = logIterator.next();
        assertEquals(e.getDate() + "\ntestLevel0 was created and added to list of levels", e.toString());
        // May fail if millis changes between lines of code.

    }

    @Test
    public void testShuffle() {
        p00.setType(STRAIGHT); // should be added with rotation 0
        testLevel1.shuffle();
        assertTrue(p00.getUserRotation() <= 1);
        p00.setType(T_SHAPE);
        testLevel1.shuffle();
        assertTrue(p00.getUserRotation() <= 3);
        // Although it would be good to test with multiple pipes changed, there would be
        // no logical way of testing this function that hasn't already been tested here.
    }

    @Test
    public void testIsSolutionWhenAutoWin() {
        assertTrue(testLevel0.isSolution()); // no pipe case
        p00.setType(CROSS);
        assertTrue(testLevel1.isSolution()); // unchangeable pipe case
    }

    @Test
    public void testIsSolutionVarying() {
        p00.setType(L_SHAPE);
        p00.rotate('u');
        assertFalse(testLevel1.isSolution());
        p00.rotate('s');
        assertTrue(testLevel1.isSolution());
        p00.rotate('u');
        assertFalse(testLevel1.isSolution());
        p00.rotate('s');
        p43.setType(STRAIGHT);
        p43.rotate('s');
        assertFalse(testLevel1.isSolution()); // p00 is true but p43 is false therefore false

    }

    @Test
    public void testIsAutomaticWin() {
        assertTrue(testLevel0.isAutomaticWin()); // no pipe case
        p00.setType(CROSS);
        assertTrue(testLevel1.isAutomaticWin()); // unchangeable pipe case
        p43.setType(T_SHAPE);
        assertFalse(testLevel1.isAutomaticWin());
    }

    @Test
    public void testCheckBestTime() {
        testLevel0.checkBestTime(1201);
        assertEquals(1200, testLevel0.getBestTime());
        testLevel0.checkBestTime(1200);
        assertEquals(1200, testLevel0.getBestTime());
        testLevel0.checkBestTime(1199); // boundary is 1200 based on INITIAL_BEST_TIME
        assertEquals(1199, testLevel0.getBestTime());
    }

//    @Test
//    public void testLogDisplay() {
//        Level.logDisplay();
//        EventLog log = EventLog.getInstance();
//        Iterator<Event> logIterator = log.iterator();
//        Event e = logIterator.next();
//        assertEquals(e.getDate() + "\nLevels were displayed in a JComboBox", e.toString());
//        // May fail if millis changes between lines of code.
//    }
}
