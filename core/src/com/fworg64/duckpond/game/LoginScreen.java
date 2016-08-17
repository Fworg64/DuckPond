package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 8/17/2016.
 */
public class LoginScreen extends ScreenAdapter {
    public enum PINSTATE {NEEDPIN1, NEEDPIN2, PINSNOTMATCH, PINSENT};
    PINSTATE pinstate;

    String username;
    String pin;
    String Message;

    int MESSAGE_X, MESSAGE_Y;
    int USERNAME_X, USERNAME_Y;
    int WELCOME_X, WELCOME_Y;

    String welcomeNameChange = "Welcome to Share, make a\n" +
            "username to upload levels";

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera

    InputListener in;
    Vector2 touchpoint;

    Button confirmnamechangebutt;
    Button cancelnamechangebutt;

    Button butts[];

    PinPad2 pp2;
    String temppin1;
    String temppin2;

    Thread networkMaker;
    DPUploadCommunicator DPU;
    DPUploader dpUploader;

    boolean needUsername;
    boolean firsttime;
    boolean showpinpad;

    boolean need2connect;

    LoginScreen(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        if (Options.isHighres())
        {
            confirmnamechangebutt = new Button(700,1920-800,126, 126, Assets.NavigationConfirm);
            cancelnamechangebutt = new Button(300,1920-800,126, 126, Assets.NavigationCancel);

            MESSAGE_X = 420;
            MESSAGE_Y = 1920 - 365;
            USERNAME_X = 420;
            USERNAME_Y = 1920 - 255;
            WELCOME_X = 20;
            WELCOME_Y = 1920 - 465;
        }
        else
        {
            confirmnamechangebutt = new Button(400,900-400,76, 76, Assets.NavigationConfirm);
            cancelnamechangebutt = new Button(150,900-400,76, 76, Assets.NavigationCancel);

            MESSAGE_X = 230;
            MESSAGE_Y = 960 - 200;
            USERNAME_X = 230;
            USERNAME_Y = 960 - 150;
            WELCOME_X = 10;
            WELCOME_Y = 960 - 280;
        }

        butts = new Button[] {confirmnamechangebutt, cancelnamechangebutt};

        DPU = new DPUploadCommunicator();

        pinstate = PINSTATE.NEEDPIN1;
        showpinpad = false;
        pp2 = new PinPad2();

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS)
        {
            Gdx.app.debug("AD","ShareScreenShow");
            this.game.adStateListener.ShowBannerAd();
        }

        need2connect = true;
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());

        if (need2connect)
        {
            need2connect =false;
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

            //say connecting until connected
            Message = "Connecting...";
            if(networkMaker.getState()==Thread.State.TERMINATED){
                if (DPU.getState() != DPUploadCommunicator.State.ERROR)
                {
                    Message = "Connected!";

                    dpUploader.start();
                }
                else
                {
                    Message = "Network Error";

                }

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
                Message = "Make a new PIN";
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
                //Message = "herp";
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
            //logged in successfully
//            showBrowser = true;
//            BC.setTouchpoint(in.isTouched() ? touchpoint : new Vector2());
//            if (BC.isSelectionMade())
//            {
//                DPU.setFile(BC.getSelectionName(), BC.getSelectionContents());
//                Message = BC.getSelectionName() + " Sent!";
//                BC.setResetSelection(true);
//            }
        }
        if (DPU.getState() == DPUploadCommunicator.State.ERROR)
        {
            if (Message != "Network Error") Message = "Network Error";
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
        if (firsttime) Assets.font.draw(game.batch, welcomeNameChange, WELCOME_X, WELCOME_Y);
        game.batch.end();
    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }
}
