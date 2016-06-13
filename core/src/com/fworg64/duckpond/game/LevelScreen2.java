package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FloatFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fworg on 3/4/2016.
 */
public class LevelScreen2 extends ScreenAdapter
{
    public enum Direction {RIGHT, UP, LEFT, DOWN}; //CCW for magic
    public final static float SWYPE_ARROW_SCALE = 2.0f;//bigger fpr bigger?

    public static final int TOPBUTTONS_X = 90;
    public static final int TOPBUTTONS_Y = 1920-136;
    public static final int TOPBUTTONS_W = 255;
    public static final int TOPBUTTONS_H = 110;
    public static final int TOPBUTTONS_S = TOPBUTTONS_W + 100;

    public static final int LOWER_AREA_HEIGHT = 258;
    public static final int UPPER_AREA_HEIGHT = 358; //should add up to 308 + 308

    public static final int SHARE_X = 350;
    public static final int SHARE_Y = 1920-UPPER_AREA_HEIGHT;
    public static final int SHARE_W = 231;
    public static final int SHARE_H = 96;

    public static final int CHARACTER_BUTTON_X = 355;
    public static final int CHARACTER_BUTTON_Y = 1920 -1777;
    public static final int CHARACTER_BUTTON_S = 150;
    public static final int TRASH_X = 55;
    public static final int TRASH_Y = 1920-1777;
    public static final int CONFIRM_X = 800;
    public static final int CONFIRM_Y = 1920-1777;

    public static final int LOWER_AREA_BUTTON_W = 250;
    public static final int LOWER_AREA_BUTTON_H = 100;

    public static final int TSLIDER_X = 100;
    public static final int TSLIDER_W = 650;
    public static final int TSLIDER_H = 20;
    public static final int TSLIDER_Y = 50;
    public static final int TKNOB_W = 96;
    public static final int TKNOB_H = 96;

    public static final int DUCKLING_SELECT_X = 900;
    public static final int DUCKLING_SELECT_Y = 1920-485;
    public static final int DUCKLING_SELECT_S = 130;
    public static final int DUCKLING_SELECT_W = 120;
    public static final int DUCKLING_SELECT_H = 120;

    public static final int LEVEL_TIME_BUTTON_X = 55;
    public static final int LEVEL_TIME_BUTTON_Y = 1920-UPPER_AREA_HEIGHT;
    public static final int LEVEL_TIME_BUTTON_W = 40;
    public static final int LEVEL_TIME_BUTTON_H = 75;
    public static final int LEVEL_TIME_BUTTON_XS = 200;

    public static final int LEVEL_LIVES_BUTTON_X = 615;
    public static final int LEVEL_LIVES_BUTTON_Y = 1920-UPPER_AREA_HEIGHT;
    public static final int LEVEL_LIVES_BUTTON_W = 40;
    public static final int LEVEL_LIVES_BUTTON_H = 75;
    public static final int LEVEL_LIVES_BUTTON_XS = 380;

    public static final int MESSAGE_X = 20;
    public static final int MESSAGE_Y = 1920 - 175;
    public static final int MESSAGE2_X =375;
    public static final int MESSAGE3_X =700;

    public static final int LIVES_DISPLAY_X = 670;
    public static final int LIVES_DISPLAY_Y = 1920-UPPER_AREA_HEIGHT;
    public static final int LIVES_DISPLAY_S = 106;

    public static final int T_TIME_DISPLAY_X = 100;
    public static final int T_TIME_DISPLAY_Y = 1920-UPPER_AREA_HEIGHT + 60;
    public static final int C_TIME_DISPLAY_X = 800;
    public static final int C_TIME_DISPLAY_Y = 80;

    public static final int LOAD_CANCEL_X = 477;
    public static final int LOAD_CANCEL_Y = 277;
    public static final int LOAD_CANCEL_W = 126;
    public static final int LOAD_CANCEL_H = 126;
    public static final int SAVE_CANCEL_X = 297;
    public static final int SAVE_CANCEL_Y = 1920 - 500;
    public static final int SAVE_CANCEL_W = 126;
    public static final int SAVE_CANCEL_H = 126;
    public static final int SAVE_CONFIRM_X = 657;
    public static final int SAVE_CONFIRM_Y = 1920 - 500;
    public static final int SAVE_CONFIRM_W = 126;
    public static final int SAVE_CONFIRM_H = 126;

    public static final Vector2 EDITOR_OFFSET = new Vector2(219, 1920-1442);
    public static final float VELOCITY_INPUT_SCALE = .7f;
    public static final float SPRITE_ALPHA_BEFORE_SPAWN = .2f;
    public static final int MAX_DUCKLINGS = 10;
    public static final int MAX_LIVES =3;
    
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    InputListener in;
    Vector2 touchpoint;

    boolean defaultstate;
    boolean getPos;
    boolean getVel;
    boolean gettingT;
    boolean getT;
    boolean getD;
    boolean choiceDestroy;
    boolean globalTAdjust;
    boolean ready2confirm;

    Button savecancelbutt;
    Button loadcancelbutt;
    Button saveconfirmbutt;

    Button exitbutt;
    Button loadbutt;
    Button savebutt;
    Button sharebutt;
    Button trashbutt;
    Button TtimeUp;
    Button TtimeDown;
    Button LivesUp;
    Button LivesDown;
    Button Confirm;
    Button butts[];

    Array<Spawnable> spawnables;
    Spawnable tempguy;
    Vector2 temppos;
    float tempt2s;
    int tempducks;
    int time;
    int lives;

    String Message;
    String Message2;
    String Message3;
    Rectangle Tknob;
    Rectangle Tslider;

    Button[] ducklingNumber;

    Button ducks;
    Button sharks;
    Button lillies;

    Rectangle playarea;
    Rectangle placementarea;
    Rectangle upperarea;
    Rectangle lowerarea;

    FileHandle customDIR;
    FileHandle currfile;
    String filename;

    int halfobj = (int) (.5f*DuckPondGame.objWandH);
    Rectangle   goodleft   ;
    Rectangle   goodright  ;
    Rectangle   goodtop    ;
    Rectangle   goodbottom ;
    Rectangle[] goodouts   ;
    Rectangle   goodin     ;

    Browser browser;
    BrowsableFolder browsableFolder;
    BrowserCommunicator BC;


    private ShapeRenderer shapeRenderer;

    public LevelScreen2(DuckPondGame game)
    {
        //options has no effect on resolution
        Assets.load_navigation_high();
        Assets.load_font_high();
        this.game = game;
        gcam = new OrthographicCamera(DuckPondGame.highresScreenW,DuckPondGame.highresScreenH); //let us place things outside the map
        gcam.position.set(DuckPondGame.highresScreenW * .5f, DuckPondGame.highresScreenH * .5f, 0); //high res mode but assets at stdres for zoomout

        if (Gdx.app.getType() != Application.ApplicationType.WebGL)
        {
            customDIR = Gdx.files.local("CUSTOM");
            customDIR.mkdirs();
            //if (customDIR.isDirectory()) Gdx.app.debug("we shoold be", "guut");
        }
        
        spawnables = new Array<Spawnable>();
        tempguy = new Spawnable();
        temppos = new Vector2();
        tempt2s = 0;
        tempducks =-1;
        lives = 3;
        time = 30;

        playarea = new Rectangle(EDITOR_OFFSET.x, EDITOR_OFFSET.y, DuckPondGame.worldW, DuckPondGame.worldH);
        lowerarea = new Rectangle(0, 0, DuckPondGame.highresScreenW, LOWER_AREA_HEIGHT);
        upperarea = new Rectangle(0, 1920 - UPPER_AREA_HEIGHT, DuckPondGame.highresScreenW, UPPER_AREA_HEIGHT);
        placementarea = new Rectangle(0, LOWER_AREA_HEIGHT, DuckPondGame.highresScreenW, DuckPondGame.highresScreenH-LOWER_AREA_HEIGHT-UPPER_AREA_HEIGHT);
        ducks   = new Button(CHARACTER_BUTTON_X, CHARACTER_BUTTON_Y, DuckPondGame.objWandH, DuckPondGame.objWandH, Assets.LevelEditDuck);
        sharks  = new Button(CHARACTER_BUTTON_X + CHARACTER_BUTTON_S, CHARACTER_BUTTON_Y, DuckPondGame.objWandH, DuckPondGame.objWandH, Assets.LevelEditShark);
        lillies = new Button(CHARACTER_BUTTON_X + 2* CHARACTER_BUTTON_S, CHARACTER_BUTTON_Y, DuckPondGame.objWandH, DuckPondGame.objWandH, Assets.LevelEditLily);

        goodleft   = new Rectangle(placementarea.getX() + halfobj, placementarea.getY() + halfobj,playarea.getX() - 2*halfobj, placementarea.getHeight() - 2*halfobj);
        goodright  = new Rectangle(playarea.getX() + playarea.getWidth() + halfobj, placementarea.getY() + halfobj, goodleft.getWidth(), goodleft.getHeight());
        goodbottom = new Rectangle(goodleft.getX(), placementarea.getY() + halfobj, placementarea.getWidth() - 2*halfobj, playarea.getY()-placementarea.getY() - 2*halfobj);
        goodtop    = new Rectangle(goodleft.getX(), playarea.getY() + playarea.getHeight() + halfobj, goodbottom.getWidth(), goodbottom.getHeight());
        goodouts   = new Rectangle[]{goodbottom, goodleft, goodright, goodtop};
        goodin     = new Rectangle(playarea.getX() + halfobj, playarea.getY() + halfobj, playarea.getWidth() - 2*halfobj, playarea.getHeight() - 2*halfobj);

        exitbutt  = new Button(TOPBUTTONS_X + TOPBUTTONS_S*2, TOPBUTTONS_Y, TOPBUTTONS_W, TOPBUTTONS_H, Assets.LevelEditExit);
        loadbutt  = new Button(TOPBUTTONS_X + TOPBUTTONS_S, TOPBUTTONS_Y, TOPBUTTONS_W, TOPBUTTONS_H, Assets.LevelEditLOAD);
        savebutt  = new Button(TOPBUTTONS_X, TOPBUTTONS_Y, TOPBUTTONS_W, TOPBUTTONS_H, Assets.LevelEditSave);
        sharebutt = new Button(SHARE_X, SHARE_Y, SHARE_W, SHARE_H, Assets.LevelEditShare);
        Confirm   = new Button(CONFIRM_X, CONFIRM_Y, LOWER_AREA_BUTTON_W, LOWER_AREA_BUTTON_H, Assets.LevelEditConfirm);
        loadcancelbutt  = new Button(LOAD_CANCEL_X, LOAD_CANCEL_Y, LOAD_CANCEL_W, LOAD_CANCEL_H, Assets.NavigationCancel);
        savecancelbutt  = new Button(SAVE_CANCEL_X, SAVE_CANCEL_Y, SAVE_CANCEL_W, SAVE_CANCEL_H, Assets.NavigationCancel);
        saveconfirmbutt = new Button(SAVE_CONFIRM_X, SAVE_CONFIRM_Y, SAVE_CONFIRM_W, SAVE_CONFIRM_H, Assets.NavigationConfirm);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();
        Message = "";
        Message2 = "";
        Message3 = "";
        Tknob   = new Rectangle(TSLIDER_X - TKNOB_W*.5f,TSLIDER_Y + TSLIDER_H *.5f - TKNOB_H *.5f, TKNOB_W, TKNOB_H);
        Tslider = new Rectangle(TSLIDER_X, TSLIDER_Y, TSLIDER_W, TSLIDER_H);
        ducklingNumber = new Button[MAX_DUCKLINGS];
        for (int i =0; i<MAX_DUCKLINGS; i++){
            ducklingNumber[i] = new Button(DUCKLING_SELECT_X, DUCKLING_SELECT_Y - DUCKLING_SELECT_S*i,DUCKLING_SELECT_W, DUCKLING_SELECT_H , Assets.LevelEditNumblock);
            ducklingNumber[i].setButttext(Integer.toString(i));
            ducklingNumber[i].hide();
        }

        trashbutt   = new Button(TRASH_X,TRASH_Y, LOWER_AREA_BUTTON_W, LOWER_AREA_BUTTON_H, Assets.LevelEditRemoveItem);
        TtimeUp     = new Button(LEVEL_TIME_BUTTON_X + LEVEL_TIME_BUTTON_XS, LEVEL_TIME_BUTTON_Y, LEVEL_TIME_BUTTON_W, LEVEL_TIME_BUTTON_H, Assets.LevelEditFlechaDer);
        TtimeDown   = new Button(LEVEL_TIME_BUTTON_X, LEVEL_TIME_BUTTON_Y, LEVEL_TIME_BUTTON_W, LEVEL_TIME_BUTTON_H, Assets.LevelEditFlechaIzq);
        LivesUp     = new Button(LEVEL_LIVES_BUTTON_X + LEVEL_LIVES_BUTTON_XS, LEVEL_LIVES_BUTTON_Y, LEVEL_LIVES_BUTTON_W, LEVEL_LIVES_BUTTON_H, Assets.LevelEditFlechaDer);
        LivesDown   = new Button(LEVEL_LIVES_BUTTON_X, LEVEL_LIVES_BUTTON_Y, LEVEL_LIVES_BUTTON_W, LEVEL_LIVES_BUTTON_H, Assets.LevelEditFlechaIzq);

        butts = new Button[] {exitbutt, loadbutt, savebutt, sharebutt, trashbutt, TtimeDown, TtimeUp, LivesUp, LivesDown, ducks, sharks, lillies, Confirm, loadcancelbutt, savecancelbutt, saveconfirmbutt};

        saveconfirmbutt.hide();
        loadcancelbutt.hide();
        savecancelbutt.hide();

        defaultstate = true;
        getD = false;
        getT = false;
        gettingT = false;
        getVel = false;
        getPos = false;
        choiceDestroy = false;
        globalTAdjust = false;
        ready2confirm = false;

        filename ="";

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);


        if (Gdx.app.getType() == Application.ApplicationType.Android) this.game.adStateListener.HideBannerAd();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for (Button butt : butts) butt.pollPress(in.isTouched() ? touchpoint : new Vector2());
        if (exitbutt.isJustPressed())
        {
            Gdx.app.debug("screenstate", "exit");
            Options.loadOptions();
            in.hideKeyboard();
            Assets.load_levelscreen();
            Assets.load_navigation();
            Assets.load_font();
        }
        if (exitbutt.isWasPressed()) {

            game.setScreen(new LevelSelectionScreen(game));
            Assets.dispose_leveledit();
            this.dispose();
            exitbutt.pressHandled();
        }
        if (in.isBackPressed())
        {
            Gdx.app.debug("screenstate", "exit");
            Options.loadOptions();
            in.hideKeyboard();
            Assets.load_levelscreen();
            Assets.load_navigation();
            Assets.load_font();
            game.setScreen(new LevelSelectionScreen(game));
            Assets.dispose_leveledit();
            this.dispose();
            exitbutt.pressHandled();
        }
        if (savebutt.isJustPressed())
        {
            savecancelbutt.show();
            saveconfirmbutt.show();
        }
        if (savebutt.isWasPressed()) {
            if (defaultstate == true) {
                defaultstate = false;
                Gdx.app.debug("Save", "Going to save file");
            }
            //get a name
            char tempChar;
            if (Gdx.app.getType() != Application.ApplicationType.WebGL)
            {
                in.showKeyboard();
                tempChar = in.pollChar();
                if (tempChar != '\0') filename += tempChar;
                else if (in.backspaceJustPressed() && filename.length() >0) filename = filename.substring(0, filename.length() -1);

                Message = filename + "\n\n\n\n\n" + "Type a filename and press enter. (a-Z, 0-9)";

                if ((in.enterJustPressed() || saveconfirmbutt.isWasPressed()) && !filename.equals("ATTRIBUTES"))
                {
                    if (!filename.isEmpty())
                    {
                        currfile = Gdx.files.local(customDIR.path() + '/' + filename);
                        currfile.writeString(Integer.toString(time) + " " + Integer.toString(lives) + "\n", false);
                        for (Spawnable s : spawnables) {
                            currfile.writeString(s.toString() + '\n', true);
                        }
                    }
                    else Gdx.app.debug("Savefile", "Skipped due to empty name");

                    defaultstate = true;
                    in.hideKeyboard();
                    savebutt.pressHandled();
                    saveconfirmbutt.pressHandled();
                    savecancelbutt.hide();
                    saveconfirmbutt.hide();
                }

                if (savecancelbutt.isWasPressed())
                {
                    in.hideKeyboard();
                    defaultstate = true;
                    savebutt.pressHandled();
                    savecancelbutt.pressHandled();
                    saveconfirmbutt.hide();
                    savecancelbutt.hide();
                }
            }
            else
            {
                String temp =Integer.toString(time) + " " + Integer.toString(lives) + "\n";
                for (Spawnable s: spawnables)
                {
                    temp = temp + s.toString() + '\n';
                }

                Options.setCustom1(temp);
                Options.save();
                savebutt.pressHandled();
                savecancelbutt.hide();
                saveconfirmbutt.hide();
                defaultstate = true;
            }

        }
        if (loadbutt.isJustPressed())
        {
            loadcancelbutt.show();
            browsableFolder = new BrowsableFolder(DuckPondGame.customfolder, false);
            BC = new BrowserCommunicator();
            browser = new Browser(browsableFolder, BC, true);
            browser.start();
            browser.renderUpOne = false;
            defaultstate = false;
            Gdx.app.debug("Load", "Goind to load");
        }
        if (loadbutt.isWasPressed()) {
            BC.setTouchpoint(in.isTouched() ? touchpoint : new Vector2());
            if (BC.isSelectionMade()) {
                LoadFile(BC.getSelectionContents());
                BC.setClose(true);
                loadbutt.pressHandled();
                defaultstate =true;
                Tknob.setX(Tslider.x - Tknob.getWidth()*.5f);
                updateTempt2s();
                loadcancelbutt.hide();
            }
            if (loadcancelbutt.isWasPressed())
            {
                BC.setClose(true);
                defaultstate = true;
                loadbutt.pressHandled();
                loadcancelbutt.pressHandled();
                loadcancelbutt.hide();
            }
        }
        if (sharebutt.isWasPressed()){
            Gdx.app.debug("screenstate", "sharescreen");
            Options.loadOptions();
            Assets.load_share();
            Assets.load_navigation();
            Assets.load_font();
            game.setScreen(new ShareScreen2(game));
            Assets.dispose_leveledit();
            this.dispose();
            sharebutt.pressHandled();
        }
        if (trashbutt.isWasPressed()) {
            if (defaultstate){
                Gdx.app.debug("choicedestroy", "heatinup");
                choiceDestroy = true;
                defaultstate = false;
            }
            else DestroyCurrent();
            trashbutt.pressHandled();
        }
        TtimeUp.setAvailable(time<120);
        if (TtimeUp.isWasPressed()){
            time+=10;
            updateTempt2s();
            TtimeUp.pressHandled();
        }
        TtimeDown.setAvailable(time>20);
        if (TtimeDown.isWasPressed())
        {
            time-=10;
            updateTempt2s();
            TtimeDown.pressHandled();
        }
        LivesUp.setAvailable(lives<MAX_LIVES);
        if (LivesUp.isWasPressed())
        {
            lives++;
            LivesUp.pressHandled();
        }
        LivesDown.setAvailable(lives>1);
        if (LivesDown.isWasPressed())
        {
            lives--;
            LivesDown.pressHandled();
        }
        if (in.justTouched())
        {
            touchpoint.set(in.getTouchpoint());

            if (Tknob.contains(touchpoint) && defaultstate)
            {
                globalTAdjust = true;
                defaultstate = false;
            }
            Gdx.app.debug("Tocuh", touchpoint.toString());
        }
        Confirm.setAvailable(ready2confirm);
        if (choiceDestroy)
        {Choose2Destroy();}
        if (defaultstate)
        {ChooseActor();}
        if (getPos)
        {ChoosePos();}
        if (getVel)
        {ChooseVel();}
        if (getT)
        {ChooseT();}
        if (getD)
        {ChooseD();}
        if (globalTAdjust)
        {adjustGlobalT();}
    }

    @Override
    public void render(float delta) //this function gets called about 30 times a second automatically, delta is the time elapsed between calls
    {
        update();
        draw();
    }

    public void LoadFile(String filecontents)
    {
        //button pressed with file, load corresponidng file

        spawnables = new Array<Spawnable>();//first clear current file

            String levelstring = filecontents;

            ArrayList<String> levelcodes = new ArrayList<String>(Arrays.asList(levelstring.split("\n")));
            try
            {
                time = Integer.parseInt(levelcodes.get(0).split(" ")[0].trim());
                lives = Integer.parseInt(levelcodes.get(0).split(" ")[1].trim());
                levelcodes.remove(0);
                for (Iterator<String> iterator = levelcodes.iterator(); iterator.hasNext(); )
                {
                    String code = iterator.next();
                    Gdx.app.debug("code:", code);
                    String[] codelet = code.split(" ");

                    float tempstime = Float.parseFloat(codelet[0]);
                    Vector2 temppos = new Vector2();
                    Vector2 tempvel = new Vector2();
                    int tempducks = Integer.parseInt(codelet[4].trim());
                    temppos.fromString(codelet[2]);
                    tempvel.fromString(codelet[3]);

                    spawnables.add(new Spawnable(tempstime, temppos.cpy(), tempvel.cpy(), tempducks, codelet[1]));

                    iterator.remove();
                }
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                Gdx.app.debug("Error","Level File appers corrupt");
            }
    }

    public void DestroyCurrent()
    {
        Message = "Current temp\n destroyed.";
        tempguy = new Spawnable();
        temppos.setZero();
        getVel = false;
        getT = false;
        getPos = false;
        getD = false;
        defaultstate = true;
        ducks.setAvailable(true);
        sharks.setAvailable(true);
        lillies.setAvailable(true);
        loadbutt.setAvailable(true);
        savebutt.setAvailable(true);
        sharebutt.setAvailable(true);
        Confirm.setAvailable(false);
        //Gdx.app.debug("OH", "2");
    }

    public void Choose2Destroy()
    {
        Message = "Choose to remove";
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched())
        {
            for (Spawnable s: spawnables)
            {
                float del_X = s.getPos().x + EDITOR_OFFSET.x + s.getVel().x * (tempt2s-s.getTime2spawn() >0 ? tempt2s-s.getTime2spawn(): 0);
                float del_Y = s.getPos().y + EDITOR_OFFSET.y + s.getVel().y * (tempt2s-s.getTime2spawn() >0 ? tempt2s-s.getTime2spawn(): 0);
                if (touchpoint.x<(del_X + DuckPondGame.stdspriteW) && touchpoint.x > del_X) //x matches
                {
                    if (touchpoint.y<(del_Y + DuckPondGame.stdspriteH) && touchpoint.y > del_Y)//y matches
                    {
                        Message = s.getObjtype() + "\nDestroyed";
                        spawnables.removeValue(s,true);
                    }
                }

            }
        }
        if (in.justTouched()) {
            choiceDestroy = false;
            defaultstate = true;
            ducks.setAvailable(true);
            sharks.setAvailable(true);
            lillies.setAvailable(true);
            loadbutt.setAvailable(true);
            savebutt.setAvailable(true);
            sharebutt.setAvailable(true);
            Confirm.setAvailable(false);
        }
    }

    public void ChooseActor()
    {
        Message = "Choose an object to place";
        if (ducks.isWasPressed()) {
            tempguy.setObjtype("Duck");
            tempguy.setPos(new Vector2(placementarea.getX() + .5f*DuckPondGame.objWandH, placementarea.getY() + .5f*DuckPondGame.objWandH).sub(EDITOR_OFFSET));
            getPos = true;
            defaultstate = false;
            ducks.pressHandled();
            ducks.setAvailable(false);
            sharks.setAvailable(false);
            lillies.setAvailable(false);
            savebutt.setAvailable(false);
            loadbutt.setAvailable(false);
            sharebutt.setAvailable(false);
        }
        else if (sharks.isWasPressed()) {
            tempguy.setObjtype("Shark");
            tempguy.setPos(new Vector2(placementarea.getX() + .5f*DuckPondGame.objWandH, placementarea.getY() + .5f*DuckPondGame.objWandH).sub(EDITOR_OFFSET));
            getPos = true;
            defaultstate = false;
            sharks.pressHandled();
            ducks.setAvailable(false);
            sharks.setAvailable(false);
            lillies.setAvailable(false);
            savebutt.setAvailable(false);
            loadbutt.setAvailable(false);
            sharebutt.setAvailable(false);
        }
        else if (lillies.isWasPressed()) {
            tempguy.setObjtype("Lily");
            tempguy.setPos(new Vector2(playarea.getX() + .5f*DuckPondGame.objWandH, playarea.getY() + .5f*DuckPondGame.objWandH).sub(EDITOR_OFFSET));

            getPos = true;
            defaultstate = false;
            lillies.pressHandled();
            ducks.setAvailable(false);
            sharks.setAvailable(false);
            lillies.setAvailable(false);
            savebutt.setAvailable(false);
            loadbutt.setAvailable(false);
            sharebutt.setAvailable(false);
        }
    }

    public void ChoosePos()
    {
        TtimeDown.setAvailable(false);
        TtimeUp.setAvailable(false);
        LivesUp.setAvailable(false);
        LivesDown.setAvailable(false);
        Message = "Drag to position";
        Message2 = "";
        touchpoint.set(in.getTouchpoint());
        if (tempguy.getObjtype().equals("Invalid")) Gdx.app.debug("ChoosePos", "Given Invalid temp");

        if (in.isTouched() && placementarea.contains(touchpoint)) {
            ready2confirm = false;
            trashbutt.setAvailable(false);
            if (tempguy.getObjtype().equals("Shark") || tempguy.getObjtype().equals("Duck"))
            {
                //keep stuff out of play area
                for (Rectangle rect:goodouts)
                {
                    if (rect.contains(touchpoint)) {
                        tempguy.setPos(new Vector2(touchpoint.x - halfobj, touchpoint.y - halfobj).sub(EDITOR_OFFSET));
                        break;
                    }
                    else
                    {
                        Vector2 temmmp =new Vector2(tempguy.getPos().add(EDITOR_OFFSET.x + halfobj, EDITOR_OFFSET.y + halfobj));
                        if (goodbottom.contains(temmmp) || goodtop.contains(temmmp))
                        {
                            tempguy.setPos(new Vector2(touchpoint.x - EDITOR_OFFSET.x - halfobj, temmmp.y - EDITOR_OFFSET.y - halfobj));

                        }
                        else if (goodleft.contains(temmmp) || goodright.contains(temmmp))
                        {
                            tempguy.setPos(new Vector2(temmmp.x - EDITOR_OFFSET.x - halfobj, touchpoint.y - EDITOR_OFFSET.y - halfobj));

                        }
                    }
                }
            }
            else //lily
            {
                //keep stuff in play area
                if (goodin.contains(touchpoint)) {
                    tempguy.setPos(new Vector2(touchpoint.x - halfobj, touchpoint.y - halfobj).sub(EDITOR_OFFSET));
                    ready2confirm = true;
                }
                else
                {
                    if (touchpoint.x < goodin.getX()) tempguy.setPos(new Vector2(goodin.getX() - halfobj, touchpoint.y - halfobj).sub(EDITOR_OFFSET));
                    else if (touchpoint.x > goodin.getX() +goodin.getWidth()) tempguy.setPos(new Vector2(goodin.getX() + goodin.getWidth() - halfobj, touchpoint.y - halfobj).sub(EDITOR_OFFSET));
                    if (touchpoint.y < goodin.getY()) tempguy.setPos(new Vector2(touchpoint.x - halfobj, goodin.getY() - halfobj).sub(EDITOR_OFFSET));
                    else if (touchpoint.y > goodin.getY() + goodin.getHeight()) tempguy.setPos(new Vector2(touchpoint.x - halfobj, goodin.getY() + goodin.getHeight() - halfobj).sub(EDITOR_OFFSET));
                    if (touchpoint.x < goodin.getX())
                    {
                        if (touchpoint.y < goodin.getY()) tempguy.setPos(new Vector2(goodin.getX() - halfobj, goodin.getY() - halfobj).sub(EDITOR_OFFSET));
                        if (touchpoint.y > goodin.getY() + goodin.getHeight()) tempguy.setPos(new Vector2(goodin.getX() - halfobj, goodin.getY() + goodin.getHeight() - halfobj).sub(EDITOR_OFFSET));
                    }
                    else if (touchpoint.x > goodin.getX() + goodin.getWidth())
                    {
                        if (touchpoint.y < goodin.getY()) tempguy.setPos(new Vector2(goodin.getX() + goodin.getWidth() - halfobj, goodin.getY()-halfobj).sub(EDITOR_OFFSET));
                        if (touchpoint.y > goodin.getY() + goodin.getHeight()) tempguy.setPos(new Vector2(goodin.getX() + goodin.getWidth() - halfobj, goodin.getY() + goodin.getHeight()- halfobj).sub(EDITOR_OFFSET));
                    }
                }
            }
            Message = "x: " + (int)tempguy.getPos().x;
            Message2 = "y: "+ (int)tempguy.getPos().y;
        }
        else {
            ready2confirm = true;
            trashbutt.setAvailable(true);
        }
        if (ready2confirm && Confirm.isWasPressed())
        {
            getVel = true;
            if (tempguy.getObjtype().equals("Lily")) {getVel = false; getT = true;}
            getPos = false;
            ready2confirm = false;
            Confirm.pressHandled();
        }
    }

    public void ChooseVel()
    {
        if (tempguy.getVel().isZero()) Message = "Drag arrow to set Velocity";
        temppos.set(tempguy.getPos().x + DuckPondGame.objWandH *.5f, tempguy.getPos().y + DuckPondGame.objWandH *.5f);
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && placementarea.contains(touchpoint)) {
            tempguy.setVel(touchpoint.cpy().sub(EDITOR_OFFSET).sub(temppos).scl(VELOCITY_INPUT_SCALE).clamp(40, 300));
            Message = "x:"+(int)tempguy.getVel().x;
            Message2 = "y:"+(int)tempguy.getVel().y;
            Message3 = "Speed:" + (int)tempguy.getVel().len();
        }
        if (!in.isTouched() && !tempguy.getVel().isZero()) //vel was set
        {
            ready2confirm = true;
            Message = "x:"+(int)tempguy.getVel().x + "\n\n\nPress Confirm";
            Message2 = "y:"+(int)tempguy.getVel().y;
            Message3 = "Speed:" + (int)tempguy.getVel().len();
        }
        if (ready2confirm && Confirm.isWasPressed())
        {
            Message = "";
            Message2 = "";
            Message3 = "";
            getVel = false;
            getT = true;
            ready2confirm = false;
            Confirm.pressHandled();
        }
    }

    public void ChooseT()
    {
        updateTempt2s();
        if (!ready2confirm) Message = "Spawntime: Drag Slider";
        touchpoint.set(in.getTouchpoint());
        if (!in.isTouched()) ready2confirm = true;
        if (in.isTouched() && Tknob.contains(touchpoint)) gettingT = true;
        if (gettingT && in.isTouched())
        {
            slideKnob();
        }
        if (gettingT && !in.isTouched())
        {
            gettingT = false;
            ready2confirm = true;
            Message = "Spawntime: Press Confirm";
        }
        if (ready2confirm &&Confirm.isWasPressed())
        {
            if (tempguy.getObjtype().equals("Duck")) {getD = true; Gdx.app.debug("We", "a duck");}
            tempguy.setTime2spawn(tempt2s);
            if (!getD) {
                spawnables.add(tempguy);
                //Message = tempguy.toString();
                tempguy = new Spawnable();
                temppos.setZero();
                ducks.setAvailable(true);
                sharks.setAvailable(true);
                lillies.setAvailable(true);
                loadbutt.setAvailable(true);
                savebutt.setAvailable(true);
                sharebutt.setAvailable(true);
            }
            getT = false;
            ready2confirm = false;
            defaultstate = true;

            Confirm.pressHandled();
        }
    }

    public void adjustGlobalT()
    {
        updateTempt2s();
        Message = "Time:" + Float.toString(tempt2s) + " \\ " + Integer.toString(time);
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && Tknob.contains(touchpoint)) gettingT = true;
        if (gettingT && in.isTouched())
        {
            slideKnob();
        }
        if (gettingT && !in.isTouched())
        {
            gettingT = false;
            globalTAdjust = false;
            defaultstate = true;
        }
    }

    public void updateTempt2s() {
        tempt2s = (((Tknob.getX()+Tknob.getWidth()*.5f-Tslider.getX())/Tslider.getWidth()) * time);
        tempt2s = new BigDecimal(tempt2s).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    public void slideKnob()
    {
        Tknob.setX(touchpoint.x - Tknob.getWidth() *.5f);
        if (touchpoint.x< Tslider.x) Tknob.setX(Tslider.x - Tknob.getWidth()*.5f);
        if (touchpoint.x> (Tslider.x +Tslider.getWidth())) Tknob.setX(Tslider.x +Tslider.getWidth() - Tknob.getWidth()*.5f);
    }

    public void ChooseD()
    {
        for (Button butt: ducklingNumber) butt.show();
        if (tempducks ==-1) Message = "Select number of ducklings";
        else Message = Integer.toString(tempducks) + " ducklings, Press Confirm";
        touchpoint.set(in.getTouchpoint());
        for (Button butt: ducklingNumber) butt.pollPress(in.isTouched()? touchpoint:new Vector2());
        for (int i=0; i<MAX_DUCKLINGS; i++)
        {
            if (ducklingNumber[i].isWasPressed())
            {
                tempducks = i;
                ready2confirm = true;
                ducklingNumber[i].pressHandled();
            }
        }
        if (ready2confirm && Confirm.isWasPressed())
        {
            tempguy.setNumducks(tempducks);
            spawnables.add(tempguy);
            //Message = tempguy.toString();
            tempguy = new Spawnable();
            temppos.setZero();
            getD = false;
            defaultstate = true;
            tempducks =-1;
            Confirm.pressHandled();
            ducks.setAvailable(true);
            sharks.setAvailable(true);
            lillies.setAvailable(true);
            loadbutt.setAvailable(true);
            savebutt.setAvailable(true);
            sharebutt.setAvailable(true);
            for (Button butt: ducklingNumber) butt.hide();
        }
    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(.27451f, .70588f, .83922f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);


        game.batch.enableBlending();
        game.batch.begin();
        //drawbackgroundimagehere
        game.batch.draw(Assets.GameBackground, EDITOR_OFFSET.x, EDITOR_OFFSET.y);

        for (Spawnable s: spawnables)
        {
            if (s.getTime2spawn() <= tempt2s)
            {
                float render_X = s.getPos().x + EDITOR_OFFSET.x + s.getVel().x * (tempt2s-s.getTime2spawn());
                float render_Y = s.getPos().y + EDITOR_OFFSET.y + s.getVel().y * (tempt2s-s.getTime2spawn());
                if (s.getObjtype().equals("Shark"))
                {
                    Animation currAnim;
                    float ang = s.getVel().angle();
                    Sprite sprite;
                    Direction dir;
                    if (ang >=45 && ang <135) {currAnim = Assets.sharkSwimUpAnim; dir = Direction.UP;}
                    else if (ang >=135 &&  ang <225) {currAnim = Assets.sharkSwimLeftAnim; dir = Direction.LEFT;}
                    else if (ang >=225 && ang <315) {currAnim = Assets.sharkSwimDownAnim; dir = Direction.DOWN;}
                    else {currAnim = Assets.sharkSwimRightAnim; dir = Direction.RIGHT;}
                    sprite = new Sprite(currAnim.getKeyFrame(tempt2s));
                    sprite.setPosition(render_X, render_Y);
                    sprite.setOriginCenter();
                    if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() <90) sprite.setRotation(ang*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() >270) sprite.setRotation((ang-360)*.3f +360);
                    sprite.draw(game.batch);                }
                if (s.getObjtype().equals("Duck"))
                {
                    Animation currAnim;
                    float ang = s.getVel().angle();
                    Sprite sprite;
                    Direction dir;
                    if (ang >=45 && ang <135) {currAnim = Assets.swimUpAnim; dir = Direction.UP;}
                    else if (ang >=135 &&  ang <225) {currAnim = Assets.swimSideLeftAnim; dir = Direction.LEFT;}
                    else if (ang >=225 && ang <315) {currAnim = Assets.swimDownAnim; dir = Direction.DOWN;}
                    else {currAnim = Assets.swimSideRightAnim; dir = Direction.RIGHT;}
                    sprite = new Sprite(currAnim.getKeyFrame(tempt2s));
                    sprite.setPosition(render_X, render_Y);
                    sprite.setOriginCenter();
                    if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() <90) sprite.setRotation(ang*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() >270) sprite.setRotation((ang-360)*.3f +360);
                    sprite.draw(game.batch);
                    //duckling shit
                    Sprite ducklingsprite;
                    ducklingsprite = new Sprite(sprite);
                    ducklingsprite.setOriginCenter();
                    ducklingsprite.setScale(.5f);
                    for (int i =1; i<= s.getNumducks();i++)
                    {
                        if (s.getVel().len()*(tempt2s-s.getTime2spawn()) >= Duckling.ducklingDistConst * i)
                        {
                            Vector2 rendercoord = new Vector2(EDITOR_OFFSET.cpy().add(s.getPos()).add(s.getVel().cpy().scl(tempt2s - s.getTime2spawn())).sub(s.getVel().cpy().setLength(Duckling.ducklingDistConst * i)));
                            ducklingsprite.setPosition(rendercoord.x, rendercoord.y);
                            ducklingsprite.draw(game.batch);
                        }
                        else
                        {
                            ducklingsprite.setPosition(s.getPos().x + EDITOR_OFFSET.x, s.getPos().y + EDITOR_OFFSET.y);
                            ducklingsprite.draw(game.batch);
                        }
                    }
                }
                if (s.getObjtype().equals("Lily")) game.batch.draw(Assets.padRot.getKeyFrame(tempt2s), render_X, render_Y);
            }
            if (s.getTime2spawn() > tempt2s)
            {
                float render_X = s.getPos().x + EDITOR_OFFSET.x;
                float render_Y = s.getPos().y + EDITOR_OFFSET.y;
                if (s.getObjtype().equals("Shark"))
                {
                    Animation currAnim;
                    float ang = s.getVel().angle();
                    Sprite sprite;
                    Direction dir;
                    if (ang >=45 && ang <135) {currAnim = Assets.sharkSwimUpAnim; dir = Direction.UP;}
                    else if (ang >=135 &&  ang <225) {currAnim = Assets.sharkSwimLeftAnim; dir = Direction.LEFT;}
                    else if (ang >=225 && ang <315) {currAnim = Assets.sharkSwimDownAnim; dir = Direction.DOWN;}
                    else {currAnim = Assets.sharkSwimRightAnim; dir = Direction.RIGHT;}
                    sprite = new Sprite(currAnim.getKeyFrame(tempt2s));
                    sprite.setPosition(s.getPos().x + EDITOR_OFFSET.x, s.getPos().y + EDITOR_OFFSET.y);
                    sprite.setOriginCenter();
                    if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() <90) sprite.setRotation(ang*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() >270) sprite.setRotation((ang-360)*.3f +360);
                    sprite.setAlpha(SPRITE_ALPHA_BEFORE_SPAWN);
                    sprite.draw(game.batch);
                }
                if (s.getObjtype().equals("Duck"))
                {
                    Animation currAnim;
                    float ang = s.getVel().angle();
                    Sprite sprite;
                    Direction dir;
                    if (ang >=45 && ang <135) {currAnim = Assets.swimUpAnim; dir = Direction.UP;}
                    else if (ang >=135 &&  ang <225) {currAnim = Assets.swimSideLeftAnim; dir = Direction.LEFT;}
                    else if (ang >=225 && ang <315) {currAnim = Assets.swimDownAnim; dir = Direction.DOWN;}
                    else {currAnim = Assets.swimSideRightAnim; dir = Direction.RIGHT;}
                    sprite = new Sprite(currAnim.getKeyFrame(tempt2s));
                    sprite.setPosition(s.getPos().x + EDITOR_OFFSET.x, s.getPos().y + EDITOR_OFFSET.y);
                    sprite.setOriginCenter();
                    if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() <90) sprite.setRotation(ang*.3f);
                    if (dir == Direction.RIGHT && s.getVel().angle() >270) sprite.setRotation((ang-360)*.3f +360);
                    sprite.setAlpha(SPRITE_ALPHA_BEFORE_SPAWN);
                    sprite.draw(game.batch);
                }
                if (s.getObjtype().equals("Lily"))
                {
                    Sprite sprite;
                    sprite = new Sprite(Assets.padRot.getKeyFrame(tempt2s));
                    sprite.setPosition(s.getPos().x + EDITOR_OFFSET.x, s.getPos().y + EDITOR_OFFSET.y);
                    sprite.setAlpha(SPRITE_ALPHA_BEFORE_SPAWN);
                    sprite.draw(game.batch);
                }
            }
        }
        if (tempguy.getObjtype().equals("Shark"))
        {
            Animation currAnim;
            float ang = tempguy.getVel().angle();
            Sprite sprite;
            Direction dir;
            if (ang >=45 && ang <135) {currAnim = Assets.sharkSwimUpAnim; dir = Direction.UP;}
            else if (ang >=135 &&  ang <225) {currAnim = Assets.sharkSwimLeftAnim; dir = Direction.LEFT;}
            else if (ang >=225 && ang <315) {currAnim = Assets.sharkSwimDownAnim; dir = Direction.DOWN;}
            else {currAnim = Assets.sharkSwimRightAnim; dir = Direction.RIGHT;}
            sprite = new Sprite(currAnim.getKeyFrame(tempt2s));
            sprite.setPosition(tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
            sprite.setOriginCenter();
            if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
            if (dir == Direction.RIGHT && tempguy.getVel().angle() <90) sprite.setRotation(ang*.3f);
            if (dir == Direction.RIGHT && tempguy.getVel().angle() >270) sprite.setRotation((ang-360)*.3f +360);
            sprite.draw(game.batch);
        }
        else if (tempguy.getObjtype().equals("Duck"))
        {
            Animation currAnim;
            float ang = tempguy.getVel().angle();
            Sprite sprite;
            Direction dir;
            if (ang >=45 && ang <135) {currAnim = Assets.swimUpAnim; dir = Direction.UP;}
            else if (ang >=135 &&  ang <225) {currAnim = Assets.swimSideLeftAnim; dir = Direction.LEFT;}
            else if (ang >=225 && ang <315) {currAnim = Assets.swimDownAnim; dir = Direction.DOWN;}
            else {currAnim = Assets.swimSideRightAnim; dir = Direction.RIGHT;}
            sprite = new Sprite(currAnim.getKeyFrame(tempt2s));
            sprite.setPosition(tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
            sprite.setOriginCenter();
            if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
            if (dir == Direction.RIGHT && tempguy.getVel().angle() <90) sprite.setRotation(ang*.3f);
            if (dir == Direction.RIGHT && tempguy.getVel().angle() >270) sprite.setRotation((ang-360)*.3f +360);
            sprite.draw(game.batch);
        }
        else if (tempguy.getObjtype().equals("Lily")) game.batch.draw(Assets.padRot.getKeyFrame(tempt2s),tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);


        game.batch.draw(Assets.LevelEditMapaAbajo, lowerarea.getX(), lowerarea.getY(), lowerarea.getWidth(), lowerarea.getHeight());
        game.batch.draw(Assets.LevelEditMapaAbajo, upperarea.getX(), upperarea.getY(), upperarea.getWidth(), upperarea.getHeight());
        for (Button butt: butts) butt.renderSprites(game.batch);
        for (Button butt: ducklingNumber) butt.renderSprites(game.batch);

        game.batch.draw(Assets.LevelEditTimeBar, Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        game.batch.draw(Assets.LevelEditClock, Tknob.getX(), Tknob.getY());

        game.batch.draw(Assets.LevelEditUnlives, LIVES_DISPLAY_X + 2*LIVES_DISPLAY_S, LIVES_DISPLAY_Y);
        game.batch.draw(Assets.LevelEditUnlives, LIVES_DISPLAY_X + 1*LIVES_DISPLAY_S, LIVES_DISPLAY_Y);
        game.batch.draw(Assets.LevelEditUnlives, LIVES_DISPLAY_X, LIVES_DISPLAY_Y);
        switch (lives)
        {
            case 3:
                game.batch.draw(Assets.LevelEditLives,LIVES_DISPLAY_X + 2*LIVES_DISPLAY_S, LIVES_DISPLAY_Y);
            case 2:
                game.batch.draw(Assets.LevelEditLives,LIVES_DISPLAY_X + LIVES_DISPLAY_S, LIVES_DISPLAY_Y);
            case 1:
                game.batch.draw(Assets.LevelEditLives, LIVES_DISPLAY_X, LIVES_DISPLAY_Y);
            case 0:
                break;
        }

        Assets.font.draw(game.batch, Message, MESSAGE_X, MESSAGE_Y);
        Assets.font.draw(game.batch, Message2, MESSAGE2_X, MESSAGE_Y);
        Assets.font.draw(game.batch, Message3, MESSAGE3_X, MESSAGE_Y);
        Assets.font.draw(game.batch, Integer.toString(time), T_TIME_DISPLAY_X, T_TIME_DISPLAY_Y);
        Assets.font.draw(game.batch,Float.toString(tempt2s), C_TIME_DISPLAY_X, C_TIME_DISPLAY_Y);
        if (loadbutt.isWasPressed()) browser.renderSprites(game.batch);
        game.batch.end();


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
//        //draw detection bounds here for debugging
//        for(Button butt: butts) butt.renderShapes(shapeRenderer);
//        shapeRenderer.rect(Tknob.getX(), Tknob.getY(), Tknob.getWidth(), Tknob.getHeight());
//        shapeRenderer.rect(Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
//        //if (loadfile) for (Rectangle r: loadlevelbuttons) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
//
////        for (Rectangle rect : goodouts) shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
////        shapeRenderer.rect(goodin.getX(), goodin.getY(), goodin.getWidth(), goodin.getHeight());
//
//        //shapeRenderer.rect(placementarea.getX(), placementarea.getY(), placementarea.getWidth(), placementarea.getHeight());
//        //shapeRenderer.rect(playarea.getX(), playarea.getY(), playarea.getWidth(), playarea.getHeight());
//
//        shapeRenderer.end();

        if (getVel)
        {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Vector2[] swipedraw = new Vector2[4];
            Vector2 swipestart = tempguy.getPos().cpy().add(EDITOR_OFFSET).add(new Vector2(DuckPondGame.stdspriteW *.5f, DuckPondGame.stdspriteH * .5f));
            Vector2 swipeend = tempguy.getVel().cpy().add(tempguy.getPos()).add(EDITOR_OFFSET).add(new Vector2(DuckPondGame.stdspriteW *.5f, DuckPondGame.stdspriteH * .5f));
            swipedraw[0] = swipestart.cpy();
            swipedraw[3] = swipeend.cpy();
            Vector2 tmp = swipestart.cpy().lerp(swipeend, .875f); // point between begining and end
            Vector2 othertmp = tmp.cpy().sub(swipeend); //one of the wings, ish
            swipedraw[1] = new Vector2(tmp.x - othertmp.y, tmp.y + othertmp.x);
            swipedraw[2] = new Vector2(tmp.x + othertmp.y, tmp.y - othertmp.x);
            Vector2 thirdtemp = swipedraw[0].cpy().scl(SWYPE_ARROW_SCALE).sub(swipestart);
            for (int i=1;i<swipedraw.length;i++)
            {
                swipedraw[i].scl(SWYPE_ARROW_SCALE);
                swipedraw[i].sub(thirdtemp);
            }
            shapeRenderer.setColor(.2f, .5f, .5f, 1f);
            shapeRenderer.triangle(swipedraw[0].x, swipedraw[0].y, swipedraw[1].x, swipedraw[1].y, swipedraw[3].x, swipedraw[3].y);
            shapeRenderer.triangle(swipedraw[0].x, swipedraw[0].y, swipedraw[2].x, swipedraw[2].y, swipedraw[3].x, swipedraw[3].y);
            shapeRenderer.end();
            shapeRenderer.end();
        }

    }
}
