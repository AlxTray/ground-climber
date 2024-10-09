package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.alxtray.groundclimber.enums.ObjectStatus;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectListenerVisitor;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectRenderVisitor;

public abstract class EnvironmentObject {
    public abstract void acceptRender(EnvironmentObjectRenderVisitor visitor, SpriteBatch batch);
    public abstract ObjectStatus acceptContact(EnvironmentObjectListenerVisitor visitor, Player player);

}
