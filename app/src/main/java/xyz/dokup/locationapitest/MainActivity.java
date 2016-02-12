package xyz.dokup.locationapitest;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final MainActivity self = this;

    private static final int REQUEST = 9000;
    private GoogleApiClient client;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        findViewById(R.id.btn_find_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectApi();
            }
        });
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectApi();
                ((TextView)findViewById(R.id.text_location_result)).setText(getString(R.string.text_result_default));
            }
        });


    }

    private void connectApi() {
        client.connect();
    }

    private void disconnectApi() {
        if (client.isConnected()) {
            client.disconnect();
        }
    }



    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(client);
        if(location != null) {
            String result = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
            ((TextView)findViewById(R.id.text_location_result)).setText(result);
        } else {
            Toast.makeText(self, "Location is not detected", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("MainActivity", "Connection suspended");
        client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("MainActivity", "Connection faild. Error: " + connectionResult.getErrorCode());
    }
}
