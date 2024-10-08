package io.github.alxtray.groundclimber.visitors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.bodies.Platform;

public interface EnvironmentObjectRenderVisitor {
    void visitPlatform(Platform platform, SpriteBatch batch);

}
