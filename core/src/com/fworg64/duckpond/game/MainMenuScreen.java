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



/**
 * This class handles the main menu, also it should handle the level selection menu and the options menu
 * and launch the level editor and from the level selection menu launch the game
 *
 * the reason to keep all these menus in one file is so that the ads served on the first menu can be carried
 * over to level selection and options (technically they will all be the same screen)
 *
 *
 * Created by fworg on 2/4/2016.
 */
public class MainMenuScreen extends ScreenAdapter
{
    final static int PLAY_X = (int)(.309f* DuckPondGame.worldW);
    final static int PLAY_Y = (int)(.41f* DuckPondGame.worldH);
    final static int LEVEL_X = (int)(.1f* DuckPondGame.worldW);
    final static int LEVEL_Y = (int)(.253f* DuckPondGame.worldH);
    final static int OPTIONS_X = (int)(.5f* DuckPondGame.worldW);
    final static int OPTIONS_Y = (int)(.253f* DuckPondGame.worldH);
    final static int EXIT_X = (int)(.309f* DuckPondGame.worldW);
    final static int EXIT_Y = (int)(.134f* DuckPondGame.worldH);
    final static int BUTT_W = (int)(.35f*DuckPondGame.worldW);
    final static int BUTT_H = (int)(.09f*DuckPondGame.worldH);

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Rectangle playbutt; //more buttons later
    Rectangle optionbutt;
    Rectangle exitbutt;
    Rectangle leveleditbutt;

    public MainMenuScreen (DuckPondGame game)
    {
        this.game = game;
        Assets.load();
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        playbutt = new Rectangle(PLAY_X, PLAY_Y, BUTT_W, BUTT_H);
        optionbutt = new Rectangle(OPTIONS_X, OPTIONS_Y, BUTT_W, BUTT_H);
        leveleditbutt = new Rectangle(LEVEL_X, LEVEL_Y, BUTT_W, BUTT_H);
        exitbutt = new Rectangle(EXIT_X, EXIT_Y, BUTT_W, BUTT_H);

        in = new InputListener();
        touchpoint = new Vector2();

    }

    public int update() //FYOTB
    {
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched()) Gdx.app.debug("TOCUH", touchpoint.toString());

        if (playbutt.contains(touchpoint) && in.justTouched())
        {
            game.debug = "play pressed";
            game.setScreen(new LevelSelectionScreen(game));
            return 2;
        }
        if (leveleditbutt.contains(touchpoint) && in.justTouched())
        {
            game.setScreen(new LevelScreen2(game));
            return 1;
        }
        if (optionbutt.contains(touchpoint) && in.justTouched())
        {
            game.setScreen(new OptionsScreen(game));
            return 1;
        }
        if (exitbutt.contains(touchpoint) && in.justTouched())
        {
            Gdx.app.exit();
        }
        return 0;
    }

    public void draw() //fyotb
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(Assets.MainMenuBackground, 0, 0);
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(playbutt.getX(), playbutt.getY(), playbutt.getWidth(), playbutt.getHeight());
        shapeRenderer.rect(leveleditbutt.getX(), leveleditbutt.getY(), leveleditbutt.getWidth(), leveleditbutt.getHeight());
        shapeRenderer.rect(optionbutt.getX(), optionbutt.getY(), optionbutt.getWidth(), optionbutt.getHeight());
        shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
        shapeRenderer.end();

    }

    @Override
    public void render (float delta)
    {
        switch (update()) //because we can.
        {
            case 0:
                break;
            case 1:
                this.dispose();
                break;
            case 2:
                this.dispose();
                break;
        }

        draw();
    }
}
