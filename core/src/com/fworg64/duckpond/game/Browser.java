package com.fworg64.duckpond.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fworg on 6/7/2016.
 */
public class Browser extends Thread
{
    public int SIXBUTT_X ;
    public int SIXBUTT_Y;
    public int SIXBUTT_W ;
    public int SIXBUTT_H ;
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

    private Button[] sixbutts;
    private Button pageleftbutt;
    private Button pagerightbutt;
    private Button pageupbutt;
    private Button[] butts;

    private String itempicked;
    private String itemname;
    private boolean itemchosen;

    public volatile boolean renderUpOne;

    private List<String> allOptions;
    private List<String> displayOptions;
    private int pagenumber;
    private int numpages;
    private boolean canPageLeft;
    private boolean canPageRight;

    private Browsable browsable;
    private BrowserCommunicator bc;

    public Browser(Browsable browsable, BrowserCommunicator bc)
    {
        super("Broswer");
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
        }//button dims and sizes
        this.bc = bc;
        sixbutts = new Button[LEVEL_LOAD_C * LEVEL_LOAD_R];
        for (int i=0; i<LEVEL_LOAD_C; i++) {
            for (int j=0; j<LEVEL_LOAD_R;j++)
            {
                sixbutts[i*(LEVEL_LOAD_R) + j] = new Button(SIXBUTT_X + i*SIXBUTT_XS, SIXBUTT_Y - j*SIXBUTT_YS - SIXBUTT_H, SIXBUTT_W, SIXBUTT_H, Assets.NavigationWorldButt);
            }
        }
        pageleftbutt  =  new Button(PAGE_LEFT_X, PAGE_LEFT_Y - PAGE_H, PAGE_W, PAGE_H, Assets.NavigationFlechaIzq);
        pagerightbutt =  new Button(PAGE_RIGHT_X, PAGE_RIGHT_Y - PAGE_H, PAGE_W, PAGE_H, Assets.NavigationFlechaDer);
        pageupbutt    =  new Button(UP_ONE_X, UP_ONE_Y - UP_ONE_H, UP_ONE_W, UP_ONE_H, Assets.NavigationUpone);
        butts = new Button[] {pageleftbutt, pagerightbutt, pageupbutt};

        pagenumber =0;

        itempicked = "";
        itemname = "";
        itemchosen = false;

        this.browsable = browsable;
        updateAllOptions();
        setButtNames();

        renderUpOne = true;
    }

    @Override
    public void run()
    {
        while (true)
        {
            touch(bc.getTouchpoint());
            if (itemchosen)
            {
                bc.setSelectionMade(true);
                bc.setSelectionName(itemname);
                bc.setSelectionContents(itempicked);
            }
            if (bc.isResetSelection())
            {
                resetPicked();
                bc.setResetSelection(false);
            }
            if (bc.isClose())
            {
                close();
                return;
            }
        }


    }

    private void updateAllOptions()
    {
        allOptions = this.browsable.getAllOptions();
        numpages = allOptions.size()/PAGE_SIZE + 1;
        Gdx.app.debug("Browser", "allOptions updated " + allOptions.toString());
        pagenumber = 0;
        goToPage(pagenumber); //sets displayoptions
    }

    private synchronized void goToPage(int page)
    {
        pagenumber = page;
        int safeend = PAGE_SIZE*(pagenumber+1) < allOptions.size() ? PAGE_SIZE*(pagenumber+1): allOptions.size();
        displayOptions =new ArrayList<String>(allOptions.subList(PAGE_SIZE * pagenumber, safeend));
        if (pagenumber == 0) canPageLeft = false;
        else canPageLeft = true;
        if (pagenumber == numpages-1) canPageRight = false;
        else canPageRight = true;
        setButtNames();
        Gdx.app.debug("Browser", "Went to a page");
    }

    private synchronized void touch(Vector2 touchpoint)
    {
        //if (!touchpoint.isZero()) Gdx.app.debug("Browser","touched at " + touchpoint.toString());
        for (int i=0; i<sixbutts.length;i++) sixbutts[i].pollPress(touchpoint);
        for (int i =0; i<displayOptions.size(); i++)
        {
            if (sixbutts[i].isWasPressed())
            {
                browsable.pageInto(displayOptions.get(i));
                if (browsable.isFinalSelection())
                {
                    itemchosen = true;
                    itempicked = browsable.getSelectionContents();
                    itemname = browsable.getSelectionName();
                    Gdx.app.debug("Browser", "FinalItemPicked");
                }
                else
                {
                    updateAllOptions(); //calls go to page 0
                    setButtNames();
                    Gdx.app.debug("Browser", "Paged Into Something");
                }
                //Gdx.app.debug("button pressed", displayOptions.get(i));
                sixbutts[i].pressHandled();
            }
        }
        //check other buttons here as well
        for (Button butt: butts) butt.pollPress(touchpoint);
        pageupbutt.setAvailable(browsable.canPageUp());
        pageleftbutt.setAvailable(canPageLeft);
        pagerightbutt.setAvailable(canPageRight);
        if (pageleftbutt.isWasPressed() && canPageLeft) {
            goToPage(--pagenumber);//turn the paaaaaaggge
            pageleftbutt.pressHandled();
        }
        if (pagerightbutt.isWasPressed() && canPageRight) {
            goToPage(++pagenumber);
            pagerightbutt.pressHandled();
        }
        if (pageupbutt.isWasPressed() && browsable.canPageUp()) {
            browsable.pageUp();
            updateAllOptions();
            setButtNames();
            pageupbutt.pressHandled();}
    }

    private void setButtNames()
    {
        for (int i=0; i< displayOptions.size(); i++)
        {
            sixbutts[i].setButttext(displayOptions.get(i));
        }
    }

    private void resetPicked()
    {
        itemchosen = false;
        itempicked = "";
        itemname = "";
    }

    private void close() {
        browsable.close();
    }

    public void renderShapes(final ShapeRenderer shapeRenderer)
    {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(.5f, .2f, .2f, .5f);
                for (Button butt : sixbutts) butt.renderShapes(shapeRenderer);
                for (Button butt : butts) butt.renderShapes(shapeRenderer);
                shapeRenderer.end();
            }
        });

    }

    public void renderSprites(final SpriteBatch batch, final OrthographicCamera gcam)
    {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                GL20 gl = Gdx.gl;
                gl.glClearColor(.27451f, .70588f, .83922f, 1);
                gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //neccesary
                gcam.update();
                batch.setProjectionMatrix(gcam.combined);

                batch.enableBlending();
                batch.begin();
                if (renderUpOne) pageupbutt.renderSprites(batch);
                pageleftbutt.renderSprites(batch);
                pagerightbutt.renderSprites(batch);
                batch.setColor(1,1,1,1f);

                for (int i=0; i< displayOptions.size(); i++)
                {
                    sixbutts[i].renderSprites(batch);
                }

                batch.end();
            }
        });


    }

}
