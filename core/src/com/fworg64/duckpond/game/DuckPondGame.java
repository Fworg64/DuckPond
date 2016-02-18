package com.fworg64.duckpond.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This is basically the launcer for the game, most of the real loading should
 * be done here
 *
 * maybe a nice splash screen as well
 */

public class DuckPondGame extends Game {
	public SpriteBatch batch;

	public String debug; //probably make a debug file too...
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Options.loadDefault();//Assets neads options first
		Options.setHighres();
		Assets.load();


		debug = "herpp";

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
