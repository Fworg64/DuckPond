package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fworg on 5/18/2016.
 */
public class GetMoreBrowser
{
    public int SIXBUTT_X;
    public int SIXBUTT_Y;
    public int SIXBUTT_W;
    public int SIXBUTT_H;
    public int SIXBUTT_XS;
    public int SIXBUTT_YS;

    private boolean needRequest;
    private boolean gotRequest;
    private boolean wasCancelled;

    private String request;

    private Rectangle[] sixbutts;
    private static final String[] requestmap = {"S", "P", "C", "R"};

    private List<String> allOptions;
    private List<String> displayOptions;

    public GetMoreBrowser()
    {
        //SIXBUTT DIMS
        if (Options.isHighres())
        {
            SIXBUTT_W = 100;
            SIXBUTT_H = 100;
            SIXBUTT_X = 345;
            SIXBUTT_Y = 1000;
            SIXBUTT_XS = 180;
            SIXBUTT_YS = 180;
        }
        else
        {
            SIXBUTT_W = 50;
            SIXBUTT_H = 50;
            SIXBUTT_X = 222;
            SIXBUTT_Y = 400;
            SIXBUTT_XS = 65;
            SIXBUTT_YS = 65;
        }

        needRequest = false;
        wasCancelled = false;
        gotRequest = false;

        sixbutts = new Rectangle[6];
        for (int i=0; i<6; i++)
        {
            sixbutts[i] = new Rectangle(SIXBUTT_X + (i % 2)* SIXBUTT_XS, SIXBUTT_Y - (i/2)* SIXBUTT_YS, SIXBUTT_W, SIXBUTT_H);
        }
        allOptions = new ArrayList<String>();
        displayOptions = new ArrayList<String>();

        for (String s: requestmap) {allOptions.add(s); displayOptions.add(s);}//populate initial buttons
    }

    public synchronized void touch(Vector2 touchpoint)
    {
        for (int i =0; i<displayOptions.size(); i++)
        {
            if (sixbutts[i].contains(touchpoint))
            {
                setRequest(displayOptions.get(i));
                Gdx.app.debug("button pressed", displayOptions.get(i));
            }
        }
        //check other buttons here as well
    }

    public synchronized void setAllOptions(List<String> allop)
    {
        allOptions.clear();
        displayOptions.clear();
        for (int i=0; i<allop.size();i++)
        {
            allOptions.add(allop.get(i));
            if (i<6) displayOptions.add(allop.get(i));
        }
    }

    public synchronized void setNeedRequest()
    {
        needRequest = true;
    }

    public synchronized boolean isNeedRequest()
    {
        return needRequest;
    }

    public synchronized boolean isWasCancelled()
    {
        return wasCancelled;
    }

    public synchronized void setRequest(String request1)
    {
        gotRequest = true;
        request = request1;
    }

    public synchronized String getRequest()
    {
        return request;
    }

    public synchronized boolean isGotRequest()
    {
        return gotRequest;
    }

    public synchronized void unsetGotRequest() {gotRequest = false;}

    public synchronized void cancelGetRequest()
    {
        wasCancelled = true;
    }

    public synchronized void unsetNeedRequest()
    {
        needRequest = false;
    }

    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        if (needRequest){
            for (int i =0; i< displayOptions.size(); i++) shapeRenderer.rect(sixbutts[i].getX(), sixbutts[i].getY(), sixbutts[i].getWidth(), sixbutts[i].getHeight());
        }
        shapeRenderer.end();
    }

    public void renderSpritesAndText(SpriteBatch batch)
    {
        batch.enableBlending();
        batch.begin();
        if (needRequest) {
            for (int i =0; i< displayOptions.size(); i++) Assets.font.draw(batch, displayOptions.get(i), sixbutts[i].getX(), sixbutts[i].getY() + sixbutts[i].getHeight() * .5f);
        }

        batch.end();
    }
}
