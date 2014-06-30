package net.wohlfart.photon.ui;

import javax.inject.Inject;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import net.wohlfart.photon.LifecycleAdpator;
import net.wohlfart.photon.MainApplication;
import net.wohlfart.photon.PhotonBaseActivity;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.TimerImpl;
/*
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.TimerImpl;
*/
import android.os.Bundle;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;


/**
 * see: http://stackoverflow.com/questions/17652997/dagger-nested-injections
 *
 */
public class PhotonActivity extends PhotonBaseActivity {

	@Inject
	public PoolEventBus eventBus;

	@Inject
	public TimerImpl timer;

	@Inject
	public RendererImpl renderer;

	@Inject
	public StateManager stateManager;

	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);

		final GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GLES2));
		final GLWindow window = GLWindow.create(capabilities);
		window.setFullscreen(true);

		GLProfile.initSingleton();

		this.setContentView(this.getWindow(), window);

		final MainApplication example = new MainApplication(eventBus, timer, renderer, stateManager);

		window.addGLEventListener(new LifecycleAdpator(example));
/*
		window.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getMaxPressure() > 2f) { // show Keyboard
					((com.jogamp.newt.Window) mouseEvent.getSource()).setKeyboardVisible(true);
				}
			}
		});
*/
		final Animator animator = new Animator(window);
		this.setAnimator(animator);

		window.setVisible(true);
		animator.setUpdateFPSFrames(60, System.err);
		animator.resetFPSCounter();
		window.resetFPSCounter();

	}

}