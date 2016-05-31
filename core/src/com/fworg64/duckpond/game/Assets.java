package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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

    public static Texture LevelEditMapaAbajo;
    public static Texture LevelEditClock;
    public static Texture LevelEditConfirm;
    public static Texture LevelEditDuck;
    public static Texture LevelEditExit;
    public static Texture LevelEditFlechaIzq;
    public static TextureRegion LevelEditFlechaDer;
    public static Texture LevelEditLily;
    public static Texture LevelEditLOAD;
    public static Texture LevelEditRemoveItem;
    public static Texture LevelEditSave;
    public static Texture LevelEditShark;
    public static Texture LevelEditTimeBar;
    public static Texture LevelEditLives;
    public static Texture LevelEditUnlives;

    public static Texture NavigationFlechaIzq;
    public static TextureRegion NavigationFlechaDer;
    public static Texture NavigationUpone;
    public static Texture NavigationCancel;
    public static Texture NavigationConfirm;
    public static Texture NavigationWorldButt;

    public static Texture LevelSelectionMainMenu;
    public static Texture LevelSelectionCustomWorld;
    public static Texture LevelSelectionGetMore;
    public static Texture LevelSelectionWorldMaker;

    public static Texture MainMenuTitle;
    public static Texture MainMenuPlay;
    public static Texture MainMenuPlayPressed;
    public static Texture MainMenuOptions;
    public static Texture MainMenuOptionsPressed;
    public static Texture MainMenuExit;
    public static Texture MainMenuExitPressed;

    public static Texture OptionsMenu;
    public static Texture OptionsMenuSlider;
    public static Texture OptionsMenuSliderPressed;
    public static Texture OptionsMenuCheckMark;
    public static Texture OptionsMenuReturnPressed;
    public static Texture OptionsMenuCreditsPressed;

    public static Texture GameBackground;
    public static Texture HUD;
    public static Texture HUDMute;
    public static Texture HUDUnmute;
    public static Texture HUDlives;

    public static Texture PauseMenu;
    public static Texture ShowConfirmRestart;
    public static Texture ShowConfirmExit;
    public static Texture Victory;
    public static Texture Defeat;

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
    public static Animation swimUpAnim;
    public static Animation swimSideRightAnim;
    public static Animation swimSideLeftAnim;
    public static Animation swimDownAnim;
    public static Animation padAnim;
    public static Animation eatenAnim;

    private static Texture shark;
    private static TextureRegion[][] sharkframes;
    private static TextureRegion[] sharkSwimLeft;
    //private static TextureRegion[] sharkSwimRight;
    private static TextureRegion[] sharkSwimUp;
    private static TextureRegion[] sharkSwimDown;
    private static TextureRegion[] sharkEat;
    public static Array<TextureRegion> sharkSwimLeftFrames;
    public static Array<TextureRegion> sharkSwimRightFrames;
    public static Array<TextureRegion> sharkSwimUpFrames;
    public static Array<TextureRegion> sharkSwimDownFrames;
    public static Array<TextureRegion> sharkEatFrames;
    public static Animation sharkEatAnim;
    public static Animation sharkSwimLeftAnim;
    public static Animation sharkSwimRightAnim;
    public static Animation sharkSwimUpAnim;
    public static Animation sharkSwimDownAnim;

    private static Texture lily;
    private static TextureRegion[][] lilyframes;
    private static TextureRegion[] lilyRot;
    public static Array<TextureRegion> lilyRotFrames;
    public static Animation padRot;


    static String res;

    public static void load_mainmenu()
    {
        if (Options.highres) res = "highres\\";
        else res = "stdres\\";

        MainMenuTitle = new Texture(Gdx.files.internal(res + "mainmenu\\title.png"));
        MainMenuPlay = new Texture(Gdx.files.internal(res + "mainmenu\\play.png"));
        MainMenuPlayPressed = new Texture(Gdx.files.internal(res + "mainmenu\\playpressed.png"));
        MainMenuOptions = new Texture(Gdx.files.internal(res + "mainmenu\\options.png"));
        MainMenuOptionsPressed = new Texture(Gdx.files.internal(res + "mainmenu\\optionspressed.png"));
        MainMenuExit = new Texture(Gdx.files.internal(res + "mainmenu\\exit.png"));
        MainMenuExitPressed = new Texture(Gdx.files.internal(res + "mainmenu\\exitpressed.png"));
    }
    public static void dispose_mainmenu()
    {
        MainMenuTitle.dispose();
        MainMenuPlay.dispose();
        MainMenuPlayPressed.dispose();
        MainMenuOptions.dispose();
        MainMenuOptionsPressed.dispose();
        MainMenuExit.dispose();
        MainMenuExitPressed.dispose();
    }

    public static void load_options()
    {
        if (Options.highres) res = "highres\\";
        else res = "stdres\\";

        OptionsMenu = new Texture(Gdx.files.internal(res + "options\\options.png"));
        OptionsMenuSlider = new Texture(Gdx.files.internal(res + "options\\cuadrado.png"));
        OptionsMenuSliderPressed = new Texture(Gdx.files.internal(res + "options\\cuadradooscuro.png"));
        OptionsMenuCheckMark = new Texture(Gdx.files.internal(res + "options\\checkmark.png"));
        OptionsMenuReturnPressed = new Texture(Gdx.files.internal(res + "options\\returnoscuro.png"));
        OptionsMenuCreditsPressed = new Texture(Gdx.files.internal(res + "options\\creditsoscuro.png"));
    }
    public static void dispose_options()
    {
        OptionsMenu.dispose();
        OptionsMenuSlider.dispose();
        OptionsMenuSliderPressed.dispose();
        OptionsMenuCheckMark.dispose();
        OptionsMenuReturnPressed.dispose();
        OptionsMenuCreditsPressed.dispose();
    }

    public static void load_gamescreen()
    {
        if (Options.highres) res = "highres\\";
        else res = "stdres\\";

        GameBackground = new Texture(Gdx.files.internal(res + "gamescreen\\gbkgnd.png"));

        HUD = new Texture(Gdx.files.internal(res + "gamescreen\\barrita.png"));
        HUDMute = new Texture(Gdx.files.internal(res + "gamescreen\\mute.png"));
        HUDUnmute = new Texture(Gdx.files.internal(res + "gamescreen\\unmute.png"));
        HUDlives = new Texture(Gdx.files.internal(res + "gamescreen\\lives.png"));
        PauseMenu = new Texture(Gdx.files.internal(res + "gamescreen\\pause.png"));
        ShowConfirmExit = new Texture(Gdx.files.internal(res + "gamescreen\\exitconfirm.png"));
        ShowConfirmRestart = new Texture(Gdx.files.internal(res + "gamescreen\\restartconfirm.png"));
        Victory = new Texture(Gdx.files.internal(res + "gamescreen\\victory.png"));
        Defeat = new Texture(Gdx.files.internal(res + "gamescreen\\defeat.png"));

        duck = new Texture(Gdx.files.internal(res + "gamescreen\\duck.png"));
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
        swimUpAnim = new Animation(.2f, Assets.duckSwimUpFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimDownAnim = new Animation(.2f, Assets.duckSwimDownFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimSideRightAnim = new Animation(.2f, Assets.duckSwimSideRightFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimSideLeftAnim = new Animation(.2f, Assets.duckSwimSideLeftFrames, Animation.PlayMode.LOOP_PINGPONG);
        padAnim = new Animation(.2f, Assets.duckPadFrames, Animation.PlayMode.LOOP);
        eatenAnim = new Animation(.2f, Assets.duckEatenFrames, Animation.PlayMode.NORMAL);

        lily = new Texture(Gdx.files.internal(res + "gamescreen\\lily.png"));
        lilyframes = TextureRegion.split(lily, Options.spriteWidth,Options.spriteHeight);
        lilyRot = new TextureRegion[] {lilyframes[0][0], lilyframes[1][0], lilyframes[2][0]};
        lilyRotFrames = new Array<TextureRegion>(lilyRot);
        padRot = new Animation(.2f, Assets.lilyRotFrames, Animation.PlayMode.LOOP_PINGPONG);

        shark = new Texture(Gdx.files.internal(res + "gamescreen\\shark.png"));
        sharkframes = TextureRegion.split(shark, Options.spriteWidth,Options.spriteHeight);
        sharkSwimLeft = new TextureRegion[] {sharkframes[0][0], sharkframes[1][0]};
        sharkSwimUp = new TextureRegion[] {sharkframes[0][3], sharkframes[1][3]};
        sharkSwimDown = new TextureRegion[] {sharkframes[0][2], sharkframes[1][2]};
        sharkEat = new TextureRegion[] {sharkframes[0][1], sharkframes[1][1], sharkframes[2][1]};
        sharkSwimLeftFrames = new Array<TextureRegion>(sharkSwimLeft);
        sharkSwimRightFrames = new Array<TextureRegion>();
        for (TextureRegion t:sharkSwimLeftFrames)
        {
            TextureRegion temp = new TextureRegion(t);
            temp.flip(true,false);
            sharkSwimRightFrames.add(temp);
        }
        sharkSwimUpFrames = new Array<TextureRegion>(sharkSwimUp);
        sharkSwimDownFrames = new Array<TextureRegion>(sharkSwimDown);
        sharkEatFrames = new Array<TextureRegion>(sharkEat);
        sharkSwimLeftAnim = new Animation(.2f, sharkSwimLeftFrames, Animation.PlayMode.LOOP);
        sharkSwimRightAnim = new Animation(.2f, sharkSwimRightFrames, Animation.PlayMode.LOOP);
        sharkSwimUpAnim = new Animation(.2f, sharkSwimUpFrames, Animation.PlayMode.LOOP);
        sharkSwimDownAnim = new Animation(.2f, sharkSwimDownFrames, Animation.PlayMode.LOOP);
        sharkEatAnim = new Animation(.2f, Assets.sharkEatFrames, Animation.PlayMode.NORMAL);

    }
    public static void dispose_gamescreen()
    {
        GameBackground.dispose();
        HUD.dispose();
        HUDMute.dispose();
        HUDUnmute.dispose();
        HUDlives.dispose();
        PauseMenu.dispose();
        ShowConfirmExit.dispose();
        ShowConfirmRestart.dispose();
        Victory.dispose();
        Defeat.dispose();

        duck.dispose();
        lily.dispose();
        shark.dispose();
        //not sure what happens to the texture regions... dont touch them after this.
    }

    public static void load_levelscreen()
    {
        if (Options.highres) res = "highres\\";
        else res = "stdres\\";

        LevelSelectionWorldMaker = new Texture(Gdx.files.internal(res + "levelselection\\worldmakergreen.png"));
        LevelSelectionCustomWorld = new Texture(Gdx.files.internal(res + "levelselection\\custombutt.png"));
        LevelSelectionGetMore = new Texture(Gdx.files.internal(res + "levelselection\\getmore.png"));
        LevelSelectionMainMenu = new Texture(Gdx.files.internal(res + "levelselection\\mainbuttgreen.png"));


    }
    public static void dispose_levelscreen()
    {
        LevelSelectionWorldMaker.dispose();
        LevelSelectionCustomWorld.dispose();
        LevelSelectionGetMore.dispose();
        LevelSelectionMainMenu.dispose();
    }

    public static void load_navigation()
    {
        if (!Options.isHighres()) load_navigation_std();
        else load_navigation_high();
    }
    public static void load_navigation_std()
    {
        NavigationCancel = new Texture(Gdx.files.internal("stdres\\navigation\\cancel.png"));
        NavigationConfirm = new Texture(Gdx.files.internal("stdres\\navigation\\confirm.png"));
        NavigationFlechaIzq = new Texture(Gdx.files.internal("stdres\\navigation\\flechaizq.png"));
        NavigationFlechaDer = new TextureRegion(NavigationFlechaIzq);
        NavigationFlechaDer.flip(true, false);
        NavigationUpone = new Texture(Gdx.files.internal("stdres\\navigation\\upone.png"));
        NavigationWorldButt = new Texture(Gdx.files.internal("stdres\\navigation\\levelbutt.png"));
    }
    public static void load_navigation_high()
    {
        NavigationCancel = new Texture(Gdx.files.internal("highres\\navigation\\cancel.png"));
        NavigationConfirm = new Texture(Gdx.files.internal("highres\\navigation\\confirm.png"));
        NavigationFlechaIzq = new Texture(Gdx.files.internal("highres\\navigation\\flechaizq.png"));
        NavigationFlechaDer = new TextureRegion(NavigationFlechaIzq);
        NavigationFlechaDer.flip(true, false);
        NavigationUpone = new Texture(Gdx.files.internal("highres\\navigation\\upone.png"));
        NavigationWorldButt = new Texture(Gdx.files.internal("highres\\navigation\\levelbutt.png"));
    }
    public static void dispose_navigation()
    {
        NavigationFlechaIzq.dispose();
        NavigationCancel.dispose();
        NavigationConfirm.dispose();
        NavigationUpone.dispose();
        NavigationWorldButt.dispose();
    }
    
    public static void load_font()
    {
        if (Options.highres) res = "highres\\";
        else res = "stdres\\";

        font = new BitmapFont(Gdx.files.internal(res + "font\\lois.fnt"));
    }
    public static void dispose_font()
    {
        font.dispose();
    }
    public static void load_leveledit()
    {
        res = "stdres\\";

        load_font();
        LevelEditMapaAbajo = new Texture(Gdx.files.internal("leveledit\\mapaabajo.png"));
        LevelEditClock = new Texture(Gdx.files.internal("leveledit\\RELOJ.png"));
        LevelEditConfirm = new Texture(Gdx.files.internal("leveledit\\CONFIRM.png"));
        LevelEditDuck = new Texture(Gdx.files.internal("leveledit\\duck.png"));
        LevelEditExit = new Texture(Gdx.files.internal("leveledit\\EXIT.png"));
        LevelEditLily = new Texture(Gdx.files.internal("leveledit\\lily.png"));
        LevelEditLOAD = new Texture(Gdx.files.internal("leveledit\\LOAD.png"));
        LevelEditLives = new Texture(Gdx.files.internal("leveledit\\lives.png"));
        LevelEditUnlives = new Texture(Gdx.files.internal("leveledit\\unlives.png"));
        LevelEditRemoveItem = new Texture(Gdx.files.internal("leveledit\\REMOVEITEM.png"));
        LevelEditSave = new Texture(Gdx.files.internal("leveledit\\SAVE.png"));
        LevelEditShark = new Texture(Gdx.files.internal("leveledit\\shark.png"));
        LevelEditTimeBar = new Texture(Gdx.files.internal("leveledit\\timebar.png"));
        LevelEditFlechaIzq = new Texture(Gdx.files.internal("leveledit\\flechaizq.png"));
        LevelEditFlechaDer = new TextureRegion(LevelEditFlechaIzq);
        LevelEditFlechaDer.flip(true, false);

        GameBackground = new Texture(Gdx.files.internal("leveledit\\gbkgnd.png"));

        duck = new Texture(Gdx.files.internal(res + "gamescreen\\duck.png"));
        duckframes = TextureRegion.split(duck, DuckPondGame.stdspriteW,DuckPondGame.stdspriteH);
        duckSwimUp = new TextureRegion[] {duckframes[0][0], duckframes[1][0], duckframes[2][0]};
        duckSwimDown = new TextureRegion[] {duckframes[0][2], duckframes[1][2], duckframes[2][2]};
        duckSwimSideRight = new TextureRegion[] {duckframes[0][1], duckframes[1][1], duckframes[2][1]};
        //duckSwimSideLeft = new TextureRegion[] {duckframes[0][1], duckframes[1][1], duckframes[2][1]};
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
        swimUpAnim = new Animation(.2f, Assets.duckSwimUpFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimDownAnim = new Animation(.2f, Assets.duckSwimDownFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimSideRightAnim = new Animation(.2f, Assets.duckSwimSideRightFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimSideLeftAnim = new Animation(.2f, Assets.duckSwimSideLeftFrames, Animation.PlayMode.LOOP_PINGPONG);

        shark = new Texture(Gdx.files.internal(res + "gamescreen\\shark.png"));
        sharkframes = TextureRegion.split(shark, DuckPondGame.stdspriteW,DuckPondGame.stdspriteH);
        sharkSwimLeft = new TextureRegion[] {sharkframes[0][0], sharkframes[1][0]};
        sharkSwimUp = new TextureRegion[] {sharkframes[0][3], sharkframes[1][3]};
        sharkSwimDown = new TextureRegion[] {sharkframes[0][2], sharkframes[1][2]};
        sharkSwimLeftFrames = new Array<TextureRegion>(sharkSwimLeft);
        sharkSwimRightFrames = new Array<TextureRegion>();
        for (TextureRegion t:sharkSwimLeftFrames)
        {
            TextureRegion temp = new TextureRegion(t);
            temp.flip(true,false);
            sharkSwimRightFrames.add(temp);
        }
        sharkSwimUpFrames = new Array<TextureRegion>(sharkSwimUp);
        sharkSwimDownFrames = new Array<TextureRegion>(sharkSwimDown);
        sharkSwimLeftAnim = new Animation(.2f, sharkSwimLeftFrames, Animation.PlayMode.LOOP);
        sharkSwimRightAnim = new Animation(.2f, sharkSwimRightFrames, Animation.PlayMode.LOOP);
        sharkSwimUpAnim = new Animation(.2f, sharkSwimUpFrames, Animation.PlayMode.LOOP);
        sharkSwimDownAnim = new Animation(.2f, sharkSwimDownFrames, Animation.PlayMode.LOOP);

        lily = new Texture(Gdx.files.internal(res + "gamescreen\\lily.png"));
        lilyframes = TextureRegion.split(lily, DuckPondGame.stdspriteW,DuckPondGame.stdspriteH);
        lilyRot = new TextureRegion[] {lilyframes[0][0], lilyframes[1][0], lilyframes[2][0]};
        lilyRotFrames = new Array<TextureRegion>(lilyRot);
        padRot = new Animation(.2f, Assets.lilyRotFrames, Animation.PlayMode.LOOP_PINGPONG);
    }
    public static void dispose_leveledit()
    {
        GameBackground.dispose();
        LevelEditMapaAbajo.dispose();
        LevelEditClock.dispose();
        LevelEditConfirm.dispose();
        LevelEditDuck.dispose();
        LevelEditExit.dispose();
        LevelEditLily.dispose();
        LevelEditLOAD.dispose();
        LevelEditLives.dispose();
        LevelEditUnlives.dispose();
        LevelEditRemoveItem.dispose();
        LevelEditSave.dispose();
        LevelEditShark.dispose();
        LevelEditTimeBar.dispose();
        LevelEditFlechaIzq.dispose();
        duck.dispose();
        shark.dispose();
        lily.dispose();
    }
}
