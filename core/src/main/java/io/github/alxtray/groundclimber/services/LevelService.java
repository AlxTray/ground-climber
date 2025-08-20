package io.github.alxtray.groundclimber.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.pojo.LevelData;
import io.github.alxtray.groundclimber.pojo.PlatformData;
import io.github.alxtray.groundclimber.utilities.Logger;

public class LevelService {
    private LevelData levelData;
    private GameMode gameMode;

    public void createLevelData(GameMode gameMode, String levelName) {
        this.gameMode = gameMode;

        Json json = new Json();
        if (gameMode == GameMode.NORMAL) {
            levelData = json.fromJson(
                LevelData.class,
                Gdx.files.internal("levels/" + levelName).readString());
        } else if (gameMode == GameMode.ENDLESS) {
            Logger.log(
                "GameScreen",
                "Empty levelData successfully created for ENDLESS mode",
                LogLevel.INFO);
            levelData = json.fromJson(LevelData.class, Gdx.files.internal("endless.json").readString());
        }
    }

    public ObjectIntMap<String> getBounds() {
        return levelData.bounds;
    }

    public ObjectIntMap<String> getPlayerSpawn() {
        return levelData.playerSpawn;
    }

    public ObjectFloatMap<String> getCameraStartPosition() {
        return levelData.cameraStartPosition;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Array<PlatformData> getPlatformsData() {
        return levelData.platformsData;
    }
}
