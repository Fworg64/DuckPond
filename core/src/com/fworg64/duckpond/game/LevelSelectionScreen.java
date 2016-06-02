package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
    //button dims defs
    int WORLDMAKER_X;
    int WORLDMAKER_Y;
    int WORLDMAKER_W;
    int WORLDMAKER_H;

    int MAINBUTT_X;
    int MAINBUTT_Y;
    int MAINBUTT_W;
    int MAINBUTT_H;

    int GETMORE_X;
    int GETMORE_Y;
    int GETMORE_W;
    int GETMORE_H;

    int CUSTOMWORLD_X;
    int CUSTOMWORLD_Y;
    int CUSTOMWORLD_W;
    int CUSTOMWORLD_H;

    int DOWNLDBUTT_X;
    int DOWNLDBUTT_Y;
    int DOWNLDBUTT_W;
    int DOWNLDBUTT_H;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Rectangle customlevelbutt;
    Rectangle downldlevelbutt;
    Rectangle leveleditbutt;
    Rectangle mainMenubutt;
    Rectangle getmorebutt;

    FileBrowser fileBrowser;

    boolean leveleditPressed;

    public LevelSelectionScreen (DuckPondGame game)
    {
        //button dims
        if (Options.highres)
        {
            MAINBUTT_X = 62;
            MAINBUTT_Y = 1920 - 385; //BOTTOM LEFT CORNER
            MAINBUTT_W = 281;
            MAINBUTT_H = 160;

            GETMORE_X = 614;
            GETMORE_Y = 1920 -405;
            GETMORE_W = 400;
            GETMORE_H = 200;

            DOWNLDBUTT_X = 450;
            DOWNLDBUTT_Y = 1920-405;
            DOWNLDBUTT_W = 150;
            DOWNLDBUTT_H = 200;


            CUSTOMWORLD_X = 80; //BOTTOM LEFT
            CUSTOMWORLD_Y = 25;
            CUSTOMWORLD_W = 400;
            CUSTOMWORLD_H = 200;

            WORLDMAKER_X = 619; //BOTTOM LEFT
            WORLDMAKER_Y = 22;
            WORLDMAKER_W = 381;
            WORLDMAKER_H = 218;


        }
        else
        {
            MAINBUTT_X = 64;
            MAINBUTT_Y = 960 - 290;
            MAINBUTT_W = 167;
            MAINBUTT_H = 95;

            GETMORE_X = 364;
            GETMORE_Y = 960 -305;
            GETMORE_W = 237;
            GETMORE_H = 119;

            DOWNLDBUTT_X = 200;
            DOWNLDBUTT_Y = 960-305;
            DOWNLDBUTT_W = 100;
            DOWNLDBUTT_H = 119;

            CUSTOMWORLD_X = 47;
            CUSTOMWORLD_Y = 960-931;
            CUSTOMWORLD_W = 237;
            CUSTOMWORLD_H = 119;

            WORLDMAKER_X = 367;
            WORLDMAKER_Y = 960 - 935;
            WORLDMAKER_W = 226;
            WORLDMAKER_H = 129;
        }//button dims

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        game.mas.playMainMenu();

        mainMenubutt = new Rectangle(MAINBUTT_X, MAINBUTT_Y, MAINBUTT_W, MAINBUTT_H);
        customlevelbutt = new Rectangle(CUSTOMWORLD_X, CUSTOMWORLD_Y, CUSTOMWORLD_W, CUSTOMWORLD_H);
        leveleditbutt = new Rectangle(WORLDMAKER_X, WORLDMAKER_Y, WORLDMAKER_W, WORLDMAKER_H);
        getmorebutt = new Rectangle(GETMORE_X, GETMORE_Y, GETMORE_W, GETMORE_H);
        downldlevelbutt = new Rectangle(DOWNLDBUTT_X, DOWNLDBUTT_Y, DOWNLDBUTT_W, DOWNLDBUTT_H);

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
                        game.setScreen(new GameScreen(game, fileBrowser.levels.get(i).readString(), fileBrowser.levels.get(i).nameWithoutExtension()));
                        Assets.dispose_levelscreen();
                        fileBrowser.dispose();
                        this.dispose();
                    }
                    else //you picked a folder
                    {
                        fileBrowser.pageInto(fileBrowser.levels.get(i));
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
            else game.setScreen(new GameScreen(game, Options.getCustom1(), "Custom"));
        }
        if (in.justTouched() && downldlevelbutt.contains(touchpoint))
        {
            if (Gdx.app.getType() != Application.ApplicationType.WebGL)
            {
                fileBrowser.godownld();
            }
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
        if ((in.justTouched() && getmorebutt.contains(touchpoint)))
        {
            game.setScreen(new GetMoreScreen(game));
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
        game.batch.draw(Assets.LevelSelectionWorldMaker, leveleditbutt.getX(), leveleditbutt.getY());
        game.batch.draw(Assets.LevelSelectionMainMenu, mainMenubutt.getX(), mainMenubutt.getY());
        game.batch.draw(Assets.LevelSelectionCustomWorld, customlevelbutt.getX(), customlevelbutt.getY());
        game.batch.draw(Assets.LevelSelectionGetMore, getmorebutt.getX(), getmorebutt.getY());
        game.batch.end();

        fileBrowser.renderSprites(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for(Rectangle r: fileBrowser.levelbutts) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(customlevelbutt.getX(), customlevelbutt.getY(), customlevelbutt.getWidth(), customlevelbutt.getHeight());
        shapeRenderer.rect(mainMenubutt.getX(), mainMenubutt.getY(), mainMenubutt.getWidth(), mainMenubutt.getHeight());
        shapeRenderer.rect(downldlevelbutt.getX(), downldlevelbutt.getY(), downldlevelbutt.getWidth(), downldlevelbutt.getHeight());
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
