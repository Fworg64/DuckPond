package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * this is the actual gamescreen, no ads should go here
 * this file handles the GUI/HUD of the gameplay  and recieves input
 *
 * it is also responsible for calling World methods and WorldRenderer methods at appropiate times
 * as well as implementing sound
 *
 * while world.java handles game logic
 * and worldrenderer renders the world
 *
 * Not Resolution Aware
 * Created by fworg on 2/5/2016.
 */
public class GameScreen extends ScreenAdapter
{
    public final static float TIME_TO_RUN_AFTER_GAMEOVER_LOSE = 3.0f;
    public final static float SWYPE_FADE_TIME = .5f;
    public final static float SWYPE_ARROW_SCALE = 1.6f;
    enum Menus {PAUSEMENU, GMVICTORY, GMLOSE, PLAYING};

    public static int BOXOUTER_X;
    public static int BOXOUTER_Y;
    public static int PAUSETITLE_X;
    public static int PAUSETITLE_Y;

    DuckPondGame game;
    OrthographicCamera gcam;

    ShapeRenderer shapeRenderer;

    InputListener screenIn;
    Vector2 touchpointScreen;
    Vector2 touchpointWorld;
    float clock;

    MusicAndSounds mas;

    public World world;
    public World.WorldListener listener;
    public WorldRenderer renderer;

    public boolean beingswiped; //swiperfile?
    public boolean swiperegistered;
    public Vector2 swipestart;
    public Vector2 swipeend;
    public Vector2[] swipedraw;
    boolean drawswipe;
    float timeswipedrawn;

    private boolean isPaused;
    private boolean isMuted;
    private boolean ready2go;
    Menus menu;

    private Button playbutt;

    private Rectangle HUDarea;
    private Rectangle pausebutt;
    private Rectangle livesarea;
    private Rectangle mutebutt;
    private Button PAUSEUnpauseButt;
    private Button PAUSERestartButt;
    private Button PAUSELevelSelectionButt; //goes to level selectionscreen
    private Rectangle GOVLevelSelection;
    private Rectangle GOLLevelSelection;
    private Rectangle GOLrestart;

    private float TIME_RENDER_X;
    private float TIME_RENDER_Y;

    private float LEVELNAME_RENDER_X;
    private float LEVELNAME_RENDER_Y;

    private float gameoverRunTime;
    private boolean GAMEOVERMUSICFLAG;

    private boolean saydegeat;

    private String levelname;

    GameScreen(DuckPondGame game, String level, String levelname)
    {
        this.game = game;
        this.mas = game.mas;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth * .5f, Options.screenHeight * .5f, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        screenIn = new InputListener(Options.screenWidth, Options.screenHeight);

        game.mas.stopCurrMusic();
        game.mas.playGameMusic();

        touchpointScreen = new Vector2();
        touchpointWorld = new Vector2();
        clock =0;

        saydegeat = true;
        ready2go = false;

        listener = new World.WorldListener() //interface object?
        {
            @Override
            public void gameOverVictory()
            {
                isPaused = true;
                menu = Menus.GMVICTORY;
                Gdx.app.debug("gamestate", "VIctory!");
            }

            @Override
            public void gameOverLose()
            {
                isPaused = true;
                menu = Menus.GMLOSE;
                if (saydegeat) {Gdx.app.debug("gamestate", "DEGEAT");saydegeat = false;}
            }

            @Override
            public void chompNoise() {
                mas.playChomp();
            }

            @Override
            public void duckDeathNoise() {
                mas.playDucks();
            }
        }; //implement later... later is now! 2/21

        isPaused = false;
        isMuted = game.mas.isMuted;
        GAMEOVERMUSICFLAG = true;

        menu = Menus.PLAYING;

        world = new World(listener, level);
        world.LoadLevel();

        renderer = new WorldRenderer(game.batch, world);

        beingswiped = false;
        swiperegistered = false;
        swipestart = new Vector2();
        swipeend = new Vector2();
        swipedraw = new Vector2[4];
        drawswipe = false;
        timeswipedrawn =0;

        if (Options.highres)
        {
            BOXOUTER_X = 200;
            BOXOUTER_Y = 1920 - 1200;

            HUDarea = new Rectangle(0, Options.screenHeight-Options.GUIHeight, Options.screenWidth, Options.GUIHeight);
            pausebutt = new Rectangle(0,1920-241,241, 241);
            livesarea = new Rectangle(399, Options.screenHeight-Options.GUIHeight,216, 152 );
            mutebutt = new Rectangle(305, 1920-214, 133, 158);

            PAUSEUnpauseButt        = new Button(400,   1920-800,   478,    122,    Assets.GameContinue);
            PAUSERestartButt        = new Button(400,   1920-1000,  435,    111,    Assets.GameRestart);
            PAUSELevelSelectionButt = new Button(400,   1920-1200,  428,    104,    Assets.GameQuit);

            GOVLevelSelection = new Rectangle(50f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLLevelSelection = new Rectangle(250f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLrestart = new Rectangle(40f/640f *DuckPondGame.worldW, 10f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);

            playbutt = new Button(400, 800, 200, 100, Assets.GamePlay);

            TIME_RENDER_X = 7;
            TIME_RENDER_Y = 1920 - 310;
            LEVELNAME_RENDER_X = 500;
            LEVELNAME_RENDER_Y = 1920-150;
        }
        else
        {
            BOXOUTER_X = 150;
            BOXOUTER_Y = 960 - 800;

            HUDarea = new Rectangle(0, Options.screenHeight-Options.GUIHeight, Options.screenWidth, Options.GUIHeight);
            pausebutt = new Rectangle(0,960-93,93, 93);
            livesarea = new Rectangle(407,Options.screenHeight-Options.GUIHeight,74 , 42 );
            mutebutt = new Rectangle(117, 960-73, 42, 48);

            PAUSEUnpauseButt        = new Button(115f/640f *DuckPondGame.worldW, 350f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH, Assets.GameContinue);
            PAUSERestartButt        = new Button(115f/640f *DuckPondGame.worldW, 220f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH, Assets.GameRestart);
            PAUSELevelSelectionButt = new Button(115f/640f *DuckPondGame.worldW, 90f/960f * DuckPondGame.worldH, 415f/640f * DuckPondGame.worldW, 120f/915f *DuckPondGame.worldH , Assets.GameQuit);

            GOVLevelSelection = new Rectangle(50f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLLevelSelection = new Rectangle(250f/640f *DuckPondGame.worldW, 100f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);
            GOLrestart = new Rectangle(40f/640f *DuckPondGame.worldW, 10f/960f * DuckPondGame.worldH, 180f/640f * DuckPondGame.worldW, 180f/915f *DuckPondGame.worldH);

            playbutt = new Button(300, 300, 150, 75, Assets.GamePlay);

            TIME_RENDER_X = 400;
            TIME_RENDER_Y = 960 - 20; //top left?
            LEVELNAME_RENDER_X = 202;
            LEVELNAME_RENDER_Y = 960-50;
        }

        gameoverRunTime = TIME_TO_RUN_AFTER_GAMEOVER_LOSE;
        if (Gdx.app.getType() == Application.ApplicationType.Android) this.game.adStateListener.HideBannerAd();

        this.levelname = levelname;
    }

    public void update(float delta)
    {
        if (mutebutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
        {
            isMuted = !isMuted;
            if (isMuted) game.mas.mute();
            else game.mas.unmute();
        }
        if (isPaused ==false)
        {
            clock+=delta; //keep track of time

            if (screenIn.isTouched())
            {
                touchpointScreen.set(screenIn.getTouchpoint());
                if (touchpointScreen.y < Options.screenHeight - Options.GUIHeight)
                {
                    //touched the world
                    touchpointWorld.set(touchpointScreen.x * ((float)DuckPondGame.worldW/(float)Options.screenWidth),
                                        touchpointScreen.y * ((float)DuckPondGame.worldH/((float)Options.screenHeight -(float)Options.GUIHeight)));
                    //Gdx.app.debug("ToCUH", touchpointWorld.toString());

                }
            }

            if (screenIn.justTouched() && beingswiped ==false) //swipe is starting
            {
                //register swipe
                beingswiped = true;
                swipestart.set(touchpointWorld.x, touchpointWorld.y);
                Gdx.app.debug("SWIPESTART", swipestart.toString());
            }
            else if (screenIn.isTouched() && beingswiped ==true) //swipe in progess
            {
                swipeend.set(touchpointWorld.x, touchpointWorld.y);
                if (swipeend.cpy().sub(swipestart).len() >=Options.spriteWidth*.7) //swipe over, long enough
                {
                    Gdx.app.debug("SWIPECUT", swipestart.toString() + '\n' + swipeend.toString());
                    beingswiped = false;
                    swiperegistered = true;
                }
                swipedraw[0] = swipestart.cpy();
                swipedraw[3] = swipeend.cpy();
                Vector2 tmp = swipestart.cpy().lerp(swipeend, .875f); // point between begining and end
                Vector2 othertmp = tmp.cpy().sub(swipeend); //one of the wings, ish
                swipedraw[1] = new Vector2(tmp.x - othertmp.y, tmp.y + othertmp.x);
                swipedraw[2] = new Vector2(tmp.x + othertmp.y, tmp.y - othertmp.x);
                Vector2 thirdtemp = swipedraw[0].cpy().scl(SWYPE_ARROW_SCALE).sub(swipestart);
                if (Options.highres) swipedraw[0].scl(SWYPE_ARROW_SCALE);
                for (int i=1;i<swipedraw.length;i++)
                    {
                        swipedraw[i].scl(SWYPE_ARROW_SCALE);
                        if (!Options.highres) swipedraw[i].sub(thirdtemp);
                    }


                drawswipe = true;
                timeswipedrawn =0;
            }
            else if ( !screenIn.isTouched() && beingswiped ==true)//swipe is over, user terminated
            {
                beingswiped = false;
                swiperegistered = true;
                Gdx.app.debug("SWIPEEND", swipeend.toString());
            }

            if (swiperegistered)
            {
                world.update(delta, swipestart, swipeend);
                swiperegistered = false;
                Gdx.app.debug("Swipe Registered", swipestart.toString() + '\n' + swipeend.toString() + '\n' + Float.toString(swipeend.cpy().sub(swipestart).len2()));
            } else world.update(delta, swipestart, swipestart.cpy()); //probably a better way to implement this

            if ((pausebutt.contains(screenIn.getTouchpoint()) && screenIn.justTouched()) || screenIn.isBackPressed())
            {
                isPaused = true;
                menu = Menus.PAUSEMENU;
            }

        }
        else
        {
            Vector2 touchpoint = screenIn.isTouched() ? screenIn.getTouchpoint(): new Vector2();
            switch (menu)
            {
                case PAUSEMENU:
                    PAUSEUnpauseButt.pollPress(touchpoint);
                    PAUSELevelSelectionButt.pollPress(touchpoint);
                    PAUSERestartButt.pollPress(touchpoint);
                    game.mas.pauseSfx();
                    game.mas.pauseCurrMusic();
                    if (PAUSEUnpauseButt.isWasPressed())
                    {
                        isPaused = false;
                        game.mas.playGameMusic();
                        game.mas.resumeSfx();

                        screenIn.getTouchpoint(); //?
                        menu = Menus.PLAYING;
                        PAUSEUnpauseButt.pressHandled();
                    }
                    if (PAUSELevelSelectionButt.isJustPressed())
                    {
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        Assets.load_levelscreen();
                        Assets.load_navigation();
                    }
                    if (PAUSELevelSelectionButt.isWasPressed())
                    {
                        game.setScreen(new LevelSelectionScreen(game));
                        Assets.dispose_gamescreen();
                        this.dispose();
                    }
                    if (PAUSERestartButt.isWasPressed())
                    {
                        world.ReloadLevel();
                        clock = 0;
                        gameoverRunTime = TIME_TO_RUN_AFTER_GAMEOVER_LOSE;
                        menu = Menus.PLAYING;
                        isPaused = false;
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        game.mas.playGameMusic();
                        GAMEOVERMUSICFLAG = true;
                        PAUSERestartButt.pressHandled();
                    }
                    break;
                case GMLOSE:
                    if (GAMEOVERMUSICFLAG)
                    {
                        if (mas.currSong == MusicAndSounds.CurrSong.GAME) mas.stopCurrMusic();
                        mas.playGameOverMusic();
                        GAMEOVERMUSICFLAG = false;
                    }

                    if (GOLLevelSelection.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
                    {
                        mas.stopCurrMusic();
                        mas.stopSfx();
                        Assets.load_levelscreen();
                        Assets.load_navigation();
                        game.setScreen(new LevelSelectionScreen(game));
                        Assets.dispose_gamescreen();
                        this.dispose();                    }
                    if (GOLrestart.contains(screenIn.getTouchpoint()) && screenIn.justTouched())
                    {
                        world.ReloadLevel();
                        clock = 0;
                        gameoverRunTime = TIME_TO_RUN_AFTER_GAMEOVER_LOSE;
                        menu = Menus.PLAYING;
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        game.mas.playGameMusic();
                        isPaused = false;
                        GAMEOVERMUSICFLAG = true;
                    }
                    if (gameoverRunTime>0)
                    {
                        clock+=delta;
                        gameoverRunTime -=delta;
                        world.update(delta, new Vector2(), new Vector2());
                    }
                    break;
                case GMVICTORY:
                    if (GAMEOVERMUSICFLAG)
                    {
                        Gdx.app.debug("MUSIC", "STOPCURR + PLAY VICT");
                        if (mas.currSong == MusicAndSounds.CurrSong.GAME) mas.stopCurrMusic();
                        mas.playVictoryMusic();
                        GAMEOVERMUSICFLAG = false;
                    }

                    if (GOVLevelSelection.contains(screenIn.getTouchpoint()) && screenIn.justTouched()) {
                        game.mas.stopCurrMusic();
                        game.mas.stopSfx();
                        Assets.load_levelscreen();
                        Assets.load_navigation();
                        game.setScreen(new LevelSelectionScreen(game));
                        Assets.dispose_gamescreen();
                        this.dispose();                    }
                    break;
            }
        }
    }

    public void draw(float delta)
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        renderer.render(clock);
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.HUD, HUDarea.getX(), HUDarea.getY());
        Assets.font.draw(game.batch, "Time: " + Integer.toString((int) (world.time >0 ? world.time:0)), TIME_RENDER_X, TIME_RENDER_Y);
        Assets.font.draw(game.batch, levelname, LEVELNAME_RENDER_X, LEVELNAME_RENDER_Y);
        switch (world.lives)
        {
            case 3:
                game.batch.draw(Assets.HUDlives,livesarea.getX() + 2*livesarea.getWidth(), livesarea.getY());
            case 2:
                game.batch.draw(Assets.HUDlives,livesarea.getX() + livesarea.getWidth(), livesarea.getY());
            case 1:
                game.batch.draw(Assets.HUDlives, livesarea.getX(), livesarea.getY());
            case 0:
                break;
        }
        game.batch.draw(isMuted ? Assets.HUDMute : Assets.HUDUnmute, mutebutt.getX(), mutebutt.getY());
        //draw other HUD shtuf
        game.batch.end();

        if (isPaused)
        {
            game.batch.enableBlending();
            game.batch.begin();
            switch (menu)
            {
                case PAUSEMENU:
                    game.batch.draw(Assets.GameBoxOuter, BOXOUTER_X,BOXOUTER_Y);
                    game.batch.draw(Assets.GamePaused, PAUSETITLE_X, PAUSETITLE_Y);
                    PAUSEUnpauseButt.renderSprites(game.batch);
                    PAUSERestartButt.renderSprites(game.batch);
                    PAUSELevelSelectionButt.renderSprites(game.batch);
                    break;
                case GMLOSE:
                    //game.batch.draw(Assets.Defeat, 0,0);
                    break;
                case GMVICTORY:
                    //game.batch.draw(Assets.Victory, 0,0);
                    break;
            }
            game.batch.end();
        }

        if (drawswipe)
        {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(.2f, .5f, .5f, (1f - (timeswipedrawn / SWYPE_FADE_TIME) * (timeswipedrawn / SWYPE_FADE_TIME) * (timeswipedrawn / SWYPE_FADE_TIME)));
            shapeRenderer.triangle(swipedraw[0].x, swipedraw[0].y, swipedraw[1].x, swipedraw[1].y, swipedraw[3].x, swipedraw[3].y);
            shapeRenderer.triangle(swipedraw[0].x, swipedraw[0].y, swipedraw[2].x, swipedraw[2].y, swipedraw[3].x, swipedraw[3].y);
            shapeRenderer.end();
            timeswipedrawn +=delta;
            if (timeswipedrawn >= SWYPE_FADE_TIME)
            {
                drawswipe = false;
                timeswipedrawn =0;
            }
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(pausebutt.getX(), pausebutt.getY(), pausebutt.getWidth(), pausebutt.getHeight());
        shapeRenderer.rect(HUDarea.getX(), HUDarea.getY(), HUDarea.getWidth(), HUDarea.getHeight());
        if (menu == Menus.PAUSEMENU)
        {
            PAUSEUnpauseButt.renderShapes(shapeRenderer);
            PAUSERestartButt.renderShapes(shapeRenderer);
            PAUSELevelSelectionButt.renderShapes(shapeRenderer);
        }
        if (menu == Menus.GMLOSE)
        {
            shapeRenderer.rect(GOLLevelSelection.getX(), GOLLevelSelection.getY(), GOLLevelSelection.getWidth(), GOLLevelSelection.getHeight());
            shapeRenderer.rect(GOLrestart.getX(), GOLrestart.getY(), GOLrestart.getWidth(), GOLrestart.getHeight());
        }
        if (menu == Menus.GMVICTORY)
        {
            shapeRenderer.rect(GOVLevelSelection.getX(), GOVLevelSelection.getY(), GOVLevelSelection.getWidth(), GOVLevelSelection.getHeight());
        }

        shapeRenderer.end();

    }

    @Override
    public void render (float delta)
    {
        if (!ready2go)
        {
            Vector2 touchpoint = screenIn.isTouched() ? screenIn.getTouchpoint(): new Vector2();
            update(0);
            playbutt.pollPress(touchpoint);
            if (playbutt.isWasPressed())
            {
                ready2go = true;
            }

            draw(0);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            playbutt.renderShapes(shapeRenderer);
            shapeRenderer.end();
            game.batch.enableBlending();
            game.batch.begin();
            playbutt.renderSprites(game.batch);
            game.batch.end();
        }
        else {
            update(delta);
            draw(delta);
        }

    }
}
