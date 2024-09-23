package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.alxtray.groundclimber.renderers.EnvironmentObjectVisitor;

public class Platform extends EnvironmentObject {
    protected final Body body;
    private final float height;
    private final float width;

    public Platform(World world, float x, float y, float height, float width) {
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

    @Override
    public void acceptRender(EnvironmentObjectVisitor visitor, SpriteBatch batch) {
        visitor.visitPlatform(this, batch);
    }

    public Body getBody() {
        return body;
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
