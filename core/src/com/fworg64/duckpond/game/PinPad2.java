package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 6/11/2016.
 */
public class PinPad2 {
    Button butts[];
    String pinmap[] = {"1", "2","3","4","5","6","7","8","9","<BK","0","OK"};

    public int PINBUTT_W;
    public int PINBUTT_H;
    public int PINBUTT_X;
    public int PINBUTT_Y;
    public int PINBUTT_XS;
    public int PINBUTT_YS;
    public int BIGAST_X;
    public int BIGAST_Y;
    public int BIGAST_XS;

    private String pin;
    private boolean pinReady;

    public PinPad2()
    {
        if (Options.isHighres())
        {
            PINBUTT_W = 300;
            PINBUTT_H = 200;
            PINBUTT_X = 50;
            PINBUTT_Y = 1000;
            PINBUTT_XS = 350;
            PINBUTT_YS = 220;
            BIGAST_X = 40;
            BIGAST_Y = 1250;
            BIGAST_XS = 275;
        }
        else
        {
            PINBUTT_W = 150;
            PINBUTT_H = 100;
            PINBUTT_X = 70;
            PINBUTT_Y = 420;
            PINBUTT_XS = 175;
            PINBUTT_YS = 110;
            BIGAST_X = 30;
            BIGAST_Y = 560;
            BIGAST_XS = 150;
        }

        butts = new Button[12];
        for (int i =0; i< butts.length; i++)
        {
            butts[i] = new Button(PINBUTT_X + (i % 3)* PINBUTT_XS, PINBUTT_Y - (i/3)* PINBUTT_YS, PINBUTT_W, PINBUTT_H, Assets.NavigationFolder);
            butts[i].setButttext(pinmap[i]);
            if (i == 9) butts[i].setAvailable(false);
            if (i == 11) butts[i].setAvailable(false);
        }

        pin = "";
        pinReady = false;
    }

    public void pollPress(Vector2 touchpoint)
    {
        butts[9].setAvailable(pin.length() >0);
        butts[11].setAvailable(pin.length() ==4);
        for (int i=0; i<butts.length;i++)
        {
            butts[i].pollPress(touchpoint);
            if (butts[i].isWasPressed()) {
                if ((i < 9 || i == 10) && pin.length() != 4)//a number was pressed
                {
                    pin += pinmap[i];
                    butts[i].pressHandled();
                }
                if (i == 9 && pin.length() != 0) //backspace
                {
                    pin = pin.substring(0, pin.length() - 1);
                    butts[i].pressHandled();
                }
                if (i == 11 && pin.length() == 4) //okpressed
                {
                    pinReady = true;
                    //dont handle press
                }
            }
        }
    }

    public boolean isPinReady()
    {
        return pinReady;
    }

    public String getPin()
    {
        return pin;
    }

    public void resetPin()
    {
        pin = "";
        pinReady = false;
        butts[11].pressHandled();
    }

    public void renderSprites(SpriteBatch batch)
    {
        for (Button butt: butts) butt.renderSprites(batch);
        for (int i=0; i<pin.length(); i++)
        {
            batch.draw(Assets.ShareBigAst, BIGAST_X + i*BIGAST_XS, BIGAST_Y);
        }

    }
}
