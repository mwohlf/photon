package net.wohlfart.photon.state;

import javax.inject.Inject;

import net.wohlfart.photon.StartState;

public class StateManager {

    private IState currentState = StateAdaptor.INIT_STATE;

    @Inject
    StartState startState;

    @Inject
    StateManager() {}

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
