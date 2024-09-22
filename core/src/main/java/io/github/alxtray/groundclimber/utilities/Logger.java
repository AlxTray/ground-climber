package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.Gdx;
import io.github.alxtray.groundclimber.enums.LogLevel;

public class Logger {
    private Logger() {
    }

    public static void log(String locationTag, String message, LogLevel level) {
        switch (level) {
            case DEBUG:
                Gdx.app.debug(locationTag + " DEBUG", message);
                break;
            case INFO:
                Gdx.app.log(locationTag + " INFO", message);
                break;
            case ERROR:
                Gdx.app.error(locationTag + " ERROR", message);
                break;
            default:
                throw new IllegalArgumentException("Unhandled log level: " + level);
        }
    }

}
