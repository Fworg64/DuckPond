package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import javafx.stage.Screen;

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
    enum Menus {PAUSEMENU, GMVICTORY, GMLOSE, PLAYING};
    DuckPondGame game;
    OrthographicCamera gcam;

    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;
    float clock;

    public World world;
    public World.WorldListener listener;
    public WorldRenderer renderer;

    public boolean beingswiped; //swiperfile?
    public boolean swiperegistered;
    public Vector2 swipestart;
    public Vector2 swipeend;
    String swipedebug;

    private boolean isPaused;
    private boolean showConfirmRestart;
    private boolean showConfirmExit;
    Menus menu;

    private Rectangle HUDarea;
    private Rectangle pausebutt;
    private Rectangle unpausebutt;
    private Rectangle exitbutt;
    private Rectangle restartbutt;
    private Rectangle confirmYes;
    private Rectangle confirmNo;
    private Rectangle GOVnextLevel;
    private Rectangle GOVmainMenu;
    private Rectangle GOLmainMenu;
    private Rectangle GOLrestart;
    private Rectangle GOLlevelSelect;
    //private Rectangle resetbutt;


    GameScreen(DuckPondGame game, FileHandle level)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener();

        touchpoint = new Vector2(); //input vector3, 3 for compatibilliyt
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
        }; //implement later... later is now! 2/21

        isPaused = false;
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
        //swipedebug = "herp";

        //resetbutt = new Rectangle(0, .9f*DuckPondGame.worldH, .1f*DuckPondGame.worldW, DuckPondGame.worldH);
        HUDarea = new Rectangle(0, (1-.1f)*Options.screenHeight, Options.screenWidth, .1f*Options.screenHeight);
        pausebutt = new Rectangle(0,DuckPondGame.worldH -.5f*HUDarea.getHeight(),(.3125f)*DuckPondGame.worldW, .1f*DuckPondGame.worldH);
        unpausebutt = new Rectangle(115f/640f *DuckPondGame.worldW, 90f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
        restartbutt = new Rectangle(115f/640f *DuckPondGame.worldW, 350f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
        exitbutt = new Rectangle(115f/640f *DuckPondGame.worldW, 220f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH);
        confirmYes = new Rectangle(115f/640f *DuckPondGame.worldW, 300f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 80f/915f *DuckPondGame.worldH);
        confirmNo = new Rectangle(350f/640f *DuckPondGame.worldW, 300f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 80f/915f *DuckPondGame.worldH);
        GOVmainMenu = new Rectangle(50f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
        GOLmainMenu = new Rectangle(50f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
        GOLrestart = new Rectangle(400f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);

    }

    public void update(float delta)
    {
        if (isPaused ==false)
        {
            clock+=delta; //keep track of time

            if (in.justTouched() && beingswiped ==false) //swipe is starting
            {
                touchpoint.set(in.getTouchpoint());
                //register swipe
                beingswiped = true;
                swipestart.set(touchpoint.x, touchpoint.y);
            }
            else if (in.isTouched() && beingswiped ==true) //swipe in progess
            {
                touchpoint.set(in.getTouchpoint());
                swipeend.set(touchpoint.x, touchpoint.y);
            }
            else if ( !in.isTouched() && beingswiped ==true)//swipe is over
            {
                beingswiped = false;
                swiperegistered = true;
            }

            if (swiperegistered)
            {
                world.update(delta, swipestart, swipeend);
                swiperegistered = false;
                Gdx.app.debug("Swipe Registered",swipestart.toString() + '\n'+swipeend.toString());
            }
            else world.update(delta, swipestart, swipestart.cpy()); //probably a better way to implement this

            //if (resetbutt.contains(touchpoint)) world.LoadLevel(); //not really a reset, but ya know
            if (pausebutt.contains(in.getTouchpoint()) && in.justTouched())
            {
                isPaused = true;
                menu = Menus.PAUSEMENU;
            }
        }
        else
        {
            switch (menu)
            {
                case PAUSEMENU:
                    if (unpausebutt.contains(in.getTouchpoint()) && in.justTouched())
                    {
                        isPaused = false;

                        in.getTouchpoint();
                        menu = Menus.PLAYING;
                    }
                    if (exitbutt.contains(in.getTouchpoint()) && in.justTouched() && !(showConfirmRestart || showConfirmExit))
                    {
                        showConfirmExit = true;
                    }
                    if (restartbutt.contains(in.getTouchpoint()) && in.justTouched() && !(showConfirmRestart || showConfirmExit))
                    {
                        showConfirmRestart = true;
                    }
                    if (showConfirmExit == true && confirmYes.contains(in.getTouchpoint()) && in.justTouched())
                    {
                        game.setScreen(new MainMenuScreen(game));
                    }
                    if (showConfirmRestart == true && confirmYes.contains(in.getTouchpoint()) && in.justTouched())
                    {
                        world.ReloadLevel();
                        showConfirmRestart = false;
                        isPaused = false;
                        menu = Menus.PLAYING;
                    }
                    if ((showConfirmExit || showConfirmRestart) && confirmNo.contains(in.getTouchpoint())&& in.justTouched())
                    {
                        showConfirmExit = false;
                        showConfirmRestart = false;
                    }
                    break;
                case GMLOSE:
                    if (GOLmainMenu.contains(in.getTouchpoint()) && in.justTouched())
                    {
                        game.setScreen(new MainMenuScreen(game));
                    }
                    if (GOLrestart.contains(in.getTouchpoint()) && in.justTouched())
                    {
                        world.ReloadLevel();
                        menu = Menus.PLAYING;
                        isPaused = false;
                    }
                    break;
                case GMVICTORY:
                    if (GOVmainMenu.contains(in.getTouchpoint()) && in.justTouched())
                    {
                        game.setScreen(new MainMenuScreen(game));
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
        game.batch.begin();
        game.batch.draw(Assets.HUD, HUDarea.getX(), HUDarea.getY(), HUDarea.getWidth(), HUDarea.getHeight());
        //draw other HUD shtuf
        game.batch.end();

        if (isPaused)
        {
            game.batch.enableBlending();
            game.batch.begin();
            switch (menu)
            {
                case PAUSEMENU:
                    game.batch.draw(Assets.PauseMenu, 0,0,Options.screenWidth, Options.screenHeight);
                    if (showConfirmExit) game.batch.draw(Assets.ShowConfirmExit, 0, 0, Options.screenWidth, Options.screenHeight);
                    if (showConfirmRestart) game.batch.draw(Assets.ShowConfirmRestart, 0, 0, Options.screenWidth, Options.screenHeight);
                    break;
                case GMLOSE:
                    game.batch.draw(Assets.Defeat, 0,0,Options.screenWidth, Options.screenHeight);
                    break;
                case GMVICTORY:
                    game.batch.draw(Assets.Victory, 0,0,Options.screenWidth, Options.screenHeight);
                    break;
            }
            game.batch.end();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(pausebutt.getX(), pausebutt.getY(), pausebutt.getWidth(), pausebutt.getHeight());
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
            shapeRenderer.rect(GOLmainMenu.getX(), GOLmainMenu.getY(), GOLmainMenu.getWidth(), GOLmainMenu.getHeight());
            shapeRenderer.rect(GOLrestart.getX(), GOLrestart.getY(), GOLrestart.getWidth(), GOLrestart.getHeight());
        }
        if (menu == Menus.GMVICTORY)
        {
            shapeRenderer.rect(GOVmainMenu.getX(), GOVmainMenu.getY(), GOVmainMenu.getWidth(), GOVmainMenu.getHeight());
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
