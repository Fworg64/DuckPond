package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fworg on 2/20/2016.
 */
public class Duckling
{
    public static final int ducklingDistConst = 50; //world units
    public static final float CRUMB_SPACE_TIME = .5f;
    public enum State {INITIALIZING, SWIMMING, PADDING, PAD, EATEN, DEAD};
    public enum Direction {RIGHT, UP, LEFT, DOWN};

    Rectangle pos; //make a default obj class with a rect
    Circle col; //for collisions
    Vector2 posv;
    Vector2 vel;
    State state;
    float clock;

    private Animation swimUpAnim;
    private Animation swimSideRightAnim;
    private Animation swimSideLeftAnim;
    private Animation swimDownAnim;
    private Animation padAnim;
    private Animation eatenAnim;
    private Animation currAnim;
    public Sprite sprite;
    Direction dir;


    private List<Vector2> checkpoints;
    float followclock;
    float distance_waited;
    boolean GOGOGO;

    public Duckling(int x, int y)
    {
        pos = new Rectangle(x,y, DuckPondGame.objWandH/2, DuckPondGame.objWandH/2);
        col = new Circle(pos.getX()+ 1.0f * pos.getWidth(), pos.getY() + 1.0f* pos.getHeight(), pos.getWidth()*.33f);
        posv = new Vector2(pos.getX(), pos.getY());
        vel = new Vector2();

        checkpoints = new ArrayList<Vector2>(200); //something something minimum speed, max distance apart, time between points balah balah
        checkpoints.add(posv.cpy());
        followclock =0;
        distance_waited =0;
        GOGOGO = false;
        state = State.INITIALIZING;

        clock =0;

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

    public void follow(Vector2 pos2follow)
    {
        //update posv and vel and pos and col

        if (!GOGOGO) {
            if (followclock >= CRUMB_SPACE_TIME)
            {
                followclock -= CRUMB_SPACE_TIME;
                if (pos2follow.cpy().sub(posv).len() != 0) checkpoints.add(pos2follow.cpy());
                distance_waited = posv.cpy().sub(pos2follow).len();
                //Gdx.app.debug("distancewaited", Float.toString(distance_waited));
                if (distance_waited >= ducklingDistConst)
                {
                    GOGOGO = true;
                    state = State.SWIMMING;
                    vel.set(checkpoints.get(1).cpy().sub(posv)); //set the vel to nonzero or theyll pad
                }

            }
        }
        else
        {
            if (followclock >= CRUMB_SPACE_TIME)
            {
                followclock -=CRUMB_SPACE_TIME;
                checkpoints.add(pos2follow.cpy());
                posv.set(checkpoints.get(1).cpy());

                vel.set(checkpoints.get(2).cpy().sub(checkpoints.get(1).cpy())); //not actual vel, just direction for animation and detecting a stop
                pos.setPosition(posv.cpy());
                col.setPosition(pos.getX()+ 1.0f * pos.getWidth(), pos.getY() + 1.0f* pos.getHeight());

                Gdx.app.debug(checkpoints.get(2).toString(), checkpoints.get(1).toString());
                Gdx.app.debug("vel",vel.toString());
                checkpoints.remove(0); //this doesnt run fast enough to be done by the next line maybe
            }
            else
            {
                posv.set(checkpoints.get(0).cpy().lerp(checkpoints.get(1), followclock/CRUMB_SPACE_TIME));
                pos.setPosition(posv.cpy());
                col.setPosition(pos.getX()+ 1.0f * pos.getWidth(), pos.getY() + 1.0f* pos.getHeight());
                Gdx.app.debug("vel", vel.toString());
            }
        }



    }

    public void update(float delta)
    {
        clock += delta;
        followclock += delta;

        setSprite();
        if (vel.isZero() && state == State.SWIMMING){
            state = State.PAD;
        }
    }

    public void getEaten()
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
            currAnim = padAnim;
            if (vel.len() * clock >= DuckPondGame.objWandH*.7f) vel.setZero();
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
        if (state != State.PAD && dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
        if (dir == Direction.RIGHT && vel.angle() <90) sprite.setRotation(ang*.3f);
        if (dir == Direction.RIGHT && vel.angle() >270) sprite.setRotation((ang-360)*.3f +360);

    }
}
