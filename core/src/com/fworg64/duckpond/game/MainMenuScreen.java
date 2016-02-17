package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;



/**
 * This class handles the main menu, also it should handle the level selection menu and the options menu
 * and launch the level editor and from the level selection menu launch the game
 *
 * the reason to keep all these menus in one file is so that the ads served on the first menu can be carried
 * over to level selection and options (technically they will all be the same screen)
 *
 * Created by fworg on 2/4/2016.
 */
public class MainMenuScreen extends ScreenAdapter
{
    final static int MENU_X = 64; //top left corner of menu buttons
    final static int MENU_Y = 235;//top left corner of menu buttons
    final static int MENU_WIDTH = 192; //width of buttons
    final static int BUTT_HEIGHT = 45; //Height of buttons, must be < space
    final static int MENU_SPACE = 64;//height of button and gap between the next

    boolean showOptions;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    Rectangle playbutt; //more buttons later
    Rectangle optionbutt;
    Rectangle exitbutt;
    Rectangle leveleditbutt;

    Rectangle SaveReturn;
    Rectangle ExitNoSave;
    Rectangle StdRes;
    Rectangle HighRes;
    Rectangle PremiumButt;
    Rectangle CreditsButt;

    Vector3 touchpoint; //input vector

    String touchpointstr;
    String buttbounds;

    public MainMenuScreen (DuckPondGame game) //fuck your one true brace
    {
        this.game = game;
        gcam = new OrthographicCamera(320, 480);
        gcam.position.set(320 / 2, 480 / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        playbutt = new Rectangle(MENU_X, MENU_Y, MENU_WIDTH, BUTT_HEIGHT); //bounds for button
        leveleditbutt = new Rectangle(MENU_X, MENU_Y - MENU_SPACE, MENU_WIDTH,BUTT_HEIGHT);
        optionbutt = new Rectangle(MENU_X, MENU_Y - 2*MENU_SPACE, MENU_WIDTH,BUTT_HEIGHT);
        exitbutt = new Rectangle(MENU_X, MENU_Y - 3*MENU_SPACE, MENU_WIDTH,BUTT_HEIGHT);

        SaveReturn = new Rectangle(43,297,106, 45);
        ExitNoSave = new Rectangle(171, 297, 106, 45);

        showOptions = false;

        touchpoint = new Vector3(); //input vector3, 3 for compatibilliyt

        touchpointstr ="";
        //buttbounds =String.format("%.1f, %.1f", playbutt.getX(), playbutt.getY());

    }

    public void update() //FYOTB
    {
        if (Gdx.input.justTouched())
        {
            gcam.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0)); //this is kinda odd
            //touchpointstr = String.format("%.1f, %.1f", touchpoint.x, touchpoint.y);

            if (showOptions ==false)
            {
                if (playbutt.contains(touchpoint.x, touchpoint.y))
                {
                    game.debug = "play pressed";
                    game.setScreen(new GameScreen(game));
                    return;
                }
                if (optionbutt.contains(touchpoint.x, touchpoint.y))
                {
                    showOptions = true;
                }
                if (exitbutt.contains(touchpoint.x, touchpoint.y))
                {
                    Gdx.app.exit();
                }
            }
            if (showOptions ==true)
            {
                if (SaveReturn.contains(touchpoint.x, touchpoint.y))
                {
                    showOptions =false;
                }
                if (ExitNoSave.contains(touchpoint.x, touchpoint.y))
                {
                    showOptions =false;
                }
            }

            else game.debug = "WAHT A MISS";
        }
    }

    public void draw() //fyotb
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        if (showOptions == false)
        {
            game.batch.disableBlending();
            game.batch.begin();
            game.batch.draw(Assets.MainMenuBackgroundStd, 0, 0, 320, 480);
            game.batch.end();
        }
        else if (showOptions ==true)
        {
            game.batch.disableBlending();
            game.batch.begin();
            game.batch.draw(Assets.OptionsMenuStd, 0, 0, 320, 480);
            game.batch.end();
        }


        if (showOptions ==false)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(.5f, .2f, .2f, .5f);
            shapeRenderer.rect(playbutt.getX(), playbutt.getY(), playbutt.getWidth(), playbutt.getHeight());
            shapeRenderer.rect(leveleditbutt.getX(), leveleditbutt.getY(), leveleditbutt.getWidth(), leveleditbutt.getHeight());
            shapeRenderer.rect(optionbutt.getX(), optionbutt.getY(), optionbutt.getWidth(), optionbutt.getHeight());
            shapeRenderer.rect(exitbutt.getX(), exitbutt.getY(), exitbutt.getWidth(), exitbutt.getHeight());
            shapeRenderer.end();
        }
        else if (showOptions ==true)
        {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(.5f, .2f, .2f, .5f);
            shapeRenderer.rect(SaveReturn.getX(), SaveReturn.getY(), SaveReturn.getWidth(), SaveReturn.getHeight());
            shapeRenderer.rect(ExitNoSave.getX(), ExitNoSave.getY(), ExitNoSave.getWidth(), ExitNoSave.getHeight());
            shapeRenderer.end();
        }

        //debug text
//        game.batch.begin();
//        Assets.font.draw(game.batch, game.debug, 50, 450);
//        Assets.font.draw(game.batch, buttbounds, 50, 400);
//        Assets.font.draw(game.batch, touchpointstr, 50, 350);
//        game.batch.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
