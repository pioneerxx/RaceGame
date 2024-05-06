package edu.school21.racelogic.FiniteStateMachineTest;
import edu.school21.racelogic.FiniteStateMachine.FiniteStateMachine;
import edu.school21.racelogic.MapMatrix.MapMatrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapMatrixTest {
    @Test
    public void levelTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        assertEquals(1, matrix.getLevel());
    }

    @Test
    public void scoreTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        assertEquals(matrix.getScore(), 0);
    }

    @Test
    public void highscoreTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        assertEquals(matrix.getHighScore(), 0);
    }

    @Test
    public void pauseTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        assertFalse(matrix.getPaused());
        matrix.userInput(MapMatrix.Action.Pause);
        assertTrue(matrix.getPaused());
    }

    @Test
    public void setHighscoreTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        long highscore = 150;
        matrix.setHighScore(highscore);
        assertEquals(highscore, matrix.getHighScore());
    }

    @Test
    public void speedTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        assertEquals(matrix.getSpeed(), 150);
        matrix.userInput(MapMatrix.Action.Up);
        assertEquals(matrix.getSpeed(), 75);
        matrix.userInput(MapMatrix.Action.Down);
        matrix.userInput(MapMatrix.Action.Down);
        assertEquals(matrix.getSpeed(), 300);
    }

    @Test
    public void newHighscoreTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.userInput(MapMatrix.Action.Start);
        assertFalse(matrix.isNewHighScore());
    }

    @Test
    public void crashTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.userInput(MapMatrix.Action.Start);
        matrix.updateState();
        matrix.userInput(MapMatrix.Action.Left);
        matrix.userInput(MapMatrix.Action.Right);
        matrix.userInput(MapMatrix.Action.Down);
        matrix.userInput(MapMatrix.Action.Up);
        while (matrix.getState() != FiniteStateMachine.State.END)
            matrix.updateState();
        assertSame(matrix.getState(), FiniteStateMachine.State.END);
    }

}
