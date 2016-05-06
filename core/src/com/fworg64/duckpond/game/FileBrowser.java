package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by fworg on 5/6/2016.
 *
 * class to make filebrowsing less of a mess
 */
public class FileBrowser
{
    public final String CUSTOM_FOLDER_NAME = "CUSTOM";
    public int LEVEL_LOAD_X ;
    public int LEVEL_LOAD_Y;
    public int LEVEL_LOAD_W ;
    public int LEVEL_LOAD_H ;
    public int LEVEL_LOAD_XS;
    public int LEVEL_LOAD_YS;

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

    Rectangle[] levelbutts;
    Rectangle pageleftbutt;
    Rectangle pagerightbutt;
    Rectangle upone;

    FileHandle levelDir;
    ArrayList<FileHandle> levels;
    int pagenumber;

    public FileBrowser()
    {
        //region valuesForRectangles
        if (Options.highres)
        {
            LEVEL_LOAD_X = 210;
            LEVEL_LOAD_Y = 1920 - 453;
            LEVEL_LOAD_W = 300;
            LEVEL_LOAD_H = 300;
            LEVEL_LOAD_XS = 360;
            LEVEL_LOAD_YS = 360;

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
        else {
            LEVEL_LOAD_X = 65;
            LEVEL_LOAD_Y = 960 -332;
            LEVEL_LOAD_W = 150;
            LEVEL_LOAD_H = 150;
            LEVEL_LOAD_XS = 180;
            LEVEL_LOAD_YS = 180;

            PAGE_RIGHT_X = 441;
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
        //endregion

        Assets.load_navigation();

        levelbutts = new Rectangle[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
                levelbutts[i*(LEVEL_LOAD_R) + j] = new Rectangle(LEVEL_LOAD_X + i*LEVEL_LOAD_XS, LEVEL_LOAD_Y - j*LEVEL_LOAD_YS - LEVEL_LOAD_H, LEVEL_LOAD_W, LEVEL_LOAD_H);
            }
        }

        pageleftbutt = new Rectangle(PAGE_LEFT_X, PAGE_LEFT_Y - PAGE_H, PAGE_W, PAGE_H);
        pagerightbutt = new Rectangle(PAGE_RIGHT_X, PAGE_RIGHT_Y - PAGE_H, PAGE_W, PAGE_H);
        upone = new Rectangle(UP_ONE_X, UP_ONE_Y - UP_ONE_H, UP_ONE_W, UP_ONE_H);

        levelDir = Gdx.files.internal("LEVELS\\");
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        levels.remove(0); //remove custom world folder
        pagenumber =0;
    }

    public void pageRight()
    {
        Gdx.app.debug("pagerighttocuh", Integer.toString(levelDir.list().length /PAGE_SIZE));
        if (levelDir.list().length / PAGE_SIZE > pagenumber) pagenumber++;
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        for (Iterator<FileHandle> iterator = levels.iterator(); iterator.hasNext();) //remove custom folder from list
        {
            FileHandle f = iterator.next();
            if (f.name().equals(CUSTOM_FOLDER_NAME)) iterator.remove();
        }
        for (int i =0; i<pagenumber*PAGE_SIZE; i++)
        {
            levels.remove(0);
        }
    }

    public void pageLeft()
    {
        if (pagenumber>0) pagenumber--;
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        for (Iterator<FileHandle> iterator = levels.iterator(); iterator.hasNext();) //remove custom folder from list
        {
            FileHandle f = iterator.next();
            if (f.name().equals(CUSTOM_FOLDER_NAME)) iterator.remove();
        }
        levels = new ArrayList<FileHandle>(levels.subList(pagenumber * PAGE_SIZE, (pagenumber+1)*PAGE_SIZE));
    }

    public void pageUp()
    {
        if (!levelDir.name().equals("LEVELS"))
        {
            levelDir = levelDir.parent(); //take us to LEVELS
            levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        }
        for (Iterator<FileHandle> iterator = levels.iterator(); iterator.hasNext();) //remove custom folder from list
        {
            FileHandle f = iterator.next();
            if (f.name().equals(CUSTOM_FOLDER_NAME)) iterator.remove();
        }
        levels = new ArrayList<FileHandle>(levels.subList(pagenumber * PAGE_SIZE, (pagenumber+1)*PAGE_SIZE));
    }

    public void gocustom()
    {
        levelDir = Gdx.files.local("LEVELS\\" + CUSTOM_FOLDER_NAME);
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
    }
    
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(.5f, .2f, .2f, .5f);
        for(Rectangle r: levelbutts) shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        shapeRenderer.rect(upone.getX(), upone.getY(), upone.getWidth(), upone.getHeight());
        shapeRenderer.rect(pageleftbutt.getX(), pageleftbutt.getY(), pageleftbutt.getWidth(), pageleftbutt.getHeight());
        shapeRenderer.rect(pagerightbutt.getX(), pagerightbutt.getY(), pagerightbutt.getWidth(), pagerightbutt.getHeight());
        shapeRenderer.end();
    }
    
    public void renderSprites(SpriteBatch batch)
    {
        batch.enableBlending();
        batch.begin();
        batch.draw(Assets.NavigationFlechaIzq, pageleftbutt.getX(), pageleftbutt.getY());
        batch.draw(Assets.NavigationFlechaDer, pagerightbutt.getX(), pagerightbutt.getY());
        batch.draw(Assets.NavigationUpone, upone.getX(), upone.getY());
        for (int i=0; i< (levelbutts.length<levels.size() ? levelbutts.length : levels.size()); i++)
        {
            Assets.font.draw(batch, levels.get(i).nameWithoutExtension(), levelbutts[i].getX(), levelbutts[i].getY() + levelbutts[i].getHeight());
            batch.draw(Assets.NavigationWorldButt, levelbutts[i].getX(), levelbutts[i].getY());
        }

        batch.end();

    }

    public void dispose()
    {
        Assets.dispose_navigation();
    }
}
