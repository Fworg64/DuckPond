package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 8/10/2016.
 */
public class GetMoreScreen extends ScreenAdapter {
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    //ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Button backbutt;
    Button[] butts;

    Browser browser;
    BrowserCommunicator browserCommunicator;

    FileHandle file;
    String message;

    public GetMoreScreen(DuckPondGame game)
    {
        if (Options.isHighres())
        {
            backbutt = new Button(40, 1920 - 400, 329, 129, Assets.NavigationBack);
        }
        else
        {
            backbutt = new Button(30, 960-220, 170, 67, Assets.NavigationBack);
        }

        butts = new Button[] { backbutt};

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS)
        {
            Gdx.app.debug("Ad","SHOW, MAINMENU");
            this.game.adStateListener.ShowBannerAd();
        }

        browserCommunicator = new BrowserCommunicator();
        browser = new Browser(new BrowsableDPGetmore(), browserCommunicator, false);
        browser.start();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        browserCommunicator.setTouchpoint(in.isTouched() ? touchpoint : new Vector2());
        if (browserCommunicator.isSelectionMade())
        {
            file = Gdx.files.local(browserCommunicator.getSelectionName());
            file.parent().mkdirs();
            file.writeString(browserCommunicator.getSelectionContents(), false);
            message = browserCommunicator.getSelectionName().substring(2).replaceFirst("/", "\n").replaceAll("/", ": ");
            Gdx.app.debug("LevelSelection", browserCommunicator.getSelectionName());
            browserCommunicator.setResetSelection(true);
        }
        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());

        if (backbutt.isJustPressed()) {
            Assets.load_makenshare();
            Assets.load_mainmenubutt();
        }
        if (backbutt.isWasPressed()) {
            game.setScreen(new MakeNShare(game));
        }
        if (in.isBackPressed()) {
            Assets.load_mainmenubutt();
            Assets.load_makenshare();
            game.setScreen(new MakeNShare(game));
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
        browser.renderSprites(game.batch);
        game.batch.end();
    }
    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

    @Override
    public void dispose()
    {
        browserCommunicator.setClose(true);
        Assets.dispose_mainmenubutt();
        Assets.dispose_makenshare();
    }
}
