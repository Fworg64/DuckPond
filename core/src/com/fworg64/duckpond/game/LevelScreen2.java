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
    public static final int EXIT_X = 1;//bottom left corner of button
    public static final int EXIT_Y = 1;
    public static final int EXIT_W = (int)(.1f * 2*DuckPondGame.worldW); //not exact yet
    public static final int EXIT_H = (int)(.1f * 2*DuckPondGame.worldH);

    public static final int SAVE_X = (int)(.01f * 2*DuckPondGame.worldW);
    public static final int SAVE_Y = (int)(.9f * 2*DuckPondGame.worldH);
    public static final int SAVE_W = (int)(.1f * 2*DuckPondGame.worldW); //not exact yet
    public static final int SAVE_H = (int)(.1f * 2*DuckPondGame.worldH);

    public static final Vector2 EDITOR_OFFSET = new Vector2(160,240);
    public static final float VELOCITY_INPUT_SCALE = .35f;
    
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    FileHandle dafile;
    InputListener in;
    Vector2 touchpoint;
    boolean getPos;
    boolean getVel;
    boolean gettingT;
    boolean getT;
    boolean getD;
    boolean choiceDestroy;

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
    Rectangle Taccept;
    Rectangle Dup;
    Rectangle Ddown;
    Rectangle Daccept;
    Rectangle TtimeUp;
    Rectangle TtimeDown;
    Rectangle LivesUp;
    Rectangle LivesDown;

    Rectangle ducks;
    Rectangle sharks;
    Rectangle lillies;

    private ShapeRenderer shapeRenderer;

    public LevelScreen2(DuckPondGame game)
    {
        wasHighres = Options.isHighres();
        Options.setStdres();
        Assets.levelEditLoad();
        this.game = game;
        gcam = new OrthographicCamera(2 * Options.screenWidth, 2* Options.screenHeight); //let us place things outside the map
        gcam.position.set(Options.screenWidth, Options.screenHeight, 0); //give ourselves a nice little camera //centered since its doubled

        if (Gdx.app.getType() != Application.ApplicationType.WebGL) dafile = Gdx.files.local("LEVELS\\test.txt");
        
        spawnables = new Array<Spawnable>();
        tempguy = new Spawnable();
        temppos = new Vector2();
        tempvel = new Vector2();
        tempt2s = -1;
        tempducks =0;
        lives = 3;
        time = 60;

        ducks = new Rectangle(400f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        sharks = new Rectangle(200f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        lillies = new Rectangle(300f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);

        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);
        savebutt = new Rectangle(SAVE_X, SAVE_Y, SAVE_W, SAVE_H);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();
        Message = "heerp";
        Tknob = new Rectangle(100f/640f * 2*Options.screenWidth, .9f* 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        Tslider = new Rectangle(100f/640f * 2*Options.screenWidth, .9f* 2*Options.screenHeight, 440f/640f * 2*Options.screenWidth, Options.spriteHeight);
        Taccept = new Rectangle(540f/640f * 2*Options.screenWidth, .8f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        Dup = new Rectangle(540f/640f * 2*Options.screenWidth, .7f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        Ddown = new Rectangle(540f/640f * 2*Options.screenWidth, .6f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        Daccept = new Rectangle(540f/640f * 2*Options.screenWidth, .5f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        trashbutt = new Rectangle(540f/640f * 2*Options.screenWidth, .25f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        TtimeUp = new Rectangle(400f/640f * 2*Options.screenWidth, .15f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        TtimeDown = new Rectangle(400f/640f * 2*Options.screenWidth, .075f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        LivesUp = new Rectangle(500f/640f * 2*Options.screenWidth, .15f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);
        LivesDown = new Rectangle(500f/640f * 2*Options.screenWidth, .074f * 2*Options.screenHeight, Options.spriteWidth, Options.spriteHeight);

        getD = false;
        getT = false;
        gettingT = false;
        getVel = false;
        getPos = false;
        choiceDestroy = false;

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);
    }

    public int update()
    {
        if (in.justTouched())
        {
            touchpoint.set(in.getTouchpoint());
            if (exitbutt.contains(touchpoint)) return 1;
            if (savebutt.contains(touchpoint)) return 2;
            if (trashbutt.contains(touchpoint)&& !(getVel || getT || getPos || getD))
            {
                Message = "Mark for destruction";
                choiceDestroy = true;
                getVel = false;
                getT = false;
                getPos = false;
                getD = false;
                Gdx.app.debug("OH", "1");
            }
            if (TtimeUp.contains(touchpoint) && time < 120) time+=30;
            if (TtimeDown.contains(touchpoint) && time>30) time-=30;
            if (LivesUp.contains(touchpoint) && lives <5) lives++;
            if (LivesDown.contains(touchpoint) && lives >1) lives--;
            Gdx.app.debug("Tocuh", touchpoint.toString());
        }
        if ((getVel || getT || getPos || getD) && trashbutt.contains(touchpoint) && in.justTouched())
        {DestroyCurrent();}
        if (choiceDestroy)
        {Choose2Destroy();}
        if (!getVel && !getT & !getPos && !getD && !choiceDestroy)
        {ChooseActor();}
        if (getPos)
        {ChoosePos();}
        if (getVel)
        {ChooseVel();}
        if (getT)
        {ChooseT();}
        if (getD)
        {ChooseD();}

        return 0;
    }

    public void draw() //fyotb
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.disableBlending();
        game.batch.begin();
        //draw background image here
        game.batch.draw(Assets.LevelEditBg, 0, 0);
        game.batch.draw(Assets.GameBackground, Options.screenWidth * .5f, Options.screenHeight * .5f);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.leveditDuck, ducks.getX(), ducks.getY());
        game.batch.draw(Assets.leveditShark, sharks.getX(), sharks.getY());
        game.batch.draw(Assets.leveditPad, lillies.getX(), lillies.getY());
        if (tempguy.getObjtype().equals("Shark")) game.batch.draw(Assets.leveditShark,tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
        else if (tempguy.getObjtype().equals("Duck")) game.batch.draw(Assets.leveditDuck,tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
        else if (tempguy.getObjtype().equals("Lily")) game.batch.draw(Assets.leveditPad,tempguy.getPos().x + EDITOR_OFFSET.x, tempguy.getPos().y + EDITOR_OFFSET.y);
        for (Spawnable s: spawnables)
        {
            if (s.getObjtype().equals("Shark")) game.batch.draw(Assets.leveditShark,s.getPos().x + EDITOR_OFFSET.x, s.getPos().y+ EDITOR_OFFSET.y);
            else if (s.getObjtype().equals("Duck")) game.batch.draw(Assets.leveditDuck,s.getPos().x + EDITOR_OFFSET.x, s.getPos().y+ EDITOR_OFFSET.y);
            else if (s.getObjtype().equals("Lily")) game.batch.draw(Assets.leveditPad,s.getPos().x + EDITOR_OFFSET.x, s.getPos().y+ EDITOR_OFFSET.y);
        }
        game.batch.draw(Assets.leveditTslider, Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        game.batch.draw(Assets.leveditTknob, Tknob.getX(), Tknob.getY());
        game.batch.draw(Assets.leveditTaccept, Taccept.getX(), Taccept.getY());
        game.batch.draw(Assets.leveditDup, Dup.getX(), Dup.getY());
        game.batch.draw(Assets.leveditDdown, Ddown.getX(), Ddown.getY());
        game.batch.draw(Assets.leveditDaccept, Daccept.getX(), Daccept.getY());
        game.batch.draw(Assets.leveditTrash, trashbutt.getX(), trashbutt.getY());
        game.batch.draw(Assets.leveditDup, TtimeUp.getX(), TtimeUp.getY());
        game.batch.draw(Assets.leveditDdown, TtimeDown.getX(), TtimeDown.getY());
        game.batch.draw(Assets.leveditDup, LivesUp.getX(), LivesUp.getY());
        game.batch.draw(Assets.leveditDdown, LivesDown.getX(), LivesDown.getY());
        Assets.font.draw(game.batch, Message, .1f * gcam.viewportWidth, .9f * gcam.viewportHeight);
        Assets.font.draw(game.batch, "Time: " + Integer.toString(time), TtimeUp.getX() - .5f*TtimeUp.getWidth(), TtimeUp.getY());
        Assets.font.draw(game.batch, "Lives: " + Integer.toString(lives), LivesUp.getX() - .5f*LivesUp.getWidth(), LivesUp.getY());
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
        shapeRenderer.rect(Taccept.getX(), Taccept.getY(), Taccept.getWidth(), Taccept.getHeight());
        shapeRenderer.rect(Dup.getX(), Dup.getY(), Dup.getWidth(), Dup.getHeight());
        shapeRenderer.rect(Ddown.getX(), Ddown.getY(), Ddown.getWidth(), Ddown.getHeight());
        shapeRenderer.rect(Daccept.getX(), Daccept.getY(), Daccept.getWidth(), Daccept.getHeight());
        shapeRenderer.rect(trashbutt.getX(), trashbutt.getY(), trashbutt.getWidth(), trashbutt.getHeight());
        shapeRenderer.rect(TtimeUp.getX(), TtimeUp.getY(), TtimeUp.getWidth(), TtimeUp.getHeight());
        shapeRenderer.rect(TtimeDown.getX(), TtimeDown.getY(), TtimeDown.getWidth(), TtimeDown.getHeight());
        shapeRenderer.rect(LivesUp.getX(), LivesUp.getY(), LivesUp.getWidth(), LivesUp.getHeight());
        shapeRenderer.rect(LivesDown.getX(), LivesDown.getY(), LivesDown.getWidth(), LivesDown.getHeight());
        if (getVel) shapeRenderer.line(temppos.cpy().add(EDITOR_OFFSET), tempvel.cpy().add(EDITOR_OFFSET));

        shapeRenderer.end();
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
        choiceDestroy = false;
        //Gdx.app.debug("OH", "2");
    }

    public void Choose2Destroy()
    {
        touchpoint.set(in.getTouchpoint());
        for (Spawnable s: spawnables)
        {
            if (in.isTouched())
            {
                if (touchpoint.x<(s.getPos().x + Options.spriteWidth) && touchpoint.x > s.getPos().x) //x matches
                {
                    if (touchpoint.y<(s.getPos().y + Options.spriteHeight) && touchpoint.y > s.getPos().y)//y matches
                    {
                        Message = s.getObjtype() + "\nDestroyed";
                        spawnables.removeValue(s,true);
                    }
                }
            }
        }
        if (in.justTouched() && !trashbutt.contains(touchpoint)) choiceDestroy = false;
    }

    public void ChooseActor()
    {
        Message = "Choose a character\nDrag to Position";
        if (in.justTouched() && ducks.contains(touchpoint)) {
            tempguy.setObjtype("Duck");
            getPos = true;
        }
        else if (in.justTouched() && sharks.contains(touchpoint)) {
            tempguy.setObjtype("Shark");
            getPos = true;
        }
        else if (in.justTouched() && lillies.contains(touchpoint)) {
            tempguy.setObjtype("Lily");
            getPos = true;
        }
    }

    public void ChoosePos()
    {
        //Message = "Drag to position";
        touchpoint.set(in.getTouchpoint());
        if (in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
            tempguy.setPos(touchpoint.cpy().sub(EDITOR_OFFSET));
            Message = tempguy.getPos().toString();
        }
        if (!in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
            getVel = true;
            if (tempguy.getObjtype().equals("Lily")) {getVel = false; getT = true;}
            getPos = false;
        }
    }

    public void ChooseVel()
    {
        Message = "Set Velocity by \nreleasing click \nnext to char";
        temppos.set(tempguy.getPos());
        if (in.isTouched()) {
            touchpoint.set(in.getTouchpoint());
            tempvel.set(touchpoint.cpy().sub(EDITOR_OFFSET));
            Message = tempvel.cpy().sub(temppos).scl(VELOCITY_INPUT_SCALE).toString() + "\nSpeed:" + Float.toString(tempvel.cpy().sub(temppos).scl(VELOCITY_INPUT_SCALE).len());
        }
        if (!in.isTouched() && !tempvel.isZero()) //vel was set
        {
            tempguy.setVel(tempvel.cpy().sub(temppos).scl(VELOCITY_INPUT_SCALE));
            getVel = false;
            getT = true;
        }
    }

    public void ChooseT()
    {
        tempt2s = ((Tknob.getX()-Tslider.getX())/Tslider.getWidth());
        Message = "Drag slider\npress confirm \nfor SpawnTime\n" + Float.toString(tempt2s);
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
        if (in.justTouched() && Taccept.contains(touchpoint))
        {
            if (tempguy.getObjtype().equals("Duck")) {getD = true; Gdx.app.debug("We", "a duck");}
            tempguy.setTime2spawn(tempt2s); //number between 0 and 1
            if (!getD) {
                spawnables.add(tempguy);
                Message = tempguy.toString();
                tempguy = new Spawnable();
                temppos.setZero();
                tempvel.setZero();
            }
            getT = false;
        }
    }

    public void ChooseD()
    {
        if (tempducks ==0) Message = "Use arrows to \nadjust #ducklings\n (0 now)";
        else Message = Integer.toString(tempducks) + " ducklings";
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched() && Dup.contains(touchpoint) && tempducks<15) tempducks++;
        if (in.justTouched() && Ddown.contains(touchpoint) && tempducks>0) tempducks--;
        if (in.justTouched() && Daccept.contains(touchpoint))
        {
            tempguy.setNumducks(tempducks);
            spawnables.add(tempguy);
            Message = tempguy.toString();
            tempguy = new Spawnable();
            temppos.setZero();
            tempvel.setZero();
            getD = false;
        }
    }
}
