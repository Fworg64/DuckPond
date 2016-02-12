package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    Vector3 touchpoint;
    float clock;

    public World world;
    public World.WorldListener listener;
    public WorldRenderer renderer;

    public boolean beingswiped; //swiperfile?
    public boolean swiperegistered;
    public Vector2 swipestart;
    public Vector2 swipeend;
    String swipedebug;


    GameScreen(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera

        touchpoint = new Vector3(); //input vector3, 3 for compatibilliyt
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

    }

    public void update(float delta)
    {
        clock+=delta; //keep track of time
        game.debug = String.format("fps =%.5f", 1/delta) + '\n' +swipestart.toString() + '\n'+ swipeend.toString();

        if (Gdx.input.justTouched() && beingswiped ==false) //swipe is starting
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); //this is kinda odd

            //register swipe, check if it was on a duck
            beingswiped = true;
            swipestart.set(touchpoint.x, touchpoint.y);
            swipedebug = "TOCUH";
        }
        else if (Gdx.input.isTouched() && beingswiped ==true) //swipe in progess
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); //this is kinda odd
            swipeend.set(touchpoint.x, touchpoint.y);
        }
        else if ( !Gdx.input.isTouched() && beingswiped ==true)//swipe is over
        {
            beingswiped = false;
            swiperegistered = true;
            swipedebug = "NO TOCUH";
        }

        if (swiperegistered)
        {
            world.update(delta, swipestart, swipeend);
            swiperegistered = false;
        }
        else world.update(delta,swipestart, swipestart.cpy()); //probably a better way to implement this



    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        renderer.render();

        //debug text
        game.batch.begin();
        Assets.font.draw(game.batch, game.debug, 20, 460);
        Assets.font.draw(game.batch, swipedebug, 20,340);
        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update(delta);
        draw();
    }
}
