package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StreamUtils;

/**
 * Created by fworg on 4/30/2016.
 */
public class ShareScreen extends ScreenAdapter
{
    public int BACKBUTT_X;
    public int BACKBUTT_Y;
    public int BACKBUTT_W;
    public int BACKBUTT_H;

    public int SHAREBUTT_X = 100;
    public int SHAREBUTT_Y = 100;
    public int SHAREBUTT_W = 100;
    public int SHAREBUTT_Z = 100;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    Rectangle sharebutt;
    Rectangle backbutt;

    InputListener in;
    Vector2 touchpoint;

    DPClientConnection dpClientConnection;
    PinPad pinPad;

    public ShareScreen (DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        Assets.load_font();

        Options.setUsername("FWORG");
        Options.save();

        if (Options.isHighres())
        {
            BACKBUTT_X =0;
            BACKBUTT_Y = 1820;
            BACKBUTT_W =100;
            BACKBUTT_H = 100;

            SHAREBUTT_X = 100;
            SHAREBUTT_Y = 100;
            SHAREBUTT_W = 100;
            SHAREBUTT_Z = 100;
        }
        else
        {
            BACKBUTT_X =0;
            BACKBUTT_Y = 800;
            BACKBUTT_W = 50;
            BACKBUTT_H = 50;

            SHAREBUTT_X = 100;
            SHAREBUTT_Y = 100;
            SHAREBUTT_W = 100;
            SHAREBUTT_Z = 100;
        } //back and sharebutt dims and pos

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        sharebutt = new Rectangle(100, 100, 100, 100);
        backbutt = new Rectangle(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H);

        pinPad = new PinPad();

        dpClientConnection = new DPClientConnection(pinPad);
        dpClientConnection.start();
    }

    public void update()
    {
        if (in.justTouched() && sharebutt.contains(in.getTouchpoint()))
        {
            share();
        }
        if (in.justTouched() && backbutt.contains(in.getTouchpoint()))
        {
            game.setScreen(new LevelScreen2(game));
            this.dispose();
        }

        if (pinPad.isNeedpin()) getPin(); // UI thread, non blocking
    }

    public void getPin()
    {
        Gdx.app.debug("GetPin", Integer.toString(pinPad.tempPin.length()));
        for (int i = 0; i< 12; i++){
            if (in.justTouched() && pinPad.pinpadbutts[i].contains(in.getTouchpoint()))
            {
                pinPad.pressKey(i);
            }
        }
    }
    
    public void share()
    {

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
        game.batch.end();

        if (pinPad.isNeedpin()) pinPad.renderSpritesAndText(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(backbutt.getX(), backbutt.getY(), backbutt.getWidth(), backbutt.getHeight());
        shapeRenderer.rect(sharebutt.getX(), sharebutt.getY(), sharebutt.getWidth(), sharebutt.getHeight());
        shapeRenderer.end();

        if (pinPad.isNeedpin()) pinPad.renderShapes(shapeRenderer);


    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

}
