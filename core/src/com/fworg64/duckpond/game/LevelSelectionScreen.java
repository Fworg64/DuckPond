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
    //button dims defs
    int WORLDMAKER_X, WORLDMAKER_Y, WORLDMAKER_W, WORLDMAKER_H;
    int BACKTODL_X, BACKTODL_Y, BACKTODL_W, BACKTODL_H;
    int MAINBUTT_X, MAINBUTT_Y, MAINBUTT_W, MAINBUTT_H;
    int GETMORE_X, GETMORE_Y, GETMORE_W, GETMORE_H;
    int TEXTBUTT_X, TEXTBUTT_Y, TEXTBUTT_W, TEXTBUTT_H;
    int MESSAGE_X, MESSAGE_Y;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    TextCycleButton textCycleButton;
    Button leveleditbutt;
    Button mainMenubutt;
    Button getmorebutt;
    Button backtodlbutt;
    Button[] butts;

    Browser browser;
    BrowserCommunicator browserCommunicator;
    volatile BrowsableDPGetmore downloadBrowsable;
    Thread networkBrosableMaker;

    public enum HandleSelection {PLAY,DOWNLOAD, UPLOAD };
    HandleSelection handleSelection;

    String message;

    public LevelSelectionScreen (DuckPondGame game)
    {
        //button dims
        if (Options.highres)
        {
            MAINBUTT_X = 62;
            MAINBUTT_Y = 1920 - 385; //BOTTOM LEFT CORNER
            MAINBUTT_W = 281;
            MAINBUTT_H = 160;

            GETMORE_X = 670; //BOTTOM LEFT
            GETMORE_Y = 25;
            GETMORE_W = 320;
            GETMORE_H = 199;

            TEXTBUTT_X = 500;
            TEXTBUTT_Y = 1920 -425;
            TEXTBUTT_W = 300;
            TEXTBUTT_H = 225;

            WORLDMAKER_X = 670; //BOTTOM LEFT
            WORLDMAKER_Y = 22;
            WORLDMAKER_W = 381;
            WORLDMAKER_H = 218;

            BACKTODL_X = 670;
            BACKTODL_Y = 22;
            BACKTODL_W = 300;
            BACKTODL_H = 200;

            MESSAGE_X = 20;
            MESSAGE_Y = 200;
        }
        else
        {
            MAINBUTT_X = 30;
            MAINBUTT_Y = 960 - 290;
            MAINBUTT_W = 167;
            MAINBUTT_H = 95;

            GETMORE_X = 420;
            GETMORE_Y = 960-931;
            GETMORE_W = 179;
            GETMORE_H = 108;

            TEXTBUTT_X = 275;
            TEXTBUTT_Y = 960 -305;
            TEXTBUTT_W = 150;
            TEXTBUTT_H = 125;

            WORLDMAKER_X = 400;
            WORLDMAKER_Y = 960 - 935;
            WORLDMAKER_W = 226;
            WORLDMAKER_H = 129;

            BACKTODL_X = 420;
            BACKTODL_Y = 960 - 935;
            BACKTODL_W = 150;
            BACKTODL_H = 100;

            MESSAGE_X = 10;
            MESSAGE_Y = 110;
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
        backtodlbutt =    new Button(BACKTODL_X, BACKTODL_Y, BACKTODL_W, BACKTODL_H, Assets.NavigationFolder);
        backtodlbutt.setButttext("Back\nto DL'd");

        butts = new Button[] {mainMenubutt, leveleditbutt, getmorebutt, backtodlbutt};
        textCycleButton = new TextCycleButton(new String[] {"Stock", "Custom", "Downloaded"}, TEXTBUTT_X, TEXTBUTT_Y, TEXTBUTT_W, TEXTBUTT_H);

        //fileBrowser = new FileBrowser();
        browserCommunicator = new BrowserCommunicator();
        browser = new Browser(new BrowsableFolder(DuckPondGame.levelsfolder, true), browserCommunicator, false);
        browser.start();
        downloadBrowsable = null;
        networkBrosableMaker = null;
        handleSelection = HandleSelection.PLAY;

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            Gdx.app.debug("AD","LEVELSELETCSHOW");
            this.game.adStateListener.ShowBannerAd();
        }

        getmorebutt.hide();
        leveleditbutt.hide();
        backtodlbutt.hide();

        message = "\nPress the >cycler< for more";
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        browserCommunicator.setTouchpoint(in.isTouched() ? touchpoint : new Vector2());
        if (browserCommunicator.isSelectionMade())//if (fileBrowser.isLevelchosen())
        {
            if (handleSelection == HandleSelection.PLAY)
            {
                    Assets.load_gamescreen();
                    game.setScreen(new GameScreen(game, browserCommunicator.getSelectionContents(), browserCommunicator.getSelectionName()));
                    Assets.dispose_navigation();
                    Assets.dispose_levelscreen();
            }
            else if (handleSelection == HandleSelection.DOWNLOAD)
            {
                FileHandle file = Gdx.files.local(browserCommunicator.getSelectionName());
                file.parent().mkdirs();
                file.writeString(browserCommunicator.getSelectionContents(), false);
                message = browserCommunicator.getSelectionName().substring(2).replaceFirst("/", "\n").replaceAll("/", ": ");
                Gdx.app.debug("LevelSelection", browserCommunicator.getSelectionName());
                browserCommunicator.setResetSelection(true);
            }
            else if (handleSelection == HandleSelection.UPLOAD)
            {
                //upload
            }

        }

        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());
        textCycleButton.pollPress(in.isTouched() ? touchpoint: new Vector2());
        if (textCycleButton.isWasPressed())
        {
            backtodlbutt.hide();
            browserCommunicator.setClose(true);
            browserCommunicator = new BrowserCommunicator(); //ditch the old shit
            switch (textCycleButton.getState())
            {
                case 0:
                    message = "";
                    browser = new Browser(new BrowsableFolder(DuckPondGame.levelsfolder, true), browserCommunicator, false);
                    getmorebutt.hide();
                    leveleditbutt.hide();
                    handleSelection = HandleSelection.PLAY;
                    break;
                case 1:
                    message = "\nMake and share";
                    browser = new Browser(new BrowsableFolder(DuckPondGame.customfolder, false), browserCommunicator, false);
                    leveleditbutt.show();
                    getmorebutt.hide();
                    handleSelection = HandleSelection.PLAY;
                    break;
                case 2:
                    message = "\nGet more with";
                    browser = new Browser(new BrowsableFolder(DuckPondGame.downloadsfolder, false), browserCommunicator, false);
                    getmorebutt.show();
                    leveleditbutt.hide();
                    handleSelection = HandleSelection.PLAY;
                    break;
            }
            browser.start();
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
        if (getmorebutt.isJustPressed())
        {
            textCycleButton.goNoState();
            networkBrosableMaker = new Thread(new Runnable() {
                @Override
                public void run() {
                    Gdx.app.debug("networkBrowsableMaker", "Starting");
                    downloadBrowsable = new BrowsableDPGetmore();
                    Gdx.app.debug("networkBrowsableMaker", "Finished");
                }
            });
            networkBrosableMaker.start();
        }
        if ((getmorebutt.isWasPressed()))
        {
            message = "GET MORE\nConnecting...";
            if(networkBrosableMaker.getState()==Thread.State.TERMINATED){
                browserCommunicator.setClose(true);
                browserCommunicator = new BrowserCommunicator();
                browser = new Browser(downloadBrowsable, browserCommunicator, false);
                browser.start();
                handleSelection = HandleSelection.DOWNLOAD;
                getmorebutt.pressHandled();
                message = "GET MORE\nConnected!";
                getmorebutt.hide();
                backtodlbutt.show();
            }
        }
        if (backtodlbutt.isWasPressed())
        {
            textCycleButton.setStateDirect(2);
            browserCommunicator.setClose(true);
            browserCommunicator = new BrowserCommunicator();
            browser = new Browser(new BrowsableFolder(DuckPondGame.downloadsfolder, false), browserCommunicator, false);
            browser.start();
            handleSelection = HandleSelection.PLAY;
            backtodlbutt.pressHandled();
            message = "\nGet more with";
            backtodlbutt.hide();
            getmorebutt.show();

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
        Assets.font.draw(game.batch, message, MESSAGE_X, MESSAGE_Y);
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
