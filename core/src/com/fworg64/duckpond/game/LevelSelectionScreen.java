package com.fworg64.duckpond.game;

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
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    FileHandle levelDir;
    FileHandle[] levels;

    Rectangle[] levelbutts;
    Rectangle customlevelbutt;

    Rectangle mainMenubutt;

    public LevelSelectionScreen (DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        levelDir = Gdx.files.internal("LEVELS\\");
        levels = levelDir.list();

        mainMenubutt = new Rectangle(10f/640f * Options.screenWidth, 800f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        customlevelbutt = new Rectangle(100f/640f * Options.screenWidth, 200f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);
        levelbutts = new Rectangle[3];
        levelbutts[0] = new Rectangle(100f/640f * Options.screenWidth, 400f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);
        levelbutts[1] = new Rectangle(300f/640f * Options.screenWidth, 400f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);
        levelbutts[2] = new Rectangle(500f/640f * Options.screenWidth, 400f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);
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
                    game.setScreen(new GameScreen(game, levels[i].readString()));
                }
            }
        }
        if (in.justTouched() && customlevelbutt.contains(touchpoint))
        {
            game.setScreen(new GameScreen(game, Options.getCustom1()));
        }
        if (in.justTouched() && mainMenubutt.contains(touchpoint))
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
        for (int i=0; i< levels.length; i++) Assets.font.draw(game.batch, levels[i].nameWithoutExtension(), levelbutts[i].getX(), levelbutts[i].getY());
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for(Rectangle r: levelbutts) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(customlevelbutt.getX(), customlevelbutt.getY(), customlevelbutt.getWidth(), customlevelbutt.getHeight());
        shapeRenderer.rect(mainMenubutt.getX(), mainMenubutt.getY(), mainMenubutt.getWidth(), mainMenubutt.getHeight());
        shapeRenderer.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
