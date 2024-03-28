package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static model.PipeType.*;
import static org.junit.jupiter.api.Assertions.*;

class PipeTest {

    private Pipe p1;

    @BeforeEach
    void runBefore() {
        p1 = new Pipe();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, p1.getUserRotation());
        assertEquals(0, p1.getUserRotation());
        assertEquals(NULL, p1.getType());
    }

    @Test
    public void testGetPipeSymbolNoRotationTypes() {
        assertEquals("NULL", p1.getPipeSymbol('u'));
        assertEquals("NULL", p1.getPipeSymbol('s'));
        p1.rotate('u');
        assertEquals("NULL", p1.getPipeSymbol('u'));
        p1.setType(CROSS);
        assertEquals("CROSS", p1.getPipeSymbol('u'));
        assertEquals("CROSS", p1.getPipeSymbol('s'));
        p1.rotate('u');
        assertEquals("CROSS", p1.getPipeSymbol('u'));
    }

    @Test
    public void testGetPipeSymbolLShape() {
        p1.setType(L_SHAPE);
        assertEquals("0L_SHAPE", p1.getPipeSymbol('u'));
        assertEquals("0L_SHAPE", p1.getPipeSymbol('s'));
        int initialPosition = p1.getUserRotation();
        p1.rotate('u');
        assertEquals("1L_SHAPE", p1.getPipeSymbol('u'));
        assertEquals("0L_SHAPE", p1.getPipeSymbol('s'));
        p1.rotate('u');
        assertEquals("2L_SHAPE", p1.getPipeSymbol('u'));
        p1.rotate('s');
        assertEquals("1L_SHAPE", p1.getPipeSymbol('s'));
        p1.rotate('u');
        assertEquals("3L_SHAPE", p1.getPipeSymbol('u'));
        p1.rotate('u');
        assertEquals("0L_SHAPE", p1.getPipeSymbol('u')); // back to 0
        int finalPosition = p1.getUserRotation();
        assertEquals(initialPosition, finalPosition);
        // The methods getPipeSymbol and rotate are similar in functionality and don't both need expansive tests.
    }

    // Assume from now on that all necessary tests have been done comparing 'u' and 's'
    @Test
    public void testGetPipeSymbolTShapeStraight() {
        p1.setType(T_SHAPE);
        assertEquals("0T_SHAPE", p1.getPipeSymbol('u')); // rotation starts at 0 again
        assertEquals("0T_SHAPE", p1.getPipeSymbol('s'));
        p1.rotate('u');
        assertEquals("1T_SHAPE", p1.getPipeSymbol('u'));
        p1.rotate('u');
        assertEquals("2T_SHAPE", p1.getPipeSymbol('u'));
        p1.rotate('u');
        assertEquals("3T_SHAPE", p1.getPipeSymbol('u'));
        p1.rotate('u');
        assertEquals("0T_SHAPE", p1.getPipeSymbol('u')); // back to 0

        p1.setType(STRAIGHT);
        assertEquals("0STRAIGHT", p1.getPipeSymbol('s'));
        p1.rotate('s');
        assertEquals("1STRAIGHT", p1.getPipeSymbol('s'));
        p1.rotate('s');
        assertEquals("0STRAIGHT", p1.getPipeSymbol('s'));
    }

    @Test
    public void testRandomizeUserRotation() {
        p1.setType(STRAIGHT);
        p1.randomizeUserRotation();
        assertTrue(p1.getUserRotation() < 2);
        p1.setType(CROSS);
        p1.randomizeUserRotation();
        assertTrue(p1.getUserRotation() < 4); // Even though it doesn't matter.
        p1.setType(T_SHAPE);
        p1.randomizeUserRotation();
        assertTrue(p1.getUserRotation() < 4);
    }

    @Test
    public void testIsEqualToSolution() {
        assertTrue(p1.isEqualToSolution()); // is changable (NULL) and equivalent rotations
        p1.setType(CROSS);
        assertTrue(p1.isEqualToSolution()); // is changable (CROSS) and equivalent rotations
        p1.setType(T_SHAPE);
        assertTrue(p1.isEqualToSolution()); // equivalent rotations
        p1.rotate('c');
        assertFalse(p1.isEqualToSolution()); // neither
        p1.rotate('u');
        assertTrue(p1.isEqualToSolution()); // equivalent rotations (not 0)
        p1.rotate('u');
        assertFalse(p1.isEqualToSolution()); // neither
        p1.setType(NULL);
        p1.rotate('c');
        assertTrue(p1.isEqualToSolution()); // is changable (NULL)
        p1.setType(CROSS);
        p1.rotate('c');
        assertTrue(p1.isEqualToSolution()); // is changable (CROSS)
    }

    @Test
    public void testLogEditChoice() {
        Pipe.logEditChoice("dead");
        EventLog log = EventLog.getInstance();
        Iterator<Event> logIterator = log.iterator();
        Event e = logIterator.next();
        assertEquals(e.getDate() + "\nPipe was dead", e.toString());
        // May fail if millis changes between lines of code.
    }
}