package net.wohlfart.photon.ui;

import javax.inject.Inject;

import net.wohlfart.photon.PhotonBaseActivity;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends PhotonBaseActivity {
  @Inject
  LocationManager locationManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // After the super.onCreate call returns we are guaranteed our injections are available.

    TextView text = new TextView(this);
    text.setText("Hello Photon!");
    setContentView(text);

    // TODO do something with the injected dependencies here!
  }
}
