package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int PAGE_RIGHT_X;
    public int PAGE_RIGHT_Y;
    public int PAGE_LEFT_X;
    public int PAGE_LEFT_Y;
    public int PAGE_W;
    public int PAGE_H;

    public int UP_ONE_X;
    public int UP_ONE_Y;
    public int UP_ONE_W;
    public int UP_ONE_H;

    public int LEVEL_LOAD_R;
    public int LEVEL_LOAD_C;
    public int PAGE_SIZE;

    private boolean needRequest;
    private boolean gotRequest;
    private boolean wasCancelled;

    private String request;

    private Rectangle[] sixbutts;
    private Rectangle pageleftbutt;
    private Rectangle pagerightbutt;
    private Rectangle pageupbutt;

    private boolean canPageLeft;
    private boolean canPageRight;
    private boolean canPageUp;
    private int pagenumber;

    private List<String> allOptions;
    private List<String> displayOptions;

    public GetMoreBrowser()
    {
        //SIXBUTT DIMS
        if (Options.isHighres())
        {
            SIXBUTT_X = 210;
            SIXBUTT_Y = 1920-453;
            SIXBUTT_W = 300;
            SIXBUTT_H = 300;
            SIXBUTT_XS = 360;
            SIXBUTT_YS = 360;

            PAGE_RIGHT_X = 694;
            PAGE_RIGHT_Y = 420;
            PAGE_LEFT_X = 300;
            PAGE_LEFT_Y = 420;
            PAGE_W = 86;
            PAGE_H = 163;

            UP_ONE_X = 480;
            UP_ONE_Y = 420;
            UP_ONE_W = 121;
            UP_ONE_H = 163;

            LEVEL_LOAD_R = 3;
            LEVEL_LOAD_C = 2;
            PAGE_SIZE = LEVEL_LOAD_R * LEVEL_LOAD_C;
        }
        else
        {
            SIXBUTT_X = 65;
            SIXBUTT_Y = 960-332;
            SIXBUTT_W = 150;
            SIXBUTT_H = 150;
            SIXBUTT_XS = 180;
            SIXBUTT_YS = 180;

            PAGE_RIGHT_X = 411;
            PAGE_RIGHT_Y = 960-687;
            PAGE_LEFT_X = 178;
            PAGE_LEFT_Y = 960-687;
            PAGE_W = 50;
            PAGE_H = 96;

            UP_ONE_X = 284;
            UP_ONE_Y = 960-687;
            UP_ONE_W = 71;
            UP_ONE_H = 96;

            LEVEL_LOAD_R = 2;
            LEVEL_LOAD_C = 3;
            PAGE_SIZE = LEVEL_LOAD_R * LEVEL_LOAD_C;
        }

        needRequest = false;
        wasCancelled = false;
        gotRequest = false;

        sixbutts = new Rectangle[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
                sixbutts[i*(LEVEL_LOAD_R) + j] = new Rectangle(SIXBUTT_X + i*SIXBUTT_XS, SIXBUTT_Y - j*SIXBUTT_YS - SIXBUTT_H, SIXBUTT_W, SIXBUTT_H);
            }
        }
        pageleftbutt = new Rectangle(PAGE_LEFT_X, PAGE_LEFT_Y - PAGE_H, PAGE_W, PAGE_H);
        pagerightbutt = new Rectangle(PAGE_RIGHT_X, PAGE_RIGHT_Y - PAGE_H, PAGE_W, PAGE_H);
        pageupbutt = new Rectangle(UP_ONE_X, UP_ONE_Y - UP_ONE_H, UP_ONE_W, UP_ONE_H);
        allOptions = new ArrayList<String>();
        displayOptions = new ArrayList<String>();

        canPageLeft = false;
        canPageRight = false;
        canPageUp = false;
        pagenumber = 0;
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
        if (pageleftbutt.contains(touchpoint) && canPageLeft) pageLeft();
        if (pagerightbutt.contains(touchpoint) && canPageRight) pageRight();
        if (pageupbutt.contains(touchpoint) && canPageUp) setRequest("\5");
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
        pagenumber = 0;
        if (allop.size()>6) canPageRight = true;
        else canPageRight = false;
        canPageLeft = false;
    }

    public synchronized void allowPageUp()
    {
        canPageUp = true;
    }

    public synchronized void disallowPageUp()
    {
        canPageUp = false;
    }

    private synchronized void pageRight()
    {
        pagenumber++;
        int safeend = 6*(pagenumber+1) < allOptions.size() ? 6*(pagenumber+1): allOptions.size();
        displayOptions =new ArrayList<String>(allOptions.subList(6 * pagenumber, safeend));
        canPageLeft = true;
        if (safeend == allOptions.size()) canPageRight=false;
    }

    private synchronized void pageLeft()
    {
        pagenumber--;
        displayOptions = new ArrayList<String>(allOptions.subList(6*pagenumber, 6*(pagenumber +1)));
        if (pagenumber ==0) canPageLeft = false;
        canPageRight = true;
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

    private synchronized void setRequest(String request1)
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
        shapeRenderer.rect(pageleftbutt.getX(), pageleftbutt.getY(), pageleftbutt.getWidth(), pageleftbutt.getHeight());
        shapeRenderer.rect(pagerightbutt.getX(), pagerightbutt.getY(), pagerightbutt.getWidth(), pagerightbutt.getHeight());
        shapeRenderer.rect(pageupbutt.getX(), pageupbutt.getY(), pageupbutt.getWidth(), pageupbutt.getHeight());
        shapeRenderer.end();
    }

    public void renderSpritesAndText(SpriteBatch batch)
    {
        batch.enableBlending();
        batch.begin();
        batch.setColor(1,1,1,.2f);
        batch.draw(Assets.NavigationFlechaIzq, pageleftbutt.getX(), pageleftbutt.getY());
        batch.draw(Assets.NavigationFlechaDer, pagerightbutt.getX(), pagerightbutt.getY());
        batch.draw(Assets.NavigationUpone, pageupbutt.getX(), pageupbutt.getY());
        batch.setColor(1,1,1,1);
        if (needRequest) {
            for (int i =0; i< displayOptions.size(); i++) {
                batch.draw(Assets.NavigationWorldButt, sixbutts[i].getX(), sixbutts[i].getY());
                Assets.font.draw(batch, displayOptions.get(i), sixbutts[i].getX(), sixbutts[i].getY() + sixbutts[i].getHeight() * .5f);
            }
        }
        if (canPageLeft) batch.draw(Assets.NavigationFlechaIzq, pageleftbutt.getX(), pageleftbutt.getY());
        if (canPageRight) batch.draw(Assets.NavigationFlechaDer, pagerightbutt.getX(), pagerightbutt.getY());
        if (canPageUp) batch.draw(Assets.NavigationUpone, pageupbutt.getX(), pageupbutt.getY());

        batch.end();
    }
}
