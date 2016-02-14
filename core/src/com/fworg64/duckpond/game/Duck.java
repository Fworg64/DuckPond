package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * These are the objects the player has control over
 * they should be animated
 *
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

    float dtheta; //when flicked this is how fast to rotate in rad/s
    Vector2 flickedinto;
    Vector2 vel;
    Vector2 posv;


    private Animation swimmingAnim;
    private Animation padAnim;
    private Animation eatenAnim;
    public Animation currAnim;

    public Duck(float x, float y, float vx, float vy)
    {
        pos = new Rectangle(x,y,96,96); //make this random for default constructor
        col = new Circle(pos.getX() + .3f* pos.getWidth(), pos.getY() + .2f* pos.getHeight(), .3f* pos.getWidth()); //this needs moved to just the base

        dtheta =0; //1 if rotating CCW, -1 for CW, 0 for no rotation
        vel = new Vector2(vx, vy); //must be floats... measured in whatever/sec
        flickedinto = vel.cpy();
        posv = new Vector2(pos.getX(),pos.getY());

        state = State.SWIMMING;

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
        if (state == State.PAD) currAnim = padAnim;
        if (state == State.EATEN)
        {
            currAnim = eatenAnim;
            if (currAnim.isAnimationFinished(clock))
            {
                state = State.DEAD;

            }
        }
    }

    public void flick (Vector2 flick)
    {
        flickedinto =flick.cpy();
        if (flickedinto.angle(vel) >0) dtheta = -1;
        else dtheta =1;
    }

    public void pad()
    {
        state = State.PAD;
    }

    public void getEaten(Shark shark)
    {
        state = State.EATEN;
        clock =0;
    }

}
