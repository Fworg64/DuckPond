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
 * This class handles the main menu, also it should handle the level selection menu and the options menu
 * and launch the level editor and from the level selection menu launch the game
 *
 * the reason to keep all these menus in one file is so that the ads served on the first menu can be carried
 * over to level selection and options (technically they will all be the same screen)
 *
 * Created by fworg on 2/4/2016.
 */
public class MainMenuScreen extends ScreenAdapter
{
    final static int MENU_X = 30;
    final static int MENU_Y = 370;
    final static int MENU_SPACE = 100;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera

    Rectangle playbutt; //more buttons later
    Rectangle optionbutt;
    Rectangle exitbutt;
    Rectangle leveleditbutt;

    Vector3 touchpoint; //input vector

    String touchpointstr;
    String buttbounds;

    public MainMenuScreen (DuckPondGame game) //fuck your one true brace
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera

        playbutt = new Rectangle(MENU_X, MENU_Y, 193, 80); //bounds for button
        leveleditbutt = new Rectangle(MENU_X, MENU_Y - MENU_SPACE, 193,80);
        optionbutt = new Rectangle(MENU_X, MENU_Y - 2*MENU_SPACE, 193,80);
        exitbutt = new Rectangle(MENU_X, MENU_Y - 3*MENU_SPACE, 193,80);

        touchpoint = new Vector3(); //input vector3, 3 for compatibilliyt

        touchpointstr ="";
        buttbounds =String.format("%.1f, %.1f", playbutt.getX(), playbutt.getY());

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
        game.batch.draw(Assets.MainMenuBackground, 0, 0, 320, 480);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.StartButt, playbutt.getX(), playbutt.getY());
        game.batch.draw(Assets.LevelEditButt, leveleditbutt.getX(), leveleditbutt.getY());
        game.batch.draw(Assets.OptionsButt, optionbutt.getX(), optionbutt.getY());
        game.batch.draw(Assets.ExitButt, exitbutt.getX(), exitbutt.getY());
        game.batch.end();

        //debug text
        game.batch.begin();
        Assets.font.draw(game.batch, game.debug, 50, 450);
        Assets.font.draw(game.batch, buttbounds, 50, 400);
        Assets.font.draw(game.batch, touchpointstr, 50, 350);
        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
