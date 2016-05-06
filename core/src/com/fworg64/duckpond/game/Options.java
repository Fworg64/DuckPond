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

    public static String custom1; //custom level, mainly for html
    public static String username;
    public static String pin;

    public static int screenWidth; //implied by highres
    public static int screenHeight;
    public static int spriteWidth; //implied by screen width
    public static int spriteHeight;
    public static int GUIElementWidth;
    public static int GUIElementHeight;
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
            prefs.putString("custom1", "60 2\n" +
                    "0.0 Duck (332.0,0.0) (-46.2,39.899998) 5\n" +
                    "0.0 Duck (-62.0,-16.0) (44.1,56.0) 7\n" +
                    "0.0 Shark (38.0,512.0) (14.7,-44.8) 0\n" +
                    "0.0 Lily (134.0,16.0) (0.0,0.0) 0\n");
            prefs.putString("username", "");
            prefs.putString("pin", "");
            prefs.flush();
        }
        isDefault = prefs.getBoolean("isDefault");
        sfxVol = prefs.getFloat("sfxVol");
        musicVol = prefs.getFloat("musicVol");
        highres = prefs.getBoolean("highres");
        custom1 = prefs.getString("custom1");
        username = prefs.getString("username");
        pin = prefs.getString("pin");

        if (highres) setHighres();
        else setStdres();
    }

    public static void setStdres()
    {
        highres = false;
        screenWidth = DuckPondGame.stdresScreenW;
        screenHeight = DuckPondGame.stdresScreenH;
        spriteWidth = DuckPondGame.stdspriteW;
        spriteHeight = DuckPondGame.stdspriteH;
        GUIHeight = DuckPondGame.stdresScreenH - DuckPondGame.worldH; //96
        GUIElementWidth = (int)(GUIHeight*.5f);
        GUIElementHeight = (int)(GUIHeight*.5f);
        prefs.putBoolean("highres", highres);
    }

    public static void setHighres()
    {
        highres = true;
        screenWidth = DuckPondGame.highresScreenW;
        screenHeight = DuckPondGame.highresScreenH;
        spriteWidth = DuckPondGame.highspriteW;
        spriteHeight = DuckPondGame.highspriteH;
        GUIHeight = DuckPondGame.highresScreenH - (int)(DuckPondGame.worldH * DuckPondGame.highresworldscaler);
        GUIElementWidth = (int)(GUIHeight*.5f);
        GUIElementHeight = (int)(GUIHeight*.5f);
        prefs.putBoolean("highres", highres);
    }

    public static boolean isHighres()
    {
        highres = prefs.getBoolean("highres");
        return highres;
    }

    public static float getMusicVol()
    {
        musicVol = prefs.getFloat("musicVol");
        return musicVol;
    }

    public static void setMusicVol(float musicVol)
    {
        Options.musicVol = musicVol;
        prefs.putFloat("musicVol", Options.musicVol);
    }

    public static float getSfxVol()
    {
        sfxVol = prefs.getFloat("sfxVol");
        return sfxVol;
    }

    public static void setSfxVol(float sfxVol)
    {
        Options.sfxVol = sfxVol;
        prefs.putFloat("sfxVol", Options.sfxVol);
    }

    public static String getCustom1()
    {
        custom1 = prefs.getString("custom1");
        return custom1;
    }

    public static void setCustom1(String custom1)
    {
        Options.custom1 = custom1;
        prefs.putString("custom1", Options.custom1);
    }

    public static void setUsername(String username1)
    {
        username = username1;
        prefs.putString("username", username);
    }

    public static String getUsername()
    {
        username = prefs.getString("username");
        return username;
    }

    public static void setSavedPin(String pin1)
    {
        pin = pin1;
        prefs.putString("pin", pin);
    }

    public static String getSavedPin()
    {
        pin = prefs.getString("pin");
        return pin;
    }

    public static void save()
    {
        prefs.flush();
    }
}
