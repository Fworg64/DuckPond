package com.fworg64.duckpond.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
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

    public final WorldListener listener;

    public int score;
    public int lives;

    String debug;

    public World (WorldListener listener)
    {
        this.listener = listener;
        ducks = new ArrayList<Duck>();
        pads = new ArrayList<Lily>();
        score =0;
        lives =3;

        debug = "ALLSGOOD";
    }

    public void LoadLevel() //eventually accept a string filename to load a level
    {
        ducks.add(new Duck(-200, 50,70,20));
        ducks.add(new Duck(600, 200, -50, 0));

        pads.add(new Lily(120, 340));

    }

    public void update(float delta, Vector2 swipestart, Vector2 swipeend)
    {

        updateDucks(delta, swipestart, swipeend);
        checkPadsAndDucks();
    }

    private void updateDucks(float delta, Vector2 swipestart, Vector2 swipeend)
    {
        for (Duck d : ducks)
        {
            d.update(delta);
            if (d.pos.contains(swipestart) && swipestart.cpy().sub(swipeend).len2() > 1)
            {
                swipeend.sub(swipestart);
                d.flick(swipeend);
                debug = "DUCK SWIPED!!\n" + swipestart.toString() + '\n'+ swipeend.toString();
            }
            // if swipe was on the duck and bigger than not much, do the flick
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
}
