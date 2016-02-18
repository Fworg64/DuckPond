package com.fworg64.duckpond.game;

/**
 * Created by fworg on 2/17/2016.
 */
public class Options //not on object
{
    public static boolean highres;
    public static int screenWidth; //float for multiplication
    public static int screenHeight;

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
        screenHeight = 480;
    }
}
