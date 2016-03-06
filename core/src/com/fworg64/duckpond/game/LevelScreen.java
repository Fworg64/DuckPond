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
 * Created by fworg on 2/17/2016.
 */
public class LevelScreen extends ScreenAdapter
{
    public static final int EXIT_X = 1;//bottom left corner of button
    public static final int EXIT_Y = 1;
    public static final int EXIT_W = (int)(.25f * Options.screenWidth); //not exact yet
    public static final int EXIT_H = (int)(.2f * Options.screenHeight);

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    InputListener in;

    boolean getVel;
    boolean VelGot;
    Vector2 velStart;
    Vector2 velEnd;
    String  VelgetterMsg;

    //file is working bro!!!!possibly consider writing to new files for each level

    FileHandle fileR = Gdx.files.local("writez.txt");
    FileHandle fileW = Gdx.files.local("writez.txt");
    Vector2 touchpoint; //input vector

    Rectangle exitbutt;

    Array<Duck> dL;//contains the ducks in the game //libgdx Array<> is better optimized for games
    Rectangle ducks;//for collision
    Vector2 tempDuckPos;
    boolean droppinDuck;
    Array<Shark> sL;//contains the ducks in the game
    Rectangle sharks;//for collision
    Vector2 tempSharkPos;
    boolean droppinShark;
    Rectangle lillies;
    Array<Lily> lL;
    Vector2 tempLilyPos;
    boolean droppinLily;

    private ShapeRenderer shapeRenderer;//this helps for checking button rectangles

    public LevelScreen(DuckPondGame game)
    {
        Options.loadDefault();
        Assets.levelEditLoad();
        this.game = game;
        gcam = new OrthographicCamera(2 * Options.screenWidth, 2* Options.screenHeight); //let us place things outside the map
        gcam.position.set(Options.screenWidth, Options.screenHeight, 0); //give ourselves a nice little camera //centered since its doubled

        //intialize all necessary variables for the editor
        dL = new Array<Duck>();
        ducks = new Rectangle(100f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        tempDuckPos = new Vector2();
        droppinDuck = false;

        sL = new Array<Shark>();
        sharks = new Rectangle(200f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        tempSharkPos = new Vector2();
        droppinShark = false;

        lL = new Array<Lily>();
        lillies = new Rectangle(300f/640f * 2*Options.screenWidth,0,Options.spriteWidth,Options.spriteHeight);
        tempLilyPos = new Vector2();
        droppinLily = false;


        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();

        getVel = false;
        VelGot = false;
        velStart = new Vector2();
        velEnd = new Vector2();
        VelgetterMsg = "";

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);
    }

    public int update()
    {
        //code that gets run each frame goes here
        if (in.justTouched())
        {
            touchpoint.set(in.getTouchpoint());
            Gdx.app.debug("Tocuh", touchpoint.toString());
        }
        if (in.justTouched() && ducks.contains(touchpoint) && !droppinDuck) //the ducks have been touched
        {
            touchpoint.set(in.getTouchpoint());
            Gdx.app.debug("Tocuh", touchpoint.toString());
            tempDuckPos.set(touchpoint.x, touchpoint.y);
            droppinDuck = true;
            Gdx.app.debug("touch", "duck");
        }

        if (in.isTouched() && droppinDuck)
        {
            touchpoint.set(in.getTouchpoint());
            tempDuckPos.set(touchpoint);
        }


        if (!in.isTouched() && droppinDuck) //duck dropped
        {
            droppinDuck = false;
            //need to get velocity and time to spawn from user
            //dL.add(new Duck(tempDuckPos.x, tempDuckPos.y, 35, 35));
            Gdx.app.debug("DuckDropped", tempDuckPos.toString());
            //get velocity information, number of ducklings, time to spawn
            getVel = true;
            VelgetterMsg = "Press+hold to choose vel, release to confirm";
        }
        if (in.justTouched() && sharks.contains(touchpoint) && !droppinShark) //the sharks have been touched
        {
        touchpoint.set(in.getTouchpoint());
        Gdx.app.debug("Tocuh", touchpoint.toString());
        tempSharkPos.set(touchpoint.x, touchpoint.y);
        droppinShark= true;
        Gdx.app.debug("touch", "duck");
        }

        if (in.isTouched() && droppinShark)
        {
            touchpoint.set(in.getTouchpoint());
            tempSharkPos.set(touchpoint);
        }


        if (!in.isTouched() && droppinShark) //shark dropped
        {
            droppinShark = false;
            //need to get velecity and time to spawn from user
            //sL.add(new Shark(tempSharkPos.x, tempSharkPos.y, 35, 35));
            Gdx.app.debug("SharkDropped", tempSharkPos.toString());
            //get velocity information, number of ducklings, time to spawn
            getVel = true;
            VelgetterMsg = "Press+hold to choose vel, release to confirm";
        }



        if (in.justTouched() && lillies.contains(touchpoint) && !droppinLily) //the lillies have been touched
        {
            touchpoint.set(in.getTouchpoint());
            Gdx.app.debug("Tocuh", touchpoint.toString());
            tempLilyPos.set(touchpoint.x, touchpoint.y);
            droppinLily= true;
            Gdx.app.debug("touch", "lily");
        }

        if (in.isTouched() && droppinLily)
        {
            touchpoint.set(in.getTouchpoint());
            tempLilyPos.set(touchpoint);
        }


        if (!in.isTouched() && droppinLily) //duck dropped
        {
            droppinLily = false;
            //I suppose lillys have no velocity and a spawn time of 0
            lL.add(new Lily(tempLilyPos.x, tempLilyPos.y));
            Gdx.app.debug("LilyDropped", tempLilyPos.toString());
            //get velocity information, number of ducklings, time to spawn
        }
        if (exitbutt.contains(touchpoint.x, touchpoint.y))
        {
            //have the boolean parameter set to true for testing but false for when we implement(maybe)
            //false for append = over writes current contents
            //true for append = continues to pad file with text
            //might need that set to true for multiple stuffs to write for file
            //use false to over write all current txt in the file

            //assuming there is just one lilypad, idk homie
            fileW.writeString("Lily " + lL.get(0).toString(), false);
            for (Duck d: dL) {
                fileW.writeString("Duck " + d.toString(), true);

            }
            for (Shark s: sL) {
                fileW.writeString("Shark: " + s.toString(), true);

            }
            String text = fileR.readString();
            System.out.println(text);
            game.setScreen(new MainMenuScreen(game));
            return 1;
        }
        //also need a save button, which exports array<>'s to a file and stores the time to spawn
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
        game.batch.draw(Assets.LevelEditBg, 0,0);
        game.batch.draw(Assets.GameBackground, Options.screenWidth * .5f, Options.screenHeight * .5f);
        game.batch.end();


        game.batch.enableBlending();
        game.batch.begin();

        if (!tempDuckPos.isZero()) game.batch.draw(Assets.leveditDuck, tempDuckPos.x, tempDuckPos.y);
        game.batch.draw(Assets.leveditDuck, ducks.getX(), ducks.getY());
        if (!tempSharkPos.isZero()) game.batch.draw(Assets.leveditShark, tempSharkPos.x, tempSharkPos.y);
        game.batch.draw(Assets.leveditShark, sharks.getX(), sharks.getY());
        if (!tempLilyPos.isZero()) game.batch.draw(Assets.leveditPad, tempLilyPos.x, tempLilyPos.y);
        game.batch.draw(Assets.leveditPad, lillies.getX(), lillies.getY());

        for (Duck f : dL){game.batch.draw(Assets.leveditDuck, f.pos.x, f.pos.y);} //oh so you wanna save lines..
        for (Shark f : sL){game.batch.draw(Assets.leveditShark, f.pos.x, f.pos.y);}
        for (Lily f: lL) {game.batch.draw(Assets.leveditPad, f.pos.x, f.pos.y);}

        Assets.font.draw(game.batch,VelgetterMsg,.1f*gcam.viewportWidth,.8f* gcam.viewportHeight);

        game.batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        //draw detection bounds here for debugging
        shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
        shapeRenderer.rect(ducks.getX(), ducks.getY(), ducks.getWidth(), ducks.getHeight());
        shapeRenderer.rect(sharks.getX(), sharks.getY(), sharks.getWidth(), sharks.getHeight());
        shapeRenderer.rect(lillies.getX(), lillies.getY(), lillies.getWidth(), lillies.getHeight());

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
                break;
        }
        draw();
    }
}
