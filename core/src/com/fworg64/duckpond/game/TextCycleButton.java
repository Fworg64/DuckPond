package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 6/8/2016.
 */
public class TextCycleButton {
    private static final Color dimmingColor = new Color(.5f, .5f, .55f, 1);


    int state;
    String[] options;
    Rectangle bounds;

    private boolean justpressed; //true if button was just pressed
    private boolean pressed; //true if button is currently pressed
    private boolean wasPressed; //ture if the button was pressed but now is not

    TextCycleButton(String[] options, float x,float y, float w, float h)
    {
        this.options = options;
        this.bounds = new Rectangle(x,y,w,h);

        justpressed = false;
        pressed = false;
        wasPressed = false;

        state =0;

    }

    public void pollPress(Vector2 touchpoint)
    {
        if (bounds.contains(touchpoint))
        {
            if (!pressed && !wasPressed) //pressed for the first time
            {
                Gdx.app.debug("Button Just Pressed", Integer.toString(this.hashCode()));
                justpressed = true;
                pressed = true;
            }
            else if (!pressed && wasPressed)//this shouldnt run, touched again before waspressed was handled
            {
                //pressed = true;
            }
            else if (pressed && !wasPressed) //still pressed
            {
                //Gdx.app.debug("Button Still Pressed", Integer.toString(this.hashCode()));
                justpressed = false;
                //pressed = true;
            }
            else if (pressed && wasPressed) //also shouldnt run still pressed after being touched after an unhandled wasPressed
            {
                //nothing
            }
        }
        else //is not pressed
        {
            if (pressed && wasPressed) //shouldn't happen
            {
                //pressed = false;
            }
            else if (pressed && !wasPressed && touchpoint.isZero()) //finger just released
            {
                Gdx.app.debug("Button released", Integer.toString(this.hashCode()));
                justpressed = false; //safe guard, only will change anything if pressed and then next cycle released
                wasPressed = true;
                pressed = false;
                state = (state + 1) % options.length;
            }
            else if (pressed && !wasPressed && !touchpoint.isZero())//finger slid off
            {
                justpressed = false;
                wasPressed = false;
                pressed = false;
            }
            else if (!pressed && wasPressed) //stable was pressed
            {
                //do nothing
            }
            else if (!pressed && !wasPressed) //shouldn't happen
            {
                //do nothing
            }
        }
    }
    public boolean isWasPressed()
    {
        return wasPressed;
    }
    public void pressHandled()
    {
        Gdx.app.debug("Button reset", Integer.toString(this.hashCode()));
        justpressed = false;
        pressed = false;
        wasPressed = false;
    }

    public int getState() {
        return state;
    }

    public void renderSprites(SpriteBatch batch)
    {
        //batch.enableBlending();
        //batch.begin();
        if (pressed || wasPressed) batch.setColor(dimmingColor);
        else batch.setColor(1f,1f,1f,1f);
        for (int i=0; i<options.length; i++)
        {
            if (i == state) Assets.font.draw(batch, " >" + options[i], bounds.getX(), bounds.getY() + bounds.getHeight() - i*bounds.getHeight()/options.length);
            else Assets.font.draw(batch, options[i], bounds.getX(), bounds.getY() + bounds.getHeight() - i*bounds.getHeight()/options.length);
        }
        if (pressed || wasPressed) batch.setColor(1f,1f,1f,1f);
        //batch.end();
    }




}
