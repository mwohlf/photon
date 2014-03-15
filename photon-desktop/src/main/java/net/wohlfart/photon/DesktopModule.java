package net.wohlfart.photon;

import dagger.Module;


@Module(
	includes = PhotonModule.class,
	injects = {DesktopStart.class},
	library = false
)
public class DesktopModule {

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html


}
