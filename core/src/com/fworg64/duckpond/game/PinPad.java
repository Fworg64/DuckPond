package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by fworg on 5/17/2016.
 */
public class PinPad
{
    public int PINBUTT_W;
    public int PINBUTT_H;
    public int PINBUTT_X;
    public int PINBUTT_Y;
    public int PINBUTT_XS;
    public int PINBUTT_YS;

    private String tempPin;
    private String message;

    Rectangle pinpadbutts[];
    public static final String pinmap[] = {"1", "2","3","4","5","6","7","8","9","<-","0","OK"};

    private boolean needpin;
    private boolean gotpin;
    private boolean inputcancelled;

    private boolean confirmingPin;

    public PinPad()
    {

        if (Options.isHighres())
        {
            PINBUTT_W = 100;
            PINBUTT_H = 100;
            PINBUTT_X = 345;
            PINBUTT_Y = 1000;
            PINBUTT_XS = 180;
            PINBUTT_YS = 180;
        }
        else
        {
            PINBUTT_W = 50;
            PINBUTT_H = 50;
            PINBUTT_X = 222;
            PINBUTT_Y = 400;
            PINBUTT_XS = 65;
            PINBUTT_YS = 65;
        }//pinbutt x,y,w,h

        needpin = false;
        gotpin = false;
        inputcancelled = false;
        confirmingPin = false;

        tempPin = "";
        message = "";

        pinpadbutts = new Rectangle[12];
        for (int i =0; i< pinpadbutts.length; i++) pinpadbutts[i] = new Rectangle(PINBUTT_X + (i % 3)* PINBUTT_XS, PINBUTT_Y - (i/3)* PINBUTT_YS, PINBUTT_W, PINBUTT_H);
    }

    public synchronized void setNeedpin()
    {
        needpin = true;
        gotpin = false;
        tempPin = "";
    }

    public synchronized void unsetNeedpin()
    {
        needpin = false;
    }

    public synchronized boolean isNeedpin()
    {
        return needpin;
    }

    public synchronized void setConfirmingPin()
    {
        confirmingPin = true;
    }

    public synchronized void unsetConfirmingPin()
    {
        confirmingPin = false;
    }

    public synchronized boolean isConfirmingPin()
    {
        return confirmingPin;
    }

    public synchronized void pressKey(int i)
    {
        if (pinmap[i].equals("<-")) {
            if (tempPin.length() <= 1)
            {
                tempPin = "\0";
                inputcancelled = true;
            }
            else tempPin = tempPin.substring(0, tempPin.length()-1);

        }
        else if (pinmap[i].equals("OK")) {
            if (tempPin.length() == 4)
            {
                gotpin = true;
                Options.setSavedPin(tempPin);
                Options.save();
                Gdx.app.debug("Pin submitted with OK", tempPin);
            }
            else Gdx.app.debug("Ok pressed", "but pin not long enough");
        }
        else if (tempPin.length() < 4) tempPin += pinmap[i];
    }

    public synchronized String getTempPin()
    {
        return tempPin;
    }

    public synchronized boolean isGotpin()
    {
        return gotpin;
    }

    public synchronized boolean isInputcancelled()
    {
        return inputcancelled;
    }

    public synchronized void resetInputcancelled()
    {
        inputcancelled = false;
    }

    public synchronized void setMessage(String message)
    {
        this.message = message;
    }

    public synchronized String getMessage()
    {
        return message;
    }


    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for (int i =0; i< 12; i++) shapeRenderer.rect(pinpadbutts[i].getX(), pinpadbutts[i].getY(), pinpadbutts[i].getWidth(), pinpadbutts[i].getHeight());
        shapeRenderer.end();
    }

    public void renderSpritesAndText(SpriteBatch batch)
    {
        batch.enableBlending();
        batch.begin();
        for (int i =0; i< 12; i++) Assets.font.draw(batch, pinmap[i], pinpadbutts[i].getX(), pinpadbutts[i].getY() + pinpadbutts[i].getHeight() * .5f);
        Assets.font.draw(batch, tempPin, PINBUTT_X, PINBUTT_Y + PINBUTT_YS);
        batch.end();
    }
}
