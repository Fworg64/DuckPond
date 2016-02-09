package com.fworg64.duckpond.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DuckPondGame extends Game {
	public SpriteBatch batch;

	public String debug; //probably make a debug file too...
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Assets.load();

		debug = "herpp";

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
