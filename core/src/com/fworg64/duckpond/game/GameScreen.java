package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
    Menus menu;

    private Rectangle HUDarea;
    private Rectangle pausebutt;
    private Rectangle unpausebutt;
    //private Rectangle resetbutt;


    GameScreen(DuckPondGame game)
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
            }

            @Override
            public void gameOverLose()
            {
                isPaused = true;
                menu = Menus.GMLOSE;
            }
        }; //implement later... later is now! 2/21

        isPaused = false;
        menu = Menus.PLAYING;

        world = new World(listener);
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
        unpausebutt = new Rectangle(115f/640f *Options.screenWidth, 90f/960f * Options.screenHeight, 415f/640f * Options.screenWidth, 120f/915f *Options.screenHeight);

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
            if (pausebutt.contains(in.getTouchpoint()))
            {
                isPaused = true;
                menu = Menus.PAUSEMENU;
                Gdx.app.debug("paused","button pressed");
            }
        }
        else
        {
            switch (menu)
            {
                case PAUSEMENU:
                    if (unpausebutt.contains(in.getTouchpoint()))
                    {
                        isPaused = false;
                        menu = Menus.PLAYING;
                    }
                    break;
                case GMLOSE:
                    break;
                case GMVICTORY:
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

        if (isPaused == false)
        {
            renderer.render(clock);

            game.batch.begin();
            game.batch.draw(Assets.HUD, HUDarea.getX(), HUDarea.getY(), HUDarea.getWidth(), HUDarea.getHeight());
            //draw other HUD shtuf
            game.batch.end();
        }
        else
        {
            game.batch.enableBlending();
            game.batch.begin();
            Gdx.app.debug("Oh", "God");
            switch (menu)
            {
                case PAUSEMENU:
                    game.batch.draw(Assets.PauseMenu, 0,0,Options.screenWidth, Options.screenHeight);
                    break;
                case GMLOSE:
                    break;
                case GMVICTORY:
                    break;
            }
            game.batch.end();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(pausebutt.getX(), pausebutt.getY(), pausebutt.getWidth(), pausebutt.getHeight());
        shapeRenderer.rect(unpausebutt.getX(), unpausebutt.getY(), unpausebutt.getWidth(), unpausebutt.getHeight());
        shapeRenderer.end();

    }

    @Override
    public void render (float delta)
    {
        update(delta);
        draw();
    }
}
