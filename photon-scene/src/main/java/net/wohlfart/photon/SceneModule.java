package net.wohlfart.photon;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module(
	injects=Game.class,
	library=true)
public class SceneModule {

	@Provides @Singleton
	Game provideGame() {
		return new Game();
	}

}
