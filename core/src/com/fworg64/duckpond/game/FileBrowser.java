package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
    public final String DOWNLOAD_FOLDER_NAME = "DOWNLOADED";
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

    Button[] levelbutts;
    Button pageleftbutt;
    Button pagerightbutt;
    Button upone;
    Button[] butts;

    FileHandle levelDir;
    ArrayList<FileHandle> levels;
    int pagenumber;

    private String levelpicked;
    private String namepicked;
    private Boolean levelchosen;

    public volatile boolean renderUpOne;

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
        //endregion



        levelbutts = new Button[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
                levelbutts[i*(LEVEL_LOAD_R) + j] = new Button(LEVEL_LOAD_X + i*LEVEL_LOAD_XS, LEVEL_LOAD_Y - j*LEVEL_LOAD_YS - LEVEL_LOAD_H, LEVEL_LOAD_W, LEVEL_LOAD_H, Assets.NavigationWorldButt);
            }
        }

        pageleftbutt =  new Button(PAGE_LEFT_X, PAGE_LEFT_Y - PAGE_H, PAGE_W, PAGE_H, Assets.NavigationFlechaIzq);
        pagerightbutt = new Button(PAGE_RIGHT_X, PAGE_RIGHT_Y - PAGE_H, PAGE_W, PAGE_H, Assets.NavigationFlechaDer);
        upone =         new Button(UP_ONE_X, UP_ONE_Y - UP_ONE_H, UP_ONE_W, UP_ONE_H, Assets.NavigationUpone);
        butts = new Button[] {pageleftbutt, pagerightbutt, upone};

        levelDir = Gdx.files.internal("LEVELS"); 
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        for (Iterator<FileHandle> iterator = levels.iterator(); iterator.hasNext();) //remove custom folder from list
        {
            FileHandle f = iterator.next();
            Gdx.app.debug("Found "+levels.size()+" Folders", f.name());
            //if (f.name().equals(CUSTOM_FOLDER_NAME)) iterator.remove();
        }
        pagenumber =0;
        setLevelButtNames();
        Gdx.app.debug("currleveldir: ", levelDir.path());

        renderUpOne = true;

        levelpicked = "";
        namepicked = "";
        levelchosen = false;
    }

    public void touch(Vector2 touchpoint)
    {
        for (int i=0; i<levelbutts.length;i++) levelbutts[i].pollPress(touchpoint);
        for(int i=0; i<levelbutts.length;i++) {
            if (levelbutts[i].isWasPressed() && !levelbutts[i].isPressed()) {
                if (levels.size() > i) //if you picked a valid choice
                {
                    if (!levels.get(i).isDirectory()) { //if you picked a level
                        levelpicked = levels.get(i).readString();
                        namepicked = levels.get(i).nameWithoutExtension();
                        levelchosen = true;
                    } else //you picked a folder
                    {
                        pageInto(levels.get(i));
                    }
                    levelbutts[i].pressHandled();
                    break;
                }
            }
        }
        for (Button butt: butts) butt.pollPress(touchpoint);
        if (upone.isWasPressed()) {pageUp(); upone.pressHandled();}
        if (pageleftbutt.isWasPressed()) {pageLeft(); pageleftbutt.pressHandled();}
        if (pagerightbutt.isWasPressed()) {pageRight(); pagerightbutt.pressHandled();}
    }
    public String getLevelPicked()
    {
        return levelpicked;
    }
    public String getNamePicked()
    {
        return namepicked;
    }
    public Boolean isLevelchosen()
    {
        return levelchosen;
    }

    private void pageRight()
    {
        int effectivelength = levelDir.list().length;
        Gdx.app.debug("pagerighttocuh", Integer.toString(effectivelength) +" "+ Integer.toString(PAGE_SIZE) +" "+ Integer.toString(pagenumber));
        if (effectivelength > PAGE_SIZE*(pagenumber+1)) pagenumber++;
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        int i =0;
        for (Iterator<FileHandle> level = levels.iterator(); level.hasNext();)
        {
            FileHandle l = level.next();
            if (i++ < PAGE_SIZE * pagenumber) level.remove(); //remove all levels on previous pages
        }
        setLevelButtNames();
        Gdx.app.debug("Page Left currleveldir: ", levelDir.path());
    }

    private void pageLeft()
    {
        if (pagenumber>0) pagenumber--;
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));

        int safelength = ((pagenumber+1)*PAGE_SIZE > levels.size()) ? levels.size() : (pagenumber+1)*PAGE_SIZE;
        Gdx.app.debug(levelDir.name(), Integer.toString(safelength));
        levels = new ArrayList<FileHandle>(levels.subList(pagenumber * PAGE_SIZE, PAGE_SIZE* pagenumber + safelength));
        setLevelButtNames();
        Gdx.app.debug("Page Rightcurrleveldir: ", levelDir.path());
    }

    private void pageUp()
    {
        if (levelDir.name().equals(CUSTOM_FOLDER_NAME) || levelDir.name().equals(DOWNLOAD_FOLDER_NAME))
        {
            levelDir = Gdx.files.internal("LEVELS");
            levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
            pagenumber =0;
        }
        else if (!levelDir.name().equals("LEVELS")) //we are in a downloaded users folder
        {
            levelDir = levelDir.parent(); //take us to downloaded
            levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));

            pagenumber=0;
            int safelength = ((pagenumber+1)*PAGE_SIZE > levels.size()) ? levels.size() : (pagenumber+1)*PAGE_SIZE;
            Gdx.app.debug(levelDir.name(), Integer.toString(safelength));
            levels = new ArrayList<FileHandle>(levels.subList(pagenumber * PAGE_SIZE, PAGE_SIZE* pagenumber + safelength));
        }
        setLevelButtNames();
        Gdx.app.debug("Page UPcurrleveldir: ", levelDir.path());
    }

    private void pageInto(FileHandle f)
    {
        if (f.isDirectory())
        {
            levelDir = f;
            levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
            pagenumber =0;
        }
        setLevelButtNames();
        Gdx.app.debug("Pageinto currleveldir: ", levelDir.path());
    }

    private void setLevelButtNames()
    {
        for (int i =0; i< (levelbutts.length< levels.size() ? levelbutts.length: levels.size()); i++)
        {
            levelbutts[i].setButttext(levels.get(i).nameWithoutExtension());
        }
    }

    public void gocustom()
    {
        levelDir = Gdx.files.local(CUSTOM_FOLDER_NAME);
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        pagenumber =0;
        Gdx.app.debug("currleveldir: ", levelDir.path());
    }

    public void godownld()
    {
        levelDir = Gdx.files.local(DOWNLOAD_FOLDER_NAME);
        levels = new ArrayList<FileHandle>(Arrays.asList(levelDir.list()));
        pagenumber =0;
        Gdx.app.debug("currleveldir: ", levelDir.path());
    }

    public void resetLevelChosen()
    {
        levelchosen = false;
        levelpicked = "";
        namepicked = "";
    }
    
    public void renderShapes(ShapeRenderer shapeRenderer)
    {
        for (Button butt : levelbutts) butt.renderShapes(shapeRenderer);
        for (Button butt : butts) butt.renderShapes(shapeRenderer);
    }
    
    public void renderSprites(SpriteBatch batch)
    {
        batch.enableBlending();
        //batch.begin();
        upone.setAvailable(!levelDir.name().equals("LEVELS"));
        pageleftbutt.setAvailable(pagenumber!=0);
        pagerightbutt.setAvailable(levelDir.list().length > (pagenumber +1)*PAGE_SIZE);
        if (renderUpOne) upone.renderSprites(batch);
        pageleftbutt.renderSprites(batch);
        pagerightbutt.renderSprites(batch);
        batch.setColor(1,1,1,1f);

        for (int i=0; i< (levelbutts.length<levels.size() ? levelbutts.length : levels.size()); i++)
        {
            levelbutts[i].renderSprites(batch);
        }

        //batch.end();

    }
}
