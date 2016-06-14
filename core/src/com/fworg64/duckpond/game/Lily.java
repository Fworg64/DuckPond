package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


/**
 * This object is the "Goal" ducks should be directed here by the player
 *
 *
 * Not Resolution Aware
 * Created by fworg on 2/10/2016.
 */
public class Lily
{
    Rectangle pos;
    Circle col; //for collisions
    Sprite sprite;

    Random random;

    public Lily(float x, float y)
    {
        pos = new Rectangle(x, y, DuckPondGame.objWandH,DuckPondGame.objWandH);
        col = new Circle(pos.getX() + .5f* pos.getWidth(), pos.getY() + .5f* pos.getHeight(), .5f* pos.getWidth());
        sprite = new Sprite(Assets.lily);
        random = new Random();
        sprite.setOriginCenter();
        sprite.setRotation(random.nextFloat()*360);
    }
    public  String toString(){
        String s = "x: " + pos.x + " y: " + pos.y + " velocity: " + " velocity x: " + "\n";
        return s;
    }


    public void update()
    {
        //stuff to determine frame of animation
    }
}
