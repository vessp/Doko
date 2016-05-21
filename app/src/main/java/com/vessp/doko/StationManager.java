package com.vessp.doko;

import android.app.Activity;

import org.joda.time.LocalTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StationManager
{
    public static List<Station> init(Activity context)
    {
        //http://www.kotsu.city.nagoya.jp/subway/station_info/index.html

        List<Station> stations = new ArrayList<>();

        Station s = new Station("Shonai-dori", 35.20426218355877, 136.8911886960268);
        s.tracks.add(makeTrack(context, "TSU-東", R.raw.shonaidori_south));
        stations.add(s);

        s = new Station("Marunoichi", 35.1750795, 136.8963108);
        s.tracks.add(makeTrack(context, "TSU-北", R.raw.marunoichi_north));
        s.tracks.add(makeTrack(context, "TSU-南", R.raw.marunoichi_south));
        s.tracks.add(makeTrack(context, "SD-西", R.raw.marunoichi_west));
        s.tracks.add(makeTrack(context, "SD-東", R.raw.marunoichi_east));
        stations.add(s);

        s = new Station("Fushimi", 35.169290, 136.897600);
        s.tracks.add(makeTrack(context, "TSU-北", R.raw.fushimi_north));
        s.tracks.add(makeTrack(context, "TSU-南", R.raw.fushimi_south));
        s.tracks.add(makeTrack(context, "HIG-西", R.raw.fushimi_west));
        s.tracks.add(makeTrack(context, "HIG-東", R.raw.fushimi_east));
        stations.add(s);

        s = new Station("Tsurumai", 35.156367, 136.916859);
        s.tracks.add(makeTrack(context, "TSU-北", R.raw.tsurumai_north));
        s.tracks.add(makeTrack(context, "TSU-南", R.raw.tsurumai_south));
        stations.add(s);

        s = new Station("Nagoya", 35.170785, 136.881897);
        s.tracks.add(makeTrack(context, "HIG-東", R.raw.nagoya_east_hig));
        stations.add(s);

        return stations;
    }

    public static Station.Track makeTrack(Activity context, String name, int res)
    {
        Station.Track t = new Station.Track(name);
        try
        {
            InputStream raw = context.getResources().openRawResource(res);
            BufferedReader r = new BufferedReader(new InputStreamReader(raw, "UTF-8"));
            String line;
            while ((line = r.readLine()) != null)
            {
                String[] hourParts = line.split("\t");
                try
                {
                    if(hourParts.length < 3)
                        throw new Exception("unexpected line formatting");

                    int hour = Integer.parseInt(hourParts[0]);
                    String[] minuteParts = hourParts[1].split(" ");
                    for(String minutePart : minuteParts)
                    {
                        int minute = Integer.parseInt(minutePart.replaceAll("[\\D]", ""));
//                        Log.d("asdf", hour + ":" + minute);
                        t.weekdays.add(new LocalTime(hour, minute));
                    }
                    minuteParts = hourParts[2].split(" ");
                    for(String minutePart : minuteParts)
                    {
                        int minute = Integer.parseInt(minutePart.replaceAll("[\\D]", ""));
//                        Log.d("asdf", hour + ":" + minute);
                        t.weekends.add(new LocalTime(hour, minute));
                    }
                }
                catch(NumberFormatException e)
                {
                    //skip line
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            return t;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
