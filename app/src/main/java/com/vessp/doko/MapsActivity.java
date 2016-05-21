package com.vessp.doko;

import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapClickListener
{
    private final String T = MapsActivity.class.getSimpleName();

    private List<Station> stations;

    private GoogleMap map;
    private TextToSpeech tts;
    private Location myLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        JodaTimeAndroid.init(this);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override public void onInit(int status) { }
        });
        tts.addSpeech("tcome", getPackageName(), R.raw.come);

        stations = StationManager.init(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null)
            {
                setUpMap();
            }
        }
    }

    private void setUpMap()
    {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMyLocationChangeListener(this);
        map.setOnMapClickListener(this);


    }

    @Override
    public void onMyLocationChange(Location loc)
    {
        Log.v(T, "onMyLocationChange(): loc: " + loc);

        DokoApp.instance.myLoc = loc;
        myLoc = loc;
        LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));


//        final LatLng MELBOURNE = new LatLng(-37.813, 144.962);
//        Marker melbourne = map.addMarker(new MarkerOptions()
//                        .position(MELBOURNE)
//                        .title("Melbourne")
//                        .snippet("Population: " + System.currentTimeMillis())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
//        );
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Log.v(T, "onMapClick(): loc: " + latLng);

        Location clickLoc = new Location("");
        clickLoc.setLatitude(latLng.latitude);
        clickLoc.setLongitude(latLng.longitude);

        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        giveStatus(clickLoc);
    }

    private void giveStatus(Location curLoc)
    {
        Station s = stations.get(0);//todo find closest

        float d = curLoc.distanceTo(s.loc()); //meters
        float v = 1;
        float t = d/v;

        LocalTime curTime = DateTime.now().toLocalTime();
//        LocalTime curTime = new LocalTime(5, 34);

//        float secTillTrain = 0;
//        for(int i=0; i<s.weekdays.size(); i++)
//        {
//            LocalTime trainTime = s.weekdays.get(i);
//            if(curTime.isBefore(trainTime))
//            {
//                Log.i(T, "targetTime: " + trainTime);
//                secTillTrain = trainTime.millisOfDay().get()/1000.0f - curTime.millisOfDay().get()/1000.0f;
//                break;
//            }
//        }
//
//        DecimalFormat f = new DecimalFormat("##.00");
//
//        String toast = "Time till train " + (int)secTillTrain/60 + " minutes.  You will arrive in " + f.format(t/60.0) + " minutes.";
//
//        if(t < secTillTrain)
//        {
//            toast += " Walking speed is fine.";
//        }
//        else
//        {
//            float newV = d / secTillTrain;
//            int percent = (int)(newV*100-100);
//            toast += " Increase speed by " + percent + " percent. " + (percent > 75 ? "Run!" : "");
//        }
//
//        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
//        tts.speak(toast, TextToSpeech.QUEUE_FLUSH, null, "1");
    }
}
