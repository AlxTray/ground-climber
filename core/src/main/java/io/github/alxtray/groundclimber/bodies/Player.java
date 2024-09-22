package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    private static final float PLAYER_DENSITY = 0.1f;
    private static final float PLAYER_FRICTION = 0.3f;
    private static final float PLAYER_RESTITUTION = 1f;
    private final Body body;

    public Player(final World world, final int x, final int y, final int radius) {

        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        body.setUserData(this);

        final CircleShape playerShape = new CircleShape();
        playerShape.setRadius(radius);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.density = PLAYER_DENSITY;
        fixtureDef.friction = PLAYER_FRICTION;
        fixtureDef.restitution = PLAYER_RESTITUTION;
        body.createFixture(fixtureDef);

        playerShape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

}
