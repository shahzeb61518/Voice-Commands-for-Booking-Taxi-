package azdeveloper.com.helpme;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Hotel extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final int REQ_CODE_SPEECH_OUTPUT = 143;
    TextView  showTextMsghere;

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    TextToSpeech t1;
    String destinationPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        showTextMsghere= findViewById(R.id.showVoicTxtHere);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    ///////////
    public void micOpening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, how can i help you");
        t1 = new TextToSpeech(Hotel.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String b = "Hi, say book me to. place name.";
                    t1.speak(b, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_OUTPUT);
        } catch (ActivityNotFoundException tim) {
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {



        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();
        }
        Log.d("lat = ",""+latitude);
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        //onMarkerClick(mMap.addMarker(markerOptions));

        nearByPlacesHotel();


        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

//    public void onMarkerClick(Marker marker){
//        if (marker.getTitle().equals(getTitle())){
//
////            startActivity(new Intent(this,ListOfPlaces.class));
//            Toast.makeText(this,"heelo from here",Toast.LENGTH_SHORT).show();
//
//        }
//        marker.showInfoWindow();
//    }

    private void foruberClick() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri = "uber://?action=setPickup&pickup=my_location&dropoff[latitude]="+latitude+"&dropoff[longitude]="+longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            intent.putExtra(Intent.EXTRA_COMPONENT_NAME,"Islamabad");
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
            }
        }
    }


    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyAYUhLG_kroCkQkgO7hUJqmlKuoXFYm1C8");

        Log.d("Hotel", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void nearByPlacesHotel()
    {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        mMap.clear();
        String hotel = "hotel";
        String url = getUrl(latitude, longitude, hotel);
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(getApplicationContext(), "Showing Nearby Hotel", Toast.LENGTH_SHORT).show();
///////////////////////////
        final Handler handler = new Handler();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        micOpening();

                    }
                });
            }
        }, 15000);

        /////////////////////////////
    }
////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_OUTPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> voicInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    showTextMsghere.setText(voicInText.get(0));



                    String scommandBookATaxi3 = "我叫一辆出租车去";
                    String scommandBookATaxi4 = "预定我的出租车";
                    String scommandBookATaxi0 = "预定出租车";
                    String scommandBookATaxi1 = "预定我";
                    String scommandBookATaxi2 = "book me a taxi to";
                    String scommandBookATaxi5 = "book a taxi for me to";
                    String scommandBookATaxi6 = "book me to";

                    /////my code start here
                    String sshowVoicTxt = showTextMsghere.getText().toString();
                 if      (scommandBookATaxi0.trim().equals(sshowVoicTxt.trim())
                         || scommandBookATaxi1.trim().equals(sshowVoicTxt.trim())
                         ||scommandBookATaxi2.trim().equals(sshowVoicTxt.trim())
                         ||scommandBookATaxi3.trim().equals(sshowVoicTxt.trim())
                         ||scommandBookATaxi4.trim().equals(sshowVoicTxt.trim())
                         ||scommandBookATaxi5.trim().equals(sshowVoicTxt.trim())
                         ||scommandBookATaxi6.trim().equals(sshowVoicTxt.trim())){

                     showTextMsghere.setText("Book me to"+ "placeName");
                        foruberClick();
                    }else{
                        Toast.makeText(getApplicationContext(),"not working",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }
}
