package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 6/6/2016.
 */
public class Button {
    private static final Color dimmingColor = new Color(.5f, .5f, .55f, 1);

    private Rectangle bounds;
    private boolean pressed; //true if button is currently pressed
    private boolean wasPressed; //ture if the button was pressed but now is not

    private Texture texture; //the texture to be drawn

    public Button(float x, float y, float w, float h, Texture texture)
    {
        bounds = new Rectangle(x,y,w,h);
        this.texture = texture;
        pressed = false;
        wasPressed = false;
    }

    public void pollPress(Vector2 vector2) //call before checking states
    {
        if (bounds.contains(vector2))
        {
            if (!pressed && !wasPressed) //pressed for the first time
            {
                pressed = true;
            }
            if (!pressed && wasPressed)//this shouldnt run, touched again before waspressed was handled
            {
                pressed = true;
            }
            if (pressed && !wasPressed) //still pressed
            {
                pressed = true;
            }
            if (pressed && wasPressed) //also shouldnt run still pressed after being touched after an unhandled wasPressed
            {
                //nothing
            }
        }
        else //is not pressed
        {
            if (pressed && wasPressed) //shouldn't happen
            {
                pressed = false;
            }
            if (pressed && !wasPressed) //finger just released
            {
                wasPressed = true;
                pressed = false;
            }
            if (!pressed && wasPressed) //stable was pressed
            {
                //do nothing
            }
            if (!pressed && !wasPressed) //shouldn't happen
            {
                //do nothing
            }
        }
    }

    public boolean isPressed()
    {
        return pressed;
    }
    public boolean isWasPressed()
    {
        return wasPressed;
    }
    public void pressHandled()
    {
        pressed = false;
        wasPressed = false;
    }

    public void renderSprites(SpriteBatch batch)
    {
        batch.enableBlending();
        batch.begin();
        if (pressed || wasPressed) batch.setColor(dimmingColor);
        else batch.setColor(1f,1f,1f,1f);
        batch.draw(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        batch.end();
    }
}
