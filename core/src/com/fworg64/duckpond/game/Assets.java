package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import sun.applet.Main;

/**
 * This is the ASSets files, it handles loading of assets
 * and is where all assets are accesed from
 *
 * this includes textures and sounds
 *
 * Created by fworg on 2/9/2016.
 */
public class Assets
{
    public static BitmapFont font;

    public static Texture MainMenuBackground;
    public static Texture OptionsMenu;
    public static Texture LevelEditBg;
    public static Texture GameBackground;

    private static Texture duck;
    private static TextureRegion[][] duckframes;
    private static TextureRegion[] duckSwim;
    private static TextureRegion[] duckPad;
    private static TextureRegion[] duckEaten;
    public static Array<TextureRegion> duckSwimFrames;
    public static Array<TextureRegion> duckPadFrames;
    public static Array<TextureRegion> duckEatenFrames;

    private static Texture shark;
    private static TextureRegion[][] sharkframes;
    private static TextureRegion[] sharkSwim;
    private static TextureRegion[] sharkEat;
    public static Array<TextureRegion> sharkSwimFrames;
    public static Array<TextureRegion> sharkEatFrames;

    private static Texture lily;
    private static TextureRegion[][] lilyframes;
    private static TextureRegion[] lilyRot;
    public static Array<TextureRegion> lilyRotFrames;




    public static void load()
    {
//        font = new BitmapFont();
//        font.setColor(Color.FIREBRICK); //eventually get a real font

        MainMenuBackground = new Texture(Gdx.files.internal("MainMenu.png"));
        OptionsMenu = new Texture(Gdx.files.internal("OptionsMenu.png"));
        LevelEditBg = new Texture(Gdx.files.internal("LevelEditBackground.png"));
        GameBackground = new Texture(Gdx.files.internal("gbkgnd.png"));

        duck = new Texture(Gdx.files.internal("duck.png"));
        duckframes = TextureRegion.split(duck, 96,96);
        duckSwim = new TextureRegion[] {duckframes[0][1], duckframes[1][1], duckframes[2][1], duckframes[3][1]};
        duckPad =  new TextureRegion[] {duckframes[0][0], duckframes[1][0]};
        duckEaten = new TextureRegion[] {duckframes[0][2], duckframes[1][2], duckframes[2][2]};
        duckSwimFrames = new Array<TextureRegion>(duckSwim);
        duckPadFrames = new Array<TextureRegion>(duckPad);
        duckEatenFrames = new Array<TextureRegion>(duckEaten);

        lily = new Texture(Gdx.files.internal("lily.png"));
        lilyframes = TextureRegion.split(lily, 96,96);
        lilyRot = new TextureRegion[] {lilyframes[0][0], lilyframes[1][0], lilyframes[2][0]};
        lilyRotFrames = new Array<TextureRegion>(lilyRot);

        shark = new Texture(Gdx.files.internal("shark.png"));
        sharkframes = TextureRegion.split(shark, 96,96);
        sharkSwim = new TextureRegion[] {sharkframes[0][0], sharkframes[1][0]};
        sharkEat = new TextureRegion[] {sharkframes[0][1], sharkframes[1][1], sharkframes[2][1]};
        sharkSwimFrames = new Array<TextureRegion>(sharkSwim);
        sharkEatFrames = new Array<TextureRegion>(sharkEat);

        // i think we're suppose to dispose of some textures here...
        //or maybe that will make the world burn

    }
}
