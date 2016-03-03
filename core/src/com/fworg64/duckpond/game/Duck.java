package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    public enum Direction {RIGHT, UP, LEFT, DOWN}; //CCW for magic

    State state;

    public float clock;
    Rectangle pos; //make a default obj class with a rect
    Circle col; //for collisions

    float dtheta; //direction to rotate 1/-1 for CW/CCW, or maybe CCW/CW.. it works
    Vector2 flickedinto;
    Vector2 vel;
    Vector2 posv;

    ArrayList<Duckling> ducklings;

    private Animation swimUpAnim;
    private Animation swimSideRightAnim;
    private Animation swimSideLeftAnim;
    private Animation swimDownAnim;
    private Animation padAnim;
    private Animation eatenAnim;
    public Animation currAnim;
    public Sprite sprite;
    Direction dir;

    public Duck(float x, float y, float vx, float vy) {
        pos = new Rectangle(x, y, DuckPondGame.spriteW, DuckPondGame.spriteH); //make this random for default constructor
        col = new Circle(pos.getX() + .5f * pos.getWidth(), pos.getY() + .5f * pos.getHeight(), .4f * pos.getWidth()); //this needs moved to just the base

        dtheta = 0; //1 if rotating CCW, -1 for CW, 0 for no rotation
        vel = new Vector2(vx, vy); //must be floats... measured in worldunits/sec
        flickedinto = vel.cpy();
        posv = new Vector2(pos.getX(), pos.getY());

        state = State.SWIMMING;

        ducklings = new ArrayList<Duckling>(5);
        for (int i = 1; i < 6; i++)
        {
            ducklings.add(new Duckling((int)(pos.getX()),(int)(pos.getY()), 30));
        }

        swimUpAnim = new Animation(.2f, Assets.duckSwimUpFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimDownAnim = new Animation(.2f, Assets.duckSwimDownFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimSideRightAnim = new Animation(.2f, Assets.duckSwimSideRightFrames, Animation.PlayMode.LOOP_PINGPONG);
        swimSideLeftAnim = new Animation(.2f, Assets.duckSwimSideLeftFrames, Animation.PlayMode.LOOP_PINGPONG);
        padAnim = new Animation(.2f, Assets.duckPadFrames, Animation.PlayMode.LOOP);
        eatenAnim = new Animation(.2f, Assets.duckEatenFrames, Animation.PlayMode.NORMAL);
        currAnim = swimUpAnim;
        dir = Direction.UP;
        sprite = new Sprite(currAnim.getKeyFrame(clock));

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
        if (state == State.PAD)
        {
            if (vel.len() * clock >= DuckPondGame.spriteW*1.0f) vel.setZero();
        }

        posv.add(vel.cpy().scl(delta)); //nother vector for good measure
        pos.setPosition(posv); //pos + vel*time = new pos
        col.setPosition(pos.getX()+ .5f * pos.getWidth(), pos.getY() + .5f* pos.getHeight());

        //stuff to determine frame animation
        setSprite(); //note, also determines when dead


        for (int i=0;i<ducklings.size();i++)
        {
            if (i==0) ducklings.get(i).follow(this.posv);
            else ducklings.get(i).follow(ducklings.get(i-1).posv);
            ducklings.get(i).update(delta); //while were iterating...
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
        dtheta =0;
        clock =0;

    }

    public void getEaten(Shark shark)
    {
        state = State.EATEN;
        clock =0;
    }

    private void setSprite()
    {
        float ang = vel.angle();
        if (state == State.SWIMMING)
        {

            if (ang >=45 && ang <135) {currAnim = swimUpAnim; dir = Direction.UP;}
            else if (ang >=135 &&  ang <225) {currAnim = swimSideLeftAnim; dir = Direction.LEFT;}
            else if (ang >=225 && ang <315) {currAnim = swimDownAnim; dir = Direction.DOWN;}
            else {currAnim = swimSideRightAnim; dir = Direction.RIGHT;}
        }
        if (state == State.PAD)
        {
            if (vel.isZero()) currAnim = padAnim;
        }
        if (state == State.EATEN)
        {
            currAnim = eatenAnim;
            if (currAnim.isAnimationFinished(clock))
            {
                state = State.DEAD;

            }
        }

        sprite = new Sprite(currAnim.getKeyFrame(clock));
        sprite.setPosition(pos.getX(), pos.getY());
        sprite.setOriginCenter();
        if (dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
        if (dir == Direction.RIGHT && vel.angle() <90) sprite.setRotation(ang*.3f);
        if (dir == Direction.RIGHT && vel.angle() >270) sprite.setRotation((ang-360)*.3f +360);

    }
    public  String toString(){
        String s = "x: " + pos.x + " y: " + pos.y + " velocity: " + " velocity x: " + this.vel.x + " velocity y: " + this.vel.y + "\n";
        return s;
    }

}
