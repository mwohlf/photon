package net.wohlfart.photon;

import java.util.Arrays;
import java.util.List;

import android.app.Application;
import dagger.ObjectGraph;

public class PhotonApplication extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        Object[] modules = getModules().toArray();
        objectGraph = ObjectGraph.create(modules);
    }

    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new AndroidModule(this),
                new PhotonModule(this)
        );
    }

    public void inject(Object object) {
    	objectGraph.inject(object);
    }

}
