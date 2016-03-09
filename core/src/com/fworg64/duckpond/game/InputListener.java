package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 2/19/2016.
 * this class is here to handle resolutions differences
 * so other classes dont have to
 *
 * It needs to project the input vector to world coordinates
 *
 * basically a resolution aware wrapper for gdx.input
 */
public class InputListener
{
    private float Xscalefactor;
    private float Yscalefactor;

    private int worldw, worldh;

    private float rawTouchX; //the touchpoint in screen coord
    private float rawTouchY;
    private int Yoffset;
    private Vector2 projTouch;// the touchpoint projected to the assumed camera

    private boolean backflag;

    public InputListener()
    {
        worldw = DuckPondGame.worldW;
        worldh = DuckPondGame.worldH;
        Xscalefactor = (float)(worldw)/((float)Gdx.app.getGraphics().getWidth());
        Yscalefactor = -(float)(worldh)/((float)Gdx.app.getGraphics().getHeight());
        Yoffset = worldh;
        Gdx.app.debug("worldW",Float.toString(worldw));
        Gdx.app.debug("worldH",Float.toString(worldh));


        Gdx.app.debug("Xscalefactor",Float.toString(Xscalefactor));
        Gdx.app.debug("Yscalefactor",Float.toString(Yscalefactor));
//        Gdx.app.debug("Yoffset",Float.toString(Yoffset));
        rawTouchX = 0;
        rawTouchY =0;
        projTouch = new Vector2();
    }

    public InputListener(int worldW, int worldH)
    {
        worldw = worldW;
        worldh = worldH;
        Xscalefactor = (float)(worldw)/((float)Gdx.app.getGraphics().getWidth());
        Yscalefactor = -(float)(worldh)/((float)Gdx.app.getGraphics().getHeight());
        Yoffset = worldh;
        Gdx.app.debug("worldW",Float.toString(worldw));
        Gdx.app.debug("worldH",Float.toString(worldh));


        Gdx.app.debug("Xscalefactor",Float.toString(Xscalefactor));
        Gdx.app.debug("Yscalefactor",Float.toString(Yscalefactor));
//        Gdx.app.debug("Yoffset",Float.toString(Yoffset));
        rawTouchX = 0;
        rawTouchY =0;
        projTouch = new Vector2();
    }

    public boolean justTouched()
    {
        return Gdx.input.justTouched();
    }

    public Vector2 getTouchpoint()
    {
        Xscalefactor = (float)(worldw)/((float)Gdx.app.getGraphics().getWidth()); //protect against resizes
        Yscalefactor = -(float)(worldh)/((float)Gdx.app.getGraphics().getHeight());
        //need to transform ydown to yup with different spaces
        rawTouchX =Gdx.input.getX();
        rawTouchY = Gdx.input.getY();
//        Gdx.app.debug("rawIN_X",Float.toString(rawTouchX));
//        Gdx.app.debug("rawIN_Y",Float.toString(rawTouchY));
        //scale x
        rawTouchX = rawTouchX*Xscalefactor;
        rawTouchY = rawTouchY*Yscalefactor + Yoffset;
//        Gdx.app.debug("projIN_X",Float.toString(rawTouchX));
//        Gdx.app.debug("projIN_Y",Float.toString(rawTouchY));
        projTouch.set(rawTouchX,rawTouchY); //im sure this can be optimized, its a linear transformation so should be done w/ a matrix
        return projTouch;
    }

    public boolean isTouched()
    {
        return Gdx.input.isTouched();
    }

    public boolean isBackPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            return true;
        }
        else return false;
    }
}
