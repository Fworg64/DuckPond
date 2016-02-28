package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fworg on 2/20/2016.
 */
public class Duckling
{
    public enum State {INITIALIZING, SWIMMING, PAD, EATEN, DEAD};
    public enum Direction {RIGHT, UP, LEFT, DOWN};
    public final static float rotConst = .6f* Duck.rotConst;

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
    public Animation currAnim;
    public Sprite sprite;
    Direction dir;

    int pointsbehind; //if the duckling has reached the first point of the trail to follow

    private ArrayList<Vector2> checkpoints;

    public Duckling(int x, int y, int pointsbehind)
    {
        pos = new Rectangle(x,y, DuckPondGame.spriteW/2, DuckPondGame.spriteH/2);
        col = new Circle(pos.getCenter(new Vector2()), pos.getWidth()/3);
        posv = new Vector2(pos.getX(), pos.getY());
        vel = new Vector2();

        checkpoints = new ArrayList<Vector2>(2*pointsbehind);
        this.pointsbehind = pointsbehind;
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
        checkpoints.add(new Vector2(pos2follow));
        if (checkpoints.size()> pointsbehind)
        {
            posv.set(checkpoints.get(0));
            vel.set(checkpoints.get(1).cpy().sub(checkpoints.get(0)));
            checkpoints.remove(0);
            state = State.SWIMMING;
        }
        pos.setPosition(posv);
        col.setPosition(pos.getCenter(new Vector2()));
    }

    public void update(float delta)
    {
        clock += delta;

        setSprite();
        if (vel.len2()<=1) state = State.PAD;
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

        sprite = new Sprite(currAnim.getKeyFrame(clock));
        sprite.setPosition(pos.getX(), pos.getY());
        sprite.setOriginCenter();
        if (state != State.PAD && dir != Direction.RIGHT) sprite.setRotation((ang - 90 * dir.ordinal())*.3f);
        if (dir == Direction.RIGHT && vel.angle() <90) sprite.setRotation(ang*.3f);
        if (dir == Direction.RIGHT && vel.angle() >270) sprite.setRotation((ang-360)*.3f +360);

    }
}
