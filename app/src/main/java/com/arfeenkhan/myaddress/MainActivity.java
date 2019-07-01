package com.arfeenkhan.myaddress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.btnLocation)
    Button btnProceed;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.rlPickLocation)
    RelativeLayout rlPick;

    private Location mLastLocation;
    double latitude;
    double longitude;

    LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);
        locationHelper.checkpermission();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
