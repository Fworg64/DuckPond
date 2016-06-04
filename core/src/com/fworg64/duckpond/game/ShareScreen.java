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
    public int PINCHANGE_X;
    public int PINCHANGE_Y;
    public int PINCHANGE_W;
    public int PINCHANGE_H;

    public int SAVECONFIRM_X;
    public int SAVECONFIRM_Y;
    public int SAVECONFIRM_W;
    public int SAVECONFIRM_H;
    public int CANCEL_X;
    public int CANCEL_Y;
    public int CANCEL_W;
    public int CANCEL_H;

    public int MESSAGE_X;
    public int MESSAGE_Y;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    Rectangle backbutt;
    Rectangle saveconfirmbutt;
    Rectangle cancelbutt;
    Rectangle namechangebutt;
    Rectangle pinchangebutt;

    InputListener in;
    Vector2 touchpoint;

    DPClientConnection dpClientConnection;
    PinPad pinPad;
    FileBrowser fileBrowser;

    FileTransferCommunicator fileTransferCommunicator;

    boolean changename;
    String username;



    String message = "Connecting...";

    public ShareScreen (DuckPondGame game)
    {
        Gdx.app.debug("Screen Starting", " Share");
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        Assets.load_font();

        if (Options.isHighres())
        {
            BACKBUTT_X = 62;
            BACKBUTT_Y = 1920 - 365;
            BACKBUTT_W =329;
            BACKBUTT_H = 129;

            USERNAME_X = 400;
            USERNAME_Y = 1920 - 250;
            USERNAMECHANGE_X = 400;
            USERNAMECHANGE_Y = 1920 - 1898;
            USERNAMECHANGE_W = 300;
            USERNAMECHANGE_H = 200;
            PINCHANGE_X = 750;
            PINCHANGE_Y = 1920 - 1898;
            PINCHANGE_W = 300;
            PINCHANGE_H = 200;

            SAVECONFIRM_X = 603;
            SAVECONFIRM_Y = 1920-700;
            SAVECONFIRM_W = 126;
            SAVECONFIRM_H = 126;

            CANCEL_X = 351;
            CANCEL_Y = 1920 - 700;
            CANCEL_W = 126;
            CANCEL_H = 126;

            MESSAGE_X = 62;
            MESSAGE_Y = 1920 - 395;
        }
        else
        {
            BACKBUTT_X =30;
            BACKBUTT_Y = 960 - 220;
            BACKBUTT_W = 170;
            BACKBUTT_H = 67;

            USERNAME_X = 210;
            USERNAME_Y = 960 - 200;
            USERNAMECHANGE_X = 240;
            USERNAMECHANGE_Y = 960 - 935;
            USERNAMECHANGE_W = 150;
            USERNAMECHANGE_H = 100;
            PINCHANGE_X = 440;
            PINCHANGE_Y = 960 - 935;
            PINCHANGE_W = 150;
            PINCHANGE_H = 100;

            SAVECONFIRM_X = 209;
            SAVECONFIRM_Y = 500;
            SAVECONFIRM_W = 74;
            SAVECONFIRM_H = 74;

            CANCEL_X = 357;
            CANCEL_Y = 500;
            CANCEL_W = 74;
            CANCEL_H = 74;

            MESSAGE_X = 30;
            MESSAGE_Y = 960 - 290;
        } //back, share=, namechange, saveconfirm, and cancel butt dims and pos

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        backbutt = new Rectangle(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H);
        namechangebutt = new Rectangle(USERNAMECHANGE_X, USERNAMECHANGE_Y, USERNAMECHANGE_W, USERNAMECHANGE_H);
        pinchangebutt = new Rectangle(PINCHANGE_X, PINCHANGE_Y, PINCHANGE_W, PINCHANGE_H);
        saveconfirmbutt = new Rectangle(SAVECONFIRM_X, SAVECONFIRM_Y, SAVECONFIRM_W, SAVECONFIRM_H);
        cancelbutt = new Rectangle(CANCEL_X, CANCEL_Y, CANCEL_W, CANCEL_H);

        pinPad = new PinPad();
        username = Options.getUsername();
        if (username.length() < 3) changename = true;
        else changename = false;

        fileBrowser = new FileBrowser();
        fileBrowser.gocustom();
        fileBrowser.renderUpOne =false;

        fileTransferCommunicator = new FileTransferCommunicator();
        connect();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched() && backbutt.contains(in.getTouchpoint()))
        {
            if (fileTransferCommunicator.isNeedfile()) fileTransferCommunicator.cancelNeedfile();
            Assets.load_leveledit();
            Assets.load_navigation();
            game.setScreen(new LevelScreen2(game));
            this.dispose();
            //should probably dispose of network stuff
        }
        if (in.justTouched() && namechangebutt.contains(in.getTouchpoint()))
        {
            Gdx.app.debug("NameChange", "Presseed");
            changename = true;
        }

        if (fileTransferCommunicator.isNeedfile()) { //set by other thread
            getFile(); //UI thread, runs quickly
            message = "Pick a file to share";
        }
        else if (pinPad.isNeedpin()) { //set by other thread
            getPin(); // UI thread, runs quickly
            message = pinPad.getMessage();
        }
        else if (changename) { //set by this thread
            if (fileTransferCommunicator.isNeedfile()) fileTransferCommunicator.cancelNeedfile();
            changeName(); //UI, runs quickly
            message = "Name must be longer than 3 characters (a-Z, 0-9)";
        }
        else message = pinPad.getMessage();



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

    public void getFile()
    {
        for(int i=0; i<fileBrowser.levelbutts.length;i++)
        {
            if (in.justTouched() && fileBrowser.levelbutts[i].contains(touchpoint))
            {
                if (fileBrowser.levels.size() > i) //if you picked a valid choice
                {
                    fileTransferCommunicator.setThefile(fileBrowser.levels.get(i).name(), fileBrowser.levels.get(i).readString()); //this is the level
                    Gdx.app.debug("filebrowser", "file pressed");
                }
            }
        }
        if (in.justTouched() && fileBrowser.pagerightbutt.contains(touchpoint)) fileBrowser.pageRight();
        if (in.justTouched() && fileBrowser.pageleftbutt.contains(touchpoint)) fileBrowser.pageLeft();
    }

    public void changeName()
    {
        char tempChar;
        in.showKeyboard();
        tempChar = in.pollChar();
        if (tempChar != '\0') {

            username += tempChar;
        }
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
    
    public void connect()
    {
        dpClientConnection = new DPClientConnection(pinPad, fileTransferCommunicator);
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
        game.batch.draw(Assets.NavigationBack, backbutt.getX(), backbutt.getY());
        game.batch.draw(Assets.ShareChangeName, namechangebutt.getX(), namechangebutt.getY());
        game.batch.draw(Assets.ShareChangePIN, pinchangebutt.getX(), pinchangebutt.getY());
        if (changename)
        {
            game.batch.draw(Assets.NavigationConfirm, saveconfirmbutt.getX(), saveconfirmbutt.getY());
            game.batch.draw(Assets.NavigationCancel, cancelbutt.getX(), cancelbutt.getY());
        }
        Assets.font.draw(game.batch, username, USERNAME_X, USERNAME_Y);

        Assets.font.draw(game.batch, message, MESSAGE_X, MESSAGE_Y);
        game.batch.end();

        if (pinPad.isNeedpin()) pinPad.renderSpritesAndText(game.batch);
        if (fileTransferCommunicator.isNeedfile()) fileBrowser.renderSprites(game.batch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(backbutt.getX(), backbutt.getY(), backbutt.getWidth(), backbutt.getHeight());
        shapeRenderer.rect(namechangebutt.getX(), namechangebutt.getY(), namechangebutt.getWidth(), namechangebutt.getHeight());
        if (changename)
        {
            shapeRenderer.rect(saveconfirmbutt.getX(), saveconfirmbutt.getY(), saveconfirmbutt.getWidth(), saveconfirmbutt.getHeight());
            shapeRenderer.rect(cancelbutt.getX(), cancelbutt.getY(), cancelbutt.getWidth(), cancelbutt.getHeight());
        }

        shapeRenderer.end();

        if (pinPad.isNeedpin()) pinPad.renderShapes(shapeRenderer);
        if (fileTransferCommunicator.isNeedfile()) fileBrowser.renderShapes(shapeRenderer);


    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

}
