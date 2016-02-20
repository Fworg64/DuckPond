package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
//might wanna use draglistener??????
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by fworg on 2/17/2016.
 */
public class LevelScreen extends ScreenAdapter
{
    public static final int EXIT_X = 0;//bottom left corner of button
    public static final int EXIT_Y = 0;
    public static final int EXIT_W = (int)(.25f * Options.screenWidth); //not exact yet
    public static final int EXIT_H = (int)(.2f * Options.screenHeight);

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    InputListener in;

    Vector2 touchpoint; //input vector

    Rectangle exitbutt;
    ArrayList<Duck> dL;//contains the ducks in the game
    Rectangle ducks;//for collision
    int duckCounter;//counts the ducks
    ArrayList<Shark> sL;//contains the ducks in the game
    Rectangle sharks;//for collision
    int sharkCounter;//counts the ducks
    Rectangle lillies;

    private ShapeRenderer shapeRenderer;//this helps for checking button rectangles

    public LevelScreen(DuckPondGame game)
    {

        Assets.load();
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera

<<<<<<< HEAD
        exitbutt = new Rectangle(0,0,100,100); //this isn't exact yet
        duckCounter=0;
        //intialize all necessary variables for the editor
        dL = new ArrayList<Duck>();
        dL.add(duckCounter, new Duck(100, 0, 96, 96));
        ducks = new Rectangle(100,0,96,96);
        duckCounter = 1;
        sL = new ArrayList<Shark>();
        sL.add(sharkCounter,new Shark(200,0,96,96));
        sharks = new Rectangle(200,0,50,96);
        sharkCounter = 1;
        lillies = new Rectangle(275,0,50,96);
=======
        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);
>>>>>>> refs/remotes/origin/master

        in = new InputListener();
        touchpoint = new Vector2();

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);
    }

    public int update()
    {
        //code that gets run each frame goes here
        if (in.justTouched())  //you just got touched son
        {
            touchpoint.set(in.getTouchpoint());


                if (ducks.contains(touchpoint.x, touchpoint.y)) {
                    //adds new duck the user wants to have in level
                    dL.add(duckCounter, new Duck(100, 0, 96, 96));
                    //test to see if ducks being touched
                    System.out.println(duckCounter);
                }
            //need to think of better algorithm to move the ducks
            //may need to implement for shark as well as well lilypad
            if (touchpoint.x > dL.get(duckCounter).getXCord()) {
                dL.get(duckCounter).setXCord(touchpoint.x);
                duckCounter++;
                dL.add(duckCounter, new Duck(100, 0, 96, 96));
            } else if (touchpoint.y > dL.get(duckCounter).getYCord()) {
                dL.get(duckCounter).setYCord(touchpoint.y);
                duckCounter++;
                dL.add(duckCounter, new Duck(100, 0, 96, 96));
            } else if (touchpoint.x > dL.get(duckCounter).getXCord() && touchpoint.y > dL.get(duckCounter).getYCord()) {
                dL.get(duckCounter).setXCord(touchpoint.x);
                dL.get(duckCounter).setYCord(touchpoint.y);
                duckCounter++;
                dL.add(duckCounter, new Duck(100, 0, 96, 96));
            }
            else{
                duckCounter++;
            }

            //if()


            //make this save and exit maybe?
            if (exitbutt.contains(touchpoint.x, touchpoint.y))
            {
                try {
                    //we need to find a common file location on all machines
                    //currently i am using where the txt file is placed on my system

                    String s = "Q:\\DuckPond-master\\Levels";

                    //keep getting filenotfound exception for some reason :((((
                    FileWriter fw = new FileWriter(s);
                    //LOOK AT COMMENT IN DRAW METHOD ABOUT ACCESSOR METHODS
                    fw.append("amount of ducks(),amount of sharks,?lilypad placement?");
                    // new line character is \n pretty sure xD
                    fw.append("\n");
                    fw.close();
                }
                    catch(FileNotFoundException e){
                        System.out.println("invalid file");
                    }
                catch(IOException io){
                    System.out.println("invalid operation, please get your life together.");
                }

                game.setScreen(new MainMenuScreen(game));


                return 1;
            }
            //other stuffs

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
        game.batch.draw(Assets.LevelEditBg, 0, 0, Options.screenWidth, Options.screenHeight);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        //draw dynamic elements here
        //the difference is is the blend(tm)

        //may have to get single duck sprite w/o the sprite sheet
        //may have to get single sprite for all of these actually!!!!!!!!!!!!!!!!
        //drawing multiple so it's easier to store in file and keep track\
        for (Duck f : dL){

            //WE MAY WANT TO IMPLEMENT ACCESOR METHODS TO GET CERTAIN COORDINATES OF EACH OBJECT!!!!!
            //WHETHER THAT OBJECT BE SHARK, LILY, OR DUCK
            //int i = dL.indexOf(f);

            game.batch.draw(Assets.duck, f.getXCord(), f.getYCord(), 96, 96);
    }
        for (Shark f : sL){

            //WE MAY WANT TO IMPLEMENT ACCESOR METHODS TO GET CERTAIN COORDINATES OF EACH OBJECT!!!!!
            //WHETHER THAT OBJECT BE SHARK, LILY, OR DUCK
            //int i = dL.indexOf(f);

            game.batch.draw(Assets.shark, 200, 0, 80, 96);
        }
        game.batch.draw(Assets.lily, 275, 0, 80, 96 );


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
        }
        draw();
    }
}
