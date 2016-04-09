package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 3/6/2016.
 */
public class LevelSelectionScreen extends ScreenAdapter
{
    public int LEVEL_LOAD_X ;
    public int LEVEL_LOAD_Y;
    public int LEVEL_LOAD_W ;
    public int LEVEL_LOAD_H ;
    public int LEVEL_LOAD_XS;
    public int LEVEL_LOAD_YS;
    public final static int LEVEL_LOAD_R =3;
    public final static int LEVEL_LOAD_C =2;

    public int UP_ONE_X;
    public int UP_ONE_Y;
    public int UP_ONE_W;
    public int UP_ONE_H;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    FileHandle levelDir;
    FileHandle[] levels;

    Rectangle[] levelbutts;
    Rectangle customlevelbutt;
    Rectangle upone;

    Rectangle mainMenubutt;

    public LevelSelectionScreen (DuckPondGame game)
    {
        if (Options.highres)
        {
            LEVEL_LOAD_X = 200;
            LEVEL_LOAD_Y = 1920 -550;
            LEVEL_LOAD_W = 200;
            LEVEL_LOAD_H = 200;
            LEVEL_LOAD_XS = 300;
            LEVEL_LOAD_YS = 300;

            UP_ONE_X = 700;
            UP_ONE_Y = 1920 - 1500;
            UP_ONE_W = 200;
            UP_ONE_H = 200;
        }
        else
        {
            LEVEL_LOAD_X = 100;
            LEVEL_LOAD_Y = 960 -250;
            LEVEL_LOAD_W = 100;
            LEVEL_LOAD_H = 100;
            LEVEL_LOAD_XS = 150;
            LEVEL_LOAD_YS = 150;
            UP_ONE_X = 400;
            UP_ONE_Y = 960 - 700;
            UP_ONE_W = 100;
            UP_ONE_H = 100;
        }

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        game.mas.playMainMenu();

        levelDir = Gdx.files.internal("LEVELS\\");
        levels = levelDir.list();

        mainMenubutt = new Rectangle(10f/640f * Options.screenWidth, 800f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        customlevelbutt = new Rectangle(100f/640f * Options.screenWidth, 150f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        levelbutts = new Rectangle[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
               levelbutts[i*(LEVEL_LOAD_R) + j] = new Rectangle(LEVEL_LOAD_X + i*LEVEL_LOAD_XS, LEVEL_LOAD_Y - j*LEVEL_LOAD_YS, LEVEL_LOAD_W, LEVEL_LOAD_H);
            }
        }
        upone = new Rectangle(UP_ONE_X, UP_ONE_Y, UP_ONE_W, UP_ONE_H);

        if (Gdx.app.getType() == Application.ApplicationType.Android) this.game.adStateListener.ShowBannerAd();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for(int i=0; i<levelbutts.length;i++)
        {
            if (in.justTouched() && levelbutts[i].contains(touchpoint))
            {
                if (levels.length > i)
                {
                    if (!levels[i].isDirectory()) game.setScreen(new GameScreen(game, levels[i].readString()));
                    else
                    {
                        levelDir = levels[i];
                        levels = levelDir.list();
                        break;
                    }
                }
            }
        }
        if (in.justTouched() && upone.contains(touchpoint))
        {
            if (!levelDir.name().equals("LEVELS"))
            {
                levelDir = levelDir.parent();
                levels = levelDir.list();
            }
        }
        if (in.justTouched() && customlevelbutt.contains(touchpoint))
        {
            game.setScreen(new GameScreen(game, Options.getCustom1()));
        }
        if ((in.justTouched() && mainMenubutt.contains(touchpoint)) || in.isBackPressed())
        {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);
        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(Assets.LevelSelectionBackground, 0, 0);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        Assets.font.draw(game.batch, "Return to MainMenu", mainMenubutt.getX(), mainMenubutt.getY());
        Assets.font.draw(game.batch, "Custom Level", customlevelbutt.getX(), customlevelbutt.getY());
        for (int i=0; i< (levelbutts.length<levels.length ? levelbutts.length : levels.length); i++) Assets.font.draw(game.batch, levels[i].nameWithoutExtension(), levelbutts[i].getX(), levelbutts[i].getY());
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for(Rectangle r: levelbutts) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(customlevelbutt.getX(), customlevelbutt.getY(), customlevelbutt.getWidth(), customlevelbutt.getHeight());
        shapeRenderer.rect(mainMenubutt.getX(), mainMenubutt.getY(), mainMenubutt.getWidth(), mainMenubutt.getHeight());
        shapeRenderer.rect(upone.getX(), upone.getY(), upone.getWidth(), upone.getHeight());
        shapeRenderer.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
