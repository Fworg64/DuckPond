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
    public enum PINSTATE {NEEDPIN1, NEEDPIN2, PINSNOTMATCH, PINSENT};
    PINSTATE pinstate;

    int BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H;
    int MESSAGE_X, MESSAGE_Y;
    int USERNAME_X, USERNAME_Y;
    int CONFIRMNAMECHANGE_X, CONFIRMNAMECHANGE_Y, CONFIRMNAMECHANGE_W, CONFIRMNAMECHANGE_H;
    int CANCELNAMECHANGE_X, CANCELNAMECHANGE_Y, CANCELNAMECHANGE_W, CANCELNAMECHANGE_H;
    int CHANGEUSERNAME_X, CHANGEUSERNAME_Y, CHANGEUSERNAME_W, CHANGEUSERNAME_H;
    int CONNECT_X, CONNECT_Y, CONNECT_W, CONNECT_H;
    
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
    Button connectbutt;
    Button butts[];

    Thread networkMaker;
    DPUploadCommunicator DPU;
    DPUploader dpUploader;

    PinPad2 pp2;
    String temppin1;
    String temppin2;

    Browser browser;
    BrowsableFolder folder;
    BrowserCommunicator BC;
    boolean showBrowser;

    boolean needUsername;
    boolean showpinpad;

    public ShareScreen2(DuckPondGame game)
    {
        Assets.load_share(); //just the big ast

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

            CONNECT_X = 420;
            CONNECT_Y = 1920-1898;
            CONNECT_W = 300;
            CONNECT_H = 200;
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

            CONNECT_X = 280;
            CONNECT_Y = 960-935;
            CONNECT_W = 150;
            CONNECT_H = 100;
        }
        
        backbutt = new Button(BACKBUTT_X, BACKBUTT_Y, BACKBUTT_W, BACKBUTT_H, Assets.NavigationBack);
        confirmnamechangebutt = new Button(CONFIRMNAMECHANGE_X, CONFIRMNAMECHANGE_Y, CONFIRMNAMECHANGE_W, CONFIRMNAMECHANGE_H, Assets.NavigationConfirm);
        cancelnamechangebutt = new Button(CANCELNAMECHANGE_X, CANCELNAMECHANGE_Y, CANCELNAMECHANGE_W, CANCELNAMECHANGE_H, Assets.NavigationCancel);
        changeusername = new Button(CHANGEUSERNAME_X, CHANGEUSERNAME_Y, CHANGEUSERNAME_W, CHANGEUSERNAME_H, Assets.NavigationFolder);
        connectbutt = new Button(CONNECT_X, CONNECT_Y, CONNECT_W, CONNECT_H, Assets.NavigationFolder);
        butts = new Button[] {backbutt, confirmnamechangebutt, cancelnamechangebutt, changeusername, connectbutt};

        changeusername.setButttext("Change\nName");
        connectbutt.setButttext("Connect");
        connectbutt.hide();
        confirmnamechangebutt.hide();
        cancelnamechangebutt.hide();

        Message = "";
        username = Options.getUsername();
        pin = Options.getSavedPin();
        
        if (username.equals("")) needUsername = true;
        else connectbutt.show();

        DPU = new DPUploadCommunicator();

        pinstate = PINSTATE.NEEDPIN1;
        showpinpad = false;
        pp2 = new PinPad2();

        folder = new BrowsableFolder(DuckPondGame.customfolder, false);
        BC = new BrowserCommunicator();
        browser = new Browser(folder, BC);
        showBrowser = false;
        browser.start();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());
        if (backbutt.isJustPressed())
        {
            //loadleveledit
            DPU.setState(DPUploadCommunicator.State.CLOSE);
            Assets.load_leveledit();
        }
        if (backbutt.isWasPressed())
        {
            //go leveledit
            game.setScreen(new LevelScreen2(game));
            Assets.dispose_share(); //just the bigast
            this.dispose();
        }
        if (changeusername.isWasPressed()) {
            needUsername = true;
            changeusername.pressHandled();
        }
        if (needUsername) changeUsername();
        if (connectbutt.isJustPressed())
        {
            //make thread communicator
            //new thread make connection
            networkMaker = new Thread(new Runnable() {
                @Override
                public void run() {
                    Gdx.app.debug("dpUploaderMaker", "Starting");
                    dpUploader = new DPUploader(DPU, username);
                    Gdx.app.debug("dpUploaderMakerMaker", "Finished");
                }
            });
            networkMaker.start();
            //connectbutt.setAvailable(false);

        }
        if (connectbutt.isWasPressed())
        {
            //say connecting until connected
            Message = "Connecting...";
            if(networkMaker.getState()==Thread.State.TERMINATED){
                Message = "Connected!";
                connectbutt.hide();
                changeusername.hide();
                connectbutt.pressHandled();
                dpUploader.start();
            }
        }
        if (DPU.getState() == DPUploadCommunicator.State.NEEDNEWPIN)
        {
            //Gdx.app.debug("Share", "Need new Pin");
            showpinpad = true;
            pp2.pollPress(in.isTouched() ? touchpoint : new Vector2());
            if (pinstate == PINSTATE.NEEDPIN1)
            {
                //Gdx.app.debug("Share", "NeedPin1");
                Message = "Enter a PIN";
                if (pp2.isPinReady()) {
                    temppin1 = pp2.getPin();
                    Gdx.app.debug("Share, Pin1Got", temppin1);
                    pp2.resetPin();
                    pinstate = PINSTATE.NEEDPIN2;
                }
            }
            if (pinstate == PINSTATE.NEEDPIN2) {
                //Gdx.app.debug("Share", "NeedPin2");
                Message = "Please Confirm";
                if (pp2.isPinReady()) {
                    temppin2 = pp2.getPin();
                    Gdx.app.debug("Share, Pin2Got", temppin2);
                    pp2.resetPin();
                    if (temppin2.equals(temppin1)) {
                        DPU.sendPin(temppin2);
                        Options.setSavedPin(temppin2);
                        Options.save();
                        Gdx.app.debug("Share", "NewPinSent: " + temppin2);
                        pinstate = PINSTATE.PINSENT;
                    }
                    else pinstate = PINSTATE.PINSNOTMATCH;
                }
            }
            if (pinstate == PINSTATE.PINSNOTMATCH)
            {

                Message = "PINs did not match";
                if (pp2.isPinReady()) {
                    temppin1 = pp2.getPin();
                    pp2.resetPin();
                    pinstate = PINSTATE.NEEDPIN2;
                }
            }
            if (pinstate == PINSTATE.PINSENT)
            {
                Gdx.app.debug("Share", "PinSent");
                showpinpad = false;
                Message = "herp";
                //do nothing?
            }

        }
        if (DPU.getState() == DPUploadCommunicator.State.NEEDCURRPIN)
        {
            //Gdx.app.debug("Share", "Need curr Pin");
            if (pin.equals(""))
            {
                Message = "Enter Pin";
                showpinpad = true;
                pp2.pollPress(in.isTouched() ? touchpoint : new Vector2());
                if (pp2.isPinReady())
                {
                    Options.setSavedPin(pp2.getPin());
                    Options.save();
                    DPU.sendPin(pp2.getPin());
                    Gdx.app.debug("Share", "CurrPinSent: " + pp2.getPin());
                    pp2.resetPin();
                    showpinpad = false;
                }
            }
            else{
                DPU.sendPin(pin);
            }
        }
        if (DPU.getState() == DPUploadCommunicator.State.WRONGPIN)
        {
            //Gdx.app.debug("Share", "HELLLOOOOOOO!!!");
            Message = "Enter PIN.";
            showpinpad = true;
            pp2.pollPress(in.isTouched() ? touchpoint : new Vector2());
            if (pp2.isPinReady())
            {
                Options.setSavedPin(pp2.getPin());
                Options.save();
                DPU.sendPin(pp2.getPin());
                Gdx.app.debug("Share", "PinRESent: " + pp2.getPin());
                pp2.resetPin();
                showpinpad = false;
            }
        }
        if (DPU.getState() == DPUploadCommunicator.State.NEEDFILE)
        {
            //Gdx.app.debug("Share", "Send in the clowns, files whatever");
            showBrowser = true;
            BC.setTouchpoint(in.isTouched() ? touchpoint : new Vector2());
            if (BC.isSelectionMade())
            {
                DPU.setFile(BC.getSelectionName(), BC.getSelectionContents());
                BC.setResetSelection(true);
                Message = BC.getSelectionName() + " Sent!";
            }
        }
        if (DPU.getState() == DPUploadCommunicator.State.ERROR)
        {
            Message = "Network Error";
        }
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
        if (showpinpad) pp2.renderSprites(game.batch);
        if (showBrowser) browser.renderSprites(game.batch);
        game.batch.end();

    }
    
    @Override
    public void render(float delta)
    {
        update();
        draw();
    }

}
