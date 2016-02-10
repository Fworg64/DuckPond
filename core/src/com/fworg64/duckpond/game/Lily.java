package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by fworg on 2/10/2016.
 */
public class Lily
{
    Rectangle pos;
    Circle col; //for collisions

    public Lily()
    {
        pos = new Rectangle(160, 360, 96, 96);
        col = new Circle(pos.getX() + .5f* pos.getWidth(), pos.getY() + .5f* pos.getHeight(), .5f* pos.getWidth());
    }

    public void update()
    {
        //stuff to determine frame of animation
    }
}
