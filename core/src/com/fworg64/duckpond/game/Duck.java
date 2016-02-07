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
    Vector2 posv;

    public Duck()
    {
        pos = new Rectangle(-100,50,96,96); //make this random for default constructor

        vel = new Vector2(50.0f, 20.0f); //must be floats... measured in whatever/sec
        accl = new Vector2(0,0);
        posv = new Vector2(-100,50);

    }

    public void update(float delta)
    {
        if (accl.len2() >= 1) accl.scl(.95f); //damping of accl, if its bigger than a small number, shrink it. taking the len2 is faster than the len
        else accl.setZero(); // if its smaller, zero it.

        vel.add(accl.cpy().scl(delta)); //velocity + accel * time = new vel
        posv.add(vel.cpy().scl(delta)); //nother vector for good measure
        pos.setPosition(posv); //pos + vel*time = new pos

        //stuff to determine frame of animation

    }

    public void flick (Vector2 flickv)
    {
        accl.set(flickv.clamp(500,2000)); //clamp flick to min/max floats
    }

}
