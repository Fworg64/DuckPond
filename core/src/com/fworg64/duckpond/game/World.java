package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
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
    public List<String> toBeLoaded;
    public boolean more2load;

    public final WorldListener listener;

    public float time;
    public float clock;
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

        toBeLoaded = new ArrayList<String>();
        more2load = false;

        time = 420;
        clock =0;
        lives =69;

        debug = "ALLSGOOD";
    }

    public void LoadLevel()
    {
        String levelstring = level;
        Gdx.app.debug(levelstring, "");
        ArrayList<String> levelcodes = new ArrayList<String>(Arrays.asList(levelstring.split("\n")));
        try
        {
            time = Float.parseFloat(levelcodes.get(0).split(" ")[0].trim());
            lives = Integer.parseInt(levelcodes.get(0).split(" ")[1].trim());
            levelcodes.remove(0);
            for(Iterator<String> iterator = levelcodes.iterator(); iterator.hasNext();)
            {
                String code = iterator.next();
                Gdx.app.debug("code:", code);
                String[] codelet = code.split(" ");
                if (Float.parseFloat(codelet[0]) ==0)
                {
                    Vector2 temppos = new Vector2();
                    Vector2 tempvel = new Vector2();
                    int tempducks = Integer.parseInt(codelet[4].trim());
                    temppos.fromString(codelet[2]);
                    tempvel.fromString(codelet[3]);

                    if (codelet[1].equals("Duck")) ducks.add(new Duck(temppos.x, temppos.y, tempvel.x, tempvel.y, tempducks));
                    if (codelet[1].equals("Shark")) sharks.add(new Shark(temppos.x, temppos.y, tempvel.x, tempvel.y));
                    if (codelet[1].equals("Lily")) pads.add(new Lily(temppos.x, temppos.y));
                    iterator.remove();
                }
            }
            if (levelcodes.size() >0) {
                toBeLoaded = levelcodes;
                more2load = true;
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
        clock = 0;

        LoadLevel();
    }

    public void checkAndLoadNotYetLoaded()
    {
        for(Iterator<String> iterator = toBeLoaded.iterator(); iterator.hasNext();)
        {
            String code = iterator.next();
            Gdx.app.debug("loading", code);
            String[] codelet = code.split(" ");
            if (Float.parseFloat(codelet[0]) <=clock)
            {
                Gdx.app.debug("loaded", codelet[0]);
                Vector2 temppos = new Vector2();
                Vector2 tempvel = new Vector2();
                int tempducks = Integer.parseInt(codelet[4].trim());
                temppos.fromString(codelet[2]);
                tempvel.fromString(codelet[3]);

                if (codelet[1].equals("Duck")) ducks.add(new Duck(temppos.x, temppos.y, tempvel.x, tempvel.y, tempducks));
                if (codelet[1].equals("Shark")) sharks.add(new Shark(temppos.x, temppos.y, tempvel.x, tempvel.y));
                if (codelet[1].equals("Lily")) pads.add(new Lily(temppos.x, temppos.y));
                iterator.remove();
            }
        }
        if (toBeLoaded.size() ==0)
        {
            more2load = false;
        }
    }

    public void update(float delta, Vector2 swipestart, Vector2 swipeend)
    {
        time -=delta;
        clock +=delta;
        if (more2load) checkAndLoadNotYetLoaded();
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
                    if (d.col.overlaps(ddd.col) && d.state == Duck.State.SWIMMING && ddd.state == Duckling.State.SWIMMING)
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
        for (Duck d: ducks)
        {
            if (d.state != Duck.State.PAD) return false;
            for (Duckling dd: d.ducklings)
            {
                if (dd.state != Duckling.State.PAD) return false;
            }
        }
        if (lives <=0) return false;
        if (more2load) return false;

        return true;
    }
    private boolean Defeat()
    {
        if (lives <=0 || time <=0) return true; //or something
        else return false;
    }
}
