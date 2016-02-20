package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    public Duckling(int x, int y, Vector2 vel)
    {
        pos = new Rectangle(x,y, DuckPondGame.spriteW/2, DuckPondGame.spriteH/2);
        col = new Circle(pos.getCenter(new Vector2()), pos.getWidth()/3);
        this.vel = new Vector2(vel.x, vel.y);
        posv = new Vector2(pos.getX(), pos.getY());
    }

    public void follow(Vector2 pos2follow,float delta)
    {
        //update posv and vel and pos and col
        if (pos2follow.cpy().sub(posv).angle(vel) >0) vel.rotate(-rotConst * vel.len());
        else vel.rotate(rotConst*vel.len());
        posv.add(vel.cpy().scl(delta));
        pos.setPosition(posv);
        col.set(pos.getCenter(new Vector2()), pos.getWidth()/3);
    }
}
