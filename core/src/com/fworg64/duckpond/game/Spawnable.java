package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 3/3/2016.
 */
public class Spawnable
{
    float time2spawn;
    Vector2 pos;
    Vector2 vel;
    int numducks; //only valid for ducks;
    String objtype;

    public void Spawnable(float t, Vector2 p, Vector2 v, int n, String otype)
    {
        time2spawn = t;
        pos.set(p);
        vel.set(v);
        numducks =n;
        objtype = otype;
    }
    public void Spawnable(float t, Vector2 p, Vector2 v, String otype)
    {
        time2spawn = t;
        pos.set(p);
        vel.set(v);
        numducks =0;
        objtype = otype;
    }
    public void Spawnable(float t, Vector2 p, String otype)
    {
        time2spawn = t;
        pos.set(p);
        vel.setZero();
        numducks =0;
        objtype = otype;
    }
    public String toString()
    {
        return Float.toString(time2spawn)+" " + objtype + " " + pos.toString() + " " + vel.toString() + " " + Integer.toString(numducks);
    }
}
