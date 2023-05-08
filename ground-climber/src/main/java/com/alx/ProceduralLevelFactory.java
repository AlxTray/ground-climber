package com.alx;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.scene.shape.Rectangle;

public class ProceduralLevelFactory implements EntityFactory {

    private final int[][] startingPlatformCoords = {{0, 50, 150}, {100, 100, 75}, {230, 50, 50}, {300, 50, 50}, {400, 150, 30}, {650, 110, 80}};

    private enum EntityType {
        PLAYER, PLATFORM, COIN, ENEMY, DEATH
    }

    public void spawnStartingPlatforms() {
        for (int[] coords : startingPlatformCoords) {
            set("currentPlatformWidth", coords[1]);
            set("currentPlatformHeight", coords[2]);
            getGameWorld().spawn("platform", coords[0], 600 - geti("currentPlatformHeight"));
        }
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
            .type(EntityType.PLATFORM)
            .viewWithBBox(new Rectangle(geti("currentPlatformWidth"), geti("currentPlatformHeight")))
            .with(new PhysicsComponent())
            .build();
    }
}
