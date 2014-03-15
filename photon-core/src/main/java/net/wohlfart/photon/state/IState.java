package net.wohlfart.photon.state;

import net.wohlfart.photon.render.IRenderer;


public interface IState  {

    void init();

	void update(float delta);

	void render(IRenderer renderer);

    boolean isDone();

    Event getTransitionEvent();

	void dispose();

}
