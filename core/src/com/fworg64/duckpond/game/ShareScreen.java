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


    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    Rectangle sharebutt;
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

        if (Options.isHighres())
        {
            PINBUTT_W = 100;
            PINBUTT_H = 100;
            PINBUTT_X = 345;
            PINBUTT_Y = 1400;
            PINBUTT_XS = 80;
            PINBUTT_YS = 80;
        }
        else
        {
            PINBUTT_W = 50;
            PINBUTT_H = 50;
            PINBUTT_X = 222;
            PINBUTT_Y = 400;
            PINBUTT_XS = 65;
            PINBUTT_YS = 65;
        }

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        sharebutt = new Rectangle(100, 100, 100, 100);
        pinpad = new Rectangle[12];
        for (int i =0; i< pinpad.length; i++) pinpad[i] = new Rectangle(PINBUTT_X, PINBUTT_Y, PINBUTT_X + (i % 3)* PINBUTT_XS, PINBUTT_Y - (i/3)* PINBUTT_YS);

        dpClientConnection = new DPClientConnection();
        dpClientConnection.start();
    }

    public void update()
    {
        if (in.justTouched() && sharebutt.contains(in.getTouchpoint()))
        {
            share();
        }

        if (dpClientConnection.needpin == true) getPin();
        if (dpClientConnection.needConfirmPin == true) confirmPin();
    }

    public void getPin()
    {
        Gdx.app.debug("Pin Entry", "Get Pin");
        temppin = "";
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
                }
                else temppin += pinmap[i];
            }
        }
    }

    public void confirmPin()
    {
        Gdx.app.debug("Pin Entry", "Confirm PIN");
        temppin = "";
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
        for (int i =0; i< 12; i++) Assets.font.draw(game.batch, pinmap[i], pinpad[i].getX(), pinpad[i].getY() + pinpad[i].getHeight() * .5f);
        game.batch.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(sharebutt.getX(), sharebutt.getY(), sharebutt.getWidth(), sharebutt.getHeight());
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
