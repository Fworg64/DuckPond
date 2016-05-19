package com.fworg64.duckpond.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

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

    String request;

    Rectangle[] sixbutts;
    public static final String[] requestmap = {"S", "P", "C", "R"};

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
            for (int i =0; i< 4; i++) shapeRenderer.rect(sixbutts[i].getX(), sixbutts[i].getY(), sixbutts[i].getWidth(), sixbutts[i].getHeight());
        }
        shapeRenderer.end();
    }

    public void renderSpritesAndText(SpriteBatch batch)
    {
        batch.enableBlending();
        batch.begin();
        if (needRequest) {
            for (int i =0; i< 4; i++) Assets.font.draw(batch, requestmap[i], sixbutts[i].getX(), sixbutts[i].getY() + sixbutts[i].getHeight() * .5f);
        }

        batch.end();
    }
}
