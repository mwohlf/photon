package org.photon.android;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import jogamp.newt.driver.android.NewtBaseActivity;
import net.wohlfart.photon.Application;
import net.wohlfart.photon.LifecycleAdpator;
import android.os.Bundle;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;


/**
 * see: http://stackoverflow.com/questions/17652997/dagger-nested-injections
 *
 */
public class AndroidStart extends NewtBaseActivity {

	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);

		final GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2ES2));
		final GLWindow window = GLWindow.create(capabilities);
		window.setFullscreen(true);

		this.setContentView(this.getWindow(), window);

		final Application example = new Application(
				// TODO

				);  // todo: the real application

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