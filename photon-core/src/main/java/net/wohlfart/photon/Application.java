package net.wohlfart.photon;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.vecmath.Matrix4f;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.UniformHandle;
import net.wohlfart.photon.shader.UniformHandle.UniformValue;
import net.wohlfart.photon.state.IState;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.time.TimerImpl;
import net.wohlfart.photon.tools.PerspectiveProjectionBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// platform independent base class contains callbacks for the
//
// responsible for global state management
public class Application implements GLEventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    protected final ShaderIdentifier DEFAULT_SHADER_ID = ShaderIdentifier.create("shader/default.vert", "shader/default.frag");


	private final PoolEventBus eventBus;
	private final TimerImpl timer;
	private final RendererImpl renderer;
	private final StateManager stateManager;

	private IState currentState;


	@Inject
	public Application(PoolEventBus eventBus,
			           TimerImpl timer,
			           RendererImpl renderer,
			           StateManager stateManager) {
		this.eventBus = eventBus;
		this.timer = timer;
		this.renderer = renderer;
		this.stateManager = stateManager;
	}

	/**
	 * Called back immediately after the OpenGL context is initialized. Can be used
	 * to perform one-time initialization. Run only once.
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		LOGGER.info("init() called: " + renderer + " " + timer + " " + eventBus);

        final Map<String, UniformValue> uniforms = new HashMap<>();

        final Matrix4f modelToWorldMatrix = new Matrix4f();
        modelToWorldMatrix.setIdentity();
        uniforms.put(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, new UniformHandle.Matrix4fValue(modelToWorldMatrix));

        final Matrix4f worldToCamMatrix = new Matrix4f();
        worldToCamMatrix.setIdentity();
        uniforms.put(ShaderParser.UNIFORM_WORLD_2_CAM_MTX, new UniformHandle.Matrix4fValue(worldToCamMatrix));

        renderer.setUniformValues(new HashMap<String, ITextureIdentifier>(), uniforms);
		renderer.init(drawable).setRenderConfig(DEFAULT_SHADER_ID, RenderConfigImpl.DEFAULT);
		currentState = stateManager.getCurrentState();
	}


	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(GLAutoDrawable drawable) {

		LOGGER.debug("display() called");
		if (currentState.isDone()) {
			eventBus.unregister(currentState);
			currentState.dispose();
			currentState = stateManager.calculateNextState();
			currentState.init();
			eventBus.register(currentState);
		}

		currentState.update(timer.getDelta());
		renderer.init(drawable); // FIXME: ugly
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
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		LOGGER.info("reshape() called");

        final Map<String, UniformValue> uniforms = new HashMap<>();

        final Matrix4f cameraToClipMatrix = new PerspectiveProjectionBuilder()
        .withFieldOfView(45)
        .withFarPlane(1000)
        .withNearPlane(1)
        .withHeight(drawable.getHeight())
        .withWidth(drawable.getWidth())
        .build();
        uniforms.put(ShaderParser.UNIFORM_CAM_2_CLIP_MTX, new UniformHandle.Matrix4fValue(cameraToClipMatrix));

        renderer.setUniformValues(new HashMap<String, ITextureIdentifier>(), uniforms);
	}

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
		LOGGER.info("dispose() called");
	}

}
