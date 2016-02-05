package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 2/5/2016.
 */
public class Duck
{
    Rectangle pos; //make a default obj class with a rect

    Vector2 vel;
    Vector2 accl;

    public Duck()
    {
        pos = new Rectangle(-100,50,96,96); //make this random for default constructor

        vel = new Vector2(.5f, .2f); //must be floats...
        accl = new Vector2(0,0);

    }

    public void update(float delta)
    {
        vel.add(accl);
        pos.setPosition(new Vector2(pos.getX(), pos.getY()).add(vel));

        //stuff to determine frame of animation

    }

}
