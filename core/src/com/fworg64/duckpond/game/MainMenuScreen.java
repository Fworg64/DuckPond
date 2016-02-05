package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;



/**
 * Created by fworg on 2/4/2016.
 */
public class MainMenuScreen extends ScreenAdapter
{
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera

    Rectangle playbutt; //more buttons later
    Vector3 touchpoint; //input vector

    public static Texture bkgnd; //put in assests file later
    public static TextureRegion bkgndRegion;

    public static Texture shit; //this too ass_sets
    public static TextureRegion play;

    String touchpointstr;
    String buttbounds;

    public MainMenuScreen (DuckPondGame game) //fuck your one true brace
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera

        playbutt = new Rectangle(50, 100, 193, 80); //bounds for buttom

        touchpoint = new Vector3(); //input vector3, 3 for compatibilliyt

        touchpointstr ="";
        buttbounds =String.format("%.1f, %.1f", playbutt.getX(), playbutt.getY());

        bkgnd = new Texture(Gdx.files.internal("bkgnd.png")); //put this shit in assets.load
        bkgndRegion = new TextureRegion(bkgnd, 0, 0, 320, 480);

        shit = new Texture(Gdx.files.internal("shit.png")); //asshole.load plx
        play = new TextureRegion(shit, 0,0,193,80);



    }

    public void update() //FYOTB
    {
        if (Gdx.input.justTouched())
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0)); //this is kinda odd
            touchpointstr = String.format("%.1f, %.1f", touchpoint.x, touchpoint.y);

            if (playbutt.contains(touchpoint.x, touchpoint.y))
            {
                game.debug = "play pressed";
                game.setScreen(new GameScreen(game));
                return;
            }

            else game.debug = "WAHT A MISS";
        }
    }

    public void draw() //fyotb
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(bkgnd, 0, 0, 320, 480);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(play, playbutt.getX(), playbutt.getY());
        game.batch.end();

        //debug text
        game.batch.begin();
        game.font.draw(game.batch, game.debug, 50, 450);
        game.font.draw(game.batch, buttbounds, 50, 400);
        game.font.draw(game.batch, touchpointstr, 50, 350);
        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
