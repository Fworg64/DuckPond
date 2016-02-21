package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fworg on 2/20/2016.
 */
public class Duckling
{
    public final static float rotConst = .6f* Duck.rotConst;
    Rectangle pos; //make a default obj class with a rect
    Circle col; //for collisions
    Vector2 vel;
    Vector2 posv;

    boolean latched; //if the duckling has reached the first point of the trail to follow

    private ArrayList<Vector2[]> checkpoints;

    public Duckling(int x, int y, Vector2 vel)
    {
        pos = new Rectangle(x,y, DuckPondGame.spriteW/2, DuckPondGame.spriteH/2);
        col = new Circle(pos.getCenter(new Vector2()), pos.getWidth()/3);
        this.vel = new Vector2(vel.x, vel.y);
        posv = new Vector2(pos.getX(), pos.getY());

        checkpoints = new ArrayList<Vector2[]>(30);
        latched = false;
    }

    public void follow(Vector2 pos2follow,Vector2 vel2follow, float delta)
    {
        //update posv and vel and pos and col
        checkpoints.add(new Vector2[]{pos2follow, vel2follow});
        if (latched ==false)
        {
            vel.set(checkpoints.get(0)[0].cpy().sub(this.posv).clamp(vel2follow.len(), vel2follow.len()));
            posv.add(vel.cpy().scl(delta));
        }
        else
        {
            posv.set(checkpoints.get(0)[0]);
            checkpoints.remove(0);
        }

        //check to see if closse

        pos.setPosition(posv);
        col.set(pos.getCenter(new Vector2()), pos.getWidth()/3);
        if (this.posv.dst(checkpoints.get(0)[0]) < 1) latched = true;
    }
}
