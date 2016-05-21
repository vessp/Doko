package com.vessp.doko;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.Duration;

public class TrackView extends LinearLayout
{
    private Station.Track track;
    private TextView name;
    private TextView time;

    public int distance;

    private Handler h = new Handler();

    public TrackView(Context context, Station.Track track)
    {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.track_view, this);
        name = (TextView)findViewById(R.id.name);
        time = (TextView)findViewById(R.id.time);

        init(track);
    }

    public void init(Station.Track track)
    {
        distance = -1;
        this.track = track;
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
            Duration ato = track.getTimeTillNext();

            name.setText(track.name);
            time.setText(dig2(ato.getStandardMinutes()) + ":" + dig2(ato.getStandardSeconds() % 60));

            if(distance != -1)
            {
                if(distance > ato.getStandardSeconds())
                    time.setTextColor(Color.RED);
                else
                    time.setTextColor(Color.GREEN);
            }

            h.postDelayed(onUpdate, 1000);
        }
    };

    private String dig2(long v)
    {
        return String.format("%02d", v);
    }
}
