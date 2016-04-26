package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FloatFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by fworg on 3/4/2016.
 */
public class LevelScreen2 extends ScreenAdapter
{
    public static final int TOPBUTTONS_X = 90;
    public static final int TOPBUTTONS_Y = 1920-136;
    public static final int TOPBUTTONS_W = 200;
    public static final int TOPBUTTONS_H = 110;
    public static final int TOPBUTTONS_S = TOPBUTTONS_W + 100;

    public static final int LOWER_AREA_HEIGHT = 308;
    public static final int UPPER_AREA_HEIGHT = 308; //should add up to 308 + 308

    public static final int CHARACTER_BUTTON_X = 55;
    public static final int CHARACTER_BUTTON_Y = 1920 -1737;
    public static final int CHARACTER_BUTTON_S = 150;
    public static final int TRASH_AND_CONFIRM_X = 800;
    public static final int CONFIRM_Y = 50;

    public static final int LOWER_AREA_BUTTON_W = 250;
    public static final int LOWER_AREA_BUTTON_H = 100;

    public static final int TSLIDER_X = 100;
    public static final int TSLIDER_W = 650;
    public static final int TSLIDER_H = 20;
    public static final int TSLIDER_Y = CONFIRM_Y + LOWER_AREA_BUTTON_H/2 - TSLIDER_H/2;
    public static final int TKNOB_W = 96;
    public static final int TKNOB_H = 96;

    public static final int DUCKLING_SELECT_X = 918;
    public static final int DUCKLING_SELECT_Y = 1920-350;
    public static final int DUCKLING_SELECT_S = 110;
    public static final int DUCKLING_SELECT_W = 96;
    public static final int DUCKLING_SELECT_H = 96;

    public static final int LEVEL_LOAD_X = 200;
    public static final int LEVEL_LOAD_Y = 1920-550;
    public static final int LEVEL_LOAD_W = 200;
    public static final int LEVEL_LOAD_H = 200;
    public static final int LEVEL_LOAD_XS = 400;
    public static final int LEVEL_LOAD_YS = 300;
    public static final int LEVEL_LOAD_R = 3;
    public static final int LEVEL_LOAD_C = 2;

    public static final int LEVEL_TIME_AND_LIVES_BUTTON_X = 515;
    public static final int LEVEL_TIME_AND_LIVES_BUTTON_Y = 1920-1750;
    public static final int LEVEL_TIME_AND_LIVES_BUTTON_W = 96;
    public static final int LEVEL_TIME_AND_LIVES_BUTTON_H = 60;
    public static final int LEVEL_TIME_AND_LIVES_BUTTON_XS = 150;
    public static final int LEVEL_TIME_AND_LIVES_BUTTON_YS = 80;

    public static final int LIVES_DISPLAY_X = 725;
    public static final int LIVES_DISPLAY_Y = 1920-UPPER_AREA_HEIGHT;
    public static final int LIVES_DISPLAY_S = 106;

    public static final int TIME_DISPLAY_X = 700;
    public static final int TIME_DISPLAY_Y = 1780;

    public static final Vector2 EDITOR_OFFSET = new Vector2(219, 1920-1392);
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
    boolean savefile;
    boolean loadfile;

    boolean wasHighres;
    Rectangle exitbutt;
    Rectangle loadbutt;
    Rectangle savebutt;

    Array<Spawnable> spawnables;
    Spawnable tempguy;
    Vector2 temppos;
    Vector2 tempvel;
    float tempt2s;
    int tempducks;
    int time;
    int lives;

    String Message;
    Rectangle Tknob;
    Rectangle Tslider;
    Rectangle Confirm;
    Rectangle trashbutt;
    Rectangle[] ducklingNumber;
    Rectangle[] loadlevelbuttons;
    Rectangle TtimeUp;
    Rectangle TtimeDown;
    Rectangle LivesUp;
    Rectangle LivesDown;

    Rectangle ducks;
    Rectangle sharks;
    Rectangle lillies;

    Rectangle playarea;
    Rectangle placementarea;
    Rectangle upperarea;
    Rectangle lowerarea;

    Sprite duckSprite;
    Sprite sharkSprite;

    FileHandle customDIR;
    FileHandle currfile;
    String filename;
    FileHandle[] customfiles;

    private ShapeRenderer shapeRenderer;

    public LevelScreen2(DuckPondGame game)
    {
        wasHighres = Options.isHighres();
        Options.setStdres();
        Assets.levelEditLoad();
        this.game = game;
        gcam = new OrthographicCamera(DuckPondGame.highresScreenW,DuckPondGame.highresScreenH); //let us place things outside the map
        gcam.position.set(DuckPondGame.highresScreenW * .5f, DuckPondGame.highresScreenH * .5f, 0); //high res mode but assets at stdres for zoomout

        if (Gdx.app.getType() != Application.ApplicationType.WebGL)
        {
            customDIR = Gdx.files.local("LEVELS\\CUSTOM\\");
            customfiles = customDIR.list();
        }
        
        spawnables = new Array<Spawnable>();
        tempguy = new Spawnable();
        temppos = new Vector2();
        tempvel = new Vector2();
        tempt2s = 0;
        tempducks =0;
        lives = 3;
        time = 30;

        playarea = new Rectangle(EDITOR_OFFSET.x, EDITOR_OFFSET.y, DuckPondGame.worldW, DuckPondGame.worldH);
        lowerarea = new Rectangle(0, 0, DuckPondGame.highresScreenW, LOWER_AREA_HEIGHT);
        placementarea = new Rectangle(0, LOWER_AREA_HEIGHT, DuckPondGame.highresScreenW, DuckPondGame.highresScreenH-LOWER_AREA_HEIGHT-UPPER_AREA_HEIGHT);
        ducks = new Rectangle(CHARACTER_BUTTON_X, CHARACTER_BUTTON_Y, DuckPondGame.objWandH, DuckPondGame.objWandH);
        sharks = new Rectangle(CHARACTER_BUTTON_X + CHARACTER_BUTTON_S, CHARACTER_BUTTON_Y, DuckPondGame.objWandH, DuckPondGame.objWandH);
        lillies = new Rectangle(CHARACTER_BUTTON_X + 2* CHARACTER_BUTTON_S, CHARACTER_BUTTON_Y, DuckPondGame.objWandH, DuckPondGame.objWandH);

        exitbutt = new Rectangle(TOPBUTTONS_X + TOPBUTTONS_S*2, TOPBUTTONS_Y, TOPBUTTONS_W, TOPBUTTONS_H);
        loadbutt = new Rectangle(TOPBUTTONS_X + TOPBUTTONS_S, TOPBUTTONS_Y, TOPBUTTONS_W, TOPBUTTONS_H);
        savebutt = new Rectangle(TOPBUTTONS_X, TOPBUTTONS_Y, TOPBUTTONS_W, TOPBUTTONS_H);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();
        Message = "heerp";
        Tknob = new Rectangle(TSLIDER_X - TKNOB_W*.5f,TSLIDER_Y + TSLIDER_H *.5f - TKNOB_H *.5f, TKNOB_W, TKNOB_H);
        Tslider = new Rectangle(TSLIDER_X, TSLIDER_Y, TSLIDER_W, TSLIDER_H);
        Confirm = new Rectangle(TRASH_AND_CONFIRM_X, CONFIRM_Y, LOWER_AREA_BUTTON_W, LOWER_AREA_BUTTON_H);
        ducklingNumber = new Rectangle[MAX_DUCKLINGS];
        for (int i =0; i<MAX_DUCKLINGS; i++){ ducklingNumber[i] = new Rectangle(DUCKLING_SELECT_X, DUCKLING_SELECT_Y - DUCKLING_SELECT_S*i,DUCKLING_SELECT_W, DUCKLING_SELECT_H );}
        loadlevelbuttons = new Rectangle[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
                loadlevelbuttons[i*(LEVEL_LOAD_R) + j] = new Rectangle(LEVEL_LOAD_X + i*LEVEL_LOAD_XS, LEVEL_LOAD_Y - j*LEVEL_LOAD_YS, LEVEL_LOAD_W, LEVEL_LOAD_H);
            }
        }
        trashbutt = new Rectangle(TRASH_AND_CONFIRM_X,CHARACTER_BUTTON_Y, LOWER_AREA_BUTTON_W, LOWER_AREA_BUTTON_H);
        TtimeUp = new Rectangle(LEVEL_TIME_AND_LIVES_BUTTON_X, LEVEL_TIME_AND_LIVES_BUTTON_Y + LEVEL_TIME_AND_LIVES_BUTTON_YS, LEVEL_TIME_AND_LIVES_BUTTON_W, LEVEL_TIME_AND_LIVES_BUTTON_H);
        TtimeDown = new Rectangle(LEVEL_TIME_AND_LIVES_BUTTON_X, LEVEL_TIME_AND_LIVES_BUTTON_Y, LEVEL_TIME_AND_LIVES_BUTTON_W, LEVEL_TIME_AND_LIVES_BUTTON_H);
        LivesUp = new Rectangle(LEVEL_TIME_AND_LIVES_BUTTON_X +LEVEL_TIME_AND_LIVES_BUTTON_XS, LEVEL_TIME_AND_LIVES_BUTTON_Y + LEVEL_TIME_AND_LIVES_BUTTON_YS, LEVEL_TIME_AND_LIVES_BUTTON_W, LEVEL_TIME_AND_LIVES_BUTTON_H);
        LivesDown = new Rectangle(LEVEL_TIME_AND_LIVES_BUTTON_X + LEVEL_TIME_AND_LIVES_BUTTON_XS, LEVEL_TIME_AND_LIVES_BUTTON_Y, LEVEL_TIME_AND_LIVES_BUTTON_W, LEVEL_TIME_AND_LIVES_BUTTON_H);

        defaultstate = true;
        getD = false;
        getT = false;
        gettingT = false;
        getVel = false;
        getPos = false;
        choiceDestroy = false;
        globalTAdjust = false;
        ready2confirm = false;
        savefile = false;

        filename ="";

        duckSprite = new Sprite(Assets.LevelEditDuck);
        sharkSprite = new Sprite(Assets.LevelEditShark);

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        if (Gdx.app.getType() == Application.ApplicationType.Android) this.game.adStateListener.HideBannerAd();
    }

    public int update()
    {
        if (in.justTouched())
        {
            touchpoint.set(in.getTouchpoint());
            if (exitbutt.contains(touchpoint)) return 1;
            if (savebutt.contains(touchpoint))
            {
                if (defaultstate == true)
                {
                    savefile = true;
                    defaultstate = false;
                }

            }
            if (loadbutt.contains(touchpoint))
            {
                loadfile = true;
                defaultstate = false;
            }
            if (trashbutt.contains(touchpoint))
            {
                if (defaultstate){
                    Gdx.app.debug("choicedestroy", "heatinup");
                    choiceDestroy = true;
                    defaultstate = false;
                }
                else DestroyCurrent();

            }
            if (TtimeUp.contains(touchpoint) && time < 120) {time+=30; updateTempt2s();}
            if (TtimeDown.contains(touchpoint) && time>30) {time-=30; updateTempt2s();}
            if (LivesUp.contains(touchpoint) && lives <MAX_LIVES) lives++;
            if (LivesDown.contains(touchpoint) && lives >1) lives--;
            if (Tknob.contains(touchpoint) && defaultstate)
            {
                globalTAdjust = true;
                defaultstate = false;
            }
            Gdx.app.debug("Tocuh", touchpoint.toString());
        }
        if (in.isBackPressed()) return 1;
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
        if (savefile)
        {savefile();}
        if (loadfile)
        {LoadFile();}

        return 0;
    }

    @Override
    public void render(float delta) //this function gets called about 30 times a second automatically, delta is the time elapsed between calls
    {
        switch (update())
        {
            case 0:
                break;
            case 1:
                Gdx.app.debug("screenstate", "exit");
                this.dispose();
                Options.loadOptions();
                if (wasHighres) Options.setHighres();
                else Options.setStdres();
                game.setScreen(new LevelSelectionScreen(game));
                break;
        }
        draw();
    }

    public void savefile()
    {
        //get a name
        char tempChar;
        if (Gdx.app.getType() != Application.ApplicationType.WebGL)
        {
            in.showKeyboard();
            tempChar = in.pollChar();
            if (tempChar != '\0') filename += tempChar;
            else if (in.backspaceJustPressed() && filename.length() >0) filename = filename.substring(0, filename.length() -1);

            Message = filename + '\n' + "Type a filename and press enter. (a-Z, 0-9)";

            if (in.enterJustPressed())
            {
                if (!filename.isEmpty())
                {
                    currfile = Gdx.files.local(customDIR.path() + '\\' + filename);
                    currfile.writeString(Integer.toString(time) + " " + Integer.toString(lives) + "\n", false);
                    for (Spawnable s : spawnables) {
                        currfile.writeString(s.toString() + '\n', true);
                    }
                }

                savefile = false;
                defaultstate = true;
                in.hideKeyboard();
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
        }
    }

    public void LoadFile()
    {
        int buttpressed =-1;//no button pressed
        touchpoint.set(in.getTouchpoint());
        for (int i =0; i<loadlevelbuttons.length;i++)
        {
            if (loadlevelbuttons[i].contains(touchpoint) && in.justTouched()){
                buttpressed = i;
            }
        }
        if (buttpressed >=0 && buttpressed <loadlevelbuttons.length)
        {
            //button pressed, load corresponidng file
            //first clear current file

            spawnables = new Array<Spawnable>();
            if (buttpressed < customfiles.length)
            {
                String levelstring = customfiles[buttpressed].readString();

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

            loadfile = false;
            defaultstate = true;
        }
    }

    public void DestroyCurrent()
    {
        Message = "Current temp\n destroyed.";
        tempguy = new Spawnable();
        temppos.setZero();
        tempvel.setZero();
        getVel = false;
        getT = false;
        getPos = false;
        getD = false;
        defaultstate = true;
        //Gdx.app.debug("OH", "2");
    }

    public void Choose2Destroy()
    {
        Message = "choose to destroy";
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched())
        {
            for (Spawnable s: spawnables)
            {
                    float del_X = s.getPos().x + EDITOR_OFFSET.x + s.getVel().x * (tempt2s-s.getTime2spawn());
                    float del_Y = s.getPos().y + EDITOR_OFFSET.y + s.getVel().y * (tempt2s-s.getTime2spawn());
                    if (touchpoint.x<(del_X + Options.spriteWidth) && touchpoint.x > del_X) //x matches
                    {
                        if (touchpoint.y<(del_Y + Options.spriteHeight) && touchpoint.y > del_Y)//y matches
                        {
                            Message = s.getObjtype() + "\nDestroyed";
                            spawnables.removeValue(s,true);
                        }
                    }
            }
        }
        if (in.justTouched() && !trashbutt.contains(touchpoint)) {
            choiceDestroy = false;
            defaultstate = true;
        }
    }

    public void ChooseActor()
    {
        Message = "Choose an object to place";
        if (in.justTouched() && ducks.contains(touchpoint)) {
            tempguy.setObjtype("Duck");
            getPos = true;
            defaultstate = false;
        }
        else if (in.justTouched() && sharks.contains(touchpoint)) {
            tempguy.setObjtype("Shark");
            getPos = true;
            defaultstate = false;
        }
        else if (in.justTouched() && lillies.contains(touchpoint)) {
            tempguy.setObjtype("Lily");
            getPos = true;
            defaultstate = false;
        }
    }

    public void ChoosePos()
    {
        Message = "Drag to position";
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && !tempguy.getObjtype().equals("Invalid")&& placementarea.contains(touchpoint)) {
            float tempx;
            if (touchpoint.x -DuckPondGame.objWandH *.5f <= 0) {tempx = DuckPondGame.objWandH *.5f;}
            else if (touchpoint.x +DuckPondGame.objWandH *.5f >= placementarea.getWidth()) {tempx = placementarea.getWidth() - DuckPondGame.objWandH *.5f;}
            else tempx = touchpoint.x;
            float tempy;
            if (touchpoint.y -DuckPondGame.objWandH *.5f <= placementarea.getY()) tempy = DuckPondGame.objWandH *.5f + placementarea.getY();
            else if (touchpoint.y +DuckPondGame.objWandH *.5f >= placementarea.getHeight() + placementarea.getY()) tempy = placementarea.getHeight()+placementarea.getY() - DuckPondGame.objWandH *.5f;
            else tempy = touchpoint.y;

            tempguy.setPos(new Vector2(tempx - .5f * DuckPondGame.objWandH, tempy - .5f * DuckPondGame.objWandH).sub(EDITOR_OFFSET));
            Message = tempguy.getPos().toString();
        }
        if (!in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
            Rectangle tempprint = new Rectangle(tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y +EDITOR_OFFSET.y, DuckPondGame.objWandH, DuckPondGame.objWandH);

            if ((tempguy.getObjtype().equals("Duck") || tempguy.getObjtype().equals("Shark")) && tempprint.overlaps(playarea))
            {
                Message = "Sharks and ducks must start outside viewing area";
            }
            else if ((tempguy.getObjtype().equals("Lily")) && !playarea.contains(tempprint))
            {
                Message = "Lilies must be placed in play area";
            }
            else {
                ready2confirm = true;
                Message = "Press confirm when you like the position";
            }
        }
        if (ready2confirm && Confirm.contains(touchpoint) && in.justTouched())
        {
            getVel = true;
            if (tempguy.getObjtype().equals("Lily")) {getVel = false; getT = true;}
            getPos = false;
            ready2confirm = false;
        }
    }

    public void ChooseVel()
    {
        Message = "Drag arrow to set Velocity";
        temppos.set(tempguy.getPos().x + DuckPondGame.objWandH *.5f, tempguy.getPos().y + DuckPondGame.objWandH *.5f);
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && placementarea.contains(touchpoint)) {
            tempvel.set(touchpoint.cpy().sub(EDITOR_OFFSET).sub(temppos).scl(VELOCITY_INPUT_SCALE).clamp(40, 200));
            Message = tempvel.toString() + "\nSpeed:" + Float.toString(tempvel.len());
        }
        if (!in.isTouched() && !tempvel.isZero()) //vel was set
        {
            ready2confirm = true;
            Message = "Press confirm when Velocity is set";
        }
        if (ready2confirm && Confirm.contains(touchpoint) && in.justTouched())
        {
            tempguy.setVel(tempvel.cpy());
            getVel = false;
            getT = true;
            ready2confirm = false;
        }
    }

    public void ChooseT()
    {
        updateTempt2s();
        Message = "Drag slider and press confirm for SpawnTime: " + Float.toString(tempt2s);
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
        }
        if (ready2confirm && in.justTouched() && Confirm.contains(touchpoint))
        {
            if (tempguy.getObjtype().equals("Duck")) {getD = true; Gdx.app.debug("We", "a duck");}
            tempguy.setTime2spawn(tempt2s);
            if (!getD) {
                spawnables.add(tempguy);
                Message = tempguy.toString();
                tempguy = new Spawnable();
                temppos.setZero();
                tempvel.setZero();
            }
            getT = false;
            ready2confirm = false;
            defaultstate = true;
        }
    }

    public void adjustGlobalT()
    {
        updateTempt2s();
        Message = "Time:" + Float.toString(tempt2s);
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

    public void updateTempt2s() {tempt2s = ((Tknob.getX()+Tknob.getWidth()*.5f-Tslider.getX())/Tslider.getWidth()) * time;}

    public void slideKnob()
    {
        Tknob.setX(touchpoint.x - Tknob.getWidth() *.5f);
        if (touchpoint.x< Tslider.x) Tknob.setX(Tslider.x - Tknob.getWidth()*.5f);
        if (touchpoint.x> (Tslider.x +Tslider.getWidth())) Tknob.setX(Tslider.x +Tslider.getWidth() - Tknob.getWidth()*.5f);
    }

    public void ChooseD()
    {
        if (tempducks ==0) Message = "Select number of ducklings";
        else Message = Integer.toString(tempducks) + " ducklings. Press Confirm to confirm";
        touchpoint.set(in.getTouchpoint());
        for (int i=0; i<MAX_DUCKLINGS; i++)
        {
            if (in.justTouched() && ducklingNumber[i].contains(touchpoint))
            {
                tempducks = i;
                ready2confirm = true;
            }
        }
        if (ready2confirm && in.justTouched() && Confirm.contains(touchpoint))
        {
            tempguy.setNumducks(tempducks);
            spawnables.add(tempguy);
            Message = tempguy.toString();
            tempguy = new Spawnable();
            temppos.setZero();
            tempvel.setZero();
            getD = false;
            defaultstate = true;
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
        //draw background image here
        game.batch.draw(Assets.LevelEditMapa, 0, 0);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.LevelEditConfirm, Confirm.getX(), Confirm.getY());
        game.batch.draw(Assets.LevelEditSave, savebutt.getX(), savebutt.getY());
        game.batch.draw(Assets.LevelEditLOAD, loadbutt.getX(), loadbutt.getY());
        game.batch.draw(Assets.LevelEditExit, exitbutt.getX(), exitbutt.getY());
        game.batch.draw(Assets.LevelEditDuck, ducks.getX(), ducks.getY());
        game.batch.draw(Assets.LevelEditShark, sharks.getX(), sharks.getY());
        game.batch.draw(Assets.LevelEditLily, lillies.getX(), lillies.getY());
        if (tempguy.getObjtype().equals("Shark")) game.batch.draw(Assets.LevelEditShark,tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
        else if (tempguy.getObjtype().equals("Duck")) game.batch.draw(Assets.LevelEditDuck,tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
        else if (tempguy.getObjtype().equals("Lily")) game.batch.draw(Assets.LevelEditLily,tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
        for (Spawnable s: spawnables)
        {
            if (s.getTime2spawn() <= tempt2s)
            {
                float render_X = s.getPos().x + EDITOR_OFFSET.x + s.getVel().x * (tempt2s-s.getTime2spawn());
                float render_Y = s.getPos().y + EDITOR_OFFSET.y + s.getVel().y * (tempt2s-s.getTime2spawn());
                if (s.getObjtype().equals("Shark")) game.batch.draw(Assets.LevelEditShark, render_X, render_Y);
                if (s.getObjtype().equals("Duck")) game.batch.draw(Assets.LevelEditDuck, render_X, render_Y);
                if (s.getObjtype().equals("Lily")) game.batch.draw(Assets.LevelEditLily, render_X, render_Y);
            }
            if (s.getTime2spawn() > tempt2s)
            {
                float render_X = s.getPos().x + EDITOR_OFFSET.x;
                float render_Y = s.getPos().y + EDITOR_OFFSET.y;
                if (s.getObjtype().equals("Shark"))
                {
                    sharkSprite.setPosition(render_X, render_Y);
                    sharkSprite.setAlpha(SPRITE_ALPHA_BEFORE_SPAWN);
                    sharkSprite.draw(game.batch);
                }
                if (s.getObjtype().equals("Duck"))
                {
                    duckSprite.setPosition(render_X, render_Y);
                    duckSprite.setAlpha(SPRITE_ALPHA_BEFORE_SPAWN);
                    duckSprite.draw(game.batch);
                }
            }
        }
        game.batch.draw(Assets.LevelEditTimeBar, Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        game.batch.draw(Assets.LevelEditClock, Tknob.getX(), Tknob.getY());

        game.batch.draw(Assets.LevelEditRemoveItem, trashbutt.getX(), trashbutt.getY());
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

        Assets.font.draw(game.batch, Message, .1f * gcam.viewportWidth, .9f * gcam.viewportHeight);
        Assets.font.draw(game.batch, "Total Time: " + Integer.toString(time), TIME_DISPLAY_X, TIME_DISPLAY_Y);
        Assets.font.draw(game.batch, "curr Time: " + Float.toString(tempt2s), TIME_DISPLAY_X, TIME_DISPLAY_Y - 60);
        if (getD) for (int i=0; i<MAX_DUCKLINGS; i++) Assets.font.draw(game.batch, Integer.toString(i), ducklingNumber[i].getX() + 36, ducklingNumber[i].getY() + 48);
        if (loadfile) for (int i=0; i<loadlevelbuttons.length; i++) {
            if (i<customfiles.length) Assets.font.draw(game.batch, customfiles[i].name(), loadlevelbuttons[i].getX(), loadlevelbuttons[i].getY());
        }
        game.batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        //draw detection bounds here for debugging
        shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
        shapeRenderer.rect(loadbutt.getX(), loadbutt.getY(), loadbutt.getWidth(), loadbutt.getHeight());
        shapeRenderer.rect(savebutt.getX(), savebutt.getY(), savebutt.getWidth(), savebutt.getHeight());
        shapeRenderer.rect(ducks.getX(), ducks.getY(), ducks.getWidth(), ducks.getHeight());
        shapeRenderer.rect(sharks.getX(), sharks.getY(), sharks.getWidth(), sharks.getHeight());
        shapeRenderer.rect(lillies.getX(), lillies.getY(), lillies.getWidth(), lillies.getHeight());
        shapeRenderer.rect(Tknob.getX(), Tknob.getY(), Tknob.getWidth(), Tknob.getHeight());
        shapeRenderer.rect(Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        shapeRenderer.rect(Confirm.getX(), Confirm.getY(), Confirm.getWidth(), Confirm.getHeight());
        if (getD) for (Rectangle r: ducklingNumber) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        if (loadfile) for (Rectangle r: loadlevelbuttons) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(trashbutt.getX(), trashbutt.getY(), trashbutt.getWidth(), trashbutt.getHeight());
        shapeRenderer.rect(TtimeUp.getX(), TtimeUp.getY(), TtimeUp.getWidth(), TtimeUp.getHeight());
        shapeRenderer.rect(TtimeDown.getX(), TtimeDown.getY(), TtimeDown.getWidth(), TtimeDown.getHeight());
        shapeRenderer.rect(LivesUp.getX(), LivesUp.getY(), LivesUp.getWidth(), LivesUp.getHeight());
        shapeRenderer.rect(LivesDown.getX(), LivesDown.getY(), LivesDown.getWidth(), LivesDown.getHeight());
        if (getVel) shapeRenderer.line(temppos.cpy().add(EDITOR_OFFSET), tempvel.cpy().add(temppos).add(EDITOR_OFFSET));

        shapeRenderer.end();
    }
}
