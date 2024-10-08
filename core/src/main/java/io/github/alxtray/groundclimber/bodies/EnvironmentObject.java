package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.enums.PlatformStatus;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectListenerVisitor;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectRenderVisitor;

public abstract class EnvironmentObject {
    public abstract void acceptRender(EnvironmentObjectRenderVisitor visitor, SpriteBatch batch);
    public abstract PlatformStatus acceptContact(EnvironmentObjectListenerVisitor visitor, Player player);

}
