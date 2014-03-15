package net.wohlfart.photon.state;

import net.wohlfart.photon.render.IRenderer;


public class StateAdaptor implements IState {

    public static final IState INIT_STATE = new StateAdaptor() {
        @Override
        public Event getTransitionEvent() {
            return Event.START;
        }
    };

    public static final IState END_STATE = new StateAdaptor();


    @Override
    public void init() {
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(IRenderer renderer) {
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void dispose() {
    }

    @Override
    public Event getTransitionEvent() {
        return Event.END;
    }

}
