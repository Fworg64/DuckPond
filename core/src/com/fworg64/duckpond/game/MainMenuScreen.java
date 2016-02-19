package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;



/**
 * This class handles the main menu, also it should handle the level selection menu and the options menu
 * and launch the level editor and from the level selection menu launch the game
 *
 * the reason to keep all these menus in one file is so that the ads served on the first menu can be carried
 * over to level selection and options (technically they will all be the same screen)
 *
 * Not Resolution Aware
 *
 * Created by fworg on 2/4/2016.
 */
public class MainMenuScreen extends ScreenAdapter
{
    final static int MENU_X = (int)(.2f * DuckPondGame.worldW); //bottom left corner of first menu button
    final static int MENU_Y = (int)(.49f * DuckPondGame.worldH);//bottom left corner of first menu button
    final static int MENU_WIDTH = (int)(.6f * DuckPondGame.worldW); //width of buttons
    final static int BUTT_HEIGHT = (int)(.1f * DuckPondGame.worldH); //Height of buttons, must be < space
    final static int MENU_SPACE = (int)(.133f * DuckPondGame.worldH);//height of button and gap between the next

    final static int OPTEXIT_X = (int)(.135f * DuckPondGame.worldW); //bottom left corner of saveandexit button for options
    final static int OPTEXIT_Y = (int)(.62f * DuckPondGame.worldH); //bottom left corner of saveandexit button for options
    final static int OPTWIDTH = (int)(.33f * DuckPondGame.worldW); // width of options exit buttons
    final static int OPTHEIGHT = (int)(.094f * DuckPondGame.worldH); //height of exit buttons
    final static int OPTSPACE = (int)(.4f * DuckPondGame.worldW); //width of button and distance to next on xaxis

    boolean showOptions;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

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


    public MainMenuScreen (DuckPondGame game) //fuck your one true brace
    {
        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        playbutt = new Rectangle(MENU_X, MENU_Y, MENU_WIDTH, BUTT_HEIGHT); //bounds for button
        leveleditbutt = new Rectangle(MENU_X, MENU_Y - MENU_SPACE, MENU_WIDTH,BUTT_HEIGHT);
        optionbutt = new Rectangle(MENU_X, MENU_Y - 2*MENU_SPACE, MENU_WIDTH,BUTT_HEIGHT);
        exitbutt = new Rectangle(MENU_X, MENU_Y - 3*MENU_SPACE, MENU_WIDTH,BUTT_HEIGHT);

        SaveReturn = new Rectangle(OPTEXIT_X,OPTEXIT_Y,OPTWIDTH, OPTHEIGHT);
        ExitNoSave = new Rectangle(OPTEXIT_X + OPTSPACE, OPTEXIT_Y, OPTWIDTH, OPTHEIGHT);

        showOptions = false;

        in = new InputListener();
        touchpoint = new Vector2();

    }

    public int update() //FYOTB
    {
        if (in.justTouched())
        {
            touchpoint.set(in.getTouchpoint());
            Gdx.app.debug("TOCUH", touchpoint.toString());

            if (showOptions ==false)
            {
                if (playbutt.contains(touchpoint.x, touchpoint.y))
                {
                    game.debug = "play pressed";
                    game.setScreen(new GameScreen(game));
                    return 2;
                }
                if (leveleditbutt.contains(touchpoint.x, touchpoint.y))
                {
                    game.setScreen(new LevelScreen(game));
                    return 1;
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

        return 0;
    }

    public void draw() //fyotb
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        if (showOptions == false)
        {
            game.batch.disableBlending();
            game.batch.begin();
            game.batch.draw(Assets.MainMenuBackground, 0, 0, Options.screenWidth, Options.screenHeight);
            game.batch.end();
        }
        else if (showOptions ==true)
        {
            game.batch.disableBlending();
            game.batch.begin();
            game.batch.draw(Assets.OptionsMenu, 0, 0, Options.screenWidth, Options.screenHeight);
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
    }

    @Override
    public void render (float delta)
    {
        switch (update()) //because we can.
        {
            case 0:
                break;
            case 1:
                this.dispose();
                break;
            case 2:
                this.dispose();
                break;
        }

        draw();
    }
}
