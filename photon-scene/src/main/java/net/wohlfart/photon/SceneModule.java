package net.wohlfart.photon;

import dagger.Module;


@Module(
	includes = {CoreModule.class},
	injects=Game.class,
	library=true)
public class SceneModule {


}
