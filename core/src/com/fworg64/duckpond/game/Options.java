package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by fworg on 2/17/2016.
 *
 * Resolution Aware (duh)
 */
public class Options
{
    public static boolean highres;
    public static float sfxVol;
    public static float musicVol;

    public static int screenWidth; //implied by highres
    public static int screenHeight;
    public static int spriteWidth; //implied by screen width
    public static int spriteHeight;
    public static int GUIWidth;
    public static int GUIHeight;

    private static Preferences prefs;
    private static boolean isDefault;

    public static void loadOptions()
    {
        prefs = Gdx.app.getPreferences("com.fworg64.duckpond.options");
        if (!prefs.contains("isDefault")) //options not present
        {
            prefs.putBoolean("isDefault", true);
            prefs.putBoolean("highres", false);
            prefs.putFloat("sfxVol", 1f);
            prefs.putFloat("musicVol", 1f);
        }
        isDefault = prefs.getBoolean("isDefault");
        sfxVol = prefs.getFloat("sfxVol");
        musicVol = prefs.getFloat("musicVol");
        highres = prefs.getBoolean("highres");

        if (highres) setHighres();
        else setStdres();
    }

    public static void setStdres()
    {
        highres = false;
        screenWidth = DuckPondGame.worldW;
        screenHeight = DuckPondGame.worldH;
        spriteWidth = DuckPondGame.spriteW;
        spriteHeight = DuckPondGame.spriteH;
        GUIWidth = 32;
        GUIHeight = 32;
        prefs.putBoolean("highres", highres);


    }

    public static void setHighres()
    {
        highres = true;
        screenWidth = 2*DuckPondGame.worldW;
        screenHeight = 2*DuckPondGame.worldH;
        spriteWidth = 2*DuckPondGame.spriteW;
        spriteHeight = 2*DuckPondGame.spriteH;
        GUIWidth = 64;
        GUIHeight = 64;
        prefs.putBoolean("highres", highres);
    }

    public static void save()
    {
        prefs.flush();
    }
}
