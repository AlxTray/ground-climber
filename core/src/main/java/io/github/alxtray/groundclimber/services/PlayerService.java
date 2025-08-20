package io.github.alxtray.groundclimber.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectIntMap;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.InputAction;

public class PlayerService {
    private static final int INITIAL_RADIUS = 16;
    private static final int MAX_VELOCITY = 240;
    private static final float Y_INITIAL_VELOCITY = -150f;
    private static final float DEFAULT_FORCE = 1500000;

    private final Player player;

    private LevelService levelService;
    private PhysicsService physicsService;

    public PlayerService(LevelService levelService, PhysicsService physicsService) {
        player = new Player(
            physicsService.getWorld(),
            levelService.getPlayerSpawn().get("x", 0),
            levelService.getPlayerSpawn().get("y", 0), INITIAL_RADIUS);
        player.getBody().setLinearVelocity(0, Y_INITIAL_VELOCITY);
    }

    public void handlePlayerAction(float delta) {
        Body playerBody = player.getBody();
        Vector2 velocity = playerBody.getLinearVelocity();
        if (Gdx.input.isKeyPressed(Input.Keys.A) && velocity.x > -MAX_VELOCITY) {
            playerBody.applyForceToCenter(-DEFAULT_FORCE * delta, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && velocity.x < MAX_VELOCITY) {
            playerBody.applyForceToCenter(DEFAULT_FORCE * delta, 0, true);
        }
    }

    public Player getPlayer() {
        return player;
    }

}
