package org.photon.android;

import javax.inject.Inject;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import jogamp.newt.driver.android.NewtBaseActivity;
import net.wohlfart.photon.SceneApplication;
import net.wohlfart.photon.LifecycleAdpator;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.TimerImpl;
import android.os.Bundle;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;


/**
 * see: http://stackoverflow.com/questions/17652997/dagger-nested-injections
 *
 */
public class PhotonActivity extends NewtBaseActivity {

	@Inject
	private PoolEventBus eventBus;

	@Inject
	private TimerImpl timer;

	@Inject
	private RendererImpl renderer;

	@Inject
	private StateManager stateManager;


	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);

		PhotonApplication application = (PhotonApplication) getApplication();
		application.getObjectGraph().inject(this);

		final GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2ES2));
		final GLWindow window = GLWindow.create(capabilities);
		window.setFullscreen(true);

		this.setContentView(this.getWindow(), window);

		final SceneApplication example = new SceneApplication(eventBus, timer, renderer, stateManager);

		window.addGLEventListener(new LifecycleAdpator(example));

		window.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getMaxPressure() > 2f) { // show Keyboard
					((com.jogamp.newt.Window) mouseEvent.getSource()).setKeyboardVisible(true);
				}
			}
		});

		final Animator animator = new Animator(window);
		this.setAnimator(animator);

		window.setVisible(true);
		animator.setUpdateFPSFrames(60, System.err);
		animator.resetFPSCounter();
		window.resetFPSCounter();
	}

}