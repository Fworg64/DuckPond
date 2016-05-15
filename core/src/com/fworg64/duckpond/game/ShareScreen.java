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
    public int PINBUTT_W;
    public int PINBUTT_H;
    public int PINBUTT_X;
    public int PINBUTT_Y;
    public int PINBUTT_XS;
    public int PINBUTT_YS;

    public int BACKBUTT_X;
    public int BACKBUTT_Y;
    public int BACKBUTT_W;
    public int BACKBUTT_H;


    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    Rectangle sharebutt;
    Rectangle backbutt;
    Rectangle pinpad[];
    public static final String pinmap[] = {"1", "2","3","4","5","6","7","8","9","<-","0","OK"};

    InputListener in;
    Vector2 touchpoint;

    DPClientConnection dpClientConnection;
    String temppin;

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
            PINBUTT_W = 100;
            PINBUTT_H = 100;
            PINBUTT_X = 345;
            PINBUTT_Y = 1500;
            PINBUTT_XS = 180;
            PINBUTT_YS = 180;

            BACKBUTT_X =0;
            BACKBUTT_Y = 1820;
            BACKBUTT_W =100;
            BACKBUTT_H = 100;
        }
        else
        {
            PINBUTT_W = 50;
            PINBUTT_H = 50;
            PINBUTT_X = 222;
            PINBUTT_Y = 600;
            PINBUTT_XS = 65;
            PINBUTT_YS = 65;

            BACKBUTT_X =0;
            BACKBUTT_Y = 800;
            BACKBUTT_W = 50;
            BACKBUTT_H = 50;
        }

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        sharebutt = new Rectangle(100, 100, 100, 100);
        backbutt = new Rectangle(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H);
        pinpad = new Rectangle[12];
        for (int i =0; i< pinpad.length; i++) pinpad[i] = new Rectangle(PINBUTT_X + (i % 3)* PINBUTT_XS, PINBUTT_Y - (i/3)* PINBUTT_YS, PINBUTT_W, PINBUTT_H);

        dpClientConnection = new DPClientConnection();
        dpClientConnection.start();

        temppin = "";
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

        if (dpClientConnection.needpin == true) getPin();
        if (dpClientConnection.needConfirmPin == true) confirmPin();
    }

    public void getPin()
    {
        Gdx.app.debug("GetPin", Integer.toString(temppin.length()));
        for (int i = 0; i< 12; i++){
            if (in.justTouched() && pinpad[i].contains(in.getTouchpoint()))
            {
                if (pinmap[i].equals("<-")) {
                    if (temppin.length() <= 1)
                    {
                        temppin = "\0";
                        Options.setSavedPin(temppin);
                        Options.save();
                        dpClientConnection.gotpin = true;
                    }
                    else temppin = temppin.substring(0, temppin.length()-1);

                }
                else if (pinmap[i].equals("OK") && temppin.length() == 4) {
                    Options.setSavedPin(temppin);
                    Options.save();
                    dpClientConnection.gotpin = true;
                    Gdx.app.debug();
                }
                else if (temppin.length() < 4) temppin += pinmap[i];
            }
        }
    }

    public void confirmPin()
    {
        Gdx.app.debug("Pin Entry", "Confirm PIN");
        for (int i = 0; i< 12; i++){
            if (in.justTouched() && pinpad[i].contains(in.getTouchpoint()))
            {
                if (pinmap[i].equals("<-")) {
                    if (temppin.length() <=1 )
                    {
                        temppin = "\0";
                        dpClientConnection.pinConfirmSuccess = false;
                        dpClientConnection.gotpin = true;
                    }
                    else temppin = temppin.substring(0, temppin.length()-1);

                }
                else if (pinmap[i].equals("OK") && temppin.equals(Options.getSavedPin())) {
                    Options.setSavedPin(temppin);
                    Options.save();
                    dpClientConnection.pinConfirmSuccess = true;
                    dpClientConnection.gotpin = true;
                }
                else temppin += pinmap[i];
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
        if (dpClientConnection.needpin == true)
        {
            for (int i =0; i< 12; i++) Assets.font.draw(game.batch, pinmap[i], pinpad[i].getX(), pinpad[i].getY() + pinpad[i].getHeight() * .5f);
            Assets.font.draw(game.batch, temppin, PINBUTT_X, PINBUTT_Y + PINBUTT_YS);
        }
        game.batch.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(backbutt.getX(), backbutt.getY(), backbutt.getWidth(), backbutt.getHeight());
        shapeRenderer.rect(sharebutt.getX(), sharebutt.getY(), sharebutt.getWidth(), sharebutt.getHeight());
        if (dpClientConnection.needpin == true)
            for (int i =0; i< 12; i++) shapeRenderer.rect(pinpad[i].getX(), pinpad[i].getY(), pinpad[i].getWidth(), pinpad[i].getHeight());
        shapeRenderer.end();


    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

}
