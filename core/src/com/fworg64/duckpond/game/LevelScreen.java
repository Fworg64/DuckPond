package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
    boolean droppinPad;

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
        droppinPad = false;


        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);

        in = new InputListener((int)gcam.viewportWidth, (int)gcam.viewportHeight);
        touchpoint = new Vector2();

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
            Gdx.app.debug("duck draaaaged", tempDuckPos.toString());
        }


        if (!in.isTouched() && droppinDuck) //duck dropped
        {
            droppinDuck = false;
            dL.add(new Duck(tempDuckPos.x, tempDuckPos.y, 35, 35));
            Gdx.app.debug("DuckDropped", tempDuckPos.toString());
            //get velocity information, number of ducklings, time to spawn
        }

        if (exitbutt.contains(touchpoint.x, touchpoint.y))
        {
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

        for (Duck f : dL){game.batch.draw(Assets.leveditDuck, f.pos.x, f.pos.y);} //oh so you wanna save lines..
        for (Shark f : sL){game.batch.draw(Assets.leveditShark, f.pos.x, f.pos.y);}
        for (Lily f: lL) {game.batch.draw(Assets.leveditPad, f.pos.x, f.pos.y);}


        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f,.2f,.2f,.5f);
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
