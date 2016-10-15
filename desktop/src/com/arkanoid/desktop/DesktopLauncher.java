package com.arkanoid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.arkanoid.ArkanoidGame;
import com.badlogic.gdx.graphics.Color;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 512;
		config.height = 640;
		config.initialBackgroundColor = Color.BLACK;
		new LwjglApplication(new ArkanoidGame(), config);
	}
}
