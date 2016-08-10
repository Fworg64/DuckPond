package com.fworg64.duckpond.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 8/10/2016.
 */
public class MakeNShare extends ScreenAdapter {

    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Button mainMenubutt, worldMakerbutt, shareLevelsbutt, getLevelsbutt;
    Button[] Butts;


    public MakeNShare(DuckPondGame game)
    {
        if (Options.highres)
        {
            mainMenubutt = new Button(62, 1920-385, 281, 160, Assets.LevelSelectionMainMenu);
            worldMakerbutt = new Button(258, 1920 - 483, 565, 285, Assets.MakeNShareWorldMaker);

        }
        else
        {
            mainMenubutt = new Button(30, 960-290, 167, 95, Assets.LevelSelectionMainMenu);
        }
    }
}
