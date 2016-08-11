package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 8/10/2016.
 */
public class MakeNShare extends ScreenAdapter {

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Button mainMenubutt, worldMakerbutt, shareLevelsbutt, getLevelsbutt;
    Button[] butts;


    public MakeNShare(DuckPondGame game)
    {
        if (Options.highres)
        {
            mainMenubutt = new Button(62, 1920-385-160, 281, 160, Assets.LevelSelectionMainMenu);
            worldMakerbutt =  new Button(258, 1920 - 483-285, 565, 285, Assets.MakeNShareWorldMaker);
            shareLevelsbutt = new Button(268, 1920-858-335, 544, 335, Assets.MakeNShareShareLevels);
            getLevelsbutt =   new Button(309, 1920-1283-348, 462, 348, Assets.MakeNShareGetMore);

        }
        else
        {
            mainMenubutt = new Button(30, 960-290-95, 167, 95, Assets.LevelSelectionMainMenu);
            worldMakerbutt =  new Button(179, 960-246-142, 282, 142, Assets.MakeNShareWorldMaker);
            shareLevelsbutt = new Button(184, 960-419-167, 271, 167, Assets.MakeNShareShareLevels);
            getLevelsbutt =   new Button(204, 960-617-174, 237, 174, Assets.MakeNShareGetMore);
        }

        butts = new Button[] {mainMenubutt, worldMakerbutt, shareLevelsbutt, getLevelsbutt};

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
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());

        if (mainMenubutt.isJustPressed()) {
            Assets.load_mainmenu();
        }
        if (mainMenubutt.isWasPressed()) {
            game.setScreen(new MainMenuScreen(game));
        }
        if (in.isBackPressed()) {
            Assets.load_mainmenu();
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }

        if (worldMakerbutt.isJustPressed())
        {
            Assets.load_leveledit();
        }
        if (worldMakerbutt.isWasPressed())
        {
            game.setScreen(new LevelScreen2(game));
            this.dispose();
        }

        if (shareLevelsbutt.isJustPressed())
        {
            Assets.load_share();
        }
        if (shareLevelsbutt.isWasPressed())
        {
            game.setScreen(new ShareScreen2(game));
            this.dispose();
        }

        if (getLevelsbutt.isJustPressed())
        {
            Assets.load_navigation();
        }
        if (getLevelsbutt.isWasPressed())
        {
            //hmm
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
        Assets.dispose_mainmenubutt();
        Assets.dispose_makenshare();
    }

}
