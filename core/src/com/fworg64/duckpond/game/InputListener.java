package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
public class InputListener implements InputProcessor
{
    private float Xscalefactor;
    private float Yscalefactor;

    private int worldw, worldh;

    private float rawTouchX; //the touchpoint in screen coord
    private float rawTouchY;
    private int Yoffset;
    private Vector2 projTouch;// the touchpoint projected to the assumed camera

    private boolean keyboardshown;
    static int textkeys[] = {Input.Keys.A, Input.Keys.B, Input.Keys.C, Input.Keys.D, Input.Keys.E, Input.Keys.F, Input.Keys.G,
            Input.Keys.H, Input.Keys.I, Input.Keys.J, Input.Keys.K, Input.Keys.L, Input.Keys.M, Input.Keys.N, Input.Keys.O, Input.Keys.P,
            Input.Keys.Q, Input.Keys.R, Input.Keys.S, Input.Keys.T, Input.Keys.U, Input.Keys.V, Input.Keys.W, Input.Keys.X, Input.Keys.Y, Input.Keys.Z,
            Input.Keys.NUM_0, Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7,
            Input.Keys.NUM_8, Input.Keys.NUM_9};
    static char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','.',
            '0','1', '2', '3', '4', '5', '6', '7', '8', '9'};
    char tempchar;
    boolean keytyped;

    public InputListener(int worldW, int worldH)
    {
        Gdx.input.setInputProcessor(this);
        keyboardshown = false;
        keytyped = false;
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
    public void showKeyboard()
    {
        if (keyboardshown == false)
        {
            if (Gdx.app.getType() == Application.ApplicationType.Android
                    || Gdx.app.getType() == Application.ApplicationType.iOS)
                Gdx.input.setOnscreenKeyboardVisible(true);
            keyboardshown = true;
        }
    }
    public void hideKeyboard()
    {
        if (Gdx.app.getType() == Application.ApplicationType.Android
                || Gdx.app.getType() == Application.ApplicationType.iOS)
            Gdx.input.setOnscreenKeyboardVisible(false);
        keyboardshown = false;
    }
    public char pollChar()
    {
        char tempChar = '\0'; //null char
        for (int i=0; i<textkeys.length; i++)
        {
            if (keytyped) tempChar = tempchar;
            keytyped = false;
        }
        return tempChar;
    }
    public boolean enterJustPressed()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
    }
    public boolean backspaceJustPressed() { return Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE);}

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (char othertemp: letters)
        {
            if (othertemp == character) {
                tempchar = character;
                keytyped = true;
                break;
            }
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
