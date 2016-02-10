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
 * Created by fworg on 2/5/2016.
 */
public class GameScreen extends ScreenAdapter
{
    DuckPondGame game;
    OrthographicCamera gcam;

    Vector3 touchpoint;
    float clock;

    public Duck fred;
    public Lily laura;

    public boolean beingswiped; //swiperfile?
    public Vector2 swipestart;
    public Vector2 swipeend;


    GameScreen(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera

        touchpoint = new Vector3(); //input vector3, 3 for compatibilliyt
        clock =0;

        fred = new Duck();
        laura = new Lily();
        beingswiped = false;
        swipestart = new Vector2();
        swipeend = new Vector2();

    }

    public void update(float delta)
    {
        clock+=delta; //keep track of time
        game.debug = String.format("fps =%.5f", 1/delta) + '\n' +swipestart.toString() + '\n'+ swipeend.toString()
                     + '\n' + String.format("dt = %f", fred.dtheta) + '\n'+ fred.vel.toString() + '\n'+ fred.pos.toString();

        if (Gdx.input.justTouched() && beingswiped ==false) //swipe is starting
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); //this is kinda odd

            //register swipe, check if it was on a duck
            beingswiped = true;
            swipestart.set(touchpoint.x, touchpoint.y);
        }
        else if (Gdx.input.isTouched() && beingswiped ==true) //swipe in progess
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); //this is kinda odd
            swipeend.set(touchpoint.x, touchpoint.y);
        }
        else if (Gdx.input.isTouched() == false && beingswiped ==true)//swipe is over
        {
            beingswiped = false;
            if (fred.pos.contains(swipestart)) //fred was swiped!!
            {
                fred.flick(swipeend.sub(swipestart).scl(.1f)); //flick fred by the swipe
            }
        }

        fred.update(delta);
        laura.update();


    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(Assets.GameBackground, 0, 0, 320, 480);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.lily, laura.pos.getX(), laura.pos.getY());
        game.batch.draw(Assets.duck, fred.pos.getX(), fred.pos.getY());
        game.batch.end();

        //debug text
        game.batch.begin();
        Assets.font.draw(game.batch, game.debug, 20, 460);
        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update(delta);
        draw();
    }
}
