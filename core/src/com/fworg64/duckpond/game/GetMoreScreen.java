package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 5/18/2016.
 */
public class GetMoreScreen extends ScreenAdapter
{
    public int BACKBUTT_X;
    public int BACKBUTT_Y;
    public int BACKBUTT_W;
    public int BACKBUTT_H;

    DuckPondGame game;
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    DPGetMoreConnection dpGetMoreConnection;
    GetMoreBrowser getMoreBrowser;

    Rectangle backbutt;

    public GetMoreScreen(DuckPondGame game)
    {
        if (Options.isHighres())
        {
            BACKBUTT_X =0;
            BACKBUTT_Y = 1820;
            BACKBUTT_W =100;
            BACKBUTT_H = 100;
        }
        else
        {
            BACKBUTT_X =0;
            BACKBUTT_Y = 800;
            BACKBUTT_W = 50;
            BACKBUTT_H = 50;
        }//buttdims

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);

        backbutt = new Rectangle(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H);


        touchpoint = new Vector2();
        getMoreBrowser = new GetMoreBrowser();

        dpGetMoreConnection = new DPGetMoreConnection(getMoreBrowser);
        dpGetMoreConnection.start();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched()) Gdx.app.debug("Just touched @" , touchpoint.toString());

        if (in.justTouched() && backbutt.contains(touchpoint)) //go back to level selection
        {
            Assets.load_levelscreen();
            Assets.load_navigation();
            game.setScreen(new LevelSelectionScreen(game));
            getMoreBrowser.cancelGetRequest();
            this.dispose();

            //should probably close network stuff here
        }

        if (getMoreBrowser.isNeedRequest())
        {
            if (in.justTouched()) getMoreBrowser.touch(touchpoint);
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
        //need assetes
        Assets.font.draw(game.batch, "back", backbutt.getX(), backbutt.getY());
        game.batch.end();

        getMoreBrowser.renderSpritesAndText(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(backbutt.getX(), backbutt.getY(), backbutt.getWidth(), backbutt.getHeight());
        shapeRenderer.end();

        getMoreBrowser.renderShapes(shapeRenderer);
    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }
}
