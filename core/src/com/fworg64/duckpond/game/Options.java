package com.fworg64.duckpond.game;

/**
 * Created by fworg on 2/17/2016.
 *
 * Resolution Aware (duh)
 */
public class Options
{
    public static boolean highres;
    public static int screenWidth; //implied by highres
    public static int screenHeight;
    public static int spriteWidth; //implied by screen width
    public static int spriteHeight;

    public static void loadDefault()
    {
        highres = false;
        screenWidth = DuckPondGame.worldW;
        screenHeight = DuckPondGame.worldH;
        spriteWidth = DuckPondGame.spriteW;
        spriteHeight = DuckPondGame.spriteH;
    }

    public static void setHighres()
    {
        highres = true;
        screenWidth = 2*DuckPondGame.worldW;
        screenHeight = 2*DuckPondGame.worldH;
        spriteWidth = 2*DuckPondGame.spriteW;
        spriteHeight = 2*DuckPondGame.spriteH;
    }
}
