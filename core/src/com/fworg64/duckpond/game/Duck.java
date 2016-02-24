package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * These are the objects the player has control over
 * they should be animated
 *
 * Not Resolution Aware
 * Created by fworg on 2/5/2016.
 */
public class Duck
{
    public static final float rotConst = .03f; //constant for adjusting rotation speed
    public enum State {SWIMMING, PAD, EATEN, DEAD}; //mainly used for animation

    State state;
    public float clock;
    Rectangle pos; //make a default obj class with a rect
    Circle col; //for collisions

    float dtheta; //direction to rotate 1/-1 for CW/CCW, or maybe CCW/CW.. it works
    Vector2 flickedinto;
    Vector2 vel;
    Vector2 posv;

    ArrayList<Duckling> ducklings;

    private Animation swimmingAnim;
    private Animation padAnim;
    private Animation eatenAnim;
    public Animation currAnim;

    public Duck(float x, float y, float vx, float vy) {
        pos = new Rectangle(x, y, DuckPondGame.spriteW, DuckPondGame.spriteH); //make this random for default constructor
        col = new Circle(pos.getX() + .3f * pos.getWidth(), pos.getY() + .2f * pos.getHeight(), .3f * pos.getWidth()); //this needs moved to just the base

        dtheta = 0; //1 if rotating CCW, -1 for CW, 0 for no rotation
        vel = new Vector2(vx, vy); //must be floats... measured in worldunits/sec
        flickedinto = vel.cpy();
        posv = new Vector2(pos.getX(), pos.getY());

        state = State.SWIMMING;

        ducklings = new ArrayList<Duckling>(5);
        for (int i = 1; i < 6; i++)
        {
            ducklings.add(new Duckling((int)(pos.getX()),(int)(pos.getY()), 25));
        }

        swimmingAnim = new Animation(.2f, Assets.duckSwimFrames, Animation.PlayMode.LOOP_PINGPONG);
        padAnim = new Animation(.2f, Assets.duckPadFrames, Animation.PlayMode.LOOP);
        eatenAnim = new Animation(.2f, Assets.duckEatenFrames, Animation.PlayMode.NORMAL);
        currAnim = swimmingAnim;

    }

    public void update(float delta)
    {
        clock +=delta;

        if (dtheta !=0)
        {
            vel.rotate(dtheta * rotConst * vel.len()); //velocity is constant-ish in magnitude
            if (vel.dot(flickedinto) / (vel.len()*flickedinto.len()) > .9997) dtheta =0; //if the vectors are close enough in angle, no more turning
            //A dot B = mag(A)*mag(B)*cos(T) : the .9998 gives us +/- 1.40 deg of indifference (cos^-1())
            //if the .999X is too close to 1, the duck gets confused...
        }

        posv.add(vel.cpy().scl(delta)); //nother vector for good measure
        pos.setPosition(posv); //pos + vel*time = new pos
        col.setPosition(pos.getX()+ .3f * pos.getWidth(), pos.getY() + .2f* pos.getHeight());

        //stuff to determine frame animation
        if (state == State.PAD)
        {
            currAnim = padAnim;
            if (vel.len() * clock >= DuckPondGame.spriteW*.7f) vel.setZero();
        }
        if (state == State.EATEN)
        {
            currAnim = eatenAnim;
            if (currAnim.isAnimationFinished(clock))
            {
                state = State.DEAD;

            }
        }

        for (int i=0;i<ducklings.size();i++)
        {
            if (i==0) ducklings.get(i).follow(this.posv);
            else ducklings.get(i).follow(ducklings.get(i-1).posv);
        }
    }

    public void flick (Vector2 flick)
    {
        flickedinto =flick.cpy();
        if (flickedinto.angle(vel) >0) dtheta = -1;
        else dtheta =1;
    }

    public void pad(Lily pad)
    {
        state = State.PAD;
        vel.set(pad.pos.getCenter(new Vector2()).sub(this.pos.getCenter(new Vector2())));
        vel.setLength(50);
        clock =0;

    }

    public void getEaten(Shark shark)
    {
        state = State.EATEN;
        clock =0;
    }

}
