package com.fworg64.duckpond.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fworg64.duckpond.game.DuckPondGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 480 *2;
		config.width = 320 *2;
		new LwjglApplication(new DuckPondGame(), config);
	}
}
