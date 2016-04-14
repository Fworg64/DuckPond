package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * this is the actual gamescreen, no ads should go here
 * this file handles the GUI/HUD of the gameplay  and recieves input
 *
 * it is also responsible for calling World methods and WorldRenderer methods at appropiate times
 * as well as implementing sound
 *
 * while world.java handles game logic
 * and worldrenderer renders the world
 *
 * Not Resolution Aware
 * Created by fworg on 2/5/2016.
 */
public class GameScreen extends ScreenAdapter
{
    public final static float TIME_TO_RUN_AFTER_GAMEOVER_LOSE = 3.0f;
    enum Menus {PAUSEMENU, GMVICTORY, GMLOSE, PLAYING};
    DuckPondGame game;
    OrthographicCamera gcam;

    ShapeRenderer shapeRenderer;

    InputListener screenIn;
    Vector2 touchpointScreen;
    Vector2 touchpointWorld;
    float clock;

    MusicAndSounds mas;

    public World world;
    public World.WorldListener listener;
    public WorldRenderer renderer;

    public boolean beingswiped; //swiperfile?
    public boolean swiperegistered;
    public Vector2 swipestart;
    public Vector2 swipeend;
    String swipedebug;

    private boolean isPaused;
    private boolean isMuted;
    private boolean showConfirmRestart;
    private boolean showConfirmExit;
    Menus menu;

    private Rectangle HUDarea;
    private Rectangle pausebutt;
    private Rectangle livesarea;
    private Rectangle mutebutt;

    private Rectangle unpausebutt;
    private Rectangle exitbutt; //goes to level selectionscreen
    private Rectangle restartbutt;
    private Rectangle confirmYes;
    private Rectangle confirmNo;
    private Rectangle GOVLevelSelection;
    private Rectangle GOLLevelSelection;
    private Rectangle GOLrestart;

    private float gameoverRunTime;
    private boolean GAMEOVERMUSICFLAG;


    GameScreen(DuckPondGame game, String level)
    {
        this.game = game;
        this.mas = game.mas;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth * .5f, Options.screenHeight * .5f, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        screenIn = new InputListener(Options.screenWidth, Options.screenHeight);

        game.mas.stopCurrMusic();
        game.mas.playGameMusic();

        touchpointScreen = new Vector2();
        touchpointWorld = new Vector2();
        clock =0;

        listener = new World.WorldListener() //interface object?
        {
            @Override
            public void gameOverVictory()
            {
                isPaused = true;
                menu = Menus.GMVICTORY;
                Gdx.app.debug("gamestate", "VIctory!");
            }

            @Override
            public void gameOverLose()
            {
                isPaused = true;
                menu = Menus.GMLOSE;
                Gdx.app.debug("gamestate", "DEGEAT");
            }

            @Override
            public void chompNoise() {
                mas.playChomp();
            }

            @Override
            public void duckDeathNoise() {
                mas.playDucks();
            }
        }; //implement later... later is now! 2/21

        isPaused = false;
        isMuted = game.mas.isMuted;
        GAMEOVERMUSICFLAG = true;

        showConfirmRestart = false;
        showConfirmExit = false;
        menu = Menus.PLAYING;

        world = new World(listener, level);
        world.LoadLevel();

        renderer = new WorldRenderer(game.batch, world);

        beingswiped = false;
        swiperegistered = false;
        swipestart = new Vector2();
        swipeend = new Vector2();

        if (Options.highres)
        {
            HUDarea = new Rectangle(0, Options.screenHeight-Options.GUIHeight, Options.screenWidth, Options.GUIHeight);
            pausebutt = new Rectangle(0,1920-241,241, 241);
            livesarea = new Rectangle(399, Options.screenHeight-Options.GUIHeight,216, 152 );
            mutebutt = new Rectangle(305, 1920-214, 133, 158);

            unpausebutt = new Rectangle(115f/640f *DuckPondGame.highresScreenW, 350f/960f * DuckPondGame.highresScreenH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
            restartbutt = new Rectangle(115f/640f *DuckPondGame.worldW, 220f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
            exitbutt = new Rectangle(115f/640f *DuckPondGame.worldW, 90f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);

            confirmYes = new Rectangle(115f/640f *DuckPondGame.worldW, 300f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 80f/915f *DuckPondGame.worldH);
            confirmNo = new Rectangle(350f/640f *DuckPondGame.worldW, 300f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 80f/915f *DuckPondGame.worldH);

            GOVLevelSelection = new Rectangle(50f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLLevelSelection = new Rectangle(250f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLrestart = new Rectangle(40f/640f *DuckPondGame.worldW, 10f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
        }
        else
        {
            HUDarea = new Rectangle(0, Options.screenHeight-Options.GUIHeight, Options.screenWidth, Options.GUIHeight);
            pausebutt = new Rectangle(0,960-93,93, 93);
            livesarea = new Rectangle(407,Options.screenHeight-Options.GUIHeight,74 , 42 );
            mutebutt = new Rectangle(117, 960-73, 42, 48);

            unpausebutt = new Rectangle(115f/640f *DuckPondGame.worldW, 350f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
            restartbutt = new Rectangle(115f/640f *DuckPondGame.worldW, 220f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
            exitbutt = new Rectangle(115f/640f *DuckPondGame.worldW, 90f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);

            confirmYes = new Rectangle(115f/640f *DuckPondGame.worldW, 300f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 80f/915f *DuckPondGame.worldH);
            confirmNo = new Rectangle(350f/640f *DuckPondGame.worldW, 300f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 80f/915f *DuckPondGame.worldH);

            GOVLevelSelection = new Rectangle(50f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLLevelSelection = new Rectangle(250f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLrestart = new Rectangle(40f/640f *DuckPondGame.worldW, 10f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
        }

        gameoverRunTime = TIME_TO_RUN_AFTER_GAMEOVER_LOSE;
        if (Gdx.app.getType() == Application.ApplicationType.Android) this.game.adStateListener.HideBannerAd();
    }

    public void update(float delta)
    {
        if (mutebutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
        {
            isMuted = !isMuted;
            if (isMuted) game.mas.mute();
            else game.mas.unmute();
        }
        if (isPaused ==false)
        {
            clock+=delta; //keep track of time

            if (screenIn.isTouched())
            {
                touchpointScreen.set(screenIn.getTouchpoint());
                if (touchpointScreen.y < Options.screenHeight - Options.GUIHeight)
                {
                    //touched the world
                    touchpointWorld.set(touchpointScreen.x * ((float)DuckPondGame.worldW/(float)Options.screenWidth),
                                        touchpointScreen.y * ((float)DuckPondGame.worldH/((float)Options.screenHeight -(float)Options.GUIHeight)));
                    //Gdx.app.debug("ToCUH", touchpointWorld.toString());

                }
            }

            if (screenIn.justTouched() && beingswiped ==false) //swipe is starting
            {
                //register swipe
                beingswiped = true;
                swipestart.set(touchpointWorld.x, touchpointWorld.y);
                Gdx.app.debug("SWIPESTART",swipestart.toString());
            }
            else if (screenIn.isTouched() && beingswiped ==true) //swipe in progess
            {
                swipeend.set(touchpointWorld.x, touchpointWorld.y);
                if (swipeend.cpy().sub(swipestart).len() >=Options.spriteWidth*.7)
                {
                    Gdx.app.debug("SWIPECUT", swipestart.toString() + '\n' + swipeend.toString());
                    beingswiped = false;
                    swiperegistered = true;
                }
            }
            else if ( !screenIn.isTouched() && beingswiped ==true)//swipe is over
            {
                beingswiped = false;
                swiperegistered = true;
                Gdx.app.debug("SWIPEEND", swipeend.toString());
            }

            if (swiperegistered)
            {
                world.update(delta, swipestart, swipeend);
                swiperegistered = false;
                Gdx.app.debug("Swipe Registered", swipestart.toString() + '\n' + swipeend.toString() + '\n' + Float.toString(swipeend.cpy().sub(swipestart).len2()));
            } else world.update(delta, swipestart, swipestart.cpy()); //probably a better way to implement this

            if ((pausebutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched()) || screenIn.isBackPressed())
            {
                isPaused = true;
                menu = Menus.PAUSEMENU;
            }

        }
        else
        {
            //game.mas.pauseCurrMusic();
            switch (menu)
            {
                case PAUSEMENU:
                    game.mas.pauseSfx();
                    game.mas.pauseCurrMusic();
                    if (unpausebutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched() && !(showConfirmExit || showConfirmRestart))
                    {
                        isPaused = false;
                        game.mas.playGameMusic();
                        game.mas.resumeSfx();

                        screenIn.getTouchpoint();
                        menu = Menus.PLAYING;
                    }
                    if (exitbutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched() && !(showConfirmRestart || showConfirmExit))
                    {
                        showConfirmExit = true;
                    }
                    if (restartbutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched() && !(showConfirmRestart || showConfirmExit))
                    {
                        showConfirmRestart = true;
                    }
                    if (showConfirmExit == true && confirmYes.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
                    {
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        game.setScreen(new LevelSelectionScreen(game));
                    }
                    if (showConfirmRestart == true && confirmYes.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
                    {
                        world.ReloadLevel();
                        clock = 0;
                        gameoverRunTime = TIME_TO_RUN_AFTER_GAMEOVER_LOSE;
                        menu = Menus.PLAYING;
                        isPaused = false;
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        game.mas.playGameMusic();
                        showConfirmRestart = false;
                        GAMEOVERMUSICFLAG = true;
                    }
                    if ((showConfirmExit || showConfirmRestart) && confirmNo.contains(screenIn.getTouchpoint())&& screenIn.justTouched())
                    {
                        showConfirmExit = false;
                        showConfirmRestart = false;
                    }
                    break;
                case GMLOSE:
                    if (GAMEOVERMUSICFLAG)
                    {
                        if (mas.currSong == MusicAndSounds.CurrSong.GAME) mas.stopCurrMusic();
                        mas.playGameOverMusic();
                        GAMEOVERMUSICFLAG = false;
                    }

                    if (GOLLevelSelection.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
                    {
                        mas.stopCurrMusic();
                        mas.stopSfx();
                        game.setScreen(new LevelSelectionScreen(game));
                    }
                    if (GOLrestart.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
                    {
                        world.ReloadLevel();
                        clock = 0;
                        gameoverRunTime = TIME_TO_RUN_AFTER_GAMEOVER_LOSE;
                        menu = Menus.PLAYING;
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        game.mas.playGameMusic();
                        isPaused = false;
                        GAMEOVERMUSICFLAG = true;
                    }
                    if (gameoverRunTime>0)
                    {
                        clock+=delta;
                        gameoverRunTime -=delta;
                        world.update(delta, new Vector2(), new Vector2());
                    }
                    break;
                case GMVICTORY:
                    if (GAMEOVERMUSICFLAG)
                    {
                        Gdx.app.debug("MUSIC", "STOPCURR + PLAY VICT");
                        if (mas.currSong == MusicAndSounds.CurrSong.GAME) mas.stopCurrMusic();
                        mas.playVictoryMusic();
                        GAMEOVERMUSICFLAG = false;
                    }

                    if (GOVLevelSelection.contains(screenIn.getTouchpoint()) && screenIn.justTouched()) {
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        game.setScreen(new LevelSelectionScreen(game));
                    }
                    break;
            }
        }
    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        renderer.render(clock);
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.HUD, HUDarea.getX(), HUDarea.getY());
        Assets.font.draw(game.batch, Integer.toString((int) (world.time >0 ? world.time:0)), .8f*Options.screenWidth, 1.0f*Options.screenHeight);
        switch (world.lives)
        {
            case 3:
                game.batch.draw(Assets.HUDlives,livesarea.getX() + 2*livesarea.getWidth(), livesarea.getY());
            case 2:
                game.batch.draw(Assets.HUDlives,livesarea.getX() + livesarea.getWidth(), livesarea.getY());
            case 1:
                game.batch.draw(Assets.HUDlives, livesarea.getX(), livesarea.getY());
            case 0:
                break;
        }
        game.batch.draw(isMuted ? Assets.HUDMute : Assets.HUDUnmute, mutebutt.getX(), mutebutt.getY());
        //draw other HUD shtuf
        game.batch.end();

        if (isPaused)
        {
            game.batch.enableBlending();
            game.batch.begin();
            switch (menu)
            {
                case PAUSEMENU:
                    game.batch.draw(Assets.PauseMenu, 0,0);
                    if (showConfirmExit) game.batch.draw(Assets.ShowConfirmExit, 0, 0);
                    if (showConfirmRestart) game.batch.draw(Assets.ShowConfirmRestart, 0, 0);
                    break;
                case GMLOSE:
                    game.batch.draw(Assets.Defeat, 0,0);
                    break;
                case GMVICTORY:
                    game.batch.draw(Assets.Victory, 0,0);
                    break;
            }
            game.batch.end();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(pausebutt.getX(), pausebutt.getY(), pausebutt.getWidth(), pausebutt.getHeight());
        shapeRenderer.rect(HUDarea.getX(), HUDarea.getY(), HUDarea.getWidth(), HUDarea.getHeight());
        if (isPaused)
        {
            shapeRenderer.rect(unpausebutt.getX(), unpausebutt.getY(), unpausebutt.getWidth(), unpausebutt.getHeight());
            shapeRenderer.rect(restartbutt.getX(), restartbutt.getY(), restartbutt.getWidth(), restartbutt.getHeight());
            shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
        }
        if (showConfirmExit || showConfirmRestart)
        {
            shapeRenderer.rect(confirmYes.getX(), confirmYes.getY(), confirmYes.getWidth(), confirmYes.getHeight());
            shapeRenderer.rect(confirmNo.getX(), confirmNo.getY(), confirmNo.getWidth(), confirmNo.getHeight());
        }
        if (menu == Menus.GMLOSE)
        {
            shapeRenderer.rect(GOLLevelSelection.getX(), GOLLevelSelection.getY(), GOLLevelSelection.getWidth(), GOLLevelSelection.getHeight());
            shapeRenderer.rect(GOLrestart.getX(), GOLrestart.getY(), GOLrestart.getWidth(), GOLrestart.getHeight());
        }
        if (menu == Menus.GMVICTORY)
        {
            shapeRenderer.rect(GOVLevelSelection.getX(), GOVLevelSelection.getY(), GOVLevelSelection.getWidth(), GOVLevelSelection.getHeight());
        }

        shapeRenderer.end();

    }

    @Override
    public void render (float delta)
    {
        update(delta);
        draw();
    }
}
