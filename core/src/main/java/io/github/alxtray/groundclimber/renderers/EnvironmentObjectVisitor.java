package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.bodies.Platform;

public interface EnvironmentObjectVisitor {
    void visitPlatform(Platform platform, SpriteBatch batch);

}
