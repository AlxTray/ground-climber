package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.renderers.EnvironmentObjectVisitor;

public abstract class EnvironmentObject {
    public abstract void acceptRender(EnvironmentObjectVisitor visitor, SpriteBatch batch);
}
