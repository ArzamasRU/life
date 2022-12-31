package ru.lavr.gdx;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(5);
//        Graphics.DisplayMode dm = Lwjgl3ApplicationConfiguration.getDisplayMode();
//        config.setWindowedMode(dm.width / 2, dm.height / 2);
        config.setWindowedMode(1280, 720);
        new Lwjgl3Application(new Starter(), config);
    }
}
