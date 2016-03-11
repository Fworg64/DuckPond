package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 3/8/2016.
 */
public class OptionsScreen extends ScreenAdapter
{
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
    int SLIDER_W;
    int SLIDER_S;

    Rectangle SaveReturn;
    Rectangle StdRes;
    Rectangle HighRes;
    Rectangle MusicSlider;
    Rectangle SfxSlider;
    Rectangle PremiumButt;
    Rectangle CreditsButt;

    boolean slidingMusic;
    boolean slidingSfx;
    float tempMusicVol;
    float tempSfxVol;

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    public OptionsScreen (DuckPondGame game)
    {
        OPTEXIT_X = (int)(.135f * Options.screenWidth); //bottom left corner of saveandexit button for options
        OPTEXIT_Y = (int)(.62f * Options.screenHeight); //bottom left corner of saveandexit button for options
        OPTWIDTH = (int)(.33f * Options.screenWidth); // width of options exit buttons
        OPTHEIGHT = (int)(.094f * Options.screenHeight); //height of exit buttons
        RES_X = (int)(62./320. * Options.screenWidth);//bottom left corner of lowres tickbox X
        RES_Y = (int)(224./480. * Options.screenHeight);//bottom left corner of lowres tickbox Y
        RES_W = Options.GUIWidth;
        RES_H = Options.GUIHeight;
        RES_S = (int)(130./320. * Options.screenWidth);
        SLIDER_X = (int)(62f/320f * Options.screenWidth);
        SLIDER_Y = (int)(200f/480f * Options.screenHeight);
        SLIDER_W = (int)(130f/320f * Options.screenWidth);
        SLIDER_S = (int)(50f/320f * Options.screenHeight);

        slidingMusic = false;
        slidingSfx = false;
        tempMusicVol =0;
        tempSfxVol =0;

        this.game = game;
        gcam = new OrthographicCamera(Options.screenWidth, Options.screenHeight);
        gcam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0); //give ourselves a nice little camera
        gcam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gcam.combined);

        SaveReturn = new Rectangle(OPTEXIT_X,OPTEXIT_Y,OPTWIDTH, OPTHEIGHT);
        StdRes = new Rectangle(RES_X, RES_Y, RES_W, RES_H);
        HighRes = new Rectangle(RES_X + RES_S, RES_Y, RES_W, RES_H);
        MusicSlider = new Rectangle(SLIDER_X + (Options.getMusicVol() * SLIDER_W), SLIDER_Y, Options.GUIWidth, Options.GUIHeight);
        SfxSlider = new Rectangle(SLIDER_X + (Options.getSfxVol() * SLIDER_W), SLIDER_Y-SLIDER_S, Options.GUIWidth, Options.GUIHeight);

        in = new InputListener(Options.screenWidth, Options.screenHeight);
        touchpoint = new Vector2();
    }

    public void update()
    {
        touchpoint.set(in.getTouchpoint());
        if (in.justTouched()) Gdx.app.debug("TOCUH", touchpoint.toString());

        if ((SaveReturn.contains(touchpoint) && in.justTouched()) || in.isBackPressed())
        {
            Options.save();
            game.setScreen(new MainMenuScreen(game));
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
            MusicSlider.setX(touchpoint.x);
            if (touchpoint.x < SLIDER_X) {MusicSlider.setX(SLIDER_X);}
            if (touchpoint.x > (SLIDER_X + SLIDER_W)) {MusicSlider.setX(SLIDER_X + SLIDER_W);}
            Options.setMusicVol((MusicSlider.getX()-SLIDER_X)/SLIDER_W);
            Assets.mainmenuloop.setVolume(Options.getMusicVol());
        }
        if (slidingMusic && !in.isTouched())
        {
            slidingMusic = false;
            Options.setMusicVol((MusicSlider.getX()-SLIDER_X)/SLIDER_W);
            Assets.mainmenuloop.setVolume(Options.getMusicVol());
            Gdx.app.debug("Volset", Float.toString((MusicSlider.getX()-SLIDER_X)/SLIDER_W));
        }
        if (SfxSlider.contains(touchpoint) && in.justTouched() && !slidingSfx)
        {
            slidingSfx = true;
        }
        if (slidingSfx && in.isTouched())
        {
            SfxSlider.setX(touchpoint.x);
            if (touchpoint.x < SLIDER_X) SfxSlider.setX(SLIDER_X);
            if (touchpoint.x > (SLIDER_X + SLIDER_W)) SfxSlider.setX(SLIDER_X +SLIDER_W);
        }
        if (slidingSfx && !in.isTouched())
        {
            slidingSfx = false;
            Options.setSfxVol((SfxSlider.getX()-SLIDER_X)/SLIDER_W);
            Gdx.app.debug("Volset", Float.toString((SfxSlider.getX()-SLIDER_X)/SLIDER_W));
        }
    }

    public void draw()
    {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
        gcam.update();
        game.batch.setProjectionMatrix(gcam.combined);

        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(Assets.OptionsMenu, 0, 0);
        if (!Options.highres) game.batch.draw(Assets.check, RES_X, RES_Y);
        else game.batch.draw(Assets.check, RES_X + RES_S, RES_Y);
        game.batch.draw(Assets.slideball, MusicSlider.getX(), MusicSlider.getY());
        game.batch.draw(Assets.slideball, SfxSlider.getX(), SfxSlider.getY());
        game.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(SaveReturn.getX(), SaveReturn.getY(), SaveReturn.getWidth(), SaveReturn.getHeight());
        shapeRenderer.rect(StdRes.getX(), StdRes.getY(), StdRes.getWidth(), StdRes.getHeight());
        shapeRenderer.rect(HighRes.getX(), HighRes.getY(), HighRes.getWidth(), HighRes.getHeight());
        shapeRenderer.rect(SfxSlider.getX(), SfxSlider.getY(), SfxSlider.getWidth(), SfxSlider.getHeight());
        shapeRenderer.rect(MusicSlider.getX(), MusicSlider.getY(), MusicSlider.getWidth(), MusicSlider.getHeight());
        shapeRenderer.end();
    }

    @Override
    public void render(float delta)
    {
        update();
        draw();
    }
}
