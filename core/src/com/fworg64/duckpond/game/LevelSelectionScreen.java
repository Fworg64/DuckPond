package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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

    Button customlevelbutt;
    Button downldlevelbutt;
    Button leveleditbutt;
    Button mainMenubutt;
    Button getmorebutt;
    Button[] butts;

    FileBrowser fileBrowser;
    boolean filebrowsermade;
    Rectangle makefilebrowser;


    public LevelSelectionScreen (DuckPondGame game)
    {
        //button dims
        if (Options.highres)
        {
            MAINBUTT_X = 62;
            MAINBUTT_Y = 1920 - 385; //BOTTOM LEFT CORNER
            MAINBUTT_W = 281;
            MAINBUTT_H = 160;

            GETMORE_X = 80; //BOTTOM LEFT
            GETMORE_Y = 25;
            GETMORE_W = 320;
            GETMORE_H = 199;


            DOWNLDBUTT_X = 420;
            DOWNLDBUTT_Y = 1920-405;
            DOWNLDBUTT_W = 300;
            DOWNLDBUTT_H = 200;

            CUSTOMWORLD_X = 750;
            CUSTOMWORLD_Y = 1920 -405;
            CUSTOMWORLD_W = 300;
            CUSTOMWORLD_H = 200;

            WORLDMAKER_X = 619; //BOTTOM LEFT
            WORLDMAKER_Y = 22;
            WORLDMAKER_W = 381;
            WORLDMAKER_H = 218;


        }
        else
        {
            MAINBUTT_X = 30;
            MAINBUTT_Y = 960 - 290;
            MAINBUTT_W = 167;
            MAINBUTT_H = 95;

            GETMORE_X = 47;
            GETMORE_Y = 960-931;
            GETMORE_W = 179;
            GETMORE_H = 108;

            DOWNLDBUTT_X = 250;
            DOWNLDBUTT_Y = 960-305;
            DOWNLDBUTT_W = 150;
            DOWNLDBUTT_H = 100;

            CUSTOMWORLD_X = 440;
            CUSTOMWORLD_Y = 960 -305;
            CUSTOMWORLD_W = 150;
            CUSTOMWORLD_H = 100;

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

        mainMenubutt =      new Button(MAINBUTT_X, MAINBUTT_Y, MAINBUTT_W, MAINBUTT_H, Assets.LevelSelectionMainMenu);
        customlevelbutt =   new Button(CUSTOMWORLD_X, CUSTOMWORLD_Y, CUSTOMWORLD_W, CUSTOMWORLD_H, Assets.LevelSelectionFolder);
        leveleditbutt =     new Button(WORLDMAKER_X, WORLDMAKER_Y, WORLDMAKER_W, WORLDMAKER_H, Assets.LevelSelectionWorldMaker);
        getmorebutt =       new Button(GETMORE_X, GETMORE_Y, GETMORE_W, GETMORE_H, Assets.LevelSelectionGetMore);
        downldlevelbutt =   new Button(DOWNLDBUTT_X, DOWNLDBUTT_Y, DOWNLDBUTT_W, DOWNLDBUTT_H, Assets.LevelSelectionFolder);
        butts = new Button[] {mainMenubutt, customlevelbutt, leveleditbutt, getmorebutt, downldlevelbutt};

        filebrowsermade = false;
        makefilebrowser = new Rectangle(Options.screenWidth * .3f, Options.screenHeight * .3f, .3f*Options.screenWidth, .3f*Options.screenHeight);

        //fileBrowser = new FileBrowser();

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            Gdx.app.debug("AD","LEVELSELETCSHOW");
            this.game.adStateListener.ShowBannerAd();
        }
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        if (!filebrowsermade)
        {
            if (in.justTouched() && makefilebrowser.contains(touchpoint))
            {
                fileBrowser = new FileBrowser();
                filebrowsermade = true;
            }
        }
        else
        {
            fileBrowser.touch(in.justTouched() ? touchpoint : new Vector2());
            if (fileBrowser.isLevelchosen())
            {
                Assets.load_gamescreen();
                game.setScreen(new GameScreen(game, fileBrowser.getLevelPicked(), fileBrowser.getNamePicked()));
            }

            for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());
            if (customlevelbutt.isWasPressed())
            {
                if (Gdx.app.getType() != Application.ApplicationType.WebGL)
                {
                    fileBrowser.gocustom();
                    customlevelbutt.pressHandled();
                }
                else game.setScreen(new GameScreen(game, Options.getCustom1(), "Custom"));
            }
            if (downldlevelbutt.isWasPressed())
            {
                if (Gdx.app.getType() != Application.ApplicationType.WebGL)
                {
                    fileBrowser.godownld();
                    downldlevelbutt.pressHandled();
                }
            }
            if (mainMenubutt.isPressed())
            {
                Assets.load_mainmenu();
            }
            if (mainMenubutt.isWasPressed())
            {
                game.setScreen(new MainMenuScreen(game));
                Assets.dispose_levelscreen();
                this.dispose();
            }
            if (in.isBackPressed())
            {
                Assets.load_mainmenu();
                game.setScreen(new MainMenuScreen(game));
                Assets.dispose_levelscreen();
                this.dispose();
            }
            if ((getmorebutt.isWasPressed()))
            {
                game.setScreen(new GetMoreScreen(game));
                Assets.dispose_levelscreen();
                this.dispose();
            }
            if (leveleditbutt.isPressed()) {
                Assets.load_leveledit();
            }
            if (leveleditbutt.isWasPressed())
            {
                game.setScreen(new LevelScreen2(game));
                Assets.dispose_levelscreen();
                this.dispose();
            }
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
        Assets.font.draw(game.batch, "CUSTOM", CUSTOMWORLD_X, CUSTOMWORLD_Y + .6f*CUSTOMWORLD_H);
        Assets.font.draw(game.batch, "DOWN\nLOADED", DOWNLDBUTT_X, DOWNLDBUTT_Y + .9f*DOWNLDBUTT_H);
        if (!filebrowsermade) Assets.font.draw(game.batch, "Debug Rectangle\nPress Me", makefilebrowser.getX(), makefilebrowser.getY() + .5f * makefilebrowser.getHeight());
        game.batch.end();

        for (Button butt : butts) butt.renderSprites(game.batch);

        if (filebrowsermade) fileBrowser.renderSprites(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);

        if (!filebrowsermade) shapeRenderer.rect(makefilebrowser.getX(), makefilebrowser.getY(), makefilebrowser.getWidth(), makefilebrowser.getHeight());

        shapeRenderer.end();

        if (filebrowsermade) fileBrowser.renderShapes(shapeRenderer);
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
