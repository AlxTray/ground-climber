package com.alx.groundclimber;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.alx.groundclimber.GroundClimber;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800, 480);
		config.useVsync(true);
		config.setTitle("Ground Climber");
		new Lwjgl3Application(new GroundClimber(), config);
	}
}
