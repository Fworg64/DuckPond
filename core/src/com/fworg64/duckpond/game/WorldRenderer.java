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
        batch.draw(Assets.GameBackground, 0, 0);
        batch.end();
    }

    private void renderObjects(float clock)
    {
        batch.enableBlending();
        batch.begin();
        if (!Options.highres)
        {
            for (Lily laura: world.pads) batch.draw(laura.padRot.getKeyFrame(clock), laura.pos.getX(), laura.pos.getY());
            for (Duck fred: world.ducks)
            {
                fred.sprite.draw(batch);
                for (Duckling f: fred.ducklings)
                {
                    f.sprite.setScale(.5f);
                    f.sprite.draw(batch);
                }
            }
            for (Shark sam: world.sharks) batch.draw(sam.currAnim.getKeyFrame(sam.clock), sam.pos.getX(), sam.pos.getY());
        }
        else
        {
            for (Lily laura: world.pads) batch.draw(laura.padRot.getKeyFrame(clock), laura.pos.getX()*DuckPondGame.highresworldscaler, laura.pos.getY()*DuckPondGame.highresworldscaler);
            for (Duck fred: world.ducks)
            {
                fred.sprite.setPosition(fred.pos.getX() *DuckPondGame.highresworldscaler, fred.pos.getY() *DuckPondGame.highresworldscaler);
                fred.sprite.draw(batch);
                for (Duckling f: fred.ducklings)
                {
                    f.sprite.setScale(.5f);
                    f.sprite.setPosition(f.pos.getX()*DuckPondGame.highresworldscaler,f.pos.getY()*DuckPondGame.highresworldscaler);
                    f.sprite.draw(batch);
                }
            }
            for (Shark sam: world.sharks) batch.draw(sam.currAnim.getKeyFrame(sam.clock), sam.pos.getX()*DuckPondGame.highresworldscaler, sam.pos.getY()*DuckPondGame.highresworldscaler);
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
            for (Duck fred: world.ducks)
            {
                shapeRenderer.circle(fred.col.x, fred.col.y, fred.col.radius);
                for (Duckling f: fred.ducklings)
                {
                    shapeRenderer.circle(f.col.x, f.col.y, f.col.radius);
                }
            }
            for (Shark sam: world.sharks) shapeRenderer.circle(sam.col.x, sam.col.y, sam.col.radius);
        }
        else
        {
            for (Lily laura: world.pads) shapeRenderer.circle(laura.col.x*DuckPondGame.highresworldscaler, laura.col.y*DuckPondGame.highresworldscaler, DuckPondGame.highresworldscaler*laura.col.radius);
            for (Duck fred: world.ducks)
            {
                shapeRenderer.circle(fred.col.x*DuckPondGame.highresworldscaler, fred.col.y*DuckPondGame.highresworldscaler, DuckPondGame.highresworldscaler*fred.col.radius);
                for (Duckling f: fred.ducklings)
                {
                    shapeRenderer.circle(f.col.x*DuckPondGame.highresworldscaler, f.col.y*DuckPondGame.highresworldscaler, f.col.radius*DuckPondGame.highresworldscaler);
                }
            }
            for (Shark sam: world.sharks) shapeRenderer.circle(sam.col.x*DuckPondGame.highresworldscaler, sam.col.y*DuckPondGame.highresworldscaler, DuckPondGame.highresworldscaler*sam.col.radius);
        }
        shapeRenderer.end();
    }
}
