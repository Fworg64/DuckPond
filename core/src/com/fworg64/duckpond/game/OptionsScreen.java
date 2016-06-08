package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 3/8/2016.
 *
 * JUST NEEDS CREDITS SCREEN AND WILL BE READY
 */
public class OptionsScreen extends ScreenAdapter
{
    public static final String credits = "Thank you BetaTesters.\n"
                                        +"Thank you Players.\n\n"
                                        +"Graphics\\Design:\n     Lois S. Dilone\n\n"
                                        +"Programing: \n     Austin F. Oltmanns\n     (Fworg64)\n\n"
                                        +"Privacy Policy:\n"
                                        +"Tcupdevelopment.me\\\n       privacypolicy.html\n\n"
                                        +"  7-8.0<>:!?1234566\n"
                                        +"       Tap to close.";

    int OPTEXIT_X;
    int OPTEXIT_Y;
    int OPTWIDTH;
    int OPTHEIGHT;
    int RES_X;
    int RES_Y;
    int RES_W;
    int RES_H;
    int RES_S;
    int SLIDER_X;
    int SLIDER_Y;
    int SLIDER_H;
    int SLIDER_W;
    int SLIDER_L;
    int SLIDER_S;
    int CREDITS_X;
    int CREDITS_Y;

    Rectangle SaveReturn;
    Rectangle StdRes;
    Rectangle HighRes;
    Rectangle MusicSlider;
    Rectangle SfxSlider;
    Rectangle CreditsButt;

    boolean returnPressed;
    boolean creditsPressed;

    boolean displaycredits;

    boolean slidingMusic;
    boolean slidingSfx;
    float tempMusicVol;
    float tempSfxVol;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    /*ShapeRenderer shapeRenderer;*/

    InputListener in;
    Vector2 touchpoint;

    public OptionsScreen (DuckPondGame game)
    {
        if (Options.highres)
        {
            OPTEXIT_X = (int)(50f/1080f * Options.screenWidth); //bottom left corner of saveandexit button for options
            OPTEXIT_Y = (int)((1-610f/1920f) * Options.screenHeight); //bottom left corner of saveandexit button for options
            OPTWIDTH = 303;
            OPTHEIGHT = 115;
            RES_X = (int)(113f/1080 * Options.screenWidth);//bottom left corner of lowres tickbox X
            RES_Y = (int)((1-902f/1920) * Options.screenHeight);//bottom left corner of lowres tickbox Y
            RES_W = (int)((93f/1080f) * Options.screenWidth);
            RES_H = RES_W;
            RES_S = (int)(565f/1080f * Options.screenWidth);
            SLIDER_X = (int)(297f/1080f * Options.screenWidth);
            SLIDER_Y = (int)((1-1260f/1920f) * Options.screenHeight);
            SLIDER_W =54;
            SLIDER_H =110;
            SLIDER_L = (int)(445f/1080f * Options.screenWidth);
            SLIDER_S = (int)(276f/1920f * Options.screenHeight);
            CreditsButt = new Rectangle(288, 1920-1761, 563, 170);
            CREDITS_X = 50;
            CREDITS_Y = 1920 - 200;
        }
        else
        {
            OPTEXIT_X = (int)(23f/640f * Options.screenWidth); //bottom left corner of saveandexit button for options
            OPTEXIT_Y = (int)((1-307f/960f) * Options.screenHeight); //bottom left corner of saveandexit button for options
            OPTWIDTH = 173;
            OPTHEIGHT = 64;
            RES_X = (int)(55f/640f * Options.screenWidth);//bottom left corner of lowres tickbox X
            RES_Y = (int)((1-443f/960f) * Options.screenHeight);//bottom left corner of lowres tickbox Y
            RES_W = (int)((63f/640f) * Options.screenWidth);
            RES_H = RES_W;
            RES_S = (int)(322f/640f * Options.screenWidth);
            SLIDER_X = (int)(160f/640f * Options.screenWidth);
            SLIDER_Y = (int)((1-642f/960f) * Options.screenHeight);
            SLIDER_W =31;
            SLIDER_H =55;
            SLIDER_L = (int)(250f/640f * Options.screenWidth);
            SLIDER_S = (int)(156f/960f * Options.screenHeight);
            CreditsButt = new Rectangle(176, 960-918, 307, 93);
            CREDITS_X = 35;
            CREDITS_Y = 960 - 100;
        }

        returnPressed = false;
        creditsPressed = false;

        slidingMusic = false;
        slidingSfx = false;
        tempMusicVol =0;
        tempSfxVol =0;

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        /*shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);*/

        SaveReturn = new Rectangle(OPTEXIT_X,OPTEXIT_Y,OPTWIDTH, OPTHEIGHT);
        StdRes = new Rectangle(RES_X, RES_Y, RES_W, RES_H);
        HighRes = new Rectangle(RES_X + RES_S, RES_Y, RES_W, RES_H);
        MusicSlider = new Rectangle(SLIDER_X + (Options.getMusicVol() * SLIDER_L), SLIDER_Y, SLIDER_W, SLIDER_H);
        SfxSlider = new Rectangle(SLIDER_X + (Options.getSfxVol() * SLIDER_L), SLIDER_Y-SLIDER_S, SLIDER_W, SLIDER_H);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();

        if (Gdx.app.getType() == Application.ApplicationType.Android) this.game.adStateListener.ShowBannerAd();
    }

    public void update()
    {
        if (!displaycredits)
        {
            touchpoint.set(in.getTouchpoint());
            if (in.justTouched()) Gdx.app.debug("TOCUH", touchpoint.toString());

            if (SaveReturn.contains(touchpoint) && in.justTouched()) {
                returnPressed = true;
                Assets.load_mainmenu();
            }
            if (returnPressed && !SaveReturn.contains(touchpoint)) returnPressed = false;
            if (returnPressed && !in.isTouched() || in.isBackPressed())
            {
                Options.save();
                Assets.load_font();
                Assets.load_mainmenu();
                game.setScreen(new MainMenuScreen(game));
                Assets.dispose_options();
            }
            if (CreditsButt.contains(touchpoint) && in.justTouched()) creditsPressed = true;
            if (creditsPressed && !CreditsButt.contains(touchpoint)) creditsPressed = false;
            if (creditsPressed && !in.isTouched())
            {
                displaycredits = true;
            }

            if (StdRes.contains(touchpoint) && in.justTouched() && Options.highres)
            {
                Options.setStdres();
            }
            if (HighRes.contains(touchpoint) && in.justTouched() && !Options.highres)
            {
                Options.setHighres();
            }
            if (MusicSlider.contains(touchpoint) && in.justTouched() && !slidingMusic) {
                slidingMusic = true;
            }
            if (slidingMusic && in.isTouched())
            {
                MusicSlider.setX(touchpoint.x -MusicSlider.getWidth()*.5f);
                if (touchpoint.x -MusicSlider.getWidth()*.5f< SLIDER_X) {MusicSlider.setX(SLIDER_X);}
                if (touchpoint.x -MusicSlider.getWidth()*.5f> (SLIDER_X + SLIDER_L)) {MusicSlider.setX(SLIDER_X + SLIDER_L);}
                Options.setMusicVol((MusicSlider.getX()-SLIDER_X)/SLIDER_L);
                game.mas.setMusicVol((MusicSlider.getX() - SLIDER_X)/SLIDER_L);
            }
            if (slidingMusic && !in.isTouched())
            {
                slidingMusic = false;
                Options.setMusicVol((MusicSlider.getX()-SLIDER_X)/SLIDER_L);
                game.mas.setMusicVol((MusicSlider.getX() - SLIDER_X) / SLIDER_L);
                Gdx.app.debug("Volset", Float.toString((MusicSlider.getX() - SLIDER_X) / SLIDER_L));
            }
            if (SfxSlider.contains(touchpoint) && in.justTouched() && !slidingSfx)
            {
                slidingSfx = true;
            }
            if (slidingSfx && in.isTouched())
            {
                SfxSlider.setX(touchpoint.x- SfxSlider.getWidth()*.5f);
                if (touchpoint.x - SfxSlider.getWidth()*.5f< SLIDER_X) SfxSlider.setX(SLIDER_X);
                if (touchpoint.x - SfxSlider.getWidth()*.5f> (SLIDER_X + SLIDER_L)) SfxSlider.setX(SLIDER_X +SLIDER_L);
                Options.setSfxVol((SfxSlider.getX() - SLIDER_X) / SLIDER_L);
                game.mas.setSfxVol((SfxSlider.getX() - SLIDER_X) / SLIDER_L);
            }
            if (slidingSfx && !in.isTouched())
            {
                slidingSfx = false;
                Options.setSfxVol((SfxSlider.getX()-SLIDER_X)/SLIDER_L);
                game.mas.setSfxVol((SfxSlider.getX()-SLIDER_X)/SLIDER_L);
                Gdx.app.debug("Sfxset", Float.toString((SfxSlider.getX()-SLIDER_X)/SLIDER_L));
            }
        }
        else
        {
            if (in.justTouched()) displaycredits = false;
        }

    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(.27451f, .70588f, .83922f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);
        game.batch.enableBlending();
        game.batch.begin();

        if (!displaycredits)
        {

            game.batch.draw(Assets.OptionsMenu, 0, 0);
            if (!Options.highres) game.batch.draw(Assets.OptionsMenuCheckMark, RES_X, RES_Y);
            else game.batch.draw(Assets.OptionsMenuCheckMark, RES_X + RES_S, RES_Y);
            if (returnPressed) game.batch.draw(Assets.OptionsMenuReturnPressed, SaveReturn.getX(), SaveReturn.getY());
            if (creditsPressed) game.batch.draw(Assets.OptionsMenuCreditsPressed, CreditsButt.getX(), CreditsButt.getY());
            game.batch.draw(slidingMusic ? Assets.OptionsMenuSliderPressed: Assets.OptionsMenuSlider, MusicSlider.getX(), MusicSlider.getY());
            game.batch.draw(slidingSfx ? Assets.OptionsMenuSliderPressed: Assets.OptionsMenuSlider, SfxSlider.getX(), SfxSlider.getY());
        }
        else {
            Assets.font.draw(game.batch, credits, CREDITS_X, CREDITS_Y);
        }

        game.batch.end();

        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(SaveReturn.getX(), SaveReturn.getY(), SaveReturn.getWidth(), SaveReturn.getHeight());
        shapeRenderer.rect(StdRes.getX(), StdRes.getY(), StdRes.getWidth(), StdRes.getHeight());
        shapeRenderer.rect(HighRes.getX(), HighRes.getY(), HighRes.getWidth(), HighRes.getHeight());
        shapeRenderer.rect(SfxSlider.getX(), SfxSlider.getY(), SfxSlider.getWidth(), SfxSlider.getHeight());
        shapeRenderer.rect(MusicSlider.getX(), MusicSlider.getY(), MusicSlider.getWidth(), MusicSlider.getHeight());
        shapeRenderer.end();*/
    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }
}
