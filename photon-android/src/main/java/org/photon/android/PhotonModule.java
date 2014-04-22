package org.photon.android;

import net.wohlfart.photon.CoreModule;
import net.wohlfart.photon.SceneModule;
import android.content.Context;
import dagger.Module;

@Module( // see: http://www.joshlong.com/jl/blogPost/dependency_injection_with_dagger_on_android.html
  includes = {CoreModule.class, SceneModule.class},
  // enumerates all the classes that may receive injections that
  // are not defined explicitly using an @Provider-annotated method in the module
  injects = {PhotonActivity.class} )
public class PhotonModule {

	private final PhotonApplication application;
	private final Context context;

	public PhotonModule(PhotonApplication application) {
        this.application = application;
        this.context = this.application.getApplicationContext();
    }
}
