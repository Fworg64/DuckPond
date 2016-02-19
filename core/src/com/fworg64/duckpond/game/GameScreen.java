package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * Created by fworg on 2/5/2016.
 */
public class GameScreen extends ScreenAdapter
{
    DuckPondGame game;
    OrthographicCamera gcam;

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

    private Rectangle resetbutt;


    GameScreen(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera

        in = new InputListener();

        touchpoint = new Vector2(); //input vector3, 3 for compatibilliyt
        clock =0;

        listener = new World.WorldListener() {}; //implement later...

        world = new World(listener);
        world.LoadLevel();

        renderer = new WorldRenderer(game.batch, world);


        beingswiped = false;
        swiperegistered = false;
        swipestart = new Vector2();
        swipeend = new Vector2();
        swipedebug = "herp";

        resetbutt = new Rectangle(0, .9f*DuckPondGame.worldH, .1f*DuckPondGame.worldW, DuckPondGame.worldH);

    }

    public void update(float delta)
    {
        clock+=delta; //keep track of time

        if (in.justTouched() && beingswiped ==false) //swipe is starting
        {
            touchpoint.set(in.getTouchpoint());

            //register swipe
            beingswiped = true;
            swipestart.set(touchpoint.x, touchpoint.y);
            swipedebug = "TOCUH";
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
            swipedebug = "NO TOCUH";
        }

        if (swiperegistered)
        {
            world.update(delta, swipestart, swipeend);
            swiperegistered = false;
            Gdx.app.debug("Swipe Registered",swipestart.toString() + '\n'+swipeend.toString());
        }
        else world.update(delta,swipestart, swipestart.cpy()); //probably a better way to implement this

        if (resetbutt.contains(touchpoint)) world.LoadLevel(); //not really a reset, but ya know



    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        renderer.render(clock);
    }

    @Override
    public void render (float delta)
    {
        update(delta);
        draw();
    }
}
