package net.wohlfart.photon.state;


public class StateManager {

    private IState currentState = StateAdaptor.INIT_STATE;

    private IState startState;

    public StateManager() {}

    public void setStartState(IState state) {
    	startState = state;
    }

	public IState getCurrentState() {
		return currentState;
	}

    public IState calculateNextState() {
        Event transitionEvent = currentState.getTransitionEvent();
        if (transitionEvent == Event.START) {
        	currentState = startState;
        } else {
        	currentState = StateAdaptor.END_STATE;
        }
        return currentState;
    }

}
