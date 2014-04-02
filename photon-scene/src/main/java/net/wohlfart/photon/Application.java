package net.wohlfart.photon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.vecmath.Matrix4f;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.events.ResizeEvent;
import net.wohlfart.photon.hud.txt.CharAtlasFactory;
import net.wohlfart.photon.hud.txt.CharDataFactory;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.Matrix4fValue;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.state.IState;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.TimerImpl;
import net.wohlfart.photon.tools.PerspectiveProjectionBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application implements ILifecycleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	private final PerspectiveProjectionBuilder perspectiveProjectionBuilder = new PerspectiveProjectionBuilder();

	protected final ShaderIdentifier DEFAULT_SHADER_ID = ShaderIdentifier.create("shader/default.vert", "shader/default.frag");

	private final PoolEventBus eventBus;
	private final TimerImpl timer;
	private final RendererImpl renderer;
	private final StateManager stateManager;

	private IState currentState;


	@Inject
	public Application(
				PoolEventBus eventBus,
			    TimerImpl timer,
			    RendererImpl renderer,
			    StateManager stateManager) {
		this.eventBus = eventBus;
		this.timer = timer;
		this.renderer = renderer;
		this.stateManager = stateManager;

        final CharAtlasFactory charAtlasFactory = new CharAtlasFactory();
        ResourceManager.INSTANCE.register(charAtlasFactory);
        ResourceManager.INSTANCE.register(new CharDataFactory(charAtlasFactory));

		this.stateManager.setStartState(new StartState());
	}

	/**
	 * Called back immediately after the OpenGL context is initialized. Can be used
	 * to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(IGraphicContext gfxCtx) {
		LOGGER.info("init() called: " + renderer + " " + timer + " " + eventBus);
		renderer.setGfxContext(gfxCtx);

        final Map<String, IUniformValue> uniforms = new HashMap<>();

        // this will be updated by the model / render command
        final Matrix4f modelToWorldMatrix = new Matrix4f();
        modelToWorldMatrix.setIdentity();
        uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new Matrix4fValue(modelToWorldMatrix));

        // cam is static at 0/0/0
        final Matrix4f worldToCamMatrix = new Matrix4f();
        worldToCamMatrix.setIdentity();
        uniforms.put(ShaderParser.UNIFORM_WORLD_2_CAM_MTX, new Matrix4fValue(worldToCamMatrix));

        gfxCtx.setUniformValues(uniforms);
        gfxCtx.setRenderConfig(DEFAULT_SHADER_ID, RenderConfigImpl.DEFAULT);

		Properties prop = new Properties();
		try (InputStream in = getClass().getResourceAsStream("/scene.properties")) {
			prop.load(in);
			float fieldOfView = Float.valueOf(prop.getProperty("fieldOfView"));
			float nearPlane = Float.valueOf(prop.getProperty("nearPlane"));
			float farPlane = Float.valueOf(prop.getProperty("farPlane"));
			perspectiveProjectionBuilder
				.withFieldOfView(fieldOfView)
				.withNearPlane(nearPlane)
				.withFarPlane(farPlane);
		} catch (IOException ex) {
			LOGGER.error("cant read properties", ex);
		}

		currentState = stateManager.getCurrentState();
	}


	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(IGraphicContext gfxCtx) {

		LOGGER.debug("display() called");
		if (currentState.isDone()) {
			eventBus.unregister(currentState);
			currentState.dispose();
			currentState = stateManager.calculateNextState();
			currentState.init();
			eventBus.register(currentState);
		}

		currentState.update(timer.getDelta());

		renderer.setGfxContext(gfxCtx);

		currentState.render(renderer);

		while (eventBus.hasEvent()) {
			eventBus.fireEvent();
		}

	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable is
	 * first set to visible.
	 */
	@Override
	public void reshape(IGraphicContext gfxCtx, int x, int y, int width, int height) {
		LOGGER.info("reshape() called");

        final Matrix4f cameraToClipMatrix = perspectiveProjectionBuilder
        		.withWidth(width - x)
        		.withHeight(height - y)
        		.build();
        renderer.setUniformValues(Collections.singletonMap(
        		ShaderParser.UNIFORM_CAM_2_CLIP_MTX,
        		(IUniformValue)new Matrix4fValue(cameraToClipMatrix)));

        eventBus.post(ResizeEvent.create(width - x, height - y));
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	 */
	@Override
	public void dispose(IGraphicContext gfxCtx) {
		LOGGER.info("dispose() called");
	}

}
