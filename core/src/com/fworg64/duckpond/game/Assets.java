package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    public static Texture GameBackground; //no need for a region if bckgnd is whole image

    private static Texture MainMenuButtons;
    private static TextureRegion[][] MainMenuButtonmap;
    public static TextureRegion StartButt;
    public static TextureRegion OptionsButt;
    public static TextureRegion LevelEditButt;
    public static TextureRegion ExitButt;

    private static Texture duck;
    private static TextureRegion[][] duckframes;
    private static TextureRegion[] duckSwim;
    private static TextureRegion[] duckPad;
    public static Array<TextureRegion> duckSwimFrames;
    public static Array<TextureRegion> duckPadFrames;

    private static Texture lily;
    private static TextureRegion[][] lilyframes;
    private static TextureRegion[] lilyRot;
    public static Array<TextureRegion> lilyRotFrames;




    public static void load()
    {
        font = new BitmapFont();
        font.setColor(Color.FIREBRICK); //eventually get a real font

        MainMenuBackground = new Texture(Gdx.files.internal("bkgnd.png"));
        MainMenuButtons = new Texture(Gdx.files.internal("buttons.png"));
        MainMenuButtonmap = TextureRegion.split(MainMenuButtons,193,80);
        StartButt = MainMenuButtonmap[0][0];
        LevelEditButt = MainMenuButtonmap[1][0];
        OptionsButt = MainMenuButtonmap[2][0];
        ExitButt = MainMenuButtonmap[3][0];

        GameBackground = new Texture(Gdx.files.internal("gbkgnd.png"));

        duck = new Texture(Gdx.files.internal("duck.png"));
        duckframes = TextureRegion.split(duck,96,96);
        duckSwim = new TextureRegion[] {duckframes[0][1], duckframes[1][1], duckframes[2][1], duckframes[3][1]};
        duckPad =  new TextureRegion[] {duckframes[0][0], duckframes[1][0]};
        duckSwimFrames = new Array<TextureRegion>(duckSwim);
        duckPadFrames = new Array<TextureRegion>(duckPad);

        lily = new Texture(Gdx.files.internal("lily.png"));
        lilyframes = TextureRegion.split(lily,96,96);
        lilyRot = new TextureRegion[] {lilyframes[0][0], lilyframes[1][0], lilyframes[2][0]};
        lilyRotFrames = new Array<TextureRegion>(lilyRot);
    }
}
