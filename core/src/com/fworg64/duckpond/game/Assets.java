package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by fworg on 2/9/2016.
 */
public class Assets
{
    public static BitmapFont font;

    public static Texture MainMenuBackground;
    public static Texture GameBackground; //no need for a region if bckgnd is whole image

    public static Texture MainMenuObjects;
    public static TextureRegion StartButt;

    public static Texture objects;
    public static TextureRegion duck; //make array once we have some animation frames
    public static TextureRegion lily;




    public static void load()
    {
        font = new BitmapFont();
        font.setColor(Color.FIREBRICK); //eventually get a real font

        MainMenuBackground = new Texture(Gdx.files.internal("bkgnd.png"));
        MainMenuObjects = new Texture(Gdx.files.internal("shit.png"));

        StartButt = new TextureRegion(MainMenuObjects, 193,80);

        GameBackground = new Texture(Gdx.files.internal("gbkgnd.png"));
        objects = new Texture(Gdx.files.internal("actors.png"));

        duck = new TextureRegion(objects, 96,96);
        lily = new TextureRegion(objects,96,0,192,96);
    }
}
