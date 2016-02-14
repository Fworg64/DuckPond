package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This file contains all the world logic
 *
 * Created by fworg on 2/10/2016.
 *
 */
public class World
{
    public interface WorldListener
    {
        //put methods here that you want executed using code in the screen class
        //these are non-logic things such as play sound effects

        //choose level might be a good function to do here as well
        // or nah... world selection should happen not in this class
    }

    public final List<Duck> ducks;
    public final List<Lily> pads;
    public final List<Shark> sharks;

    public final WorldListener listener;

    public int score;
    public int lives;

    String debug;

    public World (WorldListener listener)
    {
        this.listener = listener;
        ducks = new ArrayList<Duck>();
        pads = new ArrayList<Lily>();
        sharks = new ArrayList<Shark>();
        score =0;
        lives =3;

        debug = "ALLSGOOD";
    }

    public void LoadLevel() //eventually accept a string filename to load a level
    {
        ducks.add(new Duck(-200, 50, 70, 20));
        ducks.add(new Duck(600, 200, -50, 0));

        pads.add(new Lily(120, 340));

        sharks.add(new Shark(400, 100, -60, 10));
    }

    public void update(float delta, Vector2 swipestart, Vector2 swipeend)
    {

        updateDucks(delta, swipestart, swipeend);
        updateSharks(delta);
        checkPadsAndDucks();
        checkDucksAndSharks();
    }

    private void updateDucks(float delta, Vector2 swipestart, Vector2 swipeend)
    {
        for (Iterator<Duck> iterator = ducks.iterator(); iterator.hasNext();)
        {
            Duck d = iterator.next();
            d.update(delta);
            if (d.state == Duck.State.DEAD) {iterator.remove();} //safe way to clean dead ducks
            if (d.pos.contains(swipestart) && swipestart.cpy().sub(swipeend).len2() > 1)
            {
                swipeend.sub(swipestart);
                d.flick(swipeend);
                debug = "DUCK SWIPED!!\n" + swipestart.toString() + '\n'+ swipeend.toString();
            }
            // if swipe was on the duck and bigger than not much, do the flick
        }
    }

    private void updateSharks(float delta)
    {
        for (Shark s : sharks)
        {
            s.update(delta);
        }
    }

    private void checkPadsAndDucks()
    {
        for (Duck d: ducks)
        {
            for (Lily l: pads)
            {
                if (l.col.overlaps(d.col))
                {
                    d.pad();
                    debug = "DUCK PADDED!!";
                }

            }

        }
    }

    private void checkDucksAndSharks()
    {
        for (Shark s:sharks)
        {
            for (Duck d: ducks)
            {
                if (d.col.overlaps(s.col) && d.state != Duck.State.EATEN)
                {
                    s.eatDuck(d);
                    d.getEaten(s);
                    debug = "DUCK EATEN!!";
                }
            }
        }
    }
}
