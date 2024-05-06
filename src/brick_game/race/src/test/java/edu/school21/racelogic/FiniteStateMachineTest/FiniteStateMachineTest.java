package edu.school21.racelogic.FiniteStateMachineTest;

import edu.school21.racelogic.FiniteStateMachine.FiniteStateMachine;
import edu.school21.racelogic.MapMatrix.MapMatrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FiniteStateMachineTest {
    @Test
    public void startStateTest() {
        MapMatrix matrix = new MapMatrix();
        assertSame(matrix.getState(), FiniteStateMachine.State.START);
    }

    @Test
    public void playingStateTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        assertSame(matrix.getState(), FiniteStateMachine.State.PLAYING);
    }

    @Test
    public void endStateTest() {
        MapMatrix matrix = new MapMatrix();
        matrix.bootUp();
        matrix.userInput(MapMatrix.Action.Terminate);
        assertSame(matrix.getState(), FiniteStateMachine.State.END);
    }
}
