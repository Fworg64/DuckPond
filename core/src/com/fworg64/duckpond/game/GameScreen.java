package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public static Texture gbkgnd;
    public static TextureRegion gbkgndRegion;

    public static Texture actors; //asstes plux
    public static TextureRegion ducktex;

    public Duck fred;


    GameScreen(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera

        touchpoint = new Vector3(); //input vector3, 3 for compatibilliyt
        clock =0;

        gbkgnd = new Texture(Gdx.files.internal("gbkgnd.png")); //assets file
        gbkgndRegion = new TextureRegion(gbkgnd); //not sure how neceesrayyyry this is if its the whole image

        actors = new Texture(Gdx.files.internal("actors.png")); //have we mentioned assets file yet?
        ducktex = new TextureRegion(actors,96,96);

        fred = new Duck();

    }

    public void update(float delta)
    {
        clock+=delta; //keep track of time
        game.debug = String.format("%.5f", delta);

        fred.update(delta);

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
        game.batch.draw(gbkgnd, 0, 0, 320, 480);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(ducktex, fred.pos.getX(), fred.pos.getY());
        game.batch.end();

        //debug text
        game.batch.begin();
        game.font.draw(game.batch, game.debug, 50, 450);
        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update(delta);
        draw();
    }
}
