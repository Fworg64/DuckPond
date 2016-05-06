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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fworg on 3/6/2016.
 */
public class LevelSelectionScreen extends ScreenAdapter
{
    int LEVEL_X;
    int LEVEL_Y;
    int LEVEL_W;
    int LEVEL_H;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Rectangle customlevelbutt;
    Rectangle leveleditbutt;
    Rectangle mainMenubutt;

    FileBrowser fileBrowser;

    boolean leveleditPressed;

    public LevelSelectionScreen (DuckPondGame game)
    {
        if (Options.highres)
        {
            LEVEL_X = 700;
            LEVEL_Y = 200;

        }
        else
        {
            LEVEL_X = 500;
            LEVEL_Y = 200;
        }

        LEVEL_W = (int)(165f/640f * Options.screenWidth);
        LEVEL_H = (int)(156f/960f * Options.screenHeight);

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        game.mas.playMainMenu();

        mainMenubutt = new Rectangle(10f/640f * Options.screenWidth, 800f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        customlevelbutt = new Rectangle(100f/640f * Options.screenWidth, 150f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        leveleditbutt = new Rectangle(LEVEL_X, LEVEL_Y, LEVEL_W, LEVEL_H);
        leveleditPressed = false;

        fileBrowser = new FileBrowser();

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            Gdx.app.debug("AD","LEVELSELETCSHOW");
            this.game.adStateListener.ShowBannerAd();
        }
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for(int i=0; i<fileBrowser.levelbutts.length;i++)
        {
            if (in.justTouched() && fileBrowser.levelbutts[i].contains(touchpoint))
            {
                if (fileBrowser.levels.size() > i) //if you picked a valid choice
                {
                    if (!fileBrowser.levels.get(i).isDirectory()) { //if you picked a level
                        Assets.load_gamescreen();
                        game.setScreen(new GameScreen(game, fileBrowser.levels.get(i).readString()));
                        Assets.dispose_levelscreen();
                        fileBrowser.dispose();
                        this.dispose();
                    }
                    else //you picked a folder
                    {
                        fileBrowser.levelDir = fileBrowser.levels.get(i);
                        fileBrowser.levels = new ArrayList<FileHandle>(Arrays.asList(fileBrowser.levelDir.list()));
                        if (fileBrowser.levelDir.name().equals("LEVELS")) fileBrowser.levels.remove(0); //remove custom folder if you picked levels
                        break;
                    }
                }
            }
        }
        if (in.justTouched() && customlevelbutt.contains(touchpoint))
        {
            if (Gdx.app.getType() != Application.ApplicationType.WebGL)
            {
                fileBrowser.gocustom();
            }
            else game.setScreen(new GameScreen(game, Options.getCustom1()));
        }
        if (in.justTouched() && fileBrowser.upone.contains(touchpoint))
        {
            fileBrowser.pageUp();
        }
        if ((in.justTouched() && mainMenubutt.contains(touchpoint)) || in.isBackPressed())
        {
            Assets.load_mainmenu();
            game.setScreen(new MainMenuScreen(game));
            Assets.dispose_levelscreen();
            fileBrowser.dispose();
            this.dispose();
        }
        if ((in.justTouched() && fileBrowser.pageleftbutt.contains(touchpoint)))
        {
            fileBrowser.pageLeft();
        }
        if ((in.justTouched() && fileBrowser.pagerightbutt.contains(touchpoint)))
        {
            fileBrowser.pageRight();
        }
        if (leveleditbutt.contains(touchpoint) && in.justTouched()) leveleditPressed = true;
        if (leveleditPressed && !leveleditbutt.contains(touchpoint)) leveleditPressed = false;
        if (leveleditPressed && !in.isTouched())
        {
            Assets.load_leveledit();
            game.setScreen(new LevelScreen2(game));
            Assets.dispose_levelscreen();
            fileBrowser.dispose();
            this.dispose();
        }
    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(.27451f, .70588f, .83922f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(leveleditPressed ? Assets.MainMenuLevelEditorPressed : Assets.MainMenuLevelEditor, leveleditbutt.getX(), leveleditbutt.getY());
        Assets.font.draw(game.batch, "Return to MainMenu", mainMenubutt.getX(), mainMenubutt.getY());
        Assets.font.draw(game.batch, "Custom Level", customlevelbutt.getX(), customlevelbutt.getY());
        game.batch.end();

        fileBrowser.renderSprites(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for(Rectangle r: fileBrowser.levelbutts) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(customlevelbutt.getX(), customlevelbutt.getY(), customlevelbutt.getWidth(), customlevelbutt.getHeight());
        shapeRenderer.rect(mainMenubutt.getX(), mainMenubutt.getY(), mainMenubutt.getWidth(), mainMenubutt.getHeight());
        shapeRenderer.rect(fileBrowser.upone.getX(), fileBrowser.upone.getY(), fileBrowser.upone.getWidth(), fileBrowser.upone.getHeight());
        shapeRenderer.rect(fileBrowser.pageleftbutt.getX(), fileBrowser.pageleftbutt.getY(), fileBrowser.pageleftbutt.getWidth(), fileBrowser.pageleftbutt.getHeight());
        shapeRenderer.rect(fileBrowser.pagerightbutt.getX(), fileBrowser.pagerightbutt.getY(), fileBrowser.pagerightbutt.getWidth(), fileBrowser.pagerightbutt.getHeight());
        shapeRenderer.rect(leveleditbutt.getX(), leveleditbutt.getY(), leveleditbutt.getWidth(), leveleditbutt.getHeight());
        shapeRenderer.end();

        fileBrowser.renderShapes(shapeRenderer);
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
