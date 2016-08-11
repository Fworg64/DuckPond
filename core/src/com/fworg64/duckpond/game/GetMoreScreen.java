package com.fworg64.duckpond.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fworg on 8/10/2016.
 */
public class GetMoreScreen extends ScreenAdapter {
    DuckPondGame game; //from example
    OrthographicCamera gcam; //camera
    //ShapeRenderer shapeRenderer;

    InputListener in;
    Vector2 touchpoint;

    Button backbutt;

    public GetMoreScreen(DuckPondGame game)
    {
        if (Options.isHighres())
        {
            backbutt = new Button(40, 1920 - 400, 329, 129, Assets.NavigationBack);
        }
        else
        {
            backbutt = new Button(30, 960-220, 170, 67, Assets.NavigationBack);
        }
    }
}
