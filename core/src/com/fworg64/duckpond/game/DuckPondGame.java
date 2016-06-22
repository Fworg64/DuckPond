package com.fworg64.duckpond.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This is basically the launcher for the game, most of the real loading should
 * be done here
 *
 * Not Resolution Aware, but contains the values for world coordinates
 * maybe a nice splash screen as well
 */

public class DuckPondGame extends Game {
	public static int worldH = 864;
	public static int worldW = 640;
	public static int objWandH = 96;
	public static float highresworldscaler = 1.6875f;
	public static float highresworldinverse = .59259f;

	public static int stdspriteW = 96;
	public static int stdspriteH = 96;
	public static int highspriteW = 162;
	public static int highspriteH = 162;

	public static int stdresScreenW = 640;
	public static int stdresScreenH = 960;
	public static int highresScreenW = 1080;
	public static int highresScreenH = 1920;

	public static Color DuckPondBlue = new Color(.27451f, .70588f, .83922f, 1);
	public static Color BlueButtTit = new Color(.5f, .5f, .55f, 1);
	public static Color GreenButtTit = new Color(.5f, .5f, .55f, 1);

	public static final String customfolder = "CUSTOM";
	public static final String downloadsfolder = "DOWNLOADED";
	public static final String levelsfolder = "LEVELS";

	public static final String version = "v1.3.4";

	public SpriteBatch batch;
    public MusicAndSounds mas;

	public interface DuckPondGameAdStateListener
	{
		public void ShowBannerAd();
		public void HideBannerAd();
	}
	public DuckPondGameAdStateListener adStateListener;

//	public interface DuckPondGameResolutionListener
//	{
//		public void showHighResWindow();
//		public void showStdResWindow();
//	}
//	public DuckPondGameResolutionListener resolutionListener;
//

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_NONE);
		batch = new SpriteBatch();
		Options.loadOptions();
		Assets.load_mainmenu();
		Assets.load_font();
        mas = new MusicAndSounds();
        mas.playMainMenu();

		Gdx.input.setCatchBackKey(true);

		setScreen(new MainMenuScreen(this));
	}

	public void setAdListener(DuckPondGameAdStateListener adStateListener)
	{
		this.adStateListener = adStateListener;
	}

//	public void setResolutionListener(DuckPondGameResolutionListener resolutionListener)
//	{
//		this.resolutionListener = resolutionListener;
//	}

	@Override
	public void render () {
		super.render();
	}
}
