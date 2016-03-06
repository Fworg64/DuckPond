package com.fworg64.duckpond.game;

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
    public static final int EXIT_W = (int)(.25f * Options.screenWidth); //not exact yet
    public static final int EXIT_H = (int)(.2f * Options.screenHeight);

    public static final int SAVE_X = (int)(.01f * Options.screenWidth);
    public static final int SAVE_Y = (int)(.8f * Options.screenHeight);
    public static final int SAVE_W = (int)(.1f * Options.screenWidth); //not exact yet
    public static final int SAVE_H = (int)(.2f * Options.screenHeight);
    
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

    Rectangle exitbutt;
    Rectangle savebutt;
    Rectangle trashbutt;

    Array<Spawnable> spawnables;
    Spawnable tempguy;
    Vector2 temppos;
    Vector2 tempvel;
    float tempt2s;
    int tempducks;

    String Message;
    Rectangle Tknob;
    Rectangle Tslider;
    Rectangle Taccept;
    Rectangle Dup;
    Rectangle Ddown;
    Rectangle Daccept;

    Rectangle ducks;
    Rectangle sharks;
    Rectangle lillies;

    private ShapeRenderer shapeRenderer;

    public LevelScreen2(DuckPondGame game)
    {
        Options.loadDefault();
        Assets.levelEditLoad();
        this.game = game;
        gcam = new OrthographicCamera(2 * Options.screenWidth, 2* Options.screenHeight); //let us place things outside the map
        gcam.position.set(Options.screenWidth, Options.screenHeight, 0); //give ourselves a nice little camera //centered since its doubled
        dafile = Gdx.files.local("LEVELS\\derp.txt");
        
        spawnables = new Array<Spawnable>();
        tempguy = new Spawnable();
        temppos = new Vector2();
        tempvel = new Vector2();
        tempt2s = -1;
        tempducks =0;

        ducks = new Rectangle(100f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
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

        getD = false;
        getT = false;
        gettingT = false;
        getVel = false;
        getPos = false;
        choiceDestroy = false;

        Gdx.app.debug("testicles", tempguy.objtype);

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
            Gdx.app.debug("Tocuh", touchpoint.toString());
        }
        if ((getVel || getT || getPos || getD) && trashbutt.contains(touchpoint) && in.justTouched())
        {
            Message = "Current temp destroyed.";
            tempguy = new Spawnable();
            temppos.setZero();
            tempvel.setZero();
            getVel = false;
            getT = false;
            getPos = false;
            getD = false;
            choiceDestroy = false;
            Gdx.app.debug("OH", "2");
        }
        if (choiceDestroy)
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
        if (!getVel && !getT & !getPos && !getD && !choiceDestroy) {
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
        if (getPos) {
            //Message = "Drag to position";
            touchpoint.set(in.getTouchpoint());
            if (in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
                tempguy.setPos(touchpoint);
                Message = tempguy.getPos().toString();
            }
            if (!in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
                getVel = true;
                if (tempguy.getObjtype().equals("Lily")) {getVel = false; getT = true;}
                getPos = false;
            }
        }
        if (getVel)
        {
            Message = "Set Velocity by releasing click next to char";
            temppos.set(tempguy.getPos());
            if (in.isTouched()) {
                touchpoint.set(in.getTouchpoint());
                tempvel.set(touchpoint);
                Message = tempvel.cpy().sub(temppos).toString() + " " + Float.toString(tempvel.cpy().sub(temppos).len());
            }
            if (!in.isTouched() && !tempvel.isZero()) //vel was set
            {
                tempguy.setVel(tempvel.cpy().sub(temppos));
                getVel = false;
                getT = true;
            }
        }
        if (getT)
        {
            tempt2s = ((Tknob.getX()-Tslider.getX())/Tslider.getWidth());
            Message = "Drag slider\npress confirm for SpawnTime\n" + Float.toString(tempt2s);
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
        if (getD)
        {
            if (tempducks ==0) Message = "Use arrows to adjust #ducklings";
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
        if (tempguy.getObjtype().equals("Shark")) game.batch.draw(Assets.leveditShark,tempguy.getPos().x, tempguy.getPos().y);
        else if (tempguy.getObjtype().equals("Duck")) game.batch.draw(Assets.leveditDuck,tempguy.getPos().x, tempguy.getPos().y);
        else if (tempguy.getObjtype().equals("Lily")) game.batch.draw(Assets.leveditPad,tempguy.getPos().x, tempguy.getPos().y);
        for (Spawnable s: spawnables)
        {
            if (s.getObjtype().equals("Shark")) game.batch.draw(Assets.leveditShark,s.getPos().x, s.getPos().y);
            else if (s.getObjtype().equals("Duck")) game.batch.draw(Assets.leveditDuck,s.getPos().x, s.getPos().y);
            else if (s.getObjtype().equals("Lily")) game.batch.draw(Assets.leveditPad,s.getPos().x, s.getPos().y);
        }
        game.batch.draw(Assets.leveditTslider, Tslider.getX(), Tslider.getY(), Tslider.getWidth(), Tslider.getHeight());
        game.batch.draw(Assets.leveditTknob, Tknob.getX(), Tknob.getY());
        game.batch.draw(Assets.leveditTaccept, Taccept.getX(), Taccept.getY());
        game.batch.draw(Assets.leveditDup, Dup.getX(), Dup.getY());
        game.batch.draw(Assets.leveditDdown, Ddown.getX(), Ddown.getY());
        game.batch.draw(Assets.leveditDaccept, Daccept.getX(), Daccept.getY());
        game.batch.draw(Assets.leveditTrash, trashbutt.getX(), trashbutt.getY());
        Assets.font.draw(game.batch, Message, .1f * gcam.viewportWidth, .9f * gcam.viewportHeight);
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
        if (getVel) shapeRenderer.line(temppos, tempvel);

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
        dafile.writeString("TIME: 120\n", false);
        for (Spawnable s: spawnables)
        {
            dafile.writeString(s.toString() + '\n', true);
        }
        return 0;
    }
}