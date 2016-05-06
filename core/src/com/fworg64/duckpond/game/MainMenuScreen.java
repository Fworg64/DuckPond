package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
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
    int PLAY_X;
    int PLAY_Y;
    int PLAY_W;
    int PLAY_H;
    int OPTIONS_X;
    int OPTIONS_Y;
    int OPTIONS_W;
    int OPTIONS_H;
    int EXIT_X;
    int EXIT_Y;
    int EXIT_W;
    int EXIT_H;
    int TITTLE_X;
    int TITTLE_Y;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;
    boolean catchOtherBack;

    Rectangle playbutt;
    Rectangle optionbutt;
    Rectangle exitbutt;

    boolean playPressed;
    boolean optionsPressed;
    boolean exitPressed;

    public MainMenuScreen (DuckPondGame game)
    {
        if (Options.highres)
        {
            PLAY_X = (int)(220f/1080f* Options.screenWidth);
            PLAY_Y = (int)((1- 1226f/1920f)* Options.screenHeight);
            OPTIONS_X = (int)(80f/1080f* Options.screenWidth);
            OPTIONS_Y = (int)((1- 1665f/1920f)* Options.screenHeight);
            EXIT_X = (int)(820/1080f* Options.screenWidth);
            EXIT_Y = (int)((1- 1692f/1920f)* Options.screenHeight);
            TITTLE_X = (int)(60f/1080f * Options.screenWidth);
            TITTLE_Y = (int)((1- 882f/1920f) * Options.screenHeight);
        }
        else
        {
            PLAY_X = (int)(130f/640f* Options.screenWidth);
            PLAY_Y = (int)((1- 684f/960f)* Options.screenHeight);
            OPTIONS_X = (int)(50f/640f* Options.screenWidth);
            OPTIONS_Y = (int)((1-875f/960f)* Options.screenHeight);
            EXIT_X = (int)(478f/640f* Options.screenWidth);
            EXIT_Y = (int)((1-888f/960f)* Options.screenHeight);
            TITTLE_X = (int)(60f/640f * Options.screenWidth);
            TITTLE_Y = (int)((1- 500f/960f) * Options.screenHeight);
        }

        PLAY_W = (int)(380f/640f * Options.screenWidth);
        PLAY_H = (int)(114f/960f * Options.screenHeight);
        OPTIONS_W = (int)(154f/640f * Options.screenWidth);
        OPTIONS_H = (int)(63f/960f * Options.screenHeight);
        EXIT_W = (int)(91f/640f * Options.screenWidth);
        EXIT_H = (int)(62f/960f * Options.screenHeight);



        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        playbutt = new Rectangle(PLAY_X, PLAY_Y, PLAY_W, PLAY_H);
        optionbutt = new Rectangle(OPTIONS_X, OPTIONS_Y, OPTIONS_W, OPTIONS_H);
        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);

        playPressed = false;
        optionsPressed = false;
        exitPressed = false;

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();
        if (in.isBackPressed()) catchOtherBack = true;
        else catchOtherBack = false;

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            Gdx.app.debug("Ad","SHOW, MAINMENU");
            this.game.adStateListener.ShowBannerAd();
        }
    }

    public int update() //FYOTB
    {
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched()) Gdx.app.debug("TOCUH", touchpoint.toString());

        if (playbutt.contains(touchpoint) && in.justTouched()) {
            playPressed = true;
            Assets.load_levelscreen();
        }
        if (playPressed && !playbutt.contains(touchpoint)) playPressed = false;
        if (playPressed && !in.isTouched())
        {
            game.setScreen(new LevelSelectionScreen(game));
            Assets.dispose_mainmenu();
            return 2;
        }
        if (optionbutt.contains(touchpoint) && in.justTouched()) {
            optionsPressed = true;
            Assets.load_options();
        }
        if (optionsPressed && !optionbutt.contains(touchpoint)) optionsPressed = false;
        if (optionsPressed && !in.isTouched())
        {
            game.setScreen(new OptionsScreen(game));
            Assets.dispose_mainmenu();
            return 1;
        }
        if (exitbutt.contains(touchpoint) && in.justTouched()) exitPressed = true;
        if (exitPressed && !exitbutt.contains(touchpoint)) exitPressed = false;
        if (exitPressed && !in.isTouched())
        {
            Gdx.app.exit();
        }
        if (in.isBackPressed())
        {
           if (!catchOtherBack) Gdx.app.exit();
        }
        if (!in.isBackPressed()) catchOtherBack = false;
        return 0;
    }

    public void draw() //fyotb
    {
        //background 46b4d6: .27451, .70588, .83922
        GL20 gl = Gdx.gl;
        gl.glClearColor(DuckPondGame.DuckPondBlue.r,DuckPondGame.DuckPondBlue.g,DuckPondGame.DuckPondBlue.b,DuckPondGame.DuckPondBlue.a);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.MainMenuTitle, TITTLE_X, TITTLE_Y);
        game.batch.draw(Assets.MainMenuPlay, playbutt.getX(), playbutt.getY());
        game.batch.draw(Assets.MainMenuOptions, optionbutt.getX(), optionbutt.getY());
        game.batch.draw(Assets.MainMenuExit, exitbutt.getX(), exitbutt.getY());

        if (playPressed) {
            game.batch.setColor(DuckPondGame.BlueButtTit);
            game.batch.draw(Assets.MainMenuPlay, playbutt.getX(), playbutt.getY());
        }
        if (optionsPressed)
        {
            game.batch.setColor(DuckPondGame.GreenButtTit);
            game.batch.draw(Assets.MainMenuOptions, optionbutt.getX(), optionbutt.getY());
        }
        if (exitPressed) {
            game.batch.setColor(DuckPondGame.GreenButtTit);
            game.batch.draw(Assets.MainMenuExit, exitbutt.getX(), exitbutt.getY());
        }
        game.batch.setColor(1,1,1,1);

        Assets.font.draw(game.batch, DuckPondGame.version, 0, 100);
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(playbutt.getX(), playbutt.getY(), playbutt.getWidth(), playbutt.getHeight());
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
