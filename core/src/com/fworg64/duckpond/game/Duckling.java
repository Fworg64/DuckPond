package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fworg on 2/20/2016.
 */
public class Duckling
{
    public enum State {INITIALIZING, FOLLOWING};
    public final static float rotConst = .6f* Duck.rotConst;

    Rectangle pos; //make a default obj class with a rect
    Circle col; //for collisions
    Vector2 posv;
    State state;

    int pointsbehind; //if the duckling has reached the first point of the trail to follow

    private ArrayList<Vector2> checkpoints;

    public Duckling(int x, int y, int pointsbehind)
    {
        pos = new Rectangle(x,y, DuckPondGame.spriteW/2, DuckPondGame.spriteH/2);
        col = new Circle(pos.getCenter(new Vector2()), pos.getWidth()/3);
        posv = new Vector2(pos.getX(), pos.getY());

        checkpoints = new ArrayList<Vector2>(2*pointsbehind);
        this.pointsbehind = pointsbehind;
        state = State.INITIALIZING;
    }

    public void follow(Vector2 pos2follow)
    {
        //update posv and vel and pos and col
        checkpoints.add(new Vector2(pos2follow));
        if (checkpoints.size()> pointsbehind)
        {
            posv.set(checkpoints.get(0));
            checkpoints.remove(0);
        }
        pos.setPosition(posv);
        col.setPosition(pos.getCenter(new Vector2()));
    }
}
