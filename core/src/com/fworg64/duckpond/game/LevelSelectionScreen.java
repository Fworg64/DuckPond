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

    int TEXTBUTT_X, TEXTBUTT_Y, TEXTBUTT_W, TEXTBUTT_H;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    TextCycleButton textCycleButton;
    Button leveleditbutt;
    Button mainMenubutt;
    Button getmorebutt;
    Button[] butts;

    Browser browser;

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

            TEXTBUTT_X = 420;
            TEXTBUTT_Y = 1920 -425;
            TEXTBUTT_W = 300;
            TEXTBUTT_H = 225;

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

            TEXTBUTT_X = 250;
            TEXTBUTT_Y = 960 -305;
            TEXTBUTT_W = 150;
            TEXTBUTT_H = 125;

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
        leveleditbutt =     new Button(WORLDMAKER_X, WORLDMAKER_Y, WORLDMAKER_W, WORLDMAKER_H, Assets.LevelSelectionWorldMaker);
        getmorebutt =       new Button(GETMORE_X, GETMORE_Y, GETMORE_W, GETMORE_H, Assets.LevelSelectionGetMore);

        butts = new Button[] {mainMenubutt, leveleditbutt, getmorebutt};
        textCycleButton = new TextCycleButton(new String[] {"Stock", "Custom", "Downloaded"}, TEXTBUTT_X, TEXTBUTT_Y, TEXTBUTT_W, TEXTBUTT_H);

        //fileBrowser = new FileBrowser();
        browser = new Browser(new BrowsableFolder(DuckPondGame.levelsfolder, true));

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            Gdx.app.debug("AD","LEVELSELETCSHOW");
            this.game.adStateListener.ShowBannerAd();
        }
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        browser.touch(in.isTouched() ? touchpoint : new Vector2());
        if (browser.isItemchosen())//if (fileBrowser.isLevelchosen())
        {
            Assets.load_gamescreen();
            game.setScreen(new GameScreen(game, browser.getItempicked(), browser.getItemname()));
        }

        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());
        textCycleButton.pollPress(in.isTouched() ? touchpoint: new Vector2());
        if (textCycleButton.isWasPressed())
        {
            switch (textCycleButton.getState())
            {
                case 0:
                    browser = new Browser(new BrowsableFolder(DuckPondGame.levelsfolder, true));
                    break;
                case 1:
                    browser = new Browser(new BrowsableFolder(DuckPondGame.customfolder, false));
                    break;
                case 2:
                    browser = new Browser(new BrowsableFolder(DuckPondGame.downloadsfolder, false));
                    break;
            }
            textCycleButton.pressHandled();
        }
        if (mainMenubutt.isJustPressed())
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
        if (leveleditbutt.isJustPressed()) {
            Assets.load_leveledit();
        }
        if (leveleditbutt.isWasPressed())
        {
            game.setScreen(new LevelScreen2(game));
            Assets.dispose_levelscreen();
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
        for (Button butt : butts) butt.renderSprites(game.batch);
        textCycleButton.renderSprites(game.batch);
        browser.renderSprites(game.batch);
        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
