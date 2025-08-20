package io.github.alxtray.groundclimber.pojo;

import com.badlogic.gdx.utils.*;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.enums.LogLevel;
import io.github.alxtray.groundclimber.utilities.Logger;
import text.formic.Stringf;

public class LevelData implements Json.Serializable {
    public final ObjectIntMap<String> bounds = new ObjectIntMap<>();
    public final ObjectIntMap<String> playerSpawn = new ObjectIntMap<>();
    public final ObjectFloatMap<String> cameraStartPosition = new ObjectFloatMap<>();
    public final Array<PlatformData> platformsData = new Array<>();

    @Override
    public void write(Json json) { // No need to be used at the moment. (Useful if level editor is made)
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        Logger.log(
            "LevelData",
            "Loading level data...",
            LogLevel.INFO);

        JsonValue boundsValue = jsonData.get("data").get("bounds");
        int[] boundsArray = boundsValue.asIntArray();
        bounds.put("left", boundsArray[0]);
        bounds.put("right", boundsArray[1]);
        bounds.put("bottom", boundsArray[2]);
        bounds.put("top", boundsArray[3]);

        JsonValue playerSpawnValue = jsonData.get("data").get("player_start_pos");
        int[] playerSpawnArray = playerSpawnValue.asIntArray();
        playerSpawn.put("x", playerSpawnArray[0]);
        playerSpawn.put("y", playerSpawnArray[1]);

        JsonValue cameraStartPosValue = jsonData.get("data").get("camera_start_pos");
        float[] cameraStartPositionArray = cameraStartPosValue.asFloatArray();
        cameraStartPosition.put("x", cameraStartPositionArray[0]);
        cameraStartPosition.put("y", cameraStartPositionArray[1]);

        JsonValue platformsValue = jsonData.get("objects").get("platforms");
        for (JsonValue platformData = platformsValue.child; platformData != null; platformData = platformData.next) {
            platformsData.add(new PlatformData(
                platformData.get("type").asString(),
                platformData.get("orientation").asString(),
                platformData.get("x").asFloat(),
                platformData.get("y").asFloat(),
                platformData.get("height").asFloat(),
                platformData.get("width").asFloat()));
        }

        Logger.log(
            "LevelData",
            Stringf.format("Data for level %s loaded successfully", jsonData.get("data").get("name").asString()),
            LogLevel.INFO);
    }

}
