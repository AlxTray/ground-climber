package io.github.alxtray.groundclimber.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    private static final float PLAYER_DENSITY = 0.1f;
    private static final float PLAYER_FRICTION = 0.3f;
    private static final float PLAYER_RESTITUTION = 1f;
    private final Body body;
    private boolean upsideDown = false;

    public Player(World world, int x, int y, int radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape playerShape = new CircleShape();
        playerShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
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

    public boolean isUpsideDown() {
        return upsideDown;
    }

    public void toggleUpsideDown() {
        this.upsideDown = !upsideDown;
    }

}
