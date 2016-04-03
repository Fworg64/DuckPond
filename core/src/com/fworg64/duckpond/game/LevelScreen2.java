package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by fworg on 3/4/2016.
 */
public class LevelScreen2 extends ScreenAdapter
{
    public static final int EXIT_X = 732;//bottom left corner of button
    public static final int EXIT_Y = 1920-136;
    public static final int EXIT_W = 300; //not exact yet
    public static final int EXIT_H = 110;

    public static final int SAVE_X = 24;
    public static final int SAVE_Y = 1920-136;
    public static final int SAVE_W = 300; //not exact yet
    public static final int SAVE_H = 110;

    public static final Vector2 EDITOR_OFFSET = new Vector2(219, 1920-1392);
    public static final float VELOCITY_INPUT_SCALE = .35f;
    
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    FileHandle dafile;
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

    boolean wasHighres;
    Rectangle exitbutt;
    Rectangle savebutt;
    Rectangle trashbutt;

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
    Rectangle Dup;
    Rectangle Ddown;
    Rectangle TtimeUp;
    Rectangle TtimeDown;
    Rectangle LivesUp;
    Rectangle LivesDown;

    Rectangle ducks;
    Rectangle sharks;
    Rectangle lillies;

    Rectangle playarea;
    Rectangle placementarea;

    private ShapeRenderer shapeRenderer;

    public LevelScreen2(DuckPondGame game)
    {
        wasHighres = Options.isHighres();
        Options.setStdres();
        Assets.levelEditLoad();
        this.game = game;
        gcam = new OrthographicCamera(DuckPondGame.highresScreenW,DuckPondGame.highresScreenH); //let us place things outside the map
        gcam.position.set(DuckPondGame.highresScreenW*.5f, DuckPondGame.highresScreenH*.5f, 0); //high res mode but assets at stdres for zoomout

        if (Gdx.app.getType() != Application.ApplicationType.WebGL) dafile = Gdx.files.local("LEVELS\\test.txt");
        
        spawnables = new Array<Spawnable>();
        tempguy = new Spawnable();
        temppos = new Vector2();
        tempvel = new Vector2();
        tempt2s = 0;
        tempducks =0;
        lives = 3;
        time = 60;

        ducks = new Rectangle(60, 1920 - 1641, 96, 96);
        sharks = new Rectangle(200, 1920 - 1641, 96, 96);
        lillies = new Rectangle(350, 1920-1641, 96, 96);

        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);
        savebutt = new Rectangle(SAVE_X, SAVE_Y, SAVE_W, SAVE_H);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();
        Message = "heerp";
        Tknob = new Rectangle(22,1920-1800, 96, 96);
        Tslider = new Rectangle(0, 0, 790, 1920-1777);
        Confirm = new Rectangle(810, 1920 - 1886, 300, 110);
        Dup = new Rectangle(540f/640f       * Options.screenWidth, .7f   * Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        Ddown = new Rectangle(540f/640f     * Options.screenWidth, .6f   * Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        trashbutt = new Rectangle(794,1920-1746, 250, 100);
        TtimeUp = new Rectangle(400f/640f   * Options.screenWidth, .15f  * Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        TtimeDown = new Rectangle(400f/640f * Options.screenWidth, .075f * Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        LivesUp = new Rectangle(500f/640f   * Options.screenWidth, .15f  * Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        LivesDown = new Rectangle(500f/640f * Options.screenWidth, .074f * Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        playarea = new Rectangle(EDITOR_OFFSET.x, EDITOR_OFFSET.y, DuckPondGame.worldW, DuckPondGame.worldH);
        placementarea = new Rectangle(0, 308, 1080, 1304);

        defaultstate = true;
        getD = false;
        getT = false;
        gettingT = false;
        getVel = false;
        getPos = false;
        choiceDestroy = false;
        globalTAdjust = false;
        ready2confirm = false;

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
            if (savebutt.contains(touchpoint)) return 2;
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
            if (LivesUp.contains(touchpoint) && lives <5) lives++;
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
                game.setScreen(new MainMenuScreen(game));
                break;
            case 2:
                Gdx.app.debug("screenstate", "save");
                savefile();
                break;

        }
        draw();
    }

    public int savefile()
    {
        if (Gdx.app.getType() != Application.ApplicationType.WebGL)
        {
            dafile.writeString(Integer.toString(time) + " " + Integer.toString(lives) + "\n", false);
            for (Spawnable s: spawnables)
            {
                dafile.writeString(s.toString() + '\n', true);
            }

            Options.setCustom1(dafile.readString());
            Options.save();
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

        Message = "file saved.\n" + Integer.toString(spawnables.size);

        return 0;
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
                if (tempt2s >= s.getTime2spawn())
                {
                    float del_X = s.getPos().x + EDITOR_OFFSET.x + s.getVel().x * (tempt2s-s.getTime2spawn()) *time;
                    float del_Y = s.getPos().y + EDITOR_OFFSET.y + s.getVel().y * (tempt2s-s.getTime2spawn()) *time;
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
        }
        if (in.justTouched() && !trashbutt.contains(touchpoint)) {
            Gdx.app.debug("choicedestroy", "weout");
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
                Message = "Press confirm when ready";
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
        Message = "Set Velocity by releasing touch";
        temppos.set(tempguy.getPos());
        if (in.isTouched()) {
            touchpoint.set(in.getTouchpoint());
            tempvel.set(touchpoint.cpy().sub(EDITOR_OFFSET));
            Message = tempvel.cpy().sub(temppos).scl(VELOCITY_INPUT_SCALE).toString() + "\nSpeed:" + Float.toString(tempvel.cpy().sub(temppos).scl(VELOCITY_INPUT_SCALE).len());
        }
        if (!in.isTouched() && !tempvel.isZero()) //vel was set
        {
            ready2confirm = true;
            Message = "Press confirm when ready";
        }
        if (ready2confirm && Confirm.contains(touchpoint) && in.justTouched())
        {
            tempguy.setVel(tempvel.cpy().sub(temppos).scl(VELOCITY_INPUT_SCALE));
            getVel = false;
            getT = true;
            ready2confirm = false;
        }
    }

    public void ChooseT()
    {
        tempt2s = ((Tknob.getX()-Tslider.getX())/Tslider.getWidth()) * time;
        Message = "Drag slider and press confirm for SpawnTime: " + Float.toString(tempt2s);
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && Tknob.contains(touchpoint)) gettingT = true;
        if (gettingT && in.isTouched())
        {
            {Tknob.setX(touchpoint.x);}//clamp
            if (touchpoint.x < Tslider.x) {Tknob.setX(Tslider.x);}
            if (touchpoint.x > (Tslider.x +Tslider.getWidth())) {Tknob.setX(Tslider.x +Tslider.getWidth());}
        }
        if (gettingT && !in.isTouched())
        {
            gettingT = false;
        }
        if (in.justTouched() && Confirm.contains(touchpoint))
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
            defaultstate = true;
        }
    }

    public void adjustGlobalT()
    {
        tempt2s = ((Tknob.getX()-Tslider.getX())/Tslider.getWidth()) * time;
        Message = "Time:" + Float.toString(tempt2s);
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && Tknob.contains(touchpoint)) gettingT = true;
        if (gettingT && in.isTouched())
        {
            {Tknob.setX(touchpoint.x);}//clamp
            if (touchpoint.x < Tslider.x) {Tknob.setX(Tslider.x);}
            if (touchpoint.x > (Tslider.x +Tslider.getWidth())) {Tknob.setX(Tslider.x +Tslider.getWidth());}
        }
        if (gettingT && !in.isTouched())
        {
            gettingT = false;
            globalTAdjust = false;
            defaultstate = true;
        }
    }

    public void updateTempt2s() {tempt2s = ((Tknob.getX()-Tslider.getX())/Tslider.getWidth()) * time;}

    public void ChooseD()
    {
        if (tempducks ==0) Message = "Use arrows to adjust #ducklings: (0 now)";
        else Message = Integer.toString(tempducks) + " ducklings";
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched() && Dup.contains(touchpoint) && tempducks<15) tempducks++;
        if (in.justTouched() && Ddown.contains(touchpoint) && tempducks>0) tempducks--;
        if (in.justTouched() && Confirm.contains(touchpoint))
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

    public void draw() //fyotb
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(.27451f, .70588f, .83922f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.disableBlending();
        game.batch.begin();
        //draw background image here
        game.batch.draw(Assets.LevelEditUpperArea, 0, 1920-308);
        game.batch.draw(Assets.LevelEditLowerArea, 0, 0);
        game.batch.draw(Assets.LevelEditOutsidePlacement, 0, 308);
        game.batch.draw(Assets.LevelEditGameplayArea, EDITOR_OFFSET.x, EDITOR_OFFSET.y);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
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
        }
        game.batch.draw(Assets.LevelEditTimeBar, Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        game.batch.draw(Assets.LevelEditClock, Tknob.getX(), Tknob.getY());

        game.batch.draw(Assets.LevelEditRemoveItem, trashbutt.getX(), trashbutt.getY());

        Assets.font.draw(game.batch, Message, .1f * gcam.viewportWidth, .9f * gcam.viewportHeight);
        Assets.font.draw(game.batch, "Total Time: " + Integer.toString(time), TtimeUp.getX() - 1.0f*TtimeUp.getWidth(), TtimeUp.getY());
        Assets.font.draw(game.batch, "Lives: " + Integer.toString(lives), LivesUp.getX() - .5f*LivesUp.getWidth(), LivesUp.getY());
        Assets.font.draw(game.batch, "Save", savebutt.getX(), savebutt.getY() + .5f * savebutt.getHeight());
        Assets.font.draw(game.batch, "curr Time: " + Float.toString(tempt2s), Tslider.getX(), Tslider.getY() + 1.5f * Tknob.getHeight());
        game.batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        //draw detection bounds here for debugging
        shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
        shapeRenderer.rect(savebutt.getX(), savebutt.getY(), savebutt.getWidth(), savebutt.getHeight());
        shapeRenderer.rect(ducks.getX(), ducks.getY(), ducks.getWidth(), ducks.getHeight());
        shapeRenderer.rect(sharks.getX(), sharks.getY(), sharks.getWidth(), sharks.getHeight());
        shapeRenderer.rect(lillies.getX(), lillies.getY(), lillies.getWidth(), lillies.getHeight());
        shapeRenderer.rect(Tknob.getX(), Tknob.getY(), Tknob.getWidth(), Tknob.getHeight());
        shapeRenderer.rect(Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        shapeRenderer.rect(Confirm.getX(), Confirm.getY(), Confirm.getWidth(), Confirm.getHeight());
        shapeRenderer.rect(Dup.getX(), Dup.getY(), Dup.getWidth(), Dup.getHeight());
        shapeRenderer.rect(Ddown.getX(), Ddown.getY(), Ddown.getWidth(), Ddown.getHeight());
        shapeRenderer.rect(trashbutt.getX(), trashbutt.getY(), trashbutt.getWidth(), trashbutt.getHeight());
        shapeRenderer.rect(TtimeUp.getX(), TtimeUp.getY(), TtimeUp.getWidth(), TtimeUp.getHeight());
        shapeRenderer.rect(TtimeDown.getX(), TtimeDown.getY(), TtimeDown.getWidth(), TtimeDown.getHeight());
        shapeRenderer.rect(LivesUp.getX(), LivesUp.getY(), LivesUp.getWidth(), LivesUp.getHeight());
        shapeRenderer.rect(LivesDown.getX(), LivesDown.getY(), LivesDown.getWidth(), LivesDown.getHeight());
        if (getVel) shapeRenderer.line(temppos.cpy().add(EDITOR_OFFSET), tempvel.cpy().add(EDITOR_OFFSET));

        shapeRenderer.end();
    }
}
