package net.wohlfart.photon;

import android.app.Activity;
import android.os.Bundle;

public abstract class PhotonBaseActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  // Perform injection so that when this call returns all dependencies will be available for use.
	  ((PhotonApplication) getApplication()).inject(this);
  }

}
