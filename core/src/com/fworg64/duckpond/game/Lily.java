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
    public static final int MAXROT = 90;
    public static final int MAXSPEED = 30;

    Rectangle pos;
    Circle col; //for collisions
    public Sprite sprite;

    Random random;

    float amount2go;
    float amountgone;
    boolean direction;

    float speed;

    public Lily(float x, float y)
    {
        pos = new Rectangle(x, y, DuckPondGame.objWandH,DuckPondGame.objWandH);
        col = new Circle(pos.getX() + .5f* pos.getWidth(), pos.getY() + .5f* pos.getHeight(), .5f* pos.getWidth());
        sprite = new Sprite(Assets.lily);
        sprite.setPosition(pos.getX(), pos.getY());
        random = new Random();
        sprite.setOriginCenter();
        sprite.setRotation(random.nextFloat()*360);

        amount2go = random.nextFloat() * MAXROT;
        amountgone = 0;
        direction = random.nextBoolean();
    }
    public  String toString(){
        String s = "x: " + pos.x + " y: " + pos.y + " velocity: " + " velocity x: " + "\n";
        return s;
    }


    public void update(float delta)
    {
        //stuff to determine frame of animation
        if (amountgone>=amount2go)
        {
            amountgone=0;
            amount2go = random.nextFloat()*MAXROT;
            direction = !direction;
        }
        float tempx = (2*amountgone/amount2go -1);
        speed = -tempx*tempx*(2-tempx*tempx)+1.1f; //MUST BE GREATERTHAN 0;

        sprite.rotate(speed*MAXSPEED*delta * (direction ? -1: 1));
        amountgone+=speed*MAXSPEED*delta;

    }
}
