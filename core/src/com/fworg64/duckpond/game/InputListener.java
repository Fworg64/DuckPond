package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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

    private float rawTouchX; //the touchpoint in screen coord
    private float rawTouchY;
    private int Yoffset;
    private Vector2 projTouch;// the touchpoint projected to the assumed camera

    public InputListener()
    {
        Xscalefactor = (float)(DuckPondGame.worldW)/((float)Gdx.app.getGraphics().getWidth());
        Yscalefactor = -(float)(DuckPondGame.worldH)/((float)Gdx.app.getGraphics().getHeight());
        Yoffset = DuckPondGame.worldH;
        Gdx.app.debug("worldW",Float.toString(DuckPondGame.worldW));
        Gdx.app.debug("worldH",Float.toString(DuckPondGame.worldH));


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
        Xscalefactor = (float)(DuckPondGame.worldW)/((float)Gdx.app.getGraphics().getWidth()); //protect against resizes
        Yscalefactor = -(float)(DuckPondGame.worldH)/((float)Gdx.app.getGraphics().getHeight());
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
}
