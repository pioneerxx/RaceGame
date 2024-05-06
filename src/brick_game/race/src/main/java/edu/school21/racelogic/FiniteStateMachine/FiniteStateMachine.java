package edu.school21.racelogic.FiniteStateMachine;

public class    FiniteStateMachine {
    public enum State {
        START, PLAYING, END
    }

    private State currentState;

    public void collide() {
        currentState = State.END;
    }

    public void startRace() {
        currentState = State.PLAYING;
    }

    public State getCurrentState() {
        return currentState;
    }
    public void reset() {
        currentState = State.START;
    }

    public FiniteStateMachine() {
        currentState = State.START;
    }
}
