package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * This file handles the nuts and bolts of rendering, things like draw order and whatnot
 * Resolution Aware
 * Created by fworg on 2/10/2016.
 */
public class WorldRenderer
{
    private World world;
    private OrthographicCamera cam;
    private SpriteBatch batch;

    private ShapeRenderer shapeRenderer;

    private float clock;

    public WorldRenderer (SpriteBatch batch, World world)
    {
        this.cam = new OrthographicCamera(Options.screenWidth,Options.screenHeight);
        this.cam.position.set(Options.screenWidth / 2, Options.screenHeight / 2, 0);
        this.world = world;
        this.batch = batch;
        cam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public void render(float clock)
    {
        batch.setProjectionMatrix(cam.combined);

        renderBackground();
        renderObjects(clock);
        renderCollisionBox(); //disable this line for release (duh)
    }

    private void renderBackground() {
        batch.disableBlending();
        batch.begin();
        batch.draw(Assets.GameBackground, 0, 0, Options.screenWidth, Options.screenHeight);
        batch.end();
    }

    private void renderObjects(float clock)
    {
        batch.enableBlending();
        batch.begin();
        if (!Options.highres)
        {
            for (Lily laura: world.pads) batch.draw(laura.padRot.getKeyFrame(clock), laura.pos.getX(), laura.pos.getY(), Options.spriteWidth, Options.spriteHeight);
            for (Duck fred: world.ducks)
            {
                batch.draw(fred.currAnim.getKeyFrame(clock), fred.pos.getX(), fred.pos.getY(), Options.spriteWidth, Options.spriteHeight);
                Gdx.app.debug("Duck", fred.pos.toString());
                for (Duckling f: fred.ducklings)
                {
                    batch.draw(fred.currAnim.getKeyFrame(clock), f.pos.getX(), f.pos.getY(), f.pos.getWidth(), f.pos.getHeight());
                    Gdx.app.debug("ducklingpos", f.pos.toString());
                }
            }
            for (Shark sam: world.sharks) batch.draw(sam.currAnim.getKeyFrame(sam.clock), sam.pos.getX(), sam.pos.getY(), Options.spriteWidth, Options.spriteHeight);
        }
        else
        {
            for (Lily laura: world.pads) batch.draw(laura.padRot.getKeyFrame(clock), laura.pos.getX()*2, laura.pos.getY()*2);
            for (Duck fred: world.ducks)
            {
                batch.draw(fred.currAnim.getKeyFrame(clock), fred.pos.getX()*2, fred.pos.getY()*2);
                for (Duckling f: fred.ducklings)
                {
                    batch.draw(fred.currAnim.getKeyFrame(clock), f.pos.getX()*2, f.pos.getY()*2, f.pos.getWidth()*2, f.pos.getHeight()*2);
                }
            }
            for (Shark sam: world.sharks) batch.draw(sam.currAnim.getKeyFrame(sam.clock), sam.pos.getX()*2, sam.pos.getY()*2);
        }
        batch.end();
    }

    private void renderCollisionBox()
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        if (!Options.highres)
        {
            for (Lily laura: world.pads) shapeRenderer.circle(laura.col.x, laura.col.y, laura.col.radius);
            for (Duck fred: world.ducks) shapeRenderer.circle(fred.col.x, fred.col.y, fred.col.radius);
            for (Shark sam: world.sharks) shapeRenderer.circle(sam.col.x, sam.col.y, sam.col.radius);
        }
        else
        {
            for (Lily laura: world.pads) shapeRenderer.circle(laura.col.x*2, laura.col.y*2, 2*laura.col.radius);
            for (Duck fred: world.ducks) shapeRenderer.circle(fred.col.x*2, fred.col.y*2, 2*fred.col.radius);
            for (Shark sam: world.sharks) shapeRenderer.circle(sam.col.x*2, sam.col.y*2, 2*sam.col.radius);
        }
        shapeRenderer.end();
    }
}
