package io.github.alxtray.groundclimber.services;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.GameMode;
import io.github.alxtray.groundclimber.level.LevelData;
import io.github.alxtray.groundclimber.utilities.EndlessPlatformGenerator;

public class ControllerService {
    private final CameraService cameraService;
    private final PhysicsService physicsService;
    private final PlayerService playerService;
    private final EndlessPlatformGenerator platformGenerator;
    private final GameMode gameMode;

    public ControllerService(GameMode gameMode, LevelData levelData) {
        this.gameMode = gameMode;
        cameraService = new CameraService(levelData.getCameraStartPosition(), levelData.getBounds());
        physicsService = new PhysicsService(levelData.getPlatformsData());
        playerService = new PlayerService(physicsService.getWorld(), levelData.getPlayerSpawn());
        platformGenerator = (gameMode == GameMode.ENDLESS) ? new EndlessPlatformGenerator() : null;
    }

    public void update(float delta) {
        playerService.update(delta);
        if (gameMode == GameMode.ENDLESS) {
            platformGenerator.checkAndGenerateBatch(
                physicsService.getWorld(),
                playerService.getPlayer(),
                physicsService.getEnvironmentObjects());
        }
        cameraService.update(delta, playerService.getPlayer(), gameMode);
        physicsService.step(delta);
    }

    public Player getPlayer() {
        return playerService.getPlayer();
    }

    public OrthographicCamera getCamera() {
        return cameraService.getCamera();
    }

    public ObjectIntMap<String> getBounds() {
        return cameraService.getBounds();
    }

    public World getWorld() {
        return physicsService.getWorld();
    }

    public Array<EnvironmentObject> getEnvironmentObjects() {
        return physicsService.getEnvironmentObjects();
    }

}
