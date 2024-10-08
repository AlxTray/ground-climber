package io.github.alxtray.groundclimber.listeners;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.github.alxtray.groundclimber.bodies.platforms.CrackedPlatform;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectListenerVisitor;

public class EnvironmentObjectListener implements EnvironmentObjectListenerVisitor {
    private static final Vector2 BOUNCE_LINEAR_IMPULSE = new Vector2(0, -20000f);

    @Override
    public void visitCrackedPlatform(CrackedPlatform platform) {
        platform.incrementCrackLevel();
    }

    @Override
    public void visitBouncyPlatform(Player player) {
        player.getBody().applyLinearImpulse(BOUNCE_LINEAR_IMPULSE, player.getBody().getWorldCenter(), true);
    }

    @Override
    public void visitGravityPlatform(Player player) {
        World world = player.getBody().getWorld();
        Vector2 currentGravity = world.getGravity();
        world.setGravity(new Vector2(currentGravity.x, -currentGravity.y));
        player.toggleUpsideDown();
    }

}
