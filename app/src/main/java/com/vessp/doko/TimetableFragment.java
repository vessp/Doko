package com.vessp.doko;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TimetableFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ListView lv = new ListView(getActivity());
        lv.setAdapter(new StationAdapter(getActivity(), StationManager.init(getActivity())));
        lv.setBackgroundColor(Color.BLACK);
        return lv;
    }

    public class StationAdapter extends ArrayAdapter<Station>
    {
        public StationAdapter(Context context, List<Station> list)
        {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final Station station = getItem(position);
            StationView view;
            if(convertView != null)
                view = (StationView)convertView;
            else
                view = new StationView(getContext(), station);

            return view;
        }
    }
}
