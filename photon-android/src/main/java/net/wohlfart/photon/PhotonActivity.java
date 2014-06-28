package net.wohlfart.photon;

import jogamp.newt.driver.android.NewtBaseActivity;
/*
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.TimerImpl;
*/
import android.os.Bundle;


/**
 * see: http://stackoverflow.com/questions/17652997/dagger-nested-injections
 *
 */
public class PhotonActivity extends NewtBaseActivity {
/*
	@Inject
	public PoolEventBus eventBus;

	@Inject
	public TimerImpl timer;

	@Inject
	public RendererImpl renderer;

	@Inject
	public StateManager stateManager;
*/
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
/*
		final GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2ES2));
		final GLWindow window = GLWindow.create(capabilities);
		window.setFullscreen(true);

		this.setContentView(this.getWindow(), window);

		final MainApplication example = new MainApplication(eventBus, timer, renderer, stateManager);

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
		*/
	}

}