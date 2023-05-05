package com.alx;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.shape.Rectangle;

public class ProceduralLevelFactory implements EntityFactory {

    private final int[][] startingPlatformCoords = {{0, 550}, {100, 550}, {150, 550}, {230, 550}, {300, 550}, {400, 550}, {450, 550}, {500, 550}, {650, 550}};

    private enum EntityType {
        PLATFORM, COIN, ENEMY, DEATH
    }

    public void spawnStartingPlatforms() {
        for (int[] coords : startingPlatformCoords) { 
            spawn("platform", coords[0], coords[1]);
        }
    }

    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
            .type(EntityType.PLATFORM)
            .viewWithBBox(new Rectangle(50, 50))
            .build();
    }
}
