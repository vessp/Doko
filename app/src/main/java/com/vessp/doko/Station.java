package com.vessp.doko;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class Station
{
    public String name;
    public final LatLng latLng;
    public List<Track> tracks = new ArrayList<>();

    public Station(String name, double lat, double lon)
    {
        this.name = name;
        latLng = new LatLng(lat, lon);
    }

    public Location loc()
    {
        Location loc = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }

    public static class Track
    {
        public String name;
        public List<LocalTime> weekdays = new ArrayList<>();
        public List<LocalTime> weekends = new ArrayList<>();

        public Track(String name)
        {
            this.name = name;
        }

        public Duration getTimeTillNext()
        {
            DateTime now =  DateTime.now();

            List<LocalTime> times = weekdays;
            if(now.getDayOfWeek() == DateTimeConstants.SATURDAY || now.getDayOfWeek() == DateTimeConstants.SUNDAY)
                times = weekends;

            LocalTime curTime = now.toLocalTime();

            float secTillTrain = 0;
            for(int i=0; i<times.size(); i++)
            {
                LocalTime trainTime = times.get(i);
                if(curTime.isBefore(trainTime))
                {
//                    Log.i(T, "targetTime: " + trainTime);
                    secTillTrain = trainTime.millisOfDay().get()/1000.0f - curTime.millisOfDay().get()/1000.0f;
                    break;
                }
            }

            return new Duration((long)(secTillTrain * 1000));
        }
    }

}
