package com.example.mpos_projekat;

import android.Manifest;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private double latitude, longitude;
    private double end_latitude, end_longitude;

    NotificationManagerCompat notificationManagerCompat;

    String tipVozila;

    List<Vozilo> listaVozila;

    String model;
    String marka;
    String registracija;
    int brPutnika;
    double cena;
    String kljuc;
    String tip;
    boolean rezervisan, vecRezervisao = false, dataUpdated;
    int count = 0, brMinuta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        tipVozila = getIntent().getStringExtra("tipVozila");
        notificationManagerCompat= NotificationManagerCompat.from(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            checkLocationPermission();
        }

        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Google Play Services nije dostupan!");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services je dostupan.");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listaVozila = new ArrayList<>();

        new FirebaseDatabaseHelper().readVehicles(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {

                for (Vozilo v: vozila) {
                    listaVozila.add(v);
                }

                for (Vozilo vozilo: listaVozila) {
                    if (vozilo.getTip().equals(tipVozila)) {
                        if (!vozilo.isRezervisan()) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(vozilo.getLat(), vozilo.getLng()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(vozilo.getLat(), vozilo.getLng()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                    }
                }

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Trenutna pozicija");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }
    Vozilo vozilo = new Vozilo();

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.setDraggable(true);

        if (!marker.equals(mCurrLocationMarker)) {
            if (!vecRezervisao) {
                new FirebaseDatabaseHelper().readVehicles(new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {
                        for (int i = 0; i < vozila.size(); i++) {
                            if (vozila.get(i).getLat() == marker.getPosition().latitude &&
                                    vozila.get(i).getLng() == marker.getPosition().longitude) {

                                vozilo.setTip(vozila.get(i).getTip());
                                vozilo.setBrojPutnika(vozila.get(i).getBrojPutnika());
                                vozilo.setCenaPoMinutu(vozila.get(i).getCenaPoMinutu());
                                vozilo.setRegistracija(vozila.get(i).getRegistracija());
                                vozilo.setModel(vozila.get(i).getModel());
                                vozilo.setMarka(vozila.get(i).getMarka());
                                vozilo.setLng(vozila.get(i).getLng());
                                vozilo.setLat(vozila.get(i).getLat());
                                vozilo.setRezervisan(vozila.get(i).isRezervisan());

                                cena = vozilo.getCenaPoMinutu();
                                registracija = vozilo.getRegistracija();
                                brPutnika =vozilo.getBrojPutnika();
                                marka = vozilo.getMarka();
                                model = vozilo.getModel();
                                kljuc = keys.get(i);

                                //ovde je bio openDialog kad je polovicno radio


                                Log.d("Vozilo: ", vozilo.toString());
                                break;
                            }
                        }
                        if (!vecRezervisao) {
                            if (!vozilo.isRezervisan()) {
                                openDialog();
                            } else {
                                Toast.makeText(GoogleMapActivity.this, "Ovo vozilo je zauzeto!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void DataIsInserted() {

                    }
                    @Override
                    public void DataIsUpdated() {

                    }
                    @Override
                    public void DataIsDeleted() {

                    }
                });
            } else {
                Toast.makeText(this, "Vec ste rezervisali vozilo!", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void openDialog() {
        PopupDialog popupDialog = new PopupDialog();
        Bundle bundle = new Bundle();
        bundle.putString("marka", marka);
        bundle.putString("model", model);
        bundle.putDouble("cena", cena);
        bundle.putInt("brPutnika", brPutnika);
        bundle.putString("registracija", registracija);
        bundle.putDouble("lat", vozilo.getLat());
        bundle.putDouble("lng", vozilo.getLng());
        bundle.putString("kljuc", kljuc);
        bundle.putBoolean("rezervisan", vozilo.isRezervisan());
        bundle.putString("tip", vozilo.getTip());

        popupDialog.setArguments(bundle);

        popupDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void prikaziNotifikaciju(String poruka, String naslov) {

        Notification notification = new NotificationCompat.Builder(this, App.CHANEL_1_ID)
                .setContentTitle(naslov)
                .setContentText(poruka)
                .setSmallIcon(R.drawable.ikonica)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setOngoing(true)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    public void prikaziOtklonjivuNotifikaciju (String poruka, String naslov) {

        Notification notification = new NotificationCompat.Builder(this, App.CHANEL_1_ID)
                .setContentTitle(naslov)
                .setContentText(poruka)
                .setSmallIcon(R.drawable.ikonica)
                .build();
        notificationManagerCompat.notify(1, notification);
    }

    Timer T;
    public void pokreniTimer () {
        T = new Timer();
        count = 0;
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (count == (brMinuta-5)*60) {
                        prikaziOtklonjivuNotifikaciju("Vasa voznja traje jos 5 minuta!", "Rezervacija uskoro istice");
                    }
                    if (count == brMinuta * 60) {
                        vozilo.setRezervisan(false);

                        new FirebaseDatabaseHelper().izmeniVozilo(kljuc, vozilo, new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<Vozilo> vozila, List<String> keys) {

                            }
                            @Override
                            public void DataIsInserted() {

                            }
                            @Override
                            public void DataIsUpdated() {
                                vecRezervisao = false;
                            }
                            @Override
                            public void DataIsDeleted() {

                            }
                        });
                        prikaziOtklonjivuNotifikaciju("Vasa voznja je zavrsena", "Dovidjenja!");

                        T.cancel();
                        T.purge();
                        T = null;
                        return;
                    }
                    count++;
                    Log.d("Timer: ", count + "");
                    }
            });
            }
        }, 1000, 1000);

    }

}


