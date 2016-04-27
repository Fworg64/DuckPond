package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    public enum Direction {RIGHT, UP, LEFT, DOWN}; //CCW for magic


    State state;
    public float clock;
    public Rectangle pos;
    public Circle col;

    Vector2 vel;
    Vector2 posv;

    public Animation currAnim;
    public Sprite sprite;
    public Direction dir;

    public Shark (float x, float y, float xv, float yv)
    {
        state = State.SWIMMING;
        clock =0;

        pos = new Rectangle(x, y, DuckPondGame.objWandH,DuckPondGame.objWandH);
        posv = new Vector2(pos.getX(), pos.getY());
        vel = new Vector2(xv,yv);
        col = new Circle(pos.getX() + .5f* pos.getWidth(),pos.getY() + .5f*pos.getHeight(), .3f* pos.getWidth());

        currAnim = Assets.sharkSwimLeftAnim;
        dir = Direction.LEFT;
        sprite = new Sprite(currAnim.getKeyFrame(clock));

    }

    public void update(float delta)
    {
        clock += delta;
        posv.add(vel.cpy().scl(delta));
        pos.setPosition(posv);
        col.setPosition(pos.getX() + .5f* pos.getWidth(),pos.getY() + .5f*pos.getHeight());

        if (state == State.EATING)
        {
            currAnim = Assets.sharkEatAnim;
            if (currAnim.isAnimationFinished(clock))
            {
                state = State.SWIMMING;
            }
        }
        setSprite();
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

    private void setSprite()
    {
        float ang = vel.angle();
        if (state == State.SWIMMING)
        {

            if (ang >=45 && ang <135) {currAnim = Assets.sharkSwimUpAnim; dir = Direction.UP;}
            else if (ang >=135 &&  ang <225) {currAnim = Assets.sharkSwimLeftAnim; dir = Direction.LEFT;}
            else if (ang >=225 && ang <315) {currAnim = Assets.sharkSwimDownAnim; dir = Direction.DOWN;}
            else {currAnim = Assets.sharkSwimRightAnim; dir = Direction.RIGHT;}
        }
        sprite = new Sprite(currAnim.getKeyFrame(clock));
        sprite.setPosition(pos.getX(), pos.getY());
        sprite.setOriginCenter();
        if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
        if (dir == Direction.RIGHT && vel.angle() <90) sprite.setRotation(ang*.3f);
        if (dir == Direction.RIGHT && vel.angle() >270) sprite.setRotation((ang-360)*.3f +360);
    }

}
