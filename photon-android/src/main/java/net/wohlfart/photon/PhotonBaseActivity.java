package net.wohlfart.photon;

import jogamp.newt.driver.android.NewtBaseActivity;
import android.os.Bundle;

public abstract class PhotonBaseActivity extends NewtBaseActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  // Perform injection so that when this call returns all dependencies will be available for use.
	  ((PhotonApplication) getApplication()).inject(this);
  }

}
