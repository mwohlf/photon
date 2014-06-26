package net.wohlfart.photon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.inject.Inject;

import net.wohlfart.photon.entity.Skybox;
import net.wohlfart.photon.entity.stellar.PlanetClassM;
import net.wohlfart.photon.entity.stellar.SunClassG;
import net.wohlfart.photon.entity.stellar.SunClassM;
import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.CommandEvent.CommandKey;
import net.wohlfart.photon.events.MoveEvent;
import net.wohlfart.photon.events.ResizeEvent;
import net.wohlfart.photon.events.RotateEvent;
import net.wohlfart.photon.events.Subscribe;
import net.wohlfart.photon.graph.ISceneGraph;
import net.wohlfart.photon.graph.ISceneGraph.IEntity;
import net.wohlfart.photon.graph.SceneGraph;
import net.wohlfart.photon.hud.IScreenSizeListener;
import net.wohlfart.photon.hud.SimpleLayer;
import net.wohlfart.photon.pov.CanMoveImpl;
import net.wohlfart.photon.pov.CanRotateImpl;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.state.Event;
import net.wohlfart.photon.state.IState;
import net.wohlfart.photon.tools.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StartState implements IState {
	protected static final Logger LOGGER = LoggerFactory.getLogger(StartState.class);

	protected final ISceneGraph sceneGraph = new SceneGraph();

	protected final Collection<IScreenSizeListener> resizables = new HashSet<IScreenSizeListener>();

	private volatile boolean debugOnce = false;

	// delegates, the entities are moved not the cam
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
	public void command(CommandEvent event) {
		if (event.getKey() == CommandKey.DEBUG_RENDER) {
			debugOnce = true;
		} else if (event.getKey() == CommandKey.DUMP_SCENE) {
			LOGGER.debug("scene graph: " + sceneGraph);
		}
	}

	@Subscribe
	public void resize(ResizeEvent resize) {
		LOGGER.info("resize.getDimension(): " + resize.getDimension());
		Dimension dim = resize.getDimension();
		for (IScreenSizeListener resizable : resizables) {
			resizable.setScreenDimension(dim);
		}
	}

	@Override
	public void init() {

		Random random = new Random(3);

		sceneGraph.setup( new IEntity[] {

				new SimpleLayer(),
				new Skybox(),
				new SunClassM().withSeed(random.nextLong()).withPosition(-17, 0, -30),
				new SunClassG().withSeed(random.nextLong()).withPosition(+17, 0, -30),
				new PlanetClassM().withSeed(random.nextLong()).withPosition( 0, -17, -30),
				/*

				new VertexLight().withPosition(-17, 0, -30),

				new ProceduralCelestial()
					.withSize(4)
					.withPosition(0, 0, -30)
					.withCorona(new Corona().withThinkness(2f)),



				new SphereEntity().withPosition(0, 0, -30),



				new SphereEntity().withPosition(0, 0, -30),

				new QuadEntity().withPosition(new Vector3d(0, 0, -20)),
				new CubeEntity(1).withPosition(0,0,-2),
				new CubeEntity(1).withPosition(-20,0,-3f),
				new CubeEntity(1).withPosition(-12,0,-4f),
				new CubeEntity(1).withPosition(-5,0,-5f),
				new CubeEntity(1).withPosition(7,0,-6f),


				new SpriteCloud(),


				new SphereEntity().withPosition(-10, 0, -10),

				new Earth().withPosition(0, -10, -20),
				new CubeEntity(1).withPosition(22,20,-1),
			//	new SimpleEffect().addEntity(new Earth().withSize(5).withPosition( 0, 0, -10d)),

				new VertexLight().withPosition(20, 0,  30),


				new SphereEntity().withPosition(0, 0, 10),
				new SphereEntity().withPosition(0, 10, -20),
				new SphereEntity().withPosition(0, -10, -20),
				new SphereEntity().withPosition(0, 15, 20),
				new SphereEntity().withPosition(0, 20, 30),
				new SphereEntity().withPosition(10, 20, -30),
				new SphereEntity().withPosition(-10, 20, -30),

*/
		});
	}

	@Override
	public void update(float delta) {
		Iterator<IEntity> iter = sceneGraph.getEntities().iterator();
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
