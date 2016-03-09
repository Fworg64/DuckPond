package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import sun.applet.Main;

/**
 * This is the Assets files, it handles loading of assets
 * and is where all assets are accesed from
 *
 *
 * this includes textures and sounds
 *
 * Created by fworg on 2/9/2016.
 */
public class Assets
{
    public static BitmapFont font;

    public static Texture LevelEditBg;
    private static Texture LevelEditPartSheet;
    private static TextureRegion[][] LevelEditParts;
    public static TextureRegion leveditDuck;
    public static TextureRegion leveditShark;
    public static TextureRegion leveditPad;
    public static TextureRegion leveditTknob;
    public static TextureRegion leveditTslider;
    public static TextureRegion leveditDup;
    public static TextureRegion leveditDdown;
    public static TextureRegion leveditTaccept;
    public static TextureRegion leveditDaccept;
    public static TextureRegion leveditTrash;

    public static Texture LevelSelectionBackground;

    public static Texture MainMenuBackground;
    public static Texture OptionsMenu;
    public static Texture GameBackground;
    public static Texture HUD;

    public static Texture PauseMenu;
    public static Texture ShowConfirmRestart;
    public static Texture ShowConfirmExit;
    public static Texture Victory;
    public static Texture Defeat;

    private static Texture GUIelements;
    private static TextureRegion[][] GUIparts;
    public static TextureRegion check;
    public static TextureRegion slideball;

    private static Texture duck;
    private static TextureRegion[][] duckframes;
    private static TextureRegion[] duckSwimUp;
    private static TextureRegion[] duckSwimDown;
    private static TextureRegion[] duckSwimSideRight;
    //private static TextureRegion[] duckSwimSideLeft;
    private static TextureRegion[] duckPad;
    private static TextureRegion[] duckEaten;
    public static Array<TextureRegion> duckSwimUpFrames;
    public static Array<TextureRegion> duckSwimDownFrames;
    public static Array<TextureRegion> duckSwimSideRightFrames;
    public static Array<TextureRegion> duckSwimSideLeftFrames;
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

    static String res;




    public static void load()
    {
        if (Options.highres) res = "highres\\";
        else res = "stdres\\";

        font = new BitmapFont(Gdx.files.internal(res + "FONT\\opensans.fnt"));
        MainMenuBackground = new Texture(Gdx.files.internal(res + "MainMenu.png"));
        OptionsMenu = new Texture(Gdx.files.internal(res + "OptionsMenu.png"));
        GameBackground = new Texture(Gdx.files.internal(res + "gbkgnd.png"));
        HUD = new Texture(Gdx.files.internal(res + "HUD.png"));

        LevelSelectionBackground = new Texture(Gdx.files.internal(res + "LevelSelectionBckgnd.png"));

        PauseMenu = new Texture(Gdx.files.internal(res + "pause.png"));
        ShowConfirmExit = new Texture(Gdx.files.internal(res + "exitconfirm.png"));
        ShowConfirmRestart = new Texture(Gdx.files.internal(res + "restartconfirm.png"));
        Victory = new Texture(Gdx.files.internal(res + "victory.png"));
        Defeat = new Texture(Gdx.files.internal(res + "defeat.png"));

        GUIelements = new Texture(Gdx.files.internal(res + "guielements.png"));
        GUIparts = TextureRegion.split(GUIelements, Options.GUIWidth, Options.GUIHeight);
        check = GUIparts[0][0];
        slideball = GUIparts[0][1];

        duck = new Texture(Gdx.files.internal(res + "duck.png"));
        duckframes = TextureRegion.split(duck, Options.spriteWidth,Options.spriteHeight);
        duckSwimUp = new TextureRegion[] {duckframes[0][0], duckframes[1][0], duckframes[2][0]};
        duckSwimDown = new TextureRegion[] {duckframes[0][2], duckframes[1][2], duckframes[2][2]};
        duckSwimSideRight = new TextureRegion[] {duckframes[0][1], duckframes[1][1], duckframes[2][1]};
        //duckSwimSideLeft = new TextureRegion[] {duckframes[0][1], duckframes[1][1], duckframes[2][1]};
        duckPad =  new TextureRegion[] {duckframes[0][4], duckframes[1][4], duckframes[2][4]};
        duckEaten = new TextureRegion[] {duckframes[0][3], duckframes[1][3], duckframes[2][3]};
        duckSwimUpFrames = new Array<TextureRegion>(duckSwimUp);
        duckSwimDownFrames = new Array<TextureRegion>(duckSwimDown);
        duckSwimSideRightFrames = new Array<TextureRegion>(duckSwimSideRight);
        duckSwimSideLeftFrames = new Array<TextureRegion>();
        for (TextureRegion t:duckSwimSideRightFrames)
        {
            TextureRegion temp = new TextureRegion(t);
            temp.flip(true,false);
            duckSwimSideLeftFrames.add(temp);
        }
        duckPadFrames = new Array<TextureRegion>(duckPad);
        duckEatenFrames = new Array<TextureRegion>(duckEaten);

        lily = new Texture(Gdx.files.internal(res + "lily.png"));
        lilyframes = TextureRegion.split(lily, Options.spriteWidth,Options.spriteHeight);
        lilyRot = new TextureRegion[] {lilyframes[0][0], lilyframes[1][0], lilyframes[2][0]};
        lilyRotFrames = new Array<TextureRegion>(lilyRot);

        shark = new Texture(Gdx.files.internal(res + "shark.png"));
        sharkframes = TextureRegion.split(shark, Options.spriteWidth,Options.spriteHeight);
        sharkSwim = new TextureRegion[] {sharkframes[0][0], sharkframes[1][0]};
        sharkEat = new TextureRegion[] {sharkframes[0][1], sharkframes[1][1], sharkframes[2][1]};
        sharkSwimFrames = new Array<TextureRegion>(sharkSwim);
        sharkEatFrames = new Array<TextureRegion>(sharkEat);

        // i think we're suppose to dispose of some textures here...
        //or maybe that will make the world burn

    }

    public static void levelEditLoad()
    {
        font = new BitmapFont(Gdx.files.internal("leveledit\\FONT\\opensans.fnt"));
        LevelEditBg = new Texture(Gdx.files.internal("leveledit\\LevelEditBackground.png"));
        GameBackground = new Texture(Gdx.files.internal("leveledit\\gbkgnd.png"));
        LevelEditPartSheet = new Texture(Gdx.files.internal("leveledit\\leveledit.png"));
        LevelEditParts = TextureRegion.split(LevelEditPartSheet, 48, 48);
        leveditDuck = LevelEditParts[0][0];
        leveditShark = LevelEditParts[0][1];
        leveditPad = LevelEditParts[0][2];
        leveditTknob = LevelEditParts[0][3];
        leveditTslider = LevelEditParts[0][4];
        leveditDup = LevelEditParts[1][0];
        leveditDdown = LevelEditParts[1][1];
        leveditTaccept = LevelEditParts[1][2];
        leveditDaccept = LevelEditParts[1][3];
        leveditTrash = LevelEditParts[1][4];

    }
}
