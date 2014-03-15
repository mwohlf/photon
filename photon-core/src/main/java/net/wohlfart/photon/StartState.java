package net.wohlfart.photon;

import java.util.Iterator;

import javax.inject.Inject;

import net.wohlfart.photon.events.MoveEvent;
import net.wohlfart.photon.events.RotateEvent;
import net.wohlfart.photon.events.Subscribe;
import net.wohlfart.photon.graph.ISceneGraph.IEntity3D;
import net.wohlfart.photon.graph.SceneGraph;
import net.wohlfart.photon.pov.CanMoveImpl;
import net.wohlfart.photon.pov.CanRotateImpl;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.entity.SphereEntity;
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
        // adding move, since the cam is always at 0/0/0 and in -z this works
        //MathTool.add(evt, movement.getPosition(), movement.getPosition());
        movement.getPosition().add(evt);
    }

    @Subscribe
    public void rotate(RotateEvent evt) {
        LOGGER.debug("incoming rotate event: " + evt);
        // multiplying a quaternion means adding the rotations
        //MathTool.mul(evt, rotation.getRotation(), rotation.getRotation());
        rotation.getRotation().mult(evt);
    }

    @Override
    public void init() {
    	final SphereEntity sphereEntity = new SphereEntity();
    	sphereEntity.register(sceneGraph);
    }

    @Override
    public void update(float delta) {
    	Iterator<IEntity3D> iter = sceneGraph.getEntities().iterator();
    	while (iter.hasNext()) {
    		iter.next().update(rotation, movement, delta);
    	}
    	rotation.setIdentity();
    	movement.set(0, 0, 0);
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
