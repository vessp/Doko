package com.vessp.doko;

import android.app.Application;
import android.location.Location;

public class DokoApp extends Application
{
    public static DokoApp instance;

    public DokoApp()
    {
        instance = this;
    }

    public Location myLoc;

}
