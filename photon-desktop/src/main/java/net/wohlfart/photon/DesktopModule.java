package net.wohlfart.photon;

import dagger.Module;


@Module(
	includes = {CoreModule.class, SceneModule.class},
	injects = {DesktopStart.class},
	library = false
)
public class DesktopModule {

	// this module contains desktop specific view and input objects


	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html

}
