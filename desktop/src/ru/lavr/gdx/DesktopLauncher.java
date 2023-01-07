package ru.lavr.gdx;

import static ru.lavr.gdx.constants.Constant.RIGHT_EDGE;
import static ru.lavr.gdx.constants.Constant.UPPER_EDGE;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
//        Graphics.DisplayMode dm = Lwjgl3ApplicationConfiguration.getDisplayMode();
//        config.setWindowedMode(dm.width / 2, dm.height / 2);
        config.setWindowedMode(RIGHT_EDGE, UPPER_EDGE);
        new Lwjgl3Application(new Starter(), config);
    }
}
