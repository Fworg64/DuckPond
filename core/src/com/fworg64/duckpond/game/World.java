package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
        //game over victory
        public void gameOverVictory();
        //game over lose
        public void gameOverLose();
        //game over new world
        //sound effects when we get to it
    }

    public final List<Duck> ducks;
    public final List<Lily> pads;
    public final List<Shark> sharks;

    public final WorldListener listener;

    public float time;
    public int lives;

    String level;

    String debug;

    public World (WorldListener listener, String level)
    {
        this.listener = listener;
        this.level = level;
        ducks = new ArrayList<Duck>();
        pads = new ArrayList<Lily>();
        sharks = new ArrayList<Shark>();
        time = 30;
        lives =2;

        debug = "ALLSGOOD";
    }

    public void LoadLevel()
    {
        String levelstring = level;
        Gdx.app.debug(levelstring, "");
        Array<String> levelcodes = new Array<String>(levelstring.split("\n"));
        try
        {
            time = Float.parseFloat(levelcodes.get(0).split(" ")[0].trim());
            lives = Integer.parseInt(levelcodes.get(0).split(" ")[1].trim());
            levelcodes.removeIndex(0);
            for(String code: levelcodes)
            {
                String[] codelet = code.split(" ");
                Vector2 temppos = new Vector2();
                Vector2 tempvel = new Vector2();
                int tempducks = Integer.parseInt(codelet[4].trim());
                temppos.fromString(codelet[2]);
                tempvel.fromString(codelet[3]);

                if (codelet[1].equals("Duck")) ducks.add(new Duck(temppos.x, temppos.y, tempvel.x, tempvel.y, tempducks));
                if (codelet[1].equals("Shark")) sharks.add(new Shark(temppos.x, temppos.y, tempvel.x, tempvel.y));
                if (codelet[1].equals("Lily")) pads.add(new Lily(temppos.x, temppos.y));
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            Gdx.app.debug("Error","Level File appers corrupt");
        }

    }

    public void ReloadLevel() //should reload the current level
    {
        ducks.clear();
        pads.clear();
        sharks.clear();
        time =30;
        lives =2;

        LoadLevel();
    }

    public void update(float delta, Vector2 swipestart, Vector2 swipeend)
    {
        time -=delta;
        updateDucks(delta, swipestart, swipeend);
        updateSharks(delta);
        checkPadsAndDucks();
        checkDucksAndSharks();
        checkDucksAndLings();
        if (Victory()) listener.gameOverVictory();
        if (Defeat()) listener.gameOverLose();
    }

    private void updateDucks(float delta, Vector2 swipestart, Vector2 swipeend)
    {
        for (Iterator<Duck> iterator = ducks.iterator(); iterator.hasNext();)
        {
            Duck d = iterator.next();
            d.update(delta);
            if (d.state == Duck.State.DEAD) {iterator.remove();} //safe way to clean dead ducks
            if (swipestart.cpy().sub(swipeend).len2()>1) Gdx.app.debug("Known Duck",d.pos.toString());
            if (d.pos.contains(swipestart) && swipestart.cpy().sub(swipeend).len2() > 1 && d.state == Duck.State.SWIMMING)
            {
                swipeend.sub(swipestart);
                d.flick(swipeend);
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
                if (l.col.overlaps(d.col) && d.state == Duck.State.SWIMMING)
                {
                    d.pad(l);
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
                if (d.col.overlaps(s.col) && d.state == Duck.State.SWIMMING)
                {
                    s.eatDuck(d);
                    d.getEaten();
                    lives -=1;
                }
                for (Duckling dd: d.ducklings)
                {
                    if (dd.col.overlaps(s.col) && dd.state == Duckling.State.SWIMMING)
                    {
                        s.eatDuck(d);
                        d.getEaten();
                        lives-=1;
                    }
                }
            }
        }
    }

    public void checkDucksAndLings()
    {
        for (Duck d : ducks)
        {
            for (Duck dd: ducks)
            {
                if (d != dd && d.col.overlaps(dd.col) && d.state == Duck.State.SWIMMING && dd.state == Duck.State.SWIMMING)
                {
                    d.getEaten();
                    lives--;
                    if (d != dd)
                    {
                        dd.getEaten();
                        lives--;
                    }
                }
                for (Duckling ddd: dd.ducklings)
                {
                    if (d.col.overlaps(ddd.col) && d.state == Duck.State.SWIMMING && dd.state == Duck.State.SWIMMING && ddd.state == Duckling.State.SWIMMING)
                    {
                        d.getEaten();
                        lives--;
                        if (d != dd)
                        {
                            dd.getEaten();
                            lives--;
                        }
                    }
                }
            }
        }
    }

    private boolean Victory()
    {
        if (false) return true;
        else return false;
    }
    private boolean Defeat()
    {
        if (lives <=0 || time <=0) return true; //or something
        else return false;
    }
}
