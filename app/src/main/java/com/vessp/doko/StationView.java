package com.vessp.doko;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StationView extends LinearLayout
{
    private Station station;

    private TextView name;
    private TextView distance;
    private GridLayout grid;

    private Handler h = new Handler();

    public StationView(Context context, Station station)
    {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.station_view, this);
        name = (TextView)findViewById(R.id.name);
        distance = (TextView)findViewById(R.id.distance);
        grid = (GridLayout)findViewById(R.id.grid);

        init(station);
    }

    public void init(Station station)
    {
        grid.removeAllViews();
        this.station = station;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        h.post(onUpdate);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        h.removeCallbacks(onUpdate);
    }


    private Runnable onUpdate = new Runnable()
    {
        @Override
        public void run()
        {
            name.setText(station.name);

            if(grid.getChildCount() == 0)
            {
                for (Station.Track track : station.tracks)
                {
                    grid.addView(new TrackView(getContext(), track));
                }
            }

            if(DokoApp.instance.myLoc != null)
            {
                int d = (int)DokoApp.instance.myLoc.distanceTo(station.loc());
                distance.setText(d + "m");
                setAlpha((float) scaleVal(d, 500, 4000, 1.0, 0.3));

                for(int i=0; i<grid.getChildCount(); i++)
                {
                    TrackView trackView = (TrackView) grid.getChildAt(i);
                    trackView.distance = d;
                }
            }

            h.postDelayed(onUpdate, 1000);
        }
    };

    private static double scaleVal(double curVal, double valA, double valB, double finalA, double finalB)
    {
        double progress = (curVal - valA) / (valB - valA);

//        if(bounded)
        progress = Math.max(Math.min(progress, 1.0), 0.0);
        return finalA + (progress * (finalB - finalA));
    }
}
