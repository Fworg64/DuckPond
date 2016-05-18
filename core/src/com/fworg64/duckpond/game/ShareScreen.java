package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
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

    public int USERNAME_X;
    public int USERNAME_Y;
    public int USERNAMECHANGE_X;
    public int USERNAMECHANGE_Y;
    public int USERNAMECHANGE_W;
    public int USERNAMECHANGE_H;

    public int SAVECONFIRM_X;
    public int SAVECONFIRM_Y;
    public int SAVECONFIRM_W;
    public int SAVECONFIRM_H;
    public int CANCEL_X;
    public int CANCEL_Y;
    public int CANCEL_W;
    public int CANCEL_H;

    public int SHAREBUTT_X;
    public int SHAREBUTT_Y;
    public int SHAREBUTT_W;
    public int SHAREBUTT_H;

    public int MESSAGE_X;
    public int MESSAGE_Y;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    Rectangle sharebutt;
    Rectangle backbutt;
    Rectangle saveconfirmbutt;
    Rectangle cancelbutt;
    Rectangle namechangebutt;

    InputListener in;
    Vector2 touchpoint;

    DPClientConnection dpClientConnection;
    PinPad pinPad;

    boolean changename;
    String username;

    String message = "";

    public ShareScreen (DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        Assets.load_font();

        if (Options.isHighres())
        {
            BACKBUTT_X =0;
            BACKBUTT_Y = 1820;
            BACKBUTT_W =100;
            BACKBUTT_H = 100;

            SHAREBUTT_X = 100;
            SHAREBUTT_Y = 100;
            SHAREBUTT_W = 100;
            SHAREBUTT_H = 100;

            USERNAME_X = 200;
            USERNAME_Y = 1800;
            USERNAMECHANGE_X = 700;
            USERNAMECHANGE_Y = 1800;
            USERNAMECHANGE_W = 100;
            USERNAMECHANGE_H = 100;

            SAVECONFIRM_X = 700;
            SAVECONFIRM_Y = 1600;
            SAVECONFIRM_W = 100;
            SAVECONFIRM_H = 100;

            CANCEL_X = 300;
            CANCEL_Y = 1600;
            CANCEL_W = 100;
            CANCEL_H = 100;

            MESSAGE_X = 200;
            MESSAGE_Y = 1700;
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
            SHAREBUTT_H = 100;

            USERNAME_X = 100;
            USERNAME_Y = 700;
            USERNAMECHANGE_X = 500;
            USERNAMECHANGE_Y = 800;
            USERNAMECHANGE_W = 50;
            USERNAMECHANGE_H = 50;

            SAVECONFIRM_X = 500;
            SAVECONFIRM_Y = 700;
            SAVECONFIRM_W = 50;
            SAVECONFIRM_H = 50;

            CANCEL_X = 200;
            CANCEL_Y = 700;
            CANCEL_W = 50;
            CANCEL_H = 50;

            MESSAGE_X = 100;
            MESSAGE_Y = 800;
        } //back, share=, namechange, saveconfirm, and cancel butt dims and pos

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        sharebutt = new Rectangle(SHAREBUTT_X, SHAREBUTT_Y, SHAREBUTT_W, SHAREBUTT_H);
        backbutt = new Rectangle(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H);
        namechangebutt = new Rectangle(USERNAMECHANGE_X, USERNAMECHANGE_Y, USERNAMECHANGE_W, USERNAMECHANGE_H);
        saveconfirmbutt = new Rectangle(SAVECONFIRM_X, SAVECONFIRM_Y, SAVECONFIRM_W, SAVECONFIRM_H);
        cancelbutt = new Rectangle(CANCEL_X, CANCEL_Y, CANCEL_W, CANCEL_H);

        pinPad = new PinPad();
        username = Options.getUsername();
        if (username.length() < 3) changename = true;
        else changename = false;

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
        if (in.justTouched() && namechangebutt.contains(in.getTouchpoint())) changename = true;

        if (pinPad.isNeedpin()) {
            getPin(); // UI thread, non blocking
            message = pinPad.getMessage();
        }
        else if (changename) {
            changeName(); //UI, non blocking
            message = "Name must be longer than 3 characters (a-Z, 0-9)";
        }
        else message = "Default message";



    }

    public void getPin()
    {
        //Gdx.app.debug("GetPin", Integer.toString(pinPad.tempPin.length()));
        for (int i = 0; i< 12; i++){
            if (in.justTouched() && pinPad.pinpadbutts[i].contains(in.getTouchpoint()))
            {
                pinPad.pressKey(i);
            }
        }
    }

    public void changeName()
    {
        char tempChar;
        in.showKeyboard();
        tempChar = in.pollChar();
        if (tempChar != '\0') username += tempChar;
        else if (in.backspaceJustPressed() && username.length() >0) username = username.substring(0, username.length() -1);

        //Gdx.app.debug("Type a filename and press enter. (a-Z, 0-9)", "");

        if (in.enterJustPressed() || (in.justTouched() && saveconfirmbutt.contains(touchpoint)))
        {
            if (username.length() >3)
            {
                Options.setUsername(username);
                Options.save();
            }

            changename = false;
            in.hideKeyboard();
        }

        if (in.justTouched() && cancelbutt.contains(touchpoint))
        {
            in.hideKeyboard();
            changename = false;
        }
    }
    
    public void share()
    {
        dpClientConnection = new DPClientConnection(pinPad);
        dpClientConnection.start();
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
        Assets.font.draw(game.batch, "share", sharebutt.getX(), sharebutt.getY());
        Assets.font.draw(game.batch, "namechange", namechangebutt.getX(), namechangebutt.getY());
        if (changename)
        {
            Assets.font.draw(game.batch, "saveconfirm", saveconfirmbutt.getX(), saveconfirmbutt.getY());
            Assets.font.draw(game.batch, "cancel", cancelbutt.getX(), cancelbutt.getY());
        }
        Assets.font.draw(game.batch, username, USERNAME_X, USERNAME_Y);

        Assets.font.draw(game.batch, message, MESSAGE_X, MESSAGE_Y);
        game.batch.end();

        if (pinPad.isNeedpin()) pinPad.renderSpritesAndText(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(backbutt.getX(), backbutt.getY(), backbutt.getWidth(), backbutt.getHeight());
        shapeRenderer.rect(sharebutt.getX(), sharebutt.getY(), sharebutt.getWidth(), sharebutt.getHeight());
        shapeRenderer.rect(namechangebutt.getX(), namechangebutt.getY(), namechangebutt.getWidth(), namechangebutt.getHeight());
        if (changename)
        {
            shapeRenderer.rect(saveconfirmbutt.getX(), saveconfirmbutt.getY(), saveconfirmbutt.getWidth(), saveconfirmbutt.getHeight());
            shapeRenderer.rect(cancelbutt.getX(), cancelbutt.getY(), cancelbutt.getWidth(), cancelbutt.getHeight());
        }

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
