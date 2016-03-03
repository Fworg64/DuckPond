package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by fworg on 2/12/2016.
 *
 * Not Resolution Aware
 */
public class Shark
{
    public enum State {SWIMMING, EATING};

    State state;
    public float clock;
    public Rectangle pos;
    public Circle col;

    Vector2 vel;
    Vector2 posv;

    private Animation eatAnim;
    private Animation swimAnim;
    public Animation currAnim;

    public Shark (float x, float y, float xv, float yv)
    {
        state = State.SWIMMING;
        clock =0;

        pos = new Rectangle(x, y, DuckPondGame.spriteW,DuckPondGame.spriteH);
        posv = new Vector2(pos.getX(), pos.getY());
        vel = new Vector2(xv,yv);
        col = new Circle(pos.getX() + .5f* pos.getWidth(),pos.getY() + .5f*pos.getHeight(), .3f* pos.getWidth());

        swimAnim = new Animation(.2f, Assets.sharkSwimFrames, Animation.PlayMode.LOOP);
        eatAnim = new Animation(.2f, Assets.sharkEatFrames, Animation.PlayMode.NORMAL);
        currAnim = swimAnim;
    }

    public void update(float delta)
    {
        clock += delta;
        posv.add(vel.cpy().scl(delta));
        pos.setPosition(posv);
        col.setPosition(pos.getX() + .5f* pos.getWidth(),pos.getY() + .5f*pos.getHeight());

        if (state == State.EATING)
        {
            currAnim = eatAnim;
            if (currAnim.isAnimationFinished(clock))
            {
                state = State.SWIMMING;
                currAnim = swimAnim;
            }
        }
    }

    public void eatDuck(Duck duck)
    {
        state = State.EATING;
        clock =0;
    }
    public  String toString(){
        String s = "x: " + pos.x + " y: " + pos.y + " velocity: " + " velocity x: " + this.vel.x + " velocity y: " + this.vel.y + "\n";
        return s;
    }

}
