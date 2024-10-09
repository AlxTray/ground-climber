package io.github.alxtray.groundclimber.visitors;

import io.github.alxtray.groundclimber.bodies.platforms.CrackedPlatform;
import io.github.alxtray.groundclimber.bodies.Player;

public interface EnvironmentObjectListenerVisitor {
    void visitCrackedPlatform(CrackedPlatform platform);
    void visitBouncyPlatform(Player player);
    void visitGravityPlatform(Player player);

}
