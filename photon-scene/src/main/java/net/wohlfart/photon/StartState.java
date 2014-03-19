package net.wohlfart.photon;

import java.util.Iterator;

import javax.inject.Inject;

import net.wohlfart.photon.entity.Skybox;
import net.wohlfart.photon.entity.SphereEntity;
import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.CommandEvent.CommandKey;
import net.wohlfart.photon.events.MoveEvent;
import net.wohlfart.photon.events.RotateEvent;
import net.wohlfart.photon.events.Subscribe;
import net.wohlfart.photon.graph.ISceneGraph.IEntity3D;
import net.wohlfart.photon.graph.SceneGraph;
import net.wohlfart.photon.pov.CanMoveImpl;
import net.wohlfart.photon.pov.CanRotateImpl;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.state.Event;
import net.wohlfart.photon.state.IState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StartState implements IState {
	protected static final Logger LOGGER = LoggerFactory.getLogger(StartState.class);

	protected final SceneGraph sceneGraph = new SceneGraph();

	private volatile boolean debugOnce = false;

	// delegates, the scene is moved not the cam
	private final CanRotateImpl rotation = new CanRotateImpl();
	private final CanMoveImpl movement = new CanMoveImpl();


	@Inject
	StartState() {}

	@Subscribe
	public void move(MoveEvent evt) {
		LOGGER.debug("incoming move event: " + evt);
		movement.getPosition().add(evt.get());
	}

	@Subscribe
	public void rotate(RotateEvent evt) {
		LOGGER.debug("incoming rotate event: " + evt);
		rotation.getRotation().mult(evt.get());
	}

	@Subscribe
	public void shutdown(CommandEvent event) {
		if (event.getKey() == CommandKey.DEBUG_RENDER) {
			debugOnce = true;
		} else if (event.getKey() == CommandKey.DEBUG_RENDER) {
			// todo
		}
	}

	@Override
	public void init() {

		new Skybox() .register(sceneGraph);

		new SphereEntity()  .withPosition(10, 0, 0) .register(sceneGraph);

		// new SphereEntity()  .withPosition(20, 0, 0) .register(sceneGraph);

		// new SphereEntity()  .withPosition(30, 0, 0) .register(sceneGraph);

		/*
		new Earth() .withPosition(10, 0, 0) .register(sceneGraph);

		new Earth() .withPosition(20, 0, 0) .register(sceneGraph);

		new Earth() .withPosition(30, 0, 0) .register(sceneGraph);

		new Earth() .withPosition(40, 0, 0) .register(sceneGraph);

		new Earth() .withPosition(50, 0, 0) .register(sceneGraph);
		*/

		// new ProceduralCelestial() .withPosition(0, 0, -10) .withCorona(new Corona().withThinkness(2)) .register(sceneGraph);

		//	 new SimpleEffect().addEntity(new Earth().withPosition(10, 0, 0)) .register(sceneGraph)



	//	SimpleEffect effect = new SimpleEffect();
	//	effect.addEntity(new Earth().withSize(5).withPosition( 0, 0, -10d));
	//	effect.register(sceneGraph);

		//   new QuadEntity() .register(sceneGraph);



		/*
        new QuadEntity() .withPosition(new Vector3d(+15, 0, 0)).register(sceneGraph);
        new QuadEntity() .withPosition(new Vector3d(-15, 0, 0)).register(sceneGraph);

        new SphereEntity() .register(sceneGraph);
		 */

	}

	@Override
	public void update(float delta) {
		Iterator<IEntity3D> iter = sceneGraph.getEntities().iterator();
		while (iter.hasNext()) {
			iter.next().update(rotation, movement, delta);
		}
		movement.set(0, 0, 0);
		rotation.set(0, 0, 0, 1);
	}

	@Override
	public void render(IRenderer renderer) {

		if (debugOnce) {
			renderer.setDebugMode(true);
			renderer.renderParent(sceneGraph.getRenderCache());
			renderer.setDebugMode(false);
			debugOnce = false;
		} else {
			renderer.renderParent(sceneGraph.getRenderCache());
		}
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public Event getTransitionEvent() {
		return Event.END;
	}

	@Override
	public void dispose() {
	}

}
