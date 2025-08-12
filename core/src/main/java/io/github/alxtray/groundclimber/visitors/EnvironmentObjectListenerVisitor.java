package io.github.alxtray.groundclimber.visitors;

import io.github.alxtray.groundclimber.bodies.platforms.CrackedPlatform;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.bodies.platforms.TeleportPlatform;

public interface EnvironmentObjectListenerVisitor {
    void visitCrackedPlatform(CrackedPlatform platform);
    void visitBouncyPlatform(Player player);
    void visitGravityPlatform(Player player);
    void visitTeleportPlatform(Player player, TeleportPlatform linkedPlatform);

}
