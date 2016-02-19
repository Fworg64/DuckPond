package com.fworg64.duckpond.game;

/**
 * Created by fworg on 2/17/2016.
 */
public class Options
{
    public static boolean highres;
    public static int screenWidth;
    public static int screenHeight;

    public Options()
    {
        loadDefault();
    }

    public static void loadDefault()
    {
        highres = false;
        screenWidth = 320;
        screenHeight = 480;
    }

    public static void setHighres()
    {
        highres = true;
        screenWidth = 640;
        screenHeight = 960;
    }
}
