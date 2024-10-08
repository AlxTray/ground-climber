package io.github.alxtray.groundclimber.bodies.platforms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.alxtray.groundclimber.bodies.EnvironmentObject;
import io.github.alxtray.groundclimber.bodies.Player;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import io.github.alxtray.groundclimber.enums.PlatformStatus;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectListenerVisitor;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectRenderVisitor;

public class Platform extends EnvironmentObject {
    protected final Body body;
    private final PlatformOrientation orientation;
    private final float height;
    private final float width;

    public Platform(World world, PlatformOrientation orientation, float x, float y, float height, float width) {
        this.orientation = orientation;
        this.height = height;
        this.width = width;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Box2D places based on the centre so needs to be adjusted for LibGDX which places based on bottom-left corner
        bodyDef.position.set(x + (width / 2), y + (height / 2));
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public Texture getOverlayTexture() {
        return null;
    }

    @Override
    public void acceptRender(EnvironmentObjectRenderVisitor visitor, SpriteBatch batch) {
        visitor.visitPlatform(this, batch);
    }

    @Override
    public PlatformStatus acceptContact(EnvironmentObjectListenerVisitor visitor, Player player) {
        return PlatformStatus.NoChange;
    }

    public Body getBody() {
        return body;
    }

    public PlatformOrientation getOrientation() {
        return orientation;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

}
