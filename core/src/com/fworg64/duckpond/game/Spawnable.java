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

    public Spawnable(float t, Vector2 p, Vector2 v, int n, String otype)
    {
        time2spawn = t;
        pos = new Vector2(p);
        vel = new Vector2(v);
        numducks =n;
        objtype = otype;
    }
    public Spawnable(float t, Vector2 p, Vector2 v, String otype)
    {
        time2spawn = t;
        pos = new Vector2(p);
        vel = new Vector2(v);
        numducks =0;
        objtype = otype;
    }
    public Spawnable(float t, Vector2 p, String otype)
    {
        time2spawn = t;
        pos = new Vector2(p);
        vel = new Vector2();
        numducks =0;
        objtype = otype;
    }
    public Spawnable()
    {
        time2spawn = -1;
        pos = new Vector2();
        vel = new Vector2();
        numducks =0;
        objtype = "Invalid";
    }
    public Spawnable(Vector2 p)
    {
        time2spawn = -1;
        pos = new Vector2(p);
        vel = new Vector2();
        numducks =0;
        objtype = "Invalid";
    }

    public void setVel(Vector2 v) {vel.set(v);}
    public void setTime2spawn(float t) {time2spawn = t;}
    public void setPos(Vector2 p) {pos.set(p);}
    public void setNumducks(int n) {numducks = n;}
    public void setObjtype(String s) {objtype = s;}
    public String getObjtype() {return objtype;}
    public Vector2 getVel() {return vel.cpy();}
    public Vector2 getPos() {return pos.cpy();}
    public float getTime2spawn() {return time2spawn;}
    public String toString()
    {
        return Float.toString(time2spawn)+" " + objtype + " " + pos.toString() + " " + vel.toString() + " " + Integer.toString(numducks);
    }
}
