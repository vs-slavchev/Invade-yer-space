package main.states;

public class StateManager {

    public enum States {
        MENU, PLAY
    }

    private static States state = States.MENU;

    public static States getState() {
        return state;
    }

    public static void setState(States setState) {
        state = setState;
    }

}