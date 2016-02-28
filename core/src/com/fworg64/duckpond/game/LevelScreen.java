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
    public boolean beingswiped; //swiperfile?
    public boolean swiperegistered;
    public Vector2 swipestart;
    public Vector2 swipeend;
    String swipedebug;

    private ShapeRenderer shapeRenderer;//this helps for checking button rectangles

    public LevelScreen(DuckPondGame game)
    {

        Assets.load();
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera

        exitbutt = new Rectangle(1,1,20,20); //this isn't exact yet
        duckCounter=0;
        //intialize all necessary variables for the editor
        dL = new ArrayList<Duck>();
        dL.add(duckCounter, new Duck(200, 0, 96, 96));
        ducks = new Rectangle(200,0,96,96);
        duckCounter = 1;
        sL = new ArrayList<Shark>();
        sL.add(sharkCounter,new Shark(300,0,96,96));
        sharks = new Rectangle(400,0,50,96);
        sharkCounter = 1;
        lillies = new Rectangle(275,0,50,96);
        exitbutt = new Rectangle(EXIT_X, EXIT_Y, EXIT_W, EXIT_H);

        in = new InputListener();
        touchpoint = new Vector2();

        beingswiped = false;
        swiperegistered = false;
        swipestart = new Vector2();
        swipeend = new Vector2();
        swipedebug = "herp";

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);
    }
//plz work
    public int update()
    {
        //code that gets run each frame goes here
        if (in.justTouched())  //you just got touched son
        {
            touchpoint.set(in.getTouchpoint());
            if (in.justTouched() && beingswiped ==false) //swipe is starting
            {
                touchpoint.set(in.getTouchpoint());

                //register swipe
                beingswiped = true;
                swipestart.set(touchpoint.x, touchpoint.y);
                swipedebug = "TOCUH";
            }
            else if (in.isTouched() && beingswiped ==true) //swipe in progess
            {
                touchpoint.set(in.getTouchpoint());
                swipeend.set(touchpoint.x, touchpoint.y);
            }
            else if ( !in.isTouched() && beingswiped ==true)//swipe is over
            {
                beingswiped = false;
                swiperegistered = true;
                swipedebug = "NO TOCUH";
            }

            if (swiperegistered)
            {
               // world.update(delta, swipestart, swipeend);
                dL.get(duckCounter).pos.x = swipeend.x;
                dL.get(duckCounter).pos.y = touchpoint.y;
                duckCounter++;
                dL.add(duckCounter, new Duck(200, 0, 96, 96));
                swiperegistered = false;
                Gdx.app.debug("Swipe Registered",swipestart.toString() + '\n'+swipeend.toString());
            }
           // else world.update(delta,swipestart, swipestart.cpy()); //probably a better way to implement this

                if (ducks.contains(touchpoint.x, touchpoint.y)) {
                    //adds new duck the user wants to have in level
                    dL.add(duckCounter, new Duck(200, 0, 96, 96));
                    //test to see if ducks being touched
                    System.out.println(duckCounter);
                }
            //need to think of better algorithm to move the ducks
            //may need to implement for shark as well as well lilypad
            /**
            if (touchpoint.x >= dL.get(duckCounter).pos.x) {
                dL.get(duckCounter).pos.x = touchpoint.x;
                duckCounter++;
                dL.add(duckCounter, new Duck(100, 0, 96, 96));
            } else if (touchpoint.y > dL.get(duckCounter).pos.y) {
                dL.get(duckCounter).pos.y = touchpoint.y;
                duckCounter++;
                dL.add(duckCounter, new Duck(100, 0, 96, 96));
            } else if (touchpoint.x > dL.get(duckCounter).pos.x && touchpoint.y > dL.get(duckCounter).pos.y) {
                dL.get(duckCounter).pos.x = touchpoint.x;
                dL.get(duckCounter).pos.y = touchpoint.y;
                duckCounter++;
                dL.add(duckCounter, new Duck(100, 0, 96, 96));
            }
           //duck counter?
            **/



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
                    //fw.append("amount of ducks(),amount of sharks,?lilypad placement?");

                    //********fw.append wasn't defined until after java 1.6, unfortunatly this means we cant use it with the html version
                    //******** however, i dont think file saving would be the same anyway...

                    // new line character is \n pretty sure xD
                    //fw.append("\n");
                    fw.close();
                }
                    catch(FileNotFoundException e){
                        System.out.println("invalid file");
                    }
                catch(IOException io){
                    System.out.println("invalid operation, please get your life together.");
                }

               // game.setScreen(new MainMenuScreen(game));


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

            game.batch.draw(Assets.duckSwimSideRightFrames.first(), f.pos.x, f.pos.y, 96, 96);
    }
        for (Shark f : sL){

            //WE MAY WANT TO IMPLEMENT ACCESOR METHODS TO GET CERTAIN COORDINATES OF EACH OBJECT!!!!!
            //WHETHER THAT OBJECT BE SHARK, LILY, OR DUCK
            //int i = dL.indexOf(f);

            game.batch.draw(Assets.sharkSwimFrames.first(), f.pos.x, f.pos.y, 80, 96);
        }
        game.batch.draw(Assets.lilyRotFrames.first(),lillies.x, lillies.y, 80, 96 );


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
