package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 2/20/2016.
 */
public class Duckling
{
    Rectangle pos; //make a default obj class with a rect
    Circle col; //for collisions
    Vector2 vel;
    Vector2 posv;

    public Duckling(int x, int y)
    {
        pos = new Rectangle(x,y, DuckPondGame.spriteW/2, DuckPondGame.spriteH/2);
        col = new Circle(pos.getCenter(new Vector2()), pos.getWidth()/3);
        vel = new Vector2();
        posv = new Vector2(pos.getX(), pos.getY());
    }

    public void follow(Vector2 pos2follow, Vector2 vel2follow,float delta)
    {
        //update posv and vel and pos and col
        vel.set(posv.cpy().sub(pos2follow).clamp(vel2follow.len(), vel2follow.len()));
        posv.add(vel.cpy().scl(delta));
        pos.setPosition(posv);
        col.set(pos.getCenter(new Vector2()), pos.getWidth()/3);
    }
}
