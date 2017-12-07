package com.dev.sevice.findbymap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 11/16/2017 AD.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener{
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;
    private static EditText edt_start;
    private static ImageView img_start;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        init();
        initlistenner();
    }

    private void init(){
        edt_start = (EditText)findViewById(R.id.edt_start);
        img_start = (ImageView)findViewById(R.id.img_start);
    }

    private void initlistenner(){
        img_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_start:
                getLocationBySearch("start");
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
//            latitude = lastLocation.getLatitude();
//            longitude = lastLocation.getLongitude();
            getCurrentLocation();

        } else {
            Toast.makeText(this, "ไม่สามารถระบุตำแหน่งได้", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(13.676475, 100.603902);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        moveMap(sydney.latitude,sydney.longitude);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 140, 10, 0);


    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MainActivity Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private String starDistination = "";
    private void getLocationBySearch(String txt_position) {
        try {
            if(txt_position.equalsIgnoreCase("start")){
                starDistination = edt_start.getText().toString().trim();
            }else if(txt_position.equalsIgnoreCase("des")){
//                starDistination = edt_des.getText().toString().trim();
            }

            List<Address> addressList = null;
            if(starDistination.length()>0){
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(starDistination, 10);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address;

                if (addressList != null && addressList.size() != 0) {
                    address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                    if(txt_position.equalsIgnoreCase("start")){
//                        addMarkerStart(latLng);
//                        String strAddress = getAddressByLatLng(address.getLatitude(),address.getLongitude());
//                        edt_start.setText((strAddress.length()>50) ? strAddress.substring(0,50) + ".." : strAddress);
//                        setStartLocation(address.getLatitude(),address.getLongitude(),address.getAdminArea(),strAddress);
//                    }else if(txt_position.equalsIgnoreCase("des")){
//                        addMarkerDestination(latLng);
//                        String strAddress = getAddressByLatLng(address.getLatitude(),address.getLongitude());
//                        edt_des.setText((strAddress.length()>50) ? strAddress.substring(0,50) + ".." : strAddress);
//                        setDestination(address.getLatitude(),address.getLongitude(),address.getAdminArea(),strAddress);
//                    }

                    moveMap(address.getLatitude(),address.getLongitude());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location != null) {
                //moving the map to location
                moveMap(location.getLatitude() ,location.getLongitude());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void moveMap(double lat ,double lon) {
        LatLng latLng = new LatLng(lat, lon);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }
}
