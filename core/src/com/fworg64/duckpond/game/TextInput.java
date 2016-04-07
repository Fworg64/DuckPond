package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by fworg on 4/6/2016.
 */
public class TextInput
{
    private boolean keyboardshown;
    static int textkeys[] = {Input.Keys.A, Input.Keys.B, Input.Keys.C, Input.Keys.D, Input.Keys.E, Input.Keys.F, Input.Keys.G,
            Input.Keys.H, Input.Keys.I, Input.Keys.J, Input.Keys.K, Input.Keys.L, Input.Keys.M, Input.Keys.N, Input.Keys.O, Input.Keys.P,
            Input.Keys.Q, Input.Keys.R, Input.Keys.S, Input.Keys.T, Input.Keys.U, Input.Keys.V, Input.Keys.W, Input.Keys.X, Input.Keys.Y, Input.Keys.Z,
            Input.Keys.NUM_0, Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7,
            Input.Keys.NUM_8, Input.Keys.NUM_9};
    static char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0','1', '2', '3', '4', '5', '6', '7', '8', '9'};

    TextInput()
    {
        keyboardshown = false;
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
            if (Gdx.input.isKeyJustPressed(textkeys[i])) tempChar = letters[i];
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) tempChar = Character.toUpperCase(tempChar);
        }
        return tempChar;
    }
    public boolean enterJustPressed()
    {
        return Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
    }

}
