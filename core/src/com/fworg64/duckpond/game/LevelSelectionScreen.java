package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fworg on 3/6/2016.
 */
public class LevelSelectionScreen extends ScreenAdapter
{
    public final String CUSTOM_FOLDER_NAME = "CUSTOM";
    public int LEVEL_LOAD_X ;
    public int LEVEL_LOAD_Y;
    public int LEVEL_LOAD_W ;
    public int LEVEL_LOAD_H ;
    public int LEVEL_LOAD_XS;
    public int LEVEL_LOAD_YS;
    public final static int LEVEL_LOAD_R =3;
    public final static int LEVEL_LOAD_C =2;

    int LEVEL_X;
    int LEVEL_Y;
    int LEVEL_W;
    int LEVEL_H;

    public int UP_ONE_X;
    public int UP_ONE_Y;
    public int UP_ONE_W;
    public int UP_ONE_H;

    public int PAGE_RIGHT_X;
    public int PAGE_RIGHT_Y;
    public int PAGE_LEFT_X;
    public int PAGE_LEFT_Y;
    public int PAGE_W;
    public int PAGE_H;
    public final int PAGE_SIZE = 6;
    public int pagenumber;


    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    FileHandle levelDir;
    ArrayList<FileHandle> levels;

    Rectangle[] levelbutts;
    Rectangle customlevelbutt;
    Rectangle upone;
    Rectangle pageleftbutt;
    Rectangle pagerightbutt;
    Rectangle leveleditbutt;

    Rectangle mainMenubutt;

    boolean leveleditPressed;

    public LevelSelectionScreen (DuckPondGame game)
    {
        if (Options.highres)
        {
            LEVEL_LOAD_X = 200;
            LEVEL_LOAD_Y = 1920 -550;
            LEVEL_LOAD_W = 200;
            LEVEL_LOAD_H = 200;
            LEVEL_LOAD_XS = 300;
            LEVEL_LOAD_YS = 300;

            LEVEL_X = (int)(800f/1080f* Options.screenWidth);
            LEVEL_Y = (int)((1- 1762f/1920f)* Options.screenHeight);

            UP_ONE_X = 700;
            UP_ONE_Y = 1920 - 1500;
            UP_ONE_W = 200;
            UP_ONE_H = 200;

            PAGE_RIGHT_X = 650;
            PAGE_RIGHT_Y = 1920 - 1700;
            PAGE_LEFT_X = 50;
            PAGE_LEFT_Y = 1920 - 1700;
            PAGE_W = 200;
            PAGE_H = 200;
        }
        else
        {
            LEVEL_LOAD_X = 100;
            LEVEL_LOAD_Y = 960 -250;
            LEVEL_LOAD_W = 100;
            LEVEL_LOAD_H = 100;
            LEVEL_LOAD_XS = 150;
            LEVEL_LOAD_YS = 150;
            LEVEL_X = (int)(500f/640f* Options.screenWidth);
            LEVEL_Y = (int)((1-846f/960f)* Options.screenHeight);
            UP_ONE_X = 400;
            UP_ONE_Y = 960 - 700;
            UP_ONE_W = 100;
            UP_ONE_H = 100;

            PAGE_RIGHT_X = 400;
            PAGE_RIGHT_Y = 960 - 800;
            PAGE_LEFT_X = 300;
            PAGE_LEFT_Y = 960 -800;
            PAGE_W = 100;
            PAGE_H = 100;
        }

        LEVEL_W = (int)(165f/640f * Options.screenWidth);
        LEVEL_H = (int)(156f/960f * Options.screenHeight);

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        game.mas.playMainMenu();

        levelDir = Gdx.files.internal("LEVELS\\");
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        levels.remove(0); //remove custom world folder

        pagenumber =0;

        mainMenubutt = new Rectangle(10f/640f * Options.screenWidth, 800f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        customlevelbutt = new Rectangle(100f/640f * Options.screenWidth, 150f/960f * Options.screenHeight, 100f/640f * Options.screenWidth, 100f/960f * Options.screenHeight);

        levelbutts = new Rectangle[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
               levelbutts[i*(LEVEL_LOAD_R) + j] = new Rectangle(LEVEL_LOAD_X + i*LEVEL_LOAD_XS, LEVEL_LOAD_Y - j*LEVEL_LOAD_YS, LEVEL_LOAD_W, LEVEL_LOAD_H);
            }
        }
        upone = new Rectangle(UP_ONE_X, UP_ONE_Y, UP_ONE_W, UP_ONE_H);
        pageleftbutt = new Rectangle(PAGE_LEFT_X, PAGE_LEFT_Y, PAGE_W, PAGE_H);
        pagerightbutt = new Rectangle(PAGE_RIGHT_X, PAGE_RIGHT_Y, PAGE_W, PAGE_H);
        leveleditbutt = new Rectangle(LEVEL_X, LEVEL_Y, LEVEL_W, LEVEL_H);
        leveleditPressed = false;

        if (Gdx.app.getType() == Application.ApplicationType.Android)
        {
            Gdx.app.debug("AD","LEVELSELETCSHOW");
            this.game.adStateListener.ShowBannerAd();
        }
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        for(int i=0; i<levelbutts.length;i++)
        {
            if (in.justTouched() && levelbutts[i].contains(touchpoint))
            {
                if (levels.size() > i)
                {
                    if (!levels.get(i).isDirectory()) game.setScreen(new GameScreen(game, levels.get(i).readString()));
                    else
                    {
                        levelDir = levels.get(i);
                        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
                        if (levelDir.name().equals("LEVELS")) levels.remove(0); //remove custom folder

                        break;
                    }
                }
            }
        }
        if (in.justTouched() && customlevelbutt.contains(touchpoint))
        {
            if (Gdx.app.getType() != Application.ApplicationType.WebGL)
            {
                levelDir = Gdx.files.local("LEVELS\\" + CUSTOM_FOLDER_NAME);
                levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
            }
            else game.setScreen(new GameScreen(game, Options.getCustom1()));
        }
        if (in.justTouched() && upone.contains(touchpoint))
        {
            if (!levelDir.name().equals("LEVELS"))
            {
                levelDir = levelDir.parent(); //take us to LEVELS
                levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
                levels.remove(0); //remove custom folder
            }
        }
        if ((in.justTouched() && mainMenubutt.contains(touchpoint)) || in.isBackPressed())
        {
            game.setScreen(new MainMenuScreen(game));
        }
        if ((in.justTouched() && pageleftbutt.contains(touchpoint)) && levelDir.name().equals(CUSTOM_FOLDER_NAME))
        {
            if (pagenumber>0) pagenumber--;
            levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
            levels = new ArrayList<FileHandle>(levels.subList(pagenumber * PAGE_SIZE, (pagenumber+1)*PAGE_SIZE));
        }
        if ((in.justTouched() && pagerightbutt.contains(touchpoint) && levelDir.name().equals(CUSTOM_FOLDER_NAME)))
        {
            Gdx.app.debug("pagerighttocuh", Integer.toString(levelDir.list().length /6));
            if (levelDir.list().length / PAGE_SIZE > pagenumber) pagenumber++;
            levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
            for (int i =0; i<pagenumber*PAGE_SIZE; i++)
            {
                levels.remove(0);
            }
        }
        if (leveleditbutt.contains(touchpoint) && in.justTouched()) leveleditPressed = true;
        if (leveleditPressed && !leveleditbutt.contains(touchpoint)) leveleditPressed = false;
        if (leveleditPressed && !in.isTouched())
        {
            game.setScreen(new LevelScreen2(game));
        }
    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);
        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(Assets.LevelSelectionBackground, 0, 0);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(leveleditPressed ? Assets.MainMenuLevelEditorPressed : Assets.MainMenuLevelEditor, leveleditbutt.getX(), leveleditbutt.getY());
        Assets.font.draw(game.batch, "Return to MainMenu", mainMenubutt.getX(), mainMenubutt.getY());
        Assets.font.draw(game.batch, "Custom Level", customlevelbutt.getX(), customlevelbutt.getY());
        for (int i=0; i< (levelbutts.length<levels.size() ? levelbutts.length : levels.size()); i++) Assets.font.draw(game.batch, levels.get(i).nameWithoutExtension(), levelbutts[i].getX(), levelbutts[i].getY());
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for(Rectangle r: levelbutts) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(customlevelbutt.getX(), customlevelbutt.getY(), customlevelbutt.getWidth(), customlevelbutt.getHeight());
        shapeRenderer.rect(mainMenubutt.getX(), mainMenubutt.getY(), mainMenubutt.getWidth(), mainMenubutt.getHeight());
        shapeRenderer.rect(upone.getX(), upone.getY(), upone.getWidth(), upone.getHeight());
        shapeRenderer.rect(pageleftbutt.getX(), pageleftbutt.getY(), pageleftbutt.getWidth(), pageleftbutt.getHeight());
        shapeRenderer.rect(pagerightbutt.getX(), pagerightbutt.getY(), pagerightbutt.getWidth(), pagerightbutt.getHeight());
        shapeRenderer.rect(leveleditbutt.getX(), leveleditbutt.getY(), leveleditbutt.getWidth(), leveleditbutt.getHeight());
        shapeRenderer.end();
    }

    @Override
    public void render (float delta)
    {
        update();
        draw();
    }
}
