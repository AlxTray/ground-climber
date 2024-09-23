package io.github.alxtray.groundclimber.controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.level.LevelData;
import io.github.alxtray.groundclimber.utilities.EndlessPlatformGenerator;

public class ControllerManager {
    private final CameraController cameraController;
    private final PhysicsController physicsController;
    private final PlayerController playerController;
    private final EndlessPlatformGenerator platformGenerator;
    private final GameMode gameMode;

    public ControllerManager(GameMode gameMode, LevelData levelData) {
        this.gameMode = gameMode;
        cameraController = new CameraController(levelData.getCameraStartPosition(), levelData.getBounds());
        physicsController = new PhysicsController(levelData.getPlatformsData());
        playerController = new PlayerController(physicsController.getWorld(), levelData.getPlayerSpawn());
        platformGenerator = (gameMode == GameMode.ENDLESS) ? new EndlessPlatformGenerator() : null;
    }

    public void update(float delta) {
        playerController.update(delta);
        if (gameMode == GameMode.ENDLESS) {
            platformGenerator.checkAndGenerateBatch(
                physicsController.getWorld(),
                playerController.getPlayer(),
                physicsController.getEnvironmentObjects());
        }
        cameraController.update(delta, playerController.getPlayer(), gameMode);
        physicsController.step(delta);
    }

    public Player getPlayer() {
        return playerController.getPlayer();
    }

    public OrthographicCamera getCamera() {
        return cameraController.getCamera();
    }

    public ObjectIntMap<String> getBounds() {
        return cameraController.getBounds();
    }

    public World getWorld() {
        return physicsController.getWorld();
    }

    public Array<EnvironmentObject> getEnvironmentObjects() {
        return physicsController.getEnvironmentObjects();
    }

}
