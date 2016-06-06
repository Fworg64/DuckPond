package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 6/6/2016.
 */
public class Button {
    private static final Color dimmingColor = new Color(.5f, .5f, .55f, 1);
    private static final Color unavailableColor = new Color(1f, 1f, 1f, .2f);

    private Rectangle bounds;

    private boolean justpressed; //true if button was just pressed
    private boolean pressed; //true if button is currently pressed
    private boolean wasPressed; //ture if the button was pressed but now is not

    private boolean available;
    private String butttext;

    private Texture texture; //the texture to be drawn

    public Button(float x, float y, float w, float h, Texture texture)
    {
        bounds = new Rectangle(x,y,w,h);
        this.texture = texture;
        justpressed = false;
        pressed = false;
        wasPressed = false;
        available = true;
        butttext = "";
    }

    public void pollPress(Vector2 vector2) //call before checking states
    {
        if (bounds.contains(vector2))
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
            else if (pressed && !wasPressed) //finger just released
            {
                Gdx.app.debug("Button released", Integer.toString(this.hashCode()));
                justpressed = false; //safe guard, only will change anything if pressed and then next cycle released
                wasPressed = true;
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
        Gdx.app.debug("Button reset", Integer.toString(this.hashCode()));
        justpressed = false;
        pressed = false;
        wasPressed = false;
    }
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void setButttext(String butttext) {
        this.butttext = butttext;
    }
    public boolean isJustPressed() {
        return justpressed;
    }

    public void renderSprites(SpriteBatch batch)
    {
        //batch.enableBlending();
        //batch.begin();
        if (pressed || wasPressed) batch.setColor(dimmingColor);
        else if (!available) batch.setColor(unavailableColor);
        else batch.setColor(1f,1f,1f,1f);
        batch.draw(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        if (!butttext.equals("")) Assets.font.draw(batch, butttext, bounds.getX(), bounds.getY() + .8f * bounds.getHeight());
        //batch.end();
    }

    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        //shapeRenderer.end();
    }
}
