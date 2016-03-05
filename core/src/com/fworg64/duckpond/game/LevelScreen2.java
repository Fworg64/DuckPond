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
    boolean getT;

    Rectangle exitbutt;
    Rectangle savebutt;

    Array<Spawnable> spawnables;
    Spawnable tempguy;
    Vector2 temppos;
    Vector2 tempvel;
    String Message;

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
        dafile = Gdx.files.local("derp.txt");
        
        spawnables = new Array<Spawnable>();
        tempguy = new Spawnable();
        temppos = new Vector2();
        tempvel = new Vector2();

        ducks = new Rectangle(100f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        sharks = new Rectangle(200f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        lillies = new Rectangle(300f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);

        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);
        savebutt = new Rectangle(SAVE_X, SAVE_Y, SAVE_W, SAVE_H);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();
        Message = "heerp";

        getT = false;
        getVel = false;
        getPos = false;

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
            Gdx.app.debug("Tocuh", touchpoint.toString());
        }
        if (!getVel && !getT & !getPos) {
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
            touchpoint.set(in.getTouchpoint());
            if (in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
                tempguy.setPos(touchpoint);
            }
            if (!in.isTouched() && !tempguy.getObjtype().equals("Invalid")) {
                getVel = true;
                if (tempguy.getObjtype().equals("Lily")) {getVel = false; getT = true;}
                getPos = false;
            }
        }

        if (tempguy.getObjtype().equals("Lily")) {getVel = false;}
        if (getVel)
        {
            temppos.set(tempguy.getPos());
            if (in.isTouched()) {
                tempvel.set(touchpoint);
                Message = tempvel.cpy().sub(temppos).toString();
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
            spawnables.add(tempguy);
            Message = tempguy.toString();
            tempguy = new Spawnable();
            temppos.setZero();
            tempvel.setZero();
            getT = false;
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
        Assets.font.draw(game.batch,Message,.1f*gcam.viewportWidth,.8f* gcam.viewportHeight);
        game.batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        //draw detection bounds here for debugging
        shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
        shapeRenderer.rect(ducks.getX(), ducks.getY(), ducks.getWidth(), ducks.getHeight());
        shapeRenderer.rect(sharks.getX(), sharks.getY(), sharks.getWidth(), sharks.getHeight());
        shapeRenderer.rect(lillies.getX(), lillies.getY(), lillies.getWidth(), lillies.getHeight());
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
                this.dispose();
                game.setScreen(new MainMenuScreen(game));
                break;
            case 2:
                break;

        }
        draw();
    }

    public int savefile()
    {
        for (Spawnable s: spawnables)
        {
            //dafile.write(true, )
        }
        return 0;
    }
}
