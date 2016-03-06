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
 *
 * Created by fworg on 2/4/2016.
 */
public class MainMenuScreen extends ScreenAdapter
{
    final static int PLAY_X = (int)(.309f* DuckPondGame.worldW);
    final static int PLAY_Y = (int)(.41f* DuckPondGame.worldH);
    final static int LEVEL_X = (int)(.1f* DuckPondGame.worldW);
    final static int LEVEL_Y = (int)(.253f* DuckPondGame.worldH);
    final static int OPTIONS_X = (int)(.5f* DuckPondGame.worldW);
    final static int OPTIONS_Y = (int)(.253f* DuckPondGame.worldH);
    final static int EXIT_X = (int)(.309f* DuckPondGame.worldW);
    final static int EXIT_Y = (int)(.134f* DuckPondGame.worldH);
    final static int BUTT_W = (int)(.35f*DuckPondGame.worldW);
    final static int BUTT_H = (int)(.09f*DuckPondGame.worldH);

    final static int OPTEXIT_X = (int)(.135f * DuckPondGame.worldW); //bottom left corner of saveandexit button for options
    final static int OPTEXIT_Y = (int)(.62f * DuckPondGame.worldH); //bottom left corner of saveandexit button for options
    final static int OPTWIDTH = (int)(.33f * DuckPondGame.worldW); // width of options exit buttons
    final static int OPTHEIGHT = (int)(.094f * DuckPondGame.worldH); //height of exit buttons
    final static int RES_X = (int)(62./320. * DuckPondGame.worldW);//bottom left corner of lowres tickbox X
    final static int RES_Y = (int)(224./480. * DuckPondGame.worldH);//bottom left corner of lowres tickbox Y
    final static int RES_W = Options.GUIWidth;
    final static int RES_H = Options.GUIHeight;
    final static int RES_S = (int)(130./320. * DuckPondGame.worldW);

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
    Rectangle StdRes;
    Rectangle HighRes;
    Rectangle PremiumButt;
    Rectangle CreditsButt;


    public MainMenuScreen (DuckPondGame game)
    {
        this.game = game;
        resetCam(); //also sets shaperendererererer

        playbutt = new Rectangle(PLAY_X, PLAY_Y, BUTT_W, BUTT_H);
        optionbutt = new Rectangle(OPTIONS_X, OPTIONS_Y, BUTT_W, BUTT_H);
        leveleditbutt = new Rectangle(LEVEL_X, LEVEL_Y, BUTT_W, BUTT_H);
        exitbutt = new Rectangle(EXIT_X, EXIT_Y, BUTT_W, BUTT_H);

        SaveReturn = new Rectangle(OPTEXIT_X,OPTEXIT_Y,OPTWIDTH, OPTHEIGHT);
        StdRes = new Rectangle(RES_X, RES_Y, RES_W, RES_H);
        HighRes = new Rectangle(RES_X + RES_S, RES_Y, RES_W, RES_H);

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
                    game.setScreen(new LevelSelectionScreen(game));
                    return 2;
                }
                if (leveleditbutt.contains(touchpoint.x, touchpoint.y))
                {
                    game.setScreen(new LevelScreen2(game));
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
                if (StdRes.contains(touchpoint.x, touchpoint.y) && Options.highres)
                {
                    Options.loadDefault();
                    Assets.load();
                    resetCam();
                }
                if (HighRes.contains(touchpoint.x, touchpoint.y) && !Options.highres)
                {
                    Options.setHighres();
                    Assets.load();
                    resetCam();
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
            game.batch.enableBlending();
            game.batch.begin();
            game.batch.draw(Assets.OptionsMenu, 0, 0, Options.screenWidth, Options.screenHeight);
            if (Options.highres) game.batch.draw(Assets.check, (RES_X + RES_S)*2, RES_Y*2);
            else game.batch.draw(Assets.check, RES_X, RES_Y);
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
            shapeRenderer.rect(StdRes.getX(), StdRes.getY(), StdRes.getWidth(), StdRes.getHeight());
            shapeRenderer.rect(HighRes.getX(), HighRes.getY(), HighRes.getWidth(), HighRes.getHeight());
            shapeRenderer.end();
        }
    }

    private void resetCam()
    {
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);
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
