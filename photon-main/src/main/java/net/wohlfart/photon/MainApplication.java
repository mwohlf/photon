package net.wohlfart.photon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;

import javax.inject.Inject;
import javax.vecmath.Matrix4f;

import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.events.ResizeEvent;
import net.wohlfart.photon.hud.txt.CharAtlasFactory;
import net.wohlfart.photon.hud.txt.CharDataFactory;
import net.wohlfart.photon.render.RenderConfigImpl;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.shader.IUniformValue;
import net.wohlfart.photon.shader.Matrix4fValue;
import net.wohlfart.photon.shader.Model2WorldMatrixValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.state.IState;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.TimerImpl;
import net.wohlfart.photon.tools.Perspective;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



// see: http://www.opengl.org/wiki/Common_Mistakes
public class MainApplication implements ILifecycleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

	private final PoolEventBus eventBus;
	private final TimerImpl timer;
	private final RendererImpl renderer;
	private final StateManager stateManager;

	private IState currentState;


	@Inject
	public MainApplication(
			PoolEventBus eventBus,
			TimerImpl timer,
			RendererImpl renderer,  // use interface here?
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

		final Collection<IUniformValue> uniforms = new HashSet<IUniformValue>();

		// this will be updated by the model / render command
		final Matrix4f modelToWorldMatrix = new Matrix4f();
		modelToWorldMatrix.setIdentity();
		uniforms.add(new Model2WorldMatrixValue(modelToWorldMatrix));

		// cam is static at 0/0/0
		final Matrix4f worldToCamMatrix = new Matrix4f();
		worldToCamMatrix.setIdentity();
		uniforms.add(new Matrix4fValue(ShaderParser.UNIFORM_WORLD_2_CAM_MTX, worldToCamMatrix));

		gfxCtx.addUniformValues(uniforms);
		gfxCtx.setRenderConfig(ShaderIdent.DEFAULT_SHADER, RenderConfigImpl.DEFAULT);

		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream("/scene.properties");
			prop.load(in);
			float fieldOfViewDegree = Float.valueOf(prop.getProperty("fieldOfViewDegree"));
			float nearPlane = Float.valueOf(prop.getProperty("nearPlane"));
			float farPlane = Float.valueOf(prop.getProperty("farPlane"));
			float scaleFactor = Float.valueOf(prop.getProperty("scaleFactor"));
			Perspective perspective = gfxCtx.getPerspective();
			perspective.setFieldOfViewDegree(fieldOfViewDegree);
			perspective.setNearPlane(nearPlane);
			perspective.setFarPlane(farPlane);
			perspective.setScaleFactor(scaleFactor);

		} catch (IOException ex) {
			LOGGER.error("cant read properties, using default values, expect more errors", ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					LOGGER.error("error closing stream", ex);
				}
			}
		}

		currentState = stateManager.getCurrentState();
	}

	/**
	 * Called back by the animator to perform rendering.
	 */
	@Override
	public void display(IGraphicContext gfxCtx) {
		try {
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
		} catch (Exception ex) {
			LOGGER.error("error in display method", ex);
			eventBus.post(CommandEvent.exit());
			while (eventBus.hasEvent()) {
				eventBus.fireEvent();
			}
		}

	}

	/**
	 * Call-back handler for window re-size event. Also called when the drawable is
	 * first set to visible.
	 */
	@Override
	public void reshape(IGraphicContext gfxCtx, int x, int y, int width, int height) {
		LOGGER.info("reshape() called");

		final Perspective perspective = gfxCtx.getPerspective();
		perspective.setScreenWidth(width - x);
		perspective.setScreenHeight(height - y);

		final Matrix4f cameraToClipMatrix = perspective.getPerspectiveMatrix();

		renderer.addUniformValues(Collections.singleton(
				(IUniformValue)new Matrix4fValue(ShaderParser.UNIFORM_CAM_2_CLIP_MTX,cameraToClipMatrix)));

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
