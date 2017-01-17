package com.udacity.game_dev.outbreak.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.udacity.game_dev.outbreak.Utilities.Constants;
import com.udacity.game_dev.outbreak.OutbreakGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Constants.WORLD_WIDTH;
		config.height = (int) Constants.WORLD_HEIGHT;
		new LwjglApplication(new OutbreakGame(), config);
	}
}
