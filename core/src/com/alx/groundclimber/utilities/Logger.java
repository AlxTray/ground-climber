package com.alx.groundclimber.utilities;

import com.alx.groundclimber.enums.LogLevel;
import com.badlogic.gdx.Gdx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  
  private static final String LOG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
  private Logger() {
  }
  
  public static void log(String locationTag, String message, LogLevel level) {
    String tag = LocalDateTime.now().format(DateTimeFormatter.ofPattern(LOG_TIME_FORMAT)) + locationTag;
    switch (level) {
      case DEBUG:
        Gdx.app.debug(tag + " DEBUG", message);
        break;
      case INFO:
        Gdx.app.log(tag + " INFO", message);
        break; 
      case ERROR:
        Gdx.app.error(tag + " ERROR", message);
        break;
      default:
        throw new IllegalArgumentException("Unhandled log level: " + level);
    }
  }
  
}
