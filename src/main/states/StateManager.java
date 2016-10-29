package main.states;

public class StateManager {

    private static States state = States.MENU;

    public static States getState() {
        return state;
    }

    public static void setState(States setState) {
        state = setState;
    }

    public enum States {
        MENU, PLAY
    }

}