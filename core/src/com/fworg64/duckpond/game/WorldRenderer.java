package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by fworg on 2/10/2016.
 */
public class WorldRenderer
{
    private World world;
    private OrthographicCamera cam;
    private SpriteBatch batch;

    public WorldRenderer (SpriteBatch batch, World world)
    {
        this.cam = new OrthographicCamera(320,480);
        this.cam.position.set(320 / 2, 480 / 2, 0);
        this.world = world;
        this.batch = batch;
        cam.update();
    }

    public void render()
    {
        batch.setProjectionMatrix(cam.combined);

        renderBackground();
        renderObjects();
        renderDebug();
    }

    private void renderBackground()
    {
        batch.disableBlending();
        batch.begin();
        batch.draw(Assets.GameBackground, 0, 0, 320, 480);
        batch.end();
    }

    private void renderObjects()
    {
        batch.enableBlending();
        batch.begin();
        for (Lily laura: world.pads) batch.draw(Assets.lily, laura.pos.getX(), laura.pos.getY());
        for (Duck fred: world.ducks) batch.draw(Assets.duck[fred.state], fred.pos.getX(), fred.pos.getY());
        batch.end();
    }

    private void renderDebug()
    {
        batch.begin();
        Assets.font.draw(batch,world.debug,10,70);
        batch.end();
    }
}
