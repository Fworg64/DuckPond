package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by fworg on 2/17/2016.
 */
public class LevelScreen extends ScreenAdapter
{
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera

    Vector3 touchpoint; //input vector

    Rectangle exitbutt;

    private ShapeRenderer shapeRenderer;//this helps for checking button rectangles

    public LevelScreen(DuckPondGame game)
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera

        exitbutt = new Rectangle(0,0,100,100); //this isn't exact yet

        touchpoint = new Vector3();

        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);
    }

    public int update()
    {
        //code that gets run each frame goes here
        if (Gdx.input.justTouched())  //you just got touched son
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0)); //store recent touchpoint in vector for handling

            if (exitbutt.contains(touchpoint.x, touchpoint.y))
            {
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
        game.batch.draw(Assets.LevelEditBgStd, 0, 0, 320, 480);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        //draw dynamic elements here
        //the difference is is the blend(tm)
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f,.2f,.2f,.5f);
        //draw detection bounds here for debugging
        shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
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
