package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;


/**
 * This object is the "Goal" ducks should be directed here by the player
 *
 * Created by fworg on 2/10/2016.
 */
public class Lily
{
    Rectangle pos;
    Circle col; //for collisions

    public Animation padRot;

    public Lily(float x, float y)
    {
        pos = new Rectangle(x, y, 96, 96);
        col = new Circle(pos.getX() + .5f* pos.getWidth(), pos.getY() + .5f* pos.getHeight(), .5f* pos.getWidth());

        padRot = new Animation(.2f, Assets.lilyRotFrames, Animation.PlayMode.LOOP_PINGPONG);
    }

    public void update()
    {
        //stuff to determine frame of animation
    }
}
