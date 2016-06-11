package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 6/11/2016.
 */
public class ShareScreen2 extends ScreenAdapter{
    int BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H;
    int MESSAGE_X, MESSAGE_Y;
    int USERNAME_X, USERNAME_Y;
    int CONFIRMNAMECHANGE_X, CONFIRMNAMECHANGE_Y, CONFIRMNAMECHANGE_W, CONFIRMNAMECHANGE_H;
    int CANCELNAMECHANGE_X, CANCELNAMECHANGE_Y, CANCELNAMECHANGE_W, CANCELNAMECHANGE_H;
    int CHANGEUSERNAME_X, CHANGEUSERNAME_Y, CHANGEUSERNAME_W, CHANGEUSERNAME_H;
    
    String username;
    String pin;
    String Message;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;
    
    Button backbutt;
    Button confirmnamechangebutt;
    Button cancelnamechangebutt;
    Button changeusername;
    Button butts[];

    boolean needUsername;
    public enum PINSTATE {NEEDPIN1, NEEDPIN2, PINSNOTMATCH};
    
    public ShareScreen2(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        if (Options.isHighres())
        {
            BACKBUTT_X = 40;
            BACKBUTT_Y = 1920 - 365;
            BACKBUTT_W = 329;
            BACKBUTT_H = 129;

            MESSAGE_X = 420;
            MESSAGE_Y = 1920 - 365;
            USERNAME_X = 420;
            USERNAME_Y = 1920 - 255;

            CONFIRMNAMECHANGE_X = 300;
            CONFIRMNAMECHANGE_Y = 1920 - 800;
            CONFIRMNAMECHANGE_W = 126;
            CONFIRMNAMECHANGE_H = 126;

            CANCELNAMECHANGE_X = 700;
            CANCELNAMECHANGE_Y = 1920 - 800;
            CANCELNAMECHANGE_W = 126;
            CANCELNAMECHANGE_H = 126;

            CHANGEUSERNAME_X = 750;
            CHANGEUSERNAME_Y = 1920 - 1898;
            CHANGEUSERNAME_W = 300;
            CHANGEUSERNAME_H = 200;
        }
        else
        {
            BACKBUTT_X = 30;
            BACKBUTT_Y = 960 - 220;
            BACKBUTT_W = 170;
            BACKBUTT_H = 67;

            MESSAGE_X = 230;
            MESSAGE_Y = 960 - 200;
            USERNAME_X = 230;
            USERNAME_Y = 960 - 150;

            CONFIRMNAMECHANGE_X = 400;
            CONFIRMNAMECHANGE_Y = 960-400;
            CONFIRMNAMECHANGE_W = 74;
            CONFIRMNAMECHANGE_H = 74;

            CANCELNAMECHANGE_X = 150;
            CANCELNAMECHANGE_Y = 960 - 400;
            CANCELNAMECHANGE_W = 74;
            CANCELNAMECHANGE_H = 74;

            CHANGEUSERNAME_X = 460;
            CHANGEUSERNAME_Y = 960 - 935;
            CHANGEUSERNAME_W = 150;
            CHANGEUSERNAME_H = 100;
        }
        
        backbutt = new Button(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H, Assets.NavigationBack);
        confirmnamechangebutt = new Button(CONFIRMNAMECHANGE_X, CONFIRMNAMECHANGE_Y, CONFIRMNAMECHANGE_W, CONFIRMNAMECHANGE_H, Assets.NavigationConfirm);
        cancelnamechangebutt = new Button(CANCELNAMECHANGE_X, CANCELNAMECHANGE_Y, CANCELNAMECHANGE_W, CANCELNAMECHANGE_H, Assets.NavigationCancel);
        changeusername = new Button(CHANGEUSERNAME_X, CHANGEUSERNAME_Y, CHANGEUSERNAME_W, CHANGEUSERNAME_H, Assets.NavigationFolder);
        butts = new Button[] {backbutt, confirmnamechangebutt, cancelnamechangebutt, changeusername};

        changeusername.setButttext("Change\nName");
        confirmnamechangebutt.hide();
        cancelnamechangebutt.hide();

        Message = "";
        username = Options.getUsername();
        pin = Options.getSavedPin();
        
        if (username.equals("")) needUsername = true;
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());
        if (backbutt.isJustPressed())
        {
            //loadleveledit
            Assets.load_leveledit();
        }
        if (backbutt.isWasPressed())
        {
            //go leveledit
            game.setScreen(new LevelScreen2(game));
            this.dispose();
        }
        if (changeusername.isWasPressed()) {
            needUsername = true;
            changeusername.pressHandled();
        }
        if (needUsername) changeUsername();
    }

    public void changeUsername()
    {
        Message = "Enter a username";
        cancelnamechangebutt.show();
        char tempChar;
        in.showKeyboard();
        tempChar = in.pollChar();
        if (tempChar != '\0') {

            username += tempChar;
        }
        else if (in.backspaceJustPressed() && username.length() >0) username = username.substring(0, username.length() -1);

        //Gdx.app.debug("Type a filename and press enter. (a-Z, 0-9)", "");

        if (username.length() >3) confirmnamechangebutt.show();
        if (in.enterJustPressed() || confirmnamechangebutt.isWasPressed())
        {
            if (username.length() >3)
            {
                Options.setUsername(username);
                Options.save();
                needUsername = false;
                in.hideKeyboard();
                confirmnamechangebutt.pressHandled();
                confirmnamechangebutt.hide();
                cancelnamechangebutt.hide();
                Message = "";
            }
        }

        if (cancelnamechangebutt.isWasPressed())
        {
            in.hideKeyboard();
            needUsername = false;
            username = Options.getUsername();
            cancelnamechangebutt.pressHandled();
            cancelnamechangebutt.hide();
            confirmnamechangebutt.hide();
            Message = "";
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
        for ( Button butt : butts) butt.renderSprites(game.batch);
        Assets.font.draw(game.batch, username, USERNAME_X, USERNAME_Y);
        Assets.font.draw(game.batch, Message, MESSAGE_X, MESSAGE_Y);
        game.batch.end();
    }
    
    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

}
